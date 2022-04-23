package EDT.ui;

import javax.swing.*;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

public class Utils {
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

    public static void showMessage(Exception e) {
        showMessage(e.getMessage());
    }

    public static void showMessage(Container mainContainer, String message) {
        JOptionPane.showMessageDialog(mainContainer, message);
    }

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
