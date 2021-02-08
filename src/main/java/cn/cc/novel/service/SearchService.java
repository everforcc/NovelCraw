package cn.cc.novel.service;

import cn.cc.novel.utils.ReturnObj;

/**
 * @author c.c.
 * @date 2021/2/5
 */
public interface SearchService {

    public ReturnObj search(String like);

    public ReturnObj book(String url);

    public ReturnObj chapter(String url);
}
