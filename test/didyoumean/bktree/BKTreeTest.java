package didyoumean.bktree;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Tim on 3/21/2016.
 */
public class BKTreeTest {

    Node node;
    BKTree tree;

    @Before
    public void setup() {
        node = new Node("setup");
        tree = new BKTree();
    }

    // ---- Node.java test, 87% coverage. ----
    // ---- Only doesn't cover Object.equals() and the toString() method ----
    // ---- Constructors of Node ----

    @Test(expected = IllegalArgumentException.class)
    public void testNodeInvalidConstructor1() throws Exception {
        // word of a Node must not be null.
        new Node(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeInvalidConstructor2() throws Exception {
        // parent may not be null
        new Node("test", -1);
    }

    // ---- getters of Node ----

    @Test
    public void testGetters() {
        assertThat("getName() should equal to the initial value set in setup().", node.getName().equals("setup"));
        assertThat("getScore() should be equal to the initial value of a node.", node.getScore() == 0);
        assertThat("getChildren() should be equal to the initial value of a node.", node.getChildren().equals(new ConcurrentHashMap<>()));
    }

    // ---- toString() of Node ----

    @Test
    public void testToString() {
        Node testNode = new Node("test", 16);
        assertThat(testNode.toString().equals("test (16)"), is(true));
    }

    // ---- addChild() of Node ----

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildException() {
        node.addChild(null);
    }

    @Test
    public void testAddChild() {
        Node child1 = new Node("setup1"); //LD == 1
        node.addChild(child1);
        assertThat("Setup1 got successfully added to node",
                node.getChildren().containsValue(child1));
        Node child2 = new Node("setup12"); //LD == 2
        node.addChild(child2);
        assertThat("Setup1 and Setup12 got successfully added to node",
                node.getChildren().containsValue(child1) && node.getChildren().containsValue(child2));
        Node child3 = new Node("setup3"); //LD == 1
        node.addChild(child3);
        assertThat("Setup3 got successfully added to a node which already has an edge with distance 1",
                node.getChildren().get(1).getChildren().containsValue(child3));
    }

    // ---- searchTree() ----

    @Test(expected = IllegalArgumentException.class)
    public void testSearchTreeIllegalArg1() {
        Node node = new Node("node");
        node.searchTreeForNodes(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchTreeIllegalArg2() {
        Node node = new Node("node");
        node.searchTreeForNodes("other node", -22);
    }

    @Test
    public void testSearchTree() {
        Node root = new Node("child");
        Node child1 = new Node("child1");
        Node child2 = new Node("child12");
        Node child3 = new Node("child3");
        root.addChild(child1);
        root.addChild(child2);
        root.addChild(child3);
        assertThat(root.searchTreeForNodes("child", 1).size() == 3, is(true));
        assertThat(root.searchTreeForNodes("child12345", 1).isEmpty(), is(true));
        assertThat(root.searchTreeForNodes("child", 2).size() == 4, is(true));
    }



    // ---- BKTree.java tests ----

    // buildTree() test

    @Test(expected = IllegalArgumentException.class)
    public void testBuildTreeInvalidArg1() throws Exception{
        tree.buildTree(null, new ConcurrentHashMap<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildTreeInvalidArg2() throws Exception{
        tree.buildTree(new ArrayList<>(), null);
    }

    @Test
    public void testBuildTree(){
        // same structure as the addChild test
        ArrayList<String> wordList = new ArrayList<>();
        String[] wordArray = {"setup", "setup1", "setup12", "setup3"};
        Collections.addAll(wordList, wordArray);
        Map<String, Integer> data = new ConcurrentHashMap<>();
        data.put("setup", 10);
        data.put("setup1", 30);
        data.put("setup12", 50);
        data.put("setup3", 100);
        Node setup = new Node("setup");
        Node setup1 = new Node("setup1");
        Node setup12 = new Node("setup12");
        Node setup3 = new Node("setup3");
        tree.buildTree(wordList, data);
        //tree looks like this: setup is the root, with children 1 and 12. setup1 has 1 child, namely setup3.
        //setup12 has no children.
        assertThat("setup is the root", tree.getRoot().equals(setup));
        assertThat("root has 2 children, setup1 and setup12",
                tree.getRoot().getChildren().containsValue(setup12) &&
                tree.getRoot().getChildren().containsValue(setup1));
        assertThat("setup1 has child setup3", tree.getRoot().getChildren().get(1).getChildren().containsValue(setup3));
        //TODO: assertThat("Tree should look like expected", );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateLDIllegalArgument1(){
        BKTree.calculateDistance(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateLDIllegalArgument2(){
        BKTree.calculateDistance("", null);
    }

    @Test
    public void testCalculateLievenshteinDistance(){
        assertThat("Empty string and any other word should have LD equal to word.length()",
                BKTree.calculateDistance("","four") == 4);
        assertThat(BKTree.calculateDistance("","") == 0, is(true));
        assertThat(BKTree.calculateDistance("food","food") == 0, is(true));
        assertThat(BKTree.calculateDistance("food","fxod") == 1, is(true));
        assertThat(BKTree.calculateDistance("fxd","food") == 2, is(true));
        assertThat(BKTree.calculateDistance("abcfood","food") == 3, is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDYMIllegalArg(){
        tree.getDYM(null);
    }

    @Test
    public void testGetDYM(){
        
    }
}