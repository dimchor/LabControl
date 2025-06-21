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
    public void selectAll(boolean select){
        for (PCInfo pc : PCScanList.getInstance().getList()) {
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
        PCInfo pc = PCScanList.getInstance().get(position);
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
        return PCScanList.getInstance().size();
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

        for (PCInfo pc : PCScanList.getInstance().getList()) {
            if (pc.isSelected()) {
                selected.add(pc);
            }
        }
        return selected;
    }
}