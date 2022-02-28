package com.incampusit.staryaar.Discover;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.incampusit.staryaar.Following.Following_Adapter;
import com.incampusit.staryaar.Following.Following_Get_Set;
import com.incampusit.staryaar.Home.Home_Get_Set;
import com.incampusit.staryaar.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.incampusit.staryaar.Profile.Profile_F;
import com.incampusit.staryaar.R;
import com.incampusit.staryaar.SimpleClasses.API_CallBack;
import com.incampusit.staryaar.SimpleClasses.ApiRequest;
import com.incampusit.staryaar.SimpleClasses.Callback;
import com.incampusit.staryaar.SimpleClasses.Fragment_Callback;
import com.incampusit.staryaar.SimpleClasses.Functions;
import com.incampusit.staryaar.SimpleClasses.Variables;
import com.incampusit.staryaar.WatchVideos.WatchVideos_F;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 * A simple {@link Fragment} subclass.
 */
public class Discover_F extends RootFragment {

    View view;
    Context context;

    RecyclerView recyclerView, user_recyclerview;
    EditText search_edit;


    SwipeRefreshLayout swiperefresh;
    ArrayList<Discover_Get_Set> datalist;
    ArrayList<Following_Get_Set> userlist;
    Discover_Adapter adapter;
    Following_Adapter user_adapter;

