package com.eqsis.notifications.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;


import com.eqsis.notifications.R;

import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {

    private List<StockItems> listItems;
    public ArrayList<StockItems>checkedStocks=new ArrayList<>();
    private Context context;


    public void filterList(ArrayList<StockItems> filteredList) {
        listItems = filteredList;
        notifyDataSetChanged();
    }
    public StockAdapter(List<StockItems> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }


    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stockrecyclerview_list_item,parent,false);
        return new StockAdapter.ViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(final StockAdapter.ViewHolder holder, int position) {
        StockItems listItem=listItems.get(position);
        holder.chkStock.setText((CharSequence) listItem.getStock().toString());
        holder.chkStock.setId(Integer.valueOf(listItem.getId().toString()));
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                CheckBox cb=(CheckBox )v;
//                if (cb.getText().equals(holder.chkStock.getText()))
                if(cb.isChecked())
                {
                    checkedStocks.add(listItems.get(pos));
                }else if(!cb.isChecked())
                {
                    checkedStocks.remove(listItems.get(pos));
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return listItems.size();
    }

    public StockItems getItem(int position) {
        return listItems.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CheckBox chkStock;
        public Context mContext;
        ItemClickListener itemClickListener;


        public ViewHolder(View itemView,Context context) {
            super(itemView);
            chkStock=itemView.findViewById(R.id.checkbox_RlistItem);
            this.mContext=context;


            chkStock.setOnClickListener(this);

        }
        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener=ic;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }
    }
}
