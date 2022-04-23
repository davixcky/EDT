package EDT.graphics;

import EDT.gfx.ContentLoader;
import EDT.input.KeyManager;
import EDT.input.MouseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Display {

    private JFrame frame;
    private Canvas mainCanvas;

    private final String title;
    private Dimension windowSize;

    private final DisplayController controller;

    public Display(DisplayController controller, String title, Dimension windowSize) {
        this.title = title;
        this.windowSize = windowSize;
        this.controller = controller;

        createDisplay();
    }

    public void start() {
        frame.setVisible(true);
    }

    private void createDisplay() {
        createFrame();
        createCanvas();
    }

    private void createFrame() {
        frame = new JFrame(title);
        setupAppIcon();

        frame.setSize(windowSize);
        frame.setLocationRelativeTo(null);

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component)evt.getSource();
                System.out.printf("w: %d, h: %d\n", c.getBounds().width, c.getBounds().height);
                controller.onResize(c.getBounds().getSize());
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                controller.stop();
            }
        });

    }

    private void createCanvas() {
        mainCanvas = new Canvas();
        mainCanvas.setPreferredSize(windowSize);
        mainCanvas.setMaximumSize(windowSize);
        mainCanvas.setMinimumSize(windowSize);
        mainCanvas.setFocusable(true);

        frame.add(mainCanvas);
        frame.pack();
    }

    private void setupAppIcon() {
        ArrayList<BufferedImage> images = new ArrayList<>();
        images.add(ContentLoader.loadImage("/ui/icons/app16.png"));
        images.add(ContentLoader.loadImage("/ui/icons/app32.png"));
        images.add(ContentLoader.loadImage("/ui/icons/app96.png"));

        frame.setIconImages(images);
    }

    public void setInputListeners(KeyManager keyManagerListener, MouseManager mouseManagerListener) {
        addKeyListener(keyManagerListener);
        addMouseListener(mouseManagerListener);
    }

    public void addKeyListener(KeyManager l) {
        frame.addKeyListener(l);
        mainCanvas.addKeyListener(l);
    }

    public void addMouseListener(MouseManager l) {
        frame.addMouseListener(l);
        frame.addMouseMotionListener(l);
        mainCanvas.addMouseListener(l);
        mainCanvas.addMouseMotionListener(l);
        mainCanvas.addMouseWheelListener(l);
        frame.addMouseWheelListener(l);
    }

    public void setTitle(String title) {
        frame.setTitle(title);
    }

    public JFrame getFrame() {
        return frame;
    }

    public Canvas getMainCanvas() {
        return mainCanvas;
    }

    public void close() {
        frame.dispose();
    }
}
