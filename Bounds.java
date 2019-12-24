package com.Mastermind.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Bounds extends Object implements MouseListener {

	public boolean occupied = false;
	private Display display;
	
	public Rectangle bounds;
	public Peg peg;
	private MiniPeg[] miniPegs = new MiniPeg[4];
	
	public Bounds(int x, int y, Display display) {
		this.display = display;
		this.x = x;
		this.y = y;
		bounds = new Rectangle(x, y, 50, 50);
		display.addMouseListener(this);
		
		
	}
	
	public void addPeg(Peg peg) {
		this.peg = peg;
	}
	
	public void removePeg() {
		peg = null;
		occupied = false;
	}
	
	public void tick() {
		if(miniPegs[1] != null) {
			miniPegs[1].setX(x + 26); miniPegs[1].setY(y + 3);
		}
		if(miniPegs[2] != null) {
			miniPegs[2].setX(x + 3); miniPegs[2].setY(y + 26);
		}
		if(miniPegs[3] != null) {
			miniPegs[3].setX(x + 26); miniPegs[3].setY(y + 26);
		}
	}
	
	public boolean canMove() {
		try {
		
		if(this == display.bounds[display.availableSlots[0]] || 
				this == display.bounds[display.availableSlots[1]] || 
				this == display.bounds[display.availableSlots[2]] || 
				this == display.bounds[display.availableSlots[3]])
				return true;
		return false;
		} catch(ArrayIndexOutOfBoundsException e) {
			display.gameOver = true;
			display.checkResult();
			if(display.result[1] < 4) display.failToSolve = true;
			return false;
		}
	}
	
	public void checkForPeg() {
		for(Peg p: display.pegs) {
			if(bounds.contains(p.getX() + 5, p.getY() + 5) && canMove()) {
				addPeg(p);
				occupied = true;
				p.attachedToBounds = true;
			}
		}
	}
	
	public void generateCircles(int blackCircleCount, int whiteCircleCount) {
		int total = blackCircleCount + whiteCircleCount;
		while(total > 4) blackCircleCount--;
		for(int i = 0; i < blackCircleCount; i++) {
			miniPegs[i] = new MiniPeg((int)x + 3, (int)y + 3);
			miniPegs[i].setColor(Color.BLACK);
		}
		for(int i = blackCircleCount; i < total; i++) {
			miniPegs[i] = new MiniPeg((int)x + 3, (int)y + 3);
			miniPegs[i].setColor(Color.WHITE);
		}
	
	}
	
	public void render(Graphics2D g) {
		if(canMove()) {
			g.setColor(new Color(240, 255, 71));
			g.fill(bounds);
		}
		
		if(occupied) {
			g.setColor(new Color(255, 105, 71));
			g.fill(bounds);
		}
		
		for(MiniPeg p: miniPegs) {
			if(p != null) {
				p.render(g);
			}
		}
		
		g.setColor(Color.BLACK);
		
		g.draw(bounds);
		
		if(peg != null) {
			peg.setX(bounds.getX() + 5);
			peg.setY(bounds.getY() + 5);
			peg.render(g);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == 3 && bounds.contains(e.getPoint()) && canMove() && !display.solvingByAI) {
			if(occupied) {
				display.pegs.removeLast();
				peg = null;
				occupied = false;
			}
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
