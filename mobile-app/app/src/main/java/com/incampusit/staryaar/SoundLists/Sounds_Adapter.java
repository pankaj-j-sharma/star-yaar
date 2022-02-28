package com.incampusit.staryaar.SoundLists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

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

public class Sounds_Adapter extends RecyclerView.Adapter<Sounds_Adapter.CustomViewHolder> {
    public Context context;
    public Sounds_Adapter.OnItemClickListener listener;
    ArrayList<Sound_catagory_Get_Set> datalist;

    public Sounds_Adapter(Context context, ArrayList<Sound_catagory_Get_Set> arrayList, Sounds_Adapter.OnItemClickListener listener) {
        this.context = context;
        datalist = arrayList;
        this.listener = listener;
    }

    @Override
    public Sounds_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category_sound_layout, viewGroup, false);
        Sounds_Adapter.CustomViewHolder viewHolder = new Sounds_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    @Override
    public void onBindViewHolder(final Sounds_Adapter.CustomViewHolder holder, final int i) {
        holder.setIsRecyclable(false);


        Sound_catagory_Get_Set item = datalist.get(i);

        holder.title.setText(item.catagory);


        Sound_Items_Adapter adapter = new Sound_Items_Adapter(context, item.sound_list, new Sound_Items_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Sounds_GetSet item) {

                listener.onItemClick(view, postion, item);
            }
        });

        GridLayoutManager gridLayoutManager;
        if (item.sound_list.size() == 1)
            gridLayoutManager = new GridLayoutManager(context, 1);

        else if (item.sound_list.size() == 2)
            gridLayoutManager = new GridLayoutManager(context, 2);

        else
            gridLayoutManager = new GridLayoutManager(context, item.sound_list.size());

        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(gridLayoutManager);
        holder.recyclerView.setAdapter(adapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.findSnapView(gridLayoutManager);
        snapHelper.attachToRecyclerView(holder.recyclerView);


    }


    public interface OnItemClickListener {
        void onItemClick(View view, int postion, Sounds_GetSet item);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        RecyclerView recyclerView;

        public CustomViewHolder(View view) {
            super(view);
            //  image=view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            recyclerView = view.findViewById(R.id.horizontal_recylerview);


        }


    }


}


class Sound_Items_Adapter extends RecyclerView.Adapter<Sound_Items_Adapter.CustomViewHolder> {
    public Context context;
    public Sound_Items_Adapter.OnItemClickListener listener;
    ArrayList<Sounds_GetSet> datalist;

    public Sound_Items_Adapter(Context context, ArrayList<Sounds_GetSet> arrayList, Sound_Items_Adapter.OnItemClickListener listener) {
        this.context = context;
        datalist = arrayList;
        this.listener = listener;
    }

    @Override
    public Sound_Items_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sound_layout, viewGroup, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(Variables.screen_width - 50, RecyclerView.LayoutParams.WRAP_CONTENT));
        Sound_Items_Adapter.CustomViewHolder viewHolder = new Sound_Items_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    @Override
    public void onBindViewHolder(final Sound_Items_Adapter.CustomViewHolder holder, final int i) {
        holder.setIsRecyclable(false);

        Sounds_GetSet item = datalist.get(i);
        try {

            holder.bind(i, datalist.get(i), listener);

            holder.sound_name.setText(item.sound_name);
            holder.description_txt.setText(item.description);


            if (item.thum.equals("")) {
                item.thum = "Null";
            }
            /* Replace picasso with Glide
            Picasso.with(context).load(item.thum)
                    .placeholder(context.getResources().getDrawable(R.drawable.image_placeholder))
                    .into(holder.sound_image);
             */
            Glide.with(context).load(item.thum)
                    .placeholder(context.getResources().getDrawable(R.drawable.star_home)).centerCrop()
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                    .into(holder.sound_image);


        } catch (Exception e) {

        }

    }


    public interface OnItemClickListener {
        void onItemClick(View view, int postion, Sounds_GetSet item);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageButton done, fav_btn;
        TextView sound_name, description_txt;
        ImageView sound_image;

        public CustomViewHolder(View view) {
            super(view);

            done = view.findViewById(R.id.done);
            fav_btn = view.findViewById(R.id.fav_btn);


            sound_name = view.findViewById(R.id.sound_name);
            description_txt = view.findViewById(R.id.description_txt);
            sound_image = view.findViewById(R.id.sound_image);

        }

        public void bind(final int pos, final Sounds_GetSet item, final Sound_Items_Adapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, pos, item);
                }
            });

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, pos, item);
                }
            });

            fav_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, pos, item);
                }
            });

        }


    }


}

