package com.shuvo.rft.superrent.dashboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.shuvo.rft.superrent.R;
import com.shuvo.rft.superrent.dashboard.arraylists.FiveCollectLists;

import java.util.ArrayList;

public class FiveCollectionAdapters extends RecyclerView.Adapter<FiveCollectionAdapters.FCHOlder> {

    private final ArrayList<FiveCollectLists> mCategoryItem;
    private final Context myContext;

    public FiveCollectionAdapters(ArrayList<FiveCollectLists> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public FCHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.five_collection_list_details_view, parent, false);
        return new FCHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FCHOlder holder, int position) {
        FiveCollectLists fiveCollectLists = mCategoryItem.get(position);
        holder.renterName.setText(fiveCollectLists.getDue_renter_name());
        holder.collDate.setText(fiveCollectLists.getRhmbd_bill_collect_date().isEmpty() ? "No Date Found" : fiveCollectLists.getRhmbd_bill_collect_date());
        holder.rentAmonut.setText(fiveCollectLists.getRhmbd_collected_amt());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class FCHOlder extends RecyclerView.ViewHolder {

        public TextView renterName;
        public TextView collDate;
        public TextView rentAmonut;

        public FCHOlder(@NonNull View itemView) {
            super(itemView);

            renterName = itemView.findViewById(R.id.renter_name_five_coll_dashboard);
            collDate = itemView.findViewById(R.id.collection_date_five_coll_dashboard);
            rentAmonut = itemView.findViewById(R.id.amount_five_coll_dashboard);
        }
    }
}
