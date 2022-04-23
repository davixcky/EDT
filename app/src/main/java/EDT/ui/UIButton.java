package EDT.ui;

import EDT.gfx.Assets;
import EDT.services.data.list.linkedList.LinkedList;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class UIButton extends UIObject {

    public enum BUTTON_TYPE {
        NONE,
        DEFAULT,
        PRIMARY,
        SUCCESS,
        INFO,
        WARNING,
        DANGER,
    };

    private static final LinkedList<BufferedImage> buttonsAssets = Assets.getUiComponents(Assets.UI_ELEMENTS.BUTTONS);
    public static BufferedImage btnImage = buttonsAssets.get(0);
    public static BufferedImage btnHoverImage = buttonsAssets.get(3);
    private LinkedList<BufferedImage> images;
    private ActionListener clicker;
    private String text = null;
    private String hoverText = null;
    private boolean isCustomSize = false;
    private boolean customFontSize = false;
    private int fontSize;
    private Dimension size;
    private BUTTON_TYPE currentType;

    public UIButton(float x, float y, int width, int height, LinkedList<BufferedImage> images, ActionListener clicker) {
        super(x, y, width, height);
        this.images = images;
        this.clicker = clicker;
        isCustomSize = true;
        size = new Dimension(width, height);

        currentType = BUTTON_TYPE.NONE;
    }

    public UIButton(ActionListener clicker) {
        this(0, 0, btnImage, clicker);
    }

    public UIButton(BUTTON_TYPE buttonType, ActionListener clicker) {
        this(clicker);

        currentType = buttonType;
    }

    public UIButton(int squareSize, BufferedImage image, ActionListener clicker) {
        this(0, 0, squareSize, squareSize, image, clicker);
    }

    public UIButton(float x, float y, ActionListener clicker) {
        this(x, y, btnImage, clicker);
    }

    public UIButton(float x, float y, BufferedImage image, ActionListener clicker) {
        this(x, y, image.getWidth(), image.getHeight(), (LinkedList<BufferedImage>) null, clicker);

        images = new LinkedList<>();
        images.add(UIButton.btnHoverImage);
        images.add(image);

        this.width = image.getWidth();
        this.height = image.getHeight();

        this.clicker = clicker;
    }

    public UIButton(float x, float y, int width, int height, BufferedImage loadImage, ActionListener clicker) {
        this(x, y, width, height, new LinkedList<>(loadImage, loadImage), clicker);
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics g) {
        int currentImageIndex = hovering ? 0 : 1;
        BufferedImage currentImage = images.get(currentImageIndex);
        String currentText = hovering ? hoverText : text;

        Dimension currentDimension = new Dimension(width, height);
        int currentFontSize = customFontSize ? fontSize : (int) (currentDimension.width * 0.05f);

        int currentX = (int) (hovering ? x - 15 : x);
        int currentY = (int) (hovering ? y - 15 : y);

        int textX = (int) (currentX + currentDimension.getWidth() / 2);
        int textY = (int) (currentY + currentDimension.getHeight() / 2);


        if (currentType == BUTTON_TYPE.NONE) {
            g.drawImage(currentImage, (int) x, (int) y, (int) currentDimension.getWidth(), (int) currentDimension.getHeight(), null);
            if (text != null)
                drawString(g, currentText, textX, textY, true, Color.black, new Font(Font.SANS_SERIF, Font.PLAIN, currentFontSize));

            return;
        }

        if (!hovering) {
            g.setColor(Color.white);
            g.fillRoundRect(currentX, currentY, (int) currentDimension.getWidth(), (int) currentDimension.getHeight(), 20, 20);

        } else {
            g.setColor(Color.red);
            g.fillRoundRect(currentX, currentY, (int) currentDimension.getWidth() + 30, (int) currentDimension.getHeight() + 20, 20, 20);
        }

    }


    @Override
    public void onClick() {
        clicker.actionPerformed();
    }

    @Override
    public void onMouseChanged(MouseEvent e) {

    }

    @Override
    public void onObjectDragged(MouseEvent e) {

    }

    @Override
    public void onObjectKeyPressed(KeyEvent e) {
        System.out.println(e.getKeyChar());
    }

    public void setHoverImage(BufferedImage hoverImage) {
        images.add(0, hoverImage);
    }

    public void setText(String text) {
        this.text = text;

        if (hoverText == null)
            hoverText = text;
    }

    public void setHoverText(String hoverText) {
        this.hoverText = hoverText;

        if (text == null)
            text = hoverText;
    }

    public void setSize(Dimension size) {
        this.size = size;
        isCustomSize = true;

        bounds.width = size.width;
        bounds.height = size.height;

        this.width = size.width;
        this.height = size.height;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        this.customFontSize = true;
    }

    public void setImage(BufferedImage image) {
        this.images.add(0, image);
    }

}
