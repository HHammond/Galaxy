
import java.awt.Graphics2D;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;


public class SolarSystem extends BaseGameObject implements MouseListener,MouseMotionListener{
		
	float x, y;
	float radius;
	float shadowRadius;
	float selectionRadius ;
	Color baseColour;
	Color centerColour;

	int [] mouseLocation;

	double strokeOpacity;
	boolean selected;

	String title = "Title";


	public SolarSystem(double x, double y){

		drawable = true;
		actor = true;

		this.x = (float)x;
		this.y = (float)y;
		float maxRadius = 15;
		float minRadius = 9;
		radius = (float)Math.random()*(maxRadius-minRadius)+minRadius;

		baseColour = randomColor(150,255,255);
		centerColour = baseColour.brighter();
		strokeOpacity = 0.0;

		mouseLocation = new int[2];
		mouseLocation[0] = -1;
		mouseLocation[1] = -1;

		shadowRadius = 2*radius;
		selectionRadius = radius*2.2f;
		selectionRadius += (selectionRadius)%2;
		selected = false;
	}

	private Color randomColor(int min, int max,int alpha){
		return new Color((int)(Math.random()*(max-min) )+min,
		                 (int)(Math.random()*(max-min) )+min,
		                 (int)(Math.random()*(max-min) )+min,
		                 alpha
		 );
	}

	public void paint(Graphics2D g){

		Point2D center = new Point2D.Float(x, y);

		//outer glow effect
			Color[] shadow = {new Color(255,255,255,(int)(255*0.5)),new Color(255,255,255,0)};
		float[] shadowDist = {0.0f,1.0f};
		RadialGradientPaint outline = new RadialGradientPaint(center, shadowRadius/2, shadowDist,shadow);

		//star fill effect
		Color[] starColors = { centerColour, baseColour,new Color(255,255,255,0) };
		float[] dist = {0.0f, 0.6f, 1.0f};
		RadialGradientPaint starColoring = new RadialGradientPaint(center, (float)radius/2, dist, starColors);

		g.setPaint(outline);
		g.fill( new Ellipse2D.Double( x-shadowRadius/2,y-shadowRadius/2,shadowRadius,shadowRadius) );

		g.setPaint(starColoring);
		g.fill( new Ellipse2D.Double( x-radius/2,y-radius/2,radius,radius) );

		boolean sel = selected;
		if( inRadius(	mouseLocation[0], mouseLocation[1], x, y, selectionRadius/2) || sel)		{
			strokeOpacity = 1;
			sel = true;
		}
		else{
			strokeOpacity = 0.1;
		}

		g.setColor(new Color(255,255,255,(int)(255*strokeOpacity)));

		g.setStroke( new BasicStroke(2) );
		g.draw( new Ellipse2D.Double( x-selectionRadius/2,y-selectionRadius/2,selectionRadius,selectionRadius));

		if(sel) generateTitleBox(g);

	}

	private void generateTitleBox(Graphics2D g){
		//g.setColor(new Color(255,255,255,(int)(255*0.2)));
		//g.fillRect((int)x-50,(int)y+30,150,18);

	}

	public void act(){
	}

	public Box getBoundingBox(){
		return new Box( x-shadowRadius,y-shadowRadius,shadowRadius*2,shadowRadius*2);
	}

	public boolean inRadius(double px, double py,double cx, double cy, double radius){
		return Math.pow(cx-px,2)+Math.pow(cy-py,2) <= Math.pow(radius,2) ;
	}

	public boolean hit( double px, double py){
		return inRadius( px,py,x,y,selectionRadius/2);
	}

	public void mouseClicked(MouseEvent e) {
		if( hit( e.getX(), e.getY() )){
			this.selected = !selected;
		}
		else{
			this.selected = false;
		}
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseMoved(MouseEvent e) {
    	mouseLocation[0] = e.getX();
    	mouseLocation[1] = e.getY();
    }
    public void mouseDragged(MouseEvent e) {
    }

}