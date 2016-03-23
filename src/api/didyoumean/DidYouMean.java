package api.didyoumean;

import api.didyoumean.bktree.BKTree;
import api.didyoumean.levenshteinautomata.*;
import api.database.IDBControl;
import api.tree.Root;

import java.util.*;

/**
 * Created by Tim on 3/9/2016.
 */
public class DidYouMean {
    private IDBControl databaseController;
    private BKTree tree;

    private DYM method;
    private Root root;
    private static final int MAX_DISTANCE = 3;
    private LevenshteinAutomataFactory laf;

    public DidYouMean(IDBControl idbControl, DYM method) {
        this.databaseController = idbControl;
        this.method = method;
        tree = new BKTree();
        setup();
    }

    /**
     * Gets the data from the api.database, and saves it in this Class.
     * Also creates a new api.tree from the data (takes a few seconds).
     */
    public void setup() {
        switch(method) {
            case LEVENSHTEIN:
                root = new Root();
                databaseController.getData().entrySet().forEach(
                        entry -> root.addOrIncrementWord(entry.getKey(), entry.getValue())
                );
                laf = new LevenshteinAutomataFactory(MAX_DISTANCE);
                break;
            case BKTREE:
                List<String> tree = new ArrayList<>(databaseController.getData().keySet());
                getTree().buildTree(tree, databaseController.getData());
                break;
        }
    }

    /**
     * Gets the final did-you-mean suggestion, given a search string.
     *
     * @param searchString The user's search string.
     * @return The String the user most likely meant to type. May be equal to the given search string.
     * @throws IllegalArgumentException if {@code searchString} is {@code null}.
     */
    public String getDYM(String searchString) {
        if (searchString == null) {
            throw new IllegalArgumentException("Search string is null in DidYouMean.getDYMFromString.");
        } else if (method == DYM.BKTREE) {
            return getTree().getDYM(searchString);
        } else if (method == DYM.LEVENSHTEIN) {
            return LevenshteinAutomata.intersect(root, laf, searchString, 1).get(0);
        } else {
            return null;
        }
    }

    public void setMethod(DYM method) {
        this.method = method;
    }

    public BKTree getTree() {
        return tree;
    }


}