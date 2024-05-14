package com.saphirus.utils;

import org.bukkit.Color;

public class VeyznAPI {


    public static Color hexToBukkitColor(String hexCode) {
        // Check if the hex code starts with "#" and remove it
        if (hexCode.startsWith("#")) {
            hexCode = hexCode.substring(1);
        }

        // Parse the hex code to an integer
        int rgb = Integer.parseInt(hexCode, 16);

        // Extract the red, green, and blue values
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        // Create and return a Bukkit Color object
        return Color.fromRGB(red, green, blue);
    }
}
