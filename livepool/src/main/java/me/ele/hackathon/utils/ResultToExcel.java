package me.ele.hackathon.utils;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将结果输入法哦excel
 *
 * @author oukailiang
 * @create 2016-10-24 上午10:34
 */

public class ResultToExcel {
    private static final Logger log = LoggerFactory.getLogger(ResultToExcel.class);

    /**
     * 将一行数据写到指定的excel文件中，不覆盖原来数据
     *
     * @param resultArr   需要写入文件的数组数据
     * @param outFilePath 需要输出到的excel文件
     */
    public static void arrayToExcel(String[] resultArr, String outFilePath) {

        try {
            WritableWorkbook workbook = createWorkbook(outFilePath);
            WritableSheet sheet = workbook.getSheet(0);
            int row = sheet.getRows();
            int len = resultArr.length;
            for (int col = 0; col < len; col++) {
                sheet.addCell(new Label(col, row, resultArr[col]));
            }
            //把创建的内容写入到输出流中，并关闭输出流
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 将一行数据写到指定的excel文件中，不覆盖原来数据
     *
     * @param resultList  需要写入文件的一行数据数据list
     * @param outFilePath 需要输出到的excel文件
     */
    public static void listToExcel(List<String> resultList, String outFilePath) {

        try {
            WritableWorkbook workbook = createWorkbook(outFilePath);
            WritableSheet sheet = workbook.getSheet(0);
            int row = sheet.getRows();
            int len = resultList.size();
            for (int col = 0; col < len; col++) {
                sheet.addCell(new Label(col, row, resultList.get(col)));
            }
            //把创建的内容写入到输出流中，并关闭输出流
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 将集合数组数据写到指定的excel文件中，不覆盖原来数据
     *
     * @param resultList  需要写入文件的集合数组数据
     * @param outFilePath 需要输出到的excel文件
     */
    public static void listArrayToExcel(List<String[]> resultList, String outFilePath) {

        try {

            WritableWorkbook workbook = createWorkbook(outFilePath);
            int sheetNum = workbook.getSheets().length;
            WritableSheet sheet = workbook.getSheet(sheetNum - 1);
            int size = resultList.size();
            //原来文件中有的行数
            int rows = sheet.getRows();

            for (int row = 0; row < size; row++) {
                if (rows >= 65535) {
                    workbook.createSheet("Sheet" + (sheetNum + 1), sheetNum);
                    sheet = workbook.getSheet(sheetNum);
                    rows = sheet.getRows();
                }
                String[] resultArr = resultList.get(row);
                int len = resultArr.length;
                for (int col = 0; col < len; col++) {
                    sheet.addCell(new Label(col, rows, resultArr[col]));
                }
                rows++;
            }
            //把创建的内容写入到输出流中，并关闭输出流
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static WritableWorkbook createWorkbook(String outFilePath) {
        Workbook wb;
        WritableWorkbook workbook = null;
        try {

            File file = new File(outFilePath);
            //文件不存在需要创建
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                workbook = Workbook.createWorkbook(file);
                workbook.createSheet("Sheet1", 0);
            } else {
                wb = Workbook.getWorkbook(file);
                workbook = Workbook.createWorkbook(file, wb);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (BiffException e) {
            log.error(e.getMessage());
        }
        return workbook;
    }

    public static void readExcelToTxt(String excelPath, String txtPath) {
        Workbook wb;
        WritableWorkbook workbook = null;
        try {

            File file = new File(txtPath);
            //文件不存在需要创建
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter br = new BufferedWriter(fileWriter);

            wb = Workbook.getWorkbook(new File(excelPath));
            workbook = Workbook.createWorkbook(file, wb);
            StringBuffer sb = new StringBuffer();
            Sheet sheet = workbook.getSheet(0);
            int rows = sheet.getRows();
            int cols = sheet.getColumns();
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    System.out.print(sheet.getCell(col, row).getContents() + "  ");
                    sb.append(sheet.getCell(col, row).getContents()).append(" ");
                }
                sb.append("\n");
                System.out.println();
            }

            br.write(sb.toString());
            br.flush();
            System.out.println(rows + "=" + cols);

        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (BiffException e) {
            log.error(e.getMessage());
        }

    }

    /**
     * @param resultArr
     * @param workbook
     */
    public static void arrayToExcel(String[] resultArr, WritableWorkbook workbook) {

        try {
            //获得Sheet
            WritableSheet sheet = workbook.getSheet(0);
            int len = resultArr.length;
            int row = sheet.getRows();
            for (int col = 0; col < len; col++) {
                sheet.addCell(new Label(col, row, resultArr[col]));
            }
            //把创建的内容写入到输出流中，并关闭输出流
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        String outFilePath = "/Users/oukailiang/Desktop/excel.xls";
//        String[] arrayResult = {"1.3", "3.3"};
//        List<String[]> listArr = new ArrayList<>();
//        for (int num = 0; num < 600000; num++) {
//            arrayResult[0] = num + "";
//            //arrayToExcel(arrayResult, outFilePath);
//            listArr.add(arrayResult);
//            System.out.println(num);
//        }
//
//        listArr.add(arrayResult);
//        listArrayToExcel(listArr, outFilePath);
        readExcelToTxt("/Users/oukailiang/Desktop/excel.xls", "/Users/oukailiang/Desktop/rst_order_sum_avg.txt");
    }
}
