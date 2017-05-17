package com.rhemea.puzzler.blocks.model;


import java.util.ArrayList;

public class Block {

	//as a block is part of a BlockContainer, the object needs to be able to indicate when it is used
	//in order to have only one copy of the block on the table.
	private boolean 	used = false;
	
//	private int		blocknumber =  0;
	
	
	//A block can be rotated and therefor it has a lot of different shapes.
	private Shape[] 	shapes = null;
	
	//Initialisation of a Block read from the properties file and the creation of all it's different shapes.
	public Block(int num) {
		ArrayList<Shape> tset = new ArrayList<Shape>();
//		this.blocknumber = num;
		shapes = new Shape[Integer.parseInt(ResourceLoader.getString("blocks.block" + num + ".shape.rotations"))];
		for (short i = 0; i < shapes.length ; i++ ) {
			int sh = Integer.parseInt(ResourceLoader.getString("blocks.block"+ num +".shape"+ i +".height"));
			int sw = Integer.parseInt(ResourceLoader.getString("blocks.block"+ num +".shape"+ i +".width"));
			shapes[i] = new Shape(sw, sh, i,num);
			
			if (tset.contains(shapes[i])) {
				System.out.println("DUPLICATE BLOCK");
			}
			else {
				tset.add(shapes[i]);
			}
		}
	}
	
	//If the block is placed on the grid, make it unavailable to do so again.
	public void setUsed(boolean fl) {
		used = fl;
	}
	
	public boolean isUsed() {
		return(used);	
	}
	
	//As a shape has different X and Y values through mirroring, these attributes can be requested.
	public int getShapeWidth(int snr) {
		return(shapes[snr].getWidth());
	}

	//As a shape has different X and Y values through mirroring, these attributes can be requested.
	public int getShapeHeight(int snr) {
		return(shapes[snr].getHeight());
	}
	
	//This method returns on of the physical shapes of the block..
	public char[][] getShapeMap(int snr) {
		return(shapes[snr].getBlockMap());
	}

	
	//This method prints all the shapes in a block
	public void printBlock() {
		for (int i = 0; i < shapes.length ; i++ ) {
			System.out.println("Shape #" + i );
			char [][] mp = shapes[i].map;
			for (int j = 0; j < mp.length ; j++ ) {
				for (int k = 0 ; k < mp[j].length ; k++ ) {
					System.out.print(mp[j][k]);
				}
				System.out.println("");
			}
			System.out.println("");
		}
	}
	
	//this method prints one shape of a block
	public void printBlock(int shape) {
		System.out.println("Shape #" + shape );
		char [][] mp = shapes[shape].map;
		for (int j = 0; j < mp.length ; j++ ) {
			for (int k = 0 ; k < mp[j].length ; k++ ) {
				System.out.print(mp[j][k]);
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	//Giving the caller information on how many shapes a block can have
	public int getNumberOfShapes() {
		return(shapes.length);
	}
	
	public Shape getShape(int i) {
		return(shapes[i]);
		
	}
	
	//This class will hold a shap for a block
	class Shape {
		private int 		width = 0;
		private int 		height = 0;
		private int 		numset = 0;
		
		private char [] [] map;
		
		public Shape(int w, int h, int shapenr, int blocknr) {
			this.width = w;
			this.height = h;
			map = new char [h][w];
			
			for (int i = 0; i < h ; i++ ) {
				String rec = ResourceLoader.getString("blocks.block" + blocknr + ".shape" + shapenr + ".row" + i);
				for (int j = 0; j < w ; j++ ) {
					map [i][j] = rec.charAt(j * 2);
				}
			}
		}
		
		public int getHeight() {
			return(height);
		}	
		
		public int getWidth() {
			return(width);
		}
		
		public char[][] getBlockMap() {
			return(map);
		}
		
		public void shapePlaced() {
			numset++;
		}
		
		public int getShapePlaced() {
			return(numset);
			
		}
	}
	
	
}

