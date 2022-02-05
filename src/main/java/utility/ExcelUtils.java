package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

    private static XSSFSheet ExcelWSheet;
    private static XSSFWorkbook ExcelWBook;
    private static XSSFCell cell;
    private static XSSFRow row;

    public static String getPath() {
        String path = "";
        File file = new File("");
        String absolutePathOfFirstFile = file.getAbsolutePath();
        path = absolutePathOfFirstFile.replaceAll("\\\\+", "/");
        return path;
    }

    public static Object[][] getTableArray(String FilePath, String SheetName) throws Exception {
        String[][] tabArray = null;
        try {
            FileInputStream ExcelFile = new FileInputStream(getPath() + "/src/test/resources/testData/" + FilePath);

            ExcelWBook = new XSSFWorkbook(ExcelFile);
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
            int startRow = 1;
            int startCol = 0;
            int ci, cj;

            int totalRows = ExcelWSheet.getLastRowNum();
            row = ExcelWSheet.getRow(0);
            int totalCols = row.getLastCellNum();
            tabArray = new String[totalRows][totalCols];
            ci = 0;
            for (int i = startRow; i <= totalRows; i++, ci++) {
                cj = 0;
                for (int j = startCol; j < totalCols; j++, cj++) {
                    tabArray[ci][cj] = getCellData(i, j);
                    System.out.println(tabArray[ci][cj]);
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not read the Excel sheet");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("Could not read the Excel sheet");
            e.printStackTrace();
        }
        return (tabArray);

    }

    public static String getCellData(int RowNum, int ColNum) throws Exception {
        try {
            cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
            if (cell.getCellType().getCode() == 1){
                return cell.getStringCellValue();
            }
            else if(cell.getCellType().getCode() == 0){
                return String.valueOf(cell.getNumericCellValue());
            }
            else
                return cell.getStringCellValue();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw (e);
        }
    }

    public String getSingleCellData(String columnName, int row, String sheet) throws Exception {
        int count=0;
        String val = "";
        try {
            FileInputStream ExcelFile = new FileInputStream(getPath() + "/src/test/resources/testData/testdata.xlsx");

            ExcelWBook = new XSSFWorkbook(ExcelFile);
            ExcelWSheet = ExcelWBook.getSheet(sheet);

            this.row = ExcelWSheet.getRow(0);
            int totalCols = this.row.getLastCellNum();

            for(int c=0;c<totalCols;c++){
                cell = ExcelWSheet.getRow(0).getCell(c);
                if (cell.getCellType().getCode() == 1){
                    val = cell.getStringCellValue();
                }
                else if(cell.getCellType().getCode() == 0){
                    val = String.valueOf(cell.getNumericCellValue());
                }
                else
                    val = cell.getStringCellValue();
                if(val.equals(columnName)){
                    count = c;
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not read the Excel sheet");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("Could not read the Excel sheet");
            e.printStackTrace();
        }
        cell = ExcelWSheet.getRow(row).getCell(count);
        if (cell.getCellType().getCode() == 1){
            val = cell.getStringCellValue();
        }
        else if(cell.getCellType().getCode() == 0){
            val = String.valueOf(cell.getNumericCellValue());
        }
        else
            return cell.getStringCellValue();
        return String.valueOf(val);
    }
}
