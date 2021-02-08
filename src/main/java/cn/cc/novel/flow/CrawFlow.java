package cn.cc.novel.flow;

import cn.cc.novel.model.BookModel;
import cn.cc.novel.model.ContentModel;
import cn.cc.novel.model.RootModel;
import cn.cc.novel.model.TypeModel;
import cn.cc.novel.utils.InputStreamUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.xsoup.Xsoup;
import org.apache.commons.lang.StringUtils;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author c.c.
 * @date 2021/2/1
 */
public class CrawFlow {

    private static Map map;
    private static String charset;
    private static String rootUrl;

    private static String typeUrl;
    private static boolean typeNextPageFlag = true;
    private static String typeTotalPage;

    // 网站跟
    private static RootModel rootModel;
    private static Map<String, RootModel> rootModelMap = new HashMap<>();
    private static List<String> urlList = new ArrayList<>();

    // 目录
    private static boolean chapterNextPageFlag = true;
    private static String chapterUrl;

    public static void main(String[] args) {
        try {


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    // 初始化配置列表
    public static void init() {
       /* List<RootModel> rootModelList = Novel_init.init();
        System.out.println("加载配置列表:共 " + rootModelList.size() + " 个");
        String url;
        for (RootModel rootModel : rootModelList) {
            url = rootModel.getRootUrl();
            System.out.println(" >>> " + url);
            rootModelMap.put(url, rootModel);
            urlList.add(url);
        }*/
    }

    // 爬取的流程，全站爬取，基本流程
    public static void flow() {
        try {
            List<String> typeList = root(rootModel);
            for (String type : typeList) {
                typeUrl = rootUrl + type;
                // 类型
                while (typeNextPageFlag) { // 下一页类型
                    List<String> typePageList = type(typeUrl, rootModel);
                    System.out.println("while >>> ");
                    typePageList.forEach(System.out::println);
                    for (String bookUrl : typePageList) { // 循环当前类型
                        // 章节
                        chapterUrl = bookUrl;
                        while (chapterNextPageFlag) { // 是否有下一页目录
                            List<String> stringList = book(bookUrl, rootModel);
                            if (stringList != null && stringList.size() != 0) { // 循环当前目录
                                for (String str : stringList) {
                                    // 内容
                                    content(rootUrl + str, rootModel);
                                }
                            }
                        }
                    }
                    System.out.println("typeNextPageFlag:" + typeNextPageFlag);
                    System.out.println("typeUrl:" + typeUrl);
                    System.out.println("总页数:" + typeTotalPage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //1.访问根目录
    public static List<String> root(RootModel rootModel) throws Exception {

        Document document = requestForHtml(rootModel.getRootUrl(), rootModel);
        if (document != null) {

            map = rootModel.getHeaders();
            charset = rootModel.getCharset();
            rootUrl = rootModel.getRootUrl();

            List<String> result = Xsoup.compile(rootModel.getNovelTypeUrlPattern()).evaluate(document).list();

            return parseUrl(result,rootModel.getRootUrl(),rootModel);
        }
        throw new Exception(rootModel.getRootUrl() + "异常");
    }

    //2.小说类型或者搜索
    public static List<String> type(String url, RootModel rootModel) throws Exception {
        Document document = requestForHtml(url, rootModel);
        if (document != null) {
            TypeModel typeModel = rootModel.getTypeModel();
            //System.out.println(document.body());
            List<String> result = Xsoup.compile(typeModel.getNovelUrlPattern()).evaluate(document).list();
            String flag = null;
            System.out.println("typeModel.getNextPagePattern():" + typeModel.getNextPagePattern());

            // 不一定有下一页
            if("" != typeModel.getNextPagePattern()&&typeModel.getNextPagePattern()!=null) {
                flag = Xsoup.compile(typeModel.getNextPagePattern()).evaluate(document).get();
            }
            // 不一定需要总页数
            if("" != typeModel.getTotalPagePattern()&&typeModel.getTotalPagePattern()!=null) {
                typeTotalPage = Xsoup.compile(typeModel.getTotalPagePattern()).evaluate(document).get();
            }

            if (flag == null) {
                typeNextPageFlag = false;
            } else {
                typeNextPageFlag = true;
                typeUrl = flag;
            }

            return parseUrl(result,url,rootModel);
        }
        throw new Exception(url + "异常");
    }

    public static List<String> search(String like, RootModel rootModel) throws Exception {
        if (rootModel != null) {

            String url = rootModel.getSearchUrl();
            Map<String,String> searchMap = rootModel.getSearchModel().getKeyMap();
            System.out.println("是否需要加密:" + rootModel.getSearchModel().getEncode());
            if(StringUtils.isNotEmpty(rootModel.getSearchModel().getEncode())){
                like = URLEncoder.encode(like,rootModel.getSearchModel().getEncode());
            }
            searchMap.put(rootModel.getSearchModel().getSearchKey(),like);

            Document document;
            if ("POST".equals(rootModel.getSearchModel().getSearchType())) {
                document = requestForHtml(url, rootModel, searchMap, Connection.Method.POST);
            }else {
                document = requestForHtml(url, rootModel,searchMap);
            }
            //System.out.println(document);
            List<String> result = Xsoup.compile(rootModel.getSearchModel().getNovelUrlPattern()).evaluate(document).list();
            List<String> name = Xsoup.compile(rootModel.getSearchModel().getNovelNamePattern()).evaluate(document).list();

            return parseUrl(concatUrlName(result,name),url,rootModel);
        }
        throw new Exception("入参异常");
    }

    public static List<String> concatUrlName(List<String> result,List<String> name) throws Exception {
        if(result==null||result.size()==0||name==null||name.size()==0){
            throw new Exception("未搜索到书籍");
        }
        if(result.size()==name.size()){
            List<String> concat = new ArrayList<>();
            int size = result.size();
            for(int i = 0;i < size; i++){
                concat.add(result.get(i) + " " + name.get(i));
            }
            return concat;
        }
        throw new Exception("书籍名称与链接不匹配");
    }


    //3.某个书籍
    public static List<String> book(String url,RootModel rootModel) throws Exception {
        System.out.println("入参:" + url);
        List<String> result = null;
        List<String> name = null;
        Document document = requestForHtml(url,rootModel);
        if(document != null){
            BookModel bookModel = rootModel.getBookModel();

            if("".equals(bookModel.getMenuUrlPattern())||null==bookModel.getMenuUrlPattern()){
                System.out.println("bookModel.getChapterListPattern():" + bookModel.getChapterListPattern());
                result = Xsoup.compile(bookModel.getChapterListPattern()).evaluate(document).list();
                name = Xsoup.compile(bookModel.getChapterNameListPattern()).evaluate(document).list();
            }else {
                System.out.println("bookModel.getMenuUrlPattern():" + bookModel.getMenuUrlPattern());
                List<String> chapter = Xsoup.compile(bookModel.getMenuUrlPattern()).evaluate(document).list();
                System.out.println(chapter.size());
                if(chapter.size()==1) {
                    document = requestForHtml(rootUrl + chapter.get(0),rootModel);
                    result = Xsoup.compile(bookModel.getChapterListPattern()).evaluate(document).list();
                    name = Xsoup.compile(bookModel.getChapterNameListPattern()).evaluate(document).list();
                }
            }

            // 是否有下一页
            System.out.println("bookModel.getNextChapterListPattern():" + bookModel.getNextChapterListPattern());
            if("" != bookModel.getNextChapterListPattern() && null != bookModel.getNextChapterListPattern()){
                String nextPage = Xsoup.compile(bookModel.getNextChapterListPattern()).evaluate(document).get();
                System.out.println("nextPage:" + nextPage);
                if(""==nextPage){
                    chapterNextPageFlag = false;
                }else {
                    chapterNextPageFlag = true;
                    chapterUrl = rootModelMap.get(rootUrl(url)).getRootUrl() + nextPage;
                    // 有的网站最后一页到了不消失，手动判断下
                    if(url.equals(chapterUrl)){
                        chapterNextPageFlag = false;
                        chapterUrl = "";
                    }
                }

            }else {
                chapterNextPageFlag = false;
            }

            System.out.println("chapterNextPageFlag:" + chapterNextPageFlag);
            System.out.println("chapterUrl:" + chapterUrl);


            return parseUrl(concatUrlName(result,name),url,rootModel);
        }
        throw new Exception(url + "异常");
     }

    //4.小说内容
    public static String content(String url,RootModel rootModel) throws Exception {
        System.out.println("content:" + url);

        // 设置字符集，有的网站会单独设置，字符集
        rootModel.setCharset(rootModel.getContentModel().getCharset()==null?rootModel.getCharset():rootModel.getContentModel().getCharset());

        Document document = requestForHtml(url,rootModel);
        if(document != null){
            ContentModel contentModel = rootModel.getContentModel();

            System.out.println("contentModel.getContent():" + contentModel.getContent());
            System.out.println("contentModel.getTitle():" + contentModel.getTitle());

            String title = Xsoup.compile(contentModel.getTitle()).evaluate(document).get();

            String content = "";
            if("".equals(rootModel.getContentModel().getNextPage())||null == rootModel.getContentModel().getNextPage()){
                content = Xsoup.compile(contentModel.getContent()).evaluate(document).get();
            }else {
                List<String> urlList = Xsoup.compile(contentModel.getNextPage()).evaluate(document).list();
                for(String s:urlList){
                    s = parseUrl(s,url,rootModel);
                    System.out.println("parseUrl(s,url,rootModel):" + s);
                    document = requestForHtml(s,rootModel);
                    content += Xsoup.compile(contentModel.getContent()).evaluate(document).get();
                }
            }

            System.out.print(" 标题 >>> ");
            System.out.println(title);
            System.out.print(" : 内容 --- ");
            System.out.println(content);
            if(content.contains("src=\"/")){
                content = content.replaceAll("src=\"/","src=\"" + rootModel.getRootUrl() + "/");
                System.out.print(" : 替换图片后 --- ");
            }
            System.out.println(content);
            return title + content;
        }
            throw new Exception(url + "异常");
    }



    // 公共请求方法 , 重试还是有问题
    public static Document requestForHtml(String url,RootModel rootModel){ // 容错处理和重试次数，超时等等
        return requestForHtml(url,rootModel,new HashMap<>());
    }

    private static int retry = 0;
    private static int timeout = 3000;
    public static Document requestForHtml(String url,RootModel rootModel,Map<String,String> searchMap){ // 容错处理和重试次数，超时等等

        try {
            String html = Jsoup.connect(url).headers(rootModel.getHeaders()).data(searchMap).timeout(timeout).execute().charset(rootModel.getCharset()).body();
            Document document = Jsoup.parse(html);
            retry = 0;
            return document;
        } catch (Exception e) {
            retry++;
            if(retry < 6){
                timeout = timeout * 2;
                System.err.println(url + " >>> 请求异常: " + retry + " >>> "  + e + ",耗时timeout:" + timeout);
                requestForHtml(url,rootModel,searchMap);
            }

            //e.printStackTrace();
        }finally {
            retry = 0;
            timeout = 3000;
        }
        return null;
    }


    public static Document requestForHtml(String url,RootModel rootModel,Map<String,String> searchMap,Connection.Method method){ // 容错处理和重试次数，超时等等
        try {

            //目前发现 jsoup 的post可能数据提交有问题，先修改为http
            /*String html = Jsoup.connect(url).headers(rootModel.getHeaders())
                          .method(method)
                          .timeout(3000)
                          .data(searchMap)
                          .execute().charset(rootModel.getCharset()).body();*/
            String html = requestByHttpURLConnection(url,rootModel,searchMap,timeout);

            Document document = Jsoup.parse(html);
            retry = 0;
            return document;
        } catch (Exception e) {
            retry++;
            timeout = timeout * 2;
            if(retry < 6){
                requestForHtml(url,rootModel);
                System.err.println(url + " >>> 请求异常: " + retry + " >>> "  + e + ",耗时timeout:" + timeout);
            }
            retry = 0;

            e.printStackTrace();
        }finally {
            retry = 0;
        }
        return null;
    }

    public static String requestByHttpURLConnection(String url,RootModel rootModel,Map<String,String> searchMap,int timeout) throws Exception {
        boolean gzip = false ;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

        // 设置请求头
        Map<String,String> map = rootModel.getHeaders();
        if(map!=null&&map.size()>0){
            for(Map.Entry entry : map.entrySet()){
                //System.out.println((String) entry.getKey() + "---" + conn.getRequestProperty((String) entry.getKey()));
                String key = (String) entry.getKey();
                if(conn.getRequestProperty(key)==null){
                    String value = (String) entry.getValue();
                    conn.setRequestProperty(key,value);
                    // 大小写有时候不一样
                    if("Accept-Encoding".equalsIgnoreCase(key)&&value.contains("gzip")){
                        gzip = true; // 需要解压
                    }
                }
            }
        }
        //3, 设置提交类型
        conn.setConnectTimeout(timeout);
        conn.setRequestMethod("POST");

        //4, 设置允许写出数据,默认是不允许 false
        conn.setDoOutput(true);
        conn.setDoInput(true);//当前的连接可以从服务器读取内容, 默认是true
        //conn.setConnectTimeout(6000);
        // 如果不为空就进去, 没搞懂方式

        //5, 获取向服务器写出数据的流
        OutputStream os = conn.getOutputStream();
        //参数是键值队  , 不以"?"开始
        os.write(getUrlParamsByMap(searchMap).getBytes());
        os.flush();

        //6, 获取响应的数据
        //得到服务器写回的响应数据
        System.out.println("" + conn.getResponseCode());

        if( 200==conn.getResponseCode() ){
            String returnMsg = InputStreamUtils.inputStreamStr(conn.getInputStream(),rootModel.getCharset(),gzip);
            //System.out.println(returnMsg);
            return returnMsg;
        }

        throw  new Exception(" 接受html返回数据异常 ");
    }

    public static String getUrlParamsByMap(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }

        System.out.println("组织后参数:" + s);

        return s;
    }

    public static String rootUrl(String pathUrl) throws Exception {
        try {
            URL url = new URL(pathUrl);
            return url.getProtocol() + "://" + url.getHost();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        throw new Exception("解析rootUrl异常");

    }


    // 把链接处理为能直接用的链接,目前已知就这三种
    public static String parseUrl(String url,String parentUrl,RootModel rootModel) throws Exception {
        /**
         * 三种类型 https://www.37zw.la/11/11690/
         * 1. http:// 或 https://
         * 2. / 开头
         * 3. 其他表示在父级路径加上url
         */

        if(StringUtils.isNotEmpty(url)){
            if(url.startsWith("https://")||url.startsWith("http://")){
                return url;
            }else if(url.startsWith("/")){
                return rootModel.getRootUrl() + url;
            }else if (url.endsWith(".html")){ // 不懂
                return parentUrl.substring(0,parentUrl.lastIndexOf("/") + 1) + url;
            }else {
                return parentUrl + url;
            }
        }

        throw new Exception("异常");
    }


    public static List<String> parseUrl(List<String> urlList,String parentUrl,RootModel rootModel) throws Exception {
        List<String> stringList = new ArrayList<>(urlList.size());
        for(String s:urlList){
            stringList.add(parseUrl(s,parentUrl,rootModel));
        }
        return stringList;
    }

}
