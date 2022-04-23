package EDT.graphics;

import java.awt.*;

public interface DisplayController {
    void start();
    void stop();
    void onResize(Dimension newDimensions);
}
