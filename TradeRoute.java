import java.awt.*;
import java.awt.geom.*;

public class TradeRoute extends BaseGameObject{
	public SolarSystem s1,s2;

	public TradeRoute(SolarSystem s1, SolarSystem s2){
		drawable = true;
		this.s1 = s1;
		this.s2 = s2;
	}

	public void paint(Graphics2D g){
		g.setColor(new Color(255,255,255,50));
		g.drawLine( (int)s1.x, (int)s1.y, (int)s2.x, (int)s2.y);
	}

}