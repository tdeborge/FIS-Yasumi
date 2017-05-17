package com.rhemea.puzzler.blocks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.rhemea.puzzler.blocks.model.Grid;
import com.rhemea.puzzler.blocks.model.Puzzler;

public class GPuzzle implements ActionListener, ChangeListener, IPuzzleCallBack, IPuzzleConstants {
	
	private Puzzler puz;
//	private Grid 	grid = new Grid();
	
	private ArrayList<char[][]> sol = new ArrayList<char[][]>();
	
	private JFrame mainWin;
	
	private GridViewer gv1, gv2, gv3, gv4;
	private JPanel pBlockContent;

	Box pRight, pCenter, pTop, pBottom, pLeft;
	Box pSlider, pButtons, pLabel, pViewer;
	
	private Thread t1, t2, t3, t4;
	
	JEditorPane html;
	
		
	private JButton btQuit, btNew, btLoad, btLoadAnimated;
	private JSlider slider;
	private JLabel  label;
	private JCheckBox cbAnimation;
	
	JFrame wLoading;
	
	public static void main(String args[]) {
		GPuzzle  prog = new GPuzzle();
		prog.initGui();
	}
	
	public void initGui() {
	
		wLoading = new JFrame("Intro screen");
		wLoading.setSize(new java.awt.Dimension(800,600));
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		wLoading.setVisible(false);
		wLoading.addWindowListener(new WindowAdapter()  {	
		});
		
		wLoading.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		html = new JEditorPane();
		html.setContentType("text/html");
		html.setEditable(false);
		wLoading.getContentPane().add(html);
		
		puz = new Puzzler(this);
		mainWin = new JFrame("Puzzel solver");
		mainWin.addWindowListener(new WindowAdapter()  {	
			public void windowClosing(WindowEvent we) {
				System.out.println("Closing Window");
				System.exit(0);
			}
		});
		
		btQuit = new JButton("Quit");
		btNew  = new JButton("New in Batch");
		btLoad = new JButton("Load");
		btLoadAnimated = new JButton("New Interactive");
		
		btQuit.setActionCommand("" + BT_QUIT);
		btNew.setActionCommand("" + BT_NEW);
		btLoad.setActionCommand("" + BT_LOAD);
		btLoadAnimated.setActionCommand("" + BT_LOADANIMATED);
		
		btQuit.addActionListener(this);
		btNew.addActionListener(this);
		btLoad.addActionListener(this);
		btLoadAnimated.addActionListener(this);
		
		gv1 = new GridViewer();
		gv2 = new GridViewer();
		gv3 = new GridViewer();
		gv4 = new GridViewer();
		
		t1 = new Thread(gv1);
		t2 = new Thread(gv2);
		t3 = new Thread(gv3);
		t4 = new Thread(gv4);
		
		
		slider = new JSlider(0,0,0);
		slider.addChangeListener(this);
		
		cbAnimation = new JCheckBox("Animate the blocks");
		cbAnimation.setSelected(false);
		
		label = new JLabel("Number of solutions: ");

		pButtons = new Box(BoxLayout.Y_AXIS);
		pButtons.add(btNew);
		pButtons.add(Box.createVerticalStrut(6));
		pButtons.add(btLoadAnimated);
		pButtons.add(Box.createVerticalStrut(6));
		pButtons.add(btLoad);
		pButtons.add(Box.createVerticalStrut(5));
		pButtons.add(btQuit);



		pRight = new Box(BoxLayout.X_AXIS);
		pRight.add(Box.createHorizontalStrut(10));
		pRight.add(pButtons);
		pRight.add(Box.createHorizontalStrut(10));
		
		pViewer = new Box(BoxLayout.X_AXIS);
		pViewer.add(Box.createHorizontalStrut(20));
		pViewer.add(gv1);
		pViewer.add(Box.createHorizontalStrut(20));
		pViewer.add(gv2);
		pViewer.add(Box.createHorizontalStrut(20));
		pViewer.add(gv3);
		pViewer.add(Box.createHorizontalStrut(20));
		pViewer.add(gv4);
		pViewer.add(Box.createHorizontalStrut(20));
		
		pCenter = new Box(BoxLayout.Y_AXIS);
		pCenter.add(Box.createVerticalStrut(20));
		pCenter.add(pViewer);
		pCenter.add(Box.createVerticalStrut(20));
		
		pLabel = new Box(BoxLayout.X_AXIS);
		pLabel.add(Box.createHorizontalStrut(20));
		pLabel.add(label);
		pLabel.add(Box.createHorizontalStrut(20));
		pLabel.add(cbAnimation);
		pLabel.add(Box.createHorizontalGlue());
		
		pTop = new Box(BoxLayout.Y_AXIS);
		pTop.add(Box.createVerticalStrut(10));
		pTop.add(pLabel);
		pTop.add(Box.createVerticalStrut(10));
		
		
		pLeft = new Box(BoxLayout.X_AXIS);
		pLeft.add(Box.createHorizontalStrut(10));
		
		pSlider = new Box(BoxLayout.X_AXIS);
		pSlider.add(Box.createHorizontalStrut(20));
		pSlider.add(slider);   
		pSlider.add(Box.createHorizontalStrut((int)(pRight.getPreferredSize().getWidth())));
		
		pBottom = new Box(BoxLayout.Y_AXIS);
		pBottom.add(Box.createVerticalStrut(10));
		pBottom.add(pSlider);
		pBottom.add(Box.createVerticalStrut(10));
		
		pBlockContent = new JPanel(new java.awt.BorderLayout());
		
		pBlockContent.add(pRight,"East");
		pBlockContent.add(pLeft,"West");
		pBlockContent.add(pCenter,"Center");
		pBlockContent.add(pTop,"North");
		pBlockContent.add(pBottom,"South");
		
		mainWin.getContentPane().add(pBlockContent);
		
		mainWin.pack();

		mainWin.setLocation((screenSize.width - mainWin.getSize().width)/2,5);

		wLoading.setLocation((screenSize.width - wLoading.getSize().width)/2,mainWin.getSize().height  + 20);
		
		mainWin.setVisible(true);

	}
	
