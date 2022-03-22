package EDT.services.data;

import EDT.exceptions.IllegalTreeNode;

public class NAryTree {
    private TreeNode root;
    private final Queue<TreeNode> iterationQueue;

    public NAryTree() {
        root = null;
        iterationQueue = new Queue<>();
    }

    public void setTitle(String title) {
        if (root == null) {
            root = new TreeNode(title);
            return;
        }

        root.setValue(title);
    }

    public String getTitle() {
        if (root == null) return null;

        return root.getValue();
    }

    public void insert(String parent, String value) {
        // If the tree is empty, it will set the value as the root element
        if (parent == null && root == null) {
            setTitle(value);
            return;
        }

        if (parent == null) {
            throw new NullPointerException();
        }

        if (value == null) {
            throw new IllegalTreeNode();
        }

        internal_insert(parent, value);
    }

    public void internal_insert(String parent, String value) {
        if (root.getValue().equals(parent)) {
            root.insertChild(new TreeNode(value));
            return;
        }

        boolean isNodeInserted = false;
        iterationQueue.reset();
        iterationQueue.add(root);

        while (!iterationQueue.isEmpty() && !isNodeInserted) {
            TreeNode currentTreeNode = iterationQueue.poll();

            TreeNode parentInsertion = currentTreeNode.find(new ILinkedHelper<TreeNode>() {
                @Override
                public boolean compare(TreeNode a, TreeNode b) {
                    iterationQueue.insert(a);
                    return a.getValue().equals(b.getValue());
                }
            }, parent);

            if (parentInsertion != null) {
                parentInsertion.insertChild(new TreeNode(value));
                isNodeInserted = true;
            }
        }

        System.out.println("Node was inserted: " + isNodeInserted);
        System.out.println("Remaining queue: " + iterationQueue.size());
        System.out.println();
    }

    @Override
    public String toString() {
        if (root == null) return "Empty";

        iterationQueue.reset();
        iterationQueue.add(root);

        StringBuilder data = new StringBuilder();
        final int[] idx = new int[1];
        while (!iterationQueue.isEmpty()) {
            TreeNode currentTreeNode = iterationQueue.poll();

            data.append("Parent: [").append(currentTreeNode.getValue()).append("]\n");
            idx[0] = 1;
            currentTreeNode.forEachChild(new ILinkedHelper<TreeNode>() {
                @Override
                public void handle(TreeNode node) {
                    iterationQueue.add(node);
                    data.append("\t").append(idx[0]).append(". ");
                    data.append(node.toString(0)).append("\n");
                    idx[0] += 1;
                }
            });

            data.append("\n");
        }

        return data.toString();
    }
}
