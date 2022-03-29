package EDT.UI;

import EDT.UI.screens.EDT;

import javax.swing.*;
import java.awt.*;

public class MainDisplay {
    private final JFrame frame;
    private BorderLayout borderLayout;

    public MainDisplay(String title) {
        frame = new JFrame(title);
        borderLayout = new BorderLayout();

        initFrame();
        initComponents();
    }

    private void initFrame() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setSize(new Dimension(1080, 720));
//        frame.setLayout(borderLayout);
    }

    private void initComponents() {
        frame.add(new EDT().getMainContainer());
    }

    public void show() {
        frame.setVisible(true);
    }

}
