import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import trees.RegressionTree;
import trees.attributes.Attribute;
import trees.attributes.DateAttribute;
import trees.attributes.NominalAttribute;
import trees.attributes.NumericalAttribute;

public class Main {

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
			attributes.add(new DateAttribute("Date", Attribute.AttributeType.DATE, "yyyy-MM-dd", 0, false));
			
			// The second available type is Nominal. The attribute may take its values within a set of defined strings
			String[] nominalValues = {"OUI", "NON", "JE NE SAIS PAS"};
			attributes.add(new NominalAttribute("Attribut Nominal", Attribute.AttributeType.NOMINAL, nominalValues, 1, false));
			
			// The last type is Numeric. It corresponds to doubles. The attributes may take its values within a range specified with the constructor
			attributes.add(new NumericalAttribute("Attribut Numérique", Attribute.AttributeType.NUMERICAL, 2, -10, 10.5, false));
			
			/*
			 * A regression tree is randomly build using the list of attributes and a maximum depth 
			 */
			RegressionTree tree = new RegressionTree(attributes, 10);
			
			/*
			 * Build a data that can be put in entry of the tree. The table should only contain doubles. (See attribute type's methods) 
			 */
			double[] data = {Double.longBitsToDouble(new SimpleDateFormat("yyyy-MM-dd").parse("1992-10-09").getTime()), 2, 0};
			
			
			/*
			 * Printout the value predicted by the tree (value of the ultimate leaf reached) 
			 */
			System.out.println(tree.predict(data));
			
			
			/*
			 * Test import data
			 */
			double[][] dataCSV = Parser.getDataFromSCVFile("age_pr2_without.csv");
			System.out.println("Number of instances :" + dataCSV.length);
			System.out.println("Number of attributes :" + (dataCSV[0].length-1));
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		

	}

}
