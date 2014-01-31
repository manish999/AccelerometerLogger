package com.rampgreen.acceldatacollector.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Application Database Actions class.
 * 
 * Executes the database actions such as insert, update, delete, etc.
 * 
 * 
 * @constructor context
 * 
 */

public class MyAppDbSQL {
	private MyAppDbAdapter dbAdapterObj;
	private SQLiteDatabase sqliteDBObj;
	private Context ctxContext;

	/**
	 * MyAppDbSQL Class Constructor
	 */
	protected MyAppDbSQL(Context ctxContext) {
		try {
			this.ctxContext = ctxContext;
		} catch (Exception error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbSQL.MyAppDbSQL",
			//          "exception thrown in the class constructor");
			//      errExcpError = null;
		}// end try/catch (Exception error)
	}// end constructor

	/**
	 * Database Query methods
	 */

	/**
	 * listOrderByOpts
	 * 
	 * @param int intOrderByOpt - the OrderBy Option to return
	 * @return String
	 * 
	 */
	protected String listOrderByOpts(int intOrderByOpt) {
		switch (intOrderByOpt) {
		//    case 0:
		//      return MyAppDbAdapter.KEY_ALBUM + " ASC";
		//
		//    case 1:
		//      return MyAppDbAdapter.KEY_ALBUM + " DESC";
		//
		//    case 2:
		//      return MyAppDbAdapter.KEY_ARTIST + " ASC";
		//
		//    case 3:
		//      return MyAppDbAdapter.KEY_ARTIST + " DESC";
		//
		//    case 4:
		//      return MyAppDbAdapter.KEY_ALBUMDATE + "  ASC";
		//
		//    case 5:
		//      return MyAppDbAdapter.KEY_ALBUMDATE + " DESC";
		//
		//    case 6:
		//      return MyAppDbAdapter.KEY_GENRE + "  ASC";
		//
		//    case 7:
		//      return MyAppDbAdapter.KEY_GENRE + " DESC";
		//
		//    default:
		//      return MyAppDbAdapter.KEY_ARTIST + " ASC";
		}// end switch;
		return MyAppDbAdapter.KEY_ROWID + " ASC";
	}// end listOrderByOpts

	protected boolean openDbAdapter() throws Exception {
		boolean isOpen = false;

		try {
			if (this.dbAdapterObj == null || !(this.dbAdapterObj.dbIsOpen())) {
				this.dbAdapterObj = new MyAppDbAdapter(ctxContext);

				if (this.dbAdapterObj != null)
					this.sqliteDBObj = this.dbAdapterObj.getReadableDatabase();
			}// end if (this.dbMusic == null || ...

			if (this.dbAdapterObj.dbIsOpen() && this.sqliteDBObj != null) {
				isOpen = true;
			}
		} catch (Exception error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbSQL.openDbAdapter",
			//          "exception thrown opening the db adapter");
			//      errExcpError = null;

			isOpen = false;
		}// end try/catch (Exception error)

		return isOpen;
	}

	protected boolean closeDbAdapter() throws Exception {
		boolean isClosed = false;

		try {
			// clean up DB objects
			if (this.sqliteDBObj != null) {
				this.sqliteDBObj.close();
				this.sqliteDBObj = null;
			}

			if (this.dbAdapterObj != null) {
				this.dbAdapterObj.close();
				this.dbAdapterObj = null;
			}

			isClosed = true;
		} catch (Exception error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbSQL.closeDbAdapter",
			//          "exception thrown closing the db adapter");
			//      errExcpError = null;

			isClosed = false;
		}// end try/catch (Exception error)

		return isClosed;
	}

