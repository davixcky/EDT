package EDT.services.data;


import java.util.Date;

public class GraphNode extends  Node{
    private float duration;
    private double cost;

    private LinkedList<GraphNode> dependencies;
    public GraphNode(TreeNode value, double c, float dur) {
        super(value);
        this.cost =c;
        this.duration = dur;
    }

    public double getCost() {
        return cost;
    }

    public LinkedList<GraphNode> getDependencies() {
        return dependencies;
    }

    public float getDuration() {
        return duration;
    }
}
