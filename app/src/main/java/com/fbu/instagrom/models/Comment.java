package com.fbu.instagrom.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.Date;

@ParseClassName("Comment")
@Parcel(analyze = Comment.class)
public class Comment extends ParseObject {
    public static final String KEY_TEXT = "text";
    public static final String KEY_USER = "user";
    public static final String KEY_POST = "post";
    public static final String KEY_CREATEDAT = "createdAt";

    public String getText() throws ParseException {
        return fetchIfNeeded().getString(KEY_TEXT);
    }

    public void setText(String text) {
        put(KEY_TEXT, text);
    }

    public String getUser() {
        return KEY_USER;
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseObject getPost() throws ParseException{
        return getParseObject(KEY_POST).fetchIfNeeded();
    }

    public void setPost (Post post){
        put(KEY_POST, post);
    }

    public Date getTime() {
        return getCreatedAt();
    }
}
