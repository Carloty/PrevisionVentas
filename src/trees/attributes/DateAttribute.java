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
	
	private DateFormat format;
	public final double earliest;
	public final double latest;

	public DateAttribute(String name, AttributeType type, String format, int id) throws ParseException {
		this(name, "", type, format, id);
	}
	
	public DateAttribute(String name, String description, AttributeType type, String format, int id) throws ParseException {
		super(name, description, type, id);
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
     * Generate the date string.
     */
    public String toString(Date date) {
        return this.format.format(date);
    }
    
    /**
     * Returns the double value representation of a data object.
     */
    public double valueOf(Date date) {
        return Double.longBitsToDouble(date.getTime());
    }
    
    @Override
    public double valueOf(String s) throws ParseException {
        Date d = format.parse(s);
        return Double.longBitsToDouble(d.getTime());
    }

}
