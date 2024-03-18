package com.shuvo.rft.superrent.dashboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.rft.superrent.R;
import com.shuvo.rft.superrent.dashboard.arraylists.MonthlyDueList;

import java.util.ArrayList;

public class MonthlyDueAdapter extends RecyclerView.Adapter<MonthlyDueAdapter.MDHodler> {

    private final ArrayList<MonthlyDueList> mCategoryItem;
    private final Context myContext;

    public MonthlyDueAdapter(ArrayList<MonthlyDueList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public MDHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.monthly_due_list_details_view, parent, false);
        return new MDHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MDHodler holder, int position) {
        MonthlyDueList monthlyDueList = mCategoryItem.get(position);
        holder.renterName.setText(monthlyDueList.getRenter_name());
        holder.flatNo.setText(monthlyDueList.getFlat_no().isEmpty() ? "No Flat Found" : monthlyDueList.getFlat_no());
        holder.dueAmount.setText(monthlyDueList.getDue_amount());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class MDHodler extends RecyclerView.ViewHolder {

        public TextView renterName;
        public TextView flatNo;
        public TextView dueAmount;

        public MDHodler(@NonNull View itemView) {
            super(itemView);

            renterName = itemView.findViewById(R.id.renter_name_month_due_dashboard);
            flatNo = itemView.findViewById(R.id.flat_no_for_month_due_dashboard);
            dueAmount = itemView.findViewById(R.id.amount_month_due_dashboard);
        }
    }
}
