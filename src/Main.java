import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import trees.RegressionTree;
import trees.attributes.Attribute;
import trees.visualization.VisualTree;

public class Main {

	static List<RegressionTree> population;

	public static void main(String[] args) throws ParseException {
		// Execution parameters
		int numExecutions = 5;
		int populationSize = 50; 
		int treeDepth = 5;
		double initialMutation = 0.33;
		int selectivePressure = 3;
		int stopCriteria = 50;

		// Dataset
		HashMap<Integer, Attribute> allAttributes = Parser.getAllAttributes();
		
		/*
		 * Quit the attributes that aren't correctly codified
		
		allAttributes.remove(1);
		allAttributes.remove(2);
		allAttributes.remove(3);
		allAttributes.remove(4);
		allAttributes.remove(5);
		allAttributes.remove(6);
		allAttributes.remove(7);
		allAttributes.remove(8);
		allAttributes.remove(10);
		allAttributes.remove(13);
		allAttributes.remove(14);*/
		
		double[][] allData = Parser.getDataFromFile("age_pr2_without.csv");
		//double[][] dataTrain = allData;
		double[][] dataTrain = Parser.getNSamples(6000, allData);
		/*
		 * TODO modifyAttributes
		 */


		// Executions
		List<RegressionTree> bests = new ArrayList<RegressionTree>();
		System.out.println("------------------ TEST ---------------------");
		System.out.println("Population size = " + populationSize);
		System.out.println("Trees depth = " + treeDepth);
		//System.out.println("Trees depth = " + (allAttributes.size()+1));
		System.out.println("Initial mutation percentage = " + initialMutation);
		System.out.println("Selective pressure = " + selectivePressure);
		System.out.println("Stop criteria = " + stopCriteria);
		System.out.println("---------------------------------------------");

		for (int i = 0; i < numExecutions; i++) {
			System.out.println("--------- EXECUTION NUMBER " + i + " ---------");
			// Initial population
			//System.out.println("Début initialisation pop");
			population = new ArrayList<RegressionTree>();
			for (int j = 0; j < populationSize; j++) {
				//System.out.println("Début initialisation arbre numéro " + (j+1));
				population.add(new RegressionTree(allAttributes, treeDepth));
				//population.add(new RegressionTree(allAttributes));
				//System.out.println("Fin initialisation arbre numéro " + (j+1));
			}
			//System.out.println("Fin initialisation pop");
			bests.add(execute(allAttributes, dataTrain, populationSize, initialMutation, selectivePressure, stopCriteria));		
		}

		// Test of the best trees
		int i = 0;
		for (RegressionTree tree : bests) {
			tree.reinitializeFitness();
			double fitness = tree.getEvaluation(allData);
			VisualTree treeV = new VisualTree("Tree " + i + " | " + fitness, tree);
			treeV.printTree();
			i ++;
		}

	}

	private static RegressionTree execute(HashMap<Integer,Attribute> allAttributes, double[][] dataTrain, int populationSize, double initialMutation,int selectivePressure, int stopCriteria) throws ParseException {
		List<RegressionTree> fittest;
		int i = 0;
		double mutationPercentage;
		double bestSoFar = Double.NEGATIVE_INFINITY;
		int counter = 0;


		// Execution
		do {
			System.out.println("--------- ITERATION NUMBER " + i + " ---------");
			fittest = GeneticProgramming.getFittest(population,1, dataTrain);
			if (fittest.get(0).getEvaluation(dataTrain) > bestSoFar) {
				bestSoFar = fittest.get(0).getEvaluation(dataTrain);
				counter = 0;
			} else {
				counter ++;
			}
			
			for (RegressionTree tree : population) {
				/*
				VisualTree treeV = new VisualTree("Tree", tree);
				treeV.printTree();*/
				System.out.println(tree.getEvaluation(dataTrain));
			}
			
			System.out.println("Best individual : " + fittest.get(0).getEvaluation(dataTrain));
			mutationPercentage = initialMutation;
			//mutationPercentage = (initialMutation/2) * (1 + Math.tanh((1.5*populationSize - i)/(populationSize/3)));
			computeIteration(mutationPercentage, selectivePressure, dataTrain);
			i++;
		} while (counter < stopCriteria);
		fittest = GeneticProgramming.getFittest(population,1, dataTrain);
		System.out.println("Best of all iterations : " + fittest.get(0).getEvaluation(dataTrain));
		System.out.println("Number of iterations : " + i);
		return fittest.get(0);

	}

	private static void computeIteration(double mutationPercentage, double selectivePressure, double[][] data) throws ParseException {
		List<Integer> fittestIndexes = GeneticProgramming.selection(population, selectivePressure, data);
		/*
		List<RegressionTree> children = GeneticProgramming.combination(population, fittestIndexes);
		*/
		List<RegressionTree> children = new ArrayList<RegressionTree>();
		for (Integer selected : fittestIndexes) {
			children.add(population.get(selected).copy());
		}
		GeneticProgramming.mutation(children, mutationPercentage);
		
		for (RegressionTree child : children) {
			/*
			VisualTree treeC = new VisualTree("Child", child);
			treeC.printTree();*/
			System.out.println(child.getEvaluation(data));
		}
		population = GeneticProgramming.replacement(population, children, data);
	}

}
