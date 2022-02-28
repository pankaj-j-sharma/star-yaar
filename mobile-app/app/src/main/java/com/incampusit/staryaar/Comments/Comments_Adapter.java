package com.incampusit.staryaar.Comments;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.incampusit.staryaar.R;
import com.incampusit.staryaar.SimpleClasses.Functions;
import com.incampusit.staryaar.SimpleClasses.Variables;

import java.util.ArrayList;
import java.util.Optional;

//import com.squareup.picasso.Picasso;

/*
 * Created by PANKAJ on 3/20/2018.
 */

public class Comments_Adapter extends RecyclerView.Adapter<Comments_Adapter.CustomViewHolder> {

    public Context context;
    private Comments_Adapter.OnItemClickListener listener;
    private ArrayList<Comment_Get_Set> dataList;


    public Comments_Adapter(Context context, ArrayList<Comment_Get_Set> dataList, Comments_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(dataList.get(position).comment_id);

    }

    @Override
    public Comments_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment_layout, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Comments_Adapter.CustomViewHolder viewHolder = new Comments_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onBindViewHolder(final Comments_Adapter.CustomViewHolder holder, final int i) {
        final Comment_Get_Set item = dataList.get(i);

        Comments_Adapter.Comment_Reply_Adapter replyadapter = new Comments_Adapter.Comment_Reply_Adapter(context, i, item.arrayList);
        replyadapter.setHasStableIds(true);
        holder.rvcomment_replies.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.rvcomment_replies.setAdapter(replyadapter);

        try {
            /* Replace picasso with Glide
            Picasso.with(context).
                    load(item.profile_pic)
                    .resize(50, 50)
                    .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                    .into(holder.user_pic);
             */

            holder.username.setText(item.first_name + " " + item.last_name);
            holder.message.setText(Functions.getBoldUserHandle(item.comments));
            Glide.with(context)
                    .load(item.profile_pic)
                    .centerCrop()
                    .placeholder(context.getResources().getDrawable(R.drawable.star_home))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                    .into(holder.user_pic);

        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.bind(i, item, listener);
    }


    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(int positon, Comment_Get_Set item, View view);

        void onReplyItemClick(int parentposition, int positon, Comment_Reply_Get_Set item, View view);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView username, message;
        TextView created, like, reply;
        TextView viewmore;
        ImageView user_pic, imlike;
        RecyclerView rvcomment_replies;


        public CustomViewHolder(View view) {
            super(view);

            username = view.findViewById(R.id.username);
            user_pic = view.findViewById(R.id.user_pic);
            message = view.findViewById(R.id.message);
            rvcomment_replies = view.findViewById(R.id.rvcommentreply);

            created = view.findViewById(R.id.commenttime);
            like = view.findViewById(R.id.commentlike);
            imlike = view.findViewById(R.id.imageButton);
            reply = view.findViewById(R.id.commentreply);

            viewmore = view.findViewById(R.id.tvviewmore);
        }

        public void bind(final int postion, final Comment_Get_Set item, final OnItemClickListener listener) {

            created.setText(item.created.substring(0, 2));

            if (item.likesList.size() > 1) {
                like.setText(item.likes + " likes");
            } else if (item.likesList.size() == 1) {
                like.setText(item.likes + " like");
            } else {
                like.setText("like");
            }

            if (item.arrayList.size() > 1) {
                viewmore.setText("View " + item.replies + " replies");
                viewmore.setVisibility(View.VISIBLE);
            } else if (item.arrayList.size() == 1) {
                viewmore.setText("View " + item.replies + " reply");
                viewmore.setVisibility(View.VISIBLE);
            } else {
                viewmore.setVisibility(View.GONE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Optional<Comment_Like_Get_Set> userlikeditem = item.likesList.parallelStream()
                        .filter(like -> like.fb_id.equals(Variables.sharedPreferences.getString(Variables.u_id, ""))).findFirst();

                if (userlikeditem.isPresent()) {
                    imlike.setImageResource(R.drawable.ic_star_like_fill);
                } else {
                    imlike.setImageResource(R.drawable.ic_star_like);
                }
            }

            imlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.user_liked = !item.user_liked;
                    if (item.user_liked) {
                        item.likes = String.valueOf(Integer.parseInt(item.likes) + 1);
                        if (Integer.parseInt(item.likes) > 1) {
                            like.setText(item.likes + " likes");
                        } else if (Integer.parseInt(item.likes) == 1) {
                            like.setText(item.likes + " like");
                        } else {
                            like.setText("like");
                        }
                        imlike.setImageResource(R.drawable.ic_star_like_fill);
                    } else {
                        if (Integer.parseInt(item.likes) > 0) {
                            item.likes = String.valueOf(Integer.parseInt(item.likes) - 1);
                            if (Integer.parseInt(item.likes) > 1) {
                                like.setText(item.likes + " likes");
                            } else if (Integer.parseInt(item.likes) == 1) {
                                like.setText(item.likes + " like");
                            } else {
                                like.setText("like");
                            }
                        }
                        imlike.setImageResource(R.drawable.ic_star_like);
                    }
                    listener.onItemClick(postion, item, v);
                    //imlike.setImageResource(R.drawable.ic_star_like_fill);
                }
            });

