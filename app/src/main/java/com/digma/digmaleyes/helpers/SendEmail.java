package com.digma.digmaleyes.helpers;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by janidham on 10/12/15.
 */
public class SendEmail extends AsyncTask<String, Void, JSONObject> {
    String email, url_document, document_name;
    Activity context;
    Utils utils;

    public SendEmail(String email, String url_document, String document_name, Activity context) {
        this.email = email;
        this.url_document = url_document;
        this.context = context;
        this.document_name = document_name;
        utils = new Utils(context);
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject json = new JSONObject();
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = params[0];
        //HttpGet httppostreq = new HttpGet(url);
        StringEntity se;
        HttpResponse httpresponse;
        String responseText = null;
        JSONObject jsonResponse = null;

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("email", this.email));
        pairs.add(new BasicNameValuePair("url_document", this.url_document));
        pairs.add(new BasicNameValuePair("document_name", this.document_name));

        String paramsUrl = URLEncodedUtils.format(pairs, "utf-8");
        url += "?" + paramsUrl;
        HttpGet httppostreq = new HttpGet(url);
        try {

            httpresponse = httpClient.execute(httppostreq);

            responseText = EntityUtils.toString(httpresponse.getEntity());

            jsonResponse = new JSONObject(responseText);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);


        if (jsonObject == null) {
            utils.renderMessage("Hubo un error en el servidor.", Toast.LENGTH_SHORT);
            return;
        }

        try {
            utils.renderMessage(jsonObject.getString("message"), Toast.LENGTH_SHORT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
