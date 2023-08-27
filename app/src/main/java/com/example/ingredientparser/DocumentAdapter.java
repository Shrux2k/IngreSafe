package com.example.ingredientparser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private List<Document> documentList;


    public DocumentAdapter(List<Document> documentList) {
        this.documentList = documentList;
    }



    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        Document document = documentList.get(position);
        holder.documentNameTextView.setText(document.getName());
        holder.checkBox.setChecked(document.isSelected());
        holder.documentNameTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.white));


        holder.checkBox.setOnClickListener(view -> {
            document.setSelected(holder.checkBox.isChecked());
            // Notify any listener or method that the data has changed.
            notifyDataSetChanged(); // Or use notifyItemChanged(position) for better performance.
        });
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    public void setDocumentList(List<Document> newData) {
        this.documentList = newData;
        notifyDataSetChanged();
    }

    public static class DocumentViewHolder extends RecyclerView.ViewHolder {
        public TextView documentNameTextView;
        public CheckBox checkBox;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            documentNameTextView = itemView.findViewById(R.id.documentNameTextView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
