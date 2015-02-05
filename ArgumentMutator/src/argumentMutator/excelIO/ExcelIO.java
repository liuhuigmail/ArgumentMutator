package argumentMutator.excelIO;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;

import argumentMutator.global.Config;

public class ExcelIO {
	
	
	public  static void writeMutationResults(List<argumentMutator.model.ResultModel> mutationResults){
		IPath path = Platform.getLocation().append(Config.EXCEL_MUTATOR_NAME);
		String filePath = path.toOSString();
		
		try {
			FileOutputStream output = new FileOutputStream(filePath);
			
			HSSFWorkbook workbook = new HSSFWorkbook();
			for(int sheetIndex = 0; sheetIndex < 1; sheetIndex++){
				HSSFSheet sheet = workbook.createSheet("sheet" + sheetIndex);				
				for(int rowIndex = 0; rowIndex < mutationResults.size() + 1; rowIndex++){
					HSSFRow row = sheet.createRow(rowIndex);
					if(rowIndex == 0){
						row.createCell(0).setCellValue("File");
						row.createCell(1).setCellValue("Before mutation");
						row.createCell(2).setCellValue("After mutation");
					} else{
						argumentMutator.model.ResultModel mutationResult = mutationResults.get(rowIndex-1);
						row.createCell(0).setCellValue(mutationResult.path.toString());												
						row.createCell(1).setCellValue(mutationResult.expression);												
						row.createCell(2).setCellValue(mutationResult.mutation);
					}
					
					output.flush();
				}
			}
			
			workbook.write(output);
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
