package gambler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GamblerPanel extends AbstractGamblerPanel {
	private static final long serialVersionUID = -8874786662825257049L;
	
	JFreeChart lineChart;
	XYSeries series;
	XYSeriesCollection dataset = new XYSeriesCollection();
	
	public GamblerPanel(double winProb, double initAmount, double targetAmount, double gambleAmount) {
		super(winProb, initAmount, targetAmount, gambleAmount);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Button listeners
		JButton runOnce = new JButton ("Run Once");
		runOnce.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				double gamble = gambler.gamble();
				addGamble(gamble);
			}
		});
		
		JButton runToEnd = new JButton("Run To End");
		runToEnd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				runToEnd();
			}
		});
		
		JButton clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataset.removeAllSeries();
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
		buttonsPanel.add(runOnce);
		buttonsPanel.add(Box.createHorizontalStrut(strutWidth));
		buttonsPanel.add(Box.createHorizontalGlue());
		buttonsPanel.add(runToEnd);
		buttonsPanel.add(Box.createHorizontalStrut(strutWidth));
		buttonsPanel.add(Box.createHorizontalGlue());
		buttonsPanel.add(clear);
		buttonsPanel.add(Box.createHorizontalStrut(strutWidth));
		buttonsPanel.add(Box.createHorizontalGlue());
		
		// Charts
		lineChart = ChartFactory.createXYLineChart("", "", "", dataset);
		lineChart.setAntiAlias(false);
		ChartPanel chartPanel = new ChartPanel(lineChart);

		// Final Layout
		add(Box.createVerticalStrut(strutWidth));
		add(buttonsPanel);
		add(Box.createVerticalStrut(strutWidth));
		add(chartPanel);
		add(Box.createVerticalStrut(strutWidth));
		
		// Final config
		createGambler();		
		setVisible(true);
	}
	
	@Override
	public void createGambler(double winProb, double initProb, double targetProb, double gambleAmount) {
		super.createGambler(winProb, initProb, targetProb, gambleAmount);
		Integer key = new Integer(currentRow);
		
		// Add only if not in collection
		if(dataset.indexOf(key) == -1) {
			series = new XYSeries(key);
			dataset.addSeries(series);
		}
	}
	
	
	public void addGamble(double gamble) {
		if (series.getItemCount() == 0) {
			series.add(currentColumn, initAmount);
			currentColumn++;
		}
		series.add(currentColumn, gamble);
		currentColumn++;
	}
	
	public void runToEnd() {
		lineChart.setNotify(false);
		
		// Add gambles to graph
		List<Double> gambles = gambler.gambleUntilEnd(-1);
		
		// To reduce lag, add a maximum number gambles
		int incrementAmount = (int)(Math.ceil(gambles.size()/500.0) + 0.5);
		for (int i = 0; i < gambles.size(); i += incrementAmount) {
			addGamble(gambles.get(i));
			currentColumn += incrementAmount-1;
		}
		
		lineChart.setNotify(true);
		
		// Reset the gambler
		createGambler();
	}
}