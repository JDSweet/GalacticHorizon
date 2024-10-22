package org.origin.spacegame.utilities;

import java.util.Random;

public class RandomNumberUtility
{
    private static Random random = new Random();

    public static float nextFloat(float min, float max)
    {
        return random.nextFloat() * (max - min) + min;
    }

    public static int nextInt(int min, int max)
    {
        return random.nextInt(max-min) + min;
    }
}
