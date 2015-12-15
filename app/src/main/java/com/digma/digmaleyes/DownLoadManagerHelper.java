package com.digma.digmaleyes;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by janidham on 07/12/15.
 */
public class DownLoadManagerHelper {

    private DownloadManager downloadManager;
    private long downloadReference;
    private Activity Context;

    public DownLoadManagerHelper(Activity context) {
        this.Context = context;
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        Context.registerReceiver(receiver, filter);
    }

    public void startDownload(String url, String fileName) {
        this.Context.findViewById(R.id.gif_loading).setVisibility(View.VISIBLE);

        downloadManager = (DownloadManager) Context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri Download_Uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

        //Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //Set whether this download may proceed over a roaming connection.
        request.setAllowedOverRoaming(false);
        //Set the title of this download, to be displayed in notifications (if enabled).
        request.setTitle(fileName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //Set a description of this download, to be displayed in notifications (if enabled)
        request.setDescription(fileName);
        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalFilesDir(Context, Environment.DIRECTORY_DOWNLOADS, fileName);

        //Enqueue a new download and same the referenceId
        downloadReference = downloadManager.enqueue(request);

        //renderMessage("La descarga se ha iniciado.", Toast.LENGTH_LONG);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if(downloadReference == referenceId) {
                Context.findViewById(R.id.gif_loading).setVisibility(View.GONE);
                //renderMessage("La descarga finalizó con éxito.", Toast.LENGTH_LONG);
            }
        }
    };

    public void renderMessage(String message, int time) {
        Toast toast = Toast.makeText(Context, message, time);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
