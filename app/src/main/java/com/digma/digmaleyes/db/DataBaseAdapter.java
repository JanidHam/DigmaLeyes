package com.digma.digmaleyes.db;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.digma.digmaleyes.models.Ley;

/**
 * Created by janidham on 02/12/15.
 */
public class DataBaseAdapter {

    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public DataBaseAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public DataBaseAdapter createDatabase() throws SQLException
    {
        try
        {
            mDbHelper.createDataBase();
        }
        catch (IOException mIOException)
        {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DataBaseAdapter open() throws SQLException
    {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }

    public Cursor selectAllFrom(String table) {
        try {
            String sql = "SELECT * FROM " + table;

            Cursor mCur = mDb.rawQuery(sql, null);

            return mCur;
        }
        catch (SQLException ex) {
            Log.e(TAG, "selectAllFrom " + table + " " + ex.toString());
            //throw ex;
            return null;
        }
    }

    public Cursor filterFrom(String table, String where) {
        try {
            String sql = "SELECT * FROM " + table + " " + where;

            Cursor mCur = mDb.rawQuery(sql, null);

            return mCur;

        } catch (SQLException ex) {
            Log.e(TAG, "selectAllFrom " + table + " " + ex.toString());
            //throw ex;
            return null;
        }
    }

    public Cursor getTestData()
    {
        try
        {
            String sql ="SELECT * FROM leyes";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
}
