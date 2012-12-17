/*

This code is the basis to a game using my Cygnus Game engine which
is still in development. The engine uses a relatively old linear
algebra library I wrote. 

Currently this code just implements a more advanced version of my
galaxy art generator.

I've started this project 

Created by Henry Hammond 2012

*/

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
/**
* Cygnus is the game engine instance. 
*/
public class Cygnus extends JPanel implements Runnable, KeyListener, MouseListener,MouseMotionListener{
	

	private Game game;

	// Graphics dimension of current buffer if initialized
	private Dimension size;
	final private Color defaultBackground = new Color(200,200,200);

	// Graphics context variables
	private BufferedImage backBuffer;	// where g2d is drawn to
	private Graphics2D g2d;				// Graphics2D drawing context

	// thread of this loop. 
	private Thread thread;

	// desired framerate of the game
	// used in processing of run() loop.
	private float frameRate = 120;

	// The main method which is called to run the game engine
	public static void main(String [] args){


		SplashScreen splashScreen = new SplashScreen();


		// instantiate game engine instance
		Cygnus engine = new Cygnus();
		// remove splashscreen after intantiation of game objects`
		//splashScreen.setVisible(false);
		//put game engine instance into fullscreen window if available on the system
		

		JFrame applicationWindow = new JFrame();
		applicationWindow.setLayout( new BorderLayout() );
		applicationWindow.add(engine, BorderLayout.CENTER);

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		if (gd.isFullScreenSupported() || false) {
			applicationWindow.setUndecorated(true);
			applicationWindow.setSize( Toolkit.getDefaultToolkit().getScreenSize() );
			applicationWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			gd.setFullScreenWindow(applicationWindow);
		} 
		else {
			System.err.println("Full screen not supported");
			applicationWindow.setSize(800, 600); // just something to let you see the window
			applicationWindow.setVisible(true);
			applicationWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			applicationWindow.pack();
		}

	}

	/**
	* Game engine constructor
	*/
	public Cygnus(){
		
		// initialize JPanel
		super();
		// set size and prepare for window packing
		
		size = Toolkit.getDefaultToolkit().getScreenSize();
		//size = new Dimension(800,600);
		setSize( size );
		setPreferredSize( size );
		setVisible(true);
		setFocusable(true);
		requestFocusInWindow();
		setLayout(new BorderLayout());
		
		setBackground( defaultBackground );

		game = new Game(this);
		add(game);

		//add key and mouse listeners
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		// initialize game
		

		// initialize graphics and loop
		initializeGraphicsContext();
		initializeGameThread();		
	}

	/**
	* This code is used to create the graphics context whenever required
	*/
	private void initializeGraphicsContext(){
		backBuffer = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
		backBuffer.setAccelerationPriority(1);
		g2d = backBuffer.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	/**
	* Initialize the main thread of this application
	*/
	private void initializeGameThread(){
		thread = new Thread(this);
		thread.start();
	}

	public void paint(Graphics g){

		// diagnostic information of drawing start time
		long gfxStartTime = System.nanoTime();
		
		// ensure backbuffer exists and is the correct size
		if(backBuffer == null || size != this.getSize()){
			initializeGraphicsContext();
		}
		// variable to keep track of current size versus buffer size
		size = this.getSize();

		// call game's paint method
		game.paint(g2d);

		// draw backbuffer to the screen - part of double buffering strategy
		g.drawImage(backBuffer,0,0,this);

		// variables to keep track of how long painting takes...
		// mostly here for debug and performance information
		long gfxEndTime = System.nanoTime();
		// rendering time of graphics in nanoseconds
		double renderTime = gfxEndTime-gfxStartTime;
		//System.out.println(renderTime*10e-10);
		
	}

	/**
	* The game loop 
	*/
	public void run(){

		// initialize diagnostic information
		long recovery = 0;
		long sleepTime = (long)(1000/frameRate);
		final long idealSleepTime = (long)(1000/frameRate);

		long sleepRecovery = 0;

		long threadStart;
		long threadEnd;
		long threadDelta;

		boolean painted = false;
		long lastSleepTime = 0;

		int threadCounter = 0;
		final int listenerCheckFrequency = (int)frameRate;

		// begin game loop
		while(true){

			// track the time the loop takes to run
			// loop start time
			threadStart = System.currentTimeMillis();

			// notify listeners
			if(threadCounter++ >= listenerCheckFrequency){
				threadCounter = 0;
				// update listeners
			}

			// run game logic
			game.run();
			
			// paint graphics less frequently when lagging
			// this should serve to reduce the effects of lag on
			// performance of games, since a large part of processing
			// will be spent on the actual drawing. This may eventually
			// find it's way to it's own special loop or thread.
			if( painted == false || lastSleepTime >= 0){
				// draw graphics to the screen
				repaint();
				// record that we have drawn graphics during this running of the loop
				// placing lower importance on painting next loop during lag
				painted = true;
			}
			else{
				// record that we haven't drawn graphics during this loop
				painted = false;
			}

			// Finally process the time the loop took to run and 
			// compare it to the ideal loop time in order to determine 
			// how much the system is lagging behind the ideal framerate.
			// after the amount of lag has been determined we can adjust
			// the time the thread sleeps and use sleep time for processing.
			// This should cause the system to handle lag in a more consistent
			// way and reduce the overall effects
			threadEnd = System.currentTimeMillis();
			threadDelta = threadEnd - threadStart;
			sleepTime = idealSleepTime - threadDelta - sleepRecovery;

			// keep track of the last sleep time 
			lastSleepTime = sleepTime;

			// Check if our sleep time for this loop has been processed as a negative.
			// This would indicate that the thread has taken more time to complete than
			// the ideal sleeping time, and thus the system should not sleep at all in order
			// to maximize performance
			if(sleepTime < 0){
				sleepRecovery = -sleepTime;
				// ensure recovery is less than actual sleep time to prevent infinite lagging
				sleepRecovery %= idealSleepTime;
			}
			else{
				sleepRecovery = 0;
			}

			// Finally the part of this loop where we actually tell the thread to sleep.
			// max function serves to ensure no negative times are input.
			try{
				Thread.sleep( Math.max(sleepTime,0) );
			}
			catch( java.lang.InterruptedException e ){
				// this exception doesn't need any extra attention.
			}

			 // System.out.println(sleepTime);
			 // System.out.println(painted);
		}
	}

	/*Notify game of key events*/
	public void keyTyped(KeyEvent e){
		game.keyTyped(e);
	}
	public void keyPressed(KeyEvent e){
		game.keyPressed(e);
	}
	public void keyReleased(KeyEvent e){
		game.keyReleased(e);
	}

	/* Notify game of mouse events */
	public void mouseClicked(MouseEvent e) {
		game.mouseClicked(e);
    }
    public void mouseEntered(MouseEvent e) {
    	game.mouseEntered(e);
    }
    public void mouseExited(MouseEvent e) {
    	game.mouseExited(e);
    }
    public void mousePressed(MouseEvent e) {
    	game.mousePressed(e);
    }
    public void mouseReleased(MouseEvent e) {
    	game.mouseReleased(e);
    }

    public void mouseMoved(MouseEvent e) {
    	game.mouseMoved(e);
    }

    public void mouseDragged(MouseEvent e) {
    	game.mouseDragged(e);
    }

	/*
	* This class should not be serialized, throw an error when that happens.
	*/
	private void writeObject(ObjectOutputStream oos) throws IOException {
		throw new IOException("This class is NOT serializable.");
	}
}

//Splashscreen during game initialization
class SplashScreen extends JFrame {

