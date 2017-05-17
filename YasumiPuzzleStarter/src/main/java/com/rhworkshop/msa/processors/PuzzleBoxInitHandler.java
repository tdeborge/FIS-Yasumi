package com.rhworkshop.msa.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.rhworkshop.msa.controller.PuzzleBox;
import com.rhworkshop.msa.model.Block;
import com.rhworkshop.msa.model.BlockContainer;
import com.rhworkshop.msa.model.Grid;

public class PuzzleBoxInitHandler implements Processor {
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		
		PuzzleBox pb = (PuzzleBox) exchange.getIn().getBody();
		//placing the cross on the grid and if valid, pass it on and set a valid message handle.
		Grid pGrid = pb.getpGrid();
		BlockContainer pBox = pb.getpBox();
		Block bl = pBox.getBlock(0);
		// Mark block as used
		bl.setUsed(true);
		for (int k = 0; k < bl.getNumberOfShapes(); k++) {
			char[][] grid = new char[pGrid.getGridY()][pGrid.getGridX()];
			pGrid.initGrid(grid);
			if (pGrid.placeBlockOnGrid(grid, bl, k, pb.getXgrid(), pb.getYgrid(), 0)) {
				pb.setGrid(grid);
				pb.setDepth(1);
				exchange.getIn().setHeader("validMessage", true);
				//pGrid.showGrid(grid);
			}
			else{
				exchange.getIn().setHeader("validMessage",false);
			}
		}
		
	}
}



