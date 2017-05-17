package com.rhworkshop.msa.aggregators;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import com.rhworkshop.msa.controller.PuzzleBox;

public class PuzzleBoxAggregator implements AggregationStrategy {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Exchange aggregate(Exchange olde, Exchange newe) {
//		Object content = newExchange.getIn().getBody();		
		if(olde == null){
			ArrayList <PuzzleBox> al = new ArrayList();
			al.add((PuzzleBox) newe.getIn().getBody());
			newe.getIn().setBody(al);
			return newe;
		}
		
		((List)olde.getIn().getBody()).add((PuzzleBox) newe.getIn().getBody());
		return olde;
	}
}
