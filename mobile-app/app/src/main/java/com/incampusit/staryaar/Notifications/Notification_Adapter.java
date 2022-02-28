package com.incampusit.staryaar.Notifications;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.incampusit.staryaar.R;
import com.incampusit.staryaar.SimpleClasses.Functions;
import com.incampusit.staryaar.SimpleClasses.Variables;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/*
 * Created by PANKAJ on 3/20/2018.
 */

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.CustomViewHolder> {
    public Context context;
    public Notification_Adapter.OnItemClickListener listener;
    ArrayList<Notification_Get_Set> datalist;

    public Notification_Adapter(Context context, ArrayList<Notification_Get_Set> arrayList, Notification_Adapter.OnItemClickListener listener) {
        this.context = context;
        datalist = arrayList;
        this.listener = listener;
    }

    @Override
    public Notification_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification, viewGroup, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Notification_Adapter.CustomViewHolder viewHolder = new Notification_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(datalist.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    @Override
    public void onBindViewHolder(final Notification_Adapter.CustomViewHolder holder, final int i) {
        holder.setIsRecyclable(false);

        holder.bind(i, datalist.get(i), listener);

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int postion, Notification_Get_Set item);

        void onProfileClick(View view, int postion, Notification_Get_Set item);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageButton done;
        CircleImageView user_image;
        TextView message, username, action, last_updated;
        RelativeLayout mainlayout;
        LinearLayout actionlayout, acceptrejectlayout, otheractionslayout;

        public CustomViewHolder(View view) {
            super(view);
            //  image=view.findViewById(R.id.image);
            action = view.findViewById(R.id.action);
            message = view.findViewById(R.id.message);
            username = view.findViewById(R.id.username);
            user_image = view.findViewById(R.id.user_image);
            last_updated = view.findViewById(R.id.last_updated);
            //mainlayout = view.findViewById(R.id.mainlayout);
            mainlayout = view.findViewById(R.id.submainlayout);
            actionlayout = view.findViewById(R.id.action_layout);
            acceptrejectlayout = view.findViewById(R.id.accept_reject);
            otheractionslayout = view.findViewById(R.id.other_actions);
        }

        public void bind(final int pos, final Notification_Get_Set item, final Notification_Adapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, pos, item);
                }
            });

            user_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onProfileClick(v, pos, item);
                }
            });
            username.setText(item.getTitle());
            message.setText(item.getMessage());
            last_updated.setText(Functions.ChangeDate(item.getCreatedOn()));

            if (item.getActionType().equals("FOLLOW")) {
                // follow layout commented as we need to impletment the private and restricted account
                //otheractionslayout.setVisibility(View.GONE);
                //acceptrejectlayout.setVisibility(View.VISIBLE);
                acceptrejectlayout.setVisibility(View.GONE);
                otheractionslayout.setVisibility(View.VISIBLE);
                action.setText("Profile");
            } else if (item.getActionType().equals("COMMENT")) {
                acceptrejectlayout.setVisibility(View.GONE);
                otheractionslayout.setVisibility(View.VISIBLE);
                action.setText("Reply");
            } else if (item.getActionType().equals("LIKE")) {
                acceptrejectlayout.setVisibility(View.GONE);
                otheractionslayout.setVisibility(View.VISIBLE);
                action.setText("View");
            }
            if (!item.isRead) {
                Log.d(Variables.TAG, "notification unread " + item.id);
                //mainlayout.setBackgroundColor(itemView.getResources().getColor(R.color.app_color_lighter));
                mainlayout.setBackground(itemView.getContext().getDrawable(R.drawable.back_notification_unread));
                username.setTextColor(itemView.getResources().getColor(R.color.white));
                message.setTextColor(itemView.getResources().getColor(R.color.white));
                last_updated.setTextColor(itemView.getResources().getColor(R.color.white));
            } else {
                Log.d(Variables.TAG, "notification read " + item.id);
                //mainlayout.setBackgroundColor(itemView.getResources().getColor(R.color.white));
                mainlayout.setBackground(itemView.getContext().getDrawable(R.drawable.back_notification_items));
                username.setTextColor(itemView.getResources().getColor(R.color.app_color_dark));
                message.setTextColor(itemView.getResources().getColor(R.color.app_color_dark));
                last_updated.setTextColor(itemView.getResources().getColor(R.color.last_updated));
            }
            Glide.with(itemView).load(item.getIcon()).apply(new RequestOptions().placeholder(itemView.getResources().getDrawable(R.drawable.star_home))).into(user_image);
        }

    }

}