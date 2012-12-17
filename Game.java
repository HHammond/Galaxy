
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.event.*;

@SuppressWarnings("serial")

public class Game extends Container implements Runnable,KeyListener,MouseListener{
	
	private Cygnus parent;
	protected ArrayList<GameElement> actors;
	protected ArrayList<GameElement> drawables;
	protected ArrayList<GameElement> gameElements;

	protected ArrayList<MouseListener> mouseListeners;
	protected ArrayList<MouseMotionListener> mouseMotionListeners;
	protected ArrayList<KeyListener> keyListeners;

	private Galaxy galaxy;

	public Game(Cygnus parent)
	{
		
		super();

		setFocusable(true);
		requestFocusInWindow();
		setLayout(null);
		setSize(parent.getSize());
		setVisible(true);

		this.parent = parent;

		actors			= new ArrayList<GameElement>();
		drawables		= new ArrayList<GameElement>();
		gameElements	= new ArrayList<GameElement>();

		mouseListeners = new ArrayList<MouseListener>();
		mouseMotionListeners = new ArrayList<MouseMotionListener>();
		keyListeners = new ArrayList<KeyListener>();

		galaxy = new Galaxy(this);
		this.add(galaxy);

	}

	public void add(GameElement obj)
	{
		gameElements.add(obj);

		if( obj.actor )
		{
			actors.add((GameElement)obj);
		}

		if( obj.drawable )
		{
			drawables.add((GameElement)obj);
		}

		if( MouseListener.class.isInstance(obj))
		{
			mouseListeners.add((MouseListener)obj);
		}

		if( MouseMotionListener.class.isInstance(obj))
		{
			mouseMotionListeners.add((MouseMotionListener)obj);
		}

		if( KeyListener.class.isInstance(obj))
		{
			keyListeners.add((KeyListener)obj);
		}

	}

	//THIS NEEDS TO BE TESTED THOROUGHLY!
	public void remove(GameElement obj)
	{
		actors.remove(obj);
		drawables.remove(obj);
		gameElements.remove(obj);
		if( MouseListener.class.isInstance(obj))
		{
			mouseListeners.remove((MouseListener)obj);
		}
		if( MouseMotionListener.class.isInstance(obj))
		{
			mouseMotionListeners.remove((MouseMotionListener)obj);
		}
		if( KeyListener.class.isInstance(obj))
		{
			keyListeners.remove((KeyListener)obj);
		}

	}

	public void run()
	{
		for(int i=0;i<actors.size();i++)
		{
			actors.get(i).act();
		}
	}

	public void paint(Graphics2D g)
	{
		for(int i=0;i<drawables.size();i++)
		{
			drawables.get(i).paint(g);
		}
	}

	public Box getBoundingBox()
	{
		return new Box( 0,0,getSize().width, getSize().height);
	}

	//TODO: implement listener
	public void keyTyped(KeyEvent e)
	{
		for(int i=0;i<keyListeners.size();i++)
		{
			keyListeners.get(i).keyTyped(e);
		}
	}

	public void keyPressed(KeyEvent e)
	{
		for(int i=0;i<keyListeners.size();i++)
		{
			keyListeners.get(i).keyPressed(e);
		}
	}

	public void keyReleased(KeyEvent e)
	{
		for(int i=0;i<keyListeners.size();i++)
		{
			keyListeners.get(i).keyReleased(e);
		}
	}

	public void mouseMoved(MouseEvent e) 
	{
		for(int i=0;i<mouseMotionListeners.size();i++)
		{
			mouseMotionListeners.get(i).mouseMoved(e);
		}
    }
    public void mouseDragged(MouseEvent e) 
    {
    	for(int i=0;i<mouseMotionListeners.size();i++)
    	{
			mouseMotionListeners.get(i).mouseDragged(e);
		}
    }
	public void mouseClicked(MouseEvent e) 
	{
		for(int i=0;i<mouseListeners.size();i++)
		{
			mouseListeners.get(i).mouseClicked(e);
		}
    }
    public void mouseEntered(MouseEvent e) 
    {
    	for(int i=0;i<mouseListeners.size();i++)
    	{
			mouseListeners.get(i).mouseEntered(e);
		}
    }
    public void mouseExited(MouseEvent e) 
    {
    	for(int i=0;i<mouseListeners.size();i++)
    	{
			mouseListeners.get(i).mouseExited(e);
		}
    }
    public void mousePressed(MouseEvent e) 
    {
    	for(int i=0;i<mouseListeners.size();i++)
    	{
			mouseListeners.get(i).mousePressed(e);
		}
    }
    public void mouseReleased(MouseEvent e) 
    {
    	for(int i=0;i<mouseListeners.size();i++)
    	{
			mouseListeners.get(i).mouseReleased(e);
		}
    }
	@Override
	public Dimension getSize()
	{
		return parent.getSize();
	}
}