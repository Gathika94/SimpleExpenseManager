package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Gathika on 11/20/2016.
 */

public class PersistentAccountDAO implements AccountDAO {

    private SQLiteDatabase dataBase;

    public PersistentAccountDAO(SQLiteDatabase db){
        this.dataBase=db;
    }

    @Override
    public List<String> getAccountNumbersList() {


        Cursor resultSet = dataBase.rawQuery("SELECT Account_No FROM account_table",null);

        List<String> accountNumbers = new ArrayList<String>();
        resultSet.moveToFirst();
        while(resultSet.moveToNext()){
            accountNumbers.add(resultSet.getString(resultSet.getColumnIndex("Account_No")));
            }

        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {

        Cursor resultSet = dataBase.rawQuery("SELECT * FROM account_table",null);

        List<Account> accounts = new ArrayList<Account>();
        resultSet.moveToFirst();

        while(resultSet.moveToNext()){
            Account account = new Account(resultSet.getString(resultSet.getColumnIndex("Account_No")), resultSet.getString(resultSet.getColumnIndex("Bank")), resultSet.getString(resultSet.getColumnIndex("Acc_Holder")), resultSet.getDouble(resultSet.getColumnIndex("Initial_Amount")));
            accounts.add(account);
            }

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {


        Cursor resultSet = dataBase.rawQuery("SELECT * FROM account_table WHERE Account_No = " + accountNo,null);

        Account account = null;
        resultSet.moveToFirst();
        while(resultSet.moveToNext()){
            account = new Account(resultSet.getString(resultSet.getColumnIndex("Account_No")), resultSet.getString(resultSet.getColumnIndex("Bank")), resultSet.getString(resultSet.getColumnIndex("Acc_Holder")), resultSet.getDouble(resultSet.getColumnIndex("Initial_Amount")));
             }

        return account;
    }

    @Override
    public void addAccount(Account account) {
        String qry = "INSERT INTO account_table (Account_No,Bank,Acc_Holder,Initial_Amount) VALUES (?,?,?,?)";
        SQLiteStatement statement = dataBase.compileStatement(qry);
        statement.bindString(1, account.getAccountNo());
        statement.bindString(2, account.getBankName());
        statement.bindString(3, account.getAccountHolderName());
        statement.bindDouble(4, account.getBalance());
        statement.executeInsert();


    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String qry = "DELETE FROM account_table WHERE Account_No = ?";
        SQLiteStatement statement = dataBase.compileStatement(qry);
        statement.bindString(1,accountNo);
        statement.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String qry = "UPDATE account_table SET Initial_Amount = Initial_Amount + ?";
        SQLiteStatement statement = dataBase.compileStatement(qry);
        if(expenseType == ExpenseType.EXPENSE){
              statement.bindDouble(1,-amount);
        }else{
              statement.bindDouble(1,amount);
        }

        statement.executeUpdateDelete();
    }
}
