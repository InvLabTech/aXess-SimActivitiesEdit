package net.teekay.axess.utilities;

public class MathUtil {
    public static int clampInt(int val, int min, int max) {
        return Math.max(Math.min(val, max), min);
    }
}
