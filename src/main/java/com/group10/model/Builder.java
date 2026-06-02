package com.group10.model;

/**
 *
 * @author group10
 * 
 * Interfaccia base per il pattern Builder (Task T1.3).
 * Utilizza i Generics <T> in modo che possa essere usata 
 * sia per TrackBuilder che per PlaylistBuilder.
 */
public interface Builder<T> {
    
    /**
     * Metodo finale che restituisce l'oggetto costruito.
     * @return L'istanza dell'oggetto costruito e validato.
     */
    T build();
}