package gambler;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class GamblerView2 extends JFrame {
	private static final long serialVersionUID = -8874786662825257049L;
	
	GamblerModel gambler;
	private int currentRow = 0;
	private int currentColumn = 0;
	
	JFormattedTextField winProb;
	JFormattedTextField initAmount;
	JFormattedTextField targetAmount;
	JFormattedTextField gambleAmount;
	JFreeChart lineChart;
	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	
	public GamblerView2() {
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
		
		JLabel winProbLabel = new JLabel("Probability of Winning: ");
		JLabel initAmountLabel = new JLabel("Initial Amount: ");
		JLabel targetAmountLabel = new JLabel("Target Amount: ");
		JLabel gambleAmountLabel = new JLabel("Amount to Gamble: ");
		
		// TODO Fix winProb format so it accepts decimals and fractions
		winProb = new JFormattedTextField(NumberFormat.getPercentInstance());
		initAmount = new JFormattedTextField(NumberFormat.getCurrencyInstance());
		targetAmount = new JFormattedTextField(NumberFormat.getCurrencyInstance());
		gambleAmount = new JFormattedTextField(NumberFormat.getCurrencyInstance());
		addListeners();
		
		winProb.setValue(0.25);
		initAmount.setValue(1000);
		targetAmount.setValue(1100);
		gambleAmount.setValue(1);
		
		winProb.setMaximumSize(new Dimension(Integer.MAX_VALUE, winProb.getPreferredSize().height));
		initAmount.setMaximumSize(new Dimension(Integer.MAX_VALUE, initAmount.getPreferredSize().height));
		targetAmount.setMaximumSize(new Dimension(Integer.MAX_VALUE, targetAmount.getPreferredSize().height));
		gambleAmount.setMaximumSize(new Dimension(Integer.MAX_VALUE, gambleAmount.getPreferredSize().height));
		
		final int strutWidth = 10;
		
		textPanel.add(Box.createHorizontalStrut(strutWidth));
		textPanel.add(winProbLabel);
		textPanel.add(winProb);
		textPanel.add(Box.createHorizontalStrut(strutWidth));
		textPanel.add(initAmountLabel);
		textPanel.add(initAmount);
		textPanel.add(Box.createHorizontalStrut(strutWidth));
		textPanel.add(targetAmountLabel);
		textPanel.add(targetAmount);
		textPanel.add(Box.createHorizontalStrut(strutWidth));
		textPanel.add(gambleAmountLabel);
		textPanel.add(gambleAmount);
		textPanel.add(Box.createHorizontalStrut(strutWidth));
		
		
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
		
		lineChart = ChartFactory.createLineChart("", "", "", dataset);
		lineChart.setAntiAlias(false);
		ChartPanel chartPanel = new ChartPanel(lineChart);
		
		add(Box.createVerticalStrut(strutWidth));
		add(textPanel);
		add(Box.createVerticalStrut(strutWidth));
		add(buttonsPanel);
		add(Box.createVerticalStrut(strutWidth));
		add(chartPanel);
		add(Box.createVerticalStrut(strutWidth));
		
		pack();
		
		createGambler();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void createGambler() {
		try {
			DecimalFormat df = new DecimalFormat();
			NumberFormat currency = NumberFormat.getCurrencyInstance();
			double winProb = df.parse(this.winProb.getText()).doubleValue() / 100;
			double initAmount = currency.parse(this.initAmount.getText()).doubleValue();
			double targetAmount = currency.parse(this.targetAmount.getText()).doubleValue();
			double gambleAmount = currency.parse(this.gambleAmount.getText()).doubleValue();
			
			gambler = new GamblerModel(winProb, initAmount, targetAmount, gambleAmount);
			currentRow++;
			currentColumn = 0;
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void addGamble(double gamble) {
		dataset.addValue(gamble, new Integer(currentRow), new Integer(currentColumn));
		currentColumn++;
	}
	
	private void addListeners() {
		winProb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createGambler();
			}
		});
		
		initAmount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createGambler();
			}
		});
		
		targetAmount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createGambler();
			}
		});
		
		gambleAmount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createGambler();
			}
		});
	}
	
	public static void main(String[] args) {
		new GamblerView2();
	}
}
