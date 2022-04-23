package EDT.UI.components;

import EDT.UI.UIObject;
import EDT.states.State;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class UIBufferedObject extends UIObject {

    private BufferedImage bufferedObject;
    private Graphics2D g2;

    public UIBufferedObject(State parent, float x, float y, int width, int height) {
        super(parent, x, y, width, height);

        bufferedObject = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2 = bufferedObject.createGraphics();
    }

    public void init() {
        g2 = bufferedObject.createGraphics();
        create(g2);
        g2.dispose();
    }

    @Override
    public void updateCoordsBounds(Rectangle newValue) {
        super.updateCoordsBounds(newValue);
        init();
    }

    protected abstract void create(Graphics2D graphics2D);

    @Override
    public void render(Graphics g) {
        g.drawImage(bufferedObject, (int) x, (int) y, null);
    }
}
