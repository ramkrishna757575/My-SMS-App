package com.ramkrishna.android.mymessagingapp;

/**
 * Created by ramkr on 22-Apr-16.
 *
 * Global constants used in the application. Mostly to build the query to fetch sms data.
 */
public class Constants {
    public static final String URI_SMS_DB = "content://sms/";
    public static final String URI_SMS_TABLE = "content://sms/inbox";
    public static final String COL_ID = "_id";
    public static final String COL_ADDR = "address";
    public static final String COL_MSG_BODY = "body";
    public static final String COL_DATE = "date";
    public static final String COL_READ_STATUS = "read";
    public static final String[] REQ_COLS = new String[]{COL_ID, COL_ADDR, COL_MSG_BODY, COL_DATE, COL_READ_STATUS};
    public static final String GROUP_BY_ADDR = "1=1)GROUP BY (address";
    public static final String CONDITION_OR = " OR ";
    public static final String CONDITION_AND = " AND ";
    public static final String INTENT_EXTRA_SENDER = "Sender";
    public static String SELECTION_BY_SENDER = COL_ADDR + "='?'";
    public static String SELECTION_BY_SENDER_SEARCH = COL_MSG_BODY + " LIKE '%?%'";
    public static String SELECTION_BY_SEARCH = COL_ADDR + " LIKE '%?%' " + CONDITION_OR + COL_MSG_BODY + " LIKE '%?%'";
}
