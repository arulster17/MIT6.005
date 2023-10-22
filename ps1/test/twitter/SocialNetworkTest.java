/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T20:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-17T15:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "adi", "@adi @bob @bob @george @eve @rivest", d1);
    private static final Tweet tweet2 = new Tweet(2, "bob", "@adil @bob @george", d1);
    private static final Tweet tweet3 = new Tweet(3, "cal", "eded talk @adi i@wwww", d2);
    private static final Tweet tweet4 = new Tweet(4, "dan", "@rivest tdetes @adi @rivest #hype", d2);
    private static final Tweet tweet5 = new Tweet(5, "eve", "ooby dooby @adi @cat @bob scooby @rivest", d3);
    private static final Tweet tweet6 = new Tweet(6, "adi", "ooby dooby @adi @fey @cat @eve scoobyfefefefe", d4);
    private static final Tweet tweet7 = new Tweet(7, "n", "@n", d4);
    private static final Tweet tweet8 = new Tweet(7, "k", "", d4);

    

    
    /*
     * Count:
     * adi: 3 
     * bob: 2
     * cat: 2
     * dan: 0
     * eve: 1
     * fey: 1
     * adil: 1
     * george: 2
     * rivest: 3
     */
    
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
  //testing
    // no mentions
    // only mentions self
    // only mentions others
    // both
    
    // person who follows others but isn't followed
    // person who is followed but doesnt follow others
    @Test
    public void testGuessFollowsGraph() {
        Map<String, Set<String>> followsGraph = 
                SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet6, tweet7));
        
        
        //System.out.println(followsGraph.get("rivest"));
        assertEquals("rivest", followsGraph.get("rivest"), new HashSet<>(Arrays.asList("adi", "dan", "eve")));
        assertEquals("adi", followsGraph.get("adi"), new HashSet<>(Arrays.asList("cal", "dan", "eve")));
        assertEquals("bob", followsGraph.get("bob"), new HashSet<>(Arrays.asList("adi", "eve")));
        assertEquals("cal", followsGraph.get("cal"), new HashSet<>(Arrays.asList()));
        assertEquals("dan", followsGraph.get("dan"), new HashSet<>(Arrays.asList()));
        assertEquals("eve", followsGraph.get("eve"), new HashSet<>(Arrays.asList("adi")));
        assertEquals("fey", followsGraph.get("fey"), new HashSet<>(Arrays.asList("adi")));
        assertEquals("cat", followsGraph.get("cat"), new HashSet<>(Arrays.asList("eve", "adi")));
        assertEquals("george", followsGraph.get("george"), new HashSet<>(Arrays.asList("adi", "bob")));
        assertEquals("adil", followsGraph.get("adil"), new HashSet<>(Arrays.asList("bob")));
        assertEquals("n", followsGraph.get("n"), new HashSet<>(Arrays.asList()));
        
        
        
        
        System.out.println(followsGraph);
        //empty graph
        followsGraph = new HashMap<>();
        assertTrue("wrong", followsGraph.isEmpty());
        

        
    }
  //testing
    // no mentions
    // only mentions self
    // only mentions others
    // both
    
    // person who follows others but isn't followed
    // person who is followed but doesnt follow others
    
    //empty map
    
    
    @Test
    public void testInfluencersEmpty() {
        //Map<String, Set<String>> followsGraph = new HashMap<>();
        Map<String, Set<String>> followsGraph = 
                SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet6, tweet7));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("expected start", influencers, Arrays.asList("rivest", "adi", "bob", "cat", "george", "eve", "adil", "fey", "dan", "n", "cal"));
        
     
        
        //empty map
        followsGraph = new HashMap<>();
        influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("expected empty list", influencers.isEmpty());
        
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}
