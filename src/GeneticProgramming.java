import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import trees.Node;
import trees.RegressionTree;

public class GeneticProgramming {
	
	/**
	 * Combine a population of regression trees to obtain a new population of the same size
	 * The combination is made by randomly selecting a node in each tree of the couple and swapping them
	 * @param trees All the population of trees
	 * @param fitests The indices of the trees in the mating pool
	 * @return List of all the children get by combination of the trees in the mating pool
	 */
	public static List<RegressionTree> combination(List<RegressionTree> trees, List<Integer> fitests) {
		RegressionTree father, mother;
		List<RegressionTree> children = new ArrayList<RegressionTree>();
		int popSize = fitests.size();
		Random r = new Random();
		int indice;
		
		// Do the mating
		for (int i = 0; i < popSize / 2; i++) {
			// Select two trees and delete them from the list
			indice = r.nextInt(fitests.size());
			father = trees.get(fitests.remove(indice));
			indice = r.nextInt(fitests.size());
			mother = trees.get(fitests.remove(indice));
			// Combine the two trees to get to children
			children.addAll(combination(father,mother));
		}
		return children;
	}
	
	
	/**
	 * Combine two regression trees by swapping two nodes randomly selected
	 * @param father The first regression tree
	 * @param mother The second regression tree
	 * @return A list containing the two children created by the swapping
	 */
	public static List<RegressionTree> combination(RegressionTree father, RegressionTree mother) {
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
	
	public static List<Integer> selection(List<RegressionTree> population, double selectivePressure, double[][] data){
		return ruleta(asignacionFitnessPorRango(population,selectivePressure, data));		
	}
	
	public static List<Integer> ruleta(double[] fitness) {
		int tamano = fitness.length;
		List<Integer> indicesMejores = new ArrayList<Integer>(0);
		
		// Calcular el total del fitness
		double fitnessTotal = 0;
		for (int i = 0; i < tamano; i++) {
			fitnessTotal += fitness[i];
		}
		
		// Calcular las probabilidades acumuladas
		double[] probas = new double[tamano];
		probas[0] = fitness[0] / fitnessTotal;
		for (int i = 1; i < tamano - 1; i++) {
			probas[i] = probas[i-1] + fitness[i] / fitnessTotal;
			//System.out.println("Proba cumulï¿½e pour indiv " + i + " : " + probas[i]);
		}
		probas[tamano-1] = 1;
		
		// Selecionar los individuos
		Random r = new Random();
		double valor;
		for (int i = 0; i < tamano; i++) {
			valor = r.nextDouble();
			for (int j = 0; j < tamano; j++) {
				if (valor < probas[j]) {
					indicesMejores.add(j);
					break;
				}
			}			
		}
		
		return indicesMejores;
	}
	
	public static int[] getRangoConEvaluacion(double[] evaluacion) {
		int tamano = evaluacion.length;
		int[] ranks = new int[tamano];
		
		// Calcular el rango de los individuos
				for (int i = 0; i < tamano; i++) {
					ranks[i] = 0;
					for (int j = 0; j < tamano; j++) {
						if (i != j && evaluacion[i] >= evaluacion[j]) {
							ranks[i]++;
						}
					}
					ranks[i]++;
				}
		return ranks;
	}
 	
	public static double[] getFitnessConRango(double presionSelectiva, int[] ranks) {
		int tamano = ranks.length;
		double[] fitness = new double[tamano];
		
		// Calcular el fitness
		for (int i = 0; i < tamano; i++) {
			fitness[i] = 2-presionSelectiva + 2*(presionSelectiva-1)*(ranks[i]-1)/(tamano-1);
		}
		return fitness;
	}
	
	private static double[] asignacionFitnessPorRango(List<RegressionTree> individuos, double presionSelectiva, double[][] data) {
		int tamano = individuos.size();
		double[] evaluacion = new double[tamano];
		double[] fitness = new double[tamano];

		// Asignaciï¿½n del fitness por ranking
		for (int i = 0; i < tamano; i++) {
			evaluacion[i] = (-1)*individuos.get(i).getFitness(data);
		}
		fitness = GeneticProgramming.getFitnessConRango(presionSelectiva, GeneticProgramming.getRangoConEvaluacion(evaluacion));
		return fitness;
	}
	
	private static double[] asignacionFitnessProporcional(List<RegressionTree> individuos, double[][] data) {
		int tamano = individuos.size();
		double[] fitness = new double[tamano];
		for (int i = 0; i < tamano; i++) {
			fitness[i] = (-1)*individuos.get(i).getFitness(data);
		}
		return fitness;		
	}


	
	/**
	 * Mutation phase
	 * 
	 * @param trees
	 * 		Inicial population to mutate
	 * @param mutationProbability
	 * 		Mutation probability
	 * @return
	 * 		A list representing the new population after the mutation phase
	 */
	public static List<RegressionTree> mutation(List<RegressionTree> trees, double mutationProbability){
		List<RegressionTree> populationAfterMuntation = new ArrayList<RegressionTree>();
		Random r = new Random();
		double valor;
		
		for (RegressionTree tree : trees){
			// Obtenir tous les noeuds et générer nombre aléatoire pour chacun ?
			// Muter si ce nombre est inférieur à mutationProbability (valeur de mutation differente selon le type de mutation ?
			valor = r.nextDouble();
		}
		
		return populationAfterMuntation;
	}
}
