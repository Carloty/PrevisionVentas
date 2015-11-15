package trees;

import java.util.ArrayList;
import java.util.List;

import trees.attributes.Attribute;
import trees.attributes.NominalAttribute;

public class Node {
	public final static int MAX_SALES = 100000;
	
    private List<Node> children = new ArrayList<Node>();
    private Node parent = null;
    private Attribute attribute = null;
    private double splitValue = 0.0;
    private double output = 0.0;
    
    public Node() {
    	setOutput(Math.random() * MAX_SALES);
    }

	public Node(Attribute attribute) {
        this.attribute = attribute;
    }

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

    public void addChild(Attribute attribute) {
        Node child = new Node(attribute);
        child.setParent(this);
        this.children.add(child);
    }

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
