/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.time.Instant;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        if(tweets.size() == 0) {
            throw new RuntimeException("getTimespan input is an empty list.");
        }
        Instant minInstant = tweets.get(0).getTimestamp();
        Instant maxInstant = tweets.get(0).getTimestamp();
        for(int i = 1; i < tweets.size(); i++) { 
            
            if(tweets.get(i).getTimestamp().isAfter(maxInstant)) {
                maxInstant = tweets.get(i).getTimestamp();
            }
            if(tweets.get(i).getTimestamp().isBefore(minInstant)) {
                minInstant = tweets.get(i).getTimestamp();
            }
        }
        
        return new Timespan(minInstant, maxInstant);
        
    }
    /**
     * 
     * @param c
     *          character to be checked
     * @return true if the character is valid in a Twitter username, 
     *         false otherwise
     */
    private static boolean isValidUsernameCharacter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') ||  (c >= '0' && c <= '9') || (c == '_') || (c == '-');
    }
    private static List<Integer> findCharacterInString(char target, String text) {
        List<Integer> indices = new ArrayList<Integer>();
        for(int i = 0; i < text.length(); i++) {
            if(text.charAt(i) == target)  {
                indices.add(i);
            }
        }
        return indices;
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        String currentTweetText;
        List<Integer> locationsOfAtSign;
        Set<String> result = new HashSet<String>();;
        int endIndex;
        for(Tweet T : tweets) {
            currentTweetText = T.getText().toLowerCase();
            currentTweetText = "!" + currentTweetText + "!";
            locationsOfAtSign =  findCharacterInString('@', currentTweetText);
            for(int startIndex : locationsOfAtSign) { 
                if(isValidUsernameCharacter(currentTweetText.charAt(startIndex - 1))) {
                    continue;
                }
                endIndex = startIndex + 1;
                while(isValidUsernameCharacter(currentTweetText.charAt(endIndex))){
                    endIndex++;
                }
                result.add(currentTweetText.substring(startIndex+1, endIndex));
                
            }
        }
        result.remove("@");
        result.remove("");
        
        
        return result;
        
    }

}
