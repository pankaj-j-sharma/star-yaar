package com.incampusit.staryaar.Home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.incampusit.staryaar.R;
import com.incampusit.staryaar.SimpleClasses.Variables;

import java.util.ArrayList;

//import com.squareup.picasso.Picasso;

/*
 * Created by PANKAJ on 3/20/2018.
 */

public class Home_Adapter extends RecyclerView.Adapter<Home_Adapter.CustomViewHolder> {

    public Context context;
    private Home_Adapter.OnItemClickListener listener;
    private ArrayList<Home_Get_Set> dataList;


    public Home_Adapter(Context context, ArrayList<Home_Get_Set> dataList, Home_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public Home_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_layout, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        Home_Adapter.CustomViewHolder viewHolder = new Home_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onBindViewHolder(final Home_Adapter.CustomViewHolder holder, final int i) {
        final Home_Get_Set item = dataList.get(i);
        holder.setIsRecyclable(false);

        try {

            holder.bind(i, item, listener);

            holder.username.setText(item.first_name + " " + item.last_name);


            if ((item.sound_name == null || item.sound_name.equals("") || item.sound_name.equals("null"))) {
                holder.sound_name.setText("original sound - " + item.first_name + " " + item.last_name);
            } else {
                holder.sound_name.setText(item.sound_name);
            }
            holder.sound_name.setSelected(true);


            holder.desc_txt.setText(item.video_description);

            /* replacing Picasso with Glide
            Picasso.with(context).
                    load(item.profile_pic)
                    .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                    .resize(100, 100).into(holder.user_pic);
             */
            Glide.with(context).load(item.profile_pic)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).centerCrop()
                    .placeholder(context.getResources().getDrawable(R.drawable.star_home))
                    .into(holder.user_pic);

            if ((item.sound_name == null || item.sound_name.equals(""))
                    || item.sound_name.equals("null")) {

                item.sound_pic = item.profile_pic;

            } else if (item.sound_pic.equals(""))
                item.sound_pic = "Null";

            /* Replacing picasso with Glide
            Picasso.with(context).
                    load(item.sound_pic)
                    .placeholder(context.getResources().getDrawable(R.drawable.ic_round_music))
                    .resize(100, 100).into(holder.sound_image);
             */
            Glide.with(context).load(item.sound_pic)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).centerCrop()
                    .placeholder(context.getResources().getDrawable(R.drawable.star_home))
                    .into(holder.sound_image);


            if (item.liked.equals("1")) {
                holder.like_image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_fill));
            } else {
                holder.like_image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
            }


            holder.like_txt.setText(item.like_count);
            holder.comment_txt.setText(item.video_comment_count);
            holder.viewcount_txt.setText(item.views_count);
            Log.d(Variables.TAG, "views -> " + item.views_count);

            // load thumbnail for player

            Glide.with(context)
                    .load(item.thum)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                    .into(holder.playerthumbnail);

        } catch (Exception e) {

        }
    }


    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(int positon, Home_Get_Set item, View view);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        //  PlayerView playerview;
        TextView username, desc_txt, sound_name;
        ImageView user_pic, sound_image;

        LinearLayout like_layout, comment_layout, shared_layout, sound_image_layout, more_options_layout;
        ImageView like_image, comment_image;
        TextView like_txt, comment_txt, viewcount_txt;
        ImageView playerthumbnail;

        public CustomViewHolder(View view) {
            super(view);

            // playerview=view.findViewById(R.id.playerview);

            username = view.findViewById(R.id.username);
            user_pic = view.findViewById(R.id.user_pic);
            sound_name = view.findViewById(R.id.sound_name);
            sound_image = view.findViewById(R.id.sound_image);

            like_layout = view.findViewById(R.id.like_layout);
            like_image = view.findViewById(R.id.like_image);
            like_txt = view.findViewById(R.id.like_txt);

            viewcount_txt = view.findViewById(R.id.main_view_txt);
            desc_txt = view.findViewById(R.id.desc_txt);

            comment_layout = view.findViewById(R.id.comment_layout);
            comment_image = view.findViewById(R.id.comment_image);
            comment_txt = view.findViewById(R.id.comment_txt);

            playerthumbnail = view.findViewById(R.id.thumbnail);

            sound_image_layout = view.findViewById(R.id.sound_image_layout);
            shared_layout = view.findViewById(R.id.shared_layout);

            more_options_layout = view.findViewById(R.id.more_options_layout);
        }

        public void bind(final int postion, final Home_Get_Set item, final Home_Adapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion, item, v);
                }
            });


            user_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion, item, v);
                }
            });

            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion, item, v);
                }
            });


            like_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion, item, v);
                }
            });


            comment_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion, item, v);
                }
            });

            shared_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion, item, v);
                }
            });

            sound_image_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion, item, v);
                }
            });

            more_options_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion, item, v);
                }
            });

        }


    }


}