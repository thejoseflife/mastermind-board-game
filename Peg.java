package com.Mastermind.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Peg extends Object {
	
	public Ellipse2D.Double peg = new Ellipse2D.Double();
	public int value;
	public boolean attachedToBounds = false;
	
	public Peg(double x, double y, int value) {
		this.x = x;
		this.y = y;
		this.value = value;
		
		if(value == 1) color = Color.RED;
		else if(value == 2) color = Color.CYAN;
		else if(value == 3) color = Color.GREEN;
		else if(value == 4) color = Color.YELLOW;
		else if(value == 5) color = new Color(255, 119, 0);
		else if(value == 6) color = new Color(204, 0, 255);
		else value = 0;
	}
	
	public int getValue() {
		return value;
	}
	
	public void tick() {
		peg = new Ellipse2D.Double(x, y, 40, 40);
	}

	public void render(Graphics2D g) {
		g.setColor(color);
		g.fill(peg);
		g.setColor(Color.BLACK);
		g.draw(peg);
	}

}
