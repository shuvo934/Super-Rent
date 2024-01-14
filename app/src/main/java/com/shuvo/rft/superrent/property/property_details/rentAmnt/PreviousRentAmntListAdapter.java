package com.shuvo.rft.superrent.property.property_details.rentAmnt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.divider.MaterialDivider;
import com.shuvo.rft.superrent.R;

import java.util.ArrayList;

public class PreviousRentAmntListAdapter extends RecyclerView.Adapter<PreviousRentAmntListAdapter.PRALHolder> {
    private final ArrayList<RentAmountList> mCategoryItem;
    private final Context myContext;

    public PreviousRentAmntListAdapter(ArrayList<RentAmountList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public PRALHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.rent_amount_list_details_view, parent, false);
        return new PRALHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PRALHolder holder, int position) {
        RentAmountList rentAmountList = mCategoryItem.get(position);

        holder.rentAmnt.setText(rentAmountList.getPrh_current_amt());
        holder.applyDate.setText(rentAmountList.getPrh_apply_date());
        holder.status.setText(rentAmountList.getStatus());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class PRALHolder extends RecyclerView.ViewHolder {

        TextView rentAmnt;
        TextView applyDate;
        TextView status;
        MaterialDivider divider;

        public PRALHolder(@NonNull View itemView) {
            super(itemView);

            rentAmnt = itemView.findViewById(R.id.previous_rent_amount_in_add_rent);
            applyDate = itemView.findViewById(R.id.previous_apply_date_in_add_rent);
            status = itemView.findViewById(R.id.previous_status_in_add_rent);
            divider = itemView.findViewById(R.id.previous_rent_amount_divider);
        }
    }
}
