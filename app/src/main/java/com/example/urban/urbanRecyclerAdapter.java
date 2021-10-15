package com.example.urban;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class urbanRecyclerAdapter extends RecyclerView.Adapter<urbanRecyclerAdapter.ViewHolder> {
    Context context;
    List<com.example.urban.urbanmodel> urbanmodels;
    List<com.example.urban.urbanmodel> urbanmodelList;
    DatabaseReference reference;
    DatabaseReference dreference;
    String phone;

    public urbanRecyclerAdapter(Context context, List<com.example.urban.urbanmodel> urbanmodels) {
        this.urbanmodels = urbanmodels;
        urbanmodelList = new ArrayList<>(urbanmodels);
        this.context =context;
        reference = FirebaseDatabase.getInstance().getReference().child("Form");
        dreference = FirebaseDatabase.getInstance().getReference().child("Deleted Form");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_urbanrecycler, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final com.example.urban.urbanmodel urbanmodel = urbanmodelList.get(position);

        viewHolder.name.setText(urbanmodel.getName());
        viewHolder.area.setText(urbanmodel.getArea());
        viewHolder.specialization.setText(urbanmodel.getSpecialization());
        viewHolder.count.setText(urbanmodel.getCount());

    }

    @Override
    public int getItemCount() {
        return urbanmodelList.size();
    }

    public Filter getFilter() {
        return UrbanFilter;
    }

    private Filter UrbanFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<com.example.urban.urbanmodel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(urbanmodelList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (com.example.urban.urbanmodel urbanmodel : urbanmodelList) {
                    if (urbanmodel.getName() != null) {
                        if (urbanmodel.getName().toLowerCase().contains(filterPattern) || urbanmodel.getSpecialization().toLowerCase().contains(filterPattern)) {
                            filteredList.add(urbanmodel);
                        }
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            urbanmodelList.clear();
            urbanmodelList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, area,specialization,count;
        Button  edit,delete,call;



        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            name = itemView.findViewById(R.id.name);
            area = itemView.findViewById(R.id.area);
            call = itemView.findViewById(R.id.call);
            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);
            specialization = itemView.findViewById(R.id.specialization);
            count=itemView.findViewById(R.id.count);
            call.setOnClickListener(this);
            edit.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == call) {
                int position = getAdapterPosition();
                final com.example.urban.urbanmodel urbanmodel = urbanmodelList.get(position);
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + urbanmodel.getPhoneNumber()));
                context.startActivity(callIntent);
                reference=FirebaseDatabase.getInstance().getReference("Form").child(urbanmodel.getPhoneNumber());
            }

            if (v == edit){
                int position = getAdapterPosition();
                final com.example.urban.urbanmodel urbanmodel = urbanmodels.get(position);
                Intent intent=new Intent(context,editform.class);
                intent.putExtra("phonenumber", urbanmodel.getPhoneNumber());
                context.startActivity(intent);
            }
            if (v==delete){
                AlertDialog.Builder ab = new AlertDialog.Builder(context);
                ab.setMessage("Are you sure to delete?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                int position = getAdapterPosition();
                final com.example.urban.urbanmodel urbanmodel = urbanmodels.get(position);
                phone=urbanmodel.getPhoneNumber();
            }
        }
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Do your Yes progress
                    if (phone!=null){
                        reference.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    dreference.child(phone).setValue(snapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            reference.child(phone).removeValue();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //Do your No progress
                    break;
            }
        }
    };
}



