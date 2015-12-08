package com.digma.digmaleyes;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.digma.digmaleyes.db.DataBaseAdapter;
import com.digma.digmaleyes.models.Ley;
import com.digma.digmaleyes.models.adapters.LeyAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private String drawerTitle;

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager linearLayoutManager;

    private LinearLayout config_email;
    private EditText search_txt;
    private EditText default_email_txt;
    private CheckBox check_email;
    private Button btn_save_email_config;
    private ImageButton btn_banner_center;
    private ImageView banner_bottom;
    private ImageView banner_center;

    private DataBaseAdapter mDbHelper;

    public static final String CHECK_EMAIL = "check_email";
    public static final String DEFAULT_EMAIL = "default_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_txt = (EditText) findViewById(R.id.search_text);
        config_email = (LinearLayout) findViewById(R.id.config_email);
        default_email_txt = (EditText) findViewById(R.id.default_email);
        check_email = (CheckBox) findViewById(R.id.check_default_email);
        btn_save_email_config = (Button) findViewById(R.id.btn_config_email);
        btn_banner_center = (ImageButton) findViewById(R.id.btn_banner_center);
        banner_bottom = (ImageView) findViewById(R.id.banner_bottom);
        banner_center = (ImageView) findViewById(R.id.banner_center);

        this.setNavigationDrawer(this.setToolbar());

        this.setRecycler();

        drawerTitle = getResources().getString(R.string.leyes_fundamentales);

        if (savedInstanceState == null) {
            this.initDataBase();

            this.selectItem(drawerTitle);
            this.setTitle(drawerTitle);
            this.getSharedPreferences();
            Log.i("Create", "activity created");
        }

        btn_save_email_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSharedPreferences();
            }
        });

        search_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("Change text", s.toString());
                mDbHelper.open();

                String table = getTableName(getTitle().toString());
                String where = "WHERE tags LIKE '%" + s.toString().toLowerCase().trim() + "%'";

                fillAdapter(mDbHelper.filterFrom(table, where));

                mDbHelper.close();
            }
        });

        btn_banner_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toogleBannerCenter(false);
                toogleRecyclerView(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        findViewById(R.id.main_layout)
                                .setBackground(getDrawable(R.color.background));
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        findViewById(R.id.main_layout)
                                .setBackgroundDrawable(getDrawable(R.color.background));
                    }
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Resume", "activity resume");

        //new DownloadImageTask((ImageView) findViewById(R.id.banner_bottom))
        //        .execute("http://2.bp.blogspot.com/-_ONfxvk_A-8/VPHsRP8jO3I/AAAAAAADkvk/Vy8_gFIOY9w/s1600/9%2B(1).png");

        new DownloadImageTask((ImageView) findViewById(R.id.banner_center))
                .execute("https://dardomixcomunicacional.files.wordpress.com/2012/09/banner-vertical.png");
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            toogleRecyclerView(false);
            toogleBannerCenter(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                findViewById(R.id.main_layout)
                        .setBackground(getDrawable(R.color.cardview_dark_background));
            } else {
                findViewById(R.id.main_layout)
                        .setBackgroundDrawable(getDrawable(R.color.cardview_dark_background));
            }
        }
    }

    private void getSharedPreferences() {
        SharedPreferences settings = getPreferences(0);
        boolean tmp_check_email = settings.getBoolean(CHECK_EMAIL, false);
        String tmp_default_email = settings.getString(DEFAULT_EMAIL, "");

        check_email.setChecked(tmp_check_email);
        default_email_txt.setText(tmp_default_email);
    }

    private void setSharedPreferences() {
        if (!this.isValidConfig()) return;

        SharedPreferences settings = getPreferences(0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean(CHECK_EMAIL, check_email.isChecked());
        editor.putString(DEFAULT_EMAIL, default_email_txt.getText().toString().trim());

        editor.commit();

        this.renderMessage(getResources().getString(R.string.success_save_config_email));
    }

    private boolean isValidConfig() {
        if (check_email.isChecked() == true && default_email_txt.getText().toString().trim().equals("")) {
            this.renderMessage(getResources().getString(R.string.error_save_config_email));
            check_email.setChecked(false);
            return false;
        }

        return true;
    }

    private void renderMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void setRecycler() {
        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.recycler_view);
        //recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        linearLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(linearLayoutManager);

    }

    private void setNavigationDrawer(Toolbar toolbar) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private Toolbar setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        return toolbar;
    }

    private void initDataBase() {
        mDbHelper = new DataBaseAdapter(this.getApplicationContext());

        mDbHelper.createDatabase();

    }

    private void setRecyclerViewAdapter(List<Ley> items) {
        if (items == null) items = new ArrayList<>();

        adapter = null;
        adapter = new LeyAdapter(items, this);
        recycler.setAdapter(adapter);
    }

    private void fillAdapter(Cursor data) {
        List<Ley> items = new ArrayList<>();

        //mDbHelper.open();

        //Cursor data = mDbHelper.selectAllFrom(table);

        if (data == null || data.getCount() == 0 ) {
            items.add(new Ley(-1, "No se encontraron datos..", "", ""));
            this.setRecyclerViewAdapter(items);
            return;
        }

        while(data.moveToNext()) {
            items.add(new Ley(data.getInt(0), data.getString(1), data.getString(2), data.getString(3)));
        }

        this.setRecyclerViewAdapter(items);

        //mDbHelper.close();
    }

    private String getTableName(String title) {

        if (title.equals(getResources().getString(R.string.leyes_fundamentales))) {
            return "leyes_fundamentales";
        } else if (title.equals(getResources().getString(R.string.leyes))) {
            return "leyes";
        } else if (title.equals(getResources().getString(R.string.codigos))) {
            return "codigos";
        } else if (title.equals(getResources().getString(R.string.r_legislativo))) {
            return "leyes";
        } else if (title.equals(getResources().getString(R.string.r_judicial))) {
            return "leyes";
        } else if (title.equals(getResources().getString(R.string.r_dependencias))) {
            return "leyes";
        }

        return "";
    }

    private void selectItem(String title) {
        mDbHelper.open();

        if (title.equals(getResources().getString(R.string.leyes_fundamentales))) {
            this.toogleFragment(false);
            this.toogleRecyclerView(true);
            this.fillAdapter(mDbHelper.selectAllFrom("leyes_fundamentales"));
        }
        else if (title.equals(getResources().getString(R.string.leyes))) {
            this.toogleFragment(false);
            this.toogleRecyclerView(true);
            this.fillAdapter(mDbHelper.selectAllFrom("leyes"));
        }

        else if (title.equals(getResources().getString(R.string.codigos))) {
            this.toogleFragment(false);
            this.toogleRecyclerView(true);
            this.fillAdapter(mDbHelper.selectAllFrom("codigos"));
        }

        else if (title.equals(getResources().getString(R.string.r_legislativo))) {
            this.toogleFragment(false);
            this.toogleRecyclerView(true);
            this.fillAdapter(mDbHelper.selectAllFrom("leyes"));
        }

        else if (title.equals(getResources().getString(R.string.r_judicial))) {
            this.toogleFragment(false);
            this.toogleRecyclerView(true);
            this.fillAdapter(mDbHelper.selectAllFrom("leyes"));
        }

        else if (title.equals(getResources().getString(R.string.r_dependencias))) {
            this.toogleFragment(false);
            this.toogleRecyclerView(true);
            this.fillAdapter(mDbHelper.selectAllFrom("leyes"));
        }

        else if (title.equals(getResources().getString(R.string.email))) {
            this.toogleRecyclerView(false);
            this.toogleFragment(true);
            //URL url = new URL("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464");
            /*URL url = new URL("http://www.brandemia.org/wp-content/uploads/2012/10/logo_principal.jpg");
            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
        } else {

        }

        this.setTitle(title); // Setear título actual
        mDbHelper.close();
    }

    private void toogleFragment(boolean show) {
        if (show) config_email.setVisibility(View.VISIBLE); //findViewById(R.id.main_content).setVisibility(View.VISIBLE);

        else config_email.setVisibility(View.GONE); //findViewById(R.id.main_content).setVisibility(View.INVISIBLE);
    }

    private void toogleRecyclerView(boolean show) {
        if (show) {
            recycler.setVisibility(View.VISIBLE);
            search_txt.setVisibility(View.VISIBLE);
        } else {
            recycler.setVisibility(View.GONE);
            search_txt.setVisibility(View.GONE);
        }
    }

    private void toogleBannerCenter(boolean show) {
        if (show)
            findViewById(R.id.fragment_banner).setVisibility(View.VISIBLE);

        else
            findViewById(R.id.fragment_banner).setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawers();

        else super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        String title = item.getTitle().toString();

        this.selectItem(title);

        drawerLayout.closeDrawers(); // Cerrar drawer

        return true;

        /*if (id == R.id.leyes_fundamentales) {
            // Handle the camera action
        } else if (id == R.id.leyes) {

        } else if (id == R.id.codigos) {

        } else if (id == R.id.r_legislativo) {

        } else if (id == R.id.r_judicial) {

        } else if (id == R.id.r_dependencias) {

        } else if (id == R.id.email) {

        }*/

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);

    }


    // No se esta usando por el momento
    private void setFloatingButton() {
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
}
