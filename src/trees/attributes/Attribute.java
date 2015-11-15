package trees.attributes;

import java.text.ParseException;

public abstract class Attribute {
	
	public enum AttributeType {DATE, NOMINAL, NUMERICAL};
	
	private AttributeType type;
	
	private String name;
	
	private String description;
	
	private int id;
	
	public Attribute(String name, AttributeType type, int id) {
		this(name, "", type, id);
	}
	
	public Attribute(String name, String description, AttributeType type, int id) {
		setName(name);
		setType(type);
		setDescription(description);
		setId(id);
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
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public abstract double valueOf(String string) throws ParseException;
	
}
