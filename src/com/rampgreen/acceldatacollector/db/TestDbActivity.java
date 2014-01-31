package com.rampgreen.acceldatacollector.db;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;

import com.rampgreen.acceldatacollector.R;

public class TestDbActivity extends Activity
{

	private MyAppDbSQL dbAppDbObj;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		try{
			if (this.dbAppDbObj == null) {
				// set database query class object
				this.dbAppDbObj = new MyAppDbSQL(this);
			}// end if (this.dbMusic == null)

			// save the data to the database table
			boolean dbOpenResult = this.dbAppDbObj.openDbAdapter();

			if (dbOpenResult) {
				boolean blIsSuccessful = this.dbAppDbObj.createLoginEntry("1", "manish", "manis@gmail.com", "pass1", "token1", "1");
				boolean blIsSuccessful1 = this.dbAppDbObj.createAccelDataEntry("1", "3.44,4.33,5.444", "11 jan 2013", "112 jan 2013", "Running", "43", "20","1","3", "1");
				boolean dbCloseResult = this.dbAppDbObj.closeDbAdapter();
				if (!dbCloseResult)
					throw new Exception(
							"The database was not successfully closed.");
				if (blIsSuccessful == false && blIsSuccessful1 == false) {
					//            Music_List.objDisplayAlertClass = new MyDisplayAlertClass(
					//                Music_List.this, new CustAlrtMsgOptnListener(
					//                    MessageCodes.ALERT_TYPE_MSG), "Database Issue",
					//                "There was an issue, and the register entry data was not created.");
					//
					//            break;

				} else {
					// bgh 08/26/2010 v1.03 - get the position of the list entry
					// that was just created
					//            this.intEntryListPosition = this.getListView()
					//                .getSelectedItemPosition();

					//            break;
				}

				dbOpenResult = this.dbAppDbObj.openDbAdapter();
				Cursor  cursorTableQuery,mEntryCursor ;
				if (dbOpenResult) {
					cursorTableQuery = this.dbAppDbObj.exportTableQuery(
							MyAppDbAdapter.TABLE_CREDENTIAL, MyAppDbAdapter.KEY_NAME+" DESC");
					dbCloseResult = this.dbAppDbObj.closeDbAdapter();
				}
				      dbOpenResult = this.dbAppDbObj.openDbAdapter();

				      if (dbOpenResult) {
				        mEntryCursor = this.dbAppDbObj.fetchAccelDataAll(1545);
				        dbCloseResult = this.dbAppDbObj.closeDbAdapter();

				        this.startManagingCursor(mEntryCursor);
				        
				        if (!dbCloseResult)
				          throw new Exception("The database was not successfully closed.");

				        if(mEntryCursor.moveToFirst()) {
				        	   do {
				        	     String name = mEntryCursor.getString(mEntryCursor.getColumnIndexOrThrow(MyAppDbAdapter.KEY_ROWID));
				        	     AppLog.e("AccelTABLE DATA: "+name);
				        	   } while (mEntryCursor.moveToNext());
				        	}
//				this.mEntryCursor
//	            .getString(this.mEntryCursor
//	                .getColumnIndexOrThrow(MyAppDbAdapter.KEY_ALBUM))
				      }
			}// end if (blIsSuccessful == false)
		} catch (Exception e) {
			AppLog.printStackTrace(e);
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
