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

	public NumericalAttribute(String name, AttributeType type) {
		this(name, "", type);
	}
	
	public NumericalAttribute(String name, String description, AttributeType type) {
		super(name, description, type);
	}

	@Override
	public double valueOf(String string) throws ParseException {
		return Double.valueOf(string);
	}

}
