package com.shuvo.rft.superrent.property;

import static com.shuvo.rft.superrent.MainActivity.userInfoLists;
import static com.shuvo.rft.superrent.property.PropertyInfo.p_info_circularProgressIndicator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shuvo.rft.superrent.R;
import com.shuvo.rft.superrent.property.property_details.PropertyDetailsListAdapter;
import com.shuvo.rft.superrent.property.property_details.PropertyDetailsModify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListAdapter.PLHolder> {
    private final ArrayList<PropertyList> mCategoryItem;
    private final Context myContext;

    private Boolean conn = false;
    private Boolean connected = false;

    public PropertyListAdapter(ArrayList<PropertyList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public PLHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.property_list_details_view, parent, false);
        return new PLHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PLHolder holder, int position) {
        PropertyList propertyList = mCategoryItem.get(position);

        holder.propertyName.setText(propertyList.getRpm_prop_name());
        holder.propertyAddrs.setText(propertyList.getRpm_prop_address());
        holder.ownerName.setText(propertyList.getRpm_prop_owner());
        holder.ownerContact.setText(propertyList.getRpm_prop_owner_contact());
        if (propertyList.getRpm_prop_active_flag().equals("ACTIVE")) {
            holder.status.setTextColor(ContextCompat.getColor(myContext, R.color.teal_700));
        }
        else {
            holder.status.setTextColor(ContextCompat.getColor(myContext, R.color.disabled));
        }
        holder.status.setText(propertyList.getRpm_prop_active_flag());
        holder.notes.setText(propertyList.getRpm_notes());
        if (propertyList.getPropertyDetailsLists().size() == 0) {
            holder.delete.setVisibility(View.VISIBLE);
        }
        else {
            holder.delete.setVisibility(View.GONE);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext, LinearLayoutManager.HORIZONTAL,false);
        PropertyDetailsListAdapter propertyDetailsListAdapter = new PropertyDetailsListAdapter(propertyList.getPropertyDetailsLists(), myContext, propertyList.getRpm_prop_name(),propertyList.getRpm_id());
        holder.view.setHasFixedSize(true);
        holder.view.setLayoutManager(layoutManager);
        holder.view.setAdapter(propertyDetailsListAdapter);

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class PLHolder extends RecyclerView.ViewHolder {

        public TextView propertyName;
        public TextView propertyAddrs;
        public TextView ownerName;
        public TextView ownerContact;
        public TextView notes;
        public TextView status;

        CardView edit;
        MaterialButton delete;
        MaterialButton addPropertyDetails;
        RecyclerView view;

        public PLHolder(@NonNull View itemView) {
            super(itemView);

            propertyName = itemView.findViewById(R.id.property_name);
            propertyAddrs = itemView.findViewById(R.id.property_address_text);
            ownerName = itemView.findViewById(R.id.property_owner_name);
            ownerContact = itemView.findViewById(R.id.property_owner_contact);
            notes = itemView.findViewById(R.id.property_notes);
            status = itemView.findViewById(R.id.property_active_status);
            addPropertyDetails = itemView.findViewById(R.id.add_property_details);
            view = itemView.findViewById(R.id.property_details_list_view);
            edit = itemView.findViewById(R.id.edit_property_button);
            delete = itemView.findViewById(R.id.delete_property_button);

            addPropertyDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(myContext, PropertyDetailsModify.class);
                    intent.putExtra("RPM_ID", mCategoryItem.get(getAdapterPosition()).getRpm_id());
                    intent.putExtra("RPM_PROP_NAME", mCategoryItem.get(getAdapterPosition()).getRpm_prop_name());
                    myContext.startActivity(intent);
                }
            });

            edit.setOnClickListener(view -> {
                Intent intent = new Intent(myContext, ProperrtyModify.class);
                intent.putExtra("RPM_ID", mCategoryItem.get(getAdapterPosition()).getRpm_id());
                intent.putExtra("RPM_PROP_NAME", mCategoryItem.get(getAdapterPosition()).getRpm_prop_name());
                intent.putExtra("RPM_PROP_ADDRESS", mCategoryItem.get(getAdapterPosition()).getRpm_prop_address());
                intent.putExtra("RPM_PROP_OWNER",mCategoryItem.get(getAdapterPosition()).getRpm_prop_owner());
                intent.putExtra("RPM_PROP_OWNER_CONTACT", mCategoryItem.get(getAdapterPosition()).getRpm_prop_owner_contact());
                intent.putExtra("RPM_ACTIVE_FLAG",mCategoryItem.get(getAdapterPosition()).getRpm_prop_active_flag());
                intent.putExtra("RPM_NOTES", mCategoryItem.get(getAdapterPosition()).getRpm_notes());
                intent.putExtra("RMP_EDIT_FLAG",true);
                myContext.startActivity(intent);
            });

            delete.setOnClickListener(view -> {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(myContext);
                builder.setMessage("Do you want to delete this Property?")
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            String rpm_id = mCategoryItem.get(getAdapterPosition()).getRpm_id();
                            System.out.println(rpm_id);
                            deleteRenter(rpm_id,getAdapterPosition());
                        })
                        .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            });
        }
    }

    public void deleteRenter(String rpm_id, int index) {
        p_info_circularProgressIndicator.setVisibility(View.VISIBLE);
        PropertyInfo.layout.setVisibility(View.GONE);
        PropertyInfo.PrNewButtonHandler = 1;
        conn = false;
        connected = false;

        String user_code = userInfoLists.get(0).getUser_name();
        String deletePropertyUrl = "http://119.18.148.32:8080/super/superrent_app/masterdata/updateproperty/";

        RequestQueue requestQueue = Volley.newRequestQueue(myContext);

        StringRequest deleteReq = new StringRequest(Request.Method.POST, deletePropertyUrl, response -> {
            conn = true;
            System.out.println(response);
            if (response.equals("200\n")) {
                connected = true;
                updateLayout(index);
            }
            else if (response.equals("100\n")) {
                connected = false;
                updateLayout(index);
            }
            else {
                connected = false;
                updateLayout(index);
            }

        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout(index);
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_RPM_ID",rpm_id);
                headers.put("P_OPERATION_TYPE","3");
                headers.put("P_USER_NAME",user_code);
                return headers;
            }
        };

        requestQueue.add(deleteReq);
    }

    public void updateLayout(int pos) {
        p_info_circularProgressIndicator.setVisibility(View.GONE);
        PropertyInfo.layout.setVisibility(View.VISIBLE);
        PropertyInfo.PrNewButtonHandler = 0;
        if (conn) {
            if (connected) {
                Toast.makeText(myContext,"Property Deleted Successfully",Toast.LENGTH_SHORT).show();
                mCategoryItem.remove(pos);
                notifyItemRemoved(pos);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(myContext)
                        .setMessage("Failed to Delete Property. Please try Later.")
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
