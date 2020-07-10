package com.fbu.instagrom.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;

@ParseClassName("Post")
@Parcel(analyze = Post.class)
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATEDAT = "createdAt";
    public static final String KEY_COMMENTS = "comments";

    public Post() {
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public Date getTime() {
        return getCreatedAt();
    }

    public ArrayList<Comment> getCommentList(){
        return (ArrayList<Comment>)get(KEY_COMMENTS);
    }
    public void setComment(Comment comment){
        add(KEY_COMMENTS,comment);
    }
}
