package EDT.services.data;

public class GraphNodeID {
    static int id;
    private GraphNode node;

    GraphNodeID() {
        id++;
    }

    GraphNodeID(GraphNode g) {
        this();
        this.node = g;
    }

    public GraphNode getNode() {
        return node;
    }

    public static int getId() {
        return id;
    }
}

