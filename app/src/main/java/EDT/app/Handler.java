package EDT.app;

import java.awt.*;

public class Handler {

    private App app;

    public Handler(App app) {
        this.app = app;
    }

    public App getApp() {
        return app;
    }

    public Dimension boardDimensions() {
        return app.getWindowSize();
    }

}
