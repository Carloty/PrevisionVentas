package trees;

import java.util.ArrayList;
import java.util.List;

import trees.attributes.Attribute;
import trees.attributes.NominalAttribute;

/*
 * Represents a node in a regression tree
 */
public class Node {
	
	// Maximum value for the sales to predict
	public final static int MAX_SALES = 100000;
	
	// List of the children nodes. If the node is a leaf, this list is set to null.
    private List<Node> children = new ArrayList<Node>();
    // Reference to the parent node. If the current node is a root, the parent is set to null
    private Node parent = null;
    // Which attribute is tested in this node. If the node is a leaf, this field is set to null.
    private Attribute attribute = null;
    // If the attribute that is tested is Numerical or Date, the node contains the split value and the node only has two children
    private double splitValue = 0.0;
    // If the node is a leaf, its the predicted value
    private double output = 0.0;
    
    // Constructor for a leaf node
    public Node() {
    	setOutput(Math.random() * MAX_SALES);
    }

    // Constructor for a Nominal attribute (the split values correspond to the available values of the attribute)
	public Node(Attribute attribute) {
        this.attribute = attribute;
    }

	// Constructor for a Numerical or Date attribute
    public Node(Attribute attribute, double splitValue) {
        this.attribute = attribute;
        this.splitValue = splitValue;
    }
    
    public List<Node> getChildren() {
        return children;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    /*
    public void addChild(Attribute attribute) {
        Node child = new Node(attribute);
        child.setParent(this);
        this.children.add(child);
    }
    */

    public void addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    private void setOutput(double output) {
		this.output = output;
	}
    
    private double getOutput() {
    	return this.output;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        if(this.children.size() == 0) 
            return true;
        else 
            return false;
    }

    public void removeParent() {
        this.parent = null;
    }

	/*
	 * Recursively predict the value for the data
	 * If the node is a leaf, return the output value
	 * Else, ask the good child node what's his prediction
	 */
	public double predict(double[] data) {
		if (this.isLeaf()) {
			return getOutput();		
		} else {
			int attributeId = this.attribute.getId();
			
			if (this.attribute.getType() == Attribute.AttributeType.NOMINAL) {
				NominalAttribute at = (NominalAttribute)attribute;				
				int[] splitValues = at.getSplitValues();
				for (int i = 0; i < splitValues.length; i++) {
					if ((data[attributeId] == (double)splitValues[i])){
						return children.get(i).predict(data);
					}
				}
				return Double.NaN;
			} else {
				if (data[attributeId] <= splitValue) {
                    return children.get(0).predict(data);
                } else {
                    return children.get(1).predict(data);
                }
			} 
		}
	}

}
