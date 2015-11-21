package trees.visualization;

import java.util.List;

import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import trees.Node;
import trees.RegressionTree;
import trees.attributes.Attribute;
import trees.attributes.DateAttribute;
import trees.attributes.NominalAttribute;

/**
 * Allows to visualize regression trees
 * jgraphx.jar needed (https://github.com/jgraph/jgraphx)
 * @author Céline Leduc
 *
 */
public class VisualTree extends JFrame {
	
	/** To avoid a warning from the JFrame */
	private static final long serialVersionUID = -8123406571694511514L;
	private mxGraph graph;

	/**
	 * Constructor for a VisualTree
	 * @param tree 
	 * 		RegressionTree to be printed
	 */
	public VisualTree(RegressionTree tree) {
		super("Regression tree");
		int width = 0;

		graph = new mxGraph();
		Object parent = graph.getDefaultParent();
		Node root = tree.getRoot();

		graph.getModel().beginUpdate();
		try {
			addVisualNode(parent, root, width);
		} finally {
			graph.getModel().endUpdate();
		}
	}
	
	/*
	 * Build the visual graph associated with the regression tree
	 */
	private Object addVisualNode(Object parentV, Node node, int width) {
		Object nodeV;
		if (node.isLeaf()) {
			nodeV = graph.insertVertex(parentV, null, node.getOutput(), width, 2, 80, 30, "", true);		
		} else {
			Attribute attribute = node.getAttribute();
			nodeV = graph.insertVertex(parentV, null, attribute.getName(), width, 2, 80, 30, "", true);
			if (attribute.getType() == Attribute.AttributeType.NOMINAL) {
				Object childV;
				NominalAttribute at = (NominalAttribute)attribute;				
				int[] splitValues = at.getSplitValues();
				List<Node> children = node.getChildren();
				for (int i = 0; i < children.size(); i++) {
					childV = addVisualNode(nodeV, children.get(i), i*2);
					if (i==children.size()-1 && at.isNullValuePossible()){
						graph.insertEdge(nodeV, null, "null", nodeV, childV);
					} else {
						graph.insertEdge(nodeV, null, splitValues[i], nodeV, childV);
					}
				}
			} else {
				Object childL, childR;
				List<Node> children = node.getChildren();
				childL = addVisualNode(nodeV, children.get(0), 0);
				childR = addVisualNode(nodeV, children.get(1), 2);
				if(attribute.getType() == Attribute.AttributeType.DATE){
					DateAttribute at = (DateAttribute)attribute;	
					graph.insertEdge(nodeV, null, "<= " + at.doubleToDate(node.getSplitValue()), nodeV, childL);
					graph.insertEdge(nodeV, null, "> " + at.doubleToDate(node.getSplitValue()), nodeV, childR);
				} else {
					graph.insertEdge(nodeV, null, "<= " + node.getSplitValue(), nodeV, childL);
					graph.insertEdge(nodeV, null, "> " + node.getSplitValue(), nodeV, childR);
				}
				
				if (attribute.isNullValuePossible()){
					Object childNull = addVisualNode(nodeV, children.get(2), 4);
					graph.insertEdge(nodeV, null, "null", nodeV, childNull);
				}
			}
		}
		return nodeV;
	}

	/**
	 * Open a new frame displaying the tree
	 */
	public void printTree() {
		graph.refresh();
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(400, 320);
	    this.setVisible(true);
	}
}
