package EDT.UI.screens.EDT;

import EDT.UI.Utils;
import EDT.services.data.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
    private EDTController controller;

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

    private boolean ignoreCaseSearch, equalState;

    // Nodes input
    private JComboBox<String> parentValuesComboBox;
    private JComboBox<String> nodeType;
    private JTextField nodeInputValue;
    private String currentParentValue = "EDT";

    public EDT(String edtTitle) {

        dataTree = new NAryTree();
        ignoreCaseSearch = equalState = false;

        dataTree.setTitle(edtTitle);
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
        mainPane.addTab(edtTitle, edtPane);
        edtPane.setLayout(new BorderLayout());
        leftEDTPane();
        graphicalTree();
        initMenu();

        controller = new EDTController(fileChooser, dataTree);
    }

    public void graphicalTree() {
        edtPane.add(jScrollPane1, BorderLayout.WEST);
        jScrollPane1.getViewport().add(previewTree);
        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setPreferredSize(new Dimension(300, 600));
        previewTree.setPreferredSize(new Dimension(300, 600));
    }

    private void initMenu() {
        mainMenuBar = new JMenuBar();

        initFileSubMenu();
        initReportsSubMenu();
        initActionsSubMenu();
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

        Utils.setPopupComponent(parentValuesComboBox, tree, 400, 200);

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

        JMenuItem renameProject = new JMenuItem("Rename project");
        renameProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = JOptionPane.showInputDialog(null, "Digite el nuevo nombre del proyecto");
                if (title == null) {
                    return;
                }

                // TODO: Update tree data
                dataTree.setTitle(title);
                ((DefaultMutableTreeNode) treeModel.getRoot()).setUserObject(title);
            }
        });

        fileMenu.add(export);
        fileMenu.addSeparator();
        fileMenu.add(renameProject);
        mainMenuBar.add(fileMenu);
    }

    private void initActionsSubMenu() {
        JMenu actions = new JMenu("Actions");

        JMenuItem deleteNode = new JMenuItem("Delete node");
        deleteNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nodeValue = JOptionPane.showInputDialog(null, "Digite el valor del nodo a eliminar");
                if (nodeValue == null) {
                    return;
                }

                DefaultMutableTreeNode parent = new DefaultMutableTreeNode(nodeValue);
                DefaultMutableTreeNode findParent = mutablesNodes.find(new ILinkedHelper<DefaultMutableTreeNode>() {
                    @Override
                    public boolean compare(DefaultMutableTreeNode a, DefaultMutableTreeNode b) {
                        return a.toString().equals(b.toString());
                    }
                }, parent);

                if (findParent == null) {
                    Utils.showMessage("Nodo no existe. Intente nuevamente");
                    return;
                }

                treeModel.removeNodeFromParent(findParent);
                dataTree.deleteNode(nodeValue);
            }
        });

        JCheckBoxMenuItem ignoreCase = new JCheckBoxMenuItem("Ignore case");
        ignoreCase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ignoreCaseSearch = !ignoreCaseSearch;
            }
        });
        ignoreCase.setState(true);

        JCheckBoxMenuItem equal = new JCheckBoxMenuItem("Exact node");
        equal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                equalState = !equalState;
            }
        });
        equal.setState(true);

        ignoreCaseSearch = equalState = true;

        JMenuItem searchNode = new JMenuItem("Find");
        searchNode.setAccelerator(KeyStroke.getKeyStroke('F', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        searchNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String targetNode = JOptionPane.showInputDialog(null, "Digite el valor del nodo a encontrar");
                if (targetNode == null || targetNode.equals("")) return;

                LinkedList<TreeNode> nodesSearch = dataTree.filter(new ILinkedIFilter<TreeNode>() {
                    @Override
                    public boolean isValid(TreeNode value) {
                        if (ignoreCaseSearch && equalState) {
                            return value.getValue().equalsIgnoreCase(targetNode);
                        }

                        if (ignoreCaseSearch) {
                            return value.getValue().toLowerCase().contains(targetNode);
                        }

                        if (equalState) {
                            return value.getValue().equals(targetNode);
                        }

                        return false;
                    }
                });

                System.out.println();
                System.out.println("Nodos encontrados");
                for (TreeNode node: nodesSearch) {
                    System.out.println(node.getValue());
                }
            }
        });

        JMenu searchSubMenu = new JMenu("Search");
        searchSubMenu.add(searchNode);
        searchSubMenu.addSeparator();
        searchSubMenu.add(ignoreCase);
        searchSubMenu.add(equal);

        actions.add(deleteNode);
        actions.addSeparator();
        actions.add(searchSubMenu);

        mainMenuBar.add(actions);
    }

    private void initReportsSubMenu() {
        JMenu reportsMenu = new JMenu("Reports");

        JMenuItem generate = new JMenuItem("Generate");
        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.generateFullReport();
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
        nodeType.addItem("Paquete");
        nodeType.addItem("Entregable");
        edtLeftPane.add(addNodeBtn);
        addNodeBtn.setText("Guardar nodo");
        addNodeBtn.addActionListener(this::handleNodeSaved);
        addNodeBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);
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
            Utils.showMessage(e);
            return;
        }

        if (!isNodeInserted) {
            Utils.showMessage(String.format("The package \"%s\" already contains \"%s\"", parentValue, currentValue));
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
            Utils.showMessage(e);
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
            Utils.showMessage(e);
            return;
        }

        Utils.showMessage("Project exported correctly as " + fileChooser.getSelectedFile().getAbsolutePath());
    }

    public Container getMainContainer() {
        return mainContainer;
    }

    public JMenuBar getMainMenuBar() {
        return mainMenuBar;
    }
}
