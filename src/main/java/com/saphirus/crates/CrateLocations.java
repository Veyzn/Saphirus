package com.saphirus.crates;

import com.saphirus.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class CrateLocations {

    private File file = new File("plugins/Saphirus/CrateLocations.yml");
    private YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    public CrateLocations() {

    }

    public void createCFG() {
        if(!file.exists()) {
            try {
                file.createNewFile();
                cfg.set("dummy","data");
                save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Set<String> getLocations() {return cfg.getKeys(false);}

    public boolean isLocationCrateBlock(Location loc) {return getLocations().contains(LocationUtil.locationToString(loc).replace(".","%"));}

    public void addLocation(Location loc, String crate) {
        cfg.set(LocationUtil.locationToString(loc).replace(".","%"), crate);
        save();
    }

    public void removeLocation(Location loc) {
        cfg.set(LocationUtil.locationToString(loc).replace(".","%"),null);
        save();
    }

    public String getBlockCrate(Location loc) {return cfg.getString(LocationUtil.locationToString(loc).replace(".","%"));}



    public void save() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
