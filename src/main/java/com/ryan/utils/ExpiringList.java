package com.ryan.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * List that expires an element after the given amount of time.
 *
 * @param <T> The type of element for the list to store
 */
public class ExpiringList<T> {

    private final Map<T, Long> list = new HashMap<>();
    private final long lifetimeMilliseconds;
    private final ExpirationAction<T> expirationAction;

    public ExpiringList( long duration, TimeUnit unit, ExpirationAction<T> expirationAction ) {
        this.lifetimeMilliseconds = unit.toMillis( duration );
        this.expirationAction = expirationAction;
    }

    public void add( T item ) {
        this.list.put( item, System.currentTimeMillis() + lifetimeMilliseconds );
    }

    public List<T> getAll() {
        cleanupAll();
        return new ArrayList<>( list.keySet() );
    }

    public boolean contains( T item ) {
        cleanup( item );
        return list.containsKey( item );
    }

    public void refreshExpiration( T item ) {
        if ( list.containsKey( item ) == false ) { throw new NullPointerException( "item does not exist" ); }
        list.put( item, System.currentTimeMillis() + lifetimeMilliseconds );
    }

    public void cleanup( T item ) {
        if ( list.containsKey( item ) == false || System.currentTimeMillis() <= list.get( item ) ) { return; }
        this.expirationAction.onExpiration( item );
        list.remove( item );
    }

    public void cleanupAll() {
        for ( Map.Entry<T, Long> entry : list.entrySet() ) {
            cleanup( entry.getKey() );
        }
    }

    /**
     * Removes the provided item from this list without
     * activating the Expiration Action
     * @param item the item
     */
    public void removeSilently( T item ) {
        list.remove( item );
    }

    /**
     * Activates the Expiration Action for the expiration
     * of this item and then removes the item from this list
     * @param item the item
     */
    public void expire( T item ) {
        if ( list.containsKey( item ) == false ) { return; }
        this.expirationAction.onExpiration( item );
        list.remove( item );
    }

    public interface ExpirationAction<T> {
        void onExpiration( T item );
    }
}