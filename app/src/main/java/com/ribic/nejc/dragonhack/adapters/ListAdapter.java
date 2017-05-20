package com.ribic.nejc.dragonhack.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ribic.nejc.dragonhack.R;
import com.ribic.nejc.dragonhack.objects.Item;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListAdapterViewHolder>{

    private ArrayList<Item> mItems;
    private final ListAdapter.MainAdapterOnClickHandler mClickHandler;

    @Override
    public ListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_element, parent, false);
        return new ListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListAdapterViewHolder holder, int position) {
        holder.mTextViewName.setText(mItems.get(position).name);
        holder.mTextViewPrice.setText(mItems.get(position).price);
        //TODO check if is hearted or not
//        holder.mImageViewHeart.set
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface MainAdapterOnClickHandler{
        void partyOnClick(int clickedItemIndex);
    }

    public ListAdapter(ArrayList<Item> mItems, MainAdapterOnClickHandler mClickHandler){
        this.mItems = mItems;
        this.mClickHandler = mClickHandler;
    }

    class ListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextViewName;
        private TextView mTextViewPrice;
        private ImageView mImageViewHeart;

        private ListAdapterViewHolder(View itemView) {
            super(itemView);
            mTextViewName = (TextView) itemView.findViewById(R.id.item_name);
            mTextViewPrice = (TextView) itemView.findViewById(R.id.item_price);
            mImageViewHeart = (ImageView) itemView.findViewById(R.id.item_heart);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mClickHandler.partyOnClick(position);
        }
    }
}