	/**
	 * fetchAccelMusic: Return a Cursor over the list of entries in the database
	 * 
	 * @param intSortOpt
	 *          the selected sorting option
	 * @return Cursor containing filtered query results
	 * @throws SQLException
	 *           if entry could not be found/retrieved
	 */
	protected Cursor fetchLoginDataAll(int intSortOpt) throws SQLException {
		String strOrderBy = "";
		Cursor mMusicCursor = null;

		try {
			strOrderBy = listOrderByOpts(intSortOpt);

			mMusicCursor = this.sqliteDBObj.query(MyAppDbAdapter.TABLE_CREDENTIAL,
					new String[] { MyAppDbAdapter.KEY_ROWID, MyAppDbAdapter.KEY_NAME,
					MyAppDbAdapter.KEY_USER_ID, MyAppDbAdapter.KEY_EMAIL,
					MyAppDbAdapter.KEY_PASSWORD, MyAppDbAdapter.KEY_ACCESS_TOKEN,
					MyAppDbAdapter.KEY_LOGIN_STATE},
					null, null, null, null, strOrderBy);

			if (mMusicCursor != null) {
				mMusicCursor.moveToFirst();

				return mMusicCursor;
			} else {
				return null;
			}// end if (mMusicCursor != null)
		} catch (SQLiteException error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbSQL.fetchMusic",
			//          "SQLiteException - exception thrown fetching music");
			//      errExcpError = null;

			return null;
		} catch (Exception error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbSQL.fetchMusic",
			//          "exception - exception thrown fetching music");
			//      errExcpError = null;

			return null;
		}// end try/catch (Exception error)
	}// end fetchMusic(int intSortOpt)


	/**
	 * fetchAccelMusic: Return a Cursor over the list of entries in the database
	 * 
	 * @param intSortOpt
	 *          the selected sorting option
	 * @return Cursor containing filtered query results
	 * @throws SQLException
	 *           if entry could not be found/retrieved
	 */
	protected Cursor fetchAccelDataAll(int intSortOpt) throws SQLException {
		String strOrderBy = "";
		Cursor mMusicCursor = null;

		try {
			strOrderBy = listOrderByOpts(intSortOpt);

			mMusicCursor = this.sqliteDBObj.query(MyAppDbAdapter.TABLE_ACCEL_DATA,
					new String[] { MyAppDbAdapter.KEY_ROWID, MyAppDbAdapter.KEY_XYZ,
					MyAppDbAdapter.KEY_TIMESTAMP_START, MyAppDbAdapter.KEY_TIMESTAMP_END,
					MyAppDbAdapter.KEY_ACTIVITY_TYPE, MyAppDbAdapter.KEY_DURATION,
					MyAppDbAdapter.KEY_PERCENTAGE_COMPLETED, MyAppDbAdapter.KEY_PART_COMPLETED,
					MyAppDbAdapter.KEY_TOTAL_PART, MyAppDbAdapter.KEY_PERCENTAGE_COMPLETED },
					null, null, null, null, strOrderBy);

			if (mMusicCursor != null) {
				mMusicCursor.moveToFirst();

				return mMusicCursor;
			} else {
				return null;
			}// end if (mMusicCursor != null)
		} catch (SQLiteException error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbSQL.fetchMusic",
			//          "SQLiteException - exception thrown fetching music");
			//      errExcpError = null;

			return null;
		} catch (Exception error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbSQL.fetchMusic",
			//          "exception - exception thrown fetching music");
			//      errExcpError = null;

			return null;
		}// end try/catch (Exception error)
	}// end fetchMusic(int intSortOpt)

