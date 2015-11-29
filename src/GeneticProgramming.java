import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import trees.Node;
import trees.RegressionTree;

public class GeneticProgramming {
	
	/**
	 * Combine a population of regression trees to obtain a new population of the same size
	 * The combination is made by randomly selecting a node in each tree of the couple and swapping them
	 * @param trees 
	 * 		All the population of trees
	 * @param fittest 
	 *		Indices of the trees in the mating pool
	 * @return 
	 * 		List of all the children get by combination of the trees in the mating pool
	 */
	public static List<RegressionTree> combination(List<RegressionTree> trees, List<Integer> fittest) {
		RegressionTree father, mother;
		List<RegressionTree> children = new ArrayList<RegressionTree>();
		int popSize = fittest.size();
		Random r = new Random();
		int indice;
		
		// Do the mating
		for (int i = 0; i < popSize / 2; i++) {
			// Select two trees and delete them from the list
			indice = r.nextInt(fittest.size());
			father = trees.get(fittest.remove(indice));
			indice = r.nextInt(fittest.size());
			mother = trees.get(fittest.remove(indice));
			// Combine the two trees to get to children
			children.addAll(combination(father,mother));
		}
		return children;
	}
	
	
	/*
	 * Combine two regression trees by swapping two nodes randomly selected
	 * @param father The first regression tree
	 * @param mother The second regression tree
	 * @return A list containing the two children created by the swapping
	 */
	public static List<RegressionTree> combination(RegressionTree father, RegressionTree mother) {
		/*
		 TODO change to private 
		 */
		List<RegressionTree> descendents = new ArrayList<RegressionTree>();
		RegressionTree des1, des2;
		int index1 = -1;
		int index2 = -1;
		Random r = new Random();
		
		// Copy the original trees
		des1 = father.copy();
		des2 = mother.copy();
		
		// Randomly select a node in each tree
		List<Node> nodesDes1 = des1.getAllNodes();
		Node node1 = nodesDes1.get(r.nextInt(nodesDes1.size()));
		
		// Control the selection of the second node
		List<Node> nodesDes2 = des2.getAllNodes();
		Node node2;
		do {
			node2 = nodesDes2.get(r.nextInt(nodesDes2.size()));
		} while (node2.getHeigthSubTree() > father.getDepth() - node1.getDepth() + 1);
		
		
		Node parentNode1 = null;
		Node parentNode2 = null;
		// Swap the two selected nodes
		if (!node1.isRoot()) {
			parentNode1 = node1.getParent();
			index1 = parentNode1.removeChild(node1);
		}
		if (!node2.isRoot()) {
			parentNode2 = node2.getParent();
			index2 = parentNode2.removeChild(node2);
			if (parentNode1 != null) {
				parentNode1.addChild(node2, index1);
			} else {
				des1.setRoot(node2);
			}
			parentNode2.addChild(node1, index2);
		} else {
			des2.setRoot(node1);
			if (parentNode1 != null) {
				parentNode1.addChild(node2, index1);
			} else {
				des1.setRoot(node2);
			}
		}
		// Update the depth of the nodes
		node1.setDepth();
		node2.setDepth();
		
		descendents.add(des1);
		descendents.add(des2);
		return descendents;
	}
	
	/**
	 * Select the fittest thanks to roulette method
	 * @param population 
	 * 		List of the initial regression trees
	 * @param selectivePressure 
	 * 		Selective pressure
	 * @param data 
	 * 		Data to compute the evaluation of each one of the trees
	 * @return 
	 * 		Indexes of the selected regression trees
	 */
	public static List<Integer> selection(List<RegressionTree> population, double selectivePressure, double[][] data){
		return roulette(fitnessAssignmentFromRank(population,selectivePressure, data));		
	}
	
	private static List<Integer> roulette(double[] fitness) {
		int populationSize = fitness.length;
		List<Integer> fittestIndexes = new ArrayList<Integer>(0);
		
		// Compute the sum of the fitness
		double fitnessTotal = 0;
		for (int i = 0; i < populationSize; i++) {
			fitnessTotal += fitness[i];
		}
		
		// Compute accumulated probabilities
		double[] probas = new double[populationSize];
		probas[0] = fitness[0] / fitnessTotal;
		for (int i = 1; i < populationSize - 1; i++) {
			probas[i] = probas[i-1] + fitness[i] / fitnessTotal;
		}
		probas[populationSize-1] = 1;
		
		// Select the fittest individuals
		Random r = new Random();
		double randDouble;
		for (int i = 0; i < populationSize; i++) {
			randDouble = r.nextDouble();
			for (int j = 0; j < populationSize; j++) {
				if (randDouble < probas[j]) {
					fittestIndexes.add(j);
					break;
				}
			}			
		}
		return fittestIndexes;
	}
	
	private static int[] getRankFromEvaluation(double[] evaluation) {
		int populationSize = evaluation.length;
		int[] ranks = new int[populationSize];
		
		// Compute the rank of the individuals
		for (int i = 0; i < populationSize; i++) {
			ranks[i] = 0;
			for (int j = 0; j < populationSize; j++) {
				if (i != j && evaluation[i] >= evaluation[j]) {
					ranks[i]++;
				}
			}
			ranks[i]++;
		}
		return ranks;
	}
 	
	private static double[] getFitnessFromRank(double selectivePressure, int[] ranks) {
		int populationSize = ranks.length;
		double[] fitness = new double[populationSize];
		
		// Compute the fitness
		for (int i = 0; i < populationSize; i++) {
			fitness[i] = 2-selectivePressure + 2*(selectivePressure-1)*(ranks[i]-1)/(populationSize-1);
		}
		return fitness;
	}
	
	private static double[] fitnessAssignmentFromRank(List<RegressionTree> individuals, double selectivePressure, double[][] data) {
		int populationSize = individuals.size();
		double[] evaluation = new double[populationSize];
		double[] fitness = new double[populationSize];

		// Fitness assignment from ranking
		for (int i = 0; i < populationSize; i++) {
			evaluation[i] = (-1)*individuals.get(i).getEvaluation(data);
		}
		fitness = GeneticProgramming.getFitnessFromRank(selectivePressure, GeneticProgramming.getRankFromEvaluation(evaluation));
		return fitness;
	}
	
	private static double[] proportionalFitnessAssignment(List<RegressionTree> individuals, double[][] data) {
		int populationSize = individuals.size();
		double[] fitness = new double[populationSize];
		for (int i = 0; i < populationSize; i++) {
			fitness[i] = (-1)*individuals.get(i).getEvaluation(data);
		}
		return fitness;		
	}
	
	/**
	 * Mutation phase
	 * 
	 * @param trees
	 * 		Initial population to mutate
	 * @param mutationProbability
	 * 		Mutation probability
	 * @return
	 * 		A list representing the new population after the mutation phase
	 */
	public static List<RegressionTree> mutation(List<RegressionTree> trees, double mutationProbability){
		List<RegressionTree> populationAfterMutation = new ArrayList<RegressionTree>();
		Random r = new Random();
		double randDouble;
		
		for (RegressionTree tree : trees){
			// Obtenir tous les noeuds et g�n�rer nombre al�atoire pour chacun ?
			// Muter si ce nombre est inf�rieur � mutationProbability (valeur de mutation differente selon le type de mutation ?
			randDouble = r.nextDouble();
		}
		
		return populationAfterMutation;
	}
}
