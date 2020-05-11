package Display;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.chart.*;
import org.jfree.chart.ChartFactory.*;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * The Graph class represents the graphical component of the interface.
 * <p>
 * It involves a graphical representation of the degree measures for rotation on
 * each of the 3 axes in relative real-time.
 */
@SuppressWarnings("serial")
public class Graph extends JInternalFrame {
	private Axis x, y, z;
	public int pitch, roll, yaw;
	int dialAreaWidth = 1900, dialAreaHeight = 350;
	JPanel graphs, dials;

	// CONSTRUCTOR
	public Graph() {
		setLayout(new BorderLayout());

		// set panels
		graphs = new JPanel();

		// set dials
		DrawArea graphicsArea = new DrawArea(dialAreaWidth, dialAreaHeight); // draw the graph for the first time

		// set graphs
		graphs.setLayout(new BoxLayout(graphs, BoxLayout.X_AXIS));
		x = new Axis("Pitch");
		y = new Axis("Roll");
		z = new Axis("Yaw");

		graphs.add(x);
		graphs.add(y);
		graphs.add(z);

		// add
		add(graphicsArea, BorderLayout.NORTH);
		add(graphs, BorderLayout.SOUTH);

		// set features
		setTitle("Flight Panel");
		setMaximizable(true);
		setIconifiable(true);
		setResizable(false);
		setClosable(false);
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
	}

	// METHODS
	public void update(Vector<Integer> vals) {
		int time = vals.get(0);

		pitch = vals.get(1);
		roll = vals.get(2);
		yaw = vals.get(3);

		x.update(time, pitch);
		y.update(time, roll);
		z.update(time, yaw);
	}

	void drawHorizontalLine(Graphics g, int origin_x, int origin_y, int radius, int degreeTilt) { // tilt is CCW from x+
		int end_x = (int) (origin_x + radius * Math.cos(Math.toRadians(degreeTilt)));
		int end_y = (int) (origin_y - radius * Math.sin(Math.toRadians(degreeTilt)));
		int start_x = (int) (origin_x - radius * Math.cos(Math.toRadians(degreeTilt)));
		int start_y = (int) (origin_y + radius * Math.sin(Math.toRadians(degreeTilt)));
		g.drawLine(start_x, start_y, end_x, end_y); // flat horizontal line
		g.fillOval(origin_x - 2, origin_y - 2, 4, 4);
		drawArrowLine(g, end_x, end_y, radius / 4, degreeTilt - 35);
		drawArrowLine(g, end_x, end_y, radius / 4, degreeTilt + 35);
	}

	void drawArrowLine(Graphics g, int start_x, int start_y, int radius, int degreeTilt) { // tilt is CCW from x+
		int end_x = (int) (start_x - radius * Math.cos(Math.toRadians(degreeTilt)));
		int end_y = (int) (start_y + radius * Math.sin(Math.toRadians(degreeTilt)));
		g.drawLine(start_x, start_y, end_x, end_y); // flat horizontal line
	}

	void drawArrow(Graphics g, int origin_x, int origin_y, int radius, int angle) {
		int bah = (int) ((radius + 10) / Math.sqrt(2));
		g.setColor(Color.red);
		g.drawString(((angle - 90) < 0? angle + 270: angle - 90) + "", origin_x - radius - 30, origin_y - radius - 5);
		if (angle / 100 >= 1) {
			g.drawOval(origin_x - radius + 15, origin_y - radius - 22, 8, 8);
		} else if (angle / 10 >= 1) {
			g.drawOval(origin_x - radius, origin_y - radius - 22, 8, 8);
		} else {
			g.drawOval(origin_x - radius - 10, origin_y - radius - 22, 8, 8);
		}

		g.drawOval(origin_x - radius - 10, origin_y - radius - 10, 2 * radius + 20, 2 * radius + 20);

		g.drawLine(origin_x - radius - 13, origin_y, origin_x - radius - 3, origin_y);// first circle dashes
		g.drawLine(origin_x + radius + 13, origin_y, origin_x + radius + 3, origin_y);
		g.drawLine(origin_x, origin_y - radius - 3, origin_x, origin_y - radius - 13);
		g.drawLine(origin_x, origin_y + radius + 13, origin_x, origin_y + radius + 3);

		g.drawLine(origin_x + bah + 2, origin_y - bah - 2, origin_x + bah - 4, origin_y - bah + 4);
		g.drawLine(origin_x + bah + 2, origin_y + bah + 2, origin_x + bah - 4, origin_y + bah - 4);
		g.drawLine(origin_x - bah - 2, origin_y - bah - 2, origin_x - bah + 4, origin_y - bah + 4);
		g.drawLine(origin_x - bah - 2, origin_y + bah + 2, origin_x - bah + 4, origin_y + bah - 4);

		drawHorizontalLine(g, origin_x, origin_y, radius, angle);
	}

	// method to generate graph
	public void drawGraphics(Graphics g) {
		Font sideFont = new Font("American Typewriter", Font.PLAIN, 25);
		g.setFont(sideFont);

		drawArrow(g,dialAreaWidth/6,180,7*dialAreaWidth/100, pitch + 90);
		drawArrow(g,dialAreaWidth/2,180,7*dialAreaWidth/100, roll + 90);
		drawArrow(g,dialAreaWidth*5/6,180,7*dialAreaWidth/100, yaw + 90);
	}

	// subclass of JPanel with paintComponent method to display drawn items
	class DrawArea extends JPanel {
		public DrawArea(int w, int h) {
			this.setPreferredSize(new Dimension(w, h)); // set preferred size
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			drawGraphics(g); // call draw graph method
		}
	}
}

@SuppressWarnings("serial")
class Axis extends JPanel {

	private GraphPanel graph;
	int axisVal;

	public Axis(String name) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAxis(name);

		graph = new GraphPanel(name);

		add(graph);
	}

	public void update(int time, int val) {
		graph.addValue(time, val);
	}

	private void setAxis(String name) {
		if (name.equals("Pitch"))
			axisVal = 1;
		else if (name.equals("Roll"))
			axisVal = 2;
		else if (name.equals("Yaw"))
			axisVal = 3;
	}
}

@SuppressWarnings("serial")
class GraphPanel extends JPanel {

	public XYSeries series;
	public JFreeChart lineChart;
	public ChartPanel cp;
	public XYDataset ds;
	XYSeriesCollection col;
	String axis;

	public GraphPanel(String axis) {
		this.axis = axis;
		ds = createDataset();
		lineChart = ChartFactory.createXYLineChart(axis, "t (ms)", "Angle (deg)", createDataset());
		lineChart.removeLegend();
		lineChart.setRenderingHints(
				new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
		lineChart.getPlot().setBackgroundPaint(Color.white);
		cp = new ChartPanel(lineChart);
		cp.setPreferredSize(new Dimension(600, 300));
		add(cp);
	}

	private XYDataset createDataset() {
		col = new XYSeriesCollection();

		series = new XYSeries("deg");
		series.add(0, 0);

		col.addSeries(series);

		return col;
	}

	public void addValue(int x, double y) {
		series.add(x, y);
	}

}