package api.didyoumean;

import api.controller.Controller;
import api.database.IDBControl;
import api.didyoumean.bktree.BKTree;
import api.didyoumean.levenshteinautomata.LevenshteinAutomata;
import api.didyoumean.levenshteinautomata.LevenshteinAutomataFactory;
import api.tree.Root;

import java.util.List;

/**
 * The main class that holds all the information about the BK-trees and Levenshtein Automata.
 * Created by Tim on 3/9/2016.
 */
public class DidYouMean {
    private IDBControl databaseController;
    private BKTree tree;

    private int ldWeight;



    private DYM method;
    private Root root;
    public static final int MAX_DISTANCE = 3;
    private LevenshteinAutomataFactory laf;

    /**
     * Creates a new DidYouMean, with a given database and did-you-mean data structure.
     *
     * @param idbControl The controller which connects to the database.
     * @param method     The method the DYM should be getting its values with.
     */
    public DidYouMean(IDBControl idbControl, DYM method, int ldWeight) {
        this.databaseController = idbControl;
        this.method = method;
        this.ldWeight = ldWeight;
        tree = new BKTree();
        setup();
    }

    /**
     * Gets the data from the api.database, and saves it in this Class.
     * Also creates a new api.tree from the data (takes a few seconds).
     */
    public void setup() {
        root = new Root();
        databaseController.getData().entrySet().forEach(
                entry -> root.addOrIncrementWord(entry.getKey(), entry.getValue())
        );
        laf = new LevenshteinAutomataFactory(MAX_DISTANCE);
        getTree().buildTree(databaseController.getData());
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
            return getTree().getDYM(searchString, getLdWeight());
        } else if (method == DYM.LEVENSHTEIN) {
            return LevenshteinAutomata.intersect(root, laf, searchString, getLdWeight());
        } else {
            return null;
        }
    }

    /**
     * Gets the final did-you-mean suggestion, given a search string.
     *
     * @param searchString The user's search string.
     * @param n            The maximum number of suggestions to return.
     * @return A list with at most n words the user probably meant when searching for {@code word}.
     * Sorted from most likely to least likely. May include {@code word}, if so this will be the first one.
     * @throws IllegalArgumentException if {@code searchString} is {@code null}.
     */
    public List<String> getDYM_N(String searchString, int n) {
        if (searchString == null) {
            throw new IllegalArgumentException("Search string is null in DidYouMean.getDYMFromString.");
        } else if (method == DYM.BKTREE) {
            return getTree().getDYM_N(searchString, n, getLdWeight());
        } else if (method == DYM.LEVENSHTEIN) {
            return LevenshteinAutomata.intersectN(root, laf, searchString, n, getLdWeight());
        } else {
            return null;
        }
    }

    /**
     * Sets the current DYM method of this class.
     *
     * @param method The new DYM method of this class.
     */
    public void setMethod(DYM method) {
        this.method = method;
    }

    /**
     * Returns the current main tree of this class.
     *
     * @return The current tree of this class.
     */
    public BKTree getTree() {
        return tree;
    }


    /**
     * Changes the current {@code ldWeight} to another value. Higher values mean that words with a small LD to a search term
     * get more priority compared to words that have a bigger LD.
     *
     * @throws IllegalArgumentException if weight is negative.
     */
    public void setLdWeight(int weight){
        if(weight < 0){
            throw new IllegalArgumentException("Tried to set a negative LD-weight.");
        }
        this.ldWeight = weight;
    }

    /**
     * Returns the DYM method
     */
    public DYM getMethod() {
        return method;
    }

    /**
     * Returns the current {@code ldWeight}.
     *
     * @return the current {@code ldWeight}.
     */
    public int getLdWeight() {
        return ldWeight;
    }
}
