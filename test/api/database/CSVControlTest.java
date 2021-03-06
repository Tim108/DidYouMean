package api.database;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the CSVControl using the blackbox method.
 *
 * @author Tim Blok, Frans van Dijk, Yannick Mijsters, Ramon Onis, Tim Sonderen; University of Twente
 */
public class CSVControlTest {
    private CSVControl c;
    private CSVControl deadc;

    @Before
    public void setUp() throws Exception {
        c = new CSVControl(new String[]{"./test_res/DataTest.csv"});
        deadc = new CSVControl(new String[]{"nope"});
    }

    @Test(expected = FileNotFoundException.class)
    public void testBadFile() throws Exception {
        deadc.getData();
    }

    @Test
    public void testGetData() throws Exception {
        Map<String, Integer> testData = c.getData();

        assertThat(testData.size(), is(8)); // skips everything it shouldn't/can't parse

        assertThat(testData.get("1"), is(190));
        assertThat(testData.get("one"), is(0));
        assertThat(testData.get("two"), is(26205));
        assertThat(testData.get("three"), is(72417));
        assertThat(testData.get("four"), is(128634356));
        assertThat(testData.get("five,six"), is(225337093));
        assertThat(testData.get("inch\""), is(229600));
        assertThat(testData.get("double"), is(400));
    }
}
