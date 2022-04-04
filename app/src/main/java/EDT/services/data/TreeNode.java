package EDT.services.data;

public abstract class TreeNode extends Node<String> {
    private LinkedList<TreeNode> children;

    protected String parentValue;
    public TreeNode(String value) {
        super(value);

        children = null;
    }

    public boolean insertChild(TreeNode childNode) {
        if (children == null) {
            children = new LinkedList<>();
        }

        if (children.indexOf(childNode) != -1)
            return false;

        children.insert(childNode);
        return true;
    }

    public void deleteChild(TreeNode childNode) {
        if (children == null) return;

        children.remove(childNode);
    }

    public TreeNode find(ILinkedHelper<TreeNode> func, TreeNode searchValue) {
        if (children == null) return null;

        return children.find(func, searchValue);
    }

    public void forEachChild(ILinkedHelper<TreeNode> func) {
        if (children == null) return;

        children.forEach(treeNodeListNode -> func.handle(treeNodeListNode.getValue()));
    }

    public String toString(int idx) {
        if (children == null) return getValue();

        StringBuilder data = new StringBuilder();
        children.forEach(treeNodeListNode -> {
            data.append("\t".repeat(idx)).append(treeNodeListNode.getValue().toString(idx + 1)).append("\n");
        });

        return getValue() + "\n" + data;
    }

    public String getParentValue() {
        return parentValue;
    }

    public TreeNode getChildAt(int index) {
        if (children == null) return null;

        return children.getAt2(index);
    }

    public int size() {
        if (children == null) return 0;

         return children.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TreeNode node)) return false;

        if (parentValue == null || node.parentValue == null)
            return true;

        return parentValue.equals(node.parentValue) && getValue().equals(node.getValue());
     }

    @Override
    public String toString() {
        return "TreeNode{" +
                "children=" + children +
                ", parentValue='" + parentValue + '\'' +
                '}';
    }
}
