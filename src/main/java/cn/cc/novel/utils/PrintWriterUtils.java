package cn.cc.novel.utils;

import java.io.*;

/**
 * @author c.c.
 * @date 2020/12/9
 */
public class PrintWriterUtils {

    /**
     * writer,reader 系列
     *
     * 写入字符使用比较方便，例如文本
     *
     * 不处理换行，是否换行由传来的数据自行决定
     *
     * BufferedWriter 能加速?
     */


    /**
     *
     * @param filePath 文件全路径
     * @param content
     */
    public static void fileWriter(String filePath,String content) {
          fileWriter(new File(filePath),content);
    }

    // PrintWriter
    /**
     * 系统文件 文件末尾追加，不用指定编码默认UTF-8
     */
    public static void fileWriter(File f,String content) {
        FileWriter fw = null;
        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 据说 new BufferedWriter( 包装后能缓冲加速
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

        // 乱码处理
        // new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), encoding));
        // web乱码 printWriter=new PrintWriter(new OutputStreamWriter(new FileOutputStream(ndfFileName), ConstantCharSet.UTF_8));

        pw.print(content);
        pw.flush();
        try {
            fw.flush(); pw.close(); fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // BufferedWriter 没有追加
    //  可以加参数指定编码
    // new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), encoding));
    public static void IO_BufferedWriter(String filePath,String charSet,String content) {
        try {
            //FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), charSet));
            bw.append("我是程序猿");
            bw.write("我是");// 往已有的文件上添加字符串
            bw.write("程序猿\n ");
            bw.write(content);
            bw.close();
            //fw.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    /************************************************ 写 ******************************************************************/



    /************************************************ 读 ******************************************************************/

    /**
     *  文件路径
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String fileReader(String filePath)throws Exception{
        File file = new File(filePath);
        BufferedReader reader  = new BufferedReader(new FileReader(file));
        return fileReader(reader);
    }

    public static String fileReader(File file)throws Exception{
        BufferedReader reader  = new BufferedReader(new FileReader(file));
        return fileReader(reader);
    }

    /**
     *  制定编码
     *  可以加参数指定编码 new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding));
     * @param filePath
     * @param charSet
     * @return
     * @throws Exception
     */
    public static String fileReader(String filePath,String charSet)throws Exception{
        File file = new File(filePath);
        BufferedReader reader  = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charSet));
        return fileReader(reader);
    }

    // 用来读文本流
    private static String fileReader(BufferedReader reader) throws IOException {
        String str="";
        StringBuffer content = new StringBuffer("");
        while((str=reader.readLine())!=null) {
            content.append(str + "\r\n");
        }
        reader.close();
        return content.toString();
    }

    /************************************************ 读 ******************************************************************/

}
