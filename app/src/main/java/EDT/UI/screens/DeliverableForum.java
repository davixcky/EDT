package EDT.UI.screens;

import EDT.services.data.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Consumer;

public class DeliverableForum extends JPanel {

    private Graph graph;
    private Color background = new Color(39, 39, 42);
    private Color foreground = new Color(230, 230, 230);
    private NAryTree tree;
    private JLabel durLabel;
    private JLabel deliverLabel;
    private JLabel costLabel;
    private JLabel dependencyLabel;
    private JLabel dateLabel;
    private JLabel status;
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
    private GraphNode start;
    private boolean alreadyfirst = false;

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
        this.add(status);
        status.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        status.setPreferredSize(new Dimension(100, 28));
        btnsBehaviour();
        fillPane(datePane, dateLabel, dateField, dateBtn);
        dateField.setToolTipText("Please type the starting date (From the following day or upper) in the following format dd/MM/yyyy");
        this.add(Box.createVerticalStrut(290));
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
        if (alreadyfirst == false) {
            dependencyCombo.addItem("--");
        }
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
        addBtn.setPreferredSize(new Dimension(100, 20));
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean check = true;
                boolean dependencyCheck = true;

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
                    try {
                        graph.addDependency(id2, id);
                    } catch (Exception ex) {
                        dependencyCheck = false;
                    }
                    if (dependencyCheck) {
                        status.setText("Succesfully added");
                        costField.setText("");
                        durField.setText("");
                    } else {
                        status.setText("");
                        JOptionPane.showMessageDialog(null, "The desired dependency already exists or generates a cycle", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } else if (check && dependency.equalsIgnoreCase("--")) {
                    if (start == null) {
                        float dur = Float.parseFloat(durField.getText());
                        double cost = Double.parseDouble(costField.getText());
                        dur = durationProcessing(dur);
                        GraphNodeID id = graph.addNode(new GraphNode(tree.find((String) deliverables.getSelectedItem()), cost, dur));
                        status.setText("Succesfully added");
                        start = graph.getVertex((String) deliverables.getSelectedItem());
                        alreadyfirst = true;
                    } else {
                        status.setText("");
                        JOptionPane.showMessageDialog(null, "You've already added this", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Hey, you have some wrong inputs", "Error", JOptionPane.ERROR_MESSAGE);
                    status.setText("");
                }

            }
        });
        dateBtn.setFocusPainted(false);
        dateBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);
        dateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean approved = true;

                try {
                    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateField.getText());
                    datesCalc(date);
                    if (!isdateValid(date)) {
                        throw new Exception();
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                    approved = false;
                }
                if (approved && start != null) {
                    status.setText("Schedule created successfully");
//                    GraphNode test = graph.getVertex((String) deliverables.getSelectedItem());
//                    if (test != null) {
//                        System.out.println(test.getValue().getValue());
//                        System.out.println("Last date " + test.getDate().toString());
//                    }
//                    System.out.println(graph.getTotalCost());
//                    System.out.println(graph.getTotalDuration() + " Days");
                    graph.getVertexList().forEach(new Consumer<ListNode<GraphNode>>() {
                        @Override
                        public void accept(ListNode<GraphNode> graphNodeListNode) {
                            System.out.println(graphNodeListNode.getValue().getValue().getValue());
                            System.out.println(graphNodeListNode.getValue().getDate());
                        }
                    });
                } else if (start == null) {
                    JOptionPane.showMessageDialog(null, "Something is wrong with the date", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Can't find an independent deliverable to start", "Error", JOptionPane.ERROR_MESSAGE);
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

    public void fillPane(JPanel pane, JLabel label, JTextField field, JButton button) {
        this.add(pane);
        pane.setLayout(new FlowLayout());
        pane.add(label);
        pane.add(field);
        pane.add(button);
        button.setPreferredSize(new Dimension(130, 20));
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
        for (Component c : pane.getComponents()) {
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
        //UIManager.put("ComboBox.selectionBackground", foreground);
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
        status = new JLabel("");
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

    public boolean isdateValid(Date date) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        if (c.getTime().before(new java.util.Date())) {
            return false;
        }
        return true;
    }


    public void datesCalc(Date date) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);

        if (start.getDependencies() != null) {
            start.setDate(c.getTime());
            dateAssigner(c, start);
//            start.getDependencies().forEach(new Consumer<ListNode<GraphNode>>() {
//                @Override
//                public void accept(ListNode<GraphNode> graphNodeListNode) {
//                    graphNodeListNode.getValue().setDate(c.getTime());
//                    c.add(Calendar.DAY_OF_YEAR, (int) graphNodeListNode.getValue().getDuration());
//                }
//            });
            c.setTime(date);
            graph.getVertexList().forEach(new Consumer<ListNode<GraphNode>>() {
                @Override
                public void accept(ListNode<GraphNode> graphNodeListNode) {
                    commonDates(graphNodeListNode.getValue());
                    dateAssigner(c, graphNodeListNode.getValue());
                }
            });

        }
    }

    public void dateAssigner(GregorianCalendar c, GraphNode g) {
        if (g.getDependencies().getAt(0) != null) {
            g.setDate(c.getTime());
            c.add(Calendar.DAY_OF_YEAR, (int) g.getDuration());
            dateAssigner(c, g.getDependencies().getAt(0).getValue());
        } else {
            g.setDate(c.getTime());
        }
    }

    public void commonDates(GraphNode g) {
        if (g.getDependencies() != null) {
            g.getDependencies().forEach(new Consumer<ListNode<GraphNode>>() {
                @Override
                public void accept(ListNode<GraphNode> graphNodeListNode) {
                    graphNodeListNode.getValue().setDate(g.getDependencies().getAt(0).getValue().getDate());
                }
            });
        }
    }

    /**
     * The following method returns true in the given GraphNode isn't in any of
     * the other's node dependency list
     */
    public boolean neverDependency(GraphNode objective) {

        LinkedList<GraphNode> havedependency = graph.getVertexList().filter(new ILinkedIFilter<GraphNode>() {
            @Override
            public boolean isValid(GraphNode value) {
                return (value.getDependencies() != null);
            }
        });
        GraphNode searchNode = havedependency.find(new ILinkedHelper<GraphNode>() {
            @Override
            public boolean compare(GraphNode a, GraphNode b) {
                return (a.getDependencies().find(new ILinkedHelper<GraphNode>() {
                    @Override
                    public boolean compare(GraphNode a, GraphNode b) {
                        return a.getValue().getValue().equals(b.getValue().getValue());
                    }
                }, b) != null);

            }
        }, objective);
        if (searchNode == null) {
            return true;
        }
        return false;
    }

}
