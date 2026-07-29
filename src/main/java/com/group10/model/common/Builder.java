package com.group10.model.common;

/**
 * 
 * @author group10
 * PATTERN: Builder.
 * L'interfaccia definisce il metodo build(), che crea e restituisce l'oggetto finale.
 * Il tipo generico T permette di riutilizzarla per builder diversi (ad esempio
 * TrackBuilder e PlaylistBuilder), senza duplicare il codice.
 */
public interface Builder<T> {

    T build();
}