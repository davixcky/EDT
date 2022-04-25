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
        System.out.println(vertex.toString());
        System.out.println(vertex.tail.getValue().getDependencies().head);
    }
}
