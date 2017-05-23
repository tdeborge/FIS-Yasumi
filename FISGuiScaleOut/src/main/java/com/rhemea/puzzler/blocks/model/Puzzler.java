package com.rhemea.puzzler.blocks.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.rhemea.puzzler.blocks.gui.IPuzzleCallBack;

/*
It is possible to do a no-think program and let it run for a very long time. I have tried to implement
the following optimalisations:

1) Because all figures are different, one solution actually represents 4 solutions. It is also a horizontal,
vertical and diagonal mirror that has the solution.

2) Because of reason 1, I wanted to minimize the number of iterations in the calculations. Looking at the blocks,
I foud that the block with the cross is not rotatable. You always end up with the same figure ..... For that 
reason, I took this block and placed it in all possible ways in the first quadrant (avoiding a mirrored image
of this block). If I take that block + all the other combinations of all blocks, I can stop calculating afterwards
as I also have all mirrors in the solution.

3) It is important to know when to stop trying new blocks on the grid. As all blocks are builded out of 5 grid-cells,
you can stop placing new block if the number of consecutive free cells is not dividable by 5. This was a pointer I 
took from Jan Sipke van der Veen.  
*/

//This is the actual class that performs the actions and hopefully comes to a lot of solutions
public class Puzzler implements Runnable {

	private boolean showSolution = false;
	// private int counter;

	private ArrayList<char[][]> solutions = null;

	private IPuzzleCallBack pcb = null;

	// private ResourceBundle bundle = null;
	private String solutionName = "lastCheck";
	// boolean sol = false;

	public Puzzler() {
		// Initializing the playfield and puzzle blocks for the Puzzler
		// pBox = new BlockContainer();
		// pGrid = new Grid();
	}

	public Puzzler(IPuzzleCallBack thePcb) {
		this();
		pcb = thePcb;
	}

	public void run() {
		int counter = 0;
		solutions = new ArrayList<char[][]>();

		// Send start message ... get results ... continue when completed

		ConnectionFactory cf = new ActiveMQConnectionFactory(ResourceLoader.getString("blocks.jms.username"), ResourceLoader.getString("blocks.jms.password"), ResourceLoader.getString("blocks.jms.url"));

		Connection connection;
		try {
			connection = cf.createConnection();
			// connection.setClientID("MyQueueConsumerTest");
			Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			// Destination destination =
			// session.createQueue("tbo.qa.test.yasumi.solutionEntry");
			Destination tdestination = session.createTemporaryQueue();
			// Destination tmpQ = session.createTemporaryQueue();
			MessageConsumer consumer = session.createConsumer(tdestination);

			connection.start();

			Message smsg = session.createMessage();
			smsg.setStringProperty("PuzzleID", "12345");
			smsg.setStringProperty("returnDestination", tdestination.toString().substring("temp-queue://".length()));
			session.createProducer(session.createQueue(ResourceLoader.getString("blocks.jms.destination"))).send(smsg);

			boolean complete = false;
			boolean fail = false;
			while (!complete && !fail) {
				ObjectMessage msg = (ObjectMessage) consumer.receive(30000);
				if (msg == null) {
					fail = true;
					break;
				}

				@SuppressWarnings("unchecked")
				ArrayList<char[][]> solution = (ArrayList<char[][]>) msg.getObject();
				solutions.addAll(solution);
				msg.acknowledge();

				if (solutions.size() * 4 >= 9356) {
					complete = true;
				}

				if (this.pcb != null) {
					try {
						javax.swing.SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								pcb.setLastResult(solutions);
							}
						});
					} catch (Exception e) {
					}

				}
			}
			session.close();
			connection.close();

		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		this.writeSolutionHolderObject(solutions);

		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

				public void run() {
					System.out.println("SOLUTIONS ==" + solutions.size());
					if(solutions.size() != 0){
						pcb.setResult(solutions);
						pcb.beginBlokNr(999);				
					}
				}
			});
		} catch (

		Exception e) {
		}

	}

	public static void main(String args[]) {
		System.out.println("Starting the Puzzler");
	}

	public void initSolutions() {
		solutions = new ArrayList<char[][]>();
	}

	@SuppressWarnings("unchecked")
	public void readSolutions() {
		solutions = (ArrayList<char[][]>) this.readSolutionHolderObject();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<char[][]> getSolutionArray(boolean newList) {
		if (newList) {
			solutions = new ArrayList<char[][]>();
		} else {
			solutions = (ArrayList<char[][]>) this.readSolutionHolderObject();
		}
		return (solutions);
	}

	private void writeSolutionHolderObject(Object obj) {
		try {
			FileOutputStream out = new FileOutputStream(solutionName);
			ObjectOutputStream s = new ObjectOutputStream(out);
			s.writeObject(obj);
			s.close();
			out.close();
		} catch (Exception e) {
			System.out.println("Error writing Object " + obj + "\n Message = " + e.getMessage());
			System.err.println("Error writing Object " + obj + "\n Message = " + e.getMessage());
			e.printStackTrace();
		}
	}

	private Object readSolutionHolderObject() {
		try {

			File test = new File(solutionName);
			if (!test.exists()) {
				Object ret = new ArrayList<char[][]>();
				return (ret);

			}
			java.io.FileInputStream fis = new java.io.FileInputStream(test);
			java.io.ObjectInputStream ois = new java.io.ObjectInputStream(fis);
			Object readObj = ois.readObject();
			fis.close();
			return readObj;
		} catch (Exception e) {
			System.out.println("Error reading Object " + solutionName + "\n Message = " + e.getMessage());
			System.err.println("Error reading Object " + solutionName + "\n Message = " + e.getMessage());
			e.printStackTrace();
		} // return null

		return null;
	}

	public void setInteractive(boolean showit) {
		showSolution = showit;
	}
}