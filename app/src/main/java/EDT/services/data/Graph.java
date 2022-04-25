package EDT.services.data;

import javax.swing.*;

public class Graph {
    private NAryTree tree;
private LinkedList<GraphNode> vertex;
    public Graph(NAryTree nAryTree) {
        this.tree = nAryTree;
    }
    public  void addVertex(GraphNode graphNode){
        GraphNode checkNode = vertex.find(new ILinkedHelper<GraphNode>() {
            @Override
            public boolean compare(GraphNode a, GraphNode b) {
                return a.getValue().equals(b.getValue());
            }
        }, graphNode);
        if(checkNode== null){
            vertex.insert(graphNode);
        }
    }

}
