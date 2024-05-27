package com.ryan.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for messages
 *
 * @author CyberRyan
 */
public class MsgUtils {

    private static String primaryColor = "&b"; // Represented with &p
    private static String secondaryColor = "&7"; // Represented with &s

    //                 The character used after the "&" to access this custom color
    //                            The custom color
    private static Map<Character, String> customColors = new HashMap<>();

    /**
     * Sets the primary color, which can be referenced by
     * using {@code &p}
     * @param str the primary color
     */
    public static void setPrimaryColor( String str ) {
        primaryColor = str;
    }

    /**
     * Sets the secondary color, which can be referenced by
     * using {@code &s}
     * @param str the primary color
     */
    public static void setSecondaryColor( String str ) {
        secondaryColor = str;
    }

    /**
     * @return The primary color
     */
    public static String getPrimaryColor() {
        return primaryColor;
    }

    /**
     * @return The secondary color
     */
    public static String getSecondaryColor() {
        return secondaryColor;
    }

    /**
     * Defines a new color code that, will be replaced by the color defined.
     * If a custom color is using the provided code, an error will be thrown.
     * Additionally, if the code is already provided by default Minecraft or
     * is used for the primary or secondary colors, an error will be thrown. <br>
     * <i>(Note: codes <u>are</u> case sensitive, meaning that, for example,
     * you can use 'Z' and 'z' to represent two different custom codes)</i>
     * @param code The letter that comes after the {@code &} to reference
     *             this custom color
     * @param color The color
     */
    public static void defineCustomColor( char code, String color ) {
        if ( customColors.containsKey( code ) ) { throw new IllegalArgumentException( "Custom color with code '" + code + "' already exists" ); }
        if ( Character.isLetter( code ) == false ) { throw new IllegalArgumentException( "The provided code '" + code + "' is not a letter" ); }

        // Ensuring the custom color isn't already provided by Minecraft
        boolean inUse = code >= '0' && code <= '9';
        inUse = inUse || ( code >= 'a' && code <= 'f' );
        inUse = inUse || ( code >= 'm' && code <= 'o' );
        inUse = inUse || ( code != 'r' );
        if ( inUse ) { throw new IllegalArgumentException( "Color with code '" + code + "' already exists (Minecraft provided)" ); }

        customColors.put( code, color );
    }

    /**
     * @param code Removes the custom color that corresponds to this code
     */
    public static void removeCustomColor( char code ) {
        customColors.remove( code );
    }

    private String[] addCustomColors( String ... list ) {
        String[] toReturn = new String[list.length];

        for ( int index = 0; index < list.length; index++ ) {
            toReturn[index] = list[index]
                    .replace( "&p", primaryColor )
                    .replace( "&s", secondaryColor );

            for ( Map.Entry<Character, String> entry : customColors.entrySet() ) {
                toReturn[index] = toReturn[index].replace( "&" + entry.getKey(), entry.getValue() );
            }
        }

        return toReturn;
    }

    /**
     * Adds color to an input string and returns it as a TextComponent
     * @param str the input
     * @return the TextComponent
     */
    public static TextComponent colored( String str ) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize( str );
    }

    /**
     * Adds color to a list of input strings and returns it as a TextComponent
     * @param str the input strings
     * @return the TextComponent
     */
    public static TextComponent colored( String ... str ) {
        TextComponent.Builder builder = Component.text();
        for ( String s : str ) {
            builder.append( colored( s ) );
        }
        return builder.build();

//        return Component.textOfChildren( Arrays.stream( str )
//                .map( s -> LegacyComponentSerializer.legacyAmpersand().deserialize( s ) )
//                .toArray( Component[]::new ) // tbh I have no clue if this will work or not
//        );
    }

    public static String getColoredAsString( String ... str ) {
        TextComponent component = colored( str );
        return LegacyComponentSerializer.legacySection().serialize( component );
    }

    /**
     * Adds color to an input string and sends it to a command sender
     * @param sender the command sender
     * @param str the input
     */
    public static void sendMsg( CommandSender sender, String str ) {
        sender.sendMessage( colored( str ) );
    }

    /**
     * Adds color to a list of input strings and sends it to a command sender
     * @param sender the command sender
     * @param str the input strings
     */
    public static void sendMsg( CommandSender sender, String ... str ) {
        sender.sendMessage( colored( str ) );
    }
}