package com.rhworkshop.msa.processors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rhworkshop.msa.controller.PuzzleBox;
import com.rhworkshop.msa.model.Block;
import com.rhworkshop.msa.model.BlockContainer;
import com.rhworkshop.msa.model.Grid;
import com.rhworkshop.msa.model.ResourceLoader;

public class PuzzleSolverForwarder implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(PuzzleSolverForwarder.class);

	@SuppressWarnings({ })
	@Override
	public void process(Exchange exchange) throws Exception {

		int bucketSize, computeLevel;

		logger.debug("We have reached the PuzzleSolverForwarder class");

		if (exchange.getIn().getHeader("Puzzler.Process.Tuning.BucketSize") == null
				|| exchange.getIn().getHeader("Puzzler.Process.Tuning.ComputeLevel") == null) {
			logger.debug("No Headers Set on the incoming message ... looking for BucketSize and ComputeLevel");
			bucketSize = 200;
			computeLevel = 3;
		} else {
			logger.debug("Puzzler.Process.Tuning.BucketSize: "
					+ exchange.getIn().getHeader("Puzzler.Process.Tuning.BucketSize"));
			logger.debug("Puzzler.Process.Tuning.ComputeLevel: "
					+ exchange.getIn().getHeader("Puzzler.Process.Tuning.ComputeLevel"));
			logger.debug("PUZZLEDEPTH = " + exchange.getIn().getHeader("puzzledepth"));
			bucketSize = Integer.parseInt((String) exchange.getIn().getHeader("Puzzler.Process.Tuning.BucketSize"));
			computeLevel = Integer.parseInt((String) exchange.getIn().getHeader("Puzzler.Process.Tuning.ComputeLevel"));
		}

		if (computeLevel > (int) exchange.getIn().getHeader("puzzledepth")) {
			doMessageComputation(exchange, bucketSize, computeLevel);
		} else {
			doMessageFinalization(exchange, bucketSize, computeLevel);
		}

		System.gc();
		logger.debug("Free Mem Stuff: " + Runtime.getRuntime().freeMemory());
		logger.debug("Total Mem Stuff: " + Runtime.getRuntime().totalMemory());
		logger.debug("Max Mem Stuff: " + Runtime.getRuntime().maxMemory());
	}

	@SuppressWarnings("unchecked")
	private void doMessageFinalization(Exchange exchange, int bucketSize, int computeLevel) {
		logger.debug("COMPUTATION LEVEL REACHED!!!!!");
		exchange.getIn().setHeader("puzzledepth", (int) exchange.getIn().getHeader("puzzledepth") + 50);
		
		ArrayList <char[][]> solutions = new ArrayList<char [][]>();

		ArrayList<PuzzleBox> al = null;
		ArrayList<PuzzleBox> boxForwarder = new ArrayList<PuzzleBox>();

		if (exchange.getIn().getBody() instanceof PuzzleBox) {
			al = new ArrayList<PuzzleBox>();
			al.add((PuzzleBox) exchange.getIn().getBody());
		} else {
			al = ((ArrayList<PuzzleBox>) exchange.getIn().getBody());
		}
		logger.debug("Arraylist received with " + al.size() + " PuzzleBoxes Included");
		
		for (PuzzleBox pb : al) {
			// PuzzleBox pb = (PuzzleBox) exchange.getIn().getBody();
			Grid pGrid = pb.getpGrid();
			BlockContainer pBox = pb.getpBox();
			
			placeNextBlockOnGrid(pb.getGrid(), pb.getDepth(), solutions,pGrid,pBox);
		}
		
		logger.debug("total solutions = " + solutions.size());
		//Preparing the return message ;)
		//Setting the headers to find the way back to the requestor
		if(al.size() > 0){		
			exchange.getIn().setHeader("PuzzleUnit", al.get(0).getPuzzleUnit());
			exchange.getIn().setHeader("puzzledepth", Integer.parseInt(ResourceLoader.getString("blocks.numbloks")));
			if(al.get(0).getPuzzleUnit().startsWith("NOREPLY:")){
				logger.debug("NO DESTINATION SET");
			}
			else{
				logger.debug("Setting the DESTINATION");
				exchange.getIn().setHeader("CamelJmsDestinationName", al.get(0).getPuzzleUnit());
			}
		}
		exchange.getIn().setBody(solutions);
	}

	@SuppressWarnings("unchecked")
	private void doMessageComputation(Exchange exchange, int bucketSize, int computeLevel) {

		int piterations = 0;
		int psuccess = 0;
		int pfail = 0;

		ArrayList<PuzzleBox> al = null;
		ArrayList<PuzzleBox> boxForwarder = new ArrayList<PuzzleBox>();

		if (exchange.getIn().getBody() instanceof PuzzleBox) {
			al = new ArrayList<PuzzleBox>();
			al.add((PuzzleBox) exchange.getIn().getBody());
		} else {
			al = ((ArrayList<PuzzleBox>) exchange.getIn().getBody());
		}
		logger.debug("Arraylist received with " + al.size() + " PuzzleBoxes Included");

		for (PuzzleBox pb : al) {
			Grid pGrid = pb.getpGrid();
			BlockContainer pBox = pb.getpBox();

			// Calculating the different next options ...

			int depth = pb.getDepth();
			logger.debug("Depth received: " + pb.getDepth());

			if (depth == Integer.parseInt(ResourceLoader.getString("blocks.numbloks"))) { // This
																							// is
																							// the
																							// finalization
																							// clause
																							// for
																							// the
																							// recursive
																							// calculations.
				// Solution is found ...
				// Create a solution Object and forward it to the Solution
				// Destination.
				logger.debug("solution Found:");
				// pGrid.printCompleteSolutionForGrid(pb.getGrid());
				// pGrid.showGrid(pb.getGrid());
				// for (int bloknr = 0; bloknr < pBox.getNrOfBlocks(); bloknr++)
				// {
				// logger.info("BlockNummer: " + bloknr + " state used:" +
				// pb.getpBox().getBlock(bloknr).isUsed());
				// }
				boxForwarder.add(pb);
				continue;
			}

			// Checking for the first free cell in the grid to place the block.
			boolean cellfound = false;
			char[][] grid = pb.getGrid();
			int px = 0;
			int py = 0;

			pb.setDepth(pb.getDepth() + 1);

			for (int quady = 0; quady < pGrid.getGridY(); quady++) {
				for (int quadx = 0; quadx < pGrid.getGridX(); quadx++) {
					if (grid[quady][quadx] == '0') {
						cellfound = true;
						px = quadx;
						py = quady;
						break;
					}
				}
				if (cellfound) {
					break;
				}
			}

			// When a free cell is found, we need to try every free block on it.
			for (int bloknr = 0; bloknr < pBox.getNrOfBlocks(); bloknr++) {
				logger.debug("Testing Block number: " + bloknr);
				if (pBox.getBlock(bloknr).isUsed()) {
					continue;
				}
				// Here we have found a free block.
				// We need to mark it now as used
				pBox.getBlock(bloknr).setUsed(true);

				// If we try a block, we need to try all the different shapes of
				// this block.
				for (int k = 0; k < pBox.getBlock(bloknr).getNumberOfShapes(); k++) {
					logger.debug("Testing Shape: " + k);
					char[][] mp = pBox.getBlock(bloknr).getShapeMap(k);
					// If the first cell of the block is not empty, we can try
					// the block
					if (mp[0][0] != '0') {
						char[][] grid2 = pGrid.copyGridmap(grid);
						piterations++;
						if (pGrid.placeBlockOnGrid(grid2, pBox.getBlock(bloknr), k, px, py, bloknr)) {
							psuccess++;
							PuzzleBox pb2 = (PuzzleBox) copy(pb);
							pb2.setGrid(grid2);
							handleMessageForwarding(pb2, boxForwarder);
						} else {
							pfail++;
						}
					} else {
						// if the first cell of a block is empty, it is useless
						// to try it on the exact location
						// because in that way we are generating gaps that never
						// will be filled. The solution
						// is to shift the block horizontally to the left until a
						// cell is occupied and afterwards
						// we shif the cell up to the top and calculate the same
						int i = 0;
						for (i = 0; i < pBox.getBlock(bloknr).getShapeHeight(k); i++) {
							if (mp[i][0] != '0') {
								break;
							}
						}
						char[][] grid2 = pGrid.copyGridmap(grid);
						piterations++;
						if (pGrid.placeBlockOnGrid(grid2, pBox.getBlock(bloknr), k, px, py - i, bloknr)) {
							psuccess++;
							PuzzleBox pb2 = (PuzzleBox) copy(pb);
							pb2.setGrid(grid2);
							handleMessageForwarding(pb2, boxForwarder);
						} else {
							pfail++;
						}

						int j = 0;
						for (j = 0; j < pBox.getBlock(bloknr).getShapeWidth(k); j++) {
							if (mp[0][j] != '0') {
								break;
							}
						}
						grid2 = pGrid.copyGridmap(grid);
						piterations++;
						if (pGrid.placeBlockOnGrid(grid2, pBox.getBlock(bloknr), k, px - j, py, bloknr)) {
							psuccess++;
							PuzzleBox pb2 = (PuzzleBox) copy(pb);
							pb2.setGrid(grid2);
							handleMessageForwarding(pb2, boxForwarder);
						} else {
							pfail++;
						}
					}
				}
				pBox.getBlock(bloknr).setUsed(false);
			}

			// ALL IS DONE

		}
		if (psuccess > 0) {
			logger.debug("Iterations: " + piterations + " - Success: " + psuccess + " - Fails: " + pfail
					+ " --- last measured depth = " + exchange.getIn().getHeader("puzzledepth"));
			// logger.warn("Sending: " + boxForwarder.size() + " --- last
			// measured depth = "
			// + exchange.getIn().getHeader("puzzledepth") + " the id: ");
			// exchange.getIn().setHeader("ContinueMessage", Boolean.TRUE);
		} else {
			logger.warn("Nothing was generated to forward!!!!");
			// exchange.getIn().setHeader("ContinueMessage", Boolean.FALSE);
		}
		if (boxForwarder.size() < 400) {
			exchange.getIn().setBody(boxForwarder);
			exchange.getIn().setHeader("puzzledepth", (int) exchange.getIn().getHeader("puzzledepth") + 1);
			exchange.getIn().setHeader("BucketOverflow", Boolean.FALSE);
		} else {
			@SuppressWarnings("rawtypes")
			ArrayList list = (ArrayList<?>) chopped(boxForwarder, bucketSize);
			exchange.getIn().setBody(list);
			exchange.getIn().setHeader("puzzledepth", (int) exchange.getIn().getHeader("puzzledepth") + 1);
			exchange.getIn().setHeader("BucketOverflow", Boolean.TRUE);
		}
	}

	private void handleMessageForwarding(PuzzleBox pb2, ArrayList<PuzzleBox> boxForwarder) {
		logger.debug("Next level found:");
		boxForwarder.add(pb2);
	}

	public static Object copy(Object orig) {
		Object obj = null;
		try {
			// Write the object out to a byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(orig);
			out.flush();
			out.close();

			// Make an input stream from the byte array and read
			// a copy of the object back in.
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
			obj = in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return obj;
	}

	// chops a list into non-view sublists of length L
	static <T> List<List<T>> chopped(List<T> list, final int L) {
		List<List<T>> parts = new ArrayList<List<T>>();
		final int N = list.size();
		for (int i = 0; i < N; i += L) {
			parts.add(new ArrayList<T>(list.subList(i, Math.min(N, i + L))));
		}
		return parts;
	}

	public void placeNextBlockOnGrid(char[][] grid, int quadrant, ArrayList<char[][]> solutions, Grid pGrid, BlockContainer pBox) {

		// find next available block
		int px = 0;
		int py = 0;

		// find next free block to try.
		if (quadrant == Integer.parseInt(ResourceLoader.getString("blocks.numbloks"))) {
			if (solutions.contains(grid)) {
				System.out.println("ALLERT:  DUPLICATE FOUND");
			} else {
				solutions.add(grid);
				//pGrid.showGrid(grid);				
				//pGrid.printCompleteSolutionForGrid(grid);
			}
			return;
		}

		// Checking for the first free cell in the grid to place the block.
		boolean cellfound = false;

		for (int quady = 0; quady < pGrid.getGridY(); quady++) {
			for (int quadx = 0; quadx < pGrid.getGridX(); quadx++) {
				if (grid[quady][quadx] == '0') {
					cellfound = true;
					px = quadx;
					py = quady;
					break;
				}
			}
			if (cellfound) {
				break;
			}
		}

		// When a free cell is found, we need to try every free block on it.
		for (int bloknr = 0; bloknr < pBox.getNrOfBlocks(); bloknr++) {
			Block bl = pBox.getBlock(bloknr);
			if (bl.isUsed()) {
				continue;
			}
			// Here we have found a free block.
			// We need to mark it now as used
			bl.setUsed(true);

			// If we try a block, we need to try all the different shapes of
			// this block.
			for (int k = 0; k < bl.getNumberOfShapes(); k++) {
				char[][] mp = bl.getShapeMap(k);
				// If the first cell of the block is not empty, we can try the
				// block
				if (mp[0][0] != '0') {
					char[][] grid2 = pGrid.copyGridmap(grid);
					if (pGrid.placeBlockOnGrid(grid2, bl, k, px, py, bloknr)) {
						bl.getShape(k).shapePlaced();
						placeNextBlockOnGrid(grid2, quadrant + 1, solutions, pGrid, pBox);
					}
				} else {
					// if the first cell of a block is empty, it is useless to
					// try it on the exact location
					// because in that way we are generating gaps that never
					// will be filled. The solution
					// is to shift the block horizontally to the left until a
					// cell is occupied and afterwards
					// we shif the cell up to the top and calculate the same
					int i = 0;
					for (i = 0; i < bl.getShapeHeight(k); i++) {
						if (mp[i][0] != '0') {
							break;
						}
					}
					char[][] grid2 = pGrid.copyGridmap(grid);
					if (pGrid.placeBlockOnGrid(grid2, bl, k, px, py - i, bloknr)) {
						bl.getShape(k).shapePlaced();
						placeNextBlockOnGrid(grid2, quadrant + 1, solutions, pGrid, pBox);
					}

					int j = 0;
					for (j = 0; j < bl.getShapeWidth(k); j++) {
						if (mp[0][j] != '0') {
							break;
						}
					}
					grid2 = pGrid.copyGridmap(grid);
					if (pGrid.placeBlockOnGrid(grid2, bl, k, px - j, py, bloknr)) {
						bl.getShape(k).shapePlaced();
						placeNextBlockOnGrid(grid2, quadrant + 1, solutions, pGrid, pBox);
					}
				}
			}
			bl.setUsed(false);
		}

	}

}
