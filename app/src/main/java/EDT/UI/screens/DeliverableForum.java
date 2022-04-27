package EDT.UI.screens;

import EDT.services.data.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Consumer;


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
        dependencyComboBehaviour();
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

    public void dependencyComboBehaviour() {
        comboBoxItems(dependencyCombo);
        dependencyCombo.removeItemAt(0);
        dependencyCombo.addItem("--");
        deliverables.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    fillDependencyCombo();
                }
            }
        });
    }

    public void fillDependencyCombo() {
        dependencyCombo.removeAllItems();
        comboBoxItems(dependencyCombo);
        dependencyCombo.addItem("--");
        dependencyCombo.removeItem(deliverables.getSelectedItem());
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
                String dependency = (String) dependencyCombo.getSelectedItem();

                if (check && !dependency.equalsIgnoreCase("--")) {
                    float dur = Float.parseFloat(durField.getText());
                    double cost = Double.parseDouble(costField.getText());
                    dur = durationProcessing(dur);
                    GraphNodeID id = graph.addNode(new GraphNode(tree.find((String) deliverables.getSelectedItem()), cost, dur));
                    GraphNodeID id2 = graph.addNode(new GraphNode(tree.find((String) dependencyCombo.getSelectedItem()), 0, 0));
                    graph.addDependency(id2, id);
                } else if (dependency.equalsIgnoreCase("--")) {
                    float dur = Float.parseFloat(durField.getText());
                    double cost = Double.parseDouble(costField.getText());
                    dur = durationProcessing(dur);
                    GraphNodeID id = graph.addNode(new GraphNode(tree.find((String) deliverables.getSelectedItem()), cost, dur));
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
                GraphNode test = graph.getVertex((String) deliverables.getSelectedItem());
                try {
                    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateField.getText());
                    System.out.println(date);
                    datesCalc(date);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                if (test != null) {
                    System.out.println(test.getValue().getValue());
                    System.out.println(test.getDate().toString());
                }
                System.out.println(graph.getTotalCost());
                System.out.println(graph.getTotalDuration() + " Days");
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

    /**
     * This method takes the input of durField and converts its units to days
     */
    public float durationProcessing(float dur) {
        switch (timeUnits.getSelectedIndex()) {
            case 1:
                dur = dur * 30;
                break;
            case 2:
                dur = dur * 12 * 30;
                break;
        }
        return dur;
    }

    public void datesCalc(Date date) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        graph.getVertexList().forEach(new Consumer<ListNode<GraphNode>>() {
            @Override
            public void accept(ListNode<GraphNode> graphNodeListNode) {
                graphNodeListNode.getValue().setDate(c.getTime());
                c.add(Calendar.DAY_OF_YEAR, (int) graphNodeListNode.getValue().getDuration());
            }
        });
    }
}
