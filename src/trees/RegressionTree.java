package trees;

import java.util.List;
import java.util.Random;

import trees.attributes.Attribute;
import trees.attributes.DateAttribute;
import trees.attributes.NominalAttribute;
import trees.attributes.NumericalAttribute;

/*
 * Represents a regression tree, supported for genetic programming
 */
public class RegressionTree {
	
	// The list of the available attributes in the data
	private List<Attribute> attributes = null;
	// Reference to the root node of the tree
	private Node root = null;
	// Maximum depth of the tree
	private int depth = 0;
	
	// Random constructor for a tree using a list of attributes and a maximum depth
	public RegressionTree(List<Attribute> attributes, int depthMax) {
		setAttributes(attributes);
		setDepth(depthMax);
		setRoot(randomInitialization(depthMax));
	}

	public RegressionTree(Node root) {
        setRoot(root);
	}
	
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public Node getRoot() {
		return root;
	}
	public void setRoot(Node root) {
		this.root = root;
	}
	
	/*
	 *  Predict a value for the data (output value of the ultimate leaf reached)
	 */
	public double predict(double[] data) {
		return root.predict(data);
	}
	
	/*
	 * Recursively build a regression tree. 
	 * If the depth is 1, the method returns a leaf node containing a random output value
	 * Else, for each node, an attribute is randomly selected.
	 * For Numerical or Date attribute, the split value for the node is randomly selected within the range defined for the attribute
	 * (see attributes constructors)
	 */
	private Node randomInitialization(int depth) {
		Node root;
		if (depth == 1) {
			return new Node();			
		} else {
			Random r = new Random();
			int attributeId = r.nextInt(attributes.size());
			Attribute attribute = attributes.get(attributeId);
			
			if (attribute.getType() == Attribute.AttributeType.NOMINAL) {
				NominalAttribute at = (NominalAttribute)attribute;
				int[] splitValues = at.getSplitValues();
				root = new Node(attribute);
				for (int i = 0; i < splitValues.length; i++) {
					root.addChild(randomInitialization(depth-1));
				}
			} else {
				double min, max;
				if (attribute.getType() == Attribute.AttributeType.DATE) {
					DateAttribute at = (DateAttribute)attribute;
					min = at.getEarliest();
					max = at.getLatest();
				} else {
					NumericalAttribute at = (NumericalAttribute)attribute;
					min = at.getMin();
					max = at.getMax();
				}
				root = new Node(attribute, min + r.nextDouble() * max);
				root.addChild(randomInitialization(depth-1));
				root.addChild(randomInitialization(depth-1));
			}
			return root;
		}
	}
}
