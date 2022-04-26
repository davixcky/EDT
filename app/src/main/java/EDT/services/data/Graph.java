package EDT.services.data;

import javax.swing.*;

public class Graph {
    private NAryTree tree;
private LinkedList<GraphNode> vertex = new LinkedList<>();
    public Graph(NAryTree nAryTree) {
        this.tree = nAryTree;
    }
    public  GraphNodeID addNode(GraphNode graphNode){
        GraphNode checkNode = vertex.find(new ILinkedHelper<GraphNode>() {
            @Override
            public boolean compare(GraphNode a, GraphNode b) {
                return a.getValue().equals(b.getValue());
            }
        }, graphNode);
        if(checkNode== null){
            System.out.println("added new");
            vertex.insert(graphNode);
            graphNode.setID(new GraphNodeID(graphNode));
            return graphNode.getID();
        }else{
            System.out.println("already here");
            if(checkNode.getDuration()==0){
                System.out.println("Modified");
                checkNode.setCost(graphNode.getCost());
                checkNode.setDuration(graphNode.getDuration());
            }
            return checkNode.getID();
        }
    }
    public  void addDependency(GraphNodeID source,GraphNodeID dependency){
        source.getNode().getDependencies().insert(dependency.getNode());
    }
    public void print(){

    }
    public GraphNode getVertex(String deliverable){
        GraphNode searchNode = vertex.find(new ILinkedHelper<GraphNode>() {
            @Override
            public boolean compare(GraphNode a, GraphNode b) {
                return a.getValue().getValue().equals(b.getValue().getValue());
            }
        },new GraphNode(new DeliverableTreeNode(deliverable),0,0));
        return  searchNode;
    }
}
