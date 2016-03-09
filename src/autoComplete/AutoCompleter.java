package autoComplete;

import autoComplete.TreeControl;
import database.CSVControl;
import database.IDBControl;

/**
 * The main class for the auto-completer. Suggestions to complete can be requested here.
 * This class will command the database- and tree control.
 *
 * @author Tim
 */
public class AutoCompleter {
    private IDBControl DB;
    private TreeControl TC;

    private static final int NUMSUGGESTIONS = 5;

    /**
     * Initializes a new AutoCompleter.
     */
    public AutoCompleter() {
        DB = new CSVControl();
        TC = new TreeControl(DB);
    }

    /**
     * Commands the tree control to give an array of suggestions.
     *
     * @param prefix what is already typed.
     * @return a list of suggestions to complete the prefix.
     */
    public String[] suggest(String prefix) {
        return null;
    }

    /**
     * Uses the database control to send data to the tree control so the tree control can refresh the tree.
     */
    public void refresh() {

    }
}
