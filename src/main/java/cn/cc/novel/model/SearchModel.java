package cn.cc.novel.model;

import java.util.Map;

/**
 * @author c.c.
 * @date 2021/2/1
 */
public class SearchModel {
    // 小说搜索

    // 匹配搜索后的书籍的链接地址
    private String novelUrlPattern;

    private String novelNamePattern;

    // 请求方式
    private String searchType;

    // 必要的键值对，可以为空
    private Map<String,String> keyMap;

    // 搜索的key
    private String searchKey;

    // 搜索的value
    //private String searchValue;// 可以去掉

    // 加密
    private String encode;

    public String getNovelUrlPattern() {
        return novelUrlPattern;
    }

    public void setNovelUrlPattern(String novelUrlPattern) {
        this.novelUrlPattern = novelUrlPattern;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public Map<String, String> getKeyMap() {
        return keyMap;
    }

    public void setKeyMap(Map<String, String> keyMap) {
        this.keyMap = keyMap;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    /*public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }*/

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public String getNovelNamePattern() {
        return novelNamePattern;
    }

    public void setNovelNamePattern(String novelNamePattern) {
        this.novelNamePattern = novelNamePattern;
    }
}
