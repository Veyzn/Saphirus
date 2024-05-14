package com.saphirus.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

public class Maths
{
    public static int counter;
    public static Random rand;
    
    public static int getPercent(final int howMuch, final int fromWhat) {
        return (int)(fromWhat * (howMuch / 100.0f));
    }
    
    public static double getPercentDouble(final int howMuch, final double fromWhat) {
        return fromWhat * (howMuch / 100.0f);
    }
    
    public static long getPercentLong(final int howMuch, final long fromWhat) {
        return (long)(fromWhat * (howMuch / 100.0f));
    }
    
    public static boolean isInt(final String s) {
        try {
            Integer.parseInt(s);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        return Integer.parseInt(s) >= 0;
    }
    
    public static boolean isLong(final String s) {
        try {
            Long.parseLong(s);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        return Long.parseLong(s) >= 1L;
    }
    
    public static boolean isDouble(final String s) {
        try {
            Double.parseDouble(s);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public static int randInt(final int min, final int max) {
        final int randomNum = Maths.rand.nextInt(max - min + 1) + min;
        return randomNum;
    }
    
    public static String coinsString(final int i) {
        return NumberFormat.getInstance().format(i).replace(",", "'");
    }
    
    public static String coinsString(final long l) {
        final DecimalFormat formatter = new DecimalFormat("#,###");
        return "§e§l" + formatter.format(l).replace(",", ".") + "$";
    }
    
    public static String asString(final int i) {
        return NumberFormat.getInstance().format(i).replace(",", ".");
    }
    
    public static String asString(final long l) {
        final DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(l).replace(",", ".");
    }
    
    public static boolean isBetween(final int a, final int b, final int c) {
        return (b > a) ? (c > a && c < b) : (c > b && c < a);
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static double randomDouble(final double min, final double max) {
        return min + (max - min) * Maths.rand.nextDouble();
    }
    
    public static double randomDouble() {
        return (randInt(0, 1) == 1) ? (randInt(1, 9) / 10) : ((double)(-randInt(1, 9) / 10));
    }
    
    static {
        Maths.counter = 0;
        Maths.rand = new Random();
    }
    
    public static String shortMoney(Long coins) {
        int b = 0;
        int mrd = 0;
        int m = 0;
        while (coins.longValue() >= 1000000000000L) {
          b++;
          coins = Long.valueOf(coins.longValue() - 1000000000000L);
        } 
        while (coins.longValue() >= 1000000000L) {
          mrd++;
          coins = Long.valueOf(coins.longValue() - 1000000000L);
        } 
        while (coins.longValue() >= 1000000L) {
          m++;
          coins = Long.valueOf(coins.longValue() - 1000000L);
        } 
        if (b > 0)
          return asString(b) + "T";
        if (mrd > 0)
          return asString(mrd) + "B";
        if (m > 0)
          return asString(m) + "M";
        if (coins.longValue() < 0L)
          return ""; 
        return asString(coins.longValue()) + "";
      }
}
