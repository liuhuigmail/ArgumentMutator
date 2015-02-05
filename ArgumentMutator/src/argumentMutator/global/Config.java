package argumentMutator.global;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Config {

	public static final int MIN_MEMBER_NAME_LEN = 0;
	
	// similarity
	public static final double MIN_EDIT_DISTANCE_SIMILARITY = 0.0;

	public static final String EXCEL_MUTATOR_NAME = "mutator.xls";
	
	// mutation
	public static final int MUTATION_PROBABILITY = 30;
	
	
	public static List<String> excelList = read("./leftHandStatistics.xls");
	
	public static List<String> read(String filePath){		
		List<String> results = new ArrayList<String>();		
		try {
			FileInputStream input = new FileInputStream(filePath);
			HSSFWorkbook workbook = new HSSFWorkbook(input);		
			for(int sheetIndex = 0; sheetIndex < 1; sheetIndex++){
				HSSFSheet sheet = workbook.getSheet("sheet" + sheetIndex);
				
				for(int rowIndex = 1; rowIndex < 12439 + 1; rowIndex++){//ÐÞ¸Ä³¤¶È
					
					HSSFRow row = sheet.getRow(rowIndex);
					HSSFCell cell0 = row.getCell(0);
					
					String name = cell0.getStringCellValue();
					
					results.add(name);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return results;		
	}
	
	
	public static boolean contains(String str){
		int length = excelList.size();
		for(int i = 0; i < length; i++){
			if(excelList.get(i).equals(str)){
				return true;
			}
		}
		return false;
	}
	
}
