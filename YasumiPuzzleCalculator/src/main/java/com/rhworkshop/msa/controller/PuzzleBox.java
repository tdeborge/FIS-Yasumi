package com.rhworkshop.msa.controller;

import java.io.Serializable;

import com.rhworkshop.msa.model.BlockContainer;
import com.rhworkshop.msa.model.Grid;

public class PuzzleBox implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7198757449017966396L;
	private int Xgrid , Ygrid;
	private BlockContainer pBox;
	private Grid pGrid;
	private char[][] grid; 
	private String puzzleUnit;
	private int depth;
	
	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public PuzzleBox(int x, int y){
		Xgrid = x;
		Ygrid = y;
		pBox = new BlockContainer();
		pGrid = new Grid();
		grid = new char[pGrid.getGridY()][pGrid.getGridX()];
	}
	
	public char[][] getGrid() {
		return grid;
	}

	public void setGrid(char[][] grid) {
		this.grid = grid;
	}

	public String toString(){
		return "pos of cross: X:" + Xgrid + " , Y:" + Ygrid; 
	}

	public int getXgrid() {
		return Xgrid;
	}

	public void setXgrid(int xgrid) {
		Xgrid = xgrid;
	}

	public int getYgrid() {
		return Ygrid;
	}

	public void setYgrid(int ygrid) {
		Ygrid = ygrid;
	}

	public BlockContainer getpBox() {
		return pBox;
	}

	public void setpBox(BlockContainer pBox) {
		this.pBox = pBox;
	}

	public Grid getpGrid() {
		return pGrid;
	}

	public void setpGrid(Grid pGrid) {
		this.pGrid = pGrid;
	}

	public String getPuzzleUnit() {
		return this.puzzleUnit;
	}

	public void setPuzzleUnit(String puzzleUnit) {
		this.puzzleUnit = puzzleUnit;
	}
	
	
}
