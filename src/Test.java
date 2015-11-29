import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
			List<Attribute> attributes = new ArrayList<Attribute>();
			
			// The first available type is Date. The Date may correspond to a format supported by Java
			attributes.add(new DateAttribute("Date", Attribute.AttributeType.DATE, "yyyy-MM-dd", 0, true));
			
			// The second available type is Nominal. The attribute may take its values within a set of defined strings
			String[] nominalValues = {"OUI", "NON", "JE NE SAIS PAS"};
			attributes.add(new NominalAttribute("Attribut Nominal", Attribute.AttributeType.NOMINAL, nominalValues, 1, true));
			
			// The last type is Numeric. It corresponds to doubles. The attributes may take its values within a range specified with the constructor
			attributes.add(new NumericalAttribute("Attribut Numérique", Attribute.AttributeType.NUMERICAL, 2, -10, 10.5, true));
			
			/*
			 * A regression tree is randomly build using the list of attributes and a maximum depth 
			 */
			RegressionTree tree = new RegressionTree(attributes,5);
			
			/*
			 * Build a data that can be put in entry of the tree. The table should only contain doubles. (See attribute type's methods) 
			 */
			double[] data = {Double.longBitsToDouble(new SimpleDateFormat("yyyy-MM-dd").parse("1992-10-09").getTime()), 2, 0};
			
			/*
			 * Visualize the tree in a new frame
			 */
			VisualTree treeV = new VisualTree("Regression tree", tree);
			treeV.printTree();
			
			/*
			 * Printout the value predicted by the tree (value of the ultimate leaf reached) 
			 */
			System.out.println(tree.predict(data));
			
			/*
			 * Test for the copy of the tree
			 */
			RegressionTree copy = tree.copy();
			VisualTree treeC = new VisualTree("Copy", copy);
			treeC.printTree();			
			
			/*
			 * Test import data
			 */
			double[][] dataCSV = Parser.getDataFromFile("age_pr2_without.csv");
			List<Attribute> l = Parser.getAllAttributes();
			
			/*
			 * Test choosing attributes
			 */
			/*int[] attributesToKeep = {1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1};
			dataCSV = Parser.modifyListAttributes(l, attributesToKeep, dataCSV);*/
			
			/*
			 * Test Selection
			 */
			// Creating a list of trees
			List<RegressionTree> forest = new ArrayList<RegressionTree>();
			for (int i = 0; i < 10 ; i++){
				forest.add(new RegressionTree(l, 4));
			}
			// Data selected to evaluate fitness
			double[][] test = Parser.getNSamples(10, dataCSV);
			
			// Print fitness of each tree
			for (int i = 0; i < forest.size() ; i++){
				System.out.println(i+" : "+forest.get(i).getFitness(test));
			}
			
			// Print selected indices
			List<Integer> indices = GeneticProgramming.selection(forest, 5, test);
			for (int i=0 ; i<indices.size() ; i++){
				System.out.println("selected : "+indices.get(i));
			}	
			
			/*
			 * Test combination
			 */
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

}
