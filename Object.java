package com.Mastermind.main;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Object {
	protected double x, y;
	protected Color color;
	
	public abstract void tick();
	public abstract void render(Graphics2D g);
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
}
