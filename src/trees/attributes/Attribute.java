package trees.attributes;

import java.text.ParseException;

public abstract class Attribute {
	
	public enum AttributeType {DATE, NOMINAL, NUMERICAL};
	
	private AttributeType type;
	
	private String name;
	
	private String description;
	
	public Attribute(String name, AttributeType type) {
		this(name, "", type);
	}
	
	public Attribute(String name, String description, AttributeType type) {
		setName(name);
		setType(type);
		setDescription(description);
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
	
	public abstract double valueOf(String string) throws ParseException;
	
}
