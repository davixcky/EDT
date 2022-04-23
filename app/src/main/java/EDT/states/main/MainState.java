package EDT.states.main;

import EDT.app.Handler;
import EDT.ui.UIButton;
import EDT.ui.UIObject;
import EDT.gfx.Assets;
import EDT.gfx.ContentLoader;
import EDT.states.State;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MainState extends State {

    private UIButton profileBtn;

    public static final String STATE_NAME = "MAIN_STATE";

    public MainState(Handler handler) {
        super(STATE_NAME, handler, "Home");
    }

    @Override
    protected void initComponents() {
        BufferedImage profileImage = ContentLoader.loadImage("/ui/icons/app.png");

        // Right upper corner
        profileBtn = new UIButton(this, 0, 0, 60, 60, profileImage, () -> {
            System.out.println("example");
        });

        uiManager.addObjects(profileBtn);

        resizeComponents();
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics g) {
        int y = (int) (currentDimension.width * 0.2f);

        Dimension titleDimensions = UIObject.drawString(g, "JIRA SOFTWARE",
                currentDimension.width / 2,
                y,
                true,
                Color.white,
                Assets.getFont(Assets.FontsName.SPACE_MISSION, (int) (currentDimension.width * 0.09f)));

        UIObject.drawString(g, "Developed by OBX TEAM",
                currentDimension.width / 2,
                y + (int) (titleDimensions.height * 0.5f),
                true,
                Color.white,
                Assets.getFont(Assets.FontsName.SLKSCR, (int) (titleDimensions.width * 0.04f)));

        uiManager.render(g);
    }

    @Override
    protected void resizeComponents() {
        // Update buttons dimensions if change
        profileBtn.updateCoordsBounds(new Rectangle(currentDimension.width - profileBtn.getWidth() - 20, 20, profileBtn.getWidth(), profileBtn.getHeight()));
    }
}
