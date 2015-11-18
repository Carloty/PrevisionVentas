package trees.attributes;

import java.text.ParseException;

/**
 * Class representing a NumericalAttribute
 * 
 * @author Charlotte
 * Inspired by :
 * {@link} https://github.com/haifengl/smile/blob/master/SmileData/src/main/java/smile/data/NumericAttribute.java
 *
 */
public class NumericalAttribute extends Attribute {
	
	/*
	 * Minimium taken by the NumericalAttribute
	 */
	private double min = 0;
	
	/*
	 * Maximun taken by the NumericalAttribute
	 * Initialiaze at +INF because is changed in the construtor
	 */
	private double max = Double.POSITIVE_INFINITY;

	/**
	 * Constructor of a NumericalAttribute (without description)
	 * 
	 * @param name
	 * 		Name of the attribute
	 * @param type
	 * 		Type of the attribute (see AttributeType in Attribute class)
	 * @param id
	 * 		The id of the attribute in the list of attributes in which it is added
	 * @param min
	 * 		The minimum value that can be reached by the attribute
	 * @param max
	 * 		The maximum value that can be reached by the attribute
	 */
	public NumericalAttribute(String name, AttributeType type, int id, double min, double max) {
		this(name, "", type, id, min, max);
	}
	
	/**
	 * Constructor of a NumericalAttribute
	 * 
	 * @param name
	 * 		Name of the attribute
	 * @param description
	 * 		Description of the attribute 
	 * @param type
	 * 		Type of the attribute (see AttributeType in Attribute class)
	 * @param id
	 * 		The id of the attribute in the list of attributes in which it is added
	 * @param min
	 * 		The minimum value that can be reached by the attribute
	 * @param max
	 * 		The maximum value that can be reached by the attribute
	 */
	public NumericalAttribute(String name, String description, AttributeType type, int id, double min, double max) {
		super(name, description, type, id);
		this.min = min;
		this.max = max;
	}
	
	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	/**
	 * Convert a value from the String format into a double
	 */
	@Override
	public double valueOf(String string) throws ParseException {
		return Double.valueOf(string);
	}

}
