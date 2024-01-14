package com.shuvo.rft.superrent.renter;

import static com.shuvo.rft.superrent.MainActivity.userInfoLists;
import static com.shuvo.rft.superrent.renter.RenterInfo.circularProgressIndicator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shuvo.rft.superrent.R;

import java.util.ArrayList;

public class RenterListAdapter extends RecyclerView.Adapter<RenterListAdapter.RLHolder> {
    private final ArrayList<RenterList> mCategoryItem;
    private final Context myContext;

    private Boolean conn = false;
    private Boolean connected = false;

    public RenterListAdapter(ArrayList<RenterList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public RLHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.renter_list_details_view, parent, false);
        return new RLHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RLHolder holder, int position) {
        RenterList renterList = mCategoryItem.get(position);

        holder.renterName.setText(renterList.getRri_name());
        holder.flatName.setText(renterList.getRri_business_company_name());
        holder.nationalId.setText(renterList.getRri_national_id());
        holder.contact.setText(renterList.getRri_contact());
        holder.emailName.setText(renterList.getRri_email());
        String ss;
        if (renterList.getRri_active_flag().equals("1")) {
            holder.status.setTextColor(ContextCompat.getColor(myContext, R.color.teal_700));
            ss = "ACTIVE";
        }
        else {
            holder.status.setTextColor(ContextCompat.getColor(myContext, R.color.disabled));
            ss = "IN-ACTIVE";
        }
        holder.status.setText(ss);
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class RLHolder extends RecyclerView.ViewHolder {

        public TextView renterName;
        public TextView flatName;
        public TextView nationalId;
        public TextView contact;
        public TextView emailName;
        public TextView status;

        CardView edit;
        MaterialButton delete;

        public RLHolder(@NonNull View itemView) {
            super(itemView);

            renterName = itemView.findViewById(R.id.renter_name);
            flatName = itemView.findViewById(R.id.flat_no_company_name);
            nationalId = itemView.findViewById(R.id.national_id);
            contact = itemView.findViewById(R.id.contact_no);
            emailName = itemView.findViewById(R.id.email_no);
            status = itemView.findViewById(R.id.renter_status);

            edit = itemView.findViewById(R.id.edit_renter_button);
            delete = itemView.findViewById(R.id.delete_renter_button);

            delete.setOnClickListener(view -> {

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(myContext);
                builder.setMessage("Do you want to delete this Renter?")
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            String rri_id = mCategoryItem.get(getAdapterPosition()).getRri_id();
                            System.out.println(rri_id);
                            deleteRenter(rri_id,getAdapterPosition());
                        })
                        .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            });

            edit.setOnClickListener(view -> {
                Intent intent = new Intent(myContext, RenterModify.class);
                intent.putExtra("RENTER_ID",mCategoryItem.get(getAdapterPosition()).getRri_id());
                intent.putExtra("RENTER_NAME",mCategoryItem.get(getAdapterPosition()).getRri_name());
                intent.putExtra("RENTER_N_ID",mCategoryItem.get(getAdapterPosition()).getRri_national_id());
                intent.putExtra("RENTER_CONTACT",mCategoryItem.get(getAdapterPosition()).getRri_contact());
                intent.putExtra("RENTER_EMAIL",mCategoryItem.get(getAdapterPosition()).getRri_email());
                intent.putExtra("RENTER_FLAT_NAME",mCategoryItem.get(getAdapterPosition()).getRri_business_company_name());
                intent.putExtra("RENTER_ACTIVE_FLAG",mCategoryItem.get(getAdapterPosition()).getRri_active_flag());
                intent.putExtra("RENTER_EDIT_FLAG",true);
                myContext.startActivity(intent);
            });
        }
    }

    public void deleteRenter(String rri_id, int position) {
        circularProgressIndicator.setVisibility(View.VISIBLE);
        RenterInfo.layout.setVisibility(View.GONE);
        RenterInfo.floatingButtonHandler = 1;
        conn = false;
        connected = false;
        String user_code = userInfoLists.get(0).getUser_name();

        String deleterRenterUrl = "http://119.18.148.32:8080/super/superrent_app/masterdata/deleterenter/?P_RRI_ID="+rri_id+"&P_USER_NAME="+user_code+"";

        RequestQueue requestQueue = Volley.newRequestQueue(myContext);

        StringRequest deleteReq = new StringRequest(Request.Method.DELETE, deleterRenterUrl, response -> {
            conn = true;
            System.out.println(response);
            if (response.equals("200\n")) {
                connected = true;
                updateLayout(position);
            }
            else if (response.equals("100\n")) {
                connected = false;
                updateLayout(position);
            }
            else {
                connected = false;
                updateLayout(position);
            }

        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout(position);
        });

        requestQueue.add(deleteReq);
    }

    public void updateLayout(int position) {
        circularProgressIndicator.setVisibility(View.GONE);
        RenterInfo.layout.setVisibility(View.VISIBLE);
        RenterInfo.floatingButtonHandler = 0;
        if (conn) {
            if (connected) {
                Toast.makeText(myContext,"Renter Deleted Successfully",Toast.LENGTH_SHORT).show();
                mCategoryItem.remove(position);
                notifyItemRemoved(position);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(myContext)
                        .setMessage("Failed to Delete Renter. Please try Later.")
                        .setPositiveButton("OK", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(myContext)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("OK", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> dialog.dismiss());
        }

    }
}
