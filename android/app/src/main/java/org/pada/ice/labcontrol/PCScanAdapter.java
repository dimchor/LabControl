package org.pada.ice.labcontrol;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PCScanAdapter extends RecyclerView.Adapter<PCScanAdapter.ScannedPCViewHolder> {
    private List<PCInfo> scannedPcList;
    public PCScanAdapter(List<PCInfo> scannedPcList){
        this.scannedPcList = scannedPcList;
    }

    public void selectAll(boolean select){
        for (PCInfo pc : scannedPcList) {
            pc.setSelected(select);
        }
        notifyDataSetChanged();
    }


    @Override
    public ScannedPCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scanned_pc_info, parent, false);
        return new ScannedPCViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScannedPCViewHolder holder, int position){
        PCInfo pc = scannedPcList.get(position);
        holder.textViewScannedIP.setText("IP: " + pc.ip);
        holder.getTextViewScannedMAC.setText("MAC: " + pc.mac);

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(pc.isSelected());
        holder.checkBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            pc.setSelected(isChecked);
        }));
    }

    @Override
    public int getItemCount(){
        return scannedPcList.size();
    }

    public static class ScannedPCViewHolder extends RecyclerView.ViewHolder{
        TextView textViewScannedIP;
        TextView getTextViewScannedMAC;
        CheckBox checkBox;

        public ScannedPCViewHolder(View view){
            super(view);
            textViewScannedIP = view.findViewById(R.id.scannedPcIP);
            getTextViewScannedMAC = view.findViewById(R.id.scannedPcMAC);
            checkBox = view.findViewById(R.id.checkBox_scanned_pc);
        }
    }

    public List<PCInfo> getSelectedPCs(){
        List<PCInfo> selected = new ArrayList<>();

        for (PCInfo pc : scannedPcList) {
            if (pc.isSelected()) {
                selected.add(pc);
            }
        }
        return selected;
    }
}