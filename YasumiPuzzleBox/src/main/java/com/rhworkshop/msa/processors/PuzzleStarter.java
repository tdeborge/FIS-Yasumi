package com.rhworkshop.msa.processors;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rhworkshop.msa.controller.PuzzleBox;
import com.rhworkshop.msa.model.Grid;

public class PuzzleStarter implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(PuzzleStarter.class);

	// Setting up a puzzler object that holds the first 8 block positions
	@Override
	public void process(Exchange ex) throws Exception {

		ArrayList<PuzzleBox> plist = new ArrayList<PuzzleBox>();
		// TODO Auto-generated method stub
		Grid pGrid = new Grid();

		// for (int i = pGrid.getYPos() -1; i < pGrid.getYPos(); i++) {
		// for (int j = pGrid.getXPos() -1; j < pGrid.getXPos(); j++) {
		logger.info("Starting a puzzleSession");
		for (int i = 0; i < pGrid.getYPos(); i++) {
			for (int j = 0; j < pGrid.getXPos(); j++) {
				logger.debug("Creating Box for coordinates [" + j + "," + i + "]");
				PuzzleBox pb = new PuzzleBox(j, i);
				if(ex.getIn().getHeader("returnDestination") == null){
					pb.setPuzzleUnit("NOREPLY:" + ex.getExchangeId());
				}
				else{
					pb.setPuzzleUnit((String) ex.getIn().getHeader("returnDestination"));
				}
				pb.setDepth(0);
				plist.add(pb);
			}
		}

		ex.getIn().setBody(plist);

	}

}
