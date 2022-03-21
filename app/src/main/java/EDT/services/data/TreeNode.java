package EDT.services.data;

public class TreeNode extends Node<String> {
    private LinkedList<TreeNode> children;

    public TreeNode(String value) {
        super(value);

        children = null;
    }

    public void insertChild(TreeNode childNode) {
        if (children == null) {
            children = new LinkedList<>();
        }

        children.insert(childNode);
    }

    public TreeNode find(ILinkedHelper<TreeNode> func, String searchValue) {
        if (children == null) return null;

        return children.find(func, new TreeNode(searchValue));
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "value=" + getValue() +
                ", children=" + children +
                '}';
    }
}
