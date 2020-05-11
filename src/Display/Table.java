package Display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.*;

import au.com.bytecode.opencsv.CSVWriter;

@SuppressWarnings("serial")
public class Table extends JInternalFrame {

	private DefaultTableModel model;
	private JTable table;

	// CONSTRUCTOR
	public Table() {
		// idk yet
		model = new DefaultTableModel(setHeaders(), 0);
		table = new JTable(model);

		// set features
		table.setVisible(true);

		add(new JScrollPane(table));

		setTitle("Data Logger");
		setMaximizable(true);
		setIconifiable(true);
		setResizable(false);
		setClosable(false);
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		
		try {
			OpenCSVWriter.write();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// METHODS
	public Vector<String> setHeaders() {		// Pitch, Roll, Yaw, Fin Positions, Voltages
		Vector<String> row = new Vector<String>();
		row.add("t (ms)");
		row.add("Pitch (deg)");
		row.add("Roll (deg)");
		row.add("Yaw (deg)");
		row.add("Back Flap (deg)");
		row.add("Ailerons (deg)");
		row.add("Rudder (deg)");
		row.add("V_PM (mV)");
		row.add("V_RM (mV)");
		row.add("V_YM (mV)");
		row.add("V_PL (mV)");
		row.add("V_RL (mV)");
		row.add("V_YL (mV)");
		return row;
	}

	public void addRow(Vector<Integer> vals) {
		model.addRow(vals);
	}

	public void update(Vector<Integer> vals) {
		addRow(vals);
		OpenCSVWriter.add(vals);
	}
}

class OpenCSVWriter {
	private static final String STRING_ARRAY_SAMPLE = "data.csv";

	public static void write() throws IOException {
		try (
				CSVWriter csvWriter = new CSVWriter(new FileWriter(STRING_ARRAY_SAMPLE));

//				        	Writer writer = Files.newBufferedWriter(Paths.get(STRING_ARRAY_SAMPLE));
//				
//				            CSVWriter csvWriter = new CSVWriter(writer,
//				                    CSVWriter.DEFAULT_SEPARATOR,
//				                    CSVWriter.NO_QUOTE_CHARACTER,
//				                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
//				                    CSVWriter.DEFAULT_LINE_END);
				) {
			String[] headerRecord = {"t (ms)", "Pitch (deg)", "Roll (deg)", "Yaw (deg)", "Back Flap (deg)", "Ailerons (deg)", "Rudder (deg)", 
					"V_PM (mV)", "V_RM (mV)", "V_YM (mV)", "V_PL (mV)", "V_RL (mV)", "V_YL (mV)"};
			csvWriter.writeNext(headerRecord);
			csvWriter.close();
		}
	}

	public static void add(Vector<Integer> vals) {
		String s[] = new String[vals.size()];

		for (int i = 0; i < vals.size(); i++) {
			s[i] = vals.get(i) + "";
		}

		try {
			CSVWriter csvWriter = new CSVWriter(new FileWriter(STRING_ARRAY_SAMPLE, true));

//			Writer writer = Files.newBufferedWriter(Paths.get(STRING_ARRAY_SAMPLE));
//			
//            CSVWriter csvWriter = new CSVWriter(writer,
//                    CSVWriter.DEFAULT_SEPARATOR,
//                    CSVWriter.NO_QUOTE_CHARACTER,
//                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
//                    CSVWriter.DEFAULT_LINE_END);

			csvWriter.writeNext(s);
			csvWriter.close();
		} catch (IOException e) {}
	}
}
