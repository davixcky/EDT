package EDT.UI;

import EDT.states.State;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class UIInput extends UIObject {

    private boolean enterPressed;
    private StringBuilder text;
    private ActionListener listener;
    private int minChar, maxChar;

    public UIInput(State parent, float x, float y) {
        super(parent, x, y, UIButton.btnImage.getWidth(), UIButton.btnImage.getHeight());

        enterPressed = false;
        text = new StringBuilder();

        minChar = 4;
        maxChar = 10;
    }

    public UIInput(State parent) {
        this(parent, 0, 0);
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(UIButton.btnImage, (int) x, (int) y, null);
        drawString(g, text.toString(),
                (int) x + UIButton.btnImage.getWidth() / 2,
                (int) y + 11,
                true,
                Color.white,
                new Font(Font.SANS_SERIF, Font.PLAIN, 13));
    }

    @Override
    public void onClick() {
        enterPressed = false;
    }

    @Override
    public void onMouseChanged(MouseEvent e) {
        Rectangle newBounds = new Rectangle(bounds.x - 20, bounds.y - 20, bounds.width + 40, bounds.height + 40);
        hovering = newBounds.contains(e.getX(), e.getY());
    }

    @Override
    public void onObjectDragged(MouseEvent e) {
    }

    @Override
    public void onObjectKeyPressed(KeyEvent e) {
        if (!enterPressed) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_BACK_SPACE -> {
                    deleteLastChar();
                    return;
                }
                case KeyEvent.VK_ENTER -> {
                    enterPressed();
                    return;
                }
            }

            if (text.length() < maxChar)
                text.append(e.getKeyChar());
        }
    }

    private void deleteLastChar() {
        if (text.length() > 0) {
            text.deleteCharAt(text.length() - 1);
        }
    }

    private void enterPressed() {
        if (text.length() < minChar)
            return;

        if (listener != null)
            listener.actionPerformed();
        enterPressed = true;
    }

    public void setCharLimits(int minChar, int maxChar) {
        this.minChar = minChar;
        this.maxChar = maxChar;
    }

    public String getValue() {
        String value = text.toString();
        enterPressed = false;
        text.setLength(0);
        return value;
    }

    public int getValueAsInteger() {
        String literalValue = getValue();
        int integerValue = -1;
        try {
            integerValue = Integer.parseInt(literalValue);
        } catch (Exception e) {
            System.out.println(literalValue + " is not a valid integer input value");
            // TODO: Handle literal value when is not a string
        }

        return integerValue;
    }

    public String getText() {
        return text.toString();
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }
}
