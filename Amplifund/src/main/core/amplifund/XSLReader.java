package main.core.amplifund;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import com.main.business.Constants;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import main.core.AMPLIFUND;
import main.core.Plugin.statusType;
/**
 * 
 * @author Jon Cornado
 * 
 * Reads A excel file that has expense details. All the required fields are gatherd and sent to amplifund plugin.
 * This class parses the excel file in a structured manner and it is very important for the excel file to maintain the same structure
 *
 */
public class XSLReader {

	private String location = "";// "C:\\Users\\Jon Cornado\\OneDrive\\eclipse-workspace\\MIP\\AF Export.xls";
	private String date = "";
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	Connection connection = AMPLIFUND.conn;
	PreparedStatement ps;
	ResultSet rs = null;
	// select * from upd_string('Test String');
	// select * from get_string();
/**
 * Method to read excel file in a specific manner.
 * The first line in the file usually has headers that is directly read and saved, but not used.
 * The actual records start after this. We loop through all the records.
 * The code initially checks if the first field ("02/26/16 05:57:18 PM") is of this structure else it skips to next record.
 * If the given date is matches with the sync date in database then we stop reading the file and send the records created so far to Amplifund plugin.
 * Excel file has two fields "Debit" "Credit" which are merged into one field where Debit is +ve and Credit is -ve amount
 */
	public void readDocument() {
		BatchProcess.process();
		System.out.println("starting read process");
		location = Constants.LOCATION;

		try {
			ps = connection.prepareStatement("select * from get_string()");
			rs = ps.executeQuery();
			rs.next();
			String temp = rs.getString(1);
			temp = null==rs.getString(1)?"":temp;
			System.out.println("displaying date from db" + rs.getString(1));
			if ((temp.contains("AM") || temp.contains("PM")) && temp.contains(":")) {
				date = temp;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ArrayList<String> header = new ArrayList<>();
		ArrayList<String> temp = new ArrayList<>();
		ArrayList<ArrayList<String>> values = new ArrayList<>();
		System.out.println("trying to read file at location " + location);
		File file = new File(location);
		System.out.println("starting document read process " + file.getPath());
		if (file.exists()) {

			System.out.println("working on file");
			HSSFWorkbook myWorkBook = null;
			try {
				FileInputStream fis = new FileInputStream(file);
				System.out.println("location " + file.getAbsolutePath());
				myWorkBook = new HSSFWorkbook(fis);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HSSFSheet mySheet = myWorkBook.getSheetAt(0);
			Iterator<Row> rowIterator = mySheet.iterator();
			System.out.println("starting loop");
			//loop that saves all hearders
			if (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				int position = 0;
				while (cellIterator.hasNext()) {
					position++;
					Cell currentCell = cellIterator.next();
					switch (currentCell.getCellTypeEnum()) {
					case STRING: {
						if (currentCell.getStringCellValue().equalsIgnoreCase("Debit"))
							header.add("Amount");
						else if (currentCell.getStringCellValue().equals("Credit")) {
						} else {
							header.add(currentCell.getStringCellValue());
						}
						// System.out.println(currentCell.getStringCellValue());
						break;
					}
					default: {
						break;

					}
					}

				}
				// System.out.println(header);
			}
			values.add(header);
			// loop that reads records
			outerloop: while (rowIterator.hasNext()) {
				int position = 1;
				boolean startRow = false;
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				System.out.println("--------------------------------------------");
				while (cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();
					System.out.println(position + " " + currentCell.getCellTypeEnum());
					//We read the modified date field, if it dosent match few conditions then we simply skip to next record
					if (!startRow) {
						if (currentCell.getCellTypeEnum() == CellType.STRING) {
							String value = currentCell.getStringCellValue();
							if ((value.contains("AM") || value.contains("PM")) && value.contains(":")) {
								if (value.equalsIgnoreCase(date))
									break outerloop;
								temp.add(value);
								System.out.println(position + "---" + value);
							} else {
								continue;
							}
							// position++;
							startRow = true;
						}
						
					} else {
						//Usually cells from 1 through 5 are of string type, this block directly saves them
						//Cell 8 falls in this condition
						if (currentCell.getCellTypeEnum() == CellType.STRING) {
							temp.add(currentCell.getStringCellValue());
							System.out.println(position + "---" + currentCell.getStringCellValue());
							// position++;
						}
						//2nd cell can be empty and this field stores an empty field
						if (position == 2 && currentCell.getCellTypeEnum() == CellType.BLANK) {
							temp.add("");
							System.out.println(position + "---" + " ");
							// position++;
						}
						//amount is saved here based
						if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
							// System.out.println("IN AMOUNT SECTION");
							if (position == 6) {
								System.out.println("IN AMOUNT +VE SECTION");
								temp.add(String.valueOf(currentCell.getNumericCellValue()));
							}
							if (position == 7) {
								System.out.println("IN AMOUNT -VE SECTION");
								temp.add("-"+String.valueOf(currentCell.getNumericCellValue()));
							}
							System.out.println(position + "---" + currentCell.getNumericCellValue());
							// position++;
						}
					}

					position++;

				}
				if (temp.size() > 1) {
					values.add(temp);
					// System.out.println(temp);
				}
				startRow = false;
				temp = new ArrayList<>();
				// System.out.println("-------------------------------------------------");
			}
			System.out.println(values);
			BatchProcess.process();
			if (values.size() > 1) {
				changeFileName(file);
				//AMPLIFUND.setinterfaceData("AMPLIFUND", values);
				AMPLIFUND.expense(values);

			}

		}
		else {
			System.out.println("file is not available for amplifund");
		}

	}

	public static void changeFileName(File file) {
		String name = file.getName();
		String fname = name.substring(0, name.length() - 4) + "-" + dateFormat.format(new Date())
				+ name.substring(name.length() - 4);
		System.out.println("printing location" + file.getParent());
		File temp = new File(file.getParent() + "//" + fname);
		file.renameTo(temp);
	}

	public void changeLocation(String location) {

		this.location = location;

	}

	public static void main(String[] args) {
		new XSLReader().readDocument();

	}

}
