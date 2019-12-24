package com.Mastermind.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

public class Display extends Canvas implements Runnable, MouseListener, MouseMotionListener {   
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 400;
	public static final int HEIGHT = 620;
	public static final String TITLE = "Mastermind";

	private Thread thread;
	private boolean running = false;
	private boolean pegInHand = false;
	public boolean gameOver = false;
	public boolean failToSolve = false;
	public boolean solvingByAI = false;
	
	private Random r = new Random();
	private Rectangle button = new Rectangle(310, 520, 60, 40);
	public int[] availableSlots = {9, 19, 29, 39, 49};
	public int turns = 0;
	public Bounds[] bounds = new Bounds[50];
	public int[][] sequences = new int[11][4];
	public Peg[] palet = new Peg[6];
	public int[] result = new int[2];
	public LinkedList<Peg> pegs = new LinkedList<Peg>();
	
	public Display() {
		JFrame frame = new JFrame(TITLE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		init();
			
		start();
	}
	
	public void init() {
		for(int i = 0; i <= 9; i++) {
			bounds[i] = new Bounds(10, 10 + (i * 50), this);   
			if(i < 4) sequences[0][i] = r.nextInt(6) + 1;
			if(i < 6) palet[i] = (new Peg(10 + (i * 48), 520, i + 1));
		}
		for(int i = 0; i <= 9; i++) {
			bounds[i + 10] = new Bounds(70, 10 + (i * 50), this);       
		}
		for(int i = 0; i <= 9; i++) {
			bounds[i + 20] = new Bounds(130, 10 + (i * 50), this);       
		}
		for(int i = 0; i <= 9; i++) {
			bounds[i + 30] = new Bounds(190, 10 + (i * 50), this);       
		}
		for(int i = 0; i <= 9; i++) {
			bounds[i + 40] = new Bounds(300, 10 + (i * 50), this);       
		}
		
		//displayScoreInConsole();
	}
	
	public void displayScoreInConsole() {
		for(int i: sequences[0]) {
			System.out.println(i); //Show code in console when you start up game
		}
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		while(running) {
			tick();
			if(running)
				render();
		}
		stop();
	}

	public boolean allSquaresOccupied() {
		int count = 0;
		int[] code = new int[4];
		for(Bounds b: bounds) {
			if(b.canMove() && b.occupied) {
				code[count] = b.peg.getValue();
				count++;
			}
		}
		if(count == 4) {
			turns++;
			sequences[turns][0] = code[0];
			sequences[turns][1] = code[1];
			sequences[turns][2] = code[2];
			sequences[turns][3] = code[3];
			
			return true;
		}
		return false;
	}
	
	private void tick() {
		for(Peg p: palet) p.tick();
		for(Peg p: pegs) p.tick();
		for(Bounds b: bounds) {
			b.tick();
			if(allSquaresOccupied()) {
				checkResult();
				if(result[1] == 4) gameOver = true;
				availableSlots[0]--;
				availableSlots[1]--;
				availableSlots[2]--;
				availableSlots[3]--;
				if(turns > 1) availableSlots[4]--;
				bounds[availableSlots[4]].generateCircles(result[0], result[1]);
			}
		}
		
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics2D g = (Graphics2D)bs.getDrawGraphics();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString("created by Josef Sajonz", 160, HEIGHT - 36);
		
		for(Bounds b: bounds) b.render(g);
		for(Peg p: palet) p.render(g);
		for(int i = 0; i < pegs.size(); i++) pegs.get(i).render(g);
		
		if(gameOver) {
			g.setColor(Color.WHITE);
			g.fill(new Rectangle(0, 100, WIDTH, 260));
			g.setColor(Color.BLACK);
			g.draw(new Rectangle(0, 100, WIDTH - 7, 260));
			g.setFont(new Font("arial", Font.BOLD, 20));
			
			if(!failToSolve) {
				g.drawString("Congratulations! You matched", 50, 150);
				g.drawString("the code of:", 140, 180);
				g.drawString("It took you " + turns + " turn(s).", 100, 300);
			} else {
				g.drawString("Wow, you failed to match", 72, 150);
				g.drawString("the code of:", 140, 180);
				g.drawString("Better luck next time!", 93, 300);
			}
			
			Peg[] pegs = new Peg[4];
			pegs[0] = new Peg(95, 220, sequences[0][0]);
			pegs[1] = new Peg(145, 220, sequences[0][1]);
			pegs[2] = new Peg(195, 220, sequences[0][2]);
			pegs[3] = new Peg(245, 220, sequences[0][3]);
			for(Peg p: pegs) {
				p.tick();
				p.render(g);
			}
			
			g.drawString("Click to restart.", 120, 350);
			
		}
		
		g.setColor(Color.BLACK);
		g.draw(button);
		if(solvingByAI) {
			g.setColor(Color.YELLOW);
			g.fill(button);
		}
		g.setColor(Color.BLACK);
		g.drawString("AI", 330, 545);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		new Display();
	}
	
	public void checkResult() { //[0] black circle, [1] white circle
		int blackCircleCount = 0;
		int whiteCircleCount = 0;
		for(int i = 0; i < 4; i++) {
			if(sequences[0][i] == sequences[turns][i]) whiteCircleCount++;
			else {
				for(int j: sequences[turns]) if(sequences[0][i] == j) blackCircleCount++;
			}
		}
		result[0] = blackCircleCount;
		result[1] = whiteCircleCount;
	}
	
	public void insertPegs(int one, int two, int three, int four) {
		int[] value = {one, two, three, four};
		for(int i = 0; i < 4; i++) {
			pegs.add(new Peg(bounds[availableSlots[i]].getX() + 5, bounds[availableSlots[i]].getY() + 5, value[i]));
			bounds[availableSlots[i]].checkForPeg();
		}
	}
	
	public void solveByAI() {
		reset();
		LinkedList<Integer> possibleCodes = new LinkedList<Integer>(); //Should have 1296 elements
		for(int i = 0; i < 6; i++) { //Create list
			for(int j = 0; j < 6; j++) {
				for(int n = 0; n < 6; n++) {
					for(int a = 0; a < 6; a++) {
						int tempInt = 1111 + i + (j * 10) + (n * 100) + (a * 1000);
						if(!duplicateElement(possibleCodes, tempInt)) possibleCodes.add(tempInt);
					}
				}
			}
		}
		
		insertPegs(1, 1, 2, 2);
	}
	
	public void reset() {
		availableSlots[0] = 9;
		availableSlots[1] = 19;
		availableSlots[2] = 29;
		availableSlots[3] = 39;
		availableSlots[4] = 49;
		
		gameOver = false;
		pegs.clear();
		turns = 0;
		init();
	}
	
	private boolean duplicateElement(List<Integer> list, int num) {
		for(int i: list) {
			if(num == i) return true;
		}
		return false;
	}
	
	public void mouseClicked(MouseEvent e) {

		if(button.contains(e.getPoint()) && !solvingByAI) {
			solvingByAI = true;
			solveByAI();
		}
		
		if(gameOver) {
			reset();
		}
	}
	public void mouseReleased(MouseEvent e) {
		if(pegInHand) {
			pegInHand = false;
			for(Bounds b: bounds) {
				b.checkForPeg();
			}
			for(Peg p: pegs) {
				if(!p.attachedToBounds) pegs.remove(p);
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		for(int i = 0; i < 6; i++) {
			if(!pegInHand && palet[i].peg.contains(e.getX(), e.getY()) && !solvingByAI) {
				pegInHand = true;
				pegs.add(new Peg(palet[i].getX(), palet[i].getY(), palet[i].getValue()));
				
			}
		}
		
		if(pegInHand) {
			pegs.getLast().setX(e.getX());
			pegs.getLast().setY(e.getY());
		}

	}

	public void mouseMoved(MouseEvent e) {		
	}
	public void mouseEntered(MouseEvent arg0) {	
	}
	public void mouseExited(MouseEvent arg0) {
	}
	public void mousePressed(MouseEvent arg0) {
	}
}
