package com.ryan.utils.commands;

import com.ryan.RMain;
import com.ryan.utils.MsgUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * Used to easily create a command that can then be registered to the server.
 *
 * @author CyberRyan
 */
public abstract class BasicCommand extends CommandModel implements CommandExecutor, TabCompleter {

    /**
     * Creates a new BasicCommand with the provided arguments.<p>
     *
     * <i>Note: this is where you should call the {@link #register(boolean)} method.</i>
     *
     * @param name The name of the command
     * @param permission The permission to use the command (null for no permission)
     * @param permissionMsg The message to send to the player if they do not have the permission
     * @param usage The usage of the command
     */
    public BasicCommand( String name, String permission, String permissionMsg, String usage ) {
        super( name, permission, permissionMsg, usage );
    }

    /**
     * Creates a new BasicCommand with the provided arguments. The permission
     * message is set to the default permission message provided by
     * {@link CommandModel)}.<p>
     *
     * <i>Note: this is where you should call the {@link #register(boolean)} method.</i>
     *
     * @param name The name of the command
     * @param permission The permission to use the command (null for no permission)
     * @param usage The usage of the command
     */
    public BasicCommand( String name, String permission, String usage ) {
        super( name, permission, usage );
    }

    /**
     * Creates a new BasicCommand with the provided arguments. The permission
     * message is set to the default permission message provided by
     * {@link CommandModel}. The permission is assumed to be null, meaning that
     * no permission is needed to execute this command.<p>
     *
     * <i>Note: this is where you should call the {@link #register(boolean)} method.</i>
     *
     * @param name The name of the command
     * @param usage The usage of the command
     */
    public BasicCommand( String name, String usage ) {
        super( name, usage );
    }

    /**
     * This should be ignored by most developers
     */
    @Override
    public final List<String> onTabComplete( CommandSender sender, Command command, String label, String[] args ) {
        if ( super.isTabCompleteEnabled() == false ) { return Collections.emptyList(); }

        final List<String> TO_RETURN = tabComplete( new SentCommand( this, sender, args ) );
        if ( TO_RETURN.isEmpty() == false ) { return TO_RETURN; }

        if ( super.getArgIndexes( ArgType.ONLINE_PLAYER ).contains( args.length - 1 )
                || super.getArgIndexes( ArgType.OFFLINE_PLAYER ).contains( args.length - 1 ) ) {
            if ( args[args.length - 1].isEmpty() ) { return getOnlinePlayerNames(); }
            else { return matchOnlinePlayers( args[args.length - 1] ); }
        }
        else if ( super.getArgIndexes( ArgType.STRING ).contains( args.length - 1 ) ) {
            if ( args[args.length - 1].isEmpty() ) { return super.getStringArgOptions( args.length -1 ); }
            else { return matchArgs( super.getStringArgOptions( args.length - 1 ), args[args.length - 1] ); }
        }

        return TO_RETURN;
    }

    /**
     * The tabComplete method is called when the player presses tab while typing a command.
     * Here, you can add your own tab completions for this command, or you can use the
     * automatically generated tab completions by returning an empty list. <p>
     * Returning an empty list will do the following: <br>
     * &emsp;- If the argument the command sender is currently typing has been set
     * to either {@link ArgType#ONLINE_PLAYER} or {@link ArgType#OFFLINE_PLAYER}), then a list
     * of players that match what is being typed will be presented to the command sender. <br>
     * &emsp;- If the argument the command sender is currently typing has been set
     * to {@link ArgType#STRING} and a list of string options for this argument has been provided,
     * then those string options that match what is being typed will be presented to the command sender.<br>
     * &emsp;- If the above yields no results, an empty list will be sent to the command sender. <p>
     * If you want no tab completions to be presented, return a list with one empty element.
     *
     * @param command The {@link SentCommand} of the command that is being typed
     * @return A list of strings to return to the command sender
     *
     * @see ArgType
     * @see CommandModel#setArgType(int, ArgType)
     * @see CommandModel#setStringArgOptions(int, List)
     */
    public abstract List<String> tabComplete( SentCommand command );

    /**
     * This should be ignored by most developers
     */
    @Override
    public final boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {
        // Checking if the command sender is a player
        if ( super.isDemandPlayer() && ( sender instanceof Player ) == false ) {
            MsgUtils.sendMsg( sender, "&sYou must be a player to use this command" );
            return true;
        }

        // Checking if the command sender is console
        if ( super.isDemandConsole() && ( sender instanceof ConsoleCommandSender ) == false ) {
            MsgUtils.sendMsg( sender, "&sYou must be console to use this command" );
            return true;
        }

        // Checking if sender has permissions to run the command
        if ( super.isDemandPermission() && permissionsAllowed( sender ) == false ) {
            sendPermissionMsg( sender );
            return true;
        }

        // Argument length validation
        if ( args.length < super.getMinArgLength() ) {
            sendUsage( sender );
            return true;
        }

        // Argument validation
        if ( super.isValidatingArgs() ) {
            for ( int index = 0; index < args.length; index++ ) {
                if ( super.validateArgument( sender, args[index], index ) == false ) { return true; }
            }
        }

        // Running the command asynchronously
        if ( super.runAsync() ) {
            Bukkit.getScheduler().runTaskAsynchronously( RMain.PLUGIN,
                    () -> execute( new SentCommand( this, sender, args ) )
            );
        }

        // Running the command
        else {
            return execute( new SentCommand( this, sender, args ) );
        }

        return true;
    }

    /**
     * The execute method is called when the player executes a command. All information
     * about the command, who executed it, the arguments, etc. are provided in
     * the {@link SentCommand} parameter<p>
     *
     * The value returned by this method will be returned to the
     * {@link CommandExecutor#onCommand(CommandSender, Command, String, String[])} method.
     *
     * @param command The {@link SentCommand} of the command that was executed
     * @return What to return to the {@link CommandExecutor#onCommand(CommandSender, Command, String, String[])} method
     *
     * @see CommandExecutor#onCommand(CommandSender, Command, String, String[])
     */
    public abstract boolean execute( SentCommand command );

    /**
     * Required to be executed for the command to work properly
     * @param includeTabComplete if the command should be tab completable
     */
    public void register( boolean includeTabComplete ) {
        super.setTabCompleteEnabled( includeTabComplete );

        RMain.PLUGIN.getCommand( super.getName() ).setExecutor( this );
        if ( includeTabComplete ) {
            RMain.PLUGIN.getCommand( super.getName() ).setTabCompleter( this );
        }
    }
}