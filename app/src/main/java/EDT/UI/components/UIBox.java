package EDT.UI.components;

import EDT.states.State;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class UIBox extends UIBufferedObject {

    private final Color color;
    private final String text;

    public UIBox(State parent, float x, float y, int width, int height, Color color, String text) {
        super(parent, x, y, width, height);

        this.color = color;
        this.text = text;
        init();
    }

    @Override
    protected void create(Graphics2D g2) {
        g2.setColor(color);
        g2.fillRect(0, 0, width, height);

        g2.setColor(Color.white);
        drawString(g2, text, width / 2, height / 2, true, Color.white, new Font(Font.SERIF, Font.BOLD, 14));
    }

    @Override
    public void update() {

    }

    @Override
    public void onClick() {
    }

    @Override
    public void onMouseChanged(MouseEvent e) {

    }

    @Override
    public void onObjectDragged(MouseEvent e) {

    }

    @Override
    public void onObjectKeyPressed(KeyEvent e) {

    }
}
