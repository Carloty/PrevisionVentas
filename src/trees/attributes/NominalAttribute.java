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

	private List<String> values;
	
	private Map<String, Integer> map;
	
	public NominalAttribute(String name, AttributeType type, String[] values, int id) {
		this(name, "", type, values, id);
	}
	
	public NominalAttribute(String name, String description, AttributeType type, String[] values, int id) {
		super(name, description, type, id);
		this.values = new ArrayList<String>();
		this.map = new HashMap<String, Integer>();
		for (int i = 0; i < values.length; i++) {
            this.values.add(values[i]);
            this.map.put(values[i], i);
        }
	}
	
	public int size() {
        return this.values.size();
    }
	
	public String[] values() {
        return values.toArray(new String[values.size()]);
    }
	
	public int[] getSplitValues(){
		int[] splitValues = new int[this.size()];
		for (int i = 0 ; i<this.size(); i++){
			splitValues[i] = map.get(values.get(i));
		}
		return splitValues;
	}

	@Override
    public double valueOf(String s) {// throws ParseException {
        Integer i = map.get(s);
        /*if (i == null) {
            if (open) {
                i = values.size();
                map.put(s, i);
                values.add(s);
            } else {
                throw new IllegalArgumentException("Invalid string value: " + s);
            }
        }*/
        
        return i;
    }
	
}
