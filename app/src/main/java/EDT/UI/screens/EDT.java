package EDT.UI.screens;

import EDT.services.data.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;

public class EDT {
    private final JTree tree;
    private final JTree previewTree;
    private final NAryTree dataTree;
    private final LinkedList<DefaultMutableTreeNode> mutablesNodes;
    DefaultTreeModel treeModel;
    private JButton addNodeBtn;
    private JPanel mainContainer;
    private JPanel leftContainer, centerContainer;
    private JMenuBar mainMenuBar;
    private JFileChooser fileChooser;

    // Nodes input
    private JComboBox<String> parentValuesComboBox;
    private JComboBox<String> nodeType;
    private TextField nodeInputValue;
    private String currentParentValue = "EDT";

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
        String title = dataTree.getTitle();

        mutablesNodes = new LinkedList<>();

        tree = new JTree();
        previewTree = new JTree();

        tree.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        DefaultMutableTreeNode firstNode = new DefaultMutableTreeNode(title);
        DefaultMutableTreeNode emptyNode = new DefaultMutableTreeNode("");

        treeModel = new DefaultTreeModel(firstNode);
        tree.setModel(treeModel);
        previewTree.setModel(treeModel);
        treeModel.insertNodeInto(emptyNode, firstNode, firstNode.getChildCount());

        mutablesNodes.insert(firstNode);

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                try {
                    parentValuesComboBox.removeAllItems();
                    currentParentValue = e.getPath().getLastPathComponent().toString();
                    parentValuesComboBox.setSelectedItem(currentParentValue);
                    parentValuesComboBox.addItem(currentParentValue);
                    parentValuesComboBox.getUI().setPopupVisible(parentValuesComboBox, false);
                } catch (Exception ignored) {
                }
            }
        });


        initContainers();
        initComponents();
        initMenu();
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
                fileExportHandler();
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
        mainContainer.add(Box.createRigidArea(new Dimension(5, 0)));
        mainContainer.add(centerContainer);
    }

    private void initComponents() {
        initLeftContainer();
        initCenterContainer();

        initFileChooser();
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
        parentValuesComboBox.addItem(dataTree.getTitle());

        setPopupComponent(parentValuesComboBox, tree, 400, 200);

        nodeInputValue = new TextField();

        centerContainer.add(Box.createVerticalGlue());
        centerContainer.add(parentValuesComboBox);
        centerContainer.add(nodeType);
        centerContainer.add(nodeInputValue);
        centerContainer.add(Box.createVerticalGlue());
    }

    private void handleNodeSaved(ActionEvent actionEvent) {
        String literalType = nodeType.getSelectedItem().toString();
        String parentValue = parentValuesComboBox.getSelectedItem().toString();
        String currentValue = nodeInputValue.getText();

        // Reset fields
        nodeInputValue.setText("");

        boolean isNodeInserted;
        try {
            isNodeInserted = dataTree.insert(parentValue, currentValue, literalType);
        } catch (Exception e) {
            showMessage(e.getMessage());
            return;
        }

        if (!isNodeInserted) {
            showMessage(String.format("The package \"%s\" already contains \"%s\"", parentValue, currentValue));
            return;
        }

        DefaultMutableTreeNode newMutableTreeNode = new DefaultMutableTreeNode(currentValue);
        DefaultMutableTreeNode parent = new DefaultMutableTreeNode(parentValue);
        DefaultMutableTreeNode findParent = mutablesNodes.find(new ILinkedHelper<DefaultMutableTreeNode>() {
            @Override
            public boolean compare(DefaultMutableTreeNode a, DefaultMutableTreeNode b) {
                return a.toString().equals(b.toString());
            }
        }, parent);
        treeModel.insertNodeInto(newMutableTreeNode, findParent, findParent.getChildCount());
        treeModel.reload();
        System.out.println(dataTree);

        mutablesNodes.insert(newMutableTreeNode);

        // Exit cause there is not a file handling required for a package node
        if (literalType.equalsIgnoreCase("paquete")) {
            return;
        }

        LinkedList<TreeNode> nodes = dataTree.filter(new ILinkedIFilter<TreeNode>() {
            @Override
            public boolean isValid(TreeNode node) {
                return node.getParentValue().equals(parentValue) && node.getValue().equals(currentValue);
            }
        });

        while (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
        }

        DeliverableTreeNode node = (DeliverableTreeNode) nodes.getAt2(0);
        try {
            String fileContent = Files.readString(fileChooser.getSelectedFile().toPath());
            node.setFileContent(fileContent);
        } catch (Exception e) {
            showMessage(e.getMessage());
        }

    }

    private void initNodeTypeCombobox() {
        nodeType = new JComboBox<>();
        nodeType.addItem("Paquete");
        nodeType.addItem("Entregable");
    }

    private void initFileChooser() {
        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        updateSuggestedName();
    }

    private void updateSuggestedName() {
        fileChooser.setSelectedFile(new File(FileSystemView.getFileSystemView().getDefaultDirectory(), dataTree.getTitle() + ".txt"));
    }

    private void fileExportHandler() {
        updateSuggestedName();
        int result = fileChooser.showSaveDialog(centerContainer);
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }

        try {
            dataTree.toFile(fileChooser.getSelectedFile());
        } catch (Exception e) {
            showMessage(e.getMessage());
            return;
        }

        showMessage("Project exported correctly as " + fileChooser.getSelectedFile().getAbsolutePath());
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(mainContainer, message);
    }

    public Container getMainContainer() {
        return mainContainer;
    }

    public JMenuBar getMainMenuBar() {
        return mainMenuBar;
    }
}