	/**
	 * Return a Cursor positioned at the entry that matches the given rowId
	 * 
	 * @param rowId
	 *          id of entry to retrieve
	 * @return Cursor positioned to matching entry, if found
	 * @throws SQLException
	 *           if entry could not be found/retrieved
	 *           
	 *            protected static final String KEY_XYZ = "xyz";
//  protected static final String KEY_Y = "y";
//  protected static final String KEY_Z = "z";
  protected static final String KEY_TIMESTAMP_START = "start_time_stamp";
  protected static final String KEY_TIMESTAMP_END = "end_time_stamp";
  protected static final String KEY_ACTIVITY_TYPE = "activity_type";
  protected static final String KEY_DURATION = "duration";
  protected static final String KEY_PERCENTAGE_COMPLETED = "per_completed";
  protected static final String KEY_PART_COMPLETED = "part_completed";
  protected static final String KEY_TOTAL_PART = "total_part";
  protected static final String KEY_SEND_DATA_CLOUD = "send_Cloud_state";
	 */
	protected Cursor fetchAccelDataListEntry(long rowId) throws SQLException {

		Cursor mCursor = null;

		try {
			mCursor = this.sqliteDBObj.query(true, MyAppDbAdapter.TABLE_ACCEL_DATA,
					new String[] { MyAppDbAdapter.KEY_ROWID, MyAppDbAdapter.KEY_XYZ,
					MyAppDbAdapter.KEY_TIMESTAMP_START, MyAppDbAdapter.KEY_TIMESTAMP_END,
					MyAppDbAdapter.KEY_ACTIVITY_TYPE, MyAppDbAdapter.KEY_DURATION,
					MyAppDbAdapter.KEY_PERCENTAGE_COMPLETED, MyAppDbAdapter.KEY_PART_COMPLETED,
					MyAppDbAdapter.KEY_TOTAL_PART,
					MyAppDbAdapter.KEY_PERCENTAGE_COMPLETED }, MyAppDbAdapter.KEY_ROWID + "="
							+ rowId, null, null, null, null, null);

			if (mCursor != null) {
				mCursor.moveToFirst();

				return mCursor;
			} else {
				return null;
			}// end if (mCursor != null)
		} catch (SQLException error) {
			//      MyErrorLog<SQLException> errExcpError = new MyErrorLog<SQLException>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.fetchListEntry",
			//          "ListEntry query");
			//      errExcpError = null;

			return null;
		}// end try/catch (SQLException error)
		catch (Exception error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.fetchListEntry",
			//          "ListEntry query");
			//      errExcpError = null;

			return null;
		}// end try/catch (Exception error)
	}// end fetchListEntry(long rowId)


	protected Cursor fetchLoginListEntry(long rowId) throws SQLException {

		Cursor mCursor = null;

		try {
			mCursor = this.sqliteDBObj.query(true, MyAppDbAdapter.TABLE_CREDENTIAL,
					new String[] { MyAppDbAdapter.KEY_ROWID, MyAppDbAdapter.KEY_NAME,
					MyAppDbAdapter.KEY_USER_ID, MyAppDbAdapter.KEY_EMAIL,
					MyAppDbAdapter.KEY_PASSWORD, MyAppDbAdapter.KEY_ACCESS_TOKEN,
					MyAppDbAdapter.KEY_LOGIN_STATE}, MyAppDbAdapter.KEY_ROWID + "="
							+ rowId, null, null, null, null, null);

			if (mCursor != null) {
				mCursor.moveToFirst();

				return mCursor;
			} else {
				return null;
			}// end if (mCursor != null)
		} catch (SQLException error) {
			//	      MyErrorLog<SQLException> errExcpError = new MyErrorLog<SQLException>(
			//	          this.ctxContext);
			//	      errExcpError.addToLogFile(error, "MyAppDbAdapter.fetchListEntry",
			//	          "ListEntry query");
			//	      errExcpError = null;

			return null;
		}// end try/catch (SQLException error)
		catch (Exception error) {
			//	      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(
			//	          this.ctxContext);
			//	      errExcpError.addToLogFile(error, "MyAppDbAdapter.fetchListEntry",
			//	          "ListEntry query");
			//	      errExcpError = null;

			return null;
		}// end try/catch (Exception error)
	}// end fetchListEntry(long rowId)
	/**
	 * exportTableQuery: Return a Cursor for a specific table in the database
	 * 
	 * @param strTableName
	 *          the specified table
	 * 
	 * @param strOrderBy
	 *          the selected ordering option
	 * 
	 * @return Cursor containing filtered query results
	 * @throws SQLException
	 *           if entry could not be found/retrieved
	 */
	protected Cursor exportTableQuery(String strTableName, String strOrderBy) {
		Cursor mMusicCursor = null;

		try {
			// get everything from the table
			String strSQL = "SELECT * FROM " + strTableName + " ORDER BY "
					+ strOrderBy;

			mMusicCursor = this.sqliteDBObj.rawQuery(strSQL, null);

			if (mMusicCursor != null) {
				mMusicCursor.moveToFirst();

				return mMusicCursor;
			} else {
				return null;
			}// end if (mMusicCursor != null
		} catch (SQLException error) {
			//      MyErrorLog<SQLException> errExcpError = new MyErrorLog<SQLException>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.exportTableQuery",
			//          "creating export temporary Cursor");
			//      errExcpError = null;

			return null;
		}// end try/catch (SQLException error)
		catch (Exception error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.exportTableQuery",
			//          "creating export temporary Cursor");
			//      errExcpError = null;

			return null;
		}// end try/catch (Exception error)
	}// end exportTableQuery

	/****************************************************************/
	/**
	 * createLoginEntry: Creates a new entry in the database table
	 * 
	 * @return True if the database insert succeeds
	 * @throws SQLException
	 *           if entry could not be found/retrieved
	 */

	//  + KEY_USER_ID
	//  + " TEXT NOT NULL DEFAULT '', "
	//  + KEY_XYZ
	//  + " TEXT NOT NULL DEFAULT '', "
	//  + KEY_TIMESTAMP_START
	//  + " TEXT NOT NULL DEFAULT '', "
	//  + KEY_TIMESTAMP_END
	//  + " TEXT NOT NULL DEFAULT '', "
	//  + KEY_ACTIVITY_TYPE
	//  + " TEXT NOT NULL DEFAULT '', "
	//   + KEY_DURATION
	//  + " TEXT NOT NULL DEFAULT '', "
	//  + KEY_PERCENTAGE_COMPLETED
	//  + " TEXT NOT NULL DEFAULT '', "
	//  + KEY_PART_COMPLETED
	//  + " TEXT NOT NULL DEFAULT '', "
	//  + KEY_TOTAL_PART
	//  + " TEXT NOT NULL DEFAULT '', "
	//  + KEY_SEND_DATA_CLOUD
	//  + " TEXT NOT NULL DEFAULT '');";//, " + 
	protected Boolean createAccelDataEntry(String uid, String xyzData, String timeStampStart,
			String TimtStampEnd, String ActivityType,  String duration, String percentageCompleted, String partCompleted,
			String totalPart, String sentDataToCloudFlag) {
		boolean blIsSuccessful = false;

		this.sqliteDBObj.beginTransaction();
		try {
			ContentValues initialValues = new ContentValues();

			initialValues.put(MyAppDbAdapter.KEY_USER_ID, uid);
			initialValues.put(MyAppDbAdapter.KEY_XYZ, xyzData);
			initialValues.put(MyAppDbAdapter.KEY_TIMESTAMP_START, timeStampStart);
			initialValues.put(MyAppDbAdapter.KEY_TIMESTAMP_END, TimtStampEnd);
			initialValues.put(MyAppDbAdapter.KEY_ACTIVITY_TYPE, ActivityType);
			initialValues.put(MyAppDbAdapter.KEY_DURATION, duration);
			initialValues.put(MyAppDbAdapter.KEY_PERCENTAGE_COMPLETED, percentageCompleted);
			initialValues.put(MyAppDbAdapter.KEY_PART_COMPLETED, partCompleted);
			initialValues.put(MyAppDbAdapter.KEY_TOTAL_PART, totalPart);
			initialValues.put(MyAppDbAdapter.KEY_SEND_DATA_CLOUD, sentDataToCloudFlag);

			blIsSuccessful = (this.sqliteDBObj.insert(
					MyAppDbAdapter.TABLE_ACCEL_DATA, null, initialValues) != -1);

			if (blIsSuccessful == true) {
				this.sqliteDBObj.setTransactionSuccessful();
			}
		}// end try
		catch (SQLException error) {
			//      MyErrorLog<SQLException> errExcpError = new MyErrorLog<SQLException>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.createMusicEntry",
			//          "creating music entry");
			//      errExcpError = null;
			blIsSuccessful = false;
		}// end try/catch (Exception error)
		catch (Exception error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.createMusicEntry",
			//          "creating music entry");
			//      errExcpError = null;
			blIsSuccessful = false;
		}// end try/catch (Exception error)
		finally {
			this.sqliteDBObj.endTransaction();
		}

		return blIsSuccessful;
	}// end createMusicEntry