	private BufferedImage gfx;
	public boolean running;

	public SplashScreen(){
		super();
		setSize(new Dimension(600,300));
		setBackground(new Color(120,120,120));
		setUndecorated(true);
		setLocationRelativeTo(null);
		gfx = createGraphics();
		setVisible(true);
		running = true;

	}

	public void destroy(){
		running = false;
	}

	//generate basic loading graphics
	public BufferedImage createGraphics(){
		//create drawing context with ARGB
		BufferedImage img = new BufferedImage(getSize().width,getSize().height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//begin drawing background gradient
		Point2D center = new Point2D.Float(1.0f*getSize().width/2,0.0f);
		Color[] colors = { new Color(0+100,0+100,0+100) ,new Color(0+60,0+60,0+60)};
		float[] dist = {0.0f,  1.0f};
		RadialGradientPaint p = new RadialGradientPaint(center, getSize().width/2, dist, colors);
		g2d.setPaint(p);
		g2d.fill( new Rectangle2D.Float( 0,0, getSize().width,getSize().height));


		//prepare for text rendering and curve
		g2d.setColor(Color.WHITE);
		
		// create new CubicCurve2D.Double
		CubicCurve2D c = new CubicCurve2D.Double();
		// draw CubicCurve2D.Double with set coordinates

		for(int i=0;i<6;i++){
			c.setCurve(	0, getSize().height-50-15*i, 
		        	getSize().width/2, getSize().height,
		        	getSize().width/2, getSize().height/2,
		        	 getSize().width, getSize().height-100-i*5
		           );
			g2d.draw(c);
		}

		//finally render the text
		Font header = new Font("Sans Serif",Font.BOLD,32);
		Font subHeaderFont = header.deriveFont(12f);
		String title = "CYGNUS";
		String subtitle = "INITIALIZING COMPONENTS...";
		Dimension headerSize = getStringSize(title,header,g2d);
		Dimension subehaderSize = getStringSize(subtitle,subHeaderFont,g2d);

		g2d.setColor( new Color(255,0,0));
		g2d.fillRect(0,10,8,26);

		g2d.setColor(Color.WHITE);
		g2d.setFont(header);
		g2d.drawString( title, 15, 15+headerSize.height/2);
		g2d.setFont(subHeaderFont);
		g2d.drawString( subtitle, 15+headerSize.width+15,15+headerSize.height/2);

		//return the generated image for display
		return img;
	}

	public Dimension getStringSize(String str, Font font, Graphics graphics){
		FontMetrics metrics = graphics.getFontMetrics(font);
		int height = metrics.getHeight();
		int width = metrics.stringWidth(str);

		return new Dimension(width,height);
	}

	public void paint(Graphics g){
		super.paint(g);
		g.drawImage(gfx,0,0,this);

		// loader removed
		// g.drawImage(loader( angle+=1 ),getSize().width/2-25,getSize().height/2-25,this);

	}



}