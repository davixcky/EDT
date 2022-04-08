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
import java.io.FileWriter;
import java.nio.file.Files;

public class EDT {
    private JTree tree;
    private final JTree previewTree;
    private final NAryTree dataTree;
    private final LinkedList<DefaultMutableTreeNode> mutablesNodes;
    DefaultTreeModel treeModel;
    private JButton addNodeBtn;
    private JPanel mainContainer;
    private JMenuBar mainMenuBar;
    private JFileChooser fileChooser;
    private TextArea textArea;
    private ButtonGroup traversalMethods;
    private String currentTraversalType = null;

    private JLabel Error;
    private JPanel edtPane;
    private JPanel edtLeftPane;
    private JPanel edtLeftPane1;
    private JPanel edtLeftPane2;
    private JPanel edtLeftPane3;
    private JLabel insertLabel;
    private JLabel nameLabel;
    private JLabel noteLabel;
    private JScrollPane jScrollPane1;
    private JTabbedPane mainPane;

    // Nodes input
    private JComboBox<String> parentValuesComboBox;
    private JComboBox<String> nodeType;
    private JTextField nodeInputValue;
    private String currentParentValue = "EDT";

    public EDT() {

        dataTree = new NAryTree();
        String title = dataTree.getTitle();

        mutablesNodes = new LinkedList<>();

        tree = new JTree();
        previewTree = new JTree();
        previewTree.setMinimumSize(new Dimension(500, 600));

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

        initiate();

        initContainers();
        initComponents();
        mainContainer.add(mainPane, BorderLayout.CENTER);
        mainPane.addTab("EDT", edtPane);
        edtPane.setLayout(new BorderLayout());
        leftEDTPane();
        graphicalTree();
        initMenu();
    }

