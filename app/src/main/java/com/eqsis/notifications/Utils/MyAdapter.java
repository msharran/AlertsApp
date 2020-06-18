package com.eqsis.notifications.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eqsis.notifications.R;


import java.util.List;

/**
 * Created by SHARRAN on 14/05/18.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    public MyAdapter(List<AlertItems> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    private List<AlertItems>  listItems;
    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            AlertItems listItem=listItems.get(position);
            holder.txtToken.setText((CharSequence) listItem.getToken());
            holder.txtTarget.setText(String.valueOf(listItem.getTarget()));
            holder.txtBuy.setText(String.valueOf(listItem.getBid()));
            holder.txtSell.setText(String.valueOf(listItem.getSl()));

            switch (listItem.getBuySell())
            {
                case "Bullish":
                    holder.txtbuySell.setText("BUY");
                    holder.txtbuySell.setBackgroundResource( R.drawable.left_cornor);
                    break;
                case "Bearish":
                    holder.txtbuySell.setText("SELL");
                    holder.txtbuySell.setBackgroundResource( R.drawable.left_cornor_red);
                    break;

            }

            switch (listItem.getType())
            {
                case "Intraday":

                    holder.txtType.setText(listItem.getType().toUpperCase());
                    holder.txtType.setBackgroundResource( R.drawable.right_cornor_orange);
                    break;
                case "optional":

                    holder.txtType.setText(listItem.getType().toUpperCase());
                    holder.txtType.setBackgroundResource( R.drawable.right_cornor_grey);
                    break;
                case "positional":

                    holder.txtType.setText(listItem.getType().toUpperCase());
                    holder.txtType.setBackgroundResource( R.drawable.right_cornor_purple);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtToken;
        public TextView txtTarget;
        public TextView txtBuy;
        public TextView txtSell;
        public TextView txtType;
        public TextView txtbuySell;



        public ViewHolder(View itemView) {
            super(itemView);
            txtToken=itemView.findViewById(R.id.tokenName);
            txtTarget=itemView.findViewById(R.id.target_txt);
            txtBuy=itemView.findViewById(R.id.buy_txt);
            txtSell=itemView.findViewById(R.id.sl_txt);
            txtType=itemView.findViewById(R.id.txt_statusType);
            txtbuySell=itemView.findViewById(R.id.txt_statusBuy);




        }
    }
}
