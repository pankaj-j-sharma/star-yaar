package com.incampusit.staryaar.argear;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.incampusit.staryaar.R;

import java.util.ArrayList;
import java.util.List;


public class StickerCategoryListAdapter extends RecyclerView.Adapter<StickerCategoryListAdapter.ViewHolder> {

    private static final String TAG = StickerCategoryListAdapter.class.getSimpleName();
    int index = -1;
    private List<CategoryModel> mCategories = new ArrayList<>();
    private Listener mListener;

    public StickerCategoryListAdapter(Listener listener) {
        mListener = listener;
    }

    public void setData(List<CategoryModel> categories) {
        mCategories.clear();
        index = -1;
        for (CategoryModel model : categories) {
            if (!TextUtils.equals(model.title, "filters")) {
                mCategories.add(model);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bind(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_sticker, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public interface Listener {
        void onCategorySelected(CategoryModel category);
    }

    abstract class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View v) {
            super(v);
        }

        abstract void bind(int position);
    }

    public class CategoryViewHolder extends ViewHolder implements View.OnClickListener {
        Button mButtonCategory = null;
        CategoryModel mCategory;

        CategoryViewHolder(View v) {
            super(v);
            mButtonCategory = (Button) v.findViewById(R.id.category_button);
        }

        @Override
        void bind(int position) {
            mCategory = mCategories.get(position);

            Log.d(TAG, "category_sticker " + position + " " + mCategory);
            mButtonCategory.setText(mCategory.title);
            mButtonCategory.setOnClickListener(this);

            if (index == position) {
                mButtonCategory.setBackground(itemView.getContext().getDrawable(R.drawable.camera_filter_background_selected));
                mButtonCategory.setElevation(20);
                mButtonCategory.setTextColor(itemView.getResources().getColor(R.color.app_color_dark));
            } else {
                mButtonCategory.setBackground(itemView.getContext().getDrawable(R.drawable.camera_filter_background));
                mButtonCategory.setPadding(0, 0, 0, 0);
                mButtonCategory.setElevation(0);
                mButtonCategory.setTextColor(itemView.getResources().getColor(R.color.white));
            }
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                index = getAdapterPosition();
                notifyDataSetChanged();
                mListener.onCategorySelected(mCategory);
            }
        }
    }
}