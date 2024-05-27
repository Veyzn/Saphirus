package com.ryan.utils.commands;

/**
 * For each argument in a command, the expected type of that argument
 * can be set to one of these options. This allows for automatic tab
 * completion generation, etc.
 *
 * @author CyberRyan
 */
public enum ArgType {

    /**
     * The argument must be an online player
     */
    ONLINE_PLAYER,

    /**
     * The argument must be any player, online or offline
     */
    OFFLINE_PLAYER,

    /**
     * The argument must be an integer
     */
    INTEGER,

    /**
     * The argument must be a double
     */
    DOUBLE,

    /**
     * The argument must be a string
     */
    STRING;
}