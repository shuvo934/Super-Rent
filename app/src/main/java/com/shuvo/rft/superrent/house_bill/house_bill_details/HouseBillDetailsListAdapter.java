package com.shuvo.rft.superrent.house_bill.house_bill_details;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.divider.MaterialDivider;
import com.shuvo.rft.superrent.R;

import java.util.ArrayList;

public class HouseBillDetailsListAdapter extends RecyclerView.Adapter<HouseBillDetailsListAdapter.HBDLHolder> {
    private final ArrayList<HouseBillDetailsList> mCategoryItem;
    private final Context myContext;
    private final String houseName;
    private final String monthBill;

    public HouseBillDetailsListAdapter(ArrayList<HouseBillDetailsList> mCategoryItem, Context myContext, String houseName, String monthBill) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
        this.houseName = houseName;
        this.monthBill = monthBill;
    }

    @NonNull
    @Override
    public HBDLHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.house_bill_details_list_details_view, parent, false);
        return new HBDLHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HBDLHolder holder, int position) {
        HouseBillDetailsList houseBillDetailsList = mCategoryItem.get(position);
        holder.renterFlatText.setText(houseBillDetailsList.getRenter_info());
        String billText = "Total Bill: "+ houseBillDetailsList.getRhmbd_total_bill_amt();
        holder.totBillText.setText(billText);

        String coll_amnt = houseBillDetailsList.getRhmbd_collected_amt();
        if (coll_amnt.isEmpty()) {
            String collBillText = "Bill to Collect: "+houseBillDetailsList.getRhmbd_total_bill_amt();
            holder.collBillText.setText(collBillText);
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(myContext, R.color.back_color));
            holder.renterFlatText.setTextColor(ContextCompat.getColor(myContext, R.color.primary_variant_second));
            holder.totBillText.setTextColor(ContextCompat.getColor(myContext, R.color.primary_variant_second));
            holder.collBillText.setTextColor(ContextCompat.getColor(myContext, R.color.primary_variant_second));
        }
        else {
            int collectedAmnt = Integer.parseInt(coll_amnt);
            if (!houseBillDetailsList.getRhmbd_total_bill_amt().isEmpty()) {
                int billAmnt = Integer.parseInt(houseBillDetailsList.getRhmbd_total_bill_amt());
                if (collectedAmnt == billAmnt) {
                    String collBillText = "Bill Collected";
                    holder.collBillText.setText(collBillText);
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(myContext, R.color.lemon_green));
                    holder.renterFlatText.setTextColor(ContextCompat.getColor(myContext, R.color.white));
                    holder.totBillText.setTextColor(ContextCompat.getColor(myContext, R.color.white));
                    holder.collBillText.setTextColor(ContextCompat.getColor(myContext, R.color.white));
                }
                else if (collectedAmnt < billAmnt){
                    int billToCollect = billAmnt - collectedAmnt;
                    String collBillText = "Bill to Collect: "+billToCollect;
                    holder.collBillText.setText(collBillText);
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(myContext, R.color.back_color));
                    holder.renterFlatText.setTextColor(ContextCompat.getColor(myContext, R.color.primary_variant_second));
                    holder.totBillText.setTextColor(ContextCompat.getColor(myContext, R.color.primary_variant_second));
                    holder.collBillText.setTextColor(ContextCompat.getColor(myContext, R.color.primary_variant_second));
                }
                else {
                    String collBillText = "Overcharged";
                    holder.collBillText.setText(collBillText);
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(myContext, R.color.sour_lemon));
                    holder.renterFlatText.setTextColor(ContextCompat.getColor(myContext, R.color.primary_variant_second));
                    holder.totBillText.setTextColor(ContextCompat.getColor(myContext, R.color.primary_variant_second));
                    holder.collBillText.setTextColor(ContextCompat.getColor(myContext, R.color.primary_variant_second));
                }
            }
            else {
                String collBillText = "Bill to Collect: "+houseBillDetailsList.getRhmbd_total_bill_amt();
                holder.collBillText.setText(collBillText);
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(myContext, R.color.back_color));
                holder.renterFlatText.setTextColor(ContextCompat.getColor(myContext, R.color.primary_variant_second));
                holder.totBillText.setTextColor(ContextCompat.getColor(myContext, R.color.primary_variant_second));
                holder.collBillText.setTextColor(ContextCompat.getColor(myContext, R.color.primary_variant_second));
            }

        }
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class HBDLHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView renterFlatText;
        TextView totBillText;
        TextView collBillText;
        MaterialDivider divider;
        public HBDLHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.house_bill_details_card_layout);
            renterFlatText = itemView.findViewById(R.id.house_bill_details_flat_renter_name);
            totBillText = itemView.findViewById(R.id.house_bill_details_total_bill_amnt);
            collBillText = itemView.findViewById(R.id.house_bill_details_bill_collect_amnt);
            divider = itemView.findViewById(R.id.divider_betwn_hb_renter_bill);

            cardView.setOnClickListener(view -> {
                Intent intent = new Intent(myContext, HouseBillCollection.class);
                intent.putExtra("house_name", houseName);
                intent.putExtra("month_name_bill", monthBill);
                intent.putExtra("rhmbd_id", mCategoryItem.get(getAdapterPosition()).getRhmbd_id());
                intent.putExtra("rhmbd_rhmbm_id", mCategoryItem.get(getAdapterPosition()).getRhmbd_rhmbm_id());
                intent.putExtra("rhmbd_rri_id",mCategoryItem.get(getAdapterPosition()).getRhmbd_rri_id());
                intent.putExtra("rsmbd_rpd_id", mCategoryItem.get(getAdapterPosition()).getRsmbd_rpd_id());
                intent.putExtra("renter_info",mCategoryItem.get(getAdapterPosition()).getRenter_info());
                intent.putExtra("rhmbd_ryms_id",mCategoryItem.get(getAdapterPosition()).getRhmbd_ryms_id());
                intent.putExtra("rhmbd_monthly_bill_amt",mCategoryItem.get(getAdapterPosition()).getRhmbd_monthly_bill_amt());
                intent.putExtra("rhmbd_due_amt",mCategoryItem.get(getAdapterPosition()).getRhmbd_due_amt());
                intent.putExtra("rhmbd_monthly_extra_charge",mCategoryItem.get(getAdapterPosition()).getRhmbd_monthly_extra_charge());
                intent.putExtra("rhmbd_total_bill_amt",mCategoryItem.get(getAdapterPosition()).getRhmbd_total_bill_amt());
                intent.putExtra("rhmbd_collected_amt",mCategoryItem.get(getAdapterPosition()).getRhmbd_collected_amt());
                intent.putExtra("rhmbd_bill_date",mCategoryItem.get(getAdapterPosition()).getRhmbd_bill_date());
                intent.putExtra("rhmbd_bill_collect_date",mCategoryItem.get(getAdapterPosition()).getRhmbd_bill_collect_date());
                intent.putExtra("rhmbd_notes",mCategoryItem.get(getAdapterPosition()).getRhmbd_notes());
                myContext.startActivity(intent);
            });
        }
    }
}
