package com.incampusit.staryaar.Comments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.incampusit.staryaar.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.incampusit.staryaar.R;
import com.incampusit.staryaar.SimpleClasses.API_CallBack;
import com.incampusit.staryaar.SimpleClasses.ApiRequest;
import com.incampusit.staryaar.SimpleClasses.Fragment_Data_Send;
import com.incampusit.staryaar.SimpleClasses.Functions;
import com.incampusit.staryaar.SimpleClasses.Variables;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 * A simple {@link Fragment} subclass.
 */
public class Comment_F extends RootFragment {

    public static int comment_count = 0;
    public static String commentid;
    public static int commentPosition;
    View view;
    Context context;
    RecyclerView recyclerView;
    Comments_Adapter adapter;
    ArrayList<Comment_Get_Set> data_list;
    String video_id;
    String user_id;
    String current_user = Variables.sharedPreferences.getString(Variables.u_id, "");
    EditText message_edit;
    TextView comment_reply_info;
    ImageButton send_btn;
    ProgressBar send_progress;
    TextView comment_count_txt;
    FrameLayout comment_screen;
    Fragment_Data_Send fragment_data_send;

    public Comment_F() {

    }

    @SuppressLint("ValidFragment")
    public Comment_F(int count, Fragment_Data_Send fragment_data_send) {
        comment_count = count;
        this.fragment_data_send = fragment_data_send;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comment, container, false);
        context = getContext();


