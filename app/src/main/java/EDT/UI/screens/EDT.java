package EDT.UI.screens;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Objects;

public class EDT {
    private JButton addNodeBtn;
    private JComboBox<String> nodeType;
    private JLabel typeLabel;
    private JFileChooser fileChooser;

    private JTree tree;

    private Container mainContainer;
    private Container leftContainer, centerContainer;

    private INodeListener listener;

    public EDT() {
        initContainers();
        initComponents();
    }

    private void initContainers() {
        mainContainer = new Container();
        leftContainer = new Container();
        centerContainer = new Container();

        mainContainer.setLayout(new BorderLayout());
        leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.LINE_AXIS));
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));

        mainContainer.add(leftContainer, BorderLayout.LINE_START);
        mainContainer.add(centerContainer, BorderLayout.CENTER);
    }

    private void initComponents() {
        addNodeBtn = new JButton("Save");
        addNodeBtn.addActionListener(this::addNodeBtnHandler);

        typeLabel = new JLabel();
        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        fileChooser.setVisible(false);
        fileChooser.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "Only text files";
            }
        });
        fileChooser.setAcceptAllFileFilterUsed(false);

        initNodeTypeCombobox();

        centerContainer.add(nodeType);
//        centerContainer.add(typeLabel);
        centerContainer.add(fileChooser);
        leftContainer.add(addNodeBtn);

    }

    private void addNodeBtnHandler(ActionEvent actionEvent) {
        if (listener != null) {
            listener.nodeSaved(new EDTNode(nodeType.getSelectedItem().toString(), "test", "example", "lol"));
        }
    }

    private void initNodeTypeCombobox() {
        nodeType = new JComboBox<>();

        nodeType.addItem("Paquete");
        nodeType.addItem("Entregable");

        typeLabel.setText(Objects.requireNonNull(nodeType.getSelectedItem()).toString());

        nodeType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                typeLabel.setText(e.getItem().toString());
                fileChooser.setApproveButtonText("Save");

                fileChooser.setVisible(false);
                fileChooser.setDragEnabled(true);
                fileChooser.cancelSelection();
                if (typeLabel.getText().equals("Entregable")) {
                    fileChooser.setVisible(true);
                    fileChooser.setDialogTitle("Seleccionar entregable");

                    int result = fileChooser.showOpenDialog(null);
                    fileChooser.setVisible(false);
                }
            }
        });
    }

    public void addNodeListener(INodeListener nodeListener) {
        listener = nodeListener;
    }

    public Container getMainContainer() {
        return mainContainer;
    }
}
