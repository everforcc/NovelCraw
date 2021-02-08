package cn.cc.novel.model;

/**
 * @author c.c.
 * @date 2021/2/1
 */
public class BookModel {

    // 目录链接
    private String menuUrlPattern;
    // 章节列表，有的可能是直接有，有的点击目录才会出现
    private String chapterListPattern;

    private String nextChapterListPattern;

    private String chapterNameListPattern;

    public String getMenuUrlPattern() {
        return menuUrlPattern;
    }

    public void setMenuUrlPattern(String menuUrlPattern) {
        this.menuUrlPattern = menuUrlPattern;
    }

    public String getChapterListPattern() {
        return chapterListPattern;
    }

    public void setChapterListPattern(String chapterListPattern) {
        this.chapterListPattern = chapterListPattern;
    }

    public String getNextChapterListPattern() {
        return nextChapterListPattern;
    }

    public void setNextChapterListPattern(String nextChapterListPattern) {
        this.nextChapterListPattern = nextChapterListPattern;
    }

    public String getChapterNameListPattern() {
        return chapterNameListPattern;
    }

    public void setChapterNameListPattern(String chapterNameListPattern) {
        this.chapterNameListPattern = chapterNameListPattern;
    }
}
