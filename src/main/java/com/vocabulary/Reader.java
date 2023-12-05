package com.vocabulary;

import java.util.List;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

public class Reader implements LazyLoad {
	
	private final String path;
	private final DiffrentNumbers dn;
	private int[] generatedNums;
	
	public Reader(String path, int quantity) {
		this.path = path;
		int num = checkNumOfRows();
		dn = new DiffrentNumbers(1, num, quantity);
	}
	
	@Override
	public List<Word> lazyRead() {
		generatedNums = dn.generate();
		List<Word> list = new ArrayList<>();
		try (final FileInputStream fis = new FileInputStream(path);
		Workbook workbook = new XSSFWorkbook(fis);) {
			Sheet sheet = workbook.getSheetAt(0);
			Row row;
			String[] elements = new String[3];
			for (int i = 0; i < generatedNums.length; i++) {
				row = sheet.getRow(generatedNums[i]);
				for (int j = 0; j < elements.length; j++) {
					elements[j] = getNumberFromCell(row.getCell(j));
				}
				list.add(new Word(elements[0], elements[1].split(", "), Integer.parseInt(elements[2])));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	@Override
	public void lazyWrite(List<Word> list) {
		if (generatedNums != null) {
		try (final FileInputStream fis = new FileInputStream(path);
		Workbook workbook = new XSSFWorkbook(fis);) {
			Sheet sheet = workbook.getSheetAt(0);
			Cell cell;
			for (int i = 0; i < generatedNums.length; i++) {
				cell = sheet.getRow(generatedNums[i]).getCell(2);
				if (!getNumberFromCell(cell).equals(list.get(i).getHitAsString())) {
					cell.setCellValue(list.get(i).getHitAsString());
				}
			}
			try (FileOutputStream fos = new FileOutputStream(path)) {
				workbook.write(fos);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		}
	}
	
	@Override
	public List<Word> lazyReadAndWrite(List<Word> list) {
		try (final FileInputStream fis = new FileInputStream(path);
		Workbook workbook = new XSSFWorkbook(fis);) {
			Sheet sheet = workbook.getSheetAt(0);
			Row row;
			Cell cell;
			// Write first
			for (int i = 0; i < generatedNums.length; i++) {
				cell = sheet.getRow(generatedNums[i]).getCell(2);
				if (!cell.getStringCellValue().equals(list.get(i).getHitAsString())) {
					cell.setCellValue(list.get(i).getHitAsString());
				}
			}
			// After write we Read the new words
			list.clear();
			generatedNums = dn.generate();
			String[] elements = new String[3];
			for (int i = 0; i < generatedNums.length; i++) {
				row = sheet.getRow(generatedNums[i]);
				for (int j = 0; j < elements.length; j++) {
					elements[j] = getNumberFromCell(row.getCell(j));
				}
				list.add(new Word(elements[0], elements[1].split(", "), Integer.parseInt(elements[2])));
			}
			try (FileOutputStream fos = new FileOutputStream(path)) {
				workbook.write(fos);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	public List<Word> readAllWords() {
		List<Word> list = new ArrayList<>(5000);
		try (final FileInputStream fis = new FileInputStream(path);
		Workbook workbook = new XSSFWorkbook(fis);) {
			Sheet sheet = workbook.getSheetAt(0);
			boolean firstRow = true;
			Row row;
			String word;
			String meanings;
			String numOfHits;
			Iterator<Row> rowIterator = sheet.rowIterator();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				if (firstRow) {
					firstRow = !firstRow;
					continue;
				}
				word = row.getCell(0).getStringCellValue();
				meanings = row.getCell(1).getStringCellValue();
				numOfHits = getNumberFromCell(row.getCell(2));
				list.add(new Word(word, meanings.split(", "), Integer.parseInt(numOfHits)));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	public int getSumOfHits() {
		int numOfHits = 0;
		try (final FileInputStream fis = new FileInputStream(path);
		Workbook workbook = new XSSFWorkbook(fis);) {
			Sheet sheet = workbook.getSheetAt(0);
			boolean firstRow = true;
			Row row;
			Iterator<Row> rowIterator = sheet.rowIterator();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				if (firstRow) {
					firstRow = !firstRow;
					continue;
				}
				numOfHits += Integer.parseInt(getNumberFromCell(row.getCell(2)));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return numOfHits;
	
	}
	
	public void resetHits() {
		try (final FileInputStream fis = new FileInputStream(path);
		Workbook workbook = new XSSFWorkbook(fis);) {
			Sheet sheet = workbook.getSheetAt(0);
			Row row;
			Cell cell;
			boolean firstRow = true;
			Iterator<Row> rowIterator = sheet.rowIterator();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				if (firstRow) {
					firstRow = !firstRow;
					continue;
				}
				cell = row.getCell(2);
				cell.setCellValue("0");
			}
			try (FileOutputStream fos = new FileOutputStream(path)) {
				workbook.write(fos);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private int checkNumOfRows() {
		int numberOfRows = 0;
		try (final FileInputStream fis = new FileInputStream(path);
		Workbook workbook = new XSSFWorkbook(fis);) {
			Sheet sheet = workbook.getSheetAt(0);
			numberOfRows = sheet.getPhysicalNumberOfRows();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return numberOfRows;
	}
	
	private String getNumberFromCell(Cell cell) {
		try {
			return cell.getStringCellValue();
		} catch (Exception e) {
			return Integer.toString((int)cell.getNumericCellValue());
		}
	}
	
}