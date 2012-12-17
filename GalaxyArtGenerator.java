/*

This code is based off of my initial galaxy art generator in processing.
This version is more precise and creates an all-around better image

*/

import java.awt.Graphics2D;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.ArrayList;

public class GalaxyArtGenerator
{

	public GalaxyArtGenerator()
	{

	}

	//generate grapihics
	public void getGFX(Graphics2D g,int width, int height)
	{
		//generations is a bound for number of nodes.
		//value should be selected as balance between performance and detail
		int generations = 14;
		double maxNodes = Math.pow(2,generations);
		double dispersionFactor = 0.80;
		int iterations = 3;

		double initDisance = (Math.max(width,height))/generations/dispersionFactor*6;
		double starDensity = Math.random()*0.001 + 0.0005;
		
		//background stars
		int stars = (int)(width*height*starDensity);

		for(int i=0;i<stars;i++)
		{
			g.setColor( new Color(	(int)(Math.random()*(255-150)+150),
									(int)(Math.random()*(255-150)+150),
									(int)(Math.random()*(255-150)+150),
									(int)(Math.random()*(240-50)+50))
									);
			g.fill(new Ellipse2D.Double( Math.random()*width, Math.random()*height, Math.random()*2,Math.random()*2 ));
		}

		//gas clouds and stars
		for(int j=0;j<iterations;j++)
		{

			ArrayList<Node> nodes = new ArrayList<Node>();
			Node a = new Node( width/2,height/2,initDisance,dispersionFactor,null );
			nodes.add(a);
			while( nodes.size() <= maxNodes)
			{
				for(int i=0;i<nodes.size() && nodes.size() <= maxNodes;i++)
				{
					if (nodes.get(i).edgeDistance != 0)
					{
						nodes.add(nodes.get(i).createChild());
					}
				}
			}

			for(int i=0;i<nodes.size();i++)
			{
				nodes.get(i).draw(g);
			}
		}

	}

}

class Node {
	double rad = 1;
	double dotSizeFactor = 0.005;
	double shadowWidth = 0.7;
	double shadowOpacity = 0.018;
	double edgeDistance;
	double dispersionFactor;
	double x, y;
	Node vertices;
	
	Node parent;
	
	public Node(double x, double y, double edgeDistance)
	{
		this.x = x;
		this.y = y;
		if (edgeDistance < 1) 
		{
			this.edgeDistance = 0;
		} 
		else {
			this.edgeDistance = edgeDistance;
		}
	}
	public Node(double x, double y, double edgeDistance, double dispersionFactor, Node parent) 
	{

		this.x = x;
		this.y = y;
		this.dispersionFactor = dispersionFactor;
		this.parent = parent;
		if (edgeDistance < 1) 
		{
			this.edgeDistance = 0;
		} 
		else {
			this.edgeDistance = edgeDistance;
		}
	}
	
	public Color generateColor(){
		int min = 150;
		int max = 255;
		int baseBrightness = (int)(Math.random()*(max-min)+min);
		double R = Math.random()*2-1;
		double G = Math.random()*2-1;
		double B = Math.random()*2-1;

		int offset = Math.min( 255-baseBrightness, baseBrightness);

		return new Color(	(int)(baseBrightness + R*offset),
							(int)(baseBrightness + G*offset),
							(int)(baseBrightness + B*offset)
							);
	}

	public void draw(Graphics2D p) 
	{

		if ( parent == null ) return;

		double radius = rad+dotSizeFactor*edgeDistance;
		double maxOpacity = 0.8;
		p.setColor( new Color(	(int)(Math.random()*(255-150)+150 ),
								(int)(Math.random()*(255-150)+150),
								(int)(Math.random()*(255-150)+150),
								(int)( 1/(Math.sqrt(3.14159*Math.random()*150+1)) *255*maxOpacity)
							 )
		);

		p.fill( new Ellipse2D.Double( x-radius/2, y-radius/2, radius, radius ) );

		double shadowRadius = edgeDistance*shadowWidth;

		if (shadowRadius > 0)
		{
			Point2D center = new Point2D.Float((float)x,(float)y);
			Color baseColor = new Color((int)(Math.random()*(255-150)+150),
										(int)(Math.random()*(255-150)+150),
										(int)(Math.random()*(255-150)+150),
										(int)((Math.random()*(255-100)+100)*shadowOpacity)
			);
			Color midColor = new Color( baseColor.getRed(),
										baseColor.getGreen(),
										baseColor.getBlue(),
										baseColor.getAlpha()/3*2
			);
			Color[] shadow = {
								baseColor,
								midColor,
								new Color( 255,255,255,0 )
			};

			float[] shadowDist = { 0.0f,0.7f,1.0f };
			RadialGradientPaint shader = new RadialGradientPaint(center,(float)shadowRadius/2,shadowDist,shadow); 
			p.setPaint(shader);
			p.fill( new Ellipse2D.Double( x - shadowRadius/2, y - shadowRadius/2, shadowRadius, shadowRadius ));
		}
	}

	public Node createChild() 
	{
		Node n = new Node(	x + Math.random()*edgeDistance-edgeDistance/2,
							y + Math.random()*edgeDistance-edgeDistance/2,
							edgeDistance*dispersionFactor,
							dispersionFactor,
							this
		);
		return n;
	}
}