    public void graphicalTree() {
        edtPane.add(jScrollPane1, BorderLayout.WEST);
        jScrollPane1.getViewport().add(previewTree);
        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setPreferredSize(new Dimension(300, 600));
        previewTree.setPreferredSize(new Dimension(300, 600));
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

    public void initiate() {
        parentValuesComboBox = new JComboBox<>();
        parentValuesComboBox.addItem(dataTree.getTitle());
        mainPane = new JTabbedPane();
        edtPane = new JPanel();
        nodeInputValue = new JTextField();
        jScrollPane1 = new JScrollPane();
        insertLabel = new JLabel();
        nameLabel = new JLabel();
        Error = new JLabel();
        noteLabel = new JLabel();
        edtLeftPane = new JPanel();
        edtLeftPane1 = new JPanel();
        edtLeftPane2 = new JPanel();
        edtLeftPane3 = new JPanel();
        textArea = new TextArea();
        textArea.setEditable(false);

        setPopupComponent(parentValuesComboBox, tree, 400, 200);

        addNodeBtn = new JButton();
        nodeType = new JComboBox<>();
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
                reportsHandler();
            }
        });

        reportsMenu.add(generate);
        reportsMenu.addSeparator();

        JMenu subMenu = new JMenu("Traversal method");

        traversalMethods = new ButtonGroup();
        JRadioButtonMenuItem menuItem;

        menuItem = new JRadioButtonMenuItem("Preorder");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentTraversalType != null &&currentTraversalType.equals("Preorder")) {
                    traversalMethods.clearSelection();
                    currentTraversalType = null;
                } else {
                    currentTraversalType = "Preorder";
                }
                updateTraversalArea();
            }
        });
        traversalMethods.add(menuItem);
        subMenu.add(menuItem);

        menuItem = new JRadioButtonMenuItem("Postorder");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentTraversalType != null && currentTraversalType.equals("Postorder")) {
                    traversalMethods.clearSelection();
                    currentTraversalType = null;
                } else {
                    currentTraversalType = "Preorder";
                }
                updateTraversalArea();
            }
        });
        traversalMethods.add(menuItem);
        subMenu.add(menuItem);

        menuItem = new JRadioButtonMenuItem("Inorder");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentTraversalType != null && currentTraversalType.equals("Inorder")) {
                    traversalMethods.clearSelection();
                    currentTraversalType = null;
                } else {
                    currentTraversalType = "Preorder";
                }
                updateTraversalArea();
            }
        });
        traversalMethods.add(menuItem);
        subMenu.add(menuItem);

        reportsMenu.add(subMenu);
        mainMenuBar.add(reportsMenu);
    }

    private void initContainers() {
        mainContainer = new JPanel();
        mainContainer.setLayout(new BorderLayout());
    }

    public void leftEDTPane1() {
        edtLeftPane1.setLayout(new FlowLayout());
        edtLeftPane1.add(insertLabel);
        insertLabel.setText("Insertar en: ");
        parentValuesComboBox.setPreferredSize(new Dimension(300, 30));
        parentValuesComboBox.setMinimumSize(new Dimension(300, 30));
        edtLeftPane1.add(parentValuesComboBox);
    }

    public void leftEDTPane2() {
        edtLeftPane2.setLayout(new FlowLayout());
        edtLeftPane2.add(nameLabel);
        nameLabel.setText("Nombre de paquete/entregable: ");
        nodeInputValue.setPreferredSize(new Dimension(200, 20));
        nodeInputValue.setMinimumSize(new Dimension(200, 20));
        edtLeftPane2.add(nodeInputValue);
    }

    public void leftEDTPane3() {
        edtLeftPane3.setLayout(new FlowLayout());
        edtLeftPane3.add(nodeType);
        nodeType.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
        nodeType.addItem("Paquete");
        nodeType.addItem("Entregable");
        edtLeftPane.add(addNodeBtn);
        addNodeBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);
        addNodeBtn.setText("Guardar nodo");
        addNodeBtn.addActionListener(this::handleNodeSaved);
    }

    public void leftEDTPane() {
        edtPane.add(edtLeftPane, BorderLayout.CENTER);
        edtLeftPane.setLayout(new BoxLayout(edtLeftPane, BoxLayout.PAGE_AXIS));
        edtLeftPane.add(edtLeftPane1);
        leftEDTPane1();
        edtLeftPane.add(edtLeftPane2);
        leftEDTPane2();
        edtLeftPane.add(edtLeftPane3);
        leftEDTPane3();
        edtLeftPane.add(Error);
        Error.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        Error.setForeground(Color.red);
        noteLabel.setText("Nota: Al crear un paquete se le asigna de hijo un entregable temporal sin nombre");
        edtLeftPane.add(noteLabel);
        noteLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        edtLeftPane.add(textArea);
        edtLeftPane.add(Box.createVerticalStrut(400));
    }

    private void initComponents() {
        initFileChooser();
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

        updateTraversalArea();

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

    private void updateTraversalArea() {
        textArea.setText("");

        if (currentTraversalType == null) {
            return;
        }

        switch (currentTraversalType) {
            case "Preorder":
                dataTree.preorder(new ILinkedHelper<TreeNode>() {
                    @Override
                    public void handle(TreeNode node) {
                        funcHandler(node);
                    }
                });
                break;
            case "Postorder":
                dataTree.postorder(new ILinkedHelper<TreeNode>() {
                    @Override
                    public void handle(TreeNode node) {
                        funcHandler(node);
                    }
                });
                break;
            case "Inorder":
                dataTree.inorder(new ILinkedHelper<TreeNode>() {
                    @Override
                    public void handle(TreeNode node) {
                        funcHandler(node);
                    }
                });
                break;
        }
    }

    private void funcHandler(TreeNode node) {
        textArea.setText(textArea.getText() + "\n" + node.getValue());
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
        int result = fileChooser.showSaveDialog(null);
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

    private void reportsHandler() {
        fileChooser.setSelectedFile(new File(FileSystemView.getFileSystemView().getDefaultDirectory(), dataTree.getTitle() + "_report.txt"));
        while (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
        }

        StringBuilder data = new StringBuilder();
        data.append(String.format("Reporte para [%s]:\n\n", dataTree.getTitle()));

        data.append("1. Numero de elementos (segun tipo)\n");
        data.append(String.format("\t# Paquetes       = %d\n", dataTree.getTotalPackagesNode()));
        data.append(String.format("\t# Entregables    = %d\n", dataTree.getTotalDeliverableNode()));
        data.append(String.format("\t---------- TOTAL = %d\n\n", dataTree.getSize()));

        data.append("2. Profundidad maxima\n");
        data.append(String.format("\t# Profundidad       = %d\n\n", dataTree.depth()));

        // Calculates nodes with a single deliverable node
        LinkedList<TreeNode> nodes = new LinkedList<>();
        final int[] counter = {0};
        dataTree.forEachNode(new ILinkedHelper<TreeNode>() {
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


        data.append("3. Nodos con un solo entregable\n");
        int i = 0;
        for (ListNode<TreeNode> listNode : nodes) {
            TreeNode node = listNode.getValue();
            data.append(String.format("\t%d. \"%s\" (hijo de \"%s\")\n", i++, node.getValue(), node.getParentValue()));
        }
        data.append(String.format("---Total---   %d elements\n\n", nodes.size()));

        try {
            FileWriter fileWriter = new FileWriter(fileChooser.getSelectedFile());
            fileWriter.write(data.toString());
            fileWriter.close();
        } catch (Exception e) {
            showMessage(e.getMessage());
        }

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
