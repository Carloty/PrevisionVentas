package trees.attributes;

import java.text.ParseException;

public abstract class Attribute {
	
	/**
	 *	Enumerate representing an attribute type
	 */
	public enum AttributeType {DATE, NOMINAL, NUMERICAL};
	
	/**
	 * The attribute type
	 */
	private AttributeType type;
	
	/**
	 * The name of the attribute
	 */
	private String name;
	
	/**
	 * The description associated to this attribute
	 */
	private String description;
	
	/**
	 * The ID of this attribute in the list of Attribute in which it is stored
	 */
	private int id;
	
	private boolean nullValuePossible;
	
	/**
	 * Constructor of an Attribute
	 * 
	 * @param name
	 * 		Name of the attribute
	 * @param type
	 * 		Type of the attribute (see AttributeType)
	 * @param id
	 * 		The id of the attribute in the list of attributes in which it is added
	 */
	public Attribute(String name, AttributeType type, int id, boolean nullPossible) {
		this(name, "", type, id, nullPossible);
	}
	
	/**
	 * Constructor of an Attribute
	 * 
	 * @param name
	 * 		Name of the attribute
	 * @param description
	 * 		Description of the attribute
	 * @param type
	 * 		Type of the attribute (see AttributeType)
	 * @param id
	 * 		The id of the attribute in the list of attributes in which it is added
	 */
	public Attribute(String name, String description, AttributeType type, int id, boolean nullPossible) {
		setName(name);
		setType(type);
		setDescription(description);
		setId(id);
		setNullValuePossible(nullPossible);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AttributeType getType() {
		return type;
	}

	public void setType(AttributeType type) {
		this.type = type;
	}
	
	public boolean isNullValuePossible() {
		return nullValuePossible;
	}

	public void setNullValuePossible(boolean nullValuePossible) {
		this.nullValuePossible = nullValuePossible;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	/*
     * Returns the double value representation of a value taken by the attribute (in String format).
     */
	public abstract double valueOf(String string) throws ParseException;
	
}
