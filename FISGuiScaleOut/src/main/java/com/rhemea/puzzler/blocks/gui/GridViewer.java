package com.rhemea.puzzler.blocks.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GridViewer extends  JPanel implements Runnable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private int prefHeight = 67;
//	private int prefWidth  = 111;
	private boolean animation = false;
	
	private boolean stopNow = false;
	private int quad1Anim, quad2Anim, quad3Anim, quad4Anim;
	
	private int step;
	private int totalstep = 50;
	private BufferedImage q1,q2,q3,q4;
	private Graphics2D g1,g2,g3,g4;
	
	private char[][] grid = null;
	
	public GridViewer() {
		this.setPreferredSize(new Dimension(111,67));
	}
	
	public void setGrid(char[][] gr) {
		grid = gr;
	}
	
	public void run() {
	
		stopNow = false;
		
		
		quad1Anim = (int) (java.lang.Math.random() * 12);
		quad2Anim = (int) (java.lang.Math.random() * 12);
		quad3Anim = (int) (java.lang.Math.random() * 12);
		quad4Anim = (int) (java.lang.Math.random() * 12);

				
		totalstep = this.getWidth() / 2;
		
		//Building arrays to draw different animations.
				
		q1 = new BufferedImage(this.getWidth() /2, this.getHeight() /2, BufferedImage.TYPE_3BYTE_BGR);
		q2 = new BufferedImage(this.getWidth() /2, this.getHeight() /2, BufferedImage.TYPE_3BYTE_BGR);
		q3 = new BufferedImage(this.getWidth() /2, this.getHeight() /2, BufferedImage.TYPE_3BYTE_BGR);
		q4 = new BufferedImage(this.getWidth() /2, this.getHeight() /2, BufferedImage.TYPE_3BYTE_BGR);
		
		g1 = (Graphics2D) q1.getGraphics();
		g2 = (Graphics2D) q2.getGraphics();
		g3 = (Graphics2D) q3.getGraphics();
		g4 = (Graphics2D) q4.getGraphics();

		int boxWidth = (this.getWidth() - 11) / 10;
		int boxHeight = (this.getHeight() - 7) / 6;
		
		g1.setBackground(Color.black);
		g2.setBackground(Color.black);
		g3.setBackground(Color.black);
		g4.setBackground(Color.black);
		
		g1.clearRect(0,0,this.getWidth() /2,this.getHeight()/2);
		g2.clearRect(0,0,this.getWidth() /2,this.getHeight()/2);
		g3.clearRect(0,0,this.getWidth() /2,this.getHeight()/2);
		g4.clearRect(0,0,this.getWidth() /2,this.getHeight()/2);
		
		for (int i = 0; i < grid.length; i++ ) {
			for (int j = 0; j < grid[i].length; j++ ) {
			
				switch (grid[i][j]) {
					case '1':
						setTheDamnColors(Color.red);
						break;
					case '2':
						setTheDamnColors(Color.yellow);
						break;
					case '3':
						setTheDamnColors(Color.blue);
						break;
					case '4':
						setTheDamnColors(Color.cyan);
						break;
					case '5':
						setTheDamnColors(Color.darkGray);
						break;
					case '6':
						setTheDamnColors(Color.green);
						break;
					case '7':
						setTheDamnColors(Color.magenta);
						break;
					case '8':
						setTheDamnColors(Color.orange);
						break;
					case '9':
						setTheDamnColors(Color.pink);
						break;
					case 'a':
						setTheDamnColors(Color.lightGray);
						break;
					case 'b':
						setTheDamnColors(Color.gray);
						break;
					case 'c':
						setTheDamnColors(Color.white);
						break;
				}
			
				if (i < grid.length/2 && j < grid[i].length / 2) {
					//First Quadrant
					g1.fillRect( (j * boxWidth) +j +1, (i * boxHeight) + i + 1,boxWidth, boxHeight);
				}
				else {
					if (i >= grid.length/2 && j < grid[i].length / 2) {
						//Second quadrant
						g2.fillRect( (j * boxWidth) +j +1, ((i * boxHeight) + i + 1) - (this.getHeight()/2),boxWidth, boxHeight);
					}
					else {
						if (i < grid.length/2 && j >= grid[i].length / 2) {
							//Third quadrant
							g3.fillRect( ((j * boxWidth) +j +1) - (this.getWidth() /2), (i * boxHeight) + i + 1,boxWidth, boxHeight);
						}
						else {
							//Fourth quadrant
							g4.fillRect( ((j * boxWidth) +j +1) - (this.getWidth() / 2), ((i * boxHeight) + i + 1) - (this.getHeight() / 2),boxWidth, boxHeight);
						}
					}
				}
			}	
		}	
				
		//building the blocks per quadrant.
		for (step = 0; step < totalstep ; step ++ ) {
			if (stopNow) {
				return;
			}
		
			repaint();
			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}
			
		}
	}
	
	public void stopThePainting() {
		stopNow = true;
	}
	
	public void setTheDamnColors(Color c) {
		g1.setColor(c);
		g2.setColor(c);
		g3.setColor(c);
		g4.setColor(c);		
	}
		
	public void setAnimated(boolean set) {
		animation = set;
	}
	
	
	public void paint(Graphics g) {
		update(g);
	}
	
	private void drawFirstQuadrant(Graphics2D gd, int anim) {
		switch (anim) {
			case 0:
				if (g1 != null) {
					gd.drawImage(q1, 0 ,-totalstep + step + 1,this);								
				}
				break;
			case 1:
				if (g1 != null) {
					gd.drawImage(q1, 0 ,totalstep - step - 1,this);								
				}			
				break;
			case 2:
				if (g1 != null) {
					gd.drawImage(q1, -totalstep + step + 1,0,this);								
				}			
				break;
			case 3:
				if (g1 != null) {
					gd.drawImage(q1 ,totalstep - step - 1, 0,this);								
				}			
				break;
			case 4:
				if (g1 != null) {
					gd.drawImage(q1, -totalstep + step + 1 ,-totalstep + step + 1,this);								
				}
				break;
			case 5:
				if (g1 != null) {
					gd.drawImage(q1, -totalstep + step + 1 ,totalstep - step - 1,this);								
				}			
				break;
			case 6:
				if (g1 != null) {
					gd.drawImage(q1, -totalstep + step + 1,-totalstep + step + 1,this);								
				}			
				break;
			case 7:
				if (g1 != null) {
					gd.drawImage(q1 ,totalstep - step - 1, -totalstep + step + 1,this);								
				}			
				break;
			case 8:
				if (g1 != null) {
					gd.drawImage(q1, totalstep - step - 1 ,-totalstep + step + 1,this);								
				}
				break;
			case 9:
				if (g1 != null) {
					gd.drawImage(q1, totalstep - step - 1 ,totalstep - step - 1,this);								
				}			
				break;
			case 10:
				if (g1 != null) {
					gd.drawImage(q1, -totalstep + step + 1,0,this);								
				}			
				break;
			case 11:
				if (g1 != null) {
					gd.drawImage(q1 ,totalstep - step - 1, totalstep - step - 1,this);								
				}			
				break;
		}
	}
	
	private void drawSecondQuadrant(Graphics2D gd, int anim) {
		switch (anim) {
			case 0:
				if (g2 != null) {
					gd.drawImage(q2, 0, (this.getHeight() / 2) + totalstep -step - 1 ,this);								
				}
				break;
			case 1:
				if (g2 != null) {
					gd.drawImage(q2, 0, (this.getHeight() / 2) - totalstep +step + 1 ,this);								
				}
				break;
			case 2:
				if (g2 != null) {
					gd.drawImage(q2, totalstep - step - 1 , (this.getHeight() / 2) ,this);								
				}
				break;
			case 3:
				if (g2 != null) {
					gd.drawImage(q2, -totalstep + step + 1, (this.getHeight() / 2)  ,this);								
				}
				break;
			case 4:
				if (g2 != null) {
					gd.drawImage(q2, totalstep - step - 1, (this.getHeight() / 2) + totalstep -step - 1 ,this);								
				}
				break;
			case 5:
				if (g2 != null) {
					gd.drawImage(q2, totalstep - step - 1, (this.getHeight() / 2) - totalstep +step + 1 ,this);								
				}
				break;
			case 6:
				if (g2 != null) {
					gd.drawImage(q2, totalstep - step - 1 , (this.getHeight() / 2) + totalstep -step - 1 ,this);								
				}
				break;
			case 7:
				if (g2 != null) {
					gd.drawImage(q2, -totalstep + step + 1, (this.getHeight() / 2) + totalstep -step - 1  ,this);								
				}
				break;
			case 8:
				if (g2 != null) {
					gd.drawImage(q2, -totalstep + step + 1, (this.getHeight() / 2) + totalstep -step - 1 ,this);								
				}
				break;
			case 9:
				if (g2 != null) {
					gd.drawImage(q2, -totalstep + step + 1, (this.getHeight() / 2) - totalstep +step + 1 ,this);								
				}
				break;
			case 10:
				if (g2 != null) {
					gd.drawImage(q2, totalstep - step - 1 , (this.getHeight() / 2) - totalstep +step + 1 ,this);								
				}
				break;
			case 11:
				if (g2 != null) {
					gd.drawImage(q2, -totalstep + step + 1, (this.getHeight() / 2) - totalstep +step + 1 ,this);								
				}
				break;
		}
	}

	private void drawThirdQuadrant(Graphics2D gd, int anim) {
		switch (anim) {
			case 0:
				if (g3 != null) {
					gd.drawImage(q3, (this.getWidth() / 2), totalstep - step - 1  ,this);								
				}
				break;
			case 1:
				if (g3 != null) {
					gd.drawImage(q3, (this.getWidth() / 2), -totalstep + step + 1  ,this);								
				}
				break;
			case 2:
				if (g3 != null) {
					gd.drawImage(q3, (this.getWidth() / 2) + totalstep -step - 1, 0 ,this);								
				}
				break;
			case 3:
				if (g3 != null) {
					gd.drawImage(q3, (this.getWidth() / 2) - totalstep +step + 1, 0 ,this);								
				}
				break;
			case 4:
				if (g3 != null) {
					gd.drawImage(q3, (this.getWidth() / 2) + totalstep -step - 1, totalstep - step - 1  ,this);								
				}
				break;
			case 5:
				if (g3 != null) {
					gd.drawImage(q3, (this.getWidth() / 2) + totalstep -step - 1, -totalstep + step + 1  ,this);								
				}
				break;
			case 6:
				if (g3 != null) {
					gd.drawImage(q3, (this.getWidth() / 2) + totalstep -step - 1, totalstep - step - 1 ,this);								
				}
				break;
			case 7:
				if (g3 != null) {
					gd.drawImage(q3, (this.getWidth() / 2) - totalstep +step + 1, totalstep - step - 1 ,this);								
				}
				break;
			case 8:
				if (g3 != null) {
					gd.drawImage(q3, (this.getWidth() / 2) - totalstep +step + 1, totalstep - step - 1  ,this);								
				}
				break;
			case 9:
				if (g3 != null) {
					gd.drawImage(q3, (this.getWidth() / 2) - totalstep +step + 1, -totalstep + step + 1  ,this);								
				}
				break;
			case 10:
				if (g3 != null) {
					gd.drawImage(q3, (this.getWidth() / 2) + totalstep -step - 1, -totalstep + step + 1 ,this);								
				}
				break;
			case 11:
				if (g3 != null) {
					gd.drawImage(q3, (this.getWidth() / 2) - totalstep +step + 1, -totalstep + step + 1 ,this);								
				}
				break;
		}
	}
	
	private void drawFourthQuadrant(Graphics2D gd, int anim) {
		switch (anim) {
			case 0:
				if (g4 != null) {
					gd.drawImage(q4,this.getWidth() / 2 + totalstep - step - 1,this.getHeight() / 2 - totalstep + step + 1,this);								
				}
				break;
			case 1:
				if (g4 != null) {
					gd.drawImage(q4, this.getWidth() / 2,this.getHeight() / 2 + totalstep - step - 1,this);								
				}
				break;
			case 2:
				if (g4 != null) {
					gd.drawImage(q4,this.getWidth() / 2 - totalstep + step + 1, this.getHeight() / 2,this);								
				}
				break;
			case 3:
				if (g4 != null) {
					gd.drawImage(q4,this.getWidth() / 2 + totalstep - step - 1, this.getHeight() / 2,this);								
				}
				break;
			case 4:
				if (g4 != null) {
					gd.drawImage(q4, this.getWidth() / 2 - totalstep + step + 1,this.getHeight() / 2 - totalstep + step + 1,this);								
				}
				break;
			case 5:
				if (g4 != null) {
					gd.drawImage(q4, this.getWidth() / 2 - totalstep + step + 1,this.getHeight() / 2 + totalstep - step - 1,this);								
				}
				break;
			case 6:
				if (g4 != null) {
					gd.drawImage(q4,this.getWidth() / 2 - totalstep + step + 1, this.getHeight() / 2 - totalstep + step + 1,this);								
				}
				break;
			case 7:
				if (g4 != null) {
					gd.drawImage(q4,this.getWidth() / 2 + totalstep - step - 1, this.getHeight() / 2 - totalstep + step + 1,this);								
				}
				break;
			case 8:
				if (g4 != null) {
					gd.drawImage(q4,this.getWidth() / 2 + totalstep - step - 1,this.getHeight() / 2 - totalstep + step + 1,this);								
				}
				break;
			case 9:
				if (g4 != null) {
					gd.drawImage(q4,this.getWidth() / 2 + totalstep - step - 1,this.getHeight() / 2 + totalstep - step - 1,this);								
				}
				break;
			case 10:
				if (g4 != null) {
					gd.drawImage(q4,this.getWidth() / 2 - totalstep + step + 1, this.getHeight() / 2 + totalstep - step - 1,this);								
				}
				break;
			case 11:
				if (g4 != null) {
					gd.drawImage(q4,this.getWidth() / 2 + totalstep - step - 1, this.getHeight() / 2 + totalstep - step - 1,this);								
				}
				break;
			}
	}
	
	
	public void update(Graphics g) {
		
		Graphics2D gd = (Graphics2D) g;
		
		if (animation) {
			gd.setBackground(Color.black);
			gd.clearRect(0,0,this.getWidth(), this.getHeight());
			if (g1 != null) {
				gd.clearRect(0,0,this.getWidth(),this.getHeight());
				drawFirstQuadrant(gd, quad1Anim);
				drawSecondQuadrant(gd, quad2Anim);
				drawThirdQuadrant(gd, quad3Anim);
				drawFourthQuadrant(gd, quad4Anim);
			}
			return;	
		}
		
		gd.setBackground(Color.black);
		
		int boxWidth = (this.getWidth() - 11) / 10;
		int boxHeight = (this.getHeight() - 7) / 6;
		
		gd.fillRect(0,0,this.getWidth(), this.getHeight());
		
		if (grid == null) {
			return;
		}
		for (int i = 0; i < grid.length ; i++ ) {
			for (int j = 0; j < grid[i].length ; j++ ) {
				switch (grid[i][j]) {
					case '1':
						gd.setColor(Color.red);
						break;
					case '2':
						gd.setColor(Color.yellow);
						break;
					case '3':
						gd.setColor(Color.blue);
						break;
					case '4':
						gd.setColor(Color.cyan);
						break;
					case '5':
						gd.setColor(Color.darkGray);
						break;
					case '6':
						gd.setColor(Color.green);
						break;
					case '7':
						gd.setColor(Color.magenta);
						break;
					case '8':
						gd.setColor(Color.orange);
						break;
					case '9':
						gd.setColor(Color.pink);
						break;
					case 'a':
						gd.setColor(Color.lightGray);
						break;
					case 'b':
						gd.setColor(Color.gray);
						break;
					case 'c':
						gd.setColor(Color.white);
						break;
				}
				gd.fillRect( (j * boxWidth) +j +1, (i * boxHeight) + i + 1,boxWidth, boxHeight);
				
			}
		}
	}
	
	
}