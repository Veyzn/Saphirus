package com.ryan.utils.commands;

import com.ryan.utils.MsgUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Contains all the settings for a command. Contains constructors,
 * getters, setters, and checkers (but no chess (please laugh)) for the settings.
 * Also contains ways to validate arguments for the command, easily send a response
 * to the command sender, and more.
 *
 * @author CyberRyan
 */
public class CommandModel {

    // Default messages that are sent when an argument is not
    //      entered correctly by the user
    private static final String DEFAULT_PERMISSION_MSG = "&cInsufficient Permissions!";
    private static final String DEFAULT_INVALID_PLAYER_ARG_MSG = "&7Couldn't find player %s";
    private static final String DEFAULT_INVALID_INT_MSG = "&7Couldn't find integer %s";
    private static final String DEFAULT_INVALID_DOUBLE_MSG = "&7Couldn't find double %s";

    //
    // Command Utilities
    //

    /**
     * @return a list of all online player names
     */
    public static List<String> getOnlinePlayerNames() {
        return Bukkit.getOnlinePlayers().stream()
                .map( Player::getName )
                .collect( Collectors.toList() );
    }

    /**
     * @param input the input to match
     * @return a list of all online player names that starts with the input
     */
    public static List<String> matchOnlinePlayers( String input ) {
        List<String> toReturn = new ArrayList<>();
        for ( Player player : Bukkit.getOnlinePlayers() ) {
            if ( player.getName().toLowerCase().startsWith( input.toLowerCase() ) ) {
                toReturn.add( player.getName() );
            }
        }
        return toReturn;
    }

    /**
     * Returns all strings from a list that starts with the input
     * @param list the list to match
     * @param input the input to match
     * @return a list of all strings from the list that starts with the input
     */
    public static List<String> matchArgs( ArrayList<String> list, String input ) {
        for ( int index = list.size() - 1; index >= 0; index-- ) {
            if ( list.get( index ).toLowerCase().startsWith( input.toLowerCase() ) == false ) {
                list.remove( index );
            }
        }
        return list;
    }

    /**
     * Returns all strings from a list that starts with the input
     * @param list the list to match
     * @param input the input to match
     * @return a list of all strings from the list that starts with the input
     */
    public static List<String> matchArgs( List<String> list, String input ) {
        return matchArgs( new ArrayList<>( list ), input );
    }

    // Basic command info
    private String name;
    private String permission;
    private String permissionMsg;
    private String usage;

    // Messages that are sent when an argument is not
    //      entered correctly by the user
    private String invalidPlayerMsg = null;
    private String invalidIntegerMsg = null;
    private String invalidDoubleMsg = null;

    // Settings for the command, such as to demand the
    //      command sender be a player, etc.
    private boolean demandPlayer = false;
    private boolean demandConsole = false;
    private boolean demandPermission = false;
    private boolean runAsync = false;

    // If true, then the command will look through its
    //      argument types and suggest numbers based
    //      off of those. If false, the developer will
    //      have to handle tab completions themself
    private boolean tabCompleteEnabled = true;

    // Argument validation
    private Map<Integer, ArgType> argTypes = new HashMap<>();
    private Map<Integer, List<String>> stringArgOptions = new HashMap<>();
    private int minArgLength = 0;
    private boolean validateArgs = true;
    private boolean sendValidationMsg = true;

    public CommandModel( String name, String permission, String permissionMsg, String usage ) {
        this.name = name;
        this.permission = permission;
        this.permissionMsg = permissionMsg;
        this.usage = usage;
    }

    public CommandModel( String name, String permission, String usage ) {
        this( name, permission, null, usage );
    }

    public CommandModel( String name, String usage ) {
        this( name, null, null, usage );
    }

    //
    // Checkers
    //

    /**
     * Whether the sender has permissions or not for the command
     * @param sender the sender of the command
     * @return true if the sender has permissions or if the permission variable is null, false otherwise
     */
    public boolean permissionsAllowed( CommandSender sender ) {
        if ( permission == null || permission.isBlank() ) { return true; }
        return sender.hasPermission( permission );
    }

    /**
     * Whether the sender has permissions or not for the command
     * @param sender the sender of the command
     * @return true if the sender has permissions or if the permission variable is null, false otherwise
     */
    public boolean permsAllowed( CommandSender sender ) {
        if ( permission == null || permission.isBlank() ) { return true; }
        return sender.hasPermission( permission );
    }

    //
    // Argument Validation
    //

