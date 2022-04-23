package EDT.app;

import EDT.gfx.Assets;
import EDT.gfx.ContentLoader;
import EDT.graphics.Display;
import EDT.graphics.DisplayController;
import EDT.input.KeyManager;
import EDT.input.MouseManager;
import EDT.states.State;
import EDT.states.main.MainState;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class App implements Runnable, DisplayController {

    public static final int TICKSPERS = 120;
    public static final boolean ISFRAMECAPPED = false;
    private final KeyManager keyManager;
    private final MouseManager mouseManager;
    public int ticks;
    private boolean running = false;
    private Thread gameThread;
    private Display display;
    private Dimension windowSize = new Dimension(1080, 720);
    private Image background;

    private final String title;

    public App(String title) {
        Assets.init();

        this.title = title;

        keyManager = new KeyManager();
        mouseManager = new MouseManager();
    }

    private void init() {
        display = new Display(this, title, windowSize);
        display.setInputListeners(keyManager, mouseManager);

        changeBackground("/backgrounds/main.jpg");
        initStates();

        State.goTo(MainState.STATE_NAME);

        display.start();
    }

    private void initStates() {
        new MainState(new Handler(this));
    }

    public synchronized void start() {
        // Exit if the app is already running
        if (running)
            return;

        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public synchronized void stop() {
        // Exit if the game is not running
        if (!running)
            return;

        running = false;
        try {
            System.exit(0);
            display.close();
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResize(Dimension newDimensions) {
        this.windowSize = newDimensions;

        if (State.getCurrentState() != null)
            State.getCurrentState().updateDimensions(newDimensions);
    }

    private void update() {
        // Set mouse and key listeners
        keyManager.update();

        if (State.getCurrentState() != null) {
            State.getCurrentState().update();
            display.setTitle(String.format("%s | %s", title, State.getCurrentNavigationTitle()));
        }

    }

    private void render() {
        BufferStrategy bs = display.getMainCanvas().getBufferStrategy();
        if (bs == null) {
            display.getMainCanvas().createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.clearRect(0, 0, windowSize.width, windowSize.height);
        g.drawImage(background, 0, 0, windowSize.width, windowSize.height, null);

        if (State.getCurrentState() != null)
            State.getCurrentState().render(g);

        bs.show();
        g.dispose();
    }


    @Override
    public void run() {
        init();
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / TICKSPERS;
        ticks = 0;
        long fpsTimer = System.currentTimeMillis();
        double delta = 0;
        boolean shouldRender;
        while (running) {
            shouldRender = !ISFRAMECAPPED;
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            while (delta >= 1) {
                ticks++;
                update();
                delta -= 1;
                shouldRender = true;
            }

            if (shouldRender) {
                render();
            }

            if (fpsTimer < System.currentTimeMillis() - 1000) {
                ticks = 0;
                fpsTimer = System.currentTimeMillis();
            }
        }
    }

    public Dimension getWindowSize() {
        return windowSize;
    }

    public void changeBackground(String path) {
        background = new ImageIcon(ContentLoader.loadImage(path)).getImage();
    }

    public MouseManager getMouseManager() {
        return mouseManager;
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }
}
