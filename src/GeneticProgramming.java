import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import trees.Node;
import trees.RegressionTree;
import trees.attributes.Attribute;
import trees.attributes.DateAttribute;
import trees.attributes.NumericalAttribute;
import trees.attributes.Attribute.AttributeType;

public class GeneticProgramming {

	/**
	 * Combine a population of regression trees to obtain a new population of
	 * the same size The combination is made by randomly selecting a node in
	 * each tree of the couple and swapping them
	 * 
	 * @param trees
	 *            All the population of trees
	 * @param fittest
	 *            Indices of the trees in the mating pool
	 * @return List of all the children get by combination of the trees in the
	 *         mating pool
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
			children.addAll(combination(father, mother));
		}
		return children;
	}

	/*
	 * Combine two regression trees by swapping two nodes randomly selected
	 * 
	 * @param father The first regression tree
	 * 
	 * @param mother The second regression tree
	 * 
	 * @return A list containing the two children created by the swapping
	 */
	private static List<RegressionTree> combination(RegressionTree father, RegressionTree mother) {
		List<RegressionTree> descendents = new ArrayList<RegressionTree>();
		RegressionTree des1, des2;
		int index1 = -1;
		int index2 = -1;
		Random r = new Random();

		// Copy the original trees
		des1 = father.copy();
		des2 = mother.copy();

		// Randomly select a node in the first tree
		List<Node> nodesDes1 = des1.getAllNodes();
		List<Node> nodesDes2 = des2.getAllNodes();
		Node node1, node2;
		do {
			node1 = nodesDes1.get(r.nextInt(nodesDes1.size()));
			node2 = nodesDes2.get(r.nextInt(nodesDes2.size()));
		} while (node2.getHeigthSubTree() > father.getDepth() - node1.getDepth() + 1
				|| node1.getHeigthSubTree() > father.getDepth() - node2.getDepth() + 1);

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
	 * 
	 * @param population
	 *            List of the initial regression trees
	 * @param selectivePressure
	 *            Selective pressure
	 * @param data
	 *            Data to compute the evaluation of each one of the trees
	 * @return Indexes of the selected regression trees
	 */
	public static List<Integer> selection(List<RegressionTree> population, double selectivePressure, double[][] data) {
		return roulette(fitnessAssignmentFromRank(population, selectivePressure, data));
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
			probas[i] = probas[i - 1] + fitness[i] / fitnessTotal;
		}
		probas[populationSize - 1] = 1;

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
			fitness[i] = selectivePressure * (ranks[i] - 1) / (populationSize - 1);
		}
		return fitness;
	}

	private static double[] fitnessAssignmentFromRank(List<RegressionTree> individuals, double selectivePressure,
			double[][] data) {
		int populationSize = individuals.size();
		double[] evaluation = new double[populationSize];
		double[] fitness = new double[populationSize];

		// Fitness assignment from ranking
		for (int i = 0; i < populationSize; i++) {
			evaluation[i] = individuals.get(i).getEvaluation(data);
		}
		fitness = GeneticProgramming.getFitnessFromRank(selectivePressure,
				GeneticProgramming.getRankFromEvaluation(evaluation));
		return fitness;
	}

	private static double[] proportionalFitnessAssignment(List<RegressionTree> individuals, double[][] data) {
		int populationSize = individuals.size();
		double[] fitness = new double[populationSize];
		for (int i = 0; i < populationSize; i++) {
			fitness[i] = (-1) * individuals.get(i).getEvaluation(data);
		}
		return fitness;
	}

	/**
	 * Mutation phase
	 * 
	 * @param trees
	 *            Initial population to mutate
	 * @param mutationProbability
	 *            Mutation probability
	 */
	public static void mutation(List<RegressionTree> trees, double mutationProbability) throws ParseException {
		/*
		 TODO choose the mutation's type
		 */
		for (RegressionTree tree : trees) {
			// SubTreeMutation
			subTreeMutation(tree, mutationProbability);
			// LeafMutation
			leafMutation(tree, mutationProbability);
			// NodeSplitValueMutation
			nodeSplitValueMutation(tree, mutationProbability);
			tree.reinitializeFitness();
		}
	}
	
	/*
	 * For mutationProbability% of nodes with an attribute of type DATE or NUMERICAL
	 * Change the split value thanks to a gaussian law
	 */
	private static void nodeSplitValueMutation(RegressionTree tree, double mutationProbability) {
		List<Node> nodes = tree.getAllNodes();
		Random r = new Random ();
		double randDouble;
		double gaussianDouble;
		double min, max;
		for (Node node : nodes) {
			Attribute attr = node.getAttribute();
			if (!node.isLeaf() && attr.getType() != AttributeType.NOMINAL) {
				randDouble = r.nextDouble();
				
				// if mutation needed
				if (randDouble <= mutationProbability) {
					
					// Get the range
					if (attr.getType() == AttributeType.DATE) {
						DateAttribute dateAttr = (DateAttribute)attr;
						min = dateAttr.earliest;
						max = dateAttr.latest;
					} else {
						NumericalAttribute numAttr = (NumericalAttribute)attr;
						min = numAttr.getMin();
						max = numAttr.getMax();
					}
					
					// Generate the new split value
					gaussianDouble = r.nextGaussian();
					gaussianDouble = gaussianDouble * (max - min) / 10 + node.getSplitValue();
					
					// Check that the new split value is not out of range
					if (gaussianDouble < min) {
						gaussianDouble = min;
					}
					if (gaussianDouble > max) {
						gaussianDouble = max;
					}
					node.setSplitValue(gaussianDouble);
				}
			}
		}
	}
	
	/*
	 * For mutationProbability% of leaf nodes 
	 * Change the output value thanks to a gaussian law
	 */
	private static void leafMutation(RegressionTree tree, double mutationProbability) {
		List<Node> nodes = tree.getAllNodes();
		Random r = new Random ();
		double randDouble;
		double gaussianDouble;
		for (Node node : nodes) {
			if (node.isLeaf()) {
				randDouble = r.nextDouble();
				// if mutation needed
				if (randDouble <= mutationProbability) {
					// Generate the new output value
					gaussianDouble = r.nextGaussian();
					gaussianDouble = gaussianDouble * Node.MAX_SALES/10 + node.getOutput();
					// Check that the new output value is not out of range
					if (gaussianDouble < 0) {
						gaussianDouble = 0;
					}
					node.setOutput(gaussianDouble);
				}
			}
		}
	}

	/*
	 * Scan all the nodes.
	 * The mutation probability of the node is a linear function of the depth between rootMutationProbabily and leafMutationProbability
	 * Only mutate a unique node in the tree (or none)
	 */
	private static void subTreeMutation(RegressionTree tree, double rootMutationProbability) throws ParseException {
		// The probability of mutation increase with the depth of the nodes
		double leafMutationProbability = 3 * rootMutationProbability;
		List<Node> nodes = tree.getAllNodes();
		boolean mutation = false;
		Random r = new Random();
		int randInt;
		double randDouble;
		int maxDepth = tree.getDepth();
		// While no mutation yet and still nodes to check
		while (!mutation && !nodes.isEmpty()) {
			// Select a random node
			randInt = r.nextInt(nodes.size());
			Node node = nodes.get(randInt);
			// The mutation probability of the node is a linear function of the depth between rootMutationProbabily and leafMutationProbability
			double mutationProba = ((leafMutationProbability - rootMutationProbability) * node.getDepth()
					+ rootMutationProbability * maxDepth - leafMutationProbability) / (maxDepth - 1);
			randDouble = r.nextDouble();
			// Mutation needed
			if (randDouble <= mutationProba) {
				RegressionTree newSubTree;
				// If the node is root, generate a complete tree
				if (node.isRoot()) {
					tree.setRoot(new RegressionTree(tree.getAttributes(), maxDepth).getRoot());
				// If not, generate a sub tree and link it to the parent node
				} else {
					Node parentNode = node.getParent();
					List<Attribute> parentAttributes = parentNode.getAllowedAttributes();
					HashMap<Integer,Attribute> newAttributes = new HashMap<Integer,Attribute>();
					for (Attribute attr : parentAttributes) {
						newAttributes.put(attr.getId(),attr);
						newAttributes.remove(parentNode.getAttribute());
					}
					newSubTree = new RegressionTree(newAttributes, maxDepth - node.getDepth() + 1);
					int index = parentNode.removeChild(node);
					node = newSubTree.getRoot();
					parentNode.addChild(node, index);
					node.setDepth();
				}
				// If a mutation is done, break
				mutation = true;
			} else {
				// Don't check this node anymore
				nodes.remove(randInt);
			}
		}
	}
	
	public static List<RegressionTree> replacement(List<RegressionTree> initialPopulation, List<RegressionTree> children, double[][] data) {
		//return inclusionReplacement(initialPopulation, children, data);
		return elitistReplacement(initialPopulation, children, data, 0.2);
	}
	
	private static List<RegressionTree> elitistReplacement(List<RegressionTree> initialPopulation, List<RegressionTree> children, double[][] data, double proportion) {
		List<RegressionTree> newPopulation = new ArrayList<RegressionTree>();
		int parentsToKeep = (int)Math.round(proportion*initialPopulation.size());
		newPopulation.addAll(GeneticProgramming.getFittest(initialPopulation, parentsToKeep, data));
		newPopulation.addAll(GeneticProgramming.getFittest(children, initialPopulation.size()-parentsToKeep, data));
		return newPopulation;
	}
	
	private static List<RegressionTree> inclusionReplacement(List<RegressionTree> initialPopulation, List<RegressionTree> children, double[][] data) {
		List<RegressionTree> newPopulation;
		int populationSize = initialPopulation.size();
		children.addAll(initialPopulation);
		newPopulation = GeneticProgramming.getFittest(children, populationSize, data);
		return newPopulation;
	}

	public static List<RegressionTree> getFittest(List<RegressionTree> population, int number, double[][] data) {
		List<RegressionTree> fittest = new ArrayList<RegressionTree>();
		int populationSize = population.size();
		int[] ranks;
		double[] evaluation = new double[populationSize];
		int index = populationSize;

		for (int i = 0; i < populationSize; i++) {
			evaluation[i] = population.get(i).getEvaluation(data);
		}
		ranks = GeneticProgramming.getRankFromEvaluation(evaluation);
		
		while (fittest.size() < number && index > populationSize - number) {
			for (int i = 0; i < populationSize; i++) {
				if (ranks[i]  == index) {
					fittest.add(population.get(i));
				}
				if (fittest.size() == number) {
					break;
				}
			}
			index--;
		}
		return fittest;
	}
}
