package com.incampusit.staryaar.Comments;

/*
 * Created by PANKAJ on 3/5/2019.
 */

import java.util.ArrayList;

public class Comment_Get_Set {
    public String video_id, fb_id, comment_id, first_name, last_name, profile_pic, comments, created, likes, replies, user_handle;
    public Boolean user_liked = false, show_replies = false;
    public ArrayList<Comment_Reply_Get_Set> arrayList;
    public ArrayList<Comment_Like_Get_Set> likesList;
}