	/**
	 * updateMusicEntry: Updates an existing entry in the database table
	 * 
	 * @return True if the database update succeeds
	 * @throws SQLException
	 *           if entry could not be found/retrieved
	 */
	protected boolean updateAccelDataEntry(Long rowId, String uid, String xyzData, String timeStampStart,
			String TimtStampEnd, String ActivityType,  String duration, String percentageCompleted, String partCompleted,
			String totalPart, String sentDataToCloudFlag) {
		boolean blIsSuccessful = false;

		this.sqliteDBObj.beginTransaction();
		try {
			ContentValues initialValues = new ContentValues();

			//      initialValues.put(MyAppDbAdapter.KEY_USER_ID, uid);
			initialValues.put(MyAppDbAdapter.KEY_USER_ID, uid);
			initialValues.put(MyAppDbAdapter.KEY_XYZ, xyzData);
			initialValues.put(MyAppDbAdapter.KEY_TIMESTAMP_START, timeStampStart);
			initialValues.put(MyAppDbAdapter.KEY_TIMESTAMP_END, TimtStampEnd);
			initialValues.put(MyAppDbAdapter.KEY_ACTIVITY_TYPE, ActivityType);
			initialValues.put(MyAppDbAdapter.KEY_DURATION, duration);
			initialValues.put(MyAppDbAdapter.KEY_PERCENTAGE_COMPLETED, percentageCompleted);
			initialValues.put(MyAppDbAdapter.KEY_PART_COMPLETED, partCompleted);
			initialValues.put(MyAppDbAdapter.KEY_TOTAL_PART, totalPart);
			initialValues.put(MyAppDbAdapter.KEY_SEND_DATA_CLOUD, sentDataToCloudFlag);

			blIsSuccessful = (this.sqliteDBObj.update(
					MyAppDbAdapter.TABLE_CREDENTIAL, initialValues, MyAppDbAdapter.KEY_ROWID
					+ "=" + rowId, null) != -1);

			if (blIsSuccessful == true) {
				this.sqliteDBObj.setTransactionSuccessful();
			}
		}// end try
		catch (SQLException error) {
			//      MyErrorLog<SQLException> errExcpError = new MyErrorLog<SQLException>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.updateMusicEntry",
			//          "updating music entry information");
			//      errExcpError = null;
			blIsSuccessful = false;
		}// end try/catch (Exception error)
		catch (Exception error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.updateMusicEntry",
			//          "updating music entry information");
			//      errExcpError = null;
			blIsSuccessful = false;
		}// end try/catch (Exception error)
		finally {
			this.sqliteDBObj.endTransaction();
		}

		return blIsSuccessful;
	}// end updateMusicEntry

	/**
	 * Delete the entries
	 * 
	 * 
	 * @return true if deleted, false otherwise
	 * 
	 * @throws SQLException
	 *           if the SQL scripts encounter issues
	 */
	protected boolean deleteAccelDataEntries() throws SQLException {
		// delete entries
		boolean blIsSuccessful = false;

		this.sqliteDBObj.beginTransaction();
		try {
			blIsSuccessful = this.sqliteDBObj.delete(
					MyAppDbAdapter.TABLE_CREDENTIAL, "1", null) > 0;
					if (blIsSuccessful == true) {
						this.sqliteDBObj.setTransactionSuccessful();
					}
		}// end try
		catch (SQLException error) {
			//      MyErrorLog<SQLException> errExcpError = new MyErrorLog<SQLException>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.deleteMusicEntries",
			//          "deleting music entries");
			//      errExcpError = null;
			blIsSuccessful = false;
		}// end try/catch (Exception error)
		catch (Exception error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.deleteMusicEntries",
			//          "deleting music entries");
			//      errExcpError = null;
			blIsSuccessful = false;
		}// end try/catch (Exception error)
		finally {
			this.sqliteDBObj.endTransaction();
		}

		return blIsSuccessful;
	}// end deleteMusicEntries(String strAcctName)

