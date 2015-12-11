import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import trees.RegressionTree;
import trees.attributes.Attribute;
import trees.visualization.VisualTree;

public class Main {

	static List<RegressionTree> population;
	static List<Integer> numberIterations;

	public static void main(String[] args) throws ParseException {
		// Execution parameters
		int numExecutions = 5;
		int populationSize = 5; 
		int treeDepth = 10;
		double initialMutation = 0.33;
		int selectivePressure = 3;
		int stopCriteria = 5;
		int sizeTraining = 60000;
		

		// Dataset
		HashMap<Integer, Attribute> allAttributes = Parser.getAllAttributes();		
		double[][] allData = Parser.getDataFromFile("age_pr2_without.csv");
		int[] attributesToKeep = {1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1};
		double[][] dataFiltered = Parser.modifyListAttributes(allAttributes, attributesToKeep, allData);
		//double[][] dataTrain = Parser.getNSamples(sizeTraining, allData);
		double[][] dataTrain = Parser.getNSamples(sizeTraining, dataFiltered);

		// Executions
		List<RegressionTree> bests = new ArrayList<RegressionTree>();
		numberIterations = new ArrayList<Integer>();
		System.out.println("------------------ TEST ---------------------");
		System.out.println("Population size = " + populationSize);
		System.out.println("Trees depth = " + treeDepth);
		System.out.println("Initial mutation percentage = " + initialMutation);
		System.out.println("Selective pressure = " + selectivePressure);
		System.out.println("Stop criteria = " + stopCriteria);
		System.out.println("Number of instances for training = " + sizeTraining);
		System.out.println("---------------------------------------------");

		for (int i = 1; i <= numExecutions; i++) {
			System.out.println("--------- EXECUTION NUMBER " + i + " ---------");
			population = new ArrayList<RegressionTree>();
			for (int j = 0; j < populationSize; j++) {
				population.add(new RegressionTree(allAttributes, treeDepth));
			}
			bests.add(execute(allAttributes, dataTrain, populationSize, initialMutation, selectivePressure, stopCriteria));		
		}

		// Summary of the execution
		System.out.println("---------- SUMMARY OF THE EXECUTION --------------");
		// Test of the best trees
		int i = 1;
		for (RegressionTree tree : bests) {
			System.out.println("EXECUTION " + i + " :");
			System.out.println(numberIterations.get(i-1) + " iterations");
			System.out.println("Error on training for the best tree : " + tree.getEvaluation(dataTrain));
			tree.reinitializeFitness();
			double fitness = tree.getEvaluation(allData);
			System.out.println("Error on all data for the best tree : " + fitness);
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
			System.out.println("Best individual : " + fittest.get(0).getEvaluation(dataTrain));
			mutationPercentage = initialMutation;
			computeIteration(mutationPercentage, selectivePressure, dataTrain);
			i++;
		} while (counter < stopCriteria);
		fittest = GeneticProgramming.getFittest(population,1, dataTrain);
		System.out.println("Best of all iterations : " + fittest.get(0).getEvaluation(dataTrain));
		System.out.println("Number of iterations : " + i);
		numberIterations.add(i);
		return fittest.get(0);

	}

	private static void computeIteration(double mutationPercentage, double selectivePressure, double[][] data) throws ParseException {
		List<Integer> fittestIndexes = GeneticProgramming.selection(population, selectivePressure, data);
		List<RegressionTree> children = new ArrayList<RegressionTree>();
		for (Integer selected : fittestIndexes) {
			children.add(population.get(selected).copy());
		}
		GeneticProgramming.mutation(children, mutationPercentage);
		population = GeneticProgramming.replacement(population, children, data);
	}

}