    /**
     * Validates that the argument provided is of the correct type
     * @param sender The command sender
     * @param arg The argument
     * @param index The index of the argument
     * @return True if the argument is valid or if the argument is not of a type, false otherwise
     */
    public final boolean validateArgument( CommandSender sender, String arg, int index ) {
        if ( this.validateArgs == false ) { return true; }
        if ( this.argTypes.containsKey( index ) == false ) { return true; }
        if ( arg == null ) { return false; }

        switch ( this.argTypes.get( index ) ) {
            case ONLINE_PLAYER -> {
                if ( isValidUsername( arg ) == false || Bukkit.getPlayer( arg ) == null ) {
                    if ( this.sendValidationMsg ) { sendInvalidPlayerArg( sender, arg ); }
                    return false;
                }
                return true;
            }

            case OFFLINE_PLAYER -> {
                if ( isValidUsername( arg ) == false || Bukkit.getOfflinePlayer( arg ) == null ) {
                    if ( this.sendValidationMsg ) { sendInvalidPlayerArg( sender, arg ); }
                    return false;
                }
                return true;
            }

            case INTEGER -> {
                try {
                    Integer.parseInt( arg );
                    return true;
                } catch ( NumberFormatException e ) {
                    if ( this.sendValidationMsg ) { sendInvalidIntegerArg( sender, arg ); }
                    return false;
                }
            }

            case DOUBLE -> {
                try {
                    Double.parseDouble( arg );
                    return true;
                } catch ( NumberFormatException e ) {
                    if ( this.sendValidationMsg ) { sendInvalidDoubleArg( sender, arg ); }
                    return false;
                }
            }

            case STRING -> {
                final List<String> options = this.stringArgOptions.get( index ).stream()
                        .map( String::toLowerCase )
                        .collect( Collectors.toList() );
                if ( options.contains( arg.toLowerCase() ) == false ) {
                    if ( this.sendValidationMsg ) { sendUsage( sender ); }
                    return false;
                }
                return true;
            }
        }

        return true;
    }

    /**
     * Checks if a username is allowed by Minecraft. Useful so that time isn't wasted looking up a weird name.
     * <i>(Allows periods for bedrock support)</i>
     * @param input The username to check
     * @return True if the username is valid, false otherwise
     */
    public final boolean isValidUsername( String input ) {
        if ( input.length() < 3 || input.length() > 16 ) { return false; }
        if ( input.contains( " " ) ) { return false; }
        for ( char c : input.toCharArray() ) {
            if ( Character.isLetterOrDigit( c ) == false && c != '_' && c != '.' ) {
                return false;
            }
        }

        return true;
    }

    //
    // Senders
    //

    /**
     * Sends the permission message to the sender
     * @param sender the sender of the command
     */
    public void sendPermissionMsg( CommandSender sender ) {
        MsgUtils.sendMsg( sender, getPermissionMsg() );
    }

    /**
     * Sends the permission message to the sender
     * @param sender the sender of the command
     */
    public void sendPermMsg( CommandSender sender ) {
        sendPermissionMsg( sender );
    }

    /**
     * Sends the usage message to the sender
     * @param sender the sender of the command
     */
    public void sendUsage( CommandSender sender ) {
        MsgUtils.sendMsg( sender, getUsage() );
    }

    /**
     * Sends that the name provided isn't any player's name
     * @param sender The person to send this message to
     * @param name The attempted username
     */
    public void sendInvalidPlayerArg( CommandSender sender, String name ) {
        MsgUtils.sendMsg( sender, String.format( getInvalidPlayerMsg(), name ) );
    }

    /**
     * Sends that the argument provided isn't an integer
     * @param sender The person to send this message to
     * @param arg The attempted argument
     */
    public void sendInvalidIntegerArg( CommandSender sender, String arg ) {
        MsgUtils.sendMsg( sender, String.format( getInvalidIntegerMsg(), arg ) );
    }

    /**
     * Sends that the argument provided isn't a double
     * @param sender The person to send this message to
     * @param arg The attempted argument
     */
    public void sendInvalidDoubleArg( CommandSender sender, String arg ) {
        MsgUtils.sendMsg( sender, String.format( getInvalidDoubleMsg(), arg ) );
    }

    //
    // Getters & Setters
    //

    /**
     * @return The name of the command
     */
    public final String getName() {
        return name;
    }

    /**
     * @return The permission of the command
     */
    public final String getPermission() {
        return permission;
    }

