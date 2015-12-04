import java.util.ArrayList;
import java.util.List;

import trees.RegressionTree;
import trees.attributes.Attribute;
import trees.visualization.VisualTree;

public class Main {

	static List<RegressionTree> population;

	public static void main(String[] args) {
		// Execution parameters
		int numExecutions = 1;
		int populationSize = 10; 
		int treeDepth = 6;
		double initialMutation = 1;
		int selectivePressure = 5;
		int stopCriteria = 20;

		// Dataset
		List<Attribute> allAttributes = Parser.getAllAttributes();
		double[][] allData = Parser.getDataFromFile("age_pr2_without.csv");
		double[][] dataTrain = Parser.getNSamples(600, allData);
		/*
		 * TODO modifyAttributes
		 */


		// Executions
		List<RegressionTree> bests = new ArrayList<RegressionTree>();
		System.out.println("------------------ TEST ---------------------");
		System.out.println("Population size = " + populationSize);
		System.out.println("Trees depth = " + treeDepth);
		System.out.println("Initial mutation percentage = " + initialMutation);
		System.out.println("Selective pressure = " + selectivePressure);
		System.out.println("Stop criteria = " + stopCriteria);
		System.out.println("---------------------------------------------");

		for (int i = 0; i < numExecutions; i++) {
			System.out.println("--------- EXECUTION NUMBER " + i + " ---------");
			// Initial population
			population = new ArrayList<RegressionTree>();
			for (int j = 0; j < populationSize; j++) {
				population.add(new RegressionTree(allAttributes, treeDepth));
			}
			bests.add(execute(allAttributes, dataTrain, populationSize, treeDepth, initialMutation, selectivePressure, stopCriteria));		
		}

		// Test of the best trees
		int i = 0;
		for (RegressionTree tree : bests) {
			double fitness = tree.getEvaluation(allData);
			VisualTree treeV = new VisualTree("Tree " + i + " | " + fitness, tree);
			treeV.printTree();
			i ++;
		}

	}

	private static RegressionTree execute(List<Attribute> attributes, double[][] dataTrain, int populationSize, int treeDepth, double initialMutation,int selectivePressure, int stopCriteria) {
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
				System.out.println(tree.getEvaluation(dataTrain));
			}
			System.out.println("Best individual : " + fittest.get(0).getEvaluation(dataTrain));
			mutationPercentage = (initialMutation/2) * (1 + Math.tanh((1.5*populationSize - i)/(populationSize/3)));
			computeIteration(mutationPercentage, selectivePressure, dataTrain);
			i++;
		} while (counter < stopCriteria);
		fittest = GeneticProgramming.getFittest(population,1, dataTrain);
		System.out.println("Best of all iterations : " + fittest.get(0).getEvaluation(dataTrain));
		System.out.println("Number of iterations : " + i);
		return fittest.get(0);

	}

	private static void computeIteration(double mutationPercentage, double selectivePressure, double[][] data) {
		List<Integer> fittestIndexes = GeneticProgramming.selection(population, selectivePressure, data);
		List<RegressionTree> children = GeneticProgramming.combination(population, fittestIndexes);
		GeneticProgramming.mutation(children, mutationPercentage);
		population = GeneticProgramming.replacement(population, children, data);
	}

}
