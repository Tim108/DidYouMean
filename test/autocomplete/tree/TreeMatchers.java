package autocomplete.tree;

import org.hamcrest.*;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Contains hamcrest matchers for testing auto complete tree
 *
 * @author Frans
 */
public final class TreeMatchers {

    private TreeMatchers() {
    }

    public static Matcher<Element> hasLetter(final char l) {
        return new FeatureMatcher<Element, Character>(equalTo(l), "letter", "letter") {
            @Override
            protected Character featureValueOf(Element element) {
                return element.getLetter();
            }
        };
    }

    public static Matcher<Element> wordIs(final String w) {
        return new FeatureMatcher<Element, String>(equalTo(w), "word", "word") {
            @Override
            protected String featureValueOf(Element element) {
                return element.getWord();
            }
        };
    }

    public static Matcher<Element> hasWeight(final int i) {
        return new FeatureMatcher<Element, Integer>(equalTo(i), "weight", "weight") {
            @Override
            protected Integer featureValueOf(Element element) {
                return element.getWeight();
            }
        };
    }

    public static Matcher<Element> hasNumberOfChildren(final int i) {
        return new FeatureMatcher<Element, Integer>(equalTo(i), "number of children", "number of children") {
            @Override
            protected Integer featureValueOf(Element element) {
                return element.getChildren().size();
            }
        };
    }

    public static Matcher<Element> hasChildWithLetter(final char l) {
        return new TypeSafeDiagnosingMatcher<Element>() {
            @Override
            protected boolean matchesSafely(Element element, Description mismatchDescription) {
                mismatchDescription.appendText("but it did not have a child with letter ").appendValue(l);
                return element.hasChild(l);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("should have child with letter ").appendValue(l);
            }
        };
    }
}
