package EDT.UI;

import EDT.states.State;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public abstract class UIObject {

	protected State parent;
	protected float x, y;
	protected int width, height;
	protected Rectangle bounds;
	protected boolean hovering = false;

	protected float distanceX, distanceY;
	protected boolean movement = false;

	public UIObject(State parent, float x, float y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.parent = parent;
		bounds = new Rectangle((int) x, (int) y, width, height);
	}
	
	public abstract void update();
	
	public abstract void render(Graphics g);
	
	public abstract void onClick();

	public abstract void onMouseChanged(MouseEvent e);

	public abstract void onObjectDragged(MouseEvent e);

	public abstract void onObjectKeyPressed(KeyEvent e);

	public void onMousePressed(MouseEvent e) {
		hovering = bounds.contains(e.getX(), e.getY());

		if (hovering) {
			distanceX = e.getX() - bounds.x;
			distanceY = e.getY() - bounds.y;
			movement = true;

			parent.getUiManager().setFocusedElement(this);
		}
	}

	public void onMouseDragged(MouseEvent e) {
		if (hovering && movement && State.getCurrentState() == parent)
			onObjectDragged(e);
	}
	
	public void onMouseMove(MouseEvent e){
		hovering = bounds.contains(e.getX(), e.getY());
		if (hovering)
			onMouseChanged(e);
	}
	
	public void onMouseRelease(MouseEvent e){
		if (hovering && State.getCurrentState() == parent) {
			distanceX = distanceY = 0;
			movement = false;
			onClick();
		}
	}

	public void onKeyPressed(KeyEvent e) {
		if (parent.getUiManager().getFocusedElement() == this)
			onObjectKeyPressed(e);
	}

	public static Dimension drawString(Graphics g, String text, int xPos, int yPos, boolean center, Color c, Font font){
		Dimension size = new Dimension();

		g.setColor(c);
		g.setFont(font);
		int x = xPos;
		int y = yPos;
		if(center){
			FontMetrics fm = g.getFontMetrics(font);
			x = xPos - fm.stringWidth(text) / 2;
			y = (yPos - fm.getHeight() / 2) + fm.getAscent();
		}
		g.drawString(text, x, y);

		FontMetrics fm = g.getFontMetrics(font);
		final Rectangle2D stringBounds = fm.getStringBounds(text, g);
		return new Dimension((int) stringBounds.getWidth(), (int) stringBounds.getHeight());
	}

	public void setCoords(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isHovering() {
		return hovering;
	}

	public void setHovering(boolean hovering) {
		this.hovering = hovering;
	}

	public void updateBounds(Rectangle newBounds) {
		this.bounds = newBounds;
	}

	public void updateCoordsBounds(Rectangle newValue) {
		setCoords(newValue.x, newValue.y);
		updateBounds(newValue);
		setWidth(newValue.width);
		setHeight(newValue.height);
	}

	public static float getRelativeHeight(UIObject uiObject) {
		return uiObject.getY() + uiObject.getHeight() + 10;
	}

	@Override
	public String toString() {
		return "UIObject{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
