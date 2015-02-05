package argumentMutator.utils;

import java.util.Random;

public class RandomUtil {
	
	private static RandomUtil randomUtil = new RandomUtil();
	
	private Random random = null;

	public RandomUtil() {
		random = new Random(System.currentTimeMillis());
	}

	public static boolean getRandomBooleanByPercent(int n) {
		int result = randomUtil.random.nextInt(100);
		return result < n;
	}
	
	public static int getRandomInt(int n) {
		return randomUtil.random.nextInt(n);
	}
}
