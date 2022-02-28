package com.incampusit.staryaar.argear;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.incampusit.staryaar.R;

import java.util.ArrayList;
import java.util.List;


public class StickerListAdapter extends RecyclerView.Adapter<StickerListAdapter.ViewHolder> {

    private static final String TAG = StickerListAdapter.class.getSimpleName();
    int index = -1;
    private List<ItemModel> mItems = new ArrayList<>();
    private Listener mListener;
    private Context mContext;

    public StickerListAdapter(Context context, Listener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setData(List<ItemModel> items) {
        mItems.clear();
        index = -1;
        if (items != null) {
            mItems.addAll(items);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bind(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker, parent, false);
        return new StickerViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public interface Listener {
        void onStickerSelected(int position, ItemModel item);
    }

    abstract class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View v) {
            super(v);
        }

        abstract void bind(int position);
    }

    public class StickerViewHolder extends ViewHolder implements View.OnClickListener {
        ImageView mImageViewItemThumbnail = null;

        ItemModel mItem;
        int position;

        StickerViewHolder(View v) {
            super(v);
            mImageViewItemThumbnail = v.findViewById(R.id.item_thumbnail_imageview);
        }

        @Override
        void bind(int position) {
            mItem = mItems.get(position);
            this.position = position;
            Log.d(TAG, "item_sticker " + position + " " + mItem.thumbnailUrl + " " + mItem);
            mImageViewItemThumbnail.setOnClickListener(this);

            //스티커의 섬네일을 보여줍니다
            Glide.with(mContext)
                    .load(mItem.thumbnailUrl)
                    .fitCenter()
                    .into(mImageViewItemThumbnail);

            if (index == position) {
                mImageViewItemThumbnail.setBackground(itemView.getContext().getDrawable(R.drawable.camera_filter_background_selected));
                mImageViewItemThumbnail.setPadding(20, 20, 20, 20);
                mImageViewItemThumbnail.setElevation(20);
            } else {
                mImageViewItemThumbnail.setBackground(itemView.getContext().getDrawable(R.drawable.camera_filter_background));
                mImageViewItemThumbnail.setPadding(3, 3, 3, 3);
                mImageViewItemThumbnail.setElevation(0);
            }
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                index = getAdapterPosition();
                notifyDataSetChanged();
                mListener.onStickerSelected(position, mItem);
            }
        }
    }
}