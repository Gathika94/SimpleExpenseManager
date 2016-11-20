package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gathika on 11/20/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String Database_Name="140528M.db";
    public static final String Account_Table="account_table";
    public static final String Transaction_Log= "transaction_log";


    public DatabaseHandler(Context context) {
        super(context, Database_Name, null, 1);
        SQLiteDatabase myDB= this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS " + Account_Table + "(Account_No VARCHAR PRIMARY KEY,Bank VARCHAR,Acc_Holder VARCHAR,Initial_Amount Real)" );
        db.execSQL("create table IF NOT EXISTS " + Transaction_Log+ "(Transaction_ID INTEGER PRIMARY KEY,Account_No VARCHAR,Type INT,Ammount REAL, Loged_Date DATE,FOREIGN KEY(Account_No) REFERENCES account_table(Account_No)))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
