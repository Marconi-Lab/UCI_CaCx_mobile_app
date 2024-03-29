package com.ug.air.uci_cacx.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ug.air.uci_cacx.Models.Form;
import com.ug.air.uci_cacx.R;

import java.util.List;

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.FormViewHolder> {

    Context context;
    List<Form> formList;

    private OnItemClickListener mListener;

    public FormAdapter(Context context, List<Form> formList) {
        this.context = context;
        this.formList = formList;
    }

    public interface OnItemClickListener {
        void onShowClick(int position);
        void onUploadClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public FormAdapter.FormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.form_list, parent, false);
        return new FormViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FormAdapter.FormViewHolder holder, int position) {
        Form form = formList.get(position);
        holder.screening.setText(form.getScreening_id());
        holder.date.setText(form.getDate());

        boolean complete = form.isComplete();
        if (complete){
            holder.upload.setVisibility(View.VISIBLE);
        }
        else {
            holder.upload.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return formList.size();
    }

    public static class FormViewHolder extends RecyclerView.ViewHolder {

        TextView screening, date;
        ImageView upload;

        public FormViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            screening = itemView.findViewById(R.id.screen);
            date = itemView.findViewById(R.id.date);
            upload = itemView.findViewById(R.id.upload);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onShowClick(position);
                        }
                    }
                }
            });

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onUploadClick(position);
                        }
                    }
                }
            });
        }
    }
}
