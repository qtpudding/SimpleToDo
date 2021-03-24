package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//responsible for taking data at a particular position and putting it into the viewholder

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    public interface OnClickListener{
        void onItemClicked(int position);
    }

    public interface OnLongClickListener {
        void onItemLongCLicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    public ItemsAdapter(List<String> items,OnLongClickListener longClickListener,OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    //responsible for creating each view
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create new view and wrap it in view holder
        //use layout inflator to inflate a view

        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        // wrap it inside a view holder and return it
        return new ViewHolder(todoView);
    }

    @Override
    //responsible for taking data at a particular position and putting it into a viewholder
    //binding data to a particular view holder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //grab the item at the position
        String item = items.get(position);

        //bind the item into into the specified view holder
        holder.bind(item);
    }

    @Override
    //number of items available in the data (list)
    public int getItemCount() {
        return items.size();
    }

    //define the view holder
    //container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //update the view inside of the view holder with this data
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked((getAdapterPosition()));
                }
            });

            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //remove the item from the recycler view
                    // notify the listener which position was long pressed
                    longClickListener.onItemLongCLicked(getAdapterPosition());
                    return true; //
                }
            });
        }
    }
}
