package com.rhworkshop.msa.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rhworkshop.msa.controller.PuzzleBox;
import com.rhworkshop.msa.model.Grid;

public class PuzzleGridLogger implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(PuzzleGridLogger.class);

	public void process(Exchange exchange) throws Exception {

		// Only show when single element is sent over

		if (exchange.getIn().getBody() instanceof PuzzleBox) {
			PuzzleBox pb = (PuzzleBox) exchange.getIn().getBody();
			Grid pGrid = pb.getpGrid();
			pGrid.showGrid(pb.getGrid());
			logger.warn("THIS PROCESSOR IS FOR DEBUGGING ONLY! SHOULD BE REMOVED!!!!");
		}

	}
}
