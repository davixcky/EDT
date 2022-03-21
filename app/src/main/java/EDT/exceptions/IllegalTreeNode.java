package EDT.exceptions;

public class IllegalTreeNode extends RuntimeException {
    public IllegalTreeNode() {
        super("Node value cannot be null", new Throwable());
    }
}
