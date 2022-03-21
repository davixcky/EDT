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
        if (root == null) return "Title not defined";

        return root.getValue();
    }

    public void insert(String parent, String value) {
        // TODO: Check if parent root is null
        if (parent == null) {
            throw new NullPointerException();
        }

        if (value == null) {
            throw new IllegalTreeNode();
        }

        internal_insert(parent, value);
    }

    public void internal_insert(String parent, String value) {
        boolean isNodeInserted = false;
        iterationQueue.reset();
        iterationQueue.add(root);

        System.out.println("Inserting " + value + " with " + parent + " as parent.");
        while (!iterationQueue.isEmpty() && !isNodeInserted) {
            TreeNode currentTreeNode = iterationQueue.poll();
            System.out.println("Checking if " + currentTreeNode.getValue() + " is equal to " + parent);
            if (currentTreeNode.getValue().equals(parent)) {
                currentTreeNode.insertChild(new TreeNode(value));
                isNodeInserted = true;
                continue;
            }

            // TODO: Avoid double node comparison
            TreeNode parentInsertion = currentTreeNode.find(new ILinkedHelper<TreeNode>() {
                @Override
                public boolean compare(TreeNode a, TreeNode b) {
                    iterationQueue.insert(a);
                    System.out.println("Checking if " + a.getValue() + " is equal to " + parent);
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
}
