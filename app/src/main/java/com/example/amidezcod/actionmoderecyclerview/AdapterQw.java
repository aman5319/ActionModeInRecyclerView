package com.example.amidezcod.actionmoderecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by amidezcod on 24/7/17.
 */

public class AdapterQw extends RecyclerView.Adapter<AdapterQw.MyViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Pojo> pojoArrayList;
    private ListenerForAdapter listenerForAdapter;
    private SparseBooleanArray selectedItems;
    private ArrayList<Pojo> mFilteredList;

    public AdapterQw(Context context, ArrayList<Pojo> pojoArrayList, ListenerForAdapter listenerForAdapter) {
        this.context = context;
        this.pojoArrayList = pojoArrayList;
        this.listenerForAdapter = listenerForAdapter;
        selectedItems = new SparseBooleanArray();
        mFilteredList = pojoArrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_main, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Pojo pojo = mFilteredList.get(position);
        holder.imageView.setImageResource(pojo.getId());
        holder.textView.setText(pojo.getText());
        holder.itemView.setActivated(selectedItems.get(position, false));
        applyClickEvents(holder, position);
        if (selectedItems.get(position, false)) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

    }


    private void applyClickEvents(MyViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerForAdapter.onIconClicked(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listenerForAdapter.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public void toggleSelection(int pos) {

        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);

        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public ArrayList<Integer> getSelectedItems() {
        ArrayList<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        pojoArrayList.remove(position);
    }


    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = pojoArrayList;
                } else {

                    ArrayList<Pojo> filteredList = new ArrayList<>();

                    for (Pojo pojo : pojoArrayList) {

                        if (pojo.getText().toLowerCase().contains(charString)) {

                            filteredList.add(pojo);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Pojo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface ListenerForAdapter {
        void onIconClicked(int position);

        void onRowLongClicked(int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
        TextView textView;
        ImageView imageView;
        CheckBox checkBox;

        MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
            checkBox = itemView.findViewById(R.id.checkbox1);
            itemView.setOnLongClickListener(this);
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listenerForAdapter.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }


        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            checkBox.setChecked(true);
            toggleSelection(getAdapterPosition());
        }

    }
}