	public void enableButtos(boolean doit) {
		btQuit.setEnabled(doit);
		btNew.setEnabled(doit);
		btLoad.setEnabled(doit);
		slider.setEnabled(doit);
		btLoadAnimated.setEnabled(doit);
	}
	
	public void actionPerformed(ActionEvent ae) {
		switch (Integer.parseInt(ae.getActionCommand())) {
			case BT_QUIT:
				System.exit(0);
				break;
			case BT_NEW:
				enableButtos(false);
				puz.setInteractive(false);
				Thread runner = new Thread(puz);
				runner.start();
				break;
			case BT_LOAD:
				sol = puz.getSolutionArray(false);
				if (sol.size() == 0) {
					puz.setInteractive(true);
					enableButtos(false);
					Thread runner2 = new Thread(puz);
					runner2.start();
					
				}
				else {
					label.setText("Number of solutions: " + (sol.size() * 4));
				}
				break;
			case BT_LOADANIMATED:
				enableButtos(false);
				puz.setInteractive(true);
				Thread interactive = new Thread(puz);
				interactive.start();
				break;
		}
		
		if (sol.size() == 0) {
			return;
		}
		
		slider.setMaximum(sol.size() - 1);
		slider.setMinimum(0);
		slider.setValue(0);
		gv1.setGrid((char[][])sol.get(slider.getValue()));
		gv2.setGrid(Grid.doMirrorX((char[][])sol.get(slider.getValue())));
		gv3.setGrid(Grid.doMirrorY((char[][])sol.get(slider.getValue())));
		gv4.setGrid(Grid.doMirrorY(Grid.doMirrorX((char[][])sol.get(slider.getValue()))));
		gv1.repaint();
		gv2.repaint();
		gv3.repaint();
		gv4.repaint();
		
	}
	
	
	public void stateChanged(ChangeEvent ce) {
		if (sol.size() == 0) {
			return;
		}
		
		gv1.setGrid((char[][])sol.get(slider.getValue()));
		gv2.setGrid(Grid.doMirrorX((char[][])sol.get(slider.getValue())));
		gv3.setGrid(Grid.doMirrorY((char[][])sol.get(slider.getValue())));
		gv4.setGrid(Grid.doMirrorY(Grid.doMirrorX((char[][])sol.get(slider.getValue()))));
		
		
		if (cbAnimation.isSelected() && !slider.getValueIsAdjusting()) {
			gv1.setAnimated(true);	
			gv2.setAnimated(true);	
			gv3.setAnimated(true);	
			gv4.setAnimated(true);	
	//		
			try {
				gv1.stopThePainting();
				gv2.stopThePainting();
				gv3.stopThePainting();
				gv4.stopThePainting();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			t1 = new Thread(gv1);
			t2 = new Thread(gv2);
			t3 = new Thread(gv3);
			t4 = new Thread(gv4);
			
			t1.start();
			t2.start();
			t3.start();
			t4.start();
		}
		
		else {
			if (!cbAnimation.isSelected()) {
				gv1.setAnimated(false);	
				gv2.setAnimated(false);	
				gv3.setAnimated(false);	
				gv4.setAnimated(false);	
				
				gv1.repaint();
				gv2.repaint();
				gv3.repaint();
				gv4.repaint();				
			}
		}
	}
	
	public void setResult(ArrayList<char[][]> res) {
		sol = res;
		label.setText("Number of solutions: " + (sol.size() * 4));
	}
	
	public void setLastResult(ArrayList<char[][]> res) {
		setResult(res);
		slider.setMaximum(sol.size() - 1);
		slider.setMinimum(0);
		slider.setValue(sol.size() - 1);
	}
	
	public void beginBlokNr(int i) {
	
		try {
		
			switch (i) {
				case 999:
					slider.setMaximum(sol.size() - 1);
					slider.setMinimum(0);
					slider.setValue(0);
					gv1.setGrid((char[][])sol.get(slider.getValue()));
					gv2.setGrid(Grid.doMirrorX((char[][])sol.get(slider.getValue())));
					gv3.setGrid(Grid.doMirrorY((char[][])sol.get(slider.getValue())));
					gv4.setGrid(Grid.doMirrorY(Grid.doMirrorX((char[][])sol.get(slider.getValue()))));
					gv1.repaint();
					gv2.repaint();
					gv3.repaint();
					gv4.repaint();
				
					enableButtos(true);
					wLoading.setVisible(false);
					break;
				case 0:
					System.out.println("got 0");
					//html.setPage(new java.net.URL("file", "", "stap1.html"));
					html.setPage(new java.net.URL("jar:file:/Users/tdeborge/workspace10/puzzler/target/puzzler-1.0.0-SNAPSHOT.jar!/stap1.html"));
					wLoading.setVisible(true);
					wLoading.validate();
					break;
				case 1:
					System.out.println("got " + i );
					//html.setPage(new java.net.URL("file", "","stap2.html"));
					html.setPage(new java.net.URL("jar:file:/Users/tdeborge/workspace10/puzzler/target/puzzler-1.0.0-SNAPSHOT.jar!/stap2.html"));
					html.validate();				
					break;
				case 2:
					System.out.println("got " + i );
					//html.setPage(new java.net.URL("file", "","stap3.html"));
					html.setPage(new java.net.URL("jar:file:/Users/tdeborge/workspace10/puzzler/target/puzzler-1.0.0-SNAPSHOT.jar!/stap3.html"));
					html.validate();				
					break;
				case 3:
					System.out.println("got " + i );
					//html.setPage(new java.net.URL("file", "","stap4.html"));
					html.setPage(new java.net.URL("jar:file:/Users/tdeborge/workspace10/puzzler/target/puzzler-1.0.0-SNAPSHOT.jar!/stap4.html"));
					html.validate();				
					break;
				case 4:
					System.out.println("got " + i );
					//html.setPage(new java.net.URL("file", "","stap5.html"));
					html.setPage(new java.net.URL("jar:file:/Users/tdeborge/workspace10/puzzler/target/puzzler-1.0.0-SNAPSHOT.jar!/stap5.html"));
					html.validate();				
					break;
				case 5:
					System.out.println("got " + i );
					//html.setPage(new java.net.URL("file", "","stap6.html"));
					html.setPage(new java.net.URL("jar:file:/Users/tdeborge/workspace10/puzzler/target/puzzler-1.0.0-SNAPSHOT.jar!/stap6.html"));
					html.validate();				
					break;
				case 6:
					System.out.println("got " + i );
					//html.setPage(new java.net.URL("file", "","stap7.html"));
					html.setPage(new java.net.URL("jar:file:/Users/tdeborge/workspace10/puzzler/target/puzzler-1.0.0-SNAPSHOT.jar!/stap7.html"));
					html.validate();				
					break;
				case 7:
					System.out.println("got " + i );
					//html.setPage(new java.net.URL("file", "","stap8.html"));
					html.setPage(new java.net.URL("jar:file:/Users/tdeborge/workspace10/puzzler/target/puzzler-1.0.0-SNAPSHOT.jar!/stap8.html"));
					html.validate();				
					break;
			}
			
			
		} catch (Exception e) {
			System.out.println("Exception !!!!");
			e.printStackTrace();
		}
	}
	
}