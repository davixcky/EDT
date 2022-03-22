package EDT;

import EDT.services.data.NAryTree;

public class App {
    public static void main(String[] args) {
        NAryTree tree = new NAryTree();
        tree.setTitle("This is the title");
        tree.insert("This is the title", "Node A");
        tree.insert("This is the title", "Node B");
        tree.insert("Node A", "Node A.1");


        tree.insert("Node B", "Test");

        tree.insert("Node C", "Lol");
        tree.insert("This is the title", "Node C");
        tree.insert("Node C", "Lol");
        tree.insert("Node A.1", "Node D");
        tree.insert("Node A.1", "Node D.1");
        tree.insert("Node A.1", "Node D.4");
        tree.insert("Node D", "Node E");
        tree.insert("Node E", "Node F");
        tree.insert("Node G", "Node F");
        tree.insert("Node A.1", "Node D.3");

        System.out.println(tree);
    }
}
