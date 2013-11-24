package gambler;

import org.junit.Test;

public class GamblerModelTests {
	@Test
	public void benchmark() {
		GamblerModel g = new GamblerModel(0.5, 1000, 1100, 10);
		long time = System.currentTimeMillis();
		g.multiGamble(-1);
		//System.out.println(System.currentTimeMillis() - time);
	}
	
	@Test
	public void calcTargetProb() {
		GamblerModel g = new GamblerModel(0.25, 1000, 1100, 10);
		System.out.println(g.calcTargetProb()); 
	}
}
