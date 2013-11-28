package gambler;

import java.util.concurrent.ExecutorService;

public class MultiGamblerModel {
	private final double winProb;
	private final double initAmount;
	private final double targetAmount;
	private final double gambleAmount;
	
	public MultiGamblerModel(double winProb, double initAmount, double targetAmount, double gambleAmount) {
		if (winProb < 0 || winProb > 1) {
			throw new IllegalArgumentException("Invalid value for winProb. Must be between 0 and 1.");
		}
		this.winProb = winProb;
		this.initAmount = initAmount;
		this.targetAmount = targetAmount;
		this.gambleAmount = gambleAmount;
	}
	
	public double getWinPercentage() {
		int success = 0;
		int failures = 0;
		for (int i = 0; i < 10000; ++i) {
			int result = new GamblerModel(winProb, initAmount, targetAmount, gambleAmount).fastCappedGambleUntilEnd(-1);
			if (result == 1)
				success++;
			else if (result == 0)
				failures++;
		}
		return (double)success/(success + failures);
	}
}
