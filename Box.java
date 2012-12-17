
public class Box{
	
	private double x,y,w,h;

	public Box(double x, double y, double w, double h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public double getWidth(){
		return this.w;
	}
	
	public double getHeight(){
		return this.h;
	}

	public boolean contains(double px, double py){
		return ( px >= this.x && px <= this.x+this.w) && ( py >= this.y && py <= this.y+this.h) ;
	}

	public boolean overlap(Box b){
		if( b == null ){
			return false;
		}
		return	contains( b.getX(), b.getY() ) ||
				contains( b.getX()+b.getWidth(), b.getY() ) ||
				contains( b.getX()+b.getWidth(), b.getY()+b.getHeight() ) ||
				contains( b.getX(), b.getY()+b.getHeight());
	}
}