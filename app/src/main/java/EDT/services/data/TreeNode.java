package EDT.services.data;

public class TreeNode extends Node<String> {
    public LinkedList<TreeNode> children;

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

    public TreeNode getChild(String value) {
        if (children == null) return null;

        children.find(System.out::println);

        return null;
    }

    public void iterateChildren(ILinkedHelper<TreeNode> func) {
        if (children == null) return;

        children.find(func);
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "value=" + value +
                ", children=" + children +
                '}';
    }
}
