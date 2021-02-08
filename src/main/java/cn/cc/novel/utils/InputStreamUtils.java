package cn.cc.novel.utils;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 *  这个类只做 InputStream 的工具类
 * @author c.c.
 * @date 2020/12/9
 */
public class InputStreamUtils {

    public static String inputStreamStr(InputStream inputStream,String charSet,boolean gzip)throws Exception {
        if(gzip){
            return ZipUtils.gzipRestore(inputStream);
        }
        BufferedReader br = null;
        // 默认字符编码GBK
        br = new BufferedReader(new InputStreamReader(inputStream,charSet));
        String readLine;
        StringBuilder builder = new StringBuilder();
        while ((readLine = br.readLine()) != null) {
            // 一次读一行，所以我也要换行
            builder.append(readLine + "\r\n");
        }
        String result = builder.toString();
        return result;
    }

    /**
     * 根据inputStream获得输入的字符串,带编码
     * @param inputStream 输入流
     * @param charSet 字符编码
     * @return
     * @throws Exception
     */
    public static String inputStreamStr(InputStream inputStream,String charSet)throws Exception{
        return inputStreamStr(inputStream,charSet,false);
    }

}
