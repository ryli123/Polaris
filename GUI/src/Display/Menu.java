package Display;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.swing.*;

@SuppressWarnings("serial")
public class Menu extends JDesktopPane implements ActionListener {

	private Graph graph;
	private Table table;
	private JInternalFrame start;
	private JButton button;
	private static Timer t;
	private int time, increment, end;
	private StreamConnection streamConnection;
	private OutputStream os;
	private InputStream is;

	// CONSTRUCTOR
	public Menu() throws PropertyVetoException {
		// initialize timer components
		increment = 1000;
		end = 120000;
		t = new Timer(increment, this);

		// set size
		Toolkit tk = Toolkit.getDefaultToolkit();  
		int xSize = ((int) tk.getScreenSize().getWidth());  
		int ySize = ((int) tk.getScreenSize().getHeight());  
		setSize(xSize,ySize);

		// initialize windows
		button = new JButton("Start Flight Logger");
		button.addActionListener(this);
		start = new JInternalFrame();
		start.add(button);

		graph = new Graph();
		table = new Table();

		// set pane
		add(start);
		add(graph);
		add(table);

		graph.setSize(xSize, ySize*13/20);
		table.setSize(xSize, ySize*19/80);

		graph.setLocation(0,0);
		table.setLocation(0,ySize*13/20);

		// set features
		start.setSize(xSize,ySize);
		start.setTitle("Menu");
		start.setMaximizable(true);
		start.setIconifiable(true);
		start.setResizable(false);
		start.setClosable(false);
		start.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);		
		start.setVisible(true);

		this.setBackground(Color.LIGHT_GRAY);
	}

	// METHODS
	public void startSession() throws Exception {
		go();
		t.start();
	}

	public void endSession() throws IOException {
		end();
		t.stop();
	}

	public int getTime() {
		return time;
	}

	private void empty(byte[] b) {
		b = new byte[0];
		b = new byte[1];
	}

	// sets up bluetooth connection at beginning of session
	private void go() throws Exception {
		//btspp://001403062AF3:1;authenticate=false;encrypt=false;master=false
		String hc05Url = "btspp://001403062AF3:1;authenticate=false;encrypt=false;master=false";
		streamConnection = (StreamConnection) Connector.open(hc05Url);
		os = streamConnection.openOutputStream();
		is = streamConnection.openInputStream();

		System.out.println("Connected!");
	}

	public void end() throws IOException {
		os.close();
		is.close();
		streamConnection.close();
	}

	// GUI ACTIONS
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(t)) {
			// read data as a string array
//			String s = "";
//			byte b[] = {' '};
//			boolean start = false;
//			while (true) {
//				try {
//					System.out.println("reading");
//					int size = is.read(b, 0, 1);
//					System.out.println("read: " + size);
//				} catch (IOException e1) {e1.printStackTrace();}
//				if (b[0] == '(')
//					start = true;
//				else if (start) {
//					if (b[0] != ')') {
//						s += new String(b);
//					} else {
//						System.out.println("nothing");
//						break;
//					}
//					empty(b);
//				}
//			}
//
//							byte b[] = new byte[59];
//							try {
//								is.read(b, 0, 59);
//								System.out.print("reading data: ");
//							} catch (IOException e1) {e1.printStackTrace();} 
//							String s = new String(b);
							String s = "006,023,105,-006,-023,-105,1500,1000,1000,1000,1000,1000";
				System.out.println(s);
				String vals[] = s.split(",");
				Vector<Integer> values = new Vector<Integer>();

				values.add(time);
				for (int i = 1; i < vals.length + 1; i++) {
					try {
						values.add(Integer.parseInt(vals[i - 1]));
					} catch(NumberFormatException e1) {
						values.add(0);
						System.out.println("Could not read number");}
				}

				// update components and time
				table.update(values);
				graph.update(values);
				graph.repaint();

				// check if simulation ended
				if (time >= end) {
					try {
						endSession();
					} catch (IOException e1) {e1.printStackTrace();}
				}
				else
					time += increment;
				// empty(b);
			}
			else if (e.getActionCommand().equals("Start Flight Logger")) {
				// set components to visible
				start.setVisible(false);
				graph.setVisible(true);
				table.setVisible(true);

				// start session
				try {
					startSession();
				} catch (Exception e1) {
					e1.printStackTrace();
					try {
						end();
					} catch (IOException e2) {System.out.println("Terminated.");}
				}
				time = 0;
			}

		}
	}
