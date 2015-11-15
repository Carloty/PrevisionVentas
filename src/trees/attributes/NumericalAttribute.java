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
	
	private double min = 0;
	private double max = Double.POSITIVE_INFINITY;

	public NumericalAttribute(String name, AttributeType type, int id, double min, double max) {
		this(name, "", type, id, min, max);
	}
	
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

	@Override
	public double valueOf(String string) throws ParseException {
		return Double.valueOf(string);
	}

}
