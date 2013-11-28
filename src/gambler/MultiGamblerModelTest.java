package gambler;

import org.junit.Test;

public class MultiGamblerModelTest {
	@Test
	public void testMany() {
		System.out.println(new MultiGamblerModel(0.5, 1000, 1100, 1).getWinPercentage());
	}
}
