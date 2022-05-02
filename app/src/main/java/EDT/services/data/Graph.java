package EDT.services.data;

import javax.swing.*;
import java.util.Iterator;
import java.util.function.Consumer;

public class Graph {

    private NAryTree tree;
    private LinkedList<GraphNode> vertex = new LinkedList<>();

    public Graph(NAryTree nAryTree) {
        this.tree = nAryTree;
    }

    /**
     * This method adds a new GraphNode to the Graph if it's not already in the
     * graph, gives it an GraphID and returns it. However,if the GraphNode
     * already exists, it will return it's GraphID<p>
     * If a GraphNode matching the input is found but the duration and cost
     * differ from 0, then both are updated (This happens because to establish
     * dependencies we use a dummy node with duration and cot set to 0)
     *
     */
    public GraphNodeID addNode(GraphNode graphNode) {
        GraphNode checkNode = vertex.find(new ILinkedHelper<GraphNode>() {
            @Override
            public boolean compare(GraphNode a, GraphNode b) {
                return a.getValue().equals(b.getValue());
            }
        }, graphNode);
        if (checkNode == null) {
            System.out.println("added new");
            vertex.insert(graphNode);
            graphNode.setID(new GraphNodeID(graphNode));
            return graphNode.getID();
        } else {
            System.out.println("already here");
            if (checkNode.getDuration() == 0) {
                System.out.println("Modified");
                checkNode.setCost(graphNode.getCost());
                checkNode.setDuration(graphNode.getDuration());
            }
            return checkNode.getID();
        }
    }

    /**
     * This method adds the dependency to the LinkedList of dependencies of the
     * source<p>
     * However, if doing so will result in a cycle or is redundant it throws an exception
     */
    public void addDependency(GraphNodeID source, GraphNodeID dependency) throws Exception {
        GraphNode checkNodeS = source.getNode().getDependencies().find(new ILinkedHelper<GraphNode>() {
            @Override
            public boolean compare(GraphNode a, GraphNode b) {
                return a.getValue().equals(b.getValue());
            }
        }, dependency.getNode());
        GraphNode checkNodeD = dependency.getNode().getDependencies().find(new ILinkedHelper<GraphNode>() {
            @Override
            public boolean compare(GraphNode a, GraphNode b) {
                return a.getValue().equals(b.getValue());
            }
        }, source.getNode());
        if (checkNodeS != null) {
            throw new Exception("Already a dependency");
        } else if (checkNodeD != null) {
            throw new Exception("No cycles allowed");
        } else {
            source.getNode().getDependencies().insert(dependency.getNode());
        }
    }

    /**
     * This method returns GraphNode that contains de deliverable node with the
     * typed value
     *
     */
    public GraphNode getVertex(String deliverable) {
        GraphNode searchNode = vertex.find(new ILinkedHelper<GraphNode>() {
            @Override
            public boolean compare(GraphNode a, GraphNode b) {
                return a.getValue().getValue().equals(b.getValue().getValue());
            }
        }, new GraphNode(new DeliverableTreeNode(deliverable), 0, 0));
        return searchNode;
    }

    /**
     * The following method returns the sum of all costs of the nodes in the
     * graph
     *
     */
    public double getTotalCost() {
        final double[] d = new double[1];
        vertex.forEach(new Consumer<ListNode<GraphNode>>() {
            @Override
            public void accept(ListNode<GraphNode> graphNodeListNode) {
                d[0] = d[0] + graphNodeListNode.getValue().getCost();
            }
        });
        return d[0];
    }

    /**
     * The following method returns the sum of all durations in days
     *
     */
    public float getTotalDuration() {
        final float[] d = new float[1];
        vertex.forEach(new Consumer<ListNode<GraphNode>>() {
            @Override
            public void accept(ListNode<GraphNode> graphNodeListNode) {
                d[0] = d[0] + graphNodeListNode.getValue().getDuration();
            }
        });
        return d[0];
    }

    public LinkedList<GraphNode> getVertexList() {
        return vertex;
    }

}
