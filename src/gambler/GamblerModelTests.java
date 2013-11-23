package gambler;

import org.junit.Test;

public class GamblerModelTests {
	@Test
	public void benchmark() {
		GamblerModel g = new GamblerModel(0.5, 1000, 11000000, 10);
		long time = System.currentTimeMillis();
		g.multiGamble(Integer.MAX_VALUE);
		System.out.println(System.currentTimeMillis() - time);
	}
}
