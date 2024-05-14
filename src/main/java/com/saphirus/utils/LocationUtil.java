package com.saphirus.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Random;


public class LocationUtil
{
    public static ArrayList<Location> getCircle(final Location center, final double radius, final int amount) {
        final World world = center.getWorld();
        final double increment = 6.283185307179586 / amount;
        final ArrayList<Location> locations = new ArrayList<Location>();
        for (int i = 0; i < amount; ++i) {
            final double angle = i * increment;
            final double x = center.getX() + radius * Math.cos(angle);
            final double z = center.getZ() + radius * Math.sin(angle);
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }
    
    public static Location getRandomLocation(final int radius, final Location center) {
        final Random rand = new Random();
        final double angle = rand.nextDouble() * 360.0;
        final double x = center.getX() + rand.nextDouble() * radius * Math.cos(Math.toRadians(angle));
        final double z = center.getZ() + rand.nextDouble() * radius * Math.sin(Math.toRadians(angle));
        final Location newloc = new Location(center.getWorld(), x, (double)Maths.randInt(60, 150), z);
        return newloc;
    }
    
    public static String locationToString(final Location l) {
        if (l == null) {
            final Location loc = Bukkit.getWorld("RaxMap").getSpawnLocation();
            return String.valueOf(loc.getWorld().getName()) + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch();
        }
        return String.valueOf(l.getWorld().getName()) + ":" + l.getX() + ":" + l.getY() + ":" + l.getZ() + ":" + l.getYaw() + ":" + l.getPitch();
    }
    
    public static Location stringToLocation(final String l) {
        final String[] s = l.split(":");
        return new Location(Bukkit.getWorld(s[0]), Double.parseDouble(s[1]), Double.parseDouble(s[2]), Double.parseDouble(s[3]), (float)Double.parseDouble(s[4]), (float)Double.parseDouble(s[5]));
    }
}
