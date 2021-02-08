package cn.cc.novel.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Map;

/**
 * @author c.c.
 * @date 2021/2/1
 */
public class RootModel {
    // 网站根目录
    private String rootUrl;
    // 搜索链接
    private String searchUrl;
    // 小说类型匹配
    private String novelTypeUrlPattern;
    // 请求头
    private Map<String,String> headers;
    // 字符集
    private String charset;

    private BookModel bookModel;

    private ContentModel contentModel;

    private SearchModel searchModel;

    private TypeModel typeModel;

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getNovelTypeUrlPattern() {
        return novelTypeUrlPattern;
    }

    public void setNovelTypeUrlPattern(String novelTypeUrlPattern) {
        this.novelTypeUrlPattern = novelTypeUrlPattern;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public BookModel getBookModel() {
        return bookModel;
    }

    public void setBookModel(BookModel bookModel) {
        this.bookModel = bookModel;
    }

    public ContentModel getContentModel() {
        return contentModel;
    }

    public void setContentModel(ContentModel contentModel) {
        this.contentModel = contentModel;
    }

    public SearchModel getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(SearchModel searchModel) {
        this.searchModel = searchModel;
    }

    public TypeModel getTypeModel() {
        return typeModel;
    }

    public void setTypeModel(TypeModel typeModel) {
        this.typeModel = typeModel;
    }

    public RootModel() {
    }

    public RootModel(String rootUrl, String searchUrl, String novelTypeUrlPattern, Map<String, String> headers, String charset, BookModel bookModel, ContentModel contentModel, SearchModel searchModel, TypeModel typeModel) {
        this.rootUrl = rootUrl;
        this.searchUrl = searchUrl;
        this.novelTypeUrlPattern = novelTypeUrlPattern;
        this.headers = headers;
        this.charset = charset;
        this.bookModel = bookModel;
        this.contentModel = contentModel;
        this.searchModel = searchModel;
        this.typeModel = typeModel;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteClassName);
    }
}
