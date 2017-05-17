package com.rhworkshop.msa.model;

import java.io.Serializable;

//The BlockContainer class will act as the playbox for one Puzzler. It is able to give and take blocks
public class BlockContainer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7904942115340438627L;
	Block [] blocks;

//	private ResourceBundle bundle = null;
	
	//The constructor will make a complete playbox (=all blocks + all combinations
	public  BlockContainer() {
		int numblocks = Integer.parseInt(ResourceLoader.getString("blocks.numbloks"));
		blocks = new Block[numblocks];
		for (int i = 0; i < numblocks; i++ ) {
			blocks[i] = new Block(i);
		}
	}
	
	//Just a standalone test
	public static void main(String args[]) {
		BlockContainer test = new BlockContainer();
		test.printBlocks();
	}
	
	public void printBlocks() {
		for (int i = 0; i < blocks.length ;  i++) {
			System.out.println("Printing Block " + i);
			blocks[i].printBlock();
		}
	}
	
	public int getNrOfBlocks() {
		return(blocks.length);
		
	}
	
	public Block getBlock(int bnr) {
		if (bnr > blocks.length) {
			return(null);
		}
		return(blocks[bnr]);
	}
}