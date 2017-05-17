package com.rhworkshop.msa.model;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//The grid container is the playfield of the puzzler. It will try to fit a given block at a given cell.
//It also contains information about the size and structure of the grid.
public class Grid implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7992165278930932501L;
	private static final Logger logger = LoggerFactory.getLogger(Grid.class);

	StringBuffer bf1, bf2, bf3,bf4;

	private int gridX = 0;
	private int gridY = 0;
	
//	private int quadX = 0;
//	private int quadY = 0;
	
//	private char [][] gridmap;

//	private ResourceBundle bundle = null;


	public Grid() {
		gridX = Integer.parseInt(ResourceLoader.getString("blocks.grid.x"));
		gridY = Integer.parseInt(ResourceLoader.getString("blocks.grid.y"));
		
//		quadX = gridX / 2;
//		quadY = gridY / 2;
		
		bf1 = new StringBuffer("          ");
		bf2 = new StringBuffer("          ");
		bf3 = new StringBuffer("          ");
		bf4 = new StringBuffer("          ");
	}
	
	public void initGrid(char [] [] gridmap) {
		for (int i = 0; i < gridmap.length ; i ++) {
			for (int j = 0; j < gridmap[i].length ; j++) {
				gridmap [i][j] = '0';
			}
		}
	}
	
	public int getGridX() {
		return(gridX);
	}
	
	public int getGridY() {
		return(gridY);
	}

	public int getXPos() {
		return(Integer.parseInt(ResourceLoader.getString("blocks.grid.posx")));
	}
	
	public int getYPos() {
		return(Integer.parseInt(ResourceLoader.getString("blocks.grid.posy")));
	}
	
	//Check if the block fit's in the environment.
	
	public boolean placeBlockOnGrid(char [][] gr, Block bl, int shape, int px, int py, int bloknr) {
		char [][] mp = bl.getShapeMap(shape);
		char [][] testmap = copyGridmap(gr);
		
		if ((px + bl.getShapeWidth(shape)) > gridX || px < 0) {
			return(false);
		}
		if ((py + bl.getShapeHeight(shape)) > gridY || py < 0) {
			return(false);
		}
		
		for (int i = 0; i < mp.length ; i++ ) {
			for (int j = 0; j < mp[i].length ; j++ ) {
				if (gr[py + i][px + j] != '0' && mp[i][j] != '0' ) {					
					return false;
				}
				if (mp[i][j] != '0') {
					testmap[py + i][px + j] = mp[i][j];
				}
			}
		}
		
		if (!isGoodMap(testmap)) {
//			if (bloknr == 7 && shape == 7) {
//			bl.printBlock(shape);
//			this.showGrid(testmap);
//			}
			return(false);
		}


		for (int i = 0; i < mp.length ; i++ ) {
			for (int j = 0; j < mp[i].length ; j++ ) {
				if (mp[i][j] != '0') {
					gr[py + i][px + j] = mp[i][j];
				}
			}
		}
		return(true);
	}
	
	public char[][] copyGridmap(char[][] orimap) {
		char tt [][] = new char[orimap.length][orimap[0].length];
		
		for (int i = 0; i < tt.length ; i++ ) {
			for (int j = 0; j < tt[i].length ; j++ ) {
				tt[i][j] = orimap[i][j];
			}
		}
		return(tt);
	}
	
	public boolean isGoodMap(char[][] map) {
		for (int i = 0; i < map.length ; i++ ) {
			for (int j = 0; j < map[i].length ; j++ ) {
				if (map[i][j] == '0') {
					int val = getOpenSpace(map,j,i);
					if ((val % 5) != 0) {
						return(false);
					}
				}
			}
		}
		for (int ii = 0; ii < map.length ; ii++ ) {
			for (int jj = 0; jj <map[ii].length ; jj++ ) {
				if (map[ii][jj] == 'z') {
					map[ii][jj] = '0';
				}
			}
		}
		return(true);	
	}
	
	public int getOpenSpace(char[][] map, int w, int h) {
		int result = 1;
		if (map[h][w] != '0') {
			return(0);
		}
		map[h][w] = 'z';
		if ((w + 1) < gridX) {
			result += getOpenSpace(map,w+1,h);
		}
		if ((h + 1) < gridY) {
			result += getOpenSpace(map,w,h+1);
		}
		if (w > 0) {
			result += getOpenSpace(map,w-1,h);
		}
		if (h > 0) {
			result += getOpenSpace(map,w,h-1);
		}
		return(result);		
	}
	
	public void showGrid(char[][] gridmap) {
		System.out.println("");
		for (int i = 0; i < gridmap.length ; i ++) {
			for (int j = 0; j < gridmap[i].length ; j++) {
				System.out.print(gridmap [i][j]);
			}
			System.out.println("");
		}
		System.out.println("");
	}

	public static char[][] doMirrorX(char[][] source) {
		char[][] target = new char[source.length][source[0].length];
		
		for (int i = 0; i < source.length ; i++ ) {
			for (int j = 0; j < source[i].length ; j++ ) {
				target[i][j] = source[i][source[i].length - j -1];
			}
		}
		return(target);		
	}
	
	public static char[][] doMirrorY(char[][] source) {		
		char[][] target = new char[source.length][source[0].length];
	
		for (int i = 0; i < source.length ; i++ ) {
			for (int j = 0; j < source[i].length ; j++ ) {
				target[i][j] = source[source.length - i -1][j];
			}
		}
		return(target);		
	}
	
	
	public void printCompleteSolutionForGrid(char[][] grid1) {
		char[][] grid2 = doMirrorX(grid1);
		char[][] grid3 = doMirrorY(grid1);
		char[][] grid4 = doMirrorX(grid3);
		logger.info("GRID-RESULT");
		for (int i = 0; i < grid1.length ; i++ ) {
			for (int j = 0; j < grid1[i].length ; j++ ) {
				bf1.setCharAt(j,grid1[i][j]);
				bf2.setCharAt(j,grid2[i][j]);
				bf3.setCharAt(j,grid3[i][j]);
				bf4.setCharAt(j,grid4[i][j]);
			}
			//System.out.println(bf1.toString() + "  " + bf2.toString() + "  " + bf3.toString() + "  " + bf4.toString());
			logger.info(bf1.toString() + "  " + bf2.toString() + "  " + bf3.toString() + "  " + bf4.toString());
		}
		logger.info("END RESULT");
		//System.out.println("");
		
	}	
}