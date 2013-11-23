package gambler;

import java.util.ArrayList;
import java.util.List;


public class GamblerModel {
	private final double winProb;
	private final double targetAmount;
	private final double gambleAmount;
	Double amount; // always lock before setting or getting!
	
	/**
	 * Construct a {@link GamblerModel} with the specified parameters.
	 * @param winProb The probability of winning. Must be in the range [0, 1].
	 * @param initAmount The initial amount that the gambler has. Must be >= 0.
	 * @param targetAmount The target amount for the gambler to reach. Should be
	 * greater than {@link #initAmount}.
	 * @param 
	 */
	public GamblerModel(double winProb, double initAmount, double targetAmount, double gambleAmount) {
		if (winProb < 0 || winProb > 1) {
			throw new IllegalArgumentException("Invalid value for winProb. Must be between 0 and 1.");
		}
		this.winProb = winProb;
		amount = initAmount;
		this.targetAmount = targetAmount;
		this.gambleAmount = gambleAmount;
	}
	
	/**
	 * Runs a single gamble simulation.
	 * @return Returns the new amount after the gamble.
	 */
	public double gamble() {
		if (Math.random() < winProb) {
			synchronized (amount) {
				amount += gambleAmount;
				return amount;
			}
		} else {
			synchronized (amount) {
				amount -= gambleAmount;
				return amount;
			}
		}
	}
	
	/**
	 * Runs a gamble simulation a specified number of times.
	 * @param n The number of times to run the simulation.
	 * @return Returns a list of the different gamble amounts. The last
	 * element in the list corresponds to the most recent gamble.
	 */
	public List<Double> multiGamble(int n) {
		List<Double> gambles = new ArrayList<>();
		for (int i = 0; i < n; ++i) {
			gambles.add(gamble());
		}
		return gambles;
	}
	
	/**
	 * Keeps gambling until the gambler goes bankrupt, the target amount is
	 * reached, or the cap is reached.
	 * @cap The maximum number of times to gamble. Exists so simulation
	 * does not run forever. If cap < 0, a default value will be used.
	 * @return Returns a list of the different gamble amounts. The last
	 * element in the list corresponds to the most recent gamble.
	 */
	public List<Double> gambleUntilEnd(int cap) {
		if (cap < 0) {
			cap = (int)1e4;
		}
		
		List<Double> gambles = new ArrayList<>(cap);
		for (int i = 0; i < cap; ++i) {
			double gamble = gamble();
			gambles.add(gamble);
			if (gamble <= 0 || gamble >= targetAmount) {
				return gambles;
			}
		}
		return gambles;
	}
}
