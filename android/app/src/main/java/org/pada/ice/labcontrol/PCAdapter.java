package org.pada.ice.labcontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PCAdapter extends RecyclerView.Adapter<PCAdapter.PCViewHolder> {
    private List<PCInfo> pcList;
    public PCAdapter(List<PCInfo> pcList){
        this.pcList = pcList;
    }

    @Override
    public PCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pc_info, parent, false);
        return new PCViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PCViewHolder holder, int position){
        PCInfo pc = pcList.get(position);
        holder.textViewName.setText("Name: " + pc.name);
        holder.textViewIP.setText("IP: " + pc.ip);
        holder.textViewMAC.setText("MAC: " + pc.mac);
        holder.textViewOP.setText("Operating System: " + pc.operatingSystem);
        if (pc.status) {
            holder.textViewStatus.setText("Online");
            holder.textViewStatus.setTextColor(0xFF008000);
        } else {
            holder.textViewStatus.setText("Offline");
            holder.textViewStatus.setTextColor(0xFFC70000);
        }

        //Show AlertDialog with options on click
        holder.itemView.setOnClickListener(clicked -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(clicked.getContext());
            builder.setTitle("Options for " + pc.name);
            String[] options = {"Restart", "Shutdown", "Restore", "WOL"};
            builder.setItems(options, (dialog, which) -> {
                String optionClicked = options[which];
                Toast.makeText(clicked.getContext(), optionClicked + " selected for PC: " + pc.name, Toast.LENGTH_SHORT).show();
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.setCancelable(true);

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    @Override
    public int getItemCount(){
        return pcList.size();
    }

    public static class PCViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        TextView textViewIP;
        TextView textViewMAC;
        TextView textViewOP;
        TextView textViewStatus;

        public PCViewHolder(View view){
            super(view);

            textViewName = view.findViewById(R.id.pcName);
            textViewIP = view.findViewById(R.id.pcIP);
            textViewMAC = view.findViewById(R.id.pcMAC);
            textViewOP = view.findViewById(R.id.pcOperatingSystem);
            textViewStatus = view.findViewById(R.id.pcStatus);
        }
    }
}