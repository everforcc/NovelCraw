package cn.cc.novel.controller;

import cn.cc.novel.service.SearchService;
import cn.cc.novel.utils.ReturnObj;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author c.c.
 * @date 2021/2/5
 */
@RestController
public class SearchController {

    @Autowired
    SearchService searchService;

    @GetMapping("/search")
    public ReturnObj search(String searchKey){
        System.out.println(searchKey);
        ReturnObj returnObj = searchService.search(searchKey);
        System.out.println(returnObj);
        return returnObj;
    }

    @GetMapping("/book")
    public ReturnObj book(String url){
        System.out.println(url);
        ReturnObj returnObj = searchService.book(url);
        System.out.println(returnObj);
        return returnObj;
    }

    @GetMapping("/chapter")
    public ReturnObj chapter(String url){
        System.out.println(url);
        ReturnObj returnObj = searchService.chapter(url);
        System.out.println(returnObj);
        return returnObj;
    }


}
