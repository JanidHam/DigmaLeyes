package com.digma.digmaleyes.models.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.digma.digmaleyes.DownLoadManagerHelper;
import com.digma.digmaleyes.R;
import com.digma.digmaleyes.models.Ley;

import java.util.List;

/**
 * Created by janidham on 03/12/15.
 */
public class LeyAdapter extends RecyclerView.Adapter<LeyAdapter.LeyViewHolder> {
    private List<Ley> items;
    private DownLoadManagerHelper downLoadHelper;

    public static class LeyViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombre;
        public ImageView send;
        public ImageView download;

        public LeyViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.nombre);
            download = (ImageView) v.findViewById(R.id.btn_save);
            send = (ImageView) v.findViewById(R.id.send_email);

            /*send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAdapterPosition();
                    Log.e("i", "click in send");
                }
            });*/

        }
    }

    public LeyAdapter(List<Ley> items, Activity context) {
        this.items = items;
        downLoadHelper = new DownLoadManagerHelper(context);
    }


    @Override
    public LeyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_ley, parent, false);
        return new LeyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LeyViewHolder holder, final int i) {
        holder.nombre.setText(items.get(i).getName());
        final String url = "http://congresocam.com/docs/14leginf.pdf";

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadHelper.startDownload(url, "test.pdf");
                Log.e("i", "click on download in position " + items.get(i).getUrl());
            }
        });
        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //downLoadHelper.startDownload(url, items.get(i).getName() + ".pdf");
                Log.e("i", "click in send in position " + items.get(i).getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
}
