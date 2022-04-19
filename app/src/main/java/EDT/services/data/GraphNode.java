package EDT.services.data;


import java.util.Date;

public class GraphNode extends  Node{
    private float duration;
    private double cost;
    private Date startDate;
    private int dependency;
    public GraphNode(TreeNode value, double c, float dur,int dep, Date dateTime) {
        super(value);
        this.cost =c;
        this.duration = dur;
        this.startDate = dateTime;
        this.dependency = dep;
    }
}
