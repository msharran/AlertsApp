package com.eqsis.notifications.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.eqsis.notifications.API.PreferenceApiRequest;
import com.eqsis.notifications.Activity.PreferenceActivity;
import com.eqsis.notifications.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PreferenceAdapter extends RecyclerView.Adapter<PreferenceAdapter.ViewHolder> {

    private List<PreferenceItems> listItems;
    public static  HashMap<String,String> id_subscribtion_map;
    private Context context;
     private int currentImage;
     public static HashMap<String,Integer> positionMap;
    private int[] notifyImages={R.drawable.ic_notify_green,R.drawable.ic_notify_grey};
    private HashMap<Integer, Boolean> notifyImagesMapPut= new HashMap<Integer,Boolean>()
    {
        {
            put(R.drawable.ic_notify_green, true);
            put(R.drawable.ic_notify_grey, false);

        }

    };
    private HashMap<Boolean, Integer> notifyImagesMapGet= new HashMap<Boolean, Integer>()
    {
        {
            put( true,R.drawable.ic_notify_green);
            put( false,R.drawable.ic_notify_grey);

        }

    };


    public void filterList(ArrayList<PreferenceItems> filteredList) {
//        this.listItems=new ArrayList<>();
        for(int i=0,j=0;i<filteredList.size();i++)
        {
            if(filteredList.get(i).getType().equals("Brand"))
            {
                this.listItems.set(j,filteredList.get(i));
                j++;
            }

        }
        notifyDataSetChanged();
    }

    public PreferenceAdapter(List<PreferenceItems> listItems, Context context) {
        this.listItems=new ArrayList<>();
        positionMap=new HashMap<>();
        id_subscribtion_map=new HashMap<>();

        for(PreferenceItems listItem :listItems)
        {
            if(listItem.getType().equals("Brand"))
            {

                this.listItems.add(new PreferenceItems(listItem.getStock(),listItem.getId(),listItem.getSubscribtion(),listItem.isNotified(),listItem.getType()));

                positionMap.put(listItem.getStock(),(listItem.isNotified()?1:0));
            }

        }

        this.context = context;
        this.currentImage=0;
    }


    @Override
    public PreferenceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.preference_recyclerview_list_item, parent, false);
        return new PreferenceAdapter.ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(final PreferenceAdapter.ViewHolder holder, final int position) {
        try {


             PreferenceItems listItem = listItems.get(position);
            final String stock=listItem.getStock();
            if(listItem.isNotified())
            {
                holder.notifyStock.setImageResource(notifyImagesMapGet.get(listItem.isNotified()));
            }
            if(listItem.getSubscribtion().equals("subscribed"))
            {
                holder.chkStock.setChecked(true);
            }


            holder.chkStock.setText( listItem.getStock().toString());

            holder.notifyStock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    toggleImage(holder.notifyStock,stock);
                }
            });


            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {

                    CheckBox cb = (CheckBox) v;


                        if (cb.getText().equals(holder.chkStock.getText()))
                            if (cb.isChecked()) {

                               listItems.get(pos).setSubscribtion("subscribed");
                               id_subscribtion_map.put(listItems.get(pos).getId(),listItems.get(pos).getSubscribtion());

                            } else if (!cb.isChecked()) {
                               listItems.get(pos).setSubscribtion("unsubscribed");
                                id_subscribtion_map.put(listItems.get(pos).getId(),listItems.get(pos).getSubscribtion());
                            }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void toggleImage(ImageButton notify,String stock)
    {

//        Log.d("imgbtnID", String.valueOf(notify.getId())) ;
        if(positionMap.get(stock)==(notifyImages.length-1))
        {
            notify.setImageResource(notifyImages[positionMap.get(stock)]);
            positionMap.put(stock,0);
        }else{

            notify.setImageResource(notifyImages[positionMap.get(stock)]);
            positionMap.put(stock,1);
        }



    }
    @Override
    public int getItemCount() {

        return listItems.size();
    }

    public PreferenceItems getItem(int position) {
        return listItems.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CheckBox chkStock;
        public ImageButton notifyStock;
        public Context mContext;
        ItemClickListener itemClickListener;


        public ViewHolder(View itemView, Context context) {
            super(itemView);
            chkStock = itemView.findViewById(R.id.checkbox_RlistItem_preference);
            notifyStock=itemView.findViewById(R.id.Imagebtn_Rlistitem_preference);
            this.mContext = context;

            notifyStock.setOnClickListener(this);
            chkStock.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }
    }
}