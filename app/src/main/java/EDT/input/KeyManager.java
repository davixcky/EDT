package EDT.input;

import EDT.ui.UIManager;
import EDT.services.data.list.linkedList.LinkedList;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class KeyManager implements KeyListener {

    private HashMap<String, LinkedList<KeyListener>> keyListeners;
    private boolean keys[];
    private UIManager uiManager;

    public KeyManager() {
        keyListeners = new HashMap<>();
        keys = new boolean[256];
    }

    public void setUIManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    public void update() {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() >= 256 || e.getKeyCode() < 0)
            return;

        LinkedList<KeyListener> listeners = keyListeners.get(Integer.toString(e.getKeyCode()));
        if (listeners != null) {
            for (KeyListener l : listeners) {
                l.typed();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() >= 256 || e.getKeyCode() < 0)
            return;

        LinkedList<KeyListener> listeners = keyListeners.get(Integer.toString(e.getKeyCode()));
        if (listeners != null) {
            for (KeyListener l : listeners) {
                l.pressed();
            }
        }

        keys[e.getKeyCode()] = true;
        if (uiManager != null)
            uiManager.onKeyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() >= 256 || e.getKeyCode() < 0)
            return;

        LinkedList<KeyListener> listeners = keyListeners.get(Integer.toString(e.getKeyCode()));
        if (listeners != null) {
            for (KeyListener l : listeners) {
                l.released();
            }
        }

        keys[e.getKeyCode()] = false;
    }

    public void addKeyListener(int keyCode, KeyListener l) {
        String key = Integer.toString(keyCode);

        if (!keyListeners.containsKey(key)) {
            keyListeners.put(key, new LinkedList<>());
        }

        LinkedList<KeyListener> listeners = keyListeners.get(key);
        listeners.add(l);
    }

    public boolean getStatusKey(int keyCode) {
        return keys[keyCode];
    }

    public interface KeyListener {
        void pressed();

        void typed();

        void released();
    }
}
