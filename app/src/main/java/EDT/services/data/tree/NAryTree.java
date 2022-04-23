package EDT.services.data.tree;

import EDT.exceptions.IllegalTreeNode;
import EDT.services.data.list.linkedList.ILinkedHelper;
import EDT.services.data.list.linkedList.ILinkedIFilter;
import EDT.services.data.list.linkedList.LinkedList;
import EDT.services.data.list.queue.Queue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NAryTree {
    private final Queue<TreeNode> iterationQueue;
    private final TreeNode root;
    private final LinkedList<LinkedList<String>> mapNames;

    private int totalPackagesNode, totalDeliverableNode;

    public NAryTree() {
        root = new PackageTreeNode("NO TITLE");
        iterationQueue = new Queue<>();
        mapNames = new LinkedList<>();

        totalPackagesNode = totalDeliverableNode = 0;
    }

    public static boolean isNotPackageInstance(TreeNode node) {
        return !(node instanceof PackageTreeNode);
    }

    public String getTitle() {
        return root.getValue();
    }

    public void setTitle(String title) {
        root.setValue(title);
    }

    public boolean insert(String parentValue, String value, NodeType type) {
        TreeNode newNode = getNodeInstance(type, value);
        return insert(parentValue, value, newNode);
    }

    public boolean insert(String parentValue, String value, String type) {
        NodeType nodeType = NodeType.PACKAGE_NODE;
        if (type.equalsIgnoreCase("entregable")) {
            nodeType = NodeType.DELIVERABLE_NODE;
        }

        return insert(parentValue, value, nodeType);
    }

    private boolean insert(String parentValue, String value, TreeNode node) {
        node.parentValue = parentValue;

        if (parentValue == null) {
            throw new NullPointerException();
        }

        if (value == null) {
            throw new IllegalTreeNode();
        }

        boolean isNodeInserted = internal_insert(parentValue, node);
        if (!isNodeInserted) {
            return false;
        }

        if (isNotPackageInstance(node)) {
            totalDeliverableNode += 1;
        } else {
            totalPackagesNode += 1;
        }

        return true;
    }

    public boolean insertPackageNode(String parentValue, String value) {
        return insert(parentValue, value, NodeType.PACKAGE_NODE);
    }

    public boolean insertDerivableNode(String parentValue, String value, String fileContent) {
        return insert(parentValue, value, new DeliverableTreeNode(value, fileContent));
    }

    private TreeNode getNodeInstance(NodeType type, String nodeValue) {
        return type == NodeType.DELIVERABLE_NODE ? new DeliverableTreeNode(nodeValue) : new PackageTreeNode(nodeValue);
    }

    private boolean internal_insert(String parentValue, TreeNode newNode) {
        if (root.getValue().equals(parentValue)) {
            return root.insertChild(newNode);
        }

        boolean isNodeInserted = false;
        iterationQueue.reset();
        iterationQueue.add(root);

        final int[] lastIndex = new int[1];
        if (isNotPackageInstance(newNode)) {
            mapNames.reset();
            for (int i = 0; i < root.size(); i++) {
                LinkedList<String> names = new LinkedList<>();
                names.insert(root.getValue());
                mapNames.insert(names);
            }
        }

        PackageTreeNode parentNodeCache = new PackageTreeNode(parentValue);
        while (!iterationQueue.isEmpty() && !isNodeInserted) {
            TreeNode currentTreeNode = iterationQueue.poll();

            TreeNode parentInsertion = currentTreeNode.find(new ILinkedHelper<TreeNode>() {
                @Override
                public boolean compare(TreeNode a, TreeNode b) {
                    iterationQueue.insert(a);

                    if (isNotPackageInstance(newNode)) {
                        for (int i = 0; i < mapNames.size(); i++) {
                            LinkedList<String> currentMap = mapNames.getAt(i).getValue();
                            if (currentMap.last().equals(a.getParentValue())) {
                                currentMap.insert(a.getValue());
                                lastIndex[0] = i;
                                break;
                            }
                        }
                    }

                    return a.getValue().equals(b.getValue());
                }
            }, parentNodeCache);

            if (parentInsertion != null) {
                isNodeInserted = parentInsertion.insertChild(newNode);
            }
        }

        if (!isNodeInserted) {
            return false;
        }

        if (isNotPackageInstance(newNode)) {
            ((DeliverableTreeNode) newNode).setFullPath(generateFullPath(lastIndex[0], newNode.getValue()));
        }

        return true;
    }

    private String generateFullPath(int mapIndex, String newNodeValue) {
        LinkedList<String> mapPath = mapNames.getAt2(mapIndex);
        StringBuilder literalPath = new StringBuilder();

        for (String node : mapPath) {
            literalPath.append(node).append("/");
        }

        literalPath.append(newNodeValue);
        return literalPath.toString().replace(' ', '_');
    }

    @Override
    public String toString() {
        String data = internal_toString();
        if (data == null) return "Empty";

        return data;
    }

    public void forEachNode(ILinkedHelper<TreeNode> func) {
        if (root == null) return;

        iterationQueue.reset();
        iterationQueue.add(root);
        while (!iterationQueue.isEmpty()) {
            TreeNode currentTreeNode = iterationQueue.poll();
            currentTreeNode.forEachChild(new ILinkedHelper<TreeNode>() {
                @Override
                public void handle(TreeNode node) {
                    iterationQueue.add(node);
                    func.handle(node);
                }
            });
        }
    }

    public LinkedList<TreeNode> filter(ILinkedIFilter<TreeNode> func) {
        LinkedList<TreeNode> elements = new LinkedList<>();

        forEachNode(new ILinkedHelper<TreeNode>() {
            @Override
            public void handle(TreeNode node) {
                if (func.isValid(node)) {
                    elements.insert(node);
                }
            }
        });

        return elements;
    }

    public TreeNode find(String targetValue) {
        final TreeNode[] nodeResult = {null};

        forEachNode(new ILinkedHelper<TreeNode>() {
            @Override
            public void handle(TreeNode node) {
                if (node.getValue().equals(targetValue) && nodeResult[0] == null) {
                    nodeResult[0] = node;
                }
            }
        });

        return nodeResult[0];
    }

    public boolean deleteNode(String targetNode) {
        if (targetNode == null) throw new RuntimeException("target node cannot be null");

        final boolean[] isNodeRemoved = {false};
        iterationQueue.reset();
        iterationQueue.add(root);
        while (!iterationQueue.isEmpty() && !isNodeRemoved[0]) {
            TreeNode currentTreeNode = iterationQueue.poll();
            currentTreeNode.forEachChild(new ILinkedHelper<TreeNode>() {
                @Override
                public void handle(TreeNode node) {
                    iterationQueue.add(node);

                    if (node.getValue().equals(targetNode)) {
                        if (isNotPackageInstance(node)) {
                            totalDeliverableNode--;
                        } else {
                            totalPackagesNode--;
                        }
                        currentTreeNode.deleteChild(node);
                        isNodeRemoved[0] = true;
                    }
                }
            });
        }

        return isNodeRemoved[0];
    }

    public void inorder(ILinkedHelper<TreeNode> func) {
        internal_inorder(root, func);
    }

    public void preorder(ILinkedHelper<TreeNode> func) {
        internal_preorder(root, func);
    }

    public void postorder(ILinkedHelper<TreeNode> func) {
        internal_postorder(root, func);
    }

    private void internal_inorder(TreeNode root, ILinkedHelper<TreeNode> func) {
        if (root == null) return;

        int total = root.size();
        for (int i = 0; i < total - 1; i++) {
            internal_inorder(root.getChildAt(i), func);
        }

        func.handle(root);

        internal_inorder(root.getChildAt(total - 1), func);
    }

    private void internal_preorder(TreeNode root, ILinkedHelper<TreeNode> func) {
        if (root == null) return;

        func.handle(root);
        root.forEachChild(new ILinkedHelper<TreeNode>() {
            @Override
            public void handle(TreeNode node) {
                internal_preorder(node, func);
            }
        });
    }

    private void internal_postorder(TreeNode root, ILinkedHelper<TreeNode> func) {
        if (root == null) return;

        root.forEachChild(new ILinkedHelper<TreeNode>() {
            @Override
            public void handle(TreeNode node) {
                internal_postorder(node, func);
            }
        });

        func.handle(root);
    }

    public int getTotalPackagesNode() {
        return totalPackagesNode;
    }

    public int getTotalDeliverableNode() {
        return totalDeliverableNode;
    }

    public int getSize() {
        return totalDeliverableNode + totalPackagesNode;
    }

    public int depth() {
        if (root == null) return 0;

        return internal_depth(root, 1);
    }

    private int internal_depth(TreeNode root, int depth) {
        if (root == null) return depth;

        int maxDepth = 0;
        for (int i = 0; i < root.size(); i++) {
            maxDepth = Math.max(maxDepth, internal_depth(root.getChildAt(i), depth));
        }

        return maxDepth + 1;
    }

    public void toFile(File file) throws IOException {
        if (file == null) throw new RuntimeException("file must be defined");

        String data = internal_toString();
        if (data == null) {
            data = "";
        }

        FileWriter fileWriter = new FileWriter(file);
        System.out.println(data);
        fileWriter.write(data);
        fileWriter.close();
    }

    private String internal_toString() {
        if (root == null) return null;

        StringBuilder data = new StringBuilder();

        TreeNode currentTreeNode = root;

        data.append(currentTreeNode.getValue()).append("\n");
        currentTreeNode.forEachChild(new ILinkedHelper<TreeNode>() {
            @Override
            public void handle(TreeNode node) {
                data.append("\t");
                data.append(node.toString(2)).append("\n");
            }
        });

        data.append("\n");

        return data.toString();
    }

    public enum NodeType {
        PACKAGE_NODE,
        DELIVERABLE_NODE,
    }
}
