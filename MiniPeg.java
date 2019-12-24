package com.Mastermind.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class MiniPeg extends Object {
	
	public Ellipse2D.Double miniPeg;
	
	public MiniPeg(int x, int y) {
		this.x = x;
		this.y = y;
		color = new Color(217, 217, 217);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics2D g) {
		miniPeg = new Ellipse2D.Double(x, y, 20, 20);
		
		g.setColor(color);
		g.fill(miniPeg);
		
		g.setColor(Color.BLACK);
		g.draw(miniPeg);
	}
	
}
