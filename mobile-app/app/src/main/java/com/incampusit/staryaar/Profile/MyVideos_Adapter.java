package com.incampusit.staryaar.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.incampusit.staryaar.Home.Home_Get_Set;
import com.incampusit.staryaar.R;

import java.util.ArrayList;

/*
 * Created by PANKAJ on 3/20/2018.
 */

public class MyVideos_Adapter extends RecyclerView.Adapter<MyVideos_Adapter.CustomViewHolder> {

    public Context context;
    private MyVideos_Adapter.OnItemClickListener listener;
    private ArrayList<Home_Get_Set> dataList;


    public MyVideos_Adapter(Context context, ArrayList<Home_Get_Set> dataList, MyVideos_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public MyVideos_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_myvideo_layout, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        MyVideos_Adapter.CustomViewHolder viewHolder = new MyVideos_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onBindViewHolder(final MyVideos_Adapter.CustomViewHolder holder, final int i) {
        final Home_Get_Set item = dataList.get(i);
        holder.setIsRecyclable(false);


        try {
            // Removing the gif as requested by Rishu on 02/09/2020
            // replacing with auto thumbnail or replacable thumbnail
            /*
            Glide.with(context)
                    .asGif()
                    .load(item.gif)
                    .skipMemoryCache(true)
                    .thumbnail(new RequestBuilder[]{Glide
                            .with(context)
                            .load(item.thum)})
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                            .placeholder(context.getResources().getDrawable(R.drawable.image_placeholder)).centerCrop())

                    .into(holder.thumb_image);
             */
            Glide.with(context)
                    .load(item.thum)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                    .into(holder.thumb_image);

        } catch (Exception e) {

        }


        holder.view_txt.setText(item.views);

        holder.bind(i, item, listener);

    }


    public interface OnItemClickListener {
        void onItemClick(int postion, Home_Get_Set item, View view);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {


        ImageView thumb_image;

        TextView view_txt;

        public CustomViewHolder(View view) {
            super(view);

            thumb_image = view.findViewById(R.id.thumb_image);
            view_txt = view.findViewById(R.id.view_txt);

        }

        public void bind(final int position, final Home_Get_Set item, final MyVideos_Adapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position, item, v);
                }
            });

        }

    }

}