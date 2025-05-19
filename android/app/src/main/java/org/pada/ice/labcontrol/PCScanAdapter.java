package org.pada.ice.labcontrol;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PCScanAdapter extends RecyclerView.Adapter<PCScanAdapter.ScannedPCViewHolder> {
    private List<PCInfo> scannedPcList;
    public PCScanAdapter(List<PCInfo> scannedPcList){
        this.scannedPcList = scannedPcList;
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
    }

    @Override
    public int getItemCount(){
        return scannedPcList.size();
    }

    public static class ScannedPCViewHolder extends RecyclerView.ViewHolder{
        TextView textViewScannedIP;
        TextView getTextViewScannedMAC;

        public ScannedPCViewHolder(View view){
            super(view);
            textViewScannedIP = view.findViewById(R.id.scannedPcIP);
            getTextViewScannedMAC = view.findViewById(R.id.scannedPcMAC);
        }
    }
}