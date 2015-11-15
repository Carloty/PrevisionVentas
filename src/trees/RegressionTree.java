package trees;

import java.util.List;
import java.util.Random;

import trees.attributes.Attribute;
import trees.attributes.DateAttribute;
import trees.attributes.NominalAttribute;
import trees.attributes.NumericalAttribute;

public class RegressionTree {
	private List<Attribute> attributes = null;
	private Node root = null;
	private int depth = 0;
	
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
	
	public double predict(double[] data) {
		return root.predict(data);
	}
	
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
