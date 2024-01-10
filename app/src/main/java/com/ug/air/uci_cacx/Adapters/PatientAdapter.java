package com.ug.air.uci_cacx.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ug.air.uci_cacx.Models.Patient;
import com.ug.air.uci_cacx.R;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    Context context;
    List<Patient> patientList;

    private OnItemClickListener mListener;

    public PatientAdapter(Context context, List<Patient> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    public interface OnItemClickListener {
        void onScreenClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public PatientAdapter.PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient, parent, false);
        PatientViewHolder patientViewHolder = new PatientViewHolder(view, mListener);
        return patientViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAdapter.PatientViewHolder holder, int position) {
        Patient patient = patientList.get(position);

        String name = patient.getPatient_name();
        String age = patient.getAge() + " years";
        holder.name.setText(name + " (" + age + ")");
        holder.contact.setText("+" + patient.getContact());
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public class PatientViewHolder extends RecyclerView.ViewHolder {

        TextView name, contact;
        ImageView edit;

        public PatientViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            contact = itemView.findViewById(R.id.contact);
            edit = itemView.findViewById(R.id.edit);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onScreenClick(position);
                        }
                    }
                }
            });

        }

    }
}
