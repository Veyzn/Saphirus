package com.saphirus.crates;

import java.util.Comparator;

public class PreviewComparator implements Comparator<CrateItem> {
    @Override
    public int compare(CrateItem item1, CrateItem item2) {
        // First, compare by rarity
        int rarityComparison = item1.getRarity().compareTo(item2.getRarity());

        // If rarities are different, return the comparison result based on rarity
        if (rarityComparison != 0) {
            return rarityComparison;
        }

        // If rarities are the same, compare by chance in descending order (higher chance comes first)
        return Integer.compare(item2.getChance(), item1.getChance());
    }
}
