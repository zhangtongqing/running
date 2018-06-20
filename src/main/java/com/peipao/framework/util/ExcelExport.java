package com.peipao.framework.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * excel数据导出
 *
 * @author Meteor.wu
 * @since 2017/10/20 11:29
 */

public class ExcelExport {
    public static String createExcel(HttpServletResponse response, HttpServletRequest request, String filePrfixName, HSSFWorkbook wbarray) throws Exception {
        String str = DateUtil.dateToStr(Calendar.getInstance().getTime(), DateUtil.DATE_TIME_NO_SLASH);
        String realPath = request.getSession().getServletContext().getRealPath("/");


        realPath = realPath.substring( 0, realPath.length() - 1 );
        String downloadfile = realPath + "/download";

        File root = new File( downloadfile );
        if ( !root.exists() ) {
            root.mkdirs();
        }
        deleteSameFile(root);

        //String foldername = realPath + "\\download\\" + "Excel_" + str; //window下
        String foldername = realPath + "/download/" + "Excel_" + str; //unix下
        File excelfile = new File( foldername );
        if ( !excelfile.exists() ) {
            excelfile.mkdirs();
        }
        OutputStream outf = new FileOutputStream( foldername + "/" + filePrfixName+ "_" + str + ".xls" );//unix下
        //OutputStream outf = new FileOutputStream( foldername + "\\" + filePrfixName+ "_" + str + ".xls" );//window下
        wbarray.write( outf );
        outf.close();

        String zipfilename = foldername + ".zip";
        File zipFile = new File( zipfilename );
        ZipUtil.zip( zipFile, foldername );

        deleteDirectory( foldername ); // 删除当时文件夹

        response.setContentType( "text/html; charset=UTF-8" );

        String zipfilename1 = "Excel_" + str + ".zip";
        return "/download/"+ zipfilename1;//unix下
    }

    private static void deleteSameFile(File root) {
        File[] files = root.listFiles();
        String temp;
        String strsub = DateUtil.dateToStr(Calendar.getInstance().getTime(), DateUtil.NO_SLASH);
        String zipfilenamesub = "Excel_" + strsub;
        for ( File file : files ) {
            if ( file.isFile() ) {
                temp = file.getName();
                if ( !temp.startsWith( zipfilenamesub ) ) {
                    file.delete();
                }
            }
        }
    }

    private static boolean deleteDirectory(String sPath) {
        boolean flag = false;
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if ( !sPath.endsWith( File.separator ) ) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File( sPath );
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ( !dirFile.exists() || !dirFile.isDirectory() ) {
            return false;
        }
        flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for ( int i = 0; i < files.length; i++ ) {
            // 删除子文件
            if ( files[i].isFile() ) {
                flag = deleteFile( files[i].getAbsolutePath() );
                if ( !flag )
                    break;
            } // 删除子目录
            else {
                flag = deleteDirectory( files[i].getAbsolutePath() );
                if ( !flag )
                    break;
            }
        }
        if ( !flag )
            return false;
        // 删除当前目录
        return dirFile.delete();
    }

    private static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File( sPath );
        // 路径为文件且不为空则进行删除
        if ( file.isFile() && file.exists() ) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}
