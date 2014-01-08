package com.rampgreen.acceldatacollector.csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

public class Main {
	public static void main(String[] args) {

		try {
			readLineByLineExample();

			readAllExample();

			writeCSVExample();

			writeAllExample();

			mapJavaBeanExample();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void readLineByLineExample() throws IOException {
		System.out.println("\n**** readLineByLineExample ****");
		String csvFilename = "C:\\work\\sample.csv";
		CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
		String[] row = null;
		while ((row = csvReader.readNext()) != null) {
			System.out.println(row[0] + " # " + row[1] + " #  " + row[2]);
		}

		csvReader.close();
	}

	private static void readAllExample() throws IOException {
		System.out.println("\n**** readAllExample ****");
		String[] row = null;
		String csvFilename = "C:\\work\\sample.csv";

		CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
		List content = csvReader.readAll();

		for (Object object : content) {
			row = (String[]) object;

			System.out.println(row[0] + " # " + row[1] + " #  " + row[2]);
		}

		csvReader.close();

	}

	private static void writeCSVExample() throws IOException {
		System.out.println("\n**** writeCSVExample ****");

		String csv = "C:\\work\\output.csv";

		CSVWriter writer = new CSVWriter(new FileWriter(csv));

		String[] country = "India#China#United States".split("#");
		writer.writeNext(country);
		System.out.println("CSV written successfully.");
		writer.close();
	}

	private static void writeAllExample() throws IOException {
		System.out.println("\n**** writeAllExample ****");

		String csv = "C:\\work\\output2.csv";
		CSVWriter writer = new CSVWriter(new FileWriter(csv));

		List<String[]> data = new ArrayList<String[]>();
		data.add(new String[] { "India", "New Delhi" });
		data.add(new String[] { "United States", "Washington D.C" });
		data.add(new String[] { "Germany", "Berlin" });

		writer.writeAll(data);
		System.out.println("CSV written successfully.");
		writer.close();
	}

	public static void mapJavaBeanExample() throws FileNotFoundException {
		System.out.println("\n**** mapJavaBeanExample ****");

		ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
//		strat.setType(Country.class);
		String[] columns = new String[] { "countryName", "capital" }; 
		strat.setColumnMapping(columns);

		CsvToBean csv = new CsvToBean();

		String csvFilename = "C:\\work\\sample.csv";
		CSVReader csvReader = new CSVReader(new FileReader(csvFilename));

		List list = csv.parse(strat, csvReader);
		for (Object object : list) {
//			Country country = (Country) object;
//			System.out.println(country.getCapital());
		}
	}
}