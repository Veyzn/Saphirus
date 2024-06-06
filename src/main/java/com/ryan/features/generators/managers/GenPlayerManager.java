package com.ryan.features.generators.managers;

import com.ryan.RMain;
import com.ryan.features.generators.models.GenPlayer;
import com.ryan.utils.ExpiringList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GenPlayerManager {

    private static final List<GenPlayer> standardCache = new ArrayList<>();
    private static final ExpiringList<GenPlayer> expiringSoonCache = new ExpiringList<>( 5, TimeUnit.MINUTES, GenPlayerManager::saveToFileAsync );

    private static File SAVE_DIRECTORY;
    private static final String SAVE_FILE_STRING = "%s-save.yml";
    private static final String MULTIPLIER_PATH = "multiplier";
    private static final String LEVEL_PATH = "level";
    private static final String XP_PATH = "xp";
    private static final String GEN_SLOTS_PATH = "gen-slots";
    private static final String GENS_PLACED_PATH = "gens-placed";
    private static final String MONEY_PATH = "money";
    private static final String BLOCKS_MINED_PATH = "blocks-mined";
    private static final String CROPS_FARMED_PATH = "crops-farmed";

    private static final ExecutorService EXECUTORS = Executors.newCachedThreadPool(); // async threads (unsure if we need this tbh lol)

    public static void initialize() {
        // Initialize the directory for saving data
        SAVE_DIRECTORY = new File( RMain.PLUGIN.getDataFolder().getPath() + File.separator + "gen_players" );
        SAVE_DIRECTORY.mkdirs();

        // Cache all online players
        Bukkit.getOnlinePlayers().forEach( player -> loadIntoCache( player.getUniqueId() ) );
    }

    /**
     * This a method to obtain a GenPlayer object. First,
     * this method looks through both caches (using the
     * {@link #searchCaches(UUID)} method). If this does
     * not return anything, it will then attempt to load
     * the user from a stored file (using the
     * {@link #obtainByFileLoad(UUID)} method). If this
     * also does not return anything, a new GenPlayer
     * object will be created for the given player.
     *
     * <p>If the object returned by this method is not
     * already cached, then the object will be placed in one
     * of the two caches. If the player is online, then it
     * will be placed in the standard cache. Otherwise, the
     * object will be put in the expiring soon cache. <u>This
     * is only done when the future is completed.</u></p>
     * @param uuid The UUID of the player to obtain a
     *             GenPlayer instance of
     * @return The GenPlayer instance obtained
     *
     * @see #loadIntoCache(UUID)
     */
    public static CompletableFuture<GenPlayer> obtain( UUID uuid ) {
        return CompletableFuture.supplyAsync( () -> searchCaches( uuid ) )
                .thenComposeAsync( cacheOptional -> {
                    // If the cache optional is present, we return it
                    if ( cacheOptional.isPresent() ) { return CompletableFuture.completedFuture( cacheOptional.get() ); }

                    // Otherwise, we attempt to load it from a file
                    return obtainByFileLoad( uuid )
                            .thenApplyAsync( fileLoadResult -> {
                                // If we were able to load it from the file, return that result
                                if ( fileLoadResult.isPresent() ) {
                                    cache( fileLoadResult.get() );
                                    return fileLoadResult.get();
                                }

                                // Otherwise, return a new instance
                                GenPlayer toReturn = new GenPlayer( uuid );
                                cache( toReturn );
                                return toReturn;
                            }, EXECUTORS );
                }, EXECUTORS );
    }

    /**
     * Attempts to locate a save file for the provided UUID.
     * If successfully located, then this will read the
     * contents of the file and return that as a GenPlayer.
     * Otherwise, an empty optional is returned.
     * @param uuid The UUID
     * @return The GenPlayer instance, or empty if not found
     */
    public static CompletableFuture<Optional<GenPlayer>> obtainByFileLoad( UUID uuid ) {
        return CompletableFuture.supplyAsync( () -> {
            File file = new File( SAVE_DIRECTORY, String.format( SAVE_FILE_STRING, uuid.toString() ) );
            if ( file.exists() == false ) { return Optional.empty(); }

            FileConfiguration fileAsConfig = YamlConfiguration.loadConfiguration( file );
            GenPlayer toReturn = new GenPlayer(
                    uuid,
                    fileAsConfig.getDouble( MULTIPLIER_PATH ),
                    fileAsConfig.getLong( LEVEL_PATH ),
                    fileAsConfig.getLong( XP_PATH ),
                    fileAsConfig.getInt( GEN_SLOTS_PATH ),
                    fileAsConfig.getInt( GENS_PLACED_PATH ),
                    fileAsConfig.getDouble( MONEY_PATH ),
                    fileAsConfig.getInt( BLOCKS_MINED_PATH ),
                    fileAsConfig.getInt( CROPS_FARMED_PATH )
            );
            return Optional.of( toReturn );
        }, EXECUTORS );
    }

    /**
     * Completes the Future provided by {@link #obtain(UUID)}
     * so that the GenPlayer obtained is loaded into the
     * cache
     * @param uuid The UUID of the player to obtain a
     *             GenPlayer instance of
     */
    public static void loadIntoCache( UUID uuid ) {
        obtain( uuid ).thenRun( () -> {} );
    }

    /**
     * If the player represented by the provided GenPlayer
     * instance is online, the provided GenPlayer instance
     * will be added the the standard cache. Otherwise, the
     * instance will be added to the expiring soon cache.
     * @param player The GenPlayer to cache
     */
    public static void cache( GenPlayer player ) {
        standardCache.remove( player );
        expiringSoonCache.removeSilently( player );

        if ( Bukkit.getOnlinePlayers().stream()
                .anyMatch( p -> p.getUniqueId().equals( player.getPlayerUuid() ) ) ) {
            standardCache.add( player );
        }
        else { expiringSoonCache.add( player ); }
    }

    /**
     * Adds the given GenPlayer instance to the
     * standard cache and ensures it is not in the
     * expiring soon cache.
     * @param player the GenPlayer instance
     */
    public static void cacheIntoStandardCache( GenPlayer player ) {
        expiringSoonCache.removeSilently( player );
        if ( standardCache.contains( player ) ) { return; }
        standardCache.add( player );
    }

    /**
     * Adds the given GenPlayer instance to the
     * expiring soon cache and ensures it is not
     * in the standard cache. If the given instance
     * is already in the expiring soon cache, its
     * expiration time is reset.
     * @param player the GenPlayer instance
     */
    public static void cacheIntoExpiringSoon( GenPlayer player ) {
        standardCache.remove( player );
        expiringSoonCache.removeSilently( player );
        expiringSoonCache.add( player );
    }

    /**
     * If the provided GenPlayer is in the expiring
     * soon cache, this will refresh its expiration
     * time within that cache
     *
     * <p>It is known that this will be called for an
     * element within the expiring soon cache when any
     * of the following events occur:
     * <ul>
     *     <li>The {@link #searchExpiringSoonOnly(UUID)} method returns that element</li>
     * </ul>
     * @param player the GenPlayer instance
     */
    public static void refreshExpiration( GenPlayer player ) {
        expiringSoonCache.refreshExpiration( player );
    }

    /**
     * Locates the cached GenPlayer instance for the
     * provided player and, if needed, updates which
     * cache it is in.
     *
     * <p>For example, if the player is offline but
     * their instance is in the standard cache, the
     * instance is moved to the expiring soon cache
     * @param player The player
     */
    public static void updateCacheLocation( OfflinePlayer player ) {
        Optional<GenPlayer> genPlayerOptional = searchCaches( player.getUniqueId() );
        if ( genPlayerOptional.isEmpty() ) { return; }
        GenPlayer genPlayer = genPlayerOptional.get();
        cache( genPlayer ); // since the cache() method removes the arg from both caches
                            // and adds the arg to the correct cache, we can just do this
    }

    /**
     * Searches through both caches for the provided uuid
     * @param uuid the uuid to search for
     * @return the GenPlayer instance if found, otherwise empty
     */
    public static Optional<GenPlayer> searchCaches( UUID uuid ) {
        Optional<GenPlayer> toReturn = searchStandardCacheOnly( uuid );
        if ( toReturn.isPresent() ) { return toReturn; }
        return searchExpiringSoonOnly( uuid );
    }

    /**
     * Searches through only the standard cache for the
     * provided uuid
     * @param uuid the uuid to search for
     * @return the GenPlayer instance if found, otherwise empty
     */
    public static Optional<GenPlayer> searchStandardCacheOnly( UUID uuid ) {
        for ( GenPlayer player : standardCache ) {
            if ( player.getPlayerUuid().equals( uuid ) ) {
                return Optional.of( player );
            }
        }
        return Optional.empty();
    }

    /**
     * Searches through the expiring soon cache for
     * the provided uuid
     * @param uuid the uuid to search for
     * @return the GenPlayer instance if found, otherwise empty
     */
    public static Optional<GenPlayer> searchExpiringSoonOnly( UUID uuid ) {
        for ( GenPlayer player : expiringSoonCache.getAll() ) {
            if ( player.getPlayerUuid().equals( uuid ) ) {
                refreshExpiration( player );
                return Optional.of( player );
            }
        }
        return Optional.empty();
    }

    public static void saveToFileAsync( GenPlayer player ) {
        Bukkit.getScheduler().runTaskAsynchronously( RMain.PLUGIN, () -> saveToFileSynchronously( player ) );
    }

    public static void saveToFileSynchronously( GenPlayer player ) {
        File file = new File( SAVE_DIRECTORY, String.format( SAVE_FILE_STRING, player.getPlayerUuid().toString() ) );

        // If the file doesn't exist AND the player's data is equivalent to the default data, don't save
        if ( file.exists() == false && player.isAllDefaultValues() ) { return; }

        try {
            // If the file doesn't exist, create it
            if ( file.exists() == false ) { file.createNewFile(); }

            // Writing all the data
            FileConfiguration fileAsConfig = YamlConfiguration.loadConfiguration( file );
            fileAsConfig.set( MULTIPLIER_PATH, player.getMultiplier() );
            fileAsConfig.set( LEVEL_PATH, player.getLevel() );
            fileAsConfig.set( XP_PATH, player.getXp() );
            fileAsConfig.set( GEN_SLOTS_PATH, player.getGenSlots() );
            fileAsConfig.set( GENS_PLACED_PATH, player.getGensPlaced() );
            fileAsConfig.set( MONEY_PATH, player.getMoney() );
            fileAsConfig.set( BLOCKS_MINED_PATH, player.getBlocksMined() );
            fileAsConfig.set( CROPS_FARMED_PATH, player.getCropsFarmed() );

            // Saving the data
            fileAsConfig.save( file );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public static void saveAllSynchronously() {
        standardCache.forEach( GenPlayerManager::saveToFileSynchronously );
        expiringSoonCache.getAll().forEach( GenPlayerManager::saveToFileSynchronously );
    }
}