import java.awt.Graphics2D;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.ArrayList;

import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;

public class Galaxy extends BaseGameObject implements MouseListener,MouseMotionListener{
	
	private Game parent;
	public ArrayList<SolarSystem> stars;
	public ArrayList<TradeRoute> routes;

	BufferedImage background;

	private int systems = 10;

	public Galaxy(Game parent)
	{

		drawable = true;
		actor = true;

		this.parent = parent;
		stars = new ArrayList<SolarSystem>();
		routes = new ArrayList<TradeRoute>();

		/*
		removed during development
		for(int i=0;i<systems;i++)
		{
			SolarSystem s = new SolarSystem(Math.random()*(parent.getSize().width-300)+150,
											Math.random()*(parent.getSize().height-300)+150
											);

			if( i >= 1){
				TradeRoute r = new TradeRoute( s, stars.get(i-1) );
				add(r);
			}

			add(s);
		}

		remove(stars.get(stars.size()-1));
		*/
		background = generateGalaxy();

	}

	private BufferedImage generateGalaxy()
	{
		BufferedImage bg = new BufferedImage(parent.getSize().width,
											 parent.getSize().height,
											 BufferedImage.TYPE_INT_ARGB
											 );
		bg.setAccelerationPriority(1);
		Graphics2D g2d = bg.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		GalaxyArtGenerator gen = new GalaxyArtGenerator();
		gen.getGFX(g2d,parent.getSize().width,parent.getSize().height);
		return bg;
	}

	public void add(TradeRoute r){
		routes.add(r);
		parent.add(r);
	}
	public void remove(TradeRoute r){
		routes.remove(r);
		parent.remove(r);
	}
	public void add(SolarSystem s)
	{
		stars.add(s);
		parent.add(s);
	}
	public void remove(SolarSystem s)
	{
		stars.remove(s);
		parent.remove(s);
	}

	public void act()
	{

	}

	public void paint(Graphics2D g)
	{
		//background = generateGalaxy();
		if(background == null)
		{
			background = generateGalaxy();
		}

		g.drawImage(background,0,0,parent);

		
	}

	public void mouseClicked(MouseEvent e) 
	{
    }
    public void mouseEntered(MouseEvent e) 
    {
    }
    public void mouseExited(MouseEvent e) 
    {
    }
    public void mousePressed(MouseEvent e) 
    {
    }
    public void mouseReleased(MouseEvent e) 
    {
    }
    public void mouseMoved(MouseEvent e) 
    {
    }
    public void mouseDragged(MouseEvent e) 
    {
    }


	public Box getBoundingBox()
	{
		return new Box(0,0,parent.getSize().width,parent.getSize().height);
	}
}

