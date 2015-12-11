import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import trees.Node;
import trees.RegressionTree;
import trees.attributes.Attribute;
import trees.attributes.DateAttribute;
import trees.attributes.NominalAttribute;
import trees.attributes.NumericalAttribute;
import trees.visualization.VisualTree;

public class Test {

	public static void main(String[] args) {
		
		try {
			
			/*
			 * EXAMPLE : use of a RegressionTree
			 */
			
			/*
			 * First step : build the list of the available attributes
			 * Each attribute has an id that says what's its position in the data
			 */
			HashMap<Integer,Attribute> attributes = new HashMap<Integer,Attribute>();
			
			// The first available type is Date. The Date may correspond to a format supported by Java
			DateAttribute dateAttr = new DateAttribute("Date", Attribute.AttributeType.DATE, "yyyy-MM-dd", 0, true);
			attributes.put(0,dateAttr);
			
			// The second available type is Nominal. The attribute may take its values within a set of defined strings
			String[] nominalValues = {"OUI", "NON", "JE NE SAIS PAS"};
			attributes.put(1,new NominalAttribute("Attribut Nominal", Attribute.AttributeType.NOMINAL, nominalValues, 1, true));
			
			// The last type is Numeric. It corresponds to doubles. The attributes may take its values within a range specified with the constructor
			attributes.put(2,new NumericalAttribute("Attribut Numérique", Attribute.AttributeType.NUMERICAL, 2, -100, 100, true));
			
			/*
			 * A regression tree is randomly build using the list of attributes and a maximum depth 
			 */
			RegressionTree tree = new RegressionTree(attributes, 4);
			
			
			// Build a data that can be put in entry of the tree. The table should only contain doubles. (See attribute type's methods) 
			double[] data = {dateAttr.valueOf("1992-10-09"), 2, 0};
			
			
			// Visualize the tree in a new frame
			VisualTree treeV = new VisualTree("Regression tree", tree);
			treeV.printTree();
			
			/*
			// Printout the value predicted by the tree (value of the ultimate leaf reached) 
			System.out.println(tree.predict(data));
			
			
			// Test for the copy of the tree
			RegressionTree copy = tree.copy();
			VisualTree treeC = new VisualTree("Copy", copy);
			treeC.printTree();	
					
			// Test import data
			double[][] dataCSV = Parser.getDataFromFile("age_pr2_without.csv");
			HashMap<Integer,Attribute> l = Parser.getAllAttributes();
			*/
			// Test import data train and test
			double percentTrain = 0.7;
			int numberTrain = (int)(610328*percentTrain);
			int numberTest = 610328 - numberTrain;
			double[][] dataTrain = new double[numberTrain][16];
			double[][] dataTest = new double[numberTest][16];
			Parser.getDataFromFile("age_pr2_without.csv",0.7,dataTrain,dataTest);

			/*
			// Test choosing attributes
			int[] attributesToKeep = {1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1};
			dataCSV = Parser.modifyListAttributes(l, attributesToKeep, dataCSV);
			
			// Test Selection
			
			// Creating a list of trees
			List<RegressionTree> forest = new ArrayList<RegressionTree>();
			for (int i = 0; i < 10 ; i++){
				forest.add(new RegressionTree(l, 4));
			}
			// Data selected to evaluate fitness
			double[][] test = Parser.getNSamples(10, dataCSV);
			
			// Print fitness of each tree
			for (int i = 0; i < forest.size() ; i++){
				System.out.println(i+" : "+forest.get(i).getEvaluation(test));
			}
			
			// Print selected indices
			List<Integer> indices = GeneticProgramming.selection(forest, 5, test);
			for (int i=0 ; i<indices.size() ; i++){
				System.out.println("selected : "+indices.get(i));
			}	
			
			
			//Test combination
			RegressionTree father, mother;
			father = new RegressionTree(attributes, 3);
			mother = new RegressionTree(attributes, 3);
			
			List<RegressionTree> children = GeneticProgramming.combination(father, mother);
			
			VisualTree fatherV, motherV, childV;
			fatherV = new VisualTree("Father tree", father);
			fatherV.printTree();
			motherV = new VisualTree("Mother tree", mother);
			motherV.printTree();
			for (RegressionTree child : children) {
				childV = new VisualTree("Children trees", child);
				childV.printTree();
			}
			
			// Test getDepth
			List<Node> nodes = tree.getAllNodes();
			for (Node node : nodes) {
				System.out.println("Profondeur noeud = " + node.getDepth());
				System.out.println("Attributs autorisés : " + node.getAllowedAttributes());
			}
			
			
			// Test mutation
			VisualTree treeV = new VisualTree("Before Mutation", tree);
			treeV.printTree();
			
			List<RegressionTree> trees = new ArrayList<>();
			trees.add(tree);
			GeneticProgramming.mutation(trees, 0.5);
			VisualTree treeVAfter = new VisualTree("After Mutation", trees.get(0));
			treeVAfter.printTree();	

			VisualTree treeV2 = new VisualTree("Before Mutation 2", tree);
			treeV2.printTree();
			*/
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

}
