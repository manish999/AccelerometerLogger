package com.rampgreen.acceldatacollector.csv;

import com.rampgreen.acceldatacollector.util.AppLog;

import android.os.Environment;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil
{
	//  
	public static void writeCSVFile(String filePath, String content) {
		CSVWriter writer = null;
		try 
		{
			writer = new CSVWriter(new FileWriter("/sdcard/myfile.csv"), ',');
			String[] entries = "first#second#third".split("#"); // array of your values
			writer.writeNext(entries);  
			writer.close();
		} 
		catch (IOException e)
		{
			//error
			AppLog.e(e.getMessage());
		}
	}

	/**
	 * @param filePath csv file full path like /media/New Volume/Android/workspace_android_RTNeuro/OpenCSVJavaBeanTest/sample.csv
	 * @param columnArray it will be the array of the fields to bind do in your JavaBean
	 * @param model JavaBean Model
	 * @throws FileNotFoundException
	 */
	public static void readCSVByModel(String filePath, String[] columnArray, Class<?> model) throws FileNotFoundException {
		File root = Environment.getExternalStorageDirectory();
		File csvFile = new File(root, filePath);
		
		ColumnPositionMappingStrategy<Object> strat = new ColumnPositionMappingStrategy<Object>();
		strat.setType(Object.class);
		//		 String[] columns = new String[] {"countryName", "capital"}; // the fields to bind do in your JavaBean
		strat.setColumnMapping(columnArray);

		CsvToBean<Object> csv = new CsvToBean<Object>();

		String csvFilename = filePath;//"/media/New Volume/Android/workspace_android_RTNeuro/OpenCSVJavaBeanTest/sample.csv";
		CSVReader csvReader = new CSVReader(new FileReader(csvFile));

		List list = csv.parse(strat, csvReader);
		for (Object object : list) {
			Object country = object;
			//		     System.out.println(country.getCapital());
		}

	}
	
	public static void writeCSVByModel(String filePath, ArrayList<String[]> writtenData, CsvModel model) throws IOException {
		System.out.println("\n**** writeAllExample ****");
		File root = Environment.getExternalStorageDirectory();
		File csvFile = new File(root, filePath);
//		String csv = filePath;//"C:\\work\\output2.csv";
		CSVWriter writer = new CSVWriter(new FileWriter(csvFile));

		List<String[]> data = new ArrayList<String[]>();
		
		if(writtenData == null) {
			data.add(model.toCSV().split(","));
		} else {
			data = writtenData;
		}
//		data.add(new String[] { "India", "New Delhi" });
//		data.add(new String[] { "United States", "Washington D.C" });
//		data.add(new String[] { "Germany", "Berlin" });

		writer.writeAll(data);
		System.out.println("CSV written successfully.");
		writer.close();
	}
	
	 /**
     * Round to certain number of decimals
     * 
     * @param d
     * @param decimalPlace
     * @return
     */
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
