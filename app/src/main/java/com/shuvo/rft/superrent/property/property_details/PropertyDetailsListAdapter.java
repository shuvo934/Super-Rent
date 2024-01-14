package com.shuvo.rft.superrent.property.property_details;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.divider.MaterialDivider;
import com.shuvo.rft.superrent.R;

import java.util.ArrayList;

public class PropertyDetailsListAdapter extends RecyclerView.Adapter<PropertyDetailsListAdapter.PDLHolder> {
    private final ArrayList<PropertyDetailsList> mCategoryItem;
    private final Context myContext;
    private final String propertyName;
    private final String rpm_id;

    public PropertyDetailsListAdapter(ArrayList<PropertyDetailsList> mCategoryItem, Context myContext, String propertyName, String rpm_id) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
        this.propertyName = propertyName;
        this.rpm_id = rpm_id;
    }

    @NonNull
    @Override
    public PDLHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.property_details_list_details_view, parent, false);
        return new PDLHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PDLHolder holder, int position) {
        PropertyDetailsList propertyDetailsList = mCategoryItem.get(position);

        String rpd_floor_no = propertyDetailsList.getRpd_floor_no();
        String rpd_block_no = propertyDetailsList.getRpd_block_no();
        String rpd_shop_no = propertyDetailsList.getRpd_shop_no();
        String renter = propertyDetailsList.getRri_name();

        if (rpd_floor_no.isEmpty() && rpd_block_no.isEmpty() && rpd_shop_no.isEmpty()) {
            String text = "No Name Found";
            holder.pDText.setText(text);
        }
        else if (!rpd_floor_no.isEmpty() && rpd_block_no.isEmpty() && rpd_shop_no.isEmpty()) {
            holder.pDText.setText(rpd_floor_no);
        }
        else if (rpd_floor_no.isEmpty() && !rpd_block_no.isEmpty() && rpd_shop_no.isEmpty()) {
            holder.pDText.setText(rpd_block_no);
        }
        else if (rpd_floor_no.isEmpty() && rpd_block_no.isEmpty() && !rpd_shop_no.isEmpty()) {
            holder.pDText.setText(rpd_shop_no);
        }
        else if (!rpd_floor_no.isEmpty() && !rpd_block_no.isEmpty() && rpd_shop_no.isEmpty()) {
            String text = rpd_floor_no + "-" + rpd_block_no;
            holder.pDText.setText(text);
        }
        else if (!rpd_floor_no.isEmpty() && rpd_block_no.isEmpty() && !rpd_shop_no.isEmpty()) {
            String text = rpd_floor_no + "-" + rpd_shop_no;
            holder.pDText.setText(text);
        }
        else if (rpd_floor_no.isEmpty() && !rpd_block_no.isEmpty() && !rpd_shop_no.isEmpty()) {
            String text = rpd_block_no + "-" + rpd_shop_no;
            holder.pDText.setText(text);
        }
        else if (!rpd_floor_no.isEmpty() && !rpd_block_no.isEmpty() && !rpd_shop_no.isEmpty()) {
            String text = rpd_floor_no + "-" + rpd_block_no + "-" + rpd_shop_no;
            holder.pDText.setText(text);
        }

        if (renter.isEmpty()) {
            holder.divider.setVisibility(View.GONE);
            holder.pDRText.setVisibility(View.GONE);
        }
        else {
            holder.pDRText.setVisibility(View.VISIBLE);
            holder.divider.setVisibility(View.VISIBLE);
            holder.pDRText.setText(renter);
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class PDLHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView pDText;
        TextView pDRText;
        MaterialDivider divider;

        public PDLHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.property_details_card_layout);
            pDText = itemView.findViewById(R.id.property_details_short_name);
            pDRText = itemView.findViewById(R.id.property_details_renter_name);
            divider = itemView.findViewById(R.id.divider_betwn_pr_renter);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(myContext, PropertyDetailsModify.class);
                    intent.putExtra("RPM_ID", rpm_id);
                    intent.putExtra("RPM_PROP_NAME", propertyName);
                    intent.putExtra("RPD_EDIT_FLAG",true);

                    intent.putExtra("RPD_ID", mCategoryItem.get(getAdapterPosition()).getRpd_id());
                    intent.putExtra("RPD_FLOOR_NO",mCategoryItem.get(getAdapterPosition()).getRpd_floor_no());
                    intent.putExtra("RPD_BLOCK_NO",mCategoryItem.get(getAdapterPosition()).getRpd_block_no());
                    intent.putExtra("RPD_FLAT_NO",mCategoryItem.get(getAdapterPosition()).getRpd_shop_no());
                    intent.putExtra("RPD_MEASURE",mCategoryItem.get(getAdapterPosition()).getRpd_measurement());
                    intent.putExtra("RPD_RENTER_ID",mCategoryItem.get(getAdapterPosition()).getRpd_current_renter_id());
                    intent.putExtra("RPD_RENTER_NAME",mCategoryItem.get(getAdapterPosition()).getRri_name());
                    intent.putExtra("RPD_START_DATE",mCategoryItem.get(getAdapterPosition()).getRpd_current_renter_start_date());
                    intent.putExtra("rpd_renter_rent_renew_type",mCategoryItem.get(getAdapterPosition()).getRpd_renter_rent_renew_type());
                    intent.putExtra("rpd_renter_rent_renew_after",mCategoryItem.get(getAdapterPosition()).getRpd_renter_rent_renew_after());
                    intent.putExtra("rpd_renter_type",mCategoryItem.get(getAdapterPosition()).getRpd_renter_type());
                    myContext.startActivity(intent);
                }
            });
        }
    }
}
