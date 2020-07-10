package com.fbu.instagrom.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.parceler.Parcel;

import java.util.Date;

@ParseClassName("Post")
@Parcel(analyze = Post.class)
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATEDAT = "createdAt";
    public static final String KEY_LIKES = "likes";

    public Post() {
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public Date getTime() {
        return getCreatedAt();
    }


    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

//TODO: like feature
    public JSONArray getLikes() {
        if (getJSONArray(KEY_LIKES) == null){
            JSONArray emptyArray = new JSONArray();
            put("likes", emptyArray);
            saveInBackground();
        }
        return getJSONArray(KEY_LIKES);
    }

    public void addLikes(String userId){

    }

    public boolean likedByUser(String userId, JSONArray likeList){
     return false;
    }
}
