package com.digma.digmaleyes.models.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digma.digmaleyes.DownLoadManagerHelper;
import com.digma.digmaleyes.R;
import com.digma.digmaleyes.helpers.SendEmail;
import com.digma.digmaleyes.helpers.Utils;
import com.digma.digmaleyes.models.Ley;

import java.util.List;

/**
 * Created by janidham on 03/12/15.
 */
public class LeyAdapter extends RecyclerView.Adapter<LeyAdapter.LeyViewHolder> {
    private List<Ley> items;
    private DownLoadManagerHelper downLoadHelper;
    private Utils utils;

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
        utils = new Utils(context);
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

        final String urlServer = utils.getContext().getString(R.string.url_server);
        final String urlSendEmail = utils.getContext().getString(R.string.url_send_email);
        final String url_document = items.get(i).getUrl();
        final String document_name = items.get(i).getName();

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            downLoadHelper.startDownload(urlServer + url_document, document_name);
            }
        });
        holder.send.setOnClickListener(new View.OnClickListener() {
            Activity context = utils.getContext();
            @Override
            public void onClick(View v) {
                String email = utils.isDefaultEmail();

                if (!email.equals("")) {
                    //String document_name = items.get(i).getUrl();
                    //String document = items.get(i).getName();
                    new SendEmail(email, url_document, document_name, context)
                            .execute(urlSendEmail);
                    return;
                }


                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Get the layout inflater
                final LayoutInflater inflater = context.getLayoutInflater();

                builder.setTitle("Escriba el correo!");

                builder.setView(inflater.inflate(R.layout.dialog_message, null))
                        // Add action buttons
                        .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Dialog f = (Dialog) dialog;

                                EditText editText = (EditText) f.findViewById(R.id.email_to_send);

                                String email = editText.getText().toString();

                                if (!utils.isValidEmail(email)) {
                                    utils.renderMessage("Debe ingresar un email.", Toast.LENGTH_SHORT);
                                    return;
                                }

                                utils.renderMessage("Enviando mensaje.", Toast.LENGTH_SHORT);

                                //String document_name = items.get(i).getUrl();
                                //String document = items.get(i).getName();
                                new SendEmail(email, url_document, document_name, context)
                                        .execute(urlSendEmail);

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                builder.create();
                builder.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
}
