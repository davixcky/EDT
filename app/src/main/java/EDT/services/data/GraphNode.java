package EDT.services.data;


import java.util.Date;

public class GraphNode extends Node<TreeNode> {
    private float duration;
    private double cost;
private Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    private GraphNodeID id;
    private LinkedList<GraphNode> dependencies;

    public GraphNode(TreeNode value, double c, float dur) {
        super(value);
        this.cost = c;
        this.duration = dur;
        dependencies = new LinkedList<>();
    }

    public double getCost() {
        return cost;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setID(GraphNodeID g) {
        id = g;
    }

    public GraphNodeID getID() {
        return id;
    }

    public LinkedList<GraphNode> getDependencies() {
        return dependencies;
    }

    public float getDuration() {
        return duration;
    }
}
