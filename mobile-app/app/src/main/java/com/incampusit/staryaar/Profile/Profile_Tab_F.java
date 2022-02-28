package com.incampusit.staryaar.Profile;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.material.tabs.TabLayout;
import com.incampusit.staryaar.Accounts.Login_A;
import com.incampusit.staryaar.Following.Following_F;
import com.incampusit.staryaar.Main_Menu.MainMenuActivity;
import com.incampusit.staryaar.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.incampusit.staryaar.Profile.Liked_Videos.Liked_Video_F;
import com.incampusit.staryaar.Profile.UserVideos.UserVideo_F;
import com.incampusit.staryaar.R;
import com.incampusit.staryaar.See_Full_Image_F;
import com.incampusit.staryaar.SimpleClasses.ApiRequest;
import com.incampusit.staryaar.SimpleClasses.Callback;
import com.incampusit.staryaar.SimpleClasses.Fragment_Callback;
import com.incampusit.staryaar.SimpleClasses.Functions;
import com.incampusit.staryaar.SimpleClasses.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.squareup.picasso.Picasso;

/*
 * A simple {@link Fragment} subclass.
 */
public class Profile_Tab_F extends RootFragment implements View.OnClickListener {
    public static String pic_url;
    public TextView username, video_count_txt, user_bio, userhandle, usergender;
    public ImageView imageView;
    public CircleImageView imFbIcon, imInstaIcon, imYoutubeIcon, imTwitterIcon;
    public TextView follow_count_txt, fans_count_txt, heart_count_txt;
    public boolean isdataload = false;
    public LinearLayout create_popup_layout;
    protected TabLayout tabLayout;
    protected ViewPager pager;
    View view;
    Context context;
    ImageView setting_btn;
    Bundle bundle;
    RelativeLayout tabs_main_layout;
    LinearLayout top_layout;
    private ViewPagerAdapter adapter;

    public Profile_Tab_F() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        context = getContext();


        return init();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_image:
                OpenfullsizeImage(pic_url);
                break;

            case R.id.setting_btn:
                Open_Setting();
                break;

            case R.id.following_layout:
                Open_Following();
                break;

            case R.id.fans_layout:
                Open_Followers();
                break;

            case R.id.editprofile:
                Open_Edit_profile();
                break;

