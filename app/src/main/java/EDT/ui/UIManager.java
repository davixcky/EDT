package EDT.ui;

import EDT.services.data.list.linkedList.LinkedList;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class UIManager {

	private LinkedList<UIObject> objects;

	private UIObject focusedElement = null;

	public UIManager(){
		objects = new LinkedList<UIObject>();
	}
	
	public void update(){
		for(UIObject o : objects)
			o.update();
	}
	
	public void render(Graphics g){
		for(UIObject o : objects)
			o.render(g);
	}

	public void onMousePressed(MouseEvent e) {
		for(UIObject o : objects)
			o.onMousePressed(e);
	}
	
	public void onMouseMoved(MouseEvent e){
		for(UIObject o : objects)
			o.onMouseMove(e);
	}
	
	public void onMouseReleased(MouseEvent e){
		for(UIObject o : objects)
			o.onMouseRelease(e);
	}

	public void onMouseDragged(MouseEvent e) {
		for (UIObject o: objects)
			o.onMouseDragged(e);
	}

	public void onKeyPressed(KeyEvent e) {
		for (UIObject o: objects)
			o.onKeyPressed(e);
	}
	
	public void addObjects(UIObject ...objects){
		this.objects.clear();
		this.objects.addAll(Arrays.asList(objects));
	}

	public void addObject(UIObject object) {
		if (this.objects.contains(object)) return;
		this.objects.add(object);
	}

	public void removeObject(UIObject o){
		objects.remove(o);
	}

	public LinkedList<UIObject> getObjects() {
		return objects;
	}

	public void setObjects(LinkedList<UIObject> objects) {
		this.objects = objects;
	}

	public UIObject getFocusedElement() {
		return focusedElement;
	}

	public void setFocusedElement(UIObject focusedElement) {
		this.focusedElement = focusedElement;
	}
}