        comment_screen = view.findViewById(R.id.comment_screen);
        comment_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_reply_info.setText("");
                getActivity().onBackPressed();

            }
        });

        view.findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_reply_info.setText("");
                getActivity().onBackPressed();
            }
        });


        Bundle bundle = getArguments();
        if (bundle != null) {
            video_id = bundle.getString("video_id");
            user_id = bundle.getString("user_id");
        }


        comment_count_txt = view.findViewById(R.id.comment_count);

        recyclerView = view.findViewById(R.id.recylerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);


        data_list = new ArrayList<>();
        adapter = new Comments_Adapter(context, data_list, new Comments_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, Comment_Get_Set item, View view) {
                switch (view.getId()) {
                    case R.id.user_pic:
                        // open user profile to be implemented
                        break;
                    case R.id.commentreply:
                        comment_reply_info.setText(item.user_handle + "," + postion + "," + item.comment_id);
                        // to select and open keyboard for edit text
                        message_edit.requestFocus();
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(message_edit, InputMethodManager.SHOW_IMPLICIT);
                        break;
                    case R.id.imageButton:
                        // like the user comment implmented
                        Send_Comment_Likes(item.comment_id, postion, "COMMENT");
                        break;
                }
            }

            @Override
            public void onReplyItemClick(int parentpostion, int positon, Comment_Reply_Get_Set item, View view) {
                switch (view.getId()) {
                    case R.id.user_pic:
                        // open user profile to be implemented
                        break;
                    case R.id.commentreply:
                        String userhandle = "<b>" + "@" + item.user_handle + "</b> ";
                        comment_reply_info.setText(item.user_handle + "," + item.comment_id + "," + parentpostion + "," + data_list.get(parentpostion).comment_id);
                        message_edit.setText(Html.fromHtml(userhandle), TextView.BufferType.EDITABLE);
                        message_edit.setSelection(message_edit.getText().length());
                        // to select and open keyboard for edit text
                        message_edit.requestFocus();
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(message_edit, InputMethodManager.SHOW_IMPLICIT);
                        break;
                    case R.id.imageButton:
                        // like the user reply implmented
                        Send_Reply_Likes(item.comment_id, parentpostion, positon, "REPLY");
                        break;
                }
            }
        });
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

        message_edit = view.findViewById(R.id.message_edit);
        comment_reply_info = view.findViewById(R.id.comment_reply_info);

        send_progress = view.findViewById(R.id.send_progress);
        send_btn = view.findViewById(R.id.send_btn);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = message_edit.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    if (Variables.sharedPreferences.getBoolean(Variables.islogin, false)) {
                        Send_Comments(video_id, message, comment_reply_info.getText().toString());
                        message_edit.setText(null);
                        send_progress.setVisibility(View.VISIBLE);
                        send_btn.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(context, "Please Login into the app", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        Get_All_Comments();


        return view;
    }


    @Override
    public void onDetach() {
        Functions.hideSoftKeyboard(getActivity());
        comment_reply_info.setText("");
        super.onDetach();
    }

    // this funtion will get all the comments against post
    public void Get_All_Comments() {

        Functions.Call_Api_For_get_Comment(getActivity(), video_id, new API_CallBack() {
            @Override
            public void ArrayData(ArrayList arrayList) {
                ArrayList<Comment_Get_Set> arrayList1 = arrayList;
                for (Comment_Get_Set item : arrayList1) {
                    data_list.add(item);
                }
                comment_count_txt.setText(data_list.size() + " comments");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void OnSuccess(String responce) {

            }

            @Override
            public void OnFail(String responce) {

            }

        });

    }

    // this function will send the like info for the comment and the replies
    public void Send_Comment_Likes(String comment_id, int position, String type) {
        Functions.Call_Api_For_Send_Likes(getActivity(), comment_id, type, new API_CallBack() {

            @Override
            public void ArrayData(ArrayList arrayList) {
                ArrayList<Comment_Like_Get_Set> likeList = arrayList;
                data_list.get(position).likesList = likeList;
                data_list.get(position).likes = String.valueOf(likeList.size());
                adapter.notifyItemChanged(position);
            }

            @Override
            public void OnSuccess(String responce) {
            }

            @Override
            public void OnFail(String responce) {

            }
        });
    }

    // this function will send the like info for the comment and the replies
    public void Send_Reply_Likes(String comment_id, int parentposition, int position, String type) {
        Functions.Call_Api_For_Send_Likes(getActivity(), comment_id, type, new API_CallBack() {

            @Override
            public void ArrayData(ArrayList arrayList) {
                ArrayList<Comment_Like_Get_Set> likeList = arrayList;
                data_list.get(parentposition).arrayList.get(position).likesList = likeList;
                data_list.get(parentposition).arrayList.get(position).likes = String.valueOf(likeList.size());
                adapter.notifyItemChanged(parentposition);
            }

            @Override
            public void OnSuccess(String responce) {
            }

            @Override
            public void OnFail(String responce) {

            }
        });
    }

    // this function will call an api to upload your comment
    public void Send_Comments(String video_id, final String comment, String comment_id) {

        if (!comment_id.trim().isEmpty()) {
            String[] strArr = comment_id.trim().split(",");
            commentid = strArr[strArr.length - 1];
            commentPosition = Integer.parseInt(strArr[strArr.length - 2]);
        } else {
            commentid = "";
            commentPosition = -1;
        }

        Functions.Call_Api_For_Send_Comment(getActivity(), video_id, comment, commentid, new API_CallBack() {
            @Override
            public void ArrayData(ArrayList arrayList) {

                ArrayList<Comment_Get_Set> arrayList1 = arrayList;
                for (Comment_Get_Set item : arrayList1) {
                    if (!comment_id.isEmpty() && commentPosition >= 0) {
                        data_list.remove(commentPosition);
                        data_list.add(commentPosition, item);
                        adapter.notifyItemInserted(commentPosition);
                    } else {
                        data_list.add(0, item);
                        adapter.notifyItemInserted(data_list.size() - 1);
                        comment_count++;
                    }

                    //SendPushNotification(getActivity(), user_id, comment);

                    comment_count_txt.setText(comment_count + " comments");

                    if (fragment_data_send != null)
                        fragment_data_send.onDataSent("" + comment_count);

                }
                //adapter.notifyItemInserted(0);
                send_progress.setVisibility(View.GONE);
                send_btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnSuccess(String responce) {

            }

            @Override
            public void OnFail(String responce) {

            }
        });

    }


    public void SendPushNotification(Activity activity, String user_id, String comment) {

        JSONObject notimap = new JSONObject();
        try {
            notimap.put("title", Variables.sharedPreferences.getString(Variables.u_name, "") + " Comment on your video");
            notimap.put("message", comment);
            notimap.put("icon", Variables.sharedPreferences.getString(Variables.u_pic, ""));
            notimap.put("senderid", Variables.sharedPreferences.getString(Variables.u_id, ""));
            notimap.put("receiverid", user_id);
            notimap.put("action_type", "comment");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.sendPushNotification, notimap, null);

    }


}
