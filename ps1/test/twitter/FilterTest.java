/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T20:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-17T15:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "alyssa", "is it djiedjiej to tlk about rivest so much?", d1);
    private static final Tweet tweet3 = new Tweet(3, "bbitdiddle", "eded talk in 30 minutes #hype", d2);
    private static final Tweet tweet4 = new Tweet(4, "bbitdiddle", "rivest tdetes #hype", d2);
    private static final Tweet tweet5 = new Tweet(5, "bbitdiddle", "ooby dooby scooby", d3);
    private static final Tweet tweet6 = new Tweet(6, "bbitdiddle", "ooby dooby scoobyfefefefe", d4);

    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy;
        
        //assertEquals("expected singleton list", 1, writtenBy.size());
        //assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
        
        //doesn't contain
        //some contain
        //all contain
        
        //empty list of tweets
        //non-empty list of tweets
        
        // TC1: empty list of tweets
        writtenBy = Filter.writtenBy(Arrays.asList(), "alyssa");
        assertEquals("expected empty list", 0, writtenBy.size());
        
        // TC2: non-empty list, none contain
        writtenBy = Filter.writtenBy(Arrays.asList(tweet3, tweet4, tweet5), "alyssa");
        assertEquals("expected empty list", 0, writtenBy.size());


        // TC3: non-empty list, some contain
        writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5), "alyssa");
        assertEquals("expected singleton list", 2, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet2));
        
        // TC4: non-empty list, all contain
        writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        assertEquals("expected singleton list", 2, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet2));
        
    }
    
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        
        //0 timespan
        //non-0 timespan
        
        //empty tweet list
        //non empty tweet list
        
        //no tweets in timespan
        //some tweets in timespan
        //all tweets in timespan
        
        
        
        
        
        
        Instant t1 = Instant.parse("2016-02-17T09:00:00Z");
        Instant t2 = Instant.parse("2016-02-17T12:00:00Z");
        Instant t3 = Instant.parse("2016-02-17T09:00:00Z");
        List<Tweet> inTimespan;
        
        // TC1: empty timespan
        inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(t1, t3));
        assertTrue("expected empty list", inTimespan.isEmpty());
        
        // TC2: non-0 timespan + no tweets in timespan + nonempty tweetlist
        inTimespan = Filter.inTimespan(Arrays.asList(tweet5, tweet6), new Timespan(t1, t2));
        assertTrue("expected empty list", inTimespan.isEmpty());
        
        // TC3: non-0 timespan + empty tweetlist
        inTimespan = Filter.inTimespan(Arrays.asList(), new Timespan(t1, t2));
        assertTrue("expected empty list", inTimespan.isEmpty());
        
        // TC4: non-0 timespan + some tweets in timespan + nonempty tweetlist 
        inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet5), new Timespan(t1, t2));
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }
    
    @Test
    public void testContaining() {
        List<Tweet> containing;
        // empty words
        // empty tweets
        // non-empty words + tweets
        // tweets that have none of the words
        
        // TC1: empty words list
        containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList());
        assertTrue("expected empty list", containing.isEmpty());
        
        // TC2: empty tweets list
        containing = Filter.containing(Arrays.asList(), Arrays.asList("talk", "rivest"));
        assertTrue("expected empty list", containing.isEmpty());
        
        // TC3: non-empty words + tweets with none of the words
        containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet5), Arrays.asList("wimbo", "wombo"));
        assertTrue("expected empty list", containing.isEmpty());
        
        //TC4: non-empty words + tweets with some words
        containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet6), 
                                       Arrays.asList("wimbo", "wombo", "rivest", "talk"));
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2, tweet3, tweet4)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
        assertEquals("expected size 4", 4, containing.size());
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

}
