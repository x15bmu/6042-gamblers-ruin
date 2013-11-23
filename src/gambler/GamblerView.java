package gambler;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sun.awt.HorizBagLayout;

public class GamblerView extends JFrame {
	private static final long serialVersionUID = -8874786662825257049L;
	
	JFormattedTextField winProb;
	JFormattedTextField initAmount;
	JFormattedTextField targetAmount;
	JFormattedTextField gambleAmount;
	
	public GamblerView() {
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
		
		winProb.setValue(0.5);
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
		ActionListener runOnceListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		};
		JButton runToEnd = new JButton("Run To End");
		ActionListener runToEndListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		
		JButton clear = new JButton("Clear");
		ActionListener clearListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		
		JPanel buttonsPanel = new JPanel();
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
		
		add(textPanel);
		add(buttonsPanel);
		
		pack();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new GamblerView();
	}
}
