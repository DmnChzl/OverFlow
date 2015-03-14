/**
 * Copyright (C) 2013 Damien Chazoule
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.doomy.overflow;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MessageBD";
    private static final String TABLE_NAME = "MessagesTable";
    private static final String KEY_ID = "id";
    private static final String KEY_RECIPIENT = "recipient";
    private static final String KEY_COLOR = "color";
    private static final String KEY_TEXT = "text";
    private static final String KEY_NUMBER = "number";
    private static final String[] COLONNES = { KEY_ID, KEY_RECIPIENT, KEY_COLOR,
            KEY_TEXT, KEY_NUMBER };

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i("SQLite DB : ", "Constructeur");
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {

        String CREATE_TABLE = "CREATE TABLE MessagesTable ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "recipient TEXT, "
                + "color INTEGER, " + "text TEXT, " + "number TEXT )";

        arg0.execSQL(CREATE_TABLE);
        Log.i("SQLite DB", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

        arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(arg0);
        Log.i("SQLite DB", "onUpgrade");
    }

    public void deleteAll() {
        SQLiteDatabase mDB = this.getWritableDatabase();
        mDB.delete(TABLE_NAME, null, null);
        mDB.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "'");
        Log.i("SQLite DB", "Delete All");
    }

    public void deleteOne(Message mMessage) {

        // 1. Get reference to writable DB
        SQLiteDatabase mDB = this.getWritableDatabase();
        mDB.delete(TABLE_NAME, // Table
                "id = ?", new String[] { String.valueOf(mMessage.getID()) });
        mDB.close();
        Log.i("SQLite DB : Delete : ", mMessage.toString());
    }

    public Message showOne(int id) {
        SQLiteDatabase mDB = this.getReadableDatabase();
        Cursor mCursor = mDB.query(TABLE_NAME, // A. Table
                COLONNES, // B. Column names
                " id = ?", // C. Selections
                new String[] { String.valueOf(id) }, // D. Selections args
                null, // e. Group by
                null, // f. Having
                null, // g. Order by
                null); // h. Limit
        if (mCursor != null)
            mCursor.moveToFirst();
        Message mMessage = new Message();
        mMessage.setID(Integer.parseInt(mCursor.getString(0)));
        mMessage.setRecipient(mCursor.getString(1));
        mMessage.setColor(Integer.parseInt(mCursor.getString(2)));
        mMessage.setText(mCursor.getString(3));
        mMessage.setNumber(mCursor.getString(4));
        // Log
        Log.i("SQLite DB : Show One  : ID =  " + id, mMessage.toString());

        return mMessage;
    }

    public List<Message> showAll() {

        List<Message> mMessages = new LinkedList<>();
        String mQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase mDB = this.getWritableDatabase();
        Cursor mCursor = mDB.rawQuery(mQuery, null);
        Message mMessage = null;
        if (mCursor.moveToFirst()) {
            do {
                mMessage = new Message();
                mMessage.setID(Integer.parseInt(mCursor.getString(0)));
                mMessage.setRecipient(mCursor.getString(1));
                mMessage.setColor(Integer.parseInt(mCursor.getString(2)));
                mMessage.setText(mCursor.getString(3));
                mMessage.setNumber(mCursor.getString(4));
                mMessages.add(mMessage);
            } while (mCursor.moveToNext());
        }
        Log.i("SQLite DB : Show All : ", mMessages.toString());
        return mMessages;
    }

    public void addOne(Message mMessage) {

        SQLiteDatabase mDB = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RECIPIENT, mMessage.getRecipient());
        values.put(KEY_COLOR, mMessage.getColor());
        values.put(KEY_TEXT, mMessage.getText());
        values.put(KEY_NUMBER, mMessage.getNumber());
        // Insertion
        mDB.insert(TABLE_NAME, // Table
                null, values);

        mDB.close();
        Log.i("SQLite DB : Add One  : ID =  " + mMessage.getID(), mMessage.toString());
    }

    public int updateOne(Message mMessage) {

        SQLiteDatabase mDB = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RECIPIENT, mMessage.getRecipient());
        values.put(KEY_COLOR, mMessage.getColor());
        values.put(KEY_TEXT, mMessage.getText());
        values.put(KEY_NUMBER, mMessage.getNumber());

        int i = mDB.update(TABLE_NAME, // Table
                values, // Column/Value
                "id = ?", // Selections
                new String[] { String.valueOf(mMessage.getID()) });

        mDB.close();
        Log.i("SQLite DB : Update One  : ID =  " + mMessage.getID(), mMessage.toString());

        return i;
    }

    public int getRowsCount() {
        String mQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase mDB = this.getReadableDatabase();
        Cursor mCursor = mDB.rawQuery(mQuery, null);
        int mCount = mCursor.getCount();
        mCursor.close();
        Log.i("SQLite DB : Row(s) : ", mCount + "");
        return mCount;
    }
}