	/**
	 * Delete the Music Entry per the given rowID
	 * 
	 * @param rowId
	 *          rowId of the Music Entry in the Music table to be deleted
	 * 
	 * @return true if the delete is successful, false otherwise.
	 * 
	 * @throws SQLException
	 *           if the SQL scripts encounter issues
	 */
	protected boolean deleteAccelDataEntry(long rowId) throws SQLException {
		// delete a single music entry in the music table
		boolean blIsSuccessful = false;

		this.sqliteDBObj.beginTransaction();
		try {
			Cursor crsrListRowData;

			crsrListRowData = fetchAccelDataListEntry(rowId);

			if (crsrListRowData.getCount() >= 0) {
				blIsSuccessful = this.sqliteDBObj.delete(
						MyAppDbAdapter.TABLE_CREDENTIAL, MyAppDbAdapter.KEY_ROWID + "="
								+ rowId, null) > 0;
			} else {
				blIsSuccessful = true;
			}

			crsrListRowData.close();
			crsrListRowData = null;

			if (blIsSuccessful == true) {
				this.sqliteDBObj.setTransactionSuccessful();
			}
		}// end try
		catch (SQLException error) {
			//      MyErrorLog<SQLException> errExcpError = new MyErrorLog<SQLException>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.deleteMusicEntry",
			//          "deleting music entry");
			//      errExcpError = null;
			blIsSuccessful = false;
		}// end try/catch (Exception error)
		catch (Exception error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.deleteMusicEntry",
			//          "deleting music entry");
			//      errExcpError = null;
			blIsSuccessful = false;
		}// end try/catch (Exception error)
		finally {
			this.sqliteDBObj.endTransaction();
		}

		return blIsSuccessful;
	}// end deleteMusicEntry(long rowId)

	/********************************************************************************/

	/**
	 * createLoginEntry: Creates a new entry in the database table
	 * 
	 * @return True if the database insert succeeds
	 * @throws SQLException
	 *           if entry could not be found/retrieved
	 */
	protected Boolean createLoginEntry(String uid, String name, String email,
			String password, String accessToken,  String loginState) {
		boolean blIsSuccessful = false;

		this.sqliteDBObj.beginTransaction();
		try {
			ContentValues initialValues = new ContentValues();

			initialValues.put(MyAppDbAdapter.KEY_USER_ID, uid);
			initialValues.put(MyAppDbAdapter.KEY_NAME, name);
			initialValues.put(MyAppDbAdapter.KEY_EMAIL, email);
			initialValues.put(MyAppDbAdapter.KEY_PASSWORD, password);
			initialValues.put(MyAppDbAdapter.KEY_ACCESS_TOKEN, accessToken);
			initialValues.put(MyAppDbAdapter.KEY_LOGIN_STATE, loginState);

			blIsSuccessful = (this.sqliteDBObj.insert(
					MyAppDbAdapter.TABLE_CREDENTIAL, null, initialValues) != -1);

			if (blIsSuccessful == true) {
				this.sqliteDBObj.setTransactionSuccessful();
			}
		}// end try
		catch (SQLException error) {
			//      MyErrorLog<SQLException> errExcpError = new MyErrorLog<SQLException>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.createMusicEntry",
			//          "creating music entry");
			//      errExcpError = null;
			blIsSuccessful = false;
		}// end try/catch (Exception error)
		catch (Exception error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.createMusicEntry",
			//          "creating music entry");
			//      errExcpError = null;
			blIsSuccessful = false;
		}// end try/catch (Exception error)
		finally {
			this.sqliteDBObj.endTransaction();
		}

		return blIsSuccessful;
	}// end createMusicEntry

	/**
	 * updateMusicEntry: Updates an existing entry in the database table
	 * 
	 * @return True if the database update succeeds
	 * @throws SQLException
	 *           if entry could not be found/retrieved
	 */
	protected boolean updateLoginEntry(Long rowId, String name, String email,
			String password, String accessToken,  String loginState) {
		boolean blIsSuccessful = false;

		this.sqliteDBObj.beginTransaction();
		try {
			ContentValues initialValues = new ContentValues();

			//      initialValues.put(MyAppDbAdapter.KEY_USER_ID, uid);
			initialValues.put(MyAppDbAdapter.KEY_NAME, name);
			initialValues.put(MyAppDbAdapter.KEY_EMAIL, email);
			initialValues.put(MyAppDbAdapter.KEY_PASSWORD, password);
			initialValues.put(MyAppDbAdapter.KEY_ACCESS_TOKEN, accessToken);
			initialValues.put(MyAppDbAdapter.KEY_LOGIN_STATE, loginState);

			blIsSuccessful = (this.sqliteDBObj.update(
					MyAppDbAdapter.TABLE_CREDENTIAL, initialValues, MyAppDbAdapter.KEY_ROWID
					+ "=" + rowId, null) != -1);

			if (blIsSuccessful == true) {
				this.sqliteDBObj.setTransactionSuccessful();
			}
		}// end try
		catch (SQLException error) {
			//      MyErrorLog<SQLException> errExcpError = new MyErrorLog<SQLException>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.updateMusicEntry",
			//          "updating music entry information");
			//      errExcpError = null;
			blIsSuccessful = false;
		}// end try/catch (Exception error)
		catch (Exception error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.updateMusicEntry",
			//          "updating music entry information");
			//      errExcpError = null;
			blIsSuccessful = false;
		}// end try/catch (Exception error)
		finally {
			this.sqliteDBObj.endTransaction();
		}

		return blIsSuccessful;
	}// end updateMusicEntry

