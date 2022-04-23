package EDT.states;


import EDT.app.Handler;
import EDT.UI.UIManager;

import java.awt.*;
import java.util.HashMap;

public abstract class State {

    private static State currentState = null;

    protected Handler handler;
    protected UIManager uiManager;

    protected Dimension currentDimension;
    protected String navigationTitle;

    private static final HashMap<String, State> statesRouting = new HashMap<>();

    public State(String key, Handler handler, String navigationTitle) {
        this.handler = handler;

        currentDimension = handler.boardDimensions();
        uiManager = new UIManager();
        statesRouting.put(key, this);
        this.navigationTitle = navigationTitle;

        System.out.printf("STATE: Registering a new state %s\n", key);
    }

    public static State getCurrentState() {
        return currentState;
    }

    public static void setCurrentState(State state) {
        if (currentState != null)
            currentState.stop();

        currentState = state;
        state.setUiManager();
        state.start();
    }

    protected void setUiManager() {
        handler.getApp().getMouseManager().setUIManager(uiManager);
        handler.getApp().getKeyManager().setUIManager(uiManager);
    }

    public UIManager getUiManager() {
        return uiManager;
    }

    protected void start() {
        initComponents();
    }

    protected void stop() {
        if (currentState != null)
            System.out.println("Stopping " + currentState);
    }

    public Dimension getDimension() {
        return currentDimension;
    }

    public void updateDimensions(Dimension newDimensions) {
        currentDimension = newDimensions;
        resizeComponents();
    }

    public static void goTo(String stateKey) {
        if (!statesRouting.containsKey(stateKey)) {
            throw new RuntimeException("the state " + stateKey + " is not defined");
        }

        System.out.printf("STATE: Moving to %s\n", stateKey);
        State nextState = statesRouting.get(stateKey);
        setCurrentState(nextState);

        // TODO: Fix resizing when switching between tabs
    }

    public static String getCurrentNavigationTitle() {
        return currentState.navigationTitle;
    }

    protected abstract void initComponents();

    public abstract void update();

    public abstract void render(Graphics g);

    protected abstract void resizeComponents();
}
