package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Gathika on 11/20/2016.
 */

public class PersistentTransactionDAO implements TransactionDAO {

    private SQLiteDatabase dataBase;

    public PersistentTransactionDAO(SQLiteDatabase db){
        this.dataBase = db;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String sql = "INSERT INTO transaction_log (Account_No,Type,Ammount,Loged_Date) VALUES (?,?,?,?)";
        SQLiteStatement statement = dataBase.compileStatement(sql);

        statement.bindString(1,accountNo);
        statement.bindLong(2,(expenseType == ExpenseType.EXPENSE) ? 0 : 1);
        statement.bindDouble(3,amount);
        statement.bindLong(4,date.getTime());
        statement.executeInsert();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor resultSet = dataBase.rawQuery("SELECT * FROM transaction_log",null);

        List<Transaction> allTransactions = new ArrayList<Transaction>();

        resultSet.moveToFirst();
        while(resultSet.moveToNext()){
            Transaction t = new Transaction(new Date(resultSet.getLong(resultSet.getColumnIndex("Loged_Date"))), resultSet.getString(resultSet.getColumnIndex("Account_No")), (resultSet.getInt(resultSet.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME, resultSet.getDouble(resultSet.getColumnIndex("Ammount")));
            allTransactions.add(t);
        }
        return allTransactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Cursor resultSet = dataBase.rawQuery("SELECT * FROM transaction_log LIMIT " + limit,null);
        resultSet.moveToFirst();
        List<Transaction> pgTransactions = new ArrayList<Transaction>();

        while(resultSet.moveToNext()){
            Transaction t = new Transaction(new Date(resultSet.getLong(resultSet.getColumnIndex("Loged_Date"))), resultSet.getString(resultSet.getColumnIndex("Account_No")), (resultSet.getInt(resultSet.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME, resultSet.getDouble(resultSet.getColumnIndex("Ammount")));
            pgTransactions.add(t);
        }
        return pgTransactions;
    }
}
