package EDT.UI.screens;

import EDT.services.data.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class DeliverableForum extends JPanel {
    private Graph graph;
    private Color background = new Color(95, 100, 103);
    private Color foreground = new Color(230, 230, 230);
    private NAryTree tree;
    private JLabel durLabel;
    private JLabel deliverLabel;
    private JLabel costLabel;
    private JLabel dependencyLabel;
    private JLabel dateLabel;
    private JPanel durPane;
    private JPanel deliverPane;
    private JPanel costPane;
    private JPanel dependencyPane;
    private JPanel datePane;
    private JTextField dateField;
    private JTextField durField;
    private JTextField costField;
    private JComboBox<String> dependencyCombo;
    private JComboBox<String> deliverables;
    private JComboBox<String> timeUnits;
    private JButton addBtn;
    private JButton dateBtn;

    DeliverableForum(NAryTree t) {
        this.tree = t;
        graph = new Graph(tree);
        this.setBackground(background);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        init();
        comboBoxItems(deliverables);
        comboBoxItems(dependencyCombo);
        dependencyCombo.removeItemAt(0);
        deliverables.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    dependencyCombo.removeAllItems();
                    comboBoxItems(dependencyCombo);
                    dependencyCombo.removeItem(deliverables.getSelectedItem());
                }
            }
        });
        fillPane(costPane, costLabel, costField);
        fillPane(deliverPane, deliverLabel, deliverables);
        fillPane(dependencyPane, dependencyLabel, dependencyCombo);
        fillPane(durPane, durLabel, durField, timeUnits);
        this.add(addBtn);
        btnsBehaviour();
        fillPane(datePane, dateLabel, dateField, dateBtn);
        dateField.setToolTipText("Please type the starting date in the following format dd/MM/yyyy");
        this.add(Box.createVerticalStrut(300));
        coloring(this);
    }

    public void updateTree(NAryTree newTree) {
        this.tree = newTree;
        this.revalidate();
        deliverables.removeAllItems();
        comboBoxItems(deliverables);
    }

    public void btnsBehaviour() {
        addBtn.setFocusPainted(false);
        addBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean check = true;

                try {
                    Float.parseFloat(durField.getText());
                    Double.parseDouble(costField.getText());
                } catch (Exception ex) {
                    check = false;
                }
                if (check) {
                    float dur = Float.parseFloat(durField.getText());
                    double cost = Double.parseDouble(costField.getText());
                    GraphNodeID id = graph.addNode(new GraphNode(tree.find((String) deliverables.getSelectedItem()), cost, dur));
                    GraphNodeID id2 = graph.addNode(new GraphNode(tree.find((String) dependencyCombo.getSelectedItem()), 0, 0));
                    graph.addDependency(id2,id);
                } else {
                    JOptionPane.showMessageDialog(null, "hey, you have some wrong inputs");
                }


            }
        });
        dateBtn.setFocusPainted(false);
        dateBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);
        dateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graph.print();
            }
        });
    }

    public void fillPane(JPanel pane, JLabel label, JTextField field) {
        this.add(pane);
        pane.setLayout(new FlowLayout());
        pane.add(label);
        pane.add(field);
        field.setPreferredSize(new Dimension(250, 20));
        label.setPreferredSize(new Dimension(100, 30));
        field.setAlignmentX(JTextField.CENTER_ALIGNMENT);
        coloring(pane);
    }

    public void fillPane(JPanel pane, JLabel label, JTextField field, JComboBox combo) {
        this.add(pane);
        pane.setLayout(new FlowLayout());
        pane.add(label);
        pane.add(field);
        pane.add(combo);
        combo.addItem("Days");
        combo.addItem("Months");
        combo.addItem("Years");
        combo.setPreferredSize(new Dimension(80, 20));
        field.setPreferredSize(new Dimension(100, 20));
        label.setPreferredSize(new Dimension(120, 30));
        field.setAlignmentX(JTextField.CENTER_ALIGNMENT);
        coloring(pane);
    }

    public void fillPane(JPanel pane, JLabel label, JTextField field, JButton button) {
        this.add(pane);
        pane.setLayout(new FlowLayout());
        pane.add(label);
        pane.add(field);
        pane.add(button);
        button.setPreferredSize(new Dimension(130, 15));
        field.setPreferredSize(new Dimension(100, 20));
        label.setPreferredSize(new Dimension(100, 30));
        field.setAlignmentX(JTextField.CENTER_ALIGNMENT);
        coloring(pane);
    }

    public void fillPane(JPanel pane, JLabel label, JComboBox combo) {
        this.add(pane);
        pane.setLayout(new FlowLayout());
        pane.add(label);
        pane.add(combo);
        combo.setPreferredSize(new Dimension(230, 20));
        label.setPreferredSize(new Dimension(120, 30));
        combo.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
        coloring(pane);
    }

    public void coloring(JPanel pane) {
        for (Component c : pane.getComponents()
        ) {
            if (c instanceof JComponent) {
                ((JComponent) c).setOpaque(false);
                coloring(c);
            }
        }
    }

    public void coloring(Component component) {
        if (!(component instanceof JButton) && !(component instanceof JComboBox<?>)) {
            component.setForeground(foreground);
        } else {
            component.setForeground(foreground);
            component.setBackground(background);
        }
    }

    public void init() {
        dependencyCombo = new JComboBox<>();
        dependencyLabel = new JLabel("Dependency: ");
        costField = new JTextField();
        costLabel = new JLabel("Cost: ");
        durField = new JTextField();
        durLabel = new JLabel("Duration: ");
        deliverLabel = new JLabel("Select deliverable: ");
        deliverables = new JComboBox<>();
        deliverPane = new JPanel();
        durPane = new JPanel();
        costPane = new JPanel();
        dependencyPane = new JPanel();
        addBtn = new JButton("Add to schedule");
        dateBtn = new JButton("Create Schedule");
        timeUnits = new JComboBox<>();
        dateField = new JTextField();
        dateLabel = new JLabel("Starting date: ");
        datePane = new JPanel();
    }

    public void comboBoxItems(JComboBox c) {
        LinkedList<TreeNode> nodes = tree.filter(new ILinkedIFilter<TreeNode>() {
            @Override
            public boolean isValid(TreeNode value) {
                return NAryTree.isNotPackageInstance(value);
            }
        });
        nodes.forEach(entregable -> {
            c.addItem(entregable.getValue().getValue());
        });
    }
}
