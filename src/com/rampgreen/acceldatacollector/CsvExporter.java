package com.rampgreen.acceldatacollector;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rampgreen.acceldatacollector.csv.CSVUtil;
import com.rampgreen.acceldatacollector.csv.CsvModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CsvExporter extends Activity {
	// GUI controls
	private EditText txtData;
	private Button btnWriteSDFile;
	private Button btnReadSDFile;
	private Button btnClearScreen;
	private Button btnClose;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.csv_exporter);
	// bind GUI elements with local controls
	txtData = (EditText) findViewById(R.id.txtData);
	txtData.setHint("Enter some lines of data here...");

	btnWriteSDFile = (Button) findViewById(R.id.btnWriteSDFile);
	btnWriteSDFile.setOnClickListener(new OnClickListener() {

	public void onClick(View v) {
		// write on SD card file data in the text box
		try {
			CsvModel csvModel = new CsvModel();
			
			csvModel.setActivityType(Constants.ACCEL_ACTIVITY_WALKING);
			csvModel.setDuration("1.0");
			csvModel.setUserID("92350");
			csvModel.setX(23.1234f);
			csvModel.setY(23.2345f);
			csvModel.setZ(23.3456f);
			
			ArrayList<String[]> data = new ArrayList<String[]>();
			data.add(new String[] { "India", "New Delhi" });
			data.add(new String[] { "United States", "Washington D.C" });
			data.add(new String[] { "Germany", "Berlin" });
			CSVUtil.writeCSVByModel("CSV1.csv", null, csvModel);
			
//			File myFile = new File("/sdcard/mysdfile.txt");
//			myFile.createNewFile();
//			FileOutputStream fOut = new FileOutputStream(myFile);
//			OutputStreamWriter myOutWriter = 
//									new OutputStreamWriter(fOut);
//			myOutWriter.append(txtData.getText());
//			myOutWriter.close();
//			fOut.close();
			Toast.makeText(getBaseContext(),
					"Done writing SD 'CSV1.csv'",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
	}// onClick
	}); // btnWriteSDFile

		btnReadSDFile = (Button) findViewById(R.id.btnReadSDFile);
		btnReadSDFile.setOnClickListener(new OnClickListener() {

		public void onClick(View v) {
			// write on SD card file data in the text box
		try {
			CSVUtil.readCSVByModel("CSV1.csv", CsvModel.getColumnForCSV(), CsvModel.class);
			File myFile = new File("/sdcard/CSV1.csv");
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}
			txtData.setText(aBuffer);
			myReader.close();
			Toast.makeText(getBaseContext(),
					"Done reading SD 'mysdfile.txt'",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
		}// onClick
		}); // btnReadSDFile

		btnClearScreen = (Button) findViewById(R.id.btnClearScreen);
		btnClearScreen.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// clear text box
				txtData.setText("");
			}
		}); // btnClearScreen

		btnClose = (Button) findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// clear text box
				finish();
			}
		}); // btnClose

	}// onCreate

}// AndSDcard

