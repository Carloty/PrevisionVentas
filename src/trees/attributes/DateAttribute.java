package trees.attributes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class representing a DateAttribute
 * 
 * @author Charlotte
 * Inspired by :
 * {@link} https://github.com/haifengl/smile/blob/master/SmileData/src/main/java/smile/data/DateAttribute.java
 *
 */
public class DateAttribute extends Attribute {
	
	/**
	 * The date format (yyyy-MM-dd for example)
	 */
	private DateFormat format;
	
	/**
	 * The earliest possible date
	 * (initialize at 01/01/1800 in constructor)
	 */
	public final double earliest;
	
	/**
	 * The latest possible date
	 * (initialize at 01/01/2500 in constructor)
	 */
	public final double latest;

	/**
	 * Constructor of an DateAttribute (without description)
	 * 
	 * @param name
	 * 		Name of the attribute
	 * @param type
	 * 		Type of the attribute (see AttributeType in Attribute class)
	 * @param format
	 * 		The date format associated to this attribute
	 * @param id
	 * 		The id of the attribute in the list of attributes in which it is added
	 * @throws ParseException
	 */
	public DateAttribute(String name, AttributeType type, String format, int id, boolean nullPossible) throws ParseException {
		this(name, "", type, format, id, nullPossible);
	}
	
	/**
	 * Constructor of an DateAttribute
	 * 
	 * @param name
	 * 		Name of the attribute
	 * @param description
	 * 		Description of the attribute
	 * @param type
	 * 		Type of the attribute (see AttributeType in Attribute class)
	 * @param format
	 * 		The date format associated to this attribute
	 * @param id
	 * 		The id of the attribute in the list of attributes in which it is added
	 * @throws ParseException
	 */
	public DateAttribute(String name, String description, AttributeType type, String format, int id, boolean nullPossible) throws ParseException {
		super(name, description, type, id, nullPossible);
		this.setFormat(format);
		earliest = valueOf(new SimpleDateFormat("yyyy-MM-dd").parse("1800-01-01"));
		latest = valueOf(new SimpleDateFormat("yyyy-MM-dd").parse("2500-01-01"));
	}

	public DateFormat getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = new SimpleDateFormat(format);
	}

	public void setFormat(DateFormat format) {
		this.format = format;
	}
	
	public double getEarliest() {
		return earliest;
	}
	
	public double getLatest() {
		return latest;
	}
	
    /**
     * Generate the string associated to a Date object.
     */
    public String toString(Date date) {
        return this.format.format(date);
    }
    
    /**
     * Returns the double value representation of a Date.
     */
    public double valueOf(Date date) {
        return Double.longBitsToDouble(date.getTime());
    }
    
    /**
	 * Get the double value associated to a string representing a Date
     */
    @Override
    public double valueOf(String s) throws ParseException {
    	if (!s.equals("")){
    		Date d = format.parse(s);
            return Double.longBitsToDouble(d.getTime());
    	} else {
    		return Double.NEGATIVE_INFINITY; // add condition (&& isNullValuePossible() ???)
    	}
        
    }

}
