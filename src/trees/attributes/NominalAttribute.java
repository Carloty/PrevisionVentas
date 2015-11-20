package trees.attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing a NominalAttribute
 * 
 * @author Charlotte
 * Inspired by :
 * {@link} https://github.com/haifengl/smile/blob/master/SmileData/src/main/java/smile/data/NominalAttribute.java
 *
 */
public class NominalAttribute extends Attribute {

	/**
	 * Values taken by the attribute
	 */
	private List<String> values;
	
	/**
	 * Mapping the values taken by the attribute with an int corresponding to their ID
	 */
	private Map<String, Integer> map;
	
	/**
	 * Constructor of a NominalAttribute (without description)
	 * 
	 * @param name
	 * 		Name of the attribute
	 * @param type
	 * 		Type of the attribute (see AttributeType in Attribute class)
	 * @param values
	 * 		Values taken by the attribute
	 * @param id
	 * 		The id of the attribute in the list of attributes in which it is added
	 */
	public NominalAttribute(String name, AttributeType type, String[] values, int id, boolean nullPossible) {
		this(name, "", type, values, id, nullPossible);
	}
	
	/**
	 * Constructor of a NominalAttribute
	 * 
	 * @param name
	 * 		Name of the attribute
	 * @param description
	 * 		Description of the attribute
	 * @param type
	 * 		Type of the attribute (see AttributeType in Attribute class)
	 * @param values
	 * 		Values taken by the attribute
	 * @param id
	 * 		The id of the attribute in the list of attributes in which it is added
	 */
	public NominalAttribute(String name, String description, AttributeType type, String[] values, int id, boolean nullPossible) {
		super(name, description, type, id, nullPossible);
		this.values = new ArrayList<String>();
		this.map = new HashMap<String, Integer>();
		for (int i = 0; i < values.length; i++) {
            this.values.add(values[i]);
            this.map.put(values[i], i);
        }
	}
	
	/**
	 * Get the number of values that the attribute can take
	 * 
	 * @return
	 * 		The number of possible values
	 */
	public int size() {
        return this.values.size();
    }
	
	/**
	 * Get an array of String with all the possible values of the attribute
	 * 
	 * @return
	 * 		String[] containing all the possible values
	 */
	public String[] values() {
        return values.toArray(new String[values.size()]);
    }
	
	/**
	 * Get an array of integers representing the values taken by the attributes in numeric format
	 * 
	 * @return
	 * 		int[] containing the numbers representing each possible value of the attribute
	 */
	public int[] getSplitValues(){
		int[] splitValues = new int[this.size()];
		for (int i = 0 ; i<this.size(); i++){
			splitValues[i] = map.get(values.get(i));
		}
		return splitValues;
	}

	/**
	 * Get the double value associated to a value taken by the nominal attribute
     */
	@Override
    public double valueOf(String s) {// throws ParseException {
		if (!s.equals("")){
			Integer i = map.get(s);       
	        return i;
		} else {
    		return Double.NEGATIVE_INFINITY; // add condition (&& isNullValuePossible() ???)
    	}
    }
	
}
