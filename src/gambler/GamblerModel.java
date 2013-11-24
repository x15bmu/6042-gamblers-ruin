package gambler;

import java.util.ArrayList;
import java.util.List;


public class GamblerModel {
	private final double winProb;
	private final double targetAmount;
	private final double gambleAmount;
	private final double initAmount;
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
		this.initAmount = initAmount;
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
	
	/**
	 * Gamble until the gambler goes bankrupt or the target amount is reached.
	 * Will execute indefinitely until target reached. Thus, the execution time
	 * for this function is unbounded.
	 * @return Returns true if the gambler reached the target, false otherwise.
	 */
	public boolean fastGambleUntilEnd() {
		synchronized (amount) {
			while (amount > 0 && amount < targetAmount) {
				gamble();
			}
			if (amount <= 0)
				return false;
			else 
				return true;
		}
	}
	
	/**
	 * Gamble until the gambler goes bankrupt, the target amount is reached, or
	 * the cap is reached.
	 * @param cap The maximum number of times to gamble. Exists so simulation
	 * does not run forever. If cap < 0, a default value will be used.
	 * @return Returns 1 if the gambler reached the target, 0 if the gambler
	 * went bankrupt, and -1 if the cap was reached.
	 */
	public int fastCappedGambleUntilEnd(int cap) {
		if (cap < 0) {
			cap = (int)1e6;
		}
		for (int i = 0; amount > 0 && amount < targetAmount && i < cap; ++i) {
			gamble();
		}
		if (amount >= targetAmount)
			return 1;
		else if (amount <= 0)
			return 0;
		else 
			return -1;
	}
	
	/**
	 * Calculates the probability of reaching the target according to the 
	 * formula in the textbook.
	 * @return The probability of winning.
	 */
	public double calcTargetProb() {
		double r = (1-winProb)/winProb;
		
		double num = (Math.pow(r, initAmount)-1);
		double denom = (Math.pow(r, targetAmount)-1);
		
		// prob = 0.5
		if (Math.abs(num) < 1e-9 && Math.abs(denom) < 1e-9) {
			return initAmount/targetAmount;
		}
		
		double targetProb = num/denom;
		if (!Double.isNaN(targetProb))
			return targetProb;
		
		// calculate in steps so no NaN
		double result = 1;
		int n = (int)(initAmount + 0.5);
		int t = (int)(targetProb + 0.5);
		while (t > 1 && n > 1) {
			num = Math.pow(r, 2);
			n -= 2;
			if (t % 2 == 1) {
				num *= r;
				n--;
			}
			
			denom = Math.pow(r, 2);
			r -= 2;
			if (n % 2 == 1) {
				denom *= r;
				r--;
			}
			result *= num/denom;
		}
		
		if (n != 1) {
			double tempNum = Math.pow(r, n);
			if (Double.isInfinite(result * tempNum)) {
				return 1;
			} else {
				return result*tempNum;
			}
		}
		
		if (t != 1) {
			double tempDenom = Math.pow(r, t);
			return result/tempDenom;
		}
		
		return result;
	}
}