    /**
     * @return The permission message for the command. If not set,
     * the default permission message is returned instead.
     */
    public final String getPermissionMsg() {
        return permissionMsg == null ? DEFAULT_PERMISSION_MSG : permissionMsg;
    }

    /**
     * @return The usage for the command
     */
    public final String getUsage() {
        return usage;
    }

    /**
     * @return The invalid player message for the command. If not set,
     * the default invalid player message is returned instead.
     * Note that %s is where the attempted name should be inserted.
     */
    public final String getInvalidPlayerMsg() {
        return invalidPlayerMsg == null ? DEFAULT_INVALID_PLAYER_ARG_MSG : invalidPlayerMsg;
    }

    /**
     * @return The invalid integer message for the command. If not set,
     * the default invalid integer message is returned instead.
     * Note that %s is where the invalid integer should be inserted.
     */
    public final String getInvalidIntegerMsg() {
        return invalidIntegerMsg == null ? DEFAULT_INVALID_INT_MSG : invalidIntegerMsg;
    }

    /**
     * @return The invalid double message for the command. If not set,
     * the default invalid double message is returned instead.
     * Note that %s is where the invalid double should be inserted.
     */
    public final String getInvalidDoubleMsg() {
        return invalidDoubleMsg == null ? DEFAULT_INVALID_DOUBLE_MSG : invalidDoubleMsg;
    }

    /**
     * @return True if the command is only executable by players, false otherwise
     */
    public final boolean isDemandPlayer() {
        return demandPlayer;
    }

    /**
     * @return True if the command is only executable by the console, false otherwise
     */
    public final boolean isDemandConsole() {
        return demandConsole;
    }

    /**
     * @return True if the command is only executable by players with the permission, false otherwise
     */
    public final boolean isDemandPermission() {
        return demandPermission;
    }

    /**
     * @return True if the command is asynchronous, false otherwise
     */
    public final boolean runAsync() {
        return runAsync;
    }

    /**
     * @return True if the command has tab completion enabled, false otherwise
     */
    public final boolean isTabCompleteEnabled() {
        return tabCompleteEnabled;
    }

    /**
     * Sets the message that's sent to the player when they provide an invalid player.
     * Use %s to mark where the invalid player name should be inserted.
     * @param msg The message
     */
    public final void setInvalidPlayerMsg( String msg ) {
        invalidPlayerMsg = msg;
    }

    /**
     * Sets the message that's sent to the player when they provide an invalid integer.
     * Use %s to mark where the invalid integer should be inserted.
     * @param msg The message
     */
    public final void setInvalidIntegerMsg( String msg ) {
        invalidIntegerMsg = msg;
    }

    /**
     * Sets the message that's sent to the player when they provide an invalid double.
     * Use %s to mark where the invalid double should be inserted.
     * @param msg The message
     */
    public final void setInvalidDoubleMsg( String msg ) {
        invalidDoubleMsg = msg;
    }

    /**
     * @param demandPlayer Whether the command is only executable by players (true) or not (false)
     */
    public final void setDemandPlayer( boolean demandPlayer ) {
        this.demandPlayer = demandPlayer;
    }

    /**
     * Alias for the <b>setDemandPlayer()</b> method
     * @param demandPlayer Whether the command is only executable by players (true) or not (false)
     */
    public final void demandPlayer( boolean demandPlayer ) { this.demandPlayer = demandPlayer; }

    /**
     * @param demandConsole Whether the command is only executable by the console (true) or not (false)
     */
    public final void setDemandConsole( boolean demandConsole ) {
        this.demandConsole = demandConsole;
    }

    /**
     * Alias for the <b>setDemandConsole()</b> method
     * @param demandConsole Whether the command is only executable by the console (true) or not (false)
     */
    public final void demandConsole( boolean demandConsole ) { this.demandConsole = demandConsole; }

    /**
     * @param demandPermission Whether the command is only executable by players with the permission (true) or not (false)
     */
    public final void setDemandPermission( boolean demandPermission ) {
        this.demandPermission = demandPermission;
    }

    /**
     * Alias for the <b>setDemandPermission()</b> method
     * @param demandPermission Whether the command is only executable by players with the permission (true) or not (false)
     */
    public final void demandPermission( boolean demandPermission ) { this.demandPermission = demandPermission; }

    /**
     * @param async Whether the command is asynchronous (true) or not (false)
     */
    public final void setRunAsync( boolean async ) {
        this.runAsync = async;
    }

