
import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class GameElement {

	public boolean actor = false;
	public boolean drawable = true;
	public boolean eventDispatcher = false;
	public boolean listeningToKeys = false;
	public boolean listeningToMouse = false;
	public boolean notifyable = false;


	public abstract void act();

	public abstract void paint(Graphics2D g);

	public abstract void keyNotification(ArrayList<Integer> keysDown);

	public abstract Box getBoundingBox();

	public void setActor(){
		this.actor = true;
	}
	public void setDrawable(){
		this.drawable = true;
	}
}
