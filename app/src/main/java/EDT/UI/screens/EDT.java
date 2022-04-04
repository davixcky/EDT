package EDT.UI.screens;

import EDT.services.data.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

public class EDT {
    private JButton addNodeBtn;

    private JTree tree, previewTree;
    DefaultTreeModel treeModel;

    private JPanel mainContainer;
    private JPanel leftContainer, centerContainer;

    private NAryTree dataTree;

    private JMenuBar mainMenuBar;


    // Nodes input
    private JComboBox<String> parentValuesComboBox;
    private JComboBox<String> nodeType;
    private TextField nodeInputValue;
    private String currentParentValue = "EDT";

    private LinkedList<DefaultMutableTreeNode> mutablesNodes;

    /*
    * Menu:
    *   File:
    *       Export
    *   Reports:
    *       Generate
    *   Traversal:
    *       Preorder
    *       Postorder
    *       Inorder
    * */

    public EDT() {
        dataTree = new NAryTree();

        mutablesNodes = new LinkedList<>();

        tree = new JTree();
        previewTree = new JTree();

        tree.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        DefaultMutableTreeNode n = new DefaultMutableTreeNode("EDT");
        treeModel = new DefaultTreeModel(n);
        tree.setModel(treeModel);
        DefaultMutableTreeNode Fake = new DefaultMutableTreeNode("");
        treeModel.insertNodeInto(Fake, n, n.getChildCount());
        dataTree.setTitle("EDT");

        previewTree.setModel(treeModel);

        mutablesNodes.insert(n);
//        treeModel = new DefaultTreeModel(new DefaultMutableTreeNode("EDT"));
//
//
//        tree = new JTree();
//
//        dataTree.setTitle("EDT");
//        tree.setModel(treeModel);

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                parentValuesComboBox.removeAllItems();
//                parentValuesComboBox.addItem(e.getPath().getLastPathComponent().toString());
                System.out.println("path " + e.getPath().getLastPathComponent().toString());
//                parentValuesComboBox.setSelectedItem(e.getPath().getLastPathComponent().toString());

                currentParentValue = e.getPath().getLastPathComponent().toString();
//                System.out.println("selected item " + parentValuesComboBox.getSelectedItem());
                parentValuesComboBox.setSelectedItem(currentParentValue);
                parentValuesComboBox.addItem(currentParentValue);

            }
        });


        initContainers();
        initComponents();
        initMenu();
    }

    private void initMenu() {
        mainMenuBar = new JMenuBar();

        initFileSubMenu();
        initReportsSubMenu();
    }

    private void initFileSubMenu() {
        JMenu fileMenu = new JMenu("Files");

        JMenuItem export = new JMenuItem("Export");
        export.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Launch fileChooser and call treeData.toFile()
                System.out.println("Exporting file");
            }
        });

        fileMenu.add(export);
        mainMenuBar.add(fileMenu);
    }

    private void initReportsSubMenu() {
        JMenu reportsMenu = new JMenu("Reports");

        JMenuItem generate = new JMenuItem("Generate");
        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Call counter, depth and nodes with a single deliverable
            }
        });

        reportsMenu.add(generate);
        mainMenuBar.add(reportsMenu);
    }

    private void initContainers() {
        mainContainer = new JPanel();
        leftContainer = new JPanel();
        centerContainer = new JPanel();

        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.LINE_AXIS));
        leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.LINE_AXIS));
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));

        mainContainer.setBackground(Color.black);
        leftContainer.setBackground(Color.red);
        centerContainer.setBackground(Color.blue);

        leftContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainContainer.add(leftContainer);
        mainContainer.add(Box.createRigidArea(new Dimension(5,0)));
        mainContainer.add(centerContainer);
    }

    private void initComponents() {
        initLeftContainer();
        initCenterContainer();
    }

    private void initLeftContainer() {
        addNodeBtn = new JButton("Save");
        addNodeBtn.addActionListener(this::handleNodeSaved);

        leftContainer.add(previewTree);
        leftContainer.add(addNodeBtn);
    }

    private void initCenterContainer() {
        initNodeTypeCombobox();

        parentValuesComboBox = new JComboBox<>();
        parentValuesComboBox.addItem("EDT");

        setPopupComponent(parentValuesComboBox, tree, 400, 200);


        nodeInputValue = new TextField();

        centerContainer.add(Box.createVerticalGlue());
        centerContainer.add(parentValuesComboBox);
        centerContainer.add(nodeType);
        centerContainer.add(nodeInputValue);
        centerContainer.add(Box.createVerticalGlue());
    }

    public static void setPopupComponent(JComboBox<?> combo, Component comp, int widthIncr, int heightIncr) {
        final ComboPopup popup = (ComboPopup) combo.getUI().getAccessibleChild(combo, 0);
        if (popup instanceof Container) {
            final Container c = (Container) popup;
            c.removeAll();
            c.setLayout(new GridLayout(1, 1));
            c.add(comp);
            final Dimension size = comp.getPreferredSize();
            size.width += widthIncr;
            size.height += heightIncr;
            c.setPreferredSize(size);
        }
    }

    private void handleNodeSaved(ActionEvent actionEvent) {
        NAryTree.NodeType nodeType = getNodeType();
        String parentValue = parentValuesComboBox.getSelectedItem().toString(); // TODO: Replace by dropdown value
        String currentValue = nodeInputValue.getText(); // TODO: Replace by node value

        // Reset fields
        nodeInputValue.setText("");

        System.out.println("Parent " + parentValue);
        System.out.println("Current " + currentValue);

        boolean isNodeInserted = dataTree.insert(parentValue, currentValue, nodeType);
        if (!isNodeInserted) {
            // TODO: Node was already inserted in the tree
            return;
        }

        // TODO: Insert node in GUI Tree
        // TODO: Handle when tree tries to insert in a deliverable node
        DefaultMutableTreeNode n = new DefaultMutableTreeNode(currentValue);
        DefaultMutableTreeNode parent = new DefaultMutableTreeNode(parentValue);
        DefaultMutableTreeNode findParent = mutablesNodes.find(new ILinkedHelper<DefaultMutableTreeNode>() {
            @Override
            public boolean compare(DefaultMutableTreeNode a, DefaultMutableTreeNode b) {
                return a.toString().equals(b.toString());
            }
        }, parent);
        treeModel.insertNodeInto(n, findParent, findParent.getChildCount());
        treeModel.reload();
        System.out.println(dataTree);

        mutablesNodes.insert(n);

        System.out.println("root tree " + ((DefaultMutableTreeNode) treeModel.getRoot()).toString());

        if (nodeType == NAryTree.NodeType.PACKAGE_NODE) {
            // Exit cause there is not a file handling required for a package node
            return;
        }

        // TODO: Handle file content upload and update the node with the file value
        LinkedList<TreeNode> nodes = new LinkedList<>();
        dataTree.filter(new ILinkedIFilter<TreeNode>() {
            @Override
            public boolean isValid(TreeNode node) {
                return node.getParentValue().equals(parentValue) && node.getValue().equals(currentValue);
            }
        });

        DeliverableTreeNode node = (DeliverableTreeNode) nodes.getAt2(0);
        if (node == null) {
            // TODO: This will not happen cause the node was inserted in the previous case
            return;
        }

        String fileContent = "file content";
        node.setFileContent(fileContent);
    }

    private NAryTree.NodeType getNodeType() {
        switch (nodeType.getSelectedItem().toString().toLowerCase(Locale.ROOT)) {
            case "paquete": return NAryTree.NodeType.PACKAGE_NODE;
            case "entregable": return NAryTree.NodeType.DELIVERABLE_NODE;
        }

        return NAryTree.NodeType.PACKAGE_NODE;
    }

    private void initNodeTypeCombobox() {
        nodeType = new JComboBox<>();
        nodeType.addItem("Paquete");
        nodeType.addItem("Entregable");
    }

    public Container getMainContainer() {
        return mainContainer;
    }

    public JMenuBar getMainMenuBar() {
        return mainMenuBar;
    }
}