            reply.setOnClickListener(new View.OnClickListener() {
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

            viewmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewmore.getText().toString().contains("View")) {
                        viewmore.setText(viewmore.getText().toString().replace("View", "Hide"));
                        rvcomment_replies.setVisibility(View.VISIBLE);
                        item.show_replies = true;
                    } else {
                        viewmore.setText(viewmore.getText().toString().replace("Hide", "View"));
                        rvcomment_replies.setVisibility(View.GONE);
                        item.show_replies = false;
                    }
                }
            });

            if (item.show_replies) {
                viewmore.performClick();
            }

        }

    }

    // Comment Replies Adapter
    class Comment_Reply_Adapter extends RecyclerView.Adapter<Comments_Adapter.Comment_Reply_Adapter.CustomViewHolder> {

        public Context context;
        public int parentposition;
        ArrayList<Comment_Reply_Get_Set> datalist;

        public Comment_Reply_Adapter(Context context, int parentposition, ArrayList<Comment_Reply_Get_Set> arrayList) {
            this.context = context;
            this.parentposition = parentposition;
            datalist = arrayList;
        }

        @Override
        public long getItemId(int position) {
            return Long.parseLong(datalist.get(position).comment_id);

        }

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment_reply_layout, viewGroup, false);
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            Comments_Adapter.Comment_Reply_Adapter.CustomViewHolder viewHolder = new Comments_Adapter.Comment_Reply_Adapter.CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            //holder.setIsRecyclable(false);
            final Comment_Reply_Get_Set item = datalist.get(position);
            try {
                holder.username.setText(item.first_name + " " + item.last_name);
                holder.message.setText(Functions.getBoldUserHandle(item.comments));
                Glide.with(context)
                        .load(item.profile_pic)
                        .centerCrop()
                        .placeholder(context.getResources().getDrawable(R.drawable.star_home))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                        .into(holder.user_pic);

            } catch (Exception e) {

            }

            holder.bind(parentposition, position, item, listener);
        }

        @Override
        public int getItemCount() {
            return datalist.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {

            TextView username, message;
            TextView created, like, reply;
            ImageView user_pic, imlike;

            public CustomViewHolder(@NonNull View view) {
                super(view);

                username = view.findViewById(R.id.username);
                user_pic = view.findViewById(R.id.user_pic);
                message = view.findViewById(R.id.message);
                created = view.findViewById(R.id.commenttime);
                like = view.findViewById(R.id.commentlike);
                imlike = view.findViewById(R.id.imageButton);
                reply = view.findViewById(R.id.commentreply);
            }

            public void bind(final int parentposition, final int postion, final Comment_Reply_Get_Set item, final Comments_Adapter.OnItemClickListener listener) {

                created.setText(item.created.substring(0, 2));
                if (item.likesList.size() > 1) {
                    like.setText(item.likes + " likes");
                } else if (item.likesList.size() == 1) {
                    like.setText(item.likes + " like");
                } else {
                    like.setText("like");
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Optional<Comment_Like_Get_Set> userlikeditem = item.likesList.parallelStream()
                            .filter(like -> like.fb_id.equals(Variables.sharedPreferences.getString(Variables.u_id, ""))).findFirst();

                    if (userlikeditem.isPresent()) {
                        imlike.setImageResource(R.drawable.ic_star_like_fill);
                    } else {
                        imlike.setImageResource(R.drawable.ic_star_like);
                    }
                }

                imlike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.user_liked = !item.user_liked;
                        if (item.user_liked) {
                            item.likes = String.valueOf(Integer.parseInt(item.likes) + 1);
                            if (Integer.parseInt(item.likes) > 1) {
                                like.setText(item.likes + " likes");
                            } else if (Integer.parseInt(item.likes) == 1) {
                                like.setText(item.likes + " like");
                            } else {
                                like.setText("like");
                            }
                            imlike.setImageResource(R.drawable.ic_star_like_fill);
                        } else {
                            if (Integer.parseInt(item.likes) > 0) {
                                item.likes = String.valueOf(Integer.parseInt(item.likes) - 1);
                                if (Integer.parseInt(item.likes) > 1) {
                                    like.setText(item.likes + " likes");
                                } else if (Integer.parseInt(item.likes) == 1) {
                                    like.setText(item.likes + " like");
                                } else {
                                    like.setText("like");
                                }
                            }
                            imlike.setImageResource(R.drawable.ic_star_like);
                        }
                        listener.onReplyItemClick(parentposition, postion, item, v);
                        //imlike.setImageResource(R.drawable.ic_star_like_fill);
                    }
                });

                reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onReplyItemClick(parentposition, postion, item, v);
                    }
                });

                user_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onReplyItemClick(parentposition, postion, item, v);
                    }
                });

            }
        }
    }


}