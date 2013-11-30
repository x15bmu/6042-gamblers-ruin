package gambler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;

public class MultiGamblerPanel extends AbstractGamblerPanel {
	private static final long serialVersionUID = -8874786662825257049L;
	
	JFreeChart barChart;
	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	
	public MultiGamblerPanel(double winProb, double initAmount, double targetAmount, double gambleAmount) {
		super(winProb, initAmount, targetAmount, gambleAmount);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));		
		
		// Button listeners
		JButton run = new JButton("Run");
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				run();
			}
		});
		
		JButton clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataset.clear();
				currentRow = 0;
				createGambler();
			}
		});
		
		// Button layouts
		final int strutWidth = 10;
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.add(Box.createHorizontalStrut(strutWidth));
		buttonsPanel.add(Box.createHorizontalGlue());
		buttonsPanel.add(run);
		buttonsPanel.add(Box.createHorizontalStrut(strutWidth));
		buttonsPanel.add(Box.createHorizontalGlue());
		buttonsPanel.add(clear);
		buttonsPanel.add(Box.createHorizontalStrut(strutWidth));
		buttonsPanel.add(Box.createHorizontalGlue());
		
		// Chart
		barChart = ChartFactory.createBarChart3D("", "Parameters", "Long-Term Win Probability", dataset);
		barChart.setAntiAlias(false);
		BarRenderer3D renderer = new BarRenderer3D();
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);
		barChart.getCategoryPlot().setRenderer(renderer);
		ChartPanel chartPanel = new ChartPanel(barChart);
		
		// Final layout
		add(Box.createVerticalStrut(strutWidth));
		add(buttonsPanel);
		add(Box.createVerticalStrut(strutWidth));
		add(chartPanel);
		add(Box.createVerticalStrut(strutWidth));
		
		// Final config
		createGambler(winProb, initAmount, targetAmount, gambleAmount);
		setVisible(true);
	}
	
	public void addGamble(double gamble) {
		dataset.addValue(gamble, gambler.toString() + " (" + currentRow + ")", new Integer(currentColumn));
		currentColumn++;
	}
	
	public void run() {
		barChart.setNotify(false);
		
		addGamble(gambler.calcTargetProb());
		
		barChart.setNotify(true);
		
		// Reset the gambler
		createGambler();
	}
}
