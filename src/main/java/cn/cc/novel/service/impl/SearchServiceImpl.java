package cn.cc.novel.service.impl;

import cn.cc.novel.flow.CrawFlow;
import cn.cc.novel.model.RootModel;
import cn.cc.novel.service.SearchService;
import cn.cc.novel.utils.PrintWriterUtils;
import cn.cc.novel.utils.ReturnObj;
import cn.cc.novel.utils.StatusEnum;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author c.c.
 * @date 2021/2/5
 */
@Service("SearchServiceImpl")
public class SearchServiceImpl implements SearchService {

    private static Map<String,RootModel> modelMap = new HashMap<>();
    private static List<RootModel> modelList;

    @Override
    public ReturnObj search(String like) {
        getInstanse();
        ReturnObj returnObj = new ReturnObj();
        returnObj.setData(search(modelList,like));
        returnObj.setStatusEnum(StatusEnum.Status200);
        return returnObj;
    }

    @Override
    public ReturnObj book(String url) {
        ReturnObj returnObj = new ReturnObj();
        try {
            URL u = new URL(url);
            List<String> stringList = CrawFlow.book(url,modelMap.get(u.getProtocol() + "://" + u.getHost()));
            returnObj.setData(listToMap(stringList));
            returnObj.setStatusEnum(StatusEnum.Status200);
        } catch (Exception e) {
            returnObj.setStatusEnum(StatusEnum.Status001);
            e.printStackTrace();
        }
        return returnObj;
    }

    @Override
    public ReturnObj chapter(String url) {
        ReturnObj returnObj = new ReturnObj();
        try {
            URL u = new URL(url);
            String stringList = CrawFlow.content(url,modelMap.get(u.getProtocol() + "://" + u.getHost()));
            returnObj.setData(stringList);
            returnObj.setStatusEnum(StatusEnum.Status200);
        } catch (Exception e) {
            returnObj.setStatusEnum(StatusEnum.Status001);
            e.printStackTrace();
        }
        return returnObj;
    }

    public static synchronized  void getInstanse(){
        if(modelList == null){
            System.out.println("初始化--------");
            modelList = initJSON();
        }else {
            System.out.println("已初始化");
        }
    }

    public static List<RootModel> initJSON(){
        modelList = new ArrayList<>();
        File file = new File("src/main/resources/json");
        File[] files = file.listFiles();
        if(files!=null&&files.length>0){
            for(File f:files){
                String json = getJSON(f.getAbsolutePath());
                //System.out.println(json);
                RootModel rootModel = (RootModel) JSON.parse(json);
                modelList.add(rootModel);
                modelMap.put(rootModel.getRootUrl(),rootModel);
                System.out.println("初始化已有json列表:" + rootModel.getSearchUrl());
            }
        }else {
            System.err.println("请配置网站json");
        }
        //modelList.forEach(System.out::println);
        return modelList;
    }



    List<Map<String,String>> search(List<RootModel> rootModelList,String like){
        List<Map<String,String>> mapList = new ArrayList<>();
        for(RootModel rootModel:rootModelList){

            System.out.println("rootModel.getSearchUrl() >>> " + rootModel.getSearchUrl());
            List<String> stringList = null;
            try {
                stringList = CrawFlow.search(like,rootModel);
                if(stringList != null) {
                    mapList = listToMap(stringList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*for(String s:stringList){
                System.out.println(s);
            }*/
        }
        return mapList;
    }

    private List<Map<String,String>> listToMap(List<String> stringList){
        List<Map<String,String>> mapList = new ArrayList<>();
        for (String s : stringList) {
            Map<String,String> map = new HashMap<>();
            System.out.println(">>>" + s);


            String[] strings = s.split(" ");
            map.put("url", s.substring(0,s.indexOf(" ")));
            map.put("name", s.substring(s.indexOf(" ") + 1,s.length()));
            /*System.out.println("strings[0]: " + strings[0]);
            System.out.println("strings[1]: " + strings[1]);*/
            mapList.add(map);
        }
        return mapList;
    }

    public static String getJSON(String resource){

        try {
            // 静态方法和非晶态方法不同
            return PrintWriterUtils.fileReader(resource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
