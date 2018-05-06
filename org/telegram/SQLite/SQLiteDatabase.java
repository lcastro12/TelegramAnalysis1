package org.telegram.SQLite;

import java.util.HashMap;
import java.util.Map;
import org.telegram.messenger.FileLog;
import org.telegram.ui.ApplicationLoader;

public class SQLiteDatabase {
    private boolean inTransaction = false;
    private boolean isOpen = false;
    private final Map<String, SQLitePreparedStatement> preparedMap;
    private final int sqliteHandle;
    private StackTraceElement[] temp;

    native void beginTransaction(int i);

    native void closedb(int i) throws SQLiteException;

    native void commitTransaction(int i);

    native int opendb(String str, String str2) throws SQLiteException;

    public int getSQLiteHandle() {
        return this.sqliteHandle;
    }

    public SQLiteDatabase(String fileName) throws SQLiteException {
        this.sqliteHandle = opendb(fileName, ApplicationLoader.applicationContext.getFilesDir().getPath());
        this.isOpen = true;
        this.preparedMap = new HashMap();
    }

    public boolean tableExists(String tableName) throws SQLiteException {
        checkOpened();
        if (executeInt("SELECT rowid FROM sqlite_master WHERE type='table' AND name=?;", tableName) != null) {
            return true;
        }
        return false;
    }

    public void execute(String sql, Object... args) throws SQLiteException {
        checkOpened();
        SQLiteCursor cursor = query(sql, args);
        try {
            cursor.next();
        } finally {
            cursor.dispose();
        }
    }

    public SQLitePreparedStatement executeFast(String sql) throws SQLiteException {
        return new SQLitePreparedStatement(this, sql, true);
    }

    public Integer executeInt(String sql, Object... args) throws SQLiteException {
        checkOpened();
        SQLiteCursor cursor = query(sql, args);
        try {
            if (!cursor.next()) {
                return null;
            }
            Integer valueOf = Integer.valueOf(cursor.intValue(0));
            cursor.dispose();
            return valueOf;
        } finally {
            cursor.dispose();
        }
    }

    public int executeIntOrThrow(String sql, Object... args) throws SQLiteException, SQLiteNoRowException {
        checkOpened();
        Integer val = executeInt(sql, args);
        if (val != null) {
            return val.intValue();
        }
        throw new SQLiteNoRowException();
    }

    public String executeString(String sql, Object... args) throws SQLiteException {
        checkOpened();
        SQLiteCursor cursor = query(sql, args);
        try {
            if (!cursor.next()) {
                return null;
            }
            String stringValue = cursor.stringValue(0);
            cursor.dispose();
            return stringValue;
        } finally {
            cursor.dispose();
        }
    }

    public SQLiteCursor query(String sql, Object... args) throws SQLiteException {
        checkOpened();
        SQLitePreparedStatement stmt = (SQLitePreparedStatement) this.preparedMap.get(sql);
        if (stmt == null) {
            stmt = new SQLitePreparedStatement(this, sql, false);
            this.preparedMap.put(sql, stmt);
        }
        return stmt.query(args);
    }

    public SQLiteCursor queryFinalized(String sql, Object... args) throws SQLiteException {
        checkOpened();
        return new SQLitePreparedStatement(this, sql, true).query(args);
    }

    public void close() {
        if (this.isOpen) {
            try {
                for (SQLitePreparedStatement stmt : this.preparedMap.values()) {
                    stmt.finalizeQuery();
                }
                closedb(this.sqliteHandle);
            } catch (SQLiteException e) {
                FileLog.m801e("tmessages", e.getMessage(), e);
            }
            this.isOpen = false;
        }
    }

    void checkOpened() throws SQLiteException {
        if (!this.isOpen) {
            throw new SQLiteException("Database closed");
        }
    }

    public void finalize() throws Throwable {
        super.finalize();
        close();
    }

    public void beginTransaction() throws SQLiteException {
        if (this.inTransaction) {
            throw new SQLiteException("database already in transaction");
        }
        this.inTransaction = true;
        beginTransaction(this.sqliteHandle);
    }

    public void commitTransaction() {
        this.inTransaction = false;
        commitTransaction(this.sqliteHandle);
    }
}
