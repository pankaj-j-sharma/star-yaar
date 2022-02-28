package com.incampusit.staryaar.Following;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
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

public class Following_Adapter extends RecyclerView.Adapter<Following_Adapter.CustomViewHolder> implements Filterable {
    public Context context;
    public Following_Adapter.OnItemClickListener listener;
    String following_or_fans;
    ArrayList<Following_Get_Set> datalist;
    ArrayList<Following_Get_Set> datalist_filter;

    public Following_Adapter(Context context, String following_or_fans, ArrayList<Following_Get_Set> arrayList, Following_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.following_or_fans = following_or_fans;
        datalist = arrayList;
        datalist_filter = arrayList;
        this.listener = listener;
    }

    @Override
    public Following_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_following, viewGroup, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Following_Adapter.CustomViewHolder viewHolder = new Following_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return datalist_filter.size();
    }

    @Override
    public void onBindViewHolder(final Following_Adapter.CustomViewHolder holder, final int i) {
        holder.setIsRecyclable(false);

        Following_Get_Set item = datalist_filter.get(i);

        holder.user_name.setText(item.first_name + " " + item.last_name);
        /* Replace picasso with Glide
        Picasso.with(context)
                .load(item.profile_pic)
                .placeholder(R.drawable.profile_image_placeholder)
                .into(holder.user_image);
         */
        Glide.with(context)
                .load(item.profile_pic)
                .placeholder(context.getResources().getDrawable(R.drawable.star_home))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .into(holder.user_image);

        holder.user_id.setText(item.username);

        if (item.is_show_follow_unfollow_btn
                && !item.fb_id.equals(Variables.sharedPreferences.getString(Variables.u_id, ""))
                && Variables.sharedPreferences.getBoolean(Variables.islogin, false)) {

            holder.action_txt.setVisibility(View.VISIBLE);

            if (following_or_fans.equals("following")) {

                if (item.follow.equals("0")) {
                    holder.action_txt.setText("Follow");
                    holder.action_txt.setBackground(context.getDrawable(R.drawable.button_background));
                    //holder.action_txt.setBackgroundColor(ContextCompat.getColor(context, R.color.redcolor));
                    holder.action_txt.setTextColor(ContextCompat.getColor(context, R.color.white));
                } else {
                    holder.action_txt.setText("UnFollow");
                    holder.action_txt.setBackground(ContextCompat.getDrawable(context, R.drawable.d_gray_border));
                    holder.action_txt.setTextColor(ContextCompat.getColor(context, R.color.black));
                }


            } else {

                if (item.follow.equals("0")) {
                    holder.action_txt.setText("Follow");
                    holder.action_txt.setBackground(context.getDrawable(R.drawable.button_background));
                    //holder.action_txt.setBackgroundColor(ContextCompat.getColor(context, R.color.redcolor));
                    holder.action_txt.setTextColor(ContextCompat.getColor(context, R.color.white));
                } else {
                    holder.action_txt.setText("Friends");
                    holder.action_txt.setBackground(ContextCompat.getDrawable(context, R.drawable.d_gray_border));
                    holder.action_txt.setTextColor(ContextCompat.getColor(context, R.color.black));
                }
            }

        } else {
            holder.action_txt.setVisibility(View.GONE);
        }

        holder.bind(i, datalist_filter.get(i), listener);

    }

    // that function will filter the result
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    datalist_filter = datalist;
                } else {
                    ArrayList<Following_Get_Set> filteredList = new ArrayList<>();
                    for (Following_Get_Set row : datalist) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.first_name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        } else if (row.last_name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        } else if (row.username.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        } else if (row.user_handle.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    datalist_filter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = datalist_filter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                datalist_filter = (ArrayList<Following_Get_Set>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int postion, Following_Get_Set item);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView user_image;
        TextView user_name;
        TextView user_id;
        TextView action_txt;
        RelativeLayout mainlayout;

        public CustomViewHolder(View view) {
            super(view);

            mainlayout = view.findViewById(R.id.mainlayout);

            user_image = view.findViewById(R.id.user_image);
            user_name = view.findViewById(R.id.user_name);
            user_id = view.findViewById(R.id.user_id);

            action_txt = view.findViewById(R.id.action_txt);
        }

        public void bind(final int pos, final Following_Get_Set item, final Following_Adapter.OnItemClickListener listener) {


            mainlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, pos, item);
                }
            });

            action_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, pos, item);
                }
            });


        }


    }

}