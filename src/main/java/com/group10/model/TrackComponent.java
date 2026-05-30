package com.group10.model;

/**
 * @author group10
 * * Classe Leaf del pattern Composite
 * Modella l'entità Traccia/Brano che possiamo riprodurre.
 * Implementa il pattern Builder per la creazione e validazione sicura.
 */
public class TrackComponent implements Playable {
    
    // I campi ora sono 'final' per garantire l'immutabilità dopo la creazione
    private final String title;       // necessario
    private final String author;      // necessario
    private final int duration;       // durata in secondi, necessaria
    private final String genre;       // opzionale
    private final int year;           // opzionale

    // Costruttore privato: accetta solo il Builder
    private TrackComponent(TrackComponentBuilder builder) {
        this.title = builder.title;
        this.author = builder.author;
        this.duration = builder.duration;
        this.genre = builder.genre;
        this.year = builder.year;
    }

    // --- Metodi Getter (I Setter sono stati rimossi per sicurezza) ---
    
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public int getDurationInSeconds() {
        return duration;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    // --- Implementazione del Pattern Builder (Static Inner Class) ---
    
    public static class TrackComponentBuilder {
        // Campi del builder (con valori di default per i campi opzionali)
        private String title;
        private String author;
        private int duration;
        private String genre = "Sconosciuto"; 
        private int year = 2026; 

        public TrackComponentBuilder title(String title) {
            this.title = title;
            return this;
        }

        public TrackComponentBuilder author(String author) {
            this.author = author;
            return this;
        }

        public TrackComponentBuilder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public TrackComponentBuilder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public TrackComponentBuilder year(int year) {
            this.year = year;
            return this;
        }

        // Metodo build con VALIDAZIONE
        public TrackComponent build() {
            // 1. Validazione Titolo
            if (this.title == null || this.title.trim().isEmpty()) {
                throw new IllegalStateException("Errore validazione: Il titolo della traccia è obbligatorio.");
            }
            
            // 2. Validazione Autore
            if (this.author == null || this.author.trim().isEmpty()) {
                throw new IllegalStateException("Errore validazione: L'autore della traccia è obbligatorio.");
            }
            
            // 3. Validazione Durata
            if (this.duration <= 0) {
                throw new IllegalStateException("Errore validazione: La durata della traccia deve essere maggiore di 0.");
            }

            // 4. Validazione Anno (Opzionale, ma evita anni impossibili)
            if (this.year < 1000 || this.year > 2100) {
                throw new IllegalStateException("Errore validazione: Anno di pubblicazione non valido.");
            }

            // Se tutte le validazioni passano, costruisce l'oggetto
            return new TrackComponent(this);
        }
    }
}