package com.incampusit.staryaar.Notifications;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andremion.counterfab.CounterFab;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.incampusit.staryaar.Inbox.Inbox_F;
import com.incampusit.staryaar.Inbox.Inbox_Get_Set;
import com.incampusit.staryaar.Inbox.MovableFloatingActionButton;
import com.incampusit.staryaar.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.incampusit.staryaar.Profile.Profile_F;
import com.incampusit.staryaar.R;
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

import static com.incampusit.staryaar.Main_Menu.MainMenuFragment.tabLayout;

/*
 * A simple {@link Fragment} subclass.
 */
public class Notification_F extends RootFragment implements View.OnClickListener {

    private final static float CLICK_DRAG_TOLERANCE = 10; // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.
    public static String unread_count = "0";
    public static int unread_msgs = 0;
    View view;
    Context context;
    Notification_Adapter adapter;
    RecyclerView recyclerView;
    ArrayList<Notification_Get_Set> datalist;
    AdView adView;
    String user_id = Variables.sharedPreferences.getString(Variables.u_id, "");
    CounterFab counterFab;
    private float downRawX, downRawY;
    private float dX, dY;
    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            if (datalist != null && adapter != null) {
                datalist.clear();
                Call_Api_For_get_UserNotifications();
                Refresh_Notification_Count();
                adapter.notifyItemRangeInserted(1, datalist.size());
            }
            //Toast.makeText(context, "Notification Fragment "+message, Toast.LENGTH_SHORT).show();
        }
    };

    public Notification_F() {
        // Required empty public constructor
    }

    public static void Refresh_Notification_Count() {
        if (unread_count.equals("0")) {
            tabLayout.getTabAt(3).getCustomView().findViewById(R.id.notiftext).animate().alpha(0.f).setDuration(300);
        } else {
            TextView notification_text = tabLayout.getTabAt(3).getCustomView().findViewById(R.id.notiftext);
            notification_text.setText(unread_count);
            notification_text.animate().alpha(1.f).setDuration(300);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onNotice);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onNotice);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // register broadcast listen event
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                onNotice, new IntentFilter("custom-event-name"));

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        context = getContext();

        datalist = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recylerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        adapter = new Notification_Adapter(context, datalist, new Notification_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Notification_Get_Set item) {
                //Toast.makeText(view.getContext(), item.getTitle()+" clicked", Toast.LENGTH_SHORT).show();
                Log.d(Variables.TAG, " action " + item.getActionType());
                Call_Api_For_Mark_Notif_As_Read(item, postion);

                if (item.getActionType().equals("LIKE")) {
                    Intent intent = new Intent(getActivity(), WatchVideos_F.class);
                    intent.putExtra("notification_video_id", item.getVideoId());
                    intent.putExtra("notification_type", "LIKE");
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);

                } else if (item.getActionType().equals("COMMENT")) {
                    Intent intent = new Intent(getActivity(), WatchVideos_F.class);
                    intent.putExtra("notification_video_id", item.getVideoId());
                    intent.putExtra("notification_type", "COMMENT");
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);

                } else if (item.getActionType().equals("FOLLOW")) {

                    Profile_F profile_f = new Profile_F(new Fragment_Callback() {
                        @Override
                        public void Responce(Bundle bundle) {
                            // Call_Api_For_Singlevideos(currentPage);
                        }
                    });
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                    //if (true)
                    //transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
                    //else
                    transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);

                    Bundle args = new Bundle();
                    args.putString("user_id", item.senderFbId);
                    args.putString("user_name", "Test ");
                    args.putString("user_pic", item.icon);
                    profile_f.setArguments(args);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.MainMenuFragment, profile_f).commit();
                } else if (item.getActionType().equals("OTHERS")) {
                } else {
                }


            }

            @Override
            public void onProfileClick(View view, int postion, Notification_Get_Set item) {
                Call_Api_For_Mark_Notif_As_Read(item, postion);

                Profile_F profile_f = new Profile_F(new Fragment_Callback() {
                    @Override
                    public void Responce(Bundle bundle) {
                    }
                });
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);

                Bundle args = new Bundle();
                args.putString("user_id", item.senderFbId);
                args.putString("user_name", "Test ");
                args.putString("user_pic", item.icon);
                profile_f.setArguments(args);
                transaction.addToBackStack(null);
                transaction.replace(R.id.MainMenuFragment, profile_f).commit();
            }
        }
        );
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);

        view.findViewById(R.id.inbox_btn).setOnClickListener(this);

        setupCounterFAB();
        Call_Api_For_get_UnreadMessages();

        MovableFloatingActionButton mvButton = view.findViewById(R.id.inbox_btn);
        mvButton.setImageBitmap(Functions.textAsBitmap("29", 25, Color.RED));
        mvButton.setBackground(getContext().getDrawable(R.drawable.ic_notification_gray));

        return view;
    }

    private void setupCounterFAB() {
        counterFab = view.findViewById(R.id.counter_fab);
        counterFab.setOnClickListener(this);
        counterFab.setCount(unread_msgs);
        //counterFab.setCount(12); // Set the count value to show on badge
        counterFab.setActivated(false);
        counterFab.setPadding(0, 0, 0, 0);
        counterFab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

                int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {

                    downRawX = motionEvent.getRawX();
                    downRawY = motionEvent.getRawY();
                    dX = view.getX() - downRawX;
                    dY = view.getY() - downRawY;

                    return true; // Consumed

                } else if (action == MotionEvent.ACTION_MOVE) {

                    int viewWidth = view.getWidth();
                    int viewHeight = view.getHeight();

                    View viewParent = (View) view.getParent();
                    int parentWidth = viewParent.getWidth();
                    int parentHeight = viewParent.getHeight();

                    float newX = motionEvent.getRawX() + dX;
                    newX = Math.max(layoutParams.leftMargin, newX); // Don't allow the FAB past the left hand side of the parent
                    newX = Math.min(parentWidth - viewWidth - layoutParams.rightMargin, newX); // Don't allow the FAB past the right hand side of the parent

                    float newY = motionEvent.getRawY() + dY;
                    newY = Math.max(layoutParams.topMargin, newY); // Don't allow the FAB past the top of the parent
                    newY = Math.min(parentHeight - viewHeight - layoutParams.bottomMargin, newY); // Don't allow the FAB past the bottom of the parent

                    view.animate()
                            .x(newX)
                            .y(newY)
                            .setDuration(0)
                            .start();

                    return true; // Consumed

                } else if (action == MotionEvent.ACTION_UP) {

                    float upRawX = motionEvent.getRawX();
                    float upRawY = motionEvent.getRawY();

                    float upDX = upRawX - downRawX;
                    float upDY = upRawY - downRawY;

                    if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                        return view.performClick();
                    } else { // A drag
                        return true; // Consumed
                    }

                } else {
                    //return onTouchEvent(motionEvent);
                    return view.onTouchEvent(motionEvent);
                }

            }
        });
    }

    private void Call_Api_For_get_UnreadMessages() {

        DatabaseReference root_ref;
        ValueEventListener eventListener2;
        Query inbox_query;

        root_ref = FirebaseDatabase.getInstance().getReference();
        if (Variables.sharedPreferences.getBoolean(Variables.islogin, false)) {
            inbox_query = root_ref.child("Inbox").child(Variables.user_id).orderByChild("date");
            inbox_query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Notification_F.unread_msgs = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        try {
                            Inbox_Get_Set model = ds.getValue(Inbox_Get_Set.class);
                            if (model != null && model.getStatus().equals("0")) {
                                Notification_F.unread_msgs += 1;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    counterFab.setCount(unread_msgs);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }


    private void Call_Api_For_Mark_Notif_As_Read(Notification_Get_Set notification, final int position) {

        // for making swift change in notification read
        notification.isRead = true;
        datalist.remove(position);
        datalist.add(position, notification);
        adapter.notifyDataSetChanged();
        // for making swift change in notification read

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("notification_id", notification.id);
            parameters.put("fb_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.marknotificationasread, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.optString("code");
                    if (!code.equals("200")) {
                        notification.isRead = false;
                        datalist.remove(position);
                        datalist.add(position, notification);
                        adapter.notifyDataSetChanged();
                    } else {
                        unread_count = jsonObject.optString("msg");
                        Refresh_Notification_Count();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getNotifications() {
        Call_Api_For_get_UserNotifications();
    }

    @Override
    public void onStart() {
        super.onStart();
        adView = view.findViewById(R.id.bannerad);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        if (view != null) {
            if (Variables.sharedPreferences.getBoolean(Variables.islogin, false))
                getNotifications();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                onNotice, new IntentFilter("custom-event-name"));
        if (view != null) {
            if (Variables.sharedPreferences.getBoolean(Variables.islogin, false)) {
                Log.d(Variables.TAG, "onResume Notification Fragment");
                getNotifications();
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.counter_fab:
                Open_inbox_F();
                break;
        }
    }

    private void Open_inbox_F() {

        Inbox_F inbox_f = new Inbox_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, inbox_f).commit();

    }

    // Bottom two function will call the api and get all the videos form api and parse the json data
    private void Call_Api_For_get_UserNotifications() {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.Call_Api(context, Variables.get_notifications, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Parse_notification_data(resp);
            }
        });
    }

    public void Parse_notification_data(String responce) {

        datalist.clear();

        try {
            JSONObject jsonObject = new JSONObject(responce);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                JSONArray msgArray = jsonObject.getJSONArray("msg");
                unread_count = jsonObject.optString("unread");
                Log.d(Variables.TAG, "unread notification " + unread_count);
                for (int i = 0; i < msgArray.length(); i++) {
                    JSONObject notification_data = msgArray.optJSONObject(i);

                    Notification_Get_Set item = new Notification_Get_Set();
                    item.id = notification_data.optString("id");
                    item.videoId = notification_data.optString("video_id");
                    item.senderFbId = notification_data.optString("sender_id");
                    item.receiverFbId = notification_data.optString("receiver_id");
                    item.actionType = notification_data.optString("action_type");
                    item.title = "@" + notification_data.optString("sender_handle") + " " + notification_data.optString("title");
                    item.message = notification_data.optString("messagebody");
                    item.icon = notification_data.optString("sender_pic");
                    item.otherData = notification_data.optString("otherdata");
                    item.isRead = notification_data.optString("isread").equals("1");
                    Log.d(Variables.TAG, "isread " + item.getId() + " " + notification_data.optString("isread"));
                    item.readOn = notification_data.optString("readon");
                    item.createdOn = notification_data.optString("created");
                    datalist.add(item);
                    adapter.notifyItemInserted(i);
                }
                Log.d(Variables.TAG, "datalist " + datalist.size() + " response " + responce);
                adapter.notifyDataSetChanged();

                if (datalist.isEmpty()) {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                } else
                    view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);

            } else {
                Toast.makeText(context, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
