package com.rhworkshop.msa.aggregators;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rhworkshop.msa.controller.PuzzleBox;

public class PuzzleBoxCalculatorAggregator implements AggregationStrategy {
	private static final Logger logger = LoggerFactory.getLogger(PuzzleBoxCalculatorAggregator.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Exchange aggregate(Exchange olde, Exchange newe) {
		// Object content = newExchange.getIn().getBody();

//		if ((boolean) newe.getIn().getHeader("lastmessage")) {
//			// need to ignore this
//			if (olde == null) {
//				newe.getIn().setHeader("lastmessage", true);
//				ArrayList<PuzzleBox> al = new ArrayList();
//				newe.getIn().setBody(al);
//				return newe;
//			} else {
//				olde.getIn().setHeader("lastmessage", true);
//				return olde;
//			}
//		}

		if (olde == null) {
			logger.info("Aggregator (re)started");
			ArrayList<PuzzleBox> al = new ArrayList();
			if ((boolean) newe.getIn().getHeader("lastmessage")) {
				logger.warn("GETTING AN EMPTY CONSOLIDATION");
				newe.getIn().setBody(al);
			} else {
				logger.debug("ADDING A BOX");
				al.add((PuzzleBox) newe.getIn().getBody());
				newe.getIn().setBody(al);
			}

			return newe;
		} else {
			if ((boolean) newe.getIn().getHeader("lastmessage")) {
				olde.getIn().setHeader("lastmessage", true);
				logger.debug("Last Message of Bundle Recevied ... ignoring the content ...");

			} else {
				logger.debug("ADDING A BOX");
				((ArrayList<PuzzleBox>) olde.getIn().getBody()).add((PuzzleBox) newe.getIn().getBody());
			}
			return olde;
		}

	}
}
