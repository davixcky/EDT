package EDT.UI.screens;

import EDT.services.data.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DeliverableForum extends JPanel {
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
    private JTextField dependencyField;
    private JComboBox<String> deliverables;
    private JComboBox<String> timeUnits;
    private JButton addBtn;

    DeliverableForum(NAryTree t) {
        this.tree = t;
        this.setBackground(background);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        init();
        comboBoxItems();
        fillPane(deliverPane, deliverLabel, deliverables);
        fillPane(costPane, costLabel, costField);
        fillPane(dependencyPane, dependencyLabel, dependencyField);
        fillPane(datePane, dateLabel, dateField);
        dateField.setToolTipText("Please type the starting date in the following format dd/MM/yyyy");
        fillPane(durPane, durLabel, durField, timeUnits);
        this.add(addBtn);
        addBtn();
        this.add(Box.createVerticalStrut(300));
        coloring(this);
    }
    public void updateTree(NAryTree newTree){
        this.tree = newTree;
        this.revalidate();
    }

    public void addBtn() {
        addBtn.setFocusPainted(false);
        addBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean check = true;
                Date date = null;
                try {
                    Float.parseFloat(durField.getText());
                    Double.parseDouble(costField.getText());
                    Integer.parseInt(dependencyField.getText());
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(dateField.getText());
                } catch (Exception ex) {
                    check = false;
                }
                if (check) {
                    float dur = Float.parseFloat(durField.getText());
                    double cost = Double.parseDouble(costField.getText());
                    int dependency = Integer.parseInt(dependencyField.getText());

                    try {
                        date = new SimpleDateFormat("dd/MM/yyyy").parse(dateField.getText());
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }

                    GraphNode node = new GraphNode((TreeNode) deliverables.getSelectedItem(), cost, dur, dependency, date);
                } else {
                    JOptionPane.showMessageDialog(null, "hey, you have some wrong inputs");
                }


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
        dependencyField = new JTextField();
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
        timeUnits = new JComboBox<>();
        dateField = new JTextField();
        dateLabel = new JLabel("Starting date: ");
        datePane = new JPanel();
    }

    public void comboBoxItems() {
        LinkedList<TreeNode> nodes = tree.filter(new ILinkedIFilter<TreeNode>() {
            @Override
            public boolean isValid(TreeNode value) {
                return NAryTree.isNotPackageInstance(value);
            }
        });
        nodes.forEach(entregable -> {
            deliverables.addItem(entregable.getValue().getValue());
        });
    }
}
