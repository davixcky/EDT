package EDT.services.data;


import java.util.Date;

public class GraphNode extends  Node{
    private float duration;
    private double cost;

    private TreeNode dependency;
    public GraphNode(TreeNode value, double c, float dur, TreeNode dependence) {
        super(value);
        this.cost =c;
        this.duration = dur;
        this.dependency = dependence;
    }
}
