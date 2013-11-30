package gambler;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -2598479547476507654L;
	
	double lastWinProbVal = 0.25;
	JTextField winProb;
	NumberFormat fieldFormat;
	JFormattedTextField initAmount;
	JFormattedTextField targetAmount;
	JFormattedTextField gambleAmount;
	
	GamblerPanel gambler;
	MultiGamblerPanel multiGambler;
	
	JTabbedPane tabs = new JTabbedPane();
	public MainFrame() {
		Locale locale = Locale.getDefault();
		setTitle("Gambler's Ruin");
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		// Text Panel
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
		
		JLabel winProbLabel = new JLabel("Probability of Winning: ");
		JLabel initAmountLabel = new JLabel("Initial Amount: " + Currency.getInstance(locale).getSymbol());
		JLabel targetAmountLabel = new JLabel("Target Amount: " + Currency.getInstance(locale).getSymbol());
		JLabel gambleAmountLabel = new JLabel("Amount to Gamble: " + Currency.getInstance(locale).getSymbol());
		
		winProb = new JTextField();
		fieldFormat = NumberFormat.getInstance(locale);
		fieldFormat.setMinimumFractionDigits(2);
		fieldFormat.setMaximumFractionDigits(2);
		initAmount = new JFormattedTextField(fieldFormat);
		targetAmount = new JFormattedTextField(fieldFormat);
		gambleAmount = new JFormattedTextField(fieldFormat);

		winProb.setText(Double.toString(lastWinProbVal));
		initAmount.setValue(1000);
		targetAmount.setValue(1100);
		gambleAmount.setValue(1);
		
		// Text panel layout
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
		
		// Configuration & Tabs
		gambler = new GamblerPanel(0.5, 1000, 1100, 1);
		multiGambler = new MultiGamblerPanel(0.5, 1000, 1100, 1);
		createGambler();
		addListeners();
		
		tabs.addTab("Single", gambler);
		tabs.addTab("Multi", multiGambler);
		
		// Final layout config
		add(Box.createVerticalStrut(strutWidth));
		add(textPanel);
		add(Box.createVerticalStrut(strutWidth));
		add(tabs);
		pack();
		
		// Final config
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/*
	 * Lots of listeners for actions and focuses on text fields.
	 */
	private void addListeners() {
		winProb.addFocusListener(createGamblerFocusListener);
		winProb.addActionListener(createGamblerActionListener);
		
		initAmount.addFocusListener(createGamblerFocusListener);
		initAmount.addFocusListener(formatListener);
		initAmount.addActionListener(createGamblerActionListener);
		
		targetAmount.addFocusListener(createGamblerFocusListener);
		targetAmount.addFocusListener(formatListener);
		targetAmount.addActionListener(createGamblerActionListener);
		
		gambleAmount.addFocusListener(createGamblerFocusListener);
		gambleAmount.addFocusListener(formatListener);
		gambleAmount.addActionListener(createGamblerActionListener);
	}
	private FocusListener formatListener = new FocusListener() {
		@Override
		public void focusLost(FocusEvent arg0) {
		}
		
		@Override
		public void focusGained(FocusEvent arg0) {
			try {
				NumberFormat fieldFormat2 = (NumberFormat)fieldFormat.clone();
				fieldFormat2.setGroupingUsed(false);
				JFormattedTextField emitter = (JFormattedTextField)arg0.getSource();
				emitter.setText(fieldFormat2.format(fieldFormat.parse(emitter.getText())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	};
	private FocusListener createGamblerFocusListener = new FocusListener() {
		@Override
		public void focusLost(FocusEvent e) {
			createGambler();
		}
		@Override
		public void focusGained(FocusEvent e) {
		}
	};	
	private ActionListener createGamblerActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			createGambler();
		}
	};
	
	/**
	 * Creates a gambler from the information in the text fields.
	 */
	public void createGambler() {
		try {
			double winProb = parseDoubleValue(this.winProb.getText());			
			if (winProb > 1 || winProb < 0)
				throw new NumberFormatException();
			lastWinProbVal = winProb;
			double initAmount = fieldFormat.parse(this.initAmount.getText()).doubleValue();
			double targetAmount = fieldFormat.parse(this.targetAmount.getText()).doubleValue();
			double gambleAmount = fieldFormat.parse(this.gambleAmount.getText()).doubleValue();
			gambler.createGambler(winProb, initAmount, targetAmount, gambleAmount);
			multiGambler.createGambler(winProb, initAmount, targetAmount, gambleAmount);
		} catch (NumberFormatException e) {
		} catch (ParseException e) {
		}
		winProb.setText(Double.toString(lastWinProbVal));
	}
	
	/**
	 * Get the double value of a string. Can include fractions and
	 * decimals.
	 * @param s The string to convert to a double value. Should be a valid
	 * mathematical expression containing only decimals and divisions.
	 * @return The double value of the string.
	 * @throws Throws {@link NumberFormatException} if number is invalid
	 */
	public double parseDoubleValue(String s) throws NumberFormatException {
		s = s.replaceAll(" ", "");
		String[] elems = s.split("/");
		assert (elems.length != 0);
		if (elems.length == 1) {
			return Double.parseDouble(elems[0]);
		}
		double current = Double.parseDouble(elems[0]);
		for (int i = 1; i < elems.length; ++i) {
			current /= Double.parseDouble(elems[i]);
		}
		return current;
	}
	
	public static void main(String[] args) {
		new MainFrame();
	}

}