	/**
	 * Delete the entries
	 * 
	 * 
	 * @return true if deleted, false otherwise
	 * 
	 * @throws SQLException
	 *           if the SQL scripts encounter issues
	 */
	protected boolean deleteLoginEntries() throws SQLException {
		// delete entries
		boolean blIsSuccessful = false;

		this.sqliteDBObj.beginTransaction();
		try {
			blIsSuccessful = this.sqliteDBObj.delete(
					MyAppDbAdapter.TABLE_CREDENTIAL, "1", null) > 0;
					if (blIsSuccessful == true) {
						this.sqliteDBObj.setTransactionSuccessful();
					}
		}// end try
		catch (SQLException error) {
			//      MyErrorLog<SQLException> errExcpError = new MyErrorLog<SQLException>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.deleteMusicEntries",
			//          "deleting music entries");
			//      errExcpError = null;
			blIsSuccessful = false;
		}// end try/catch (Exception error)
		catch (Exception error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.deleteMusicEntries",
			//          "deleting music entries");
			//      errExcpError = null;
			blIsSuccessful = false;
		}// end try/catch (Exception error)
		finally {
			this.sqliteDBObj.endTransaction();
		}

		return blIsSuccessful;
	}// end deleteMusicEntries(String strAcctName)

	/**
	 * Delete the Music Entry per the given rowID
	 * 
	 * @param rowId
	 *          rowId of the Music Entry in the Music table to be deleted
	 * 
	 * @return true if the delete is successful, false otherwise.
	 * 
	 * @throws SQLException
	 *           if the SQL scripts encounter issues
	 */
	protected boolean deleteLoginEntry(long rowId) throws SQLException {
		// delete a single music entry in the music table
		boolean blIsSuccessful = false;

		this.sqliteDBObj.beginTransaction();
		try {
			Cursor crsrListRowData;

			crsrListRowData = fetchLoginListEntry(rowId);

			if (crsrListRowData.getCount() >= 0) {
				blIsSuccessful = this.sqliteDBObj.delete(
						MyAppDbAdapter.TABLE_CREDENTIAL, MyAppDbAdapter.KEY_ROWID + "="
								+ rowId, null) > 0;
			} else {
				blIsSuccessful = true;
			}

			crsrListRowData.close();
			crsrListRowData = null;

			if (blIsSuccessful == true) {
				this.sqliteDBObj.setTransactionSuccessful();
			}
		}// end try
		catch (SQLException error) {
			//      MyErrorLog<SQLException> errExcpError = new MyErrorLog<SQLException>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.deleteMusicEntry",
			//          "deleting music entry");
			//      errExcpError = null;
			blIsSuccessful = false;
		}// end try/catch (Exception error)
		catch (Exception error) {
			//      MyErrorLog<Exception> errExcpError = new MyErrorLog<Exception>(
			//          this.ctxContext);
			//      errExcpError.addToLogFile(error, "MyAppDbAdapter.deleteMusicEntry",
			//          "deleting music entry");
			//      errExcpError = null;
			blIsSuccessful = false;
		}// end try/catch (Exception error)
		finally {
			this.sqliteDBObj.endTransaction();
		}

		return blIsSuccessful;
	}// end deleteMusicEntry(long rowId)

	/**********************************************************************************/
}// end MyAppDbSQL class
