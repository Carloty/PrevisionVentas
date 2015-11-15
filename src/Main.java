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
			

			List<Attribute> attributes = new ArrayList<Attribute>();
			
			attributes.add(new DateAttribute("Date", Attribute.AttributeType.DATE, "yyyy-MM-dd", 0));
			String[] nominalValues = {"OUI", "NON", "JE NE SAIS PAS"};
			attributes.add(new NominalAttribute("Attribut Nominal", Attribute.AttributeType.NOMINAL, nominalValues, 1));
			attributes.add(new NumericalAttribute("Attribut Numérique", Attribute.AttributeType.NUMERICAL, 2, -10, 10.5));
			
			RegressionTree tree = new RegressionTree(attributes, 3);
			
			double[] data = {Double.longBitsToDouble(new SimpleDateFormat("yyyy-MM-dd").parse("1992-10-09").getTime()), 2, 0};
			
			System.out.println(tree.predict(data));
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		

	}

}
