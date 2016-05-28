package com.example.woqja.young;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by KimJS on 2016-05-27.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<MyData> mItems;


    /*
    * 여기에 처음설정
    * */
    class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final RelativeLayout mLayout;
        public final TextView mItemTextView;
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLayout = (RelativeLayout) view.findViewById(R.id.itemLayout);
            mItemTextView = (TextView) view.findViewById(R.id.itemTextView);
            mImageView = (ImageView) view.findViewById(R.id.itemImageView);
        }
    }


    RecyclerViewAdapter(Context context, List<MyData> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        this.mBackground = mTypedValue.resourceId;
        this.mItems = items;
    }

    /*
    * 너가 만든 각 리스트에 해당하는 뷰 정의
    * */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    /*
    * 여기에 각 뷰마다 해당하는 작업하면됨
    * 뷰에 아이템 뿌려준다던지 클릭했을떄 뭐할지 등등등
    * */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String item = mItems.get(position).text;
        holder.mItemTextView.setText(item);
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), item, Toast.LENGTH_SHORT).show();
            }
        });
        holder.mImageView.setImageResource(mItems.get(position).img );

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
