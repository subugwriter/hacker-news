package com.xy.hackernews;

import com.google.gson.Gson;
import com.xy.hackernews.InstrumentedTestUtil;
import com.xy.hackernews.model.models.Comment;
import com.xy.hackernews.model.models.Story;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xy on 25/11/2017.
 */
public class TestData {

    public static List<Integer> storyIds = Arrays.asList(10000000, 10000001, 10000002);
    public static List<Integer> commentIds = Arrays.asList(20000000, 20000001, 20000002);
    public static List<Integer> realCommentIds = Arrays.asList(15729052, 15728291, 15728542);

    public static List<Story> stories = Arrays.asList(
            createStory(10000000, commentIds),
            createStory(10000001, commentIds),
            createStory(10000002, commentIds)
    );

    public static List<Comment> comments = Arrays.asList(
            createComment(20000000),
            createComment(20000001),
            createComment(20000002)
    );

    private static Story createStory(int id, List<Integer> kids) {
        String jsonStr = "{"
                + "\"by\" : \"dhouston\","
                + "\"descendants\" : 71,"
                + "\"id\" : " + id + ","
                + "\"kids\" : [ "
                + InstrumentedTestUtil.concatStringsWSep(kids, ", ")
                + "],"
                + "\"score\" : 111,"
                + "\"time\" : 1175714200,"
                + "\"title\" : \"My YC app: Dropbox - Throw away your USB drive\","
                + "\"type\" : \"story\","
                + "\"url\" : \"http://www.getdropbox.com/u/2/screencast.html\""
                + "}";
        Gson gson = new Gson();
        Story obj = gson.fromJson(jsonStr, Story.class);
        return obj;
    }

    private static Comment createComment(int id) {
        //dummy kids, no important
        List<Integer> kids = Arrays.asList(30000000, 30000001, 30000002);
        String jsonStr = "{"
                + "\"by\" : \"norvig\","
                + "\"id\" : " + id + ","
                + "\"kids\" : [ "
                + InstrumentedTestUtil.concatStringsWSep(kids, ", ")
                + "],"
                + "\"text\" : \"Aw shucks, guys ... you make me blush with your compliments.<p>Tell you what, Ill make a deal: I'll keep writing if you keep reading. K?\","
                + "\"time\" : 1314211127,"
                + "\"type\" : \"comment\""
                + "}";
        Gson gson = new Gson();
        Comment obj = gson.fromJson(jsonStr, Comment.class);
        return obj;
    }
}
