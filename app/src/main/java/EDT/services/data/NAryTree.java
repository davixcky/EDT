package EDT.services.data;

import EDT.exceptions.IllegalTreeNode;

public class NAryTree {
    public TreeNode root;

    private Queeu<TreeNode> iterationQueue;

    public NAryTree() {
        root = null;
        iterationQueue = new Queeu<>();
    }

    public void setTitle(String title) {
        if (root == null) {
            root = new TreeNode(title);
            return;
        }

        root.setValue(title);
    }

    public String getTitle(String title) {
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
            TreeNode tmp = iterationQueue.poll();
            System.out.println("Checking if " + tmp.getValue() + " is equal to " + parent);
            if (tmp.getValue().equals(parent)) {
                tmp.insertChild(new TreeNode(value));
                isNodeInserted = true;
                continue;
            }

//            tmp.iterateChildren((node) -> {
//                current.add(node);
//                System.out.println("Checking if " + node.getValue() + " is equal to " + parent);
//                if (node.value.equals(parent)) {
//                    isNodeInserted.set(true);
//                    node.insertChild(new TreeNode(value));
//                }
//            });

            if (tmp.children == null) continue;

            // TODO: Avoid double node comparison
            // TODO: Move this validation to node itself
            for (ListNode<TreeNode> listNode: tmp.children) {
                    TreeNode node = listNode.value;
                    iterationQueue.add(node);
                    System.out.println("Checking if " + node.getValue() + " is equal to " + parent);
                    if (node.value.equals(parent)) {
                        isNodeInserted = true;
                        node.insertChild(new TreeNode(value));
                        System.out.println("Should end here");
                        break;
                    }
            }

        }



        System.out.println("Node was inserted: " + isNodeInserted);
        System.out.println("Remaining queue: " + iterationQueue.size());
        System.out.println();
    }
}
