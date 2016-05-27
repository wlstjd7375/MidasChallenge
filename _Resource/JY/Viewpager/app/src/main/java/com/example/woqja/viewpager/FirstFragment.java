package com.example.woqja.viewpager;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;


public class FirstFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recyclerview, container, false);

        setupRecyclerView(recyclerView);
        return recyclerView;
    }

    /*
    * 사이 간격 똑같이 해주기 위해서..
    * */
    public void setItemMargin(RecyclerView recyclerView, int marginDpId) {
        final int pixel = (int) getResources().getDimension(marginDpId);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                int position = parent.getChildAdapterPosition(view);

                StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                int spanIndex = lp.getSpanIndex();
                if (position >= 0) {
                    if (spanIndex == 1) {
                        outRect.right = pixel;
                        outRect.left = pixel / 2;
                    } else {
                        if (lp.isFullSpan()) {
                            outRect.right = pixel;
                            outRect.left = pixel;
                        } else {
                            outRect.left = pixel;
                            outRect.right = pixel / 2;
                        }
                    }
                    outRect.top = pixel;
                }
            }
        });
    }

    public List<Integer> getList() {
        return Arrays.asList(R.drawable.culture_sample1, R.drawable.culture_sample2,
                R.drawable.culture_sample3, R.drawable.culture_sample4,
                R.drawable.culture_sample1, R.drawable.culture_sample2,
                R.drawable.culture_sample3, R.drawable.culture_sample4);
    }

    public void setupRecyclerView(RecyclerView recyclerView) {
        StaggeredGridLayoutManager staggeredGridView = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridView);
        setItemMargin(recyclerView, R.dimen.image_margin);

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), getList());
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Integer> mValues;

        RecyclerViewAdapter(Context context, List<Integer> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_first_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            /*
            *  이걸로 Out Of Memory 해결
            * */
            Glide.with(holder.mView.getContext())
                    .load(mValues.get(position))
                    .centerCrop()
                    .into(holder.mImageView);

            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            holder.itemView.setLayoutParams(lp);

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            private final TextView mTitleTextView;
            private final TextView mSubTitleTextView;
            private final ImageView mImageView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTitleTextView = (TextView) mView.findViewById(R.id.titleTextView);
                mSubTitleTextView = (TextView) mView.findViewById(R.id.subtitleTextView);
                mImageView = (ImageView) mView.findViewById(R.id.cultureImageView);
                mTitleTextView.setTypeface(Typeface.MONOSPACE);
                mSubTitleTextView.setTypeface(Typeface.MONOSPACE);
            }

        }
    }
}
