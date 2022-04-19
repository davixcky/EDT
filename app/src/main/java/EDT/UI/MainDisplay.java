package EDT.UI;

import EDT.UI.screens.EDT;
import org.w3c.dom.css.RGBColor;

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
        frame.setTitle("Proyect Management Office");
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setPreferredSize(new Dimension(800, 600));
    }

    private void initComponents() {
        String title;
        while ((title = JOptionPane.showInputDialog(null, "Digite el nombre del proyecto")) == null) {};

        EDT edtScreen = new EDT(title);
        frame.add(edtScreen.getMainContainer());
        frame.setJMenuBar(edtScreen.getMainMenuBar());
    }

    public void show() {
        frame.setVisible(true);
    }

}