            case R.id.im_facebook:
                Functions.LaunchSocialMedia("facebook", getContext(), imFbIcon.getTag().toString());
                break;
            case R.id.im_insta:
                Functions.LaunchSocialMedia("instagram", getContext(), imInstaIcon.getTag().toString());
                break;
            case R.id.im_youtube:
                Functions.LaunchSocialMedia("youtube", getContext(), imYoutubeIcon.getTag().toString());
                break;
            case R.id.im_twitter:
                Functions.LaunchSocialMedia("twitter", getContext(), imTwitterIcon.getTag().toString());
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if ((view != null && isVisibleToUser) && !isdataload) {
            if (Variables.sharedPreferences.getBoolean(Variables.islogin, false))
                init();
        }
        if ((view != null && isVisibleToUser) && isdataload) {

            Call_Api_For_get_Allvideos();

        }


    }


    public View init() {

        username = view.findViewById(R.id.username);
        user_bio = view.findViewById(R.id.userbio);
        userhandle = view.findViewById(R.id.userhandle);
        usergender = view.findViewById(R.id.usergender);

        imageView = view.findViewById(R.id.user_image);
        imageView.setOnClickListener(this);

        // Social media links
        imFbIcon = view.findViewById(R.id.im_facebook);
        imFbIcon.setOnClickListener(this);

        imInstaIcon = view.findViewById(R.id.im_insta);
        imInstaIcon.setOnClickListener(this);

        imYoutubeIcon = view.findViewById(R.id.im_youtube);
        imYoutubeIcon.setOnClickListener(this);

        imTwitterIcon = view.findViewById(R.id.im_twitter);
        imTwitterIcon.setOnClickListener(this);
        // Social media links

        video_count_txt = view.findViewById(R.id.video_count_txt);

        follow_count_txt = view.findViewById(R.id.follow_count_txt);
        fans_count_txt = view.findViewById(R.id.fan_count_txt);
        heart_count_txt = view.findViewById(R.id.heart_count_txt);


        setting_btn = view.findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(this);


        tabLayout = view.findViewById(R.id.tabs);
        pager = view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(1);

        adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);

        setupTabIcons();


        tabs_main_layout = view.findViewById(R.id.tabs_main_layout);
        top_layout = view.findViewById(R.id.top_layout);


        ViewTreeObserver observer = top_layout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                final int height = top_layout.getMeasuredHeight();

                top_layout.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);

                ViewTreeObserver observer = tabs_main_layout.getViewTreeObserver();
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tabs_main_layout.getLayoutParams();
                        params.height = tabs_main_layout.getMeasuredHeight() + height;
                        tabs_main_layout.setLayoutParams(params);
                        tabs_main_layout.getViewTreeObserver().removeGlobalOnLayoutListener(
                                this);

                    }
                });

            }
        });


        create_popup_layout = view.findViewById(R.id.create_popup_layout);


        view.findViewById(R.id.following_layout).setOnClickListener(this);
        view.findViewById(R.id.fans_layout).setOnClickListener(this);
        view.findViewById(R.id.editprofile).setOnClickListener(this);

        isdataload = true;


        update_profile();

        Call_Api_For_get_Allvideos();

        return view;
    }


    public void update_profile() {
        username.setText(Variables.sharedPreferences.getString(Variables.f_name, "") + " " + Variables.sharedPreferences.getString(Variables.l_name, ""));
        pic_url = Variables.sharedPreferences.getString(Variables.u_pic, "null");

        try {
            /* Replace picasso with Glide
            Picasso.with(context).load(pic_url)
                    .resize(200, 200)
                    .placeholder(R.drawable.profile_image_placeholder)
                    .centerCrop()
                    .into(imageView);
             */
            Glide.with(context).load(pic_url)
                    .placeholder(context.getResources().getDrawable(R.drawable.star_home))
                    .centerCrop()
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                    .into(imageView);

        } catch (Exception e) {

        }
        Call_Api_For_get_Allvideos();
    }


    private void setupTabIcons() {

        View view1 = LayoutInflater.from(context).inflate(R.layout.item_tabs_profile_menu, null);
        ImageView imageView1 = view1.findViewById(R.id.image);
        //imageView1.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_video_color));
        tabLayout.getTabAt(0).setCustomView(view1);

        /*
        View view2 = LayoutInflater.from(context).inflate(R.layout.item_tabs_profile_menu, null);
        ImageView imageView2 = view2.findViewById(R.id.image);
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked_video_gray));
        tabLayout.getTabAt(1).setCustomView(view2);
         */


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                ImageView image = v.findViewById(R.id.image);

                switch (tab.getPosition()) {
                    case 0:

                        if (UserVideo_F.myvideo_count > 0) {
                            create_popup_layout.setVisibility(View.GONE);
                        } else {
                            create_popup_layout.setVisibility(View.VISIBLE);
                            Animation aniRotate = AnimationUtils.loadAnimation(context, R.anim.up_and_down_animation);
                            create_popup_layout.startAnimation(aniRotate);
                        }

                        //image.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_video_color));
                        break;

                    case 1:
                        create_popup_layout.clearAnimation();
                        create_popup_layout.setVisibility(View.GONE);
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked_video_color));
                        break;
                }
                tab.setCustomView(v);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                ImageView image = v.findViewById(R.id.image);

                switch (tab.getPosition()) {
                    case 0:
                        //image.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_video_gray));
                        break;
                    case 1:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked_video_gray));
                        break;
                }

                tab.setCustomView(v);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


    }

    //this will get the all videos data of user and then parse the data
    private void Call_Api_For_get_Allvideos() {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("my_fb_id", Variables.sharedPreferences.getString(Variables.u_id, ""));
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id, ""));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.showMyAllVideos, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Parse_data(resp);
            }
        });

    }

    public void Parse_data(String responce) {

        try {
            JSONObject jsonObject = new JSONObject(responce);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                JSONArray msgArray = jsonObject.getJSONArray("msg");

                JSONObject data = msgArray.getJSONObject(0);
                JSONObject user_info = data.optJSONObject("user_info");

                String userPersonalInfoTag = "";
                HashMap<String, String> infoTag = new HashMap<>();
                if (!user_info.optString("email").equals("null") &&
                        !user_info.optString("email").isEmpty()) {
                    //userPersonalInfoTag +=user_info.optString("email").toString();
                    infoTag.put("email", user_info.optString("email").toString());
                }
                if (!user_info.optString("phone").equals("null") &&
                        !user_info.optString("phone").isEmpty()) {
                    //userPersonalInfoTag +=user_info.optString("phone").toString();
                    infoTag.put("phone", user_info.optString("phone").toString());
                }
                if (!user_info.optString("dob").equals("null") &&
                        !user_info.optString("dob").isEmpty()) {
                    //userPersonalInfoTag +=user_info.optString("dob").toString();
                    infoTag.put("dob", user_info.optString("dob").toString());
                }
                username.setTag(infoTag);

                if (!user_info.optString("facebook").equals("null") &&
                        !user_info.optString("facebook").isEmpty()) {
                    imFbIcon.setEnabled(true);
                    imFbIcon.setAlpha(1.0f);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        imFbIcon.setOutlineAmbientShadowColor(context.getResources().getColor(R.color.app_color_lightest));
                    }
                    imFbIcon.setTag(user_info.optString("facebook").toString());
                } else {
                    imFbIcon.setEnabled(false);
                    imFbIcon.setAlpha(0.3f);
                    imFbIcon.setTag("");
                }

                if (!user_info.optString("instagram").equals("null") &&
                        !user_info.optString("instagram").isEmpty()) {
                    imInstaIcon.setEnabled(true);
                    imInstaIcon.setAlpha(1.0f);
                    imInstaIcon.setTag(user_info.optString("instagram").toString());
                } else {
                    imInstaIcon.setEnabled(false);
                    imInstaIcon.setAlpha(0.3f);
                    imInstaIcon.setTag("");
                }

                if (!user_info.optString("youtube").equals("null") &&
                        !user_info.optString("youtube").isEmpty()) {
                    imYoutubeIcon.setEnabled(true);
                    imYoutubeIcon.setAlpha(1.0f);
                    imYoutubeIcon.setTag(user_info.optString("youtube").toString());
                } else {
                    imYoutubeIcon.setEnabled(false);
                    imYoutubeIcon.setAlpha(0.3f);
                    imYoutubeIcon.setTag("");
                }

                if (!user_info.optString("twitter").equals("null") &&
                        !user_info.optString("twitter").isEmpty()) {
                    imTwitterIcon.setEnabled(true);
                    imTwitterIcon.setAlpha(1.0f);
                    imTwitterIcon.setTag(user_info.optString("twitter").toString());
                } else {
                    imTwitterIcon.setEnabled(false);
                    imTwitterIcon.setAlpha(0.3f);
                    imTwitterIcon.setTag("");
                }

                username.setText(user_info.optString("first_name") + " " + user_info.optString("last_name"));

                if (user_info.optString("bio") != null &&
                        user_info.optString("bio") != "null" &&
                        !user_info.optString("bio").isEmpty()) {
                    user_bio.setVisibility(View.VISIBLE);
                    user_bio.setText(user_info.optString("bio"));
                }

                if (user_info.optString("gender") != null &&
                        user_info.optString("gender") != "null" &&
                        !user_info.optString("gender").isEmpty()) {
                    usergender.setVisibility(View.VISIBLE);
                    usergender.setText(user_info.optString("gender"));
                }

                if (user_info.optString("user_handle") != null &&
                        user_info.optString("user_handle") != "null" &&
                        !user_info.optString("user_handle").isEmpty()) {
                    userhandle.setVisibility(View.VISIBLE);
                    userhandle.setText("@" + user_info.optString("user_handle"));
                }

                Profile_F.pic_url = user_info.optString("profile_pic");
                /* Replace picasso with Glide
                Picasso.with(context)
                        .load(Profile_F.pic_url)
                        .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                        .resize(200, 200).centerCrop().into(imageView);
                 */

                Glide.with(context).load(Profile_F.pic_url)
                        .placeholder(context.getResources().getDrawable(R.drawable.star_home))
                        .centerCrop()
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                        .into(imageView);

                follow_count_txt.setText(data.optString("total_following"));
                fans_count_txt.setText(data.optString("total_fans"));
                heart_count_txt.setText(data.optString("total_heart"));


                JSONArray user_videos = data.getJSONArray("user_videos");
                if (!user_videos.toString().equals("[" + "0" + "]")) {
                    video_count_txt.setText(user_videos.length() + " Videos");
                    TextView text = (TextView) tabLayout.getTabAt(0).getCustomView().findViewById(R.id.tv_video_count);
                    text.setText(user_videos.length() + " Videos");
                    create_popup_layout.setVisibility(View.GONE);

                } else {

                    create_popup_layout.setVisibility(View.VISIBLE);
                    Animation aniRotate = AnimationUtils.loadAnimation(context, R.anim.up_and_down_animation);
                    create_popup_layout.startAnimation(aniRotate);

                }


            } else {
                Toast.makeText(context, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void Open_Setting() {

        Open_menu_tab(setting_btn);


    }

    public void Open_Edit_profile() {
        Edit_Profile_F edit_profile_f = new Edit_Profile_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {

                update_profile();
            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        Bundle args = new Bundle();
        args.putString("gender", usergender.getText().toString());
        args.putString("userhandle", userhandle.getText().toString());
        args.putString("bio", user_bio.getText().toString());

        // Social media info
        args.putString("facebook", imFbIcon.getTag().toString());
        args.putString("youtube", imYoutubeIcon.getTag().toString());
        args.putString("twitter", imTwitterIcon.getTag().toString());
        args.putString("instagram", imInstaIcon.getTag().toString());
        // Social media info

        // Personal details
        HashMap<String, String> personalDetails = (HashMap<String, String>) username.getTag();
        args.putString("email", personalDetails.get("email"));
        args.putString("phone", personalDetails.get("phone"));
        args.putString("dob", personalDetails.get("dob"));
        // Personal details

        edit_profile_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, edit_profile_f).commit();
    }

    //this method will get the big size of profile image.
    public void OpenfullsizeImage(String url) {
        See_Full_Image_F see_image_f = new See_Full_Image_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        Bundle args = new Bundle();
        args.putSerializable("image_url", url);
        see_image_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, see_image_f).commit();
    }

    public void Open_menu_tab(View anchor_view) {
        Context wrapper = new ContextThemeWrapper(context, R.style.AlertDialogCustom);
        PopupMenu popup = new PopupMenu(wrapper, anchor_view);
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popup.setGravity(Gravity.TOP | Gravity.RIGHT);
        }
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.edit_Profile_id:
                        Open_Edit_profile();
                        break;

                    case R.id.logout_id:
                        Logout();
                        break;

                }
                return true;
            }
        });

    }

    public void Open_Following() {

        Following_F following_f = new Following_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {

                Call_Api_For_get_Allvideos();

            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("id", Variables.sharedPreferences.getString(Variables.u_id, ""));
        args.putString("from_where", "following");
        following_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, following_f).commit();

    }

    public void Open_Followers() {
        Following_F following_f = new Following_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {
                Call_Api_For_get_Allvideos();
            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("id", Variables.sharedPreferences.getString(Variables.u_id, ""));
        args.putString("from_where", "fan");
        following_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, following_f).commit();

    }

    // this will erase all the user info store in locally and logout the user
    public void Logout() {
        SharedPreferences.Editor editor = Variables.sharedPreferences.edit();
        editor.putString(Variables.u_id, "").clear();
        editor.putString(Variables.u_name, "").clear();
        editor.putString(Variables.u_pic, "").clear();
        editor.putBoolean(Variables.islogin, false).clear();
        editor.commit();
        Variables.sharedPreferences.edit().clear().commit();
        getActivity().finish();
        startActivity(new Intent(getActivity(), MainMenuActivity.class));
        LoginManager.getInstance().logOut();
        if (GoogleSignIn.getLastSignedInAccount(context) != null) {
            if (Login_A.mGoogleSignInClient != null) {
                Login_A.mGoogleSignInClient.signOut();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Functions.deleteCache(context);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final Resources resources;

        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();


        public ViewPagerAdapter(final Resources resources, FragmentManager fm) {
            super(fm);
            this.resources = resources;
        }

        @Override
        public Fragment getItem(int position) {
            final Fragment result;
            switch (position) {
                case 0:
                    result = new UserVideo_F(Variables.sharedPreferences.getString(Variables.u_id, ""));
                    break;
                case 1:
                    result = new Liked_Video_F(Variables.sharedPreferences.getString(Variables.u_id, ""));
                    break;

                default:
                    result = null;
                    break;
            }

            return result;
        }

        @Override
        public int getCount() {
            return 1;
        }


        @Override
        public CharSequence getPageTitle(final int position) {
            return null;
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
