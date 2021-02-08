package cn.cc.novel.model;


/**
 * @author c.c.
 * @date 2021/2/1
 */
public class TypeModel {
    // 小说类型的链接

    // 匹配点击类型后的书籍的链接
    private String novelUrlPattern;

    private String nextPagePattern;

    private String totalPagePattern;

    public String getNovelUrlPattern() {
        return novelUrlPattern;
    }

    public void setNovelUrlPattern(String novelUrlPattern) {
        this.novelUrlPattern = novelUrlPattern;
    }

    public String getNextPagePattern() {
        return nextPagePattern;
    }

    public void setNextPagePattern(String nextPagePattern) {
        this.nextPagePattern = nextPagePattern;
    }

    public String getTotalPagePattern() {
        return totalPagePattern;
    }

    public void setTotalPagePattern(String totalPagePattern) {
        this.totalPagePattern = totalPagePattern;
    }
}
