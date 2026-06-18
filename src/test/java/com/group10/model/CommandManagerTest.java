package com.group10.model;
import com.group10.service.command.CommandManager;
import com.group10.service.command.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

class CommandManagerTest {

    private CommandManager commandManager;

    @BeforeEach
    void setUp() throws Exception {
        // Reset dell'istanza Singleton per garantire che i test siano completamente isolati tra loro
        Field instanceField = CommandManager.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        commandManager = CommandManager.getInstance();
    }

    /**
     * Classe "Stub" interna per simulare un Command e tracciarne le esecuzioni.
     * Dato che implementa la tua interfaccia Command, il CommandManager la accetterà senza problemi.
     */
    class StubCommand implements Command {
        int executeCount = 0;
        int undoCount = 0;

        @Override
        public void execute() {
            executeCount++;
        }

        @Override
        public void undo() {
            undoCount++;
        }
    }

    @Test
    void testUndoSingolo() {
        // Arrange: prepariamo il nostro finto comando
        StubCommand cmd = new StubCommand();

        // Act: eseguiamo il comando
        commandManager.executeCommand(cmd);
        
        // Assert: verifichiamo che l'esecuzione sia avvenuta regolarmente
        assertEquals(1, cmd.executeCount, "Il comando deve essere stato eseguito al momento dell'inserimento.");
        assertEquals(0, cmd.undoCount, "L'undo non deve ancora essere stato chiamato.");

        // Act: annulliamo il comando
        commandManager.undo();

        // Assert: verifichiamo che undo() sia stato chiamato esattamente una volta
        assertEquals(1, cmd.undoCount, "Il metodo undo() del comando deve essere stato invocato una volta.");
    }

    @Test
    void testUndoInSequenza() {
        // Arrange: creiamo tre comandi distinti
        StubCommand cmd1 = new StubCommand();
        StubCommand cmd2 = new StubCommand();
        StubCommand cmd3 = new StubCommand();

        // Act: eseguiamo i tre comandi in sequenza
        commandManager.executeCommand(cmd1);
        commandManager.executeCommand(cmd2);
        commandManager.executeCommand(cmd3);

        // -- Primo Undo --
        commandManager.undo();
        
        // Assert: L'ultimo comando inserito (cmd3) deve essere il primo ad essere annullato (logica LIFO della Pila)
        assertEquals(1, cmd3.undoCount, "Il comando 3 deve essere stato annullato per primo.");
        assertEquals(0, cmd2.undoCount, "Il comando 2 non deve ancora essere stato annullato.");
        assertEquals(0, cmd1.undoCount, "Il comando 1 non deve ancora essere stato annullato.");

        // -- Secondo Undo --
        commandManager.undo();
        
        // Assert: Ora deve essere stato annullato cmd2
        assertEquals(1, cmd2.undoCount, "Il comando 2 deve essere stato annullato per secondo.");
        assertEquals(0, cmd1.undoCount, "Il comando 1 non deve ancora essere stato annullato.");

        // -- Terzo Undo --
        commandManager.undo();
        
        // Assert: Infine viene annullato cmd1
        assertEquals(1, cmd1.undoCount, "Il comando 1 deve essere stato annullato per terzo.");

        // -- Quarto Undo (su Pila vuota) --
        // Assert: Verifichiamo che tentare un undo senza comandi non faccia crashare l'applicazione
        assertDoesNotThrow(() -> commandManager.undo(), 
            "Eseguire undo su uno stack vuoto non dovrebbe lanciare eccezioni grazie al controllo isEmpty().");
    }
}