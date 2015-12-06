package trees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import trees.attributes.Attribute;
import trees.attributes.DateAttribute;
import trees.attributes.NominalAttribute;
import trees.attributes.NumericalAttribute;

/**
 * Represents a regression tree, supported for genetic programming
 *
 */
public class RegressionTree {
	
	// The list of the available attributes in the data
	private HashMap<Integer,Attribute> attributes = null;
	// Reference to the root node of the tree
	private Node root = null;
	// Maximum depth of the tree
	private int depth = 0;
	// Tree fitness
	private double fitness = -1;
	
	public RegressionTree(HashMap<Integer,Attribute> attributes) {
		List<Integer> allowedAttributes = new ArrayList<Integer>();
		allowedAttributes.addAll(attributes.keySet());
		setAttributes(attributes);
		setDepth(allowedAttributes.size()+1);
		setRoot(randomInitialization(allowedAttributes, this.getDepth()));
	}
	
	/**
	 * Constructor of a RegressionTree
	 * @param attributes 
	 * 		List of Attribute that the data will contain
	 * @param depthMax
	 * 		Max depth of the tree
	 */
	public RegressionTree(HashMap<Integer,Attribute> attributes, int depthMax) {
		List<Integer> allowedAttributes = new ArrayList<Integer>();
		allowedAttributes.addAll(attributes.keySet());
		setAttributes(attributes);
		if (depthMax > allowedAttributes.size()+1) {
			setDepth(allowedAttributes.size()+1);
		} else {
			setDepth(depthMax);
		}
		setRoot(randomInitialization(allowedAttributes, this.getDepth()));
	}

	/**
	 * Constructor of a RegressionTree
	 * @param root
	 * 		The node to be set as the root of the tree
	 */
	public RegressionTree(HashMap<Integer,Attribute> attributes, Node root, int depthMax) {
		List<Integer> allowedAttributes = new ArrayList<Integer>();
		allowedAttributes.addAll(attributes.keySet());
		setAttributes(attributes);
		setDepth(depthMax);
        setRoot(root);
	}
	
	public HashMap<Integer,Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(HashMap<Integer,Attribute> attributes) {
		this.attributes = attributes;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public double getFitness() {
		return - fitness;
	}

	public void setFitness(double[][] data) {
		double f = 0;
		double error;
		for (int i = 0; i < data.length; i++){
			error = Math.abs(this.predict(data[i]) - data[i][data[i].length-1]);
			f = f + error;
		}
		f = f/data.length;
		
		this.fitness = f;
	}
	
	public void reinitializeFitness() {
		this.fitness = -1;
	}

	public Node getRoot() {
		return root;
	}
	public void setRoot(Node root) {
		this.root = root;
		root.setParent(null);
	}
	
	public List<Node> getAllNodes() {
		List<Node> nodeList = new ArrayList<Node>();
		this.getRoot().getNodes(nodeList);
		return nodeList;		
	}
	
    public RegressionTree copy() {
        Node root = this.getRoot();
        
        if (root == null) {
            return new RegressionTree(this.getAttributes(), null, this.getDepth());
        } else {
            return new RegressionTree(this.getAttributes(), root.copy(), this.getDepth());
        }
    }
	
	
	/**
	 * Predict a value for the data
	 * @param data
	 * 		The entry to predict. Each attribute has a double value.
	 * @return
	 * 		The double output value of the ultimate leaf reached in the tree
	 */
	public double predict(double[] data) {
		return root.predict(data);
	}
	
	/*
	 * Recursively build a regression tree. 
	 * If the depth is 1, the method returns a leaf node containing a random output value
	 * Else, a leaf node is created with a probability of 0.3
	 * Else, for each node, an attribute is randomly selected.
	 * For Numerical or Date attribute, the split value for the node is randomly selected within the range defined for the attribute
	 * (see attributes constructors)
	 */
	private Node randomInitialization(List<Integer> allowedAttributes, int depth) {
		//System.out.println("Random initialisation profondeur " + depth + " attributs " + allowedAttributes.toString());
		double probabilityLeafNode = 0.3;
		Node root;
		Random r = new Random();
		if (allowedAttributes.isEmpty() || depth == 1 || r.nextDouble() <= probabilityLeafNode) {
			return new Node(new ArrayList<Integer>(allowedAttributes));			
		} else {
			// Changed for the control of the attributes in the tree
			int attributeId = allowedAttributes.get(r.nextInt(allowedAttributes.size()));
			Attribute attribute = attributes.get(attributeId);
			List<Integer> childAttributes = new ArrayList<Integer>(allowedAttributes);
			if (attribute.getType() == Attribute.AttributeType.NOMINAL) {
				childAttributes.remove((Integer)attributeId);
				NominalAttribute at = (NominalAttribute)attribute;
				int[] splitValues = at.getSplitValues();
				root = new Node(new ArrayList<Integer>(allowedAttributes), attribute);
				for (int i = 0; i < splitValues.length; i++) {
					root.addChild(randomInitialization(childAttributes, depth-1));
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
				root = new Node(new ArrayList<Integer>(allowedAttributes), attribute, min + r.nextDouble() * (max-min));
				root.addChild(randomInitialization(childAttributes, depth-1));
				root.addChild(randomInitialization(childAttributes, depth-1));
			}
			// ADDED FOR NULL VALUES
			// adding a child at the end of children list to take into account null values (<-> -INF in double)
			if(attribute.isNullValuePossible()){
				root.addChild(randomInitialization(childAttributes, depth-1));
			}
			return root;
		}
	}
	
	/**
	 * Get the fitness of this tree
	 * 
	 * @param data
	 * 		Data to predict, in an array of doubles, one line per data to predict
	 * @return
	 * 		The fitness of this tree (mean of the committed errors)
	 */
	public double getEvaluation(double[][] data){
		// if fitness was never initialized
		if (this.fitness == -1){
			setFitness(data);
		}
		// return fitness value
		return getFitness();
	}
}