    /**
     * @param tabCompleteEnabled Whether the command has tab completion enabled (true) or not (false)
     */
    public final void setTabCompleteEnabled( boolean tabCompleteEnabled ) {
        this.tabCompleteEnabled = tabCompleteEnabled;
    }

    /**
     * <i>(read the class comment on {@link SubCommand}
     * for information about subcommand indices)</i>
     * @return The {@link Map} of arg index to {@link ArgType}
     */
    public final Map<Integer, ArgType> getArgTypes() {
        return argTypes;
    }

    /**
     * <i>(read the class comment on {@link SubCommand}
     * for information about subcommand indices)</i>
     * @param index The index of the argument to get
     * @return The {@link ArgType} of the argument at the given index
     */
    public final ArgType getArgType( int index ) {
        if ( argTypes.containsKey( index ) == false ) { return null; }
        return argTypes.get( index );
    }

    /**
     * <i>(read the class comment on {@link SubCommand}
     * for information about subcommand indices)</i>
     * @param type The {@link ArgType} to search for
     * @return A {@link List} of all the indexes of the given {@link ArgType}
     */
    public final List<Integer> getArgIndexes( ArgType type ) {
        List<Integer> indexes = new ArrayList<>();
        for ( int index : argTypes.keySet() ) {
            if ( argTypes.get( index ).equals( type ) ) {
                indexes.add( index );
            }
        }
        return indexes;
    }

    /**
     * <i>(read the class comment on {@link SubCommand}
     * for information about subcommand indices)</i>
     * @return The {@link Map} of arg indexes that have been set
     * as {@link ArgType#STRING} to the list of options for that argument
     */
    public final Map<Integer, List<String>> getStringArgOptions() {
        return stringArgOptions;
    }

    /**
     * <i>(read the class comment on {@link SubCommand}
     * for information about subcommand indices)</i>
     * @param index The index of the argument to get
     * @return The {@link List} of strings that are valid
     *         for the argument at the given index
     */
    public final List<String> getStringArgOptions( int index ) {
        if ( argTypes.containsKey( index ) == false || argTypes.get( index ) != ArgType.STRING ) { return null; }
        return stringArgOptions.get( index );
    }

    /**
     * <i>(read the class comment on {@link SubCommand}
     * for information about subcommand indices)</i>
     * @return The minimum length of the arguments
     */
    public final int getMinArgLength() {
        return minArgLength;
    }

    /**
     * @return True if the arguments are validated, false otherwise
     */
    public final boolean isValidatingArgs() {
        return validateArgs;
    }

    /**
     * @return True if the sender is sent a message when the arguments are invalid, false otherwise
     */
    public final boolean isSendingValidationMsg() {
        return sendValidationMsg;
    }

    /**
     * <i>(read the class comment on {@link SubCommand}
     * for information about subcommand indices)</i>
     * @param index The index of the argument to set
     * @param type The {@link ArgType} to set the argument to
     */
    public final void setArgType( int index, ArgType type ) {
        argTypes.put( index, type );
    }

    /**
     * <b>Important Notes:</b>
     * <ul>
     *   <li>
     *       The index must be set to {@link ArgType#STRING} via
     *       the {@link #setArgType(int, ArgType)} method, otherwise
     *       nothing will happen
     *   </li>
     *   <li>
     *       If the argument provided is invalid when checking
     *       via the {@link #validateArgument(CommandSender, String, int)})
     *       method and the {@link #isSendingValidationMsg()} is true,
     *       then the usage of the command will be sent
     *   </li>
     * </ul>
     *
     * <i>(read the class comment on {@link SubCommand}
     * for information about subcommand indices)</i>
     * @param index The index of the argument to set
     * @param options The {@link List} of strings that are valid for that argument <br> <p>
     */
    public final void setStringArgOptions( int index, List<String> options ) {
        if ( argTypes.containsKey( index ) == false || argTypes.get( index ) != ArgType.STRING ) { return; }
        stringArgOptions.put( index, options );
    }

    /**
     * <i>(read the class comment on {@link SubCommand}
     * for information about subcommand indices)</i>
     * @param length The minimum length of the arguments
     */
    public final void setMinArgLength( int length ) {
        minArgLength = length;
    }

    /**
     * @param validate True if the arguments are validated, false otherwise
     */
    public final void setValidateArgs( boolean validate ) {
        validateArgs = validate;
    }

    /**
     * @param send True if the sender is sent a message when the arguments are invalid, false otherwise
     */
    public final void setSendValidationMsg( boolean send ) {
        sendValidationMsg = send;
    }
}