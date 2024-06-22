package com.saphirus.gens;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saphirus.utils.LocationUtil;
import org.bukkit.Location;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GenJsonManager {
    private static final String FILE_PATH = "plugins/Saphirus/generators.json";
    private static final Gson gson = new Gson();
    private static final Type GENERATOR_LIST_TYPE = new TypeToken<List<Generator>>() {}.getType();
    private List<Generator> generators;

    public GenJsonManager() {
        this.generators = loadGenerators();
    }

    public List<Generator> loadGenerators() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, GENERATOR_LIST_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveGenerators() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(generators, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addGenerator(Location loc, int tier, String uuid) {
        generators.add(new Generator(loc, tier, uuid));
        saveGenerators();
    }

    public void removeGenerator(Location loc) {
        generators.removeIf(generator -> generator.getLocation().equals(loc));
        saveGenerators();
    }

    public List<Generator> getGeneratorsByOwner(String uuid) {
        List<Generator> playerGenerators = new ArrayList<>();
        for (Generator generator : generators) {
            if (generator.getOwner().equals(uuid)) {
                playerGenerators.add(generator);
            }
        }
        return playerGenerators;
    }

    public String getOwnerFromBlock(Location loc) {
        for (Generator generator : generators) {
            if (generator.getLocation().equals(loc)) {
                return generator.getOwner();
            }
        }
        return null;
    }

    public boolean isBlockGenerator(Location loc) {
        for (Generator generator : generators) {
            if (generator.getLocation().equals(loc)) {
                return true;
            }
        }
        return false;
    }

    public void updateTier(Location loc, int tier) {
        for (Generator generator : generators) {
            if (generator.getLocation().equals(loc)) {
                generator.setTier(tier);
                saveGenerators();
                break;
            }
        }
    }

    public int getTierFromGenerator(Location loc) {
        for (Generator generator : generators) {
            if (generator.getLocation().equals(loc)) {
                return generator.getTier();
            }
        }
        return -1; // Return -1 if the generator is not found
    }

    public static class Generator {
        private String location;
        private int tier;
        private String owner;

        public Generator(Location location, int tier, String owner) {
            this.location = LocationUtil.locationToString(location);
            this.tier = tier;
            this.owner = owner;
        }

        public Location getLocation() {
            return LocationUtil.stringToLocation(location);
        }

        public int getTier() {
            return tier;
        }

        public void setTier(int tier) {
            this.tier = tier;
        }

        public String getOwner() {
            return owner;
        }
    }
}
