package EDT;

import EDT.services.data.*;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        NAryTree tree = new NAryTree();
        tree.setTitle("This is the title");
        tree.insert("This is the title", "Node A", NAryTree.NodeType.PACKAGE_NODE);
        tree.insert("This is the title", "Node B", NAryTree.NodeType.PACKAGE_NODE);
        tree.insert("Node A", "Node A.1", NAryTree.NodeType.PACKAGE_NODE);


        tree.insert("Node B", "Test", NAryTree.NodeType.PACKAGE_NODE);

        tree.insert("Node C", "Lol", NAryTree.NodeType.PACKAGE_NODE);
        tree.insert("This is the title", "Node C", NAryTree.NodeType.PACKAGE_NODE);
        tree.insert("Node C", "Lol", NAryTree.NodeType.PACKAGE_NODE);
        tree.insert("Node A.1", "Node D", NAryTree.NodeType.PACKAGE_NODE);
        tree.insert("Node A.1", "Node D.1", NAryTree.NodeType.PACKAGE_NODE);
        tree.insert("Node A.1", "Node D.4", NAryTree.NodeType.PACKAGE_NODE);
        tree.insert("Node D", "Node E", NAryTree.NodeType.PACKAGE_NODE);
        tree.insert("Node E", "Node F", NAryTree.NodeType.PACKAGE_NODE);
        tree.insert("Node G", "Node F", NAryTree.NodeType.PACKAGE_NODE);
        tree.insert("Node A.1", "Node D.3", NAryTree.NodeType.DELIVERABLE_NODE);
        tree.insert("Node A", "Node A.52 project", NAryTree.NodeType.PACKAGE_NODE);

        tree.insertDerivableNode("Node A", "Final node", "This is the content of the file");
        tree.insertDerivableNode("Node A", "this is a try", "this is another file");


        System.out.println(tree);
        System.out.println("value " + tree.insert("Node A", "Node A.32", NAryTree.NodeType.PACKAGE_NODE));
        System.out.println("value " + tree.insert("Node A", "Node A.32", NAryTree.NodeType.PACKAGE_NODE));
        System.out.println("value " + tree.insertPackageNode("Node A", "Node A.321"));
        System.out.println("value " + tree.insertDerivableNode("Node A", "Node A.321", "content"));
        System.out.println("value " + tree.insertPackageNode("Node A", "Node A.321"));
        System.out.println(tree);

        System.out.println(tree.filter(new ILinkedIFilter<TreeNode>() {
            @Override
            public boolean isValid(TreeNode node) {
                return node.getValue().equals("Node C");
            }
        }));

        System.out.println("Find: ");
        System.out.println(tree.find("Node A")); // Node A will be printed out
        System.out.println(tree.deleteNode("Node A")); // Node A is being removed
        System.out.println(tree.find("Node A")); // Null
        System.out.println(tree); // Node A and children should not appear in the tree
        System.out.println(tree.find("Node A.1")); // Should return false

        System.out.println("\ninorder");
        tree.inorder(new ILinkedHelper<TreeNode>() {
            @Override
            public void handle(TreeNode node) {
                System.out.println(node.getValue());
            }
        });

        System.out.println("\npreorder: ");
        tree.preorder(new ILinkedHelper<TreeNode>() {
            @Override
            public void handle(TreeNode node) {
                System.out.println(node.getValue());
            }
        });

        System.out.println("\npostorder");
        tree.postorder(new ILinkedHelper<TreeNode>() {
            @Override
            public void handle(TreeNode node) {
                System.out.println(node.getValue());
            }
        });

        System.out.println(tree.depth());
        System.out.println(tree.getTotalDeliverableNode());
        System.out.println(tree.getTotalPackagesNode());


        // Calculates nodes with a single deliverable node
        LinkedList<TreeNode> nodes = new LinkedList<>();
        final int[] counter = {0};
        tree.forEachNode(new ILinkedHelper<TreeNode>() {
            @Override
            public void handle(TreeNode node) {
                counter[0] = 0;
                node.forEachChild(new ILinkedHelper<TreeNode>() {
                    @Override
                    public void handle(TreeNode child) {
                        if (NAryTree.isNotPackageInstance(child)) {
                            counter[0] += 1;
                        }
                    }
                });

                if (counter[0] == 1) {
                    nodes.insert(node);
                }
            }
        });
        for (ListNode<TreeNode> node : nodes) {
            System.out.println(node.getValue().getValue());
        }
        System.out.println(nodes.size());

        tree.toFile(new File("my-project.txt"));
    }
}
