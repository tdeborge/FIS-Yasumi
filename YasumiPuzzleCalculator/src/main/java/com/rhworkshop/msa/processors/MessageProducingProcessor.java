package com.rhworkshop.msa.processors;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;

import com.rhworkshop.msa.controller.PuzzleBox;

public class MessageProducingProcessor implements Processor {
	ProducerTemplate myProducer;
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		ArrayList <PuzzleBox> plist;
		
		plist = (ArrayList<PuzzleBox>) exchange.getIn().getBody();
		
		myProducer = exchange.getContext().createProducerTemplate();
		
		int i = 0;
		for (PuzzleBox puzzleBox : plist) {
			i++;
			//myProducer.sendBody("seda:producerReceiver","Hello From the Producing Template " + puzzleBox.toString());	
			if(plist.size() == i){
				myProducer.sendBodyAndHeader((String) exchange.getIn().getHeader("ForwardDestination"), puzzleBox, "Lastmessage", Boolean.TRUE);
			}
			else{
				myProducer.sendBodyAndHeader((String) exchange.getIn().getHeader("ForwardDestination"), puzzleBox, "Lastmessage", Boolean.FALSE);
			}
		}
	}
}
