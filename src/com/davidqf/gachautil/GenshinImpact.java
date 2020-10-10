package com.davidqf.gachautil;

import java.util.Scanner;

public class GenshinImpact {

    public static void main(String[] args) {
        System.out.println("What is the chance of rolling a 5 star character? (Default: 0.6%)");
        Scanner sc = new Scanner(System.in);
        double rate = stringToRate(sc.nextLine());
        if (rate < 0) {
            System.out.println("Invalid rate");
        } else {
            System.out.println("How many times do you roll?");
            try {
                int rolls = Integer.parseInt(sc.nextLine());
                System.out.println("There is a " + rateToRoundedPercent(1 - getFailRate(rate, rolls, 1, 0), 2) + " chance to roll a 5 star banner character");
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount");
            }
        }
    }

    public static double getFailRate(double rate, int total, int roll, int lastFiveStar) {
        if (roll - lastFiveStar == 90) {
            double fail = 0;
            if (lastFiveStar == 0) {
                fail = 0.5;
                if (roll < total) {
                    fail *= getFailRate(rate, total, roll + 1, roll);
                }
            }
            return fail;
        } else if (roll < total) {
            double fail = (1 - rate) * getFailRate(rate, total, roll + 1, lastFiveStar);
            if (lastFiveStar == 0) {
                fail += rate / 2 * getFailRate(rate, total, roll + 1, roll);
            }
            return fail;
        } else if (lastFiveStar == 0) {
            return 1 - rate / 2;
        }
        return 1 - rate;
    }

    private static double stringToRate(String s) {
        s = s.trim();
        try {
            if (s.endsWith("%")) {
                return Double.parseDouble(s.substring(0, s.length() - 1)) / 100;
            }
            return Double.parseDouble(s);
        } catch (NumberFormatException ignored) {
        }
        return -1;
    }

    private static String rateToRoundedPercent(double rate, int decimals) {
        return (int) (rate * Math.pow(10, decimals + 2) + 0.5) / Math.pow(10, decimals) + "%";
    }
}
