package main.core.old;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class XSLReader {

	public static void main(String[] args) {
		JSONObject temp = new JSONObject();
		JSONArray arr = new JSONArray();
		System.out.println(System.getProperty("user.dir"));
		File myFile = new File("C:\\Users\\Jon Cornado\\OneDrive\\eclipse-workspace\\MIP\\AF Export.xls");
		File newFile = new File("C:\\Users\\Jon Cornado\\OneDrive\\eclipse-workspace\\MIP\\test_renmae.xls");
		FileInputStream fis;
		HSSFWorkbook myWorkBook = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date date = null;
		try {
			fis = new FileInputStream(myFile);
			myWorkBook = new HSSFWorkbook(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Finds the workbook instance for XLSX file
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Return first sheet from the XLSX workbook
		HSSFSheet mySheet = myWorkBook.getSheetAt(0);
		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = mySheet.iterator();
		// Traversing over each row of XLSX file
		int i = 0;

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			// For each row, iterate through each columns
			Iterator<Cell> cellIterator = row.cellIterator();
			i++;
			int z = 0;
			if (i == 10)
				break;

			while (cellIterator.hasNext()) {
				z++;

				Cell currentCell = cellIterator.next();
				if (z == 1 && i > 1) {
					try {
						date = sdf.parse(currentCell.getStringCellValue());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("date is " + date);
				}
				// getCellTypeEnum shown as deprecated for version 3.15
				// getCellTypeEnum ill be renamed to getCellType starting from version 4.0
				if (currentCell.getCellTypeEnum() == CellType.STRING) {
					System.out.println(currentCell.getStringCellValue() + "--");
				} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
					System.out.println(currentCell.getNumericCellValue() + "--");
				}

			}
			System.out.println();
			System.out.println("");
		}

		// myFile.renameTo(newFile);
	}

}
