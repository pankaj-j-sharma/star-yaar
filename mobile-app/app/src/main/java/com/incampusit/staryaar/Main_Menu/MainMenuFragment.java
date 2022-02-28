package com.incampusit.staryaar.Main_Menu;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.incampusit.staryaar.Accounts.Login_A;
import com.incampusit.staryaar.Chat.Chat_Activity;
import com.incampusit.staryaar.Discover.Discover_F;
import com.incampusit.staryaar.Home.Home_F;
import com.incampusit.staryaar.Inbox.Inbox_Get_Set;
import com.incampusit.staryaar.Main_Menu.RelateToFragment_OnBack.OnBackPressListener;
import com.incampusit.staryaar.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.incampusit.staryaar.Notifications.Notification_F;
import com.incampusit.staryaar.Profile.Profile_Tab_F;
import com.incampusit.staryaar.R;
import com.incampusit.staryaar.SimpleClasses.ApiRequest;
import com.incampusit.staryaar.SimpleClasses.Callback;
import com.incampusit.staryaar.SimpleClasses.Variables;
import com.incampusit.staryaar.argear.ARGearConfig;
import com.incampusit.staryaar.argear.ArGearCameraActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.incampusit.staryaar.Notifications.Notification_F.unread_count;


public class MainMenuFragment extends RootFragment implements View.OnClickListener {

    public static TabLayout tabLayout;

    protected Custom_ViewPager pager;
    Context context;
    String user_id = Variables.sharedPreferences.getString(Variables.u_id, "");
    private ViewPagerAdapter adapter;
    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            //Toast.makeText(context, "Main Fragment "+message, Toast.LENGTH_SHORT).show();
            //TextView notification_text=(TextView)tabLayout.getTabAt(3).getCustomView().findViewById(R.id.notiftext);
            //notification_text.setText("1");
            //tabLayout.getTabAt(3).getCustomView().findViewById(R.id.notiftext).animate().alpha(1.f).setDuration(300);
        }
    };

    public MainMenuFragment() {

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                onNotice, new IntentFilter("custom-event-name"));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) {
            MenuItem item = menu.findItem(R.id.not_send_messsage);
            if (item != null) {
                item.setIcon(R.drawable.ic_notification_gray);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        //LocalBroadcastManager.getInstance(getContext()).registerReceiver(
        //        onNotice, new IntentFilter("custom-event-name"));
        if (Variables.sharedPreferences.getBoolean(Variables.islogin, false)) {
            Call_Api_For_get_UnreadNotifications();
            Call_Api_For_get_UnreadMessages();
        }

        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        context = getContext();
        tabLayout = view.findViewById(R.id.tabs);
        pager = view.findViewById(R.id.viewpager);
        pager.setOffscreenPageLimit(5);
        pager.setPagingEnabled(false);
        view.setOnClickListener(this);

        return view;
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
                    Log.d(Variables.TAG, "messages unread " + Notification_F.unread_msgs);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Note that we are passing childFragmentManager, not FragmentManager
        adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        setupTabIcons();

        // look for data in the recieved notifications
        if (getArguments() != null && getArguments().containsKey("google.message_id")) {
            Log.d(Variables.TAG, "Notification to main menu ");
            tabLayout.getTabAt(3).select();
        }
    }

    public boolean onBackPressed() {
        // currently visible tab Fragment
        OnBackPressListener currentFragment = (OnBackPressListener) adapter.getRegisteredFragment(pager.getCurrentItem());

        if (currentFragment != null) {
            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
            return currentFragment.onBackPressed();
        }

        // this Fragment couldn't handle the onBackPressed call
        return false;
    }

    // Bottom two function will call the api and get all the videos form api and parse the json data
    private void Call_Api_For_get_UnreadNotifications() {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.Call_Api(getContext(), Variables.get_unread_notifications, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.optString("code");
                    if (code.equals("200")) {
                        unread_count = jsonObject.optString("msg");
                        if (tabLayout != null) {
                            Notification_F.Refresh_Notification_Count();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // this function will set all the icon and text in
    // Bottom tabs when we open an activity
    private void setupTabIcons() {

        View view1 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView1 = view1.findViewById(R.id.image);
        TextView title1 = view1.findViewById(R.id.text);
        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_white));
        imageView1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_color)));
        title1.setText("Home");
        title1.setTextColor(context.getResources().getColor(R.color.app_color));
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView2 = view2.findViewById(R.id.image);
        TextView title2 = view2.findViewById(R.id.text);
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.ic_discovery_gray));
        imageView2.setColorFilter(ContextCompat.getColor(context, R.color.colorwhite_50), android.graphics.PorterDuff.Mode.SRC_IN);
        title2.setText("Discover");
        title2.setTextColor(context.getResources().getColor(R.color.colorwhite_50));
        tabLayout.getTabAt(1).setCustomView(view2);


        View view3 = LayoutInflater.from(context).inflate(R.layout.item_add_tab_layout, null);
        tabLayout.getTabAt(2).setCustomView(view3);

        View view4 = LayoutInflater.from(context).inflate(R.layout.item_notif_tablayout, null);
        ImageView imageView4 = view4.findViewById(R.id.image);
        TextView title4 = view4.findViewById(R.id.text);
        imageView4.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_gray));
        imageView4.setColorFilter(ContextCompat.getColor(context, R.color.colorwhite_50), android.graphics.PorterDuff.Mode.SRC_IN);
        title4.setText("Alerts");
        title4.setTextColor(context.getResources().getColor(R.color.colorwhite_50));
        tabLayout.getTabAt(3).setCustomView(view4);

        View view5 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView5 = view5.findViewById(R.id.image);
        TextView title5 = view5.findViewById(R.id.text);
        imageView5.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_gray));
        imageView5.setColorFilter(ContextCompat.getColor(context, R.color.colorwhite_50), android.graphics.PorterDuff.Mode.SRC_IN);
        title5.setText("Profile");
        title5.setTextColor(context.getResources().getColor(R.color.colorwhite_50));
        tabLayout.getTabAt(4).setCustomView(view5);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                ImageView image = v.findViewById(R.id.image);
                TextView title = v.findViewById(R.id.text);

                switch (tab.getPosition()) {
                    case 0:
                        OnHome_Click();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_white));
                        image.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_color)));
                        title.setTextColor(context.getResources().getColor(R.color.app_color));
                        break;

                    case 1:
                        Onother_Tab_Click();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_discover_red));
                        image.setColorFilter(ContextCompat.getColor(context, R.color.app_color), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.app_color));
                        break;


                    case 3:
                        Onother_Tab_Click();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_red));
                        image.setColorFilter(ContextCompat.getColor(context, R.color.app_color), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.app_color));
                        break;
                    case 4:
                        Onother_Tab_Click();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_red));
                        image.setColorFilter(ContextCompat.getColor(context, R.color.app_color), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.app_color));
                        break;
                }
                tab.setCustomView(v);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                ImageView image = v.findViewById(R.id.image);
                TextView title = v.findViewById(R.id.text);

                switch (tab.getPosition()) {
                    case 0:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_gray));
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;
                    case 1:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_discovery_gray));
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;

                    case 3:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_gray));
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;
                    case 4:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_gray));
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;
                }
                tab.setCustomView(v);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


        final LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        tabStrip.setEnabled(false);

        tabStrip.getChildAt(2).setClickable(false);
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check_permissions()) {
                    if (Variables.sharedPreferences.getBoolean(Variables.islogin, false)) {

                        //Intent intent = new Intent(getActivity(), Video_Recoder_A.class);
                        //Load config from Firebase if available
                        LoadARGearConfig();
                    } else {
                        Toast.makeText(context, "You have to login First", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        tabStrip.getChildAt(3).setClickable(false);

        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Variables.sharedPreferences.getBoolean(Variables.islogin, false)) {

                    TabLayout.Tab tab = tabLayout.getTabAt(3);
                    tab.select();

                } else {

                    Intent intent = new Intent(getActivity(), Login_A.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
                }

            }
        });

        tabStrip.getChildAt(4).setClickable(false);
        view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Variables.sharedPreferences.getBoolean(Variables.islogin, false)) {

                    TabLayout.Tab tab = tabLayout.getTabAt(4);
                    tab.select();

                } else {

                    Intent intent = new Intent(getActivity(), Login_A.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
                }

            }
        });


        if (MainMenuActivity.intent != null) {

            if (MainMenuActivity.intent.hasExtra("action_type")) {


                if (Variables.sharedPreferences.getBoolean(Variables.islogin, false)) {
                    String action_type = MainMenuActivity.intent.getExtras().getString("action_type");

                    if (action_type.equals("message")) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                TabLayout.Tab tab = tabLayout.getTabAt(3);
                                tab.select();
                            }
                        }, 1500);


                        String id = MainMenuActivity.intent.getExtras().getString("senderid");
                        String name = MainMenuActivity.intent.getExtras().getString("title");
                        String icon = MainMenuActivity.intent.getExtras().getString("icon");

                        chatFragment(id, name, icon);

                    }
                }

            }

        }


    }

    private void LoadARGearConfig() {
        FirebaseDatabase.getInstance().getReference().child("ARGear").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {

                    ARGearConfig config = snapshot.getValue(ARGearConfig.class);
                    Log.d(Variables.TAG, " ARGear obj " + config.getAPI_KEY() + " " + config.getAPI_URL() + " " + config.getAUTH_KEY() + " " + config.getSECRET_KEY());

                    Intent intent = new Intent(getActivity(), ArGearCameraActivity.class);
                    if (!(config.getAPI_URL().isEmpty() || config.getAPI_KEY().isEmpty() || config.getAUTH_KEY().isEmpty() || config.getSECRET_KEY().isEmpty())) {
                        intent.putExtra("API_URL", config.getAPI_URL());
                        intent.putExtra("API_KEY", config.getAPI_KEY());
                        intent.putExtra("AUTH_KEY", config.getAUTH_KEY());
                        intent.putExtra("SECRET_KEY", config.getSECRET_KEY());
                    }
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);

                } catch (Exception e) {

                    Log.d(Variables.TAG, "Error on loading config " + e.toString());
                    e.printStackTrace();

                    Intent intent = new Intent(getActivity(), ArGearCameraActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void OnHome_Click() {

        TabLayout.Tab tab1 = tabLayout.getTabAt(1);
        View view1 = tab1.getCustomView();
        ImageView imageView1 = view1.findViewById(R.id.image);
        imageView1.setColorFilter(ContextCompat.getColor(context, R.color.colorwhite_50), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView tex1 = view1.findViewById(R.id.text);
        tex1.setTextColor(context.getResources().getColor(R.color.colorwhite_50));
        tab1.setCustomView(view1);

        TabLayout.Tab tab2 = tabLayout.getTabAt(2);
        View view2 = tab2.getCustomView();
        ImageView image = view2.findViewById(R.id.image);
        //image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_white));
        image.setImageDrawable(context.getResources().getDrawable(R.drawable.star_home));
        tab2.setCustomView(view2);

        TabLayout.Tab tab3 = tabLayout.getTabAt(3);
        View view3 = tab3.getCustomView();
        ImageView imageView3 = view3.findViewById(R.id.image);
        imageView3.setColorFilter(ContextCompat.getColor(context, R.color.colorwhite_50), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView tex3 = view3.findViewById(R.id.text);
        tex3.setTextColor(context.getResources().getColor(R.color.colorwhite_50));
        tab3.setCustomView(view3);

        TabLayout.Tab tab4 = tabLayout.getTabAt(4);
        View view4 = tab4.getCustomView();
        ImageView imageView4 = view4.findViewById(R.id.image);
        imageView4.setColorFilter(ContextCompat.getColor(context, R.color.colorwhite_50), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView tex4 = view4.findViewById(R.id.text);
        tex4.setTextColor(context.getResources().getColor(R.color.colorwhite_50));
        tab4.setCustomView(view4);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pager.setLayoutParams(params);
        tabLayout.setBackground(getResources().getDrawable(R.drawable.d_top_white_line));
    }

    public void Onother_Tab_Click() {


        TabLayout.Tab tab1 = tabLayout.getTabAt(1);
        View view1 = tab1.getCustomView();
        TextView tex1 = view1.findViewById(R.id.text);
        ImageView imageView1 = view1.findViewById(R.id.image);
        imageView1.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        tex1.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab1.setCustomView(view1);

        TabLayout.Tab tab2 = tabLayout.getTabAt(2);
        View view2 = tab2.getCustomView();
        ImageView image = view2.findViewById(R.id.image);
        //image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_black));
        image.setImageDrawable(context.getResources().getDrawable(R.drawable.star_home));
        tab2.setCustomView(view2);

        TabLayout.Tab tab3 = tabLayout.getTabAt(3);
        View view3 = tab3.getCustomView();
        ImageView imageView3 = view3.findViewById(R.id.image);
        imageView3.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView tex3 = view3.findViewById(R.id.text);
        tex3.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab3.setCustomView(view3);


        TabLayout.Tab tab4 = tabLayout.getTabAt(4);
        View view4 = tab4.getCustomView();
        ImageView imageView4 = view4.findViewById(R.id.image);
        imageView4.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView tex4 = view4.findViewById(R.id.text);
        tex4.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab4.setCustomView(view4);


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ABOVE, R.id.tabs);
        pager.setLayoutParams(params);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.white));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2) {
            if (grantResults.length > 0) {
                //boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                //boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                for (int i = 0; i < grantResults.length; i++) {
                    if (!(grantResults[i] == PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(context, "permission denied", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (Variables.sharedPreferences.getBoolean(Variables.islogin, false)) {

                    //Intent intent = new Intent(getActivity(), Video_Recoder_A.class);
                    //Load config from Firebase if available
                    LoadARGearConfig();
                } else {
                    Toast.makeText(context, "You have to login First", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(context, "permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    // we need 4 permission during creating an video so we will get that permission
    // before start the video recording
    public boolean check_permissions() {

        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(context, PERMISSIONS)) {
            requestPermissions(PERMISSIONS, 2);
        } else {

            return true;
        }

        return false;
    }

    public void chatFragment(String receiverid, String name, String picture) {
        Chat_Activity chat_activity = new Chat_Activity();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);

        Bundle args = new Bundle();
        args.putString("user_id", receiverid);
        args.putString("user_name", name);
        args.putString("user_pic", picture);

        chat_activity.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, chat_activity).commit();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {


        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();


        public ViewPagerAdapter(final Resources resources, FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            final Fragment result;
            switch (position) {
                case 0:
                    result = new Home_F();
                    break;

                case 1:
                    result = new Discover_F();
                    break;

                case 2:
                    result = new BlankFragment();
                    break;

                case 3:
                    result = new Notification_F();
                    break;

                case 4:
                    result = new Profile_Tab_F();
                    break;

                default:
                    result = null;
                    break;
            }

            return result;
        }

        @Override
        public int getCount() {
            return 5;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            registeredFragments.remove(position);

            super.destroyItem(container, position, object);

        }


        /*
         * Get the Fragment by position
         *
         * @param position tab position of the fragment
         * @return
         */
        public Fragment getRegisteredFragment(int position) {

            return registeredFragments.get(position);

        }
    }


}