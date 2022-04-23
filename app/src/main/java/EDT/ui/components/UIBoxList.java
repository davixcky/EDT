package EDT.ui.components;

import EDT.ui.UIObject;
import EDT.gfx.Assets;
import EDT.services.data.list.linkedList.LinkedList;
import EDT.states.State;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class UIBoxList extends UIBufferedObject {

    private final LinkedList<Integer> boxes;
    private String title;

    public UIBoxList(State parent, float x, float y, int width, int height) {
        super(parent, x, y, width, height);

        title = "";
        boxes = new LinkedList<>();

        init();
    }

    public void addNode(int value) {
        boxes.add(value);
        init();
    }

    public void setTitle(String newTitle) {
        title = newTitle;
        init();
    }

    public void reset() {
        boxes.clear();
        title = "";
        init();
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

    @Override
    protected void create(Graphics2D graphics2D) {
        graphics2D.clearRect(0, 0, width, height);
        graphics2D.setColor(new Color(48, 52, 63, 255));
        graphics2D.fillRect(0, 0, width, height);
        UIObject.drawString(graphics2D, title,
                100,
                20,
                true,
                Color.white,
                Assets.getFont(Assets.FontsName.SLKSCR, 14));

        int i = 0;
        int nextY = 40;
        for (int box : boxes) {
            graphics2D.setColor(Color.red);
            graphics2D.fillRect(i, nextY, 40, 40);
            UIObject.drawString(graphics2D, box + "",
                    40 / 2 + i,
                    20 + 40,
                    true,
                    Color.white,
                    Assets.getFont(Assets.FontsName.SLKSCR, 14));
            i += 50;

            if (i >= width) {
                nextY += 50;
                i = 0;
            }
        }
    }

}
