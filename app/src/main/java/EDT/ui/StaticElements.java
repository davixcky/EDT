package EDT.ui;

import EDT.app.Handler;
import EDT.states.State;

public class StaticElements {

    public static UIButton backBtn(State parent, String previousRegisteredName) {
        UIButton backBtn = new UIButton(parent, 30, 30, () -> {
            State.goTo(previousRegisteredName);
            State.getCurrentState().updateDimensions(parent.getDimension());
        });
        backBtn.setText("BACK TO HOME");

        return backBtn;
    }

    public static UIButton exitBtn(State parent, Handler handler, float x, float y) {
        UIButton exitBtn = new UIButton(parent, x, y, UIButton.btnImage, () -> System.exit(0));
        exitBtn.setText("EXIT");
        exitBtn.setHoverText("CLOSE APP");

        return exitBtn;
    }

}
