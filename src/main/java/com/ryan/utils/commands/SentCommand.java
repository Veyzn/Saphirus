package com.ryan.utils.commands;

import com.ryan.utils.MsgUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Used to represent an executed command, with the sender and arguments of the command.
 * Allows for one to easily get the arguments of the executed command into a specified
 * type.
 *
 * @author CyberRyan
 */
public class SentCommand {

    private final CommandModel baseCommand;
    private final CommandSender sender;
    private final String args[];

    public SentCommand( CommandModel baseCommand, CommandSender sender, String args[] ) {
        this.baseCommand = baseCommand;
        this.sender = sender;
        this.args = args;
    }

    /**
     * @return The command that was executed
     */
    public CommandModel getCommand() {
        return baseCommand;
    }

    /**
     * @return The {@link CommandSender} that executed the command
     */
    public CommandSender getSender() {
        return sender;
    }

    /**
     * @return The {@link Player} who executed the command
     * @throws ClassCastException If the sender isn't a player
     */
    public Player getPlayer() {
        if ( sender instanceof Player ) { return ( Player ) sender; }
        throw new ClassCastException( "Sender is not a player" );
    }

    /**
     * @return The arguments that were provided to the command
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * @param index The index of the argument to get
     * @return The argument at the provided index
     * @throws IndexOutOfBoundsException If the index is out of bounds
     */
    public String getArg( int index ) {
        if ( index >= args.length ) { throw new IndexOutOfBoundsException( "Argument at index " + index + " doesn't exist" ); }
        return args[index];
    }

    /**
     * Combines the arguments starting at the provided index into a single string, separated by spaces.
     * @param startingIndex The index of the argument to start at (inclusive).
     * @return The combined arguments
     */
    public String getCombinedArgs( int startingIndex ) {
        String argsToCombine[] = new String[ args.length - startingIndex ];
        for ( int i = startingIndex; i < args.length; i++ ) {
            argsToCombine[i - startingIndex] = args[i];
        }

        return String.join( " ", argsToCombine );
    }

    /**
     * Combines the arguments starting at the provided index and ending at the provided index into
     * a single string, separated by spaces.
     * @param startingIndex The index of the argument to start at (inclusive)
     * @param endingIndex The index of the argument to end at (inclusive)
     * @return The combined arguments
     */
    public String getCombinedArgs( int startingIndex, int endingIndex ) {
        if ( startingIndex > endingIndex ) { throw new IllegalArgumentException( "Starting index cannot be greater than ending index" ); }
        String argsToCombine[] = new String[ endingIndex - startingIndex ];
        for ( int i = startingIndex; i <= endingIndex; i++ ) {
            argsToCombine[i - startingIndex] = args[i];
        }

        return String.join( " ", argsToCombine );
    }

    /**
     * Tries to convert the argument at the provided index to an {@link OfflinePlayer}
     * @param index The index of the argument to convert
     * @return The {@link OfflinePlayer} at the provided index
     * @throws IndexOutOfBoundsException If the index is out of bounds
     * @throws ClassCastException If the argument at the provided index isn't a valid username
     */
    public OfflinePlayer getOfflinePlayerAtArg( int index ) {
        if ( index >= args.length ) { throw new IndexOutOfBoundsException( "Argument at index " + index + " doesn't exist" ); }
        if ( baseCommand.getArgType( index ) == ArgType.OFFLINE_PLAYER ) { return Bukkit.getOfflinePlayer( args[index] ); }

        if ( baseCommand.isValidUsername( args[index] ) ) {
            OfflinePlayer toReturn = Bukkit.getOfflinePlayer( getArg( index ) );
            if ( toReturn != null ) { return toReturn; }
        }
        throw new ClassCastException( "Argument at index " + index + " is not an offline player" );
    }

    /**
     * Tries to convert the argument at the provided index to a {@link Player}
     * @param index The index of the argument to convert
     * @return The {@link Player} at the provided index
     * @throws IndexOutOfBoundsException If the index is out of bounds
     * @throws ClassCastException If the argument at the provided index isn't a valid username
     */
    public Player getPlayerAtArg( int index ) {
        if ( index >= args.length ) { throw new IndexOutOfBoundsException( "Argument at index " + index + " doesn't exist" ); }
        if ( baseCommand.getArgType( index ) == ArgType.ONLINE_PLAYER ) { return Bukkit.getPlayer( args[index] ); }

        if ( baseCommand.isValidUsername( args[index] ) ) {
            Player toReturn = Bukkit.getPlayer( getArg( index ) );
            if ( toReturn != null ) { return toReturn; }
        }
        throw new ClassCastException( "Argument at index " + index + " is not an online player" );
    }

    /**
     * Tries to convert the argument at the provided index to an integer
     * @param index The index of the argument to convert
     * @return The integer at the provided index
     * @throws IndexOutOfBoundsException If the index is out of bounds
     * @throws ClassCastException If the argument at the provided index isn't a valid integer
     */
    public int getIntegerAtArg( int index ) {
        if ( index >= args.length ) { throw new IndexOutOfBoundsException( "Argument at index " + index + " doesn't exist" ); }

        try {
            return Integer.parseInt( args[index] );
        } catch ( NumberFormatException e ) {
            throw new ClassCastException( "Argument at index " + index + " is not an integer" );
        }
    }

    /**
     * Tries to convert the argument at the provided index to a double
     * @param index The index of the argument to convert
     * @return The double at the provided index
     * @throws IndexOutOfBoundsException If the index is out of bounds
     * @throws ClassCastException If the argument at the provided index isn't a valid double
     */
    public double getDoubleAtArg( int index ) {
        if ( index >= args.length ) { throw new IndexOutOfBoundsException( "Argument at index " + index + " doesn't exist" ); }

        try {
            return Double.parseDouble( args[index] );
        } catch ( NumberFormatException e ) {
            throw new ClassCastException( "Argument at index " + index + " is not a double" );
        }
    }

    /**
     * Sends a message to the sender of the command
     * @param msg The message to send
     */
    public void respond( String msg ) {
        MsgUtils.sendMsg( sender, msg );
    }

    /**
     * Sends multiple messages to the sender of the command
     * @param msgs The messages to send
     */
    public void respond( String... msgs ) {
        MsgUtils.sendMsg( sender, msgs );
    }
}