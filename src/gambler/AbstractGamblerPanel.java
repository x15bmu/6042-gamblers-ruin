package gambler;

import javax.swing.JPanel;

public class AbstractGamblerPanel extends JPanel {
	private static final long serialVersionUID = -7460461237871046358L;
	
	protected double winProb;
	protected double initAmount;
	protected double targetAmount;
	protected double gambleAmount;
	
	protected GamblerModel gambler;
	protected int currentRow = 0;
	protected int currentColumn = 0;
	
	public AbstractGamblerPanel(double winProb, double initAmount, double targetAmount, double gambleAmount) {
		this.winProb = winProb;
		this.initAmount = initAmount;
		this.targetAmount = targetAmount;
		this.gambleAmount = gambleAmount;
	}
	
	public void createGambler(double winProb, double initAmount, double targetAmount, double gambleAmount) {
		this.winProb = winProb;
		this.initAmount = initAmount;
		this.targetAmount = targetAmount;
		this.gambleAmount = gambleAmount;
				
		gambler = new GamblerModel(winProb, initAmount, targetAmount, gambleAmount);
		if (currentColumn > 0) {
			currentRow++; // increase iff points added to row
		}
		currentColumn = 0;
	}
	
	public void createGambler() {
		createGambler(winProb, initAmount, targetAmount, gambleAmount);
	}
}
