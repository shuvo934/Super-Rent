package com.shuvo.rft.superrent.house_bill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.rft.superrent.R;
import com.shuvo.rft.superrent.house_bill.house_bill_details.HouseBillDetailsListAdapter;

import java.util.ArrayList;

public class HouseBillListAdapter extends RecyclerView.Adapter<HouseBillListAdapter.HBHolder> {
    private final ArrayList<HouseBillList> mCategoryItem;
    private final Context myContext;

    public HouseBillListAdapter(ArrayList<HouseBillList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public HBHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.house_bill_list_details_view, parent, false);
        return new HBHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HBHolder holder, int position) {
        HouseBillList houseBillList = mCategoryItem.get(position);
        holder.propertyName.setText(houseBillList.getRpm_prop_name());
        holder.billMonth.setText(houseBillList.getMonth_name());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext, LinearLayoutManager.HORIZONTAL,false);
        HouseBillDetailsListAdapter houseBillDetailsListAdapter = new HouseBillDetailsListAdapter(houseBillList.getHouseBillDetailsLists(), myContext,
                houseBillList.getRpm_prop_name(),houseBillList.getMonth_name());
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(houseBillDetailsListAdapter);
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class HBHolder extends RecyclerView.ViewHolder {
        public TextView propertyName;
        public TextView billMonth;
        public RecyclerView recyclerView;
        public HBHolder(@NonNull View itemView) {
            super(itemView);
            propertyName = itemView.findViewById(R.id.property_name_house_bill);
            billMonth = itemView.findViewById(R.id.house_bill_month_name);
            recyclerView = itemView.findViewById(R.id.house_bill_details_list_view);
        }
    }
}