    public Discover_F() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_discover, container, false);
        context = getContext();


        datalist = new ArrayList<>();


        recyclerView = view.findViewById(R.id.recylerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new Discover_Adapter(context, datalist, new Discover_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(ArrayList<Home_Get_Set> datalist, int postion) {
                OpenWatchVideo(postion, datalist);
            }
        });


        recyclerView.setAdapter(adapter);

        setupUserSearchRecycler();

        search_edit = view.findViewById(R.id.search_edit);
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String query = search_edit.getText().toString();
                if (adapter != null)
                    adapter.getFilter().filter(query);
                if (user_adapter != null)
                    user_adapter.getFilter().filter(query);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        swiperefresh = view.findViewById(R.id.swiperefresh);
        swiperefresh.setColorSchemeResources(R.color.black);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Call_Api_For_get_Allvideos();
                Call_Api_For_get_AllUsers();
            }
        });


        Call_Api_For_get_Allvideos();
        Call_Api_For_get_AllUsers();
        return view;
    }

    private void setupUserSearchRecycler() {

        final LinearLayoutManager userlayoutManager = new LinearLayoutManager(context);
        userlist = new ArrayList<>();
        user_recyclerview = view.findViewById(R.id.user_recylerview);
        user_recyclerview.setLayoutManager(userlayoutManager);
        user_recyclerview.setHasFixedSize(true);

        user_adapter = new Following_Adapter(context, "following", userlist, new Following_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Following_Get_Set item) {

                switch (view.getId()) {
                    case R.id.action_txt:
                        Follow_unFollow_User(item, postion);
                        break;

                    case R.id.mainlayout:
                        OpenProfile(item);
                        break;

                }

            }
        }
        );

        user_recyclerview.setAdapter(user_adapter);
        Call_Api_For_get_AllUsers();
    }

    private void Call_Api_For_get_AllUsers() {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id, ""));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.Call_Api(context, Variables.get_all_users, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Parse_following_data(resp);
            }
        });

    }

    public void Parse_following_data(String responce) {

        userlist.clear();

        try {
            JSONObject jsonObject = new JSONObject(responce);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                JSONArray msgArray = jsonObject.getJSONArray("msg");
                for (int i = 0; i < msgArray.length(); i++) {
                    JSONObject profile_data = msgArray.optJSONObject(i);

                    JSONObject follow_Status = profile_data.optJSONObject("follow_Status");

                    Following_Get_Set item = new Following_Get_Set();
                    item.fb_id = profile_data.optString("fb_id");

                    // skip if the profile is your own profile
                    if (!item.fb_id.equals(Variables.sharedPreferences.getString(Variables.u_id, ""))) {
                        item.first_name = profile_data.optString("first_name");
                        item.last_name = profile_data.optString("last_name");
                        item.bio = profile_data.optString("bio");
                        item.username = profile_data.optString("username");
                        item.profile_pic = profile_data.optString("profile_pic");
                        item.user_handle = profile_data.optString("user_handle");

                        item.follow = follow_Status.optString("follow");
                        item.follow_status_button = follow_Status.optString("follow_status_button");

                        if (Variables.sharedPreferences.getBoolean(Variables.islogin, false)) {
                            Log.d(Variables.TAG, "Logged in " + i);
                            item.is_show_follow_unfollow_btn = true;
                        } else {
                            item.is_show_follow_unfollow_btn = false;
                        }

                        userlist.add(item);
                    }
                    //user_adapter.notifyItemInserted(i);
                }

                user_adapter.notifyDataSetChanged();

            } else {
                Toast.makeText(context, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    // this will open the profile of user which have uploaded the currenlty running video
    private void OpenProfile(final Following_Get_Set item) {
        Profile_F profile_f = new Profile_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {

            }
        });
        Functions.hideSoftKeyboard(getActivity());
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        Bundle args = new Bundle();
        args.putString("user_id", item.fb_id);
        args.putString("user_name", item.first_name + " " + item.last_name);
        args.putString("user_pic", item.profile_pic);
        profile_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, profile_f).commit();
    }

    public void Follow_unFollow_User(final Following_Get_Set item, final int position) {

        final String send_status;
        if (item.follow.equals("0")) {
            send_status = "1";
        } else {
            send_status = "0";
        }

        Functions.Call_Api_For_Follow_or_unFollow(getActivity(),
                Variables.sharedPreferences.getString(Variables.u_id, ""),
                item.fb_id,
                send_status,
                new API_CallBack() {
                    @Override
                    public void ArrayData(ArrayList arrayList) {


                    }

                    @Override
                    public void OnSuccess(String responce) {

                        if (send_status.equals("1")) {
                            item.follow = "1";
                            userlist.remove(position);
                            userlist.add(position, item);
                        } else if (send_status.equals("0")) {
                            item.follow = "0";
                            userlist.remove(position);
                            userlist.add(position, item);
                        }

                        user_adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void OnFail(String responce) {

                    }

                });


    }

    // Bottom two function will get the Discover videos
    // from api and parse the json data which is shown in Discover tab

    private void Call_Api_For_get_Allvideos() {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id, "0"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("resp", parameters.toString());

        ApiRequest.Call_Api(context, Variables.discover, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Parse_data(resp);
                swiperefresh.setRefreshing(false);
            }
        });


    }


    public void Parse_data(String responce) {

        datalist.clear();

        try {
            JSONObject jsonObject = new JSONObject(responce);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                JSONArray msgArray = jsonObject.getJSONArray("msg");
                for (int d = 0; d < msgArray.length(); d++) {

                    Discover_Get_Set discover_get_set = new Discover_Get_Set();
                    JSONObject discover_object = msgArray.optJSONObject(d);
                    discover_get_set.title = discover_object.optString("section_name");

                    JSONArray video_array = discover_object.optJSONArray("sections_videos");

                    ArrayList<Home_Get_Set> video_list = new ArrayList<>();
                    for (int i = 0; i < video_array.length(); i++) {
                        JSONObject itemdata = video_array.optJSONObject(i);
                        Home_Get_Set item = new Home_Get_Set();


                        JSONObject user_info = itemdata.optJSONObject("user_info");
                        item.fb_id = user_info.optString("fb_id");
                        item.first_name = user_info.optString("first_name");
                        item.last_name = user_info.optString("last_name");
                        item.profile_pic = user_info.optString("profile_pic");

                        JSONObject count = itemdata.optJSONObject("count");
                        item.like_count = count.optString("like_count");
                        item.video_comment_count = count.optString("video_comment_count");

                        JSONObject sound_data = itemdata.optJSONObject("sound");
                        item.sound_id = sound_data.optString("id");
                        item.sound_name = sound_data.optString("sound_name");
                        item.sound_pic = sound_data.optString("thum");


                        item.video_id = itemdata.optString("id");
                        item.liked = itemdata.optString("liked");
                        item.video_url = Variables.base_url + itemdata.optString("video");
                        item.thum = Variables.base_url + itemdata.optString("thum");
                        item.gif = Variables.base_url + itemdata.optString("gif");
                        item.created_date = itemdata.optString("created");
                        item.video_description = itemdata.optString("description");

                        video_list.add(item);
                    }

                    discover_get_set.arrayList = video_list;

                    datalist.add(discover_get_set);

                }

                adapter.notifyDataSetChanged();

            } else {
                Toast.makeText(context, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    // When you click on any Video a new activity is open which will play the Clicked video
    private void OpenWatchVideo(int postion, ArrayList<Home_Get_Set> data_list) {

        Intent intent = new Intent(getActivity(), WatchVideos_F.class);
        intent.putExtra("arraylist", data_list);
        intent.putExtra("position", postion);
        startActivity(intent);

    }


}
