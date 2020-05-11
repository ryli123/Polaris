package Display;

import java.beans.PropertyVetoException;
import java.io.IOException;

import javax.swing.*;

/**
 * The GUI class represents the final program for the rendering of the system.
 * Run this class for the program.
 * <p>
 * It incorporates both the Graph and Table classes to provide a 2-option
 * display, allowing the user to choose whether to view the graphical or tabular
 * representation of flight logging data.
 * 
 * @author Ryan Li, Justin Ye, Simhon Chourasia, Eric Zhao, Austin Lin
 */
@SuppressWarnings("serial")
public class GUI extends JFrame{

	private static Menu menu;
	
	public GUI() throws PropertyVetoException {
		// initialize graph, table components
		menu = new Menu();
		
		// set pane
		setContentPane(menu);
		setTitle("Project Data Logger");
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	// MAIN METHOD
	public static void main(String[] args) throws PropertyVetoException {
		GUI window = new GUI();
		window.setVisible(true);
		window.setResizable(true);
	}
}
