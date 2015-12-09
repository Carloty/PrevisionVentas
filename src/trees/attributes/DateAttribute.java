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
	 * (initialize at 01/01/2000 in constructor)
	 */
	public double earliest;
	
	/**
	 * The latest possible date
	 * (initialize at 01/01/2020 in constructor)
	 */
	public double latest;

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
		earliest = hoursSince2010(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"));
		latest = hoursSince2010(new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01"));
	}
	
	public DateAttribute(String name, String description, AttributeType type, DateFormat format, int id, boolean nullPossible) throws ParseException {
		super(name, description, type, id, nullPossible);
		this.setFormat(format);
		earliest = hoursSince2010(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"));
		latest = hoursSince2010(new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01"));
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
	
	public void setEarliest(double earliest) {
		this.earliest = earliest;
	}
	
	
	public double getLatest() {
		return latest;
	}
	
	public void setLatest(double latest) {
		this.latest = latest;
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
        return hoursSince2010(date);
    }
    
    /**
	 * Get the double value associated to a string representing a Date
     */
    @Override
    public double valueOf(String s) throws ParseException {
    	if (!s.equals("")){
    		Date d = format.parse(s);
            return hoursSince2010(d);
    	} else {
    		return Double.NEGATIVE_INFINITY; // add condition (&& isNullValuePossible() ???)
    	}
        
    }
    
    /**
     * Get the date in String format associated to this double
     */
    public String doubleToDate(double d){
    	if (d != Double.NEGATIVE_INFINITY){
    		try {
	    		long reference = (new SimpleDateFormat("yyyy-MM-dd").parse("2010-01-01")).getTime()/(3600*1000);
	    		long diff = (long)d;
	    		long date = diff + reference;
	    		Date correspondingDate = new Date(date*3600*1000);
	    		return toString(correspondingDate);
    		} catch (Exception e){
    			System.out.println("Troubles in doubleToDate in DateAttribute");
    		}
    	}
    	return "null";        
    }
    
    /**
     * Calculate the number of hours since 01/01/2010
     * 
     * @param d
     * 		A date
     * @return
     * 		Number of hours in double format
     */
    private double hoursSince2010(Date d) {
    	try {
	    	long reference = (new SimpleDateFormat("yyyy-MM-dd").parse("2010-01-01")).getTime()/(3600*1000);
	    	long dateSince1970 = d.getTime()/(3600*1000);
	    	long diff = dateSince1970 - reference;
	    	return (new Long(diff)).doubleValue();
    	} catch (Exception e){
    		System.out.println("Troubles in hoursSince2010 in DateAttribute");
    	}
    	return Double.NaN;
    }

}
