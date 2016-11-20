package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

/**
 * Created by Gathika on 11/20/2016.
 */

public class PersistentExpenseManager extends ExpenseManager {

    DatabaseHandler emDB;
    private Context context;
    public PersistentExpenseManager(Context context) throws ExpenseManagerException {
        this.context=context;
        setup();
    }
    public static final String Database_Name="140528M.db";
    private static final String Account_Table="account_table";
    private static final String Transaction= "transaction_log";

    @Override

    public void setup() throws ExpenseManagerException {
        SQLiteDatabase emDB=context.openOrCreateDatabase("140528M",context.MODE_PRIVATE,null);
        emDB.execSQL("create table IF NOT EXISTS " + Account_Table + "(Account_No VARCHAR PRIMARY KEY,Bank VARCHAR,Acc_Holder VARCHAR,Initial_Amount Real)" );
        emDB.execSQL("create table IF NOT EXISTS " +Transaction + "(Transaction_ID INTEGER PRIMARY KEY,Account_No VARCHAR,Type INT,Ammount REAL, Loged_Date DATE,FOREIGN KEY(Account_No) REFERENCES account_table(Account_No))");
        AccountDAO accountDAO = new PersistentAccountDAO(emDB);
        setAccountsDAO(accountDAO);
        setTransactionsDAO(new PersistentTransactionDAO(emDB));
    }


}
