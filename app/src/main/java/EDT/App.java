package EDT;

import EDT.services.data.NAryTree;

public class App {
    public static void main(String[] args) {
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

    }
}
