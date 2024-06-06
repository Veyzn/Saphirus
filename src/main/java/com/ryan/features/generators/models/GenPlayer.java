package com.ryan.features.generators.models;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents the following statistics of a player:
 * Level, Experience, Gen Slots, Gens Placed, Money, Blocks Mined,
 * Crops Farmed.
 */
public class GenPlayer {

    public static final GenPlayer DEFAULT_STATE = new GenPlayer( null, 1, 0, 0, 0, 0, 0, 0, 0 );

    private final UUID playerUuid;

    private double multiplier;
    private long level;
    private long xp;
    private int genSlots;
    private int gensPlaced;
    private double money;
    private int blocksMined;
    private int cropsFarmed;

    public GenPlayer( UUID playerUuid, double multiplier, long level, long xp, int genSlots, int gensPlaced, double money, int blocksMined, int cropsFarmed ) {
        this.playerUuid = playerUuid;
        this.multiplier = multiplier;
        this.level = level;
        this.xp = xp;
        this.genSlots = genSlots;
        this.gensPlaced = gensPlaced;
        this.money = money;
        this.blocksMined = blocksMined;
        this.cropsFarmed = cropsFarmed;
    }

    public GenPlayer( UUID playerUuid ) {
        this(
                playerUuid,
                DEFAULT_STATE.getMultiplier(),
                DEFAULT_STATE.getLevel(),
                DEFAULT_STATE.getXp(),
                DEFAULT_STATE.getGenSlots(),
                DEFAULT_STATE.getGensPlaced(),
                DEFAULT_STATE.getMoney(),
                DEFAULT_STATE.getBlocksMined(),
                DEFAULT_STATE.getCropsFarmed()
        );
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier( double multiplier ) {
        this.multiplier = multiplier;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel( long level ) {
        this.level = level;
    }

    public long getXp() {
        return xp;
    }

    public void setXp( long xp ) {
        this.xp = xp;
    }

    public int getGenSlots() {
        return genSlots;
    }

    public void setGenSlots( int genSlots ) {
        this.genSlots = genSlots;
    }

    public int getGensPlaced() {
        return gensPlaced;
    }

    public void setGensPlaced( int gensPlaced ) {
        this.gensPlaced = gensPlaced;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney( double money ) {
        this.money = money;
    }

    public int getBlocksMined() {
        return blocksMined;
    }

    public void setBlocksMined( int blocksMined ) {
        this.blocksMined = blocksMined;
    }

    public int getCropsFarmed() {
        return cropsFarmed;
    }

    public void setCropsFarmed( int cropsFarmed ) {
        this.cropsFarmed = cropsFarmed;
    }

    /**
     * @return True if all the values of this instance
     * are equal to the values of the {@link #DEFAULT_STATE},
     * not including the UUID
     */
    public boolean isAllDefaultValues() {
        if ( this == DEFAULT_STATE ) return true;
        return Double.compare( getMultiplier(), DEFAULT_STATE.getMultiplier() ) == 0 && getLevel() == DEFAULT_STATE.getLevel() && getXp() == DEFAULT_STATE.getXp()
                && getGenSlots() == DEFAULT_STATE.getGenSlots() && getGensPlaced() == DEFAULT_STATE.getGensPlaced() && Double.compare( getMoney(), DEFAULT_STATE.getMoney() ) == 0
                && getBlocksMined() == DEFAULT_STATE.getBlocksMined() && getCropsFarmed() == DEFAULT_STATE.getCropsFarmed();
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        GenPlayer genPlayer = ( GenPlayer ) o;
        return Double.compare( getMultiplier(), genPlayer.getMultiplier() ) == 0 && getLevel() == genPlayer.getLevel() && getXp() == genPlayer.getXp()
                && getGenSlots() == genPlayer.getGenSlots() && getGensPlaced() == genPlayer.getGensPlaced() && Double.compare( getMoney(), genPlayer.getMoney() ) == 0
                && getBlocksMined() == genPlayer.getBlocksMined() && getCropsFarmed() == genPlayer.getCropsFarmed() && Objects.equals( getPlayerUuid(), genPlayer.getPlayerUuid() );
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode( getPlayerUuid() );
        result = 31 * result + Double.hashCode( getMultiplier() );
        result = 31 * result + Long.hashCode( getLevel() );
        result = 31 * result + Long.hashCode( getXp() );
        result = 31 * result + getGenSlots();
        result = 31 * result + getGensPlaced();
        result = 31 * result + Double.hashCode( getMoney() );
        result = 31 * result + getBlocksMined();
        result = 31 * result + getCropsFarmed();
        return result;
    }
}