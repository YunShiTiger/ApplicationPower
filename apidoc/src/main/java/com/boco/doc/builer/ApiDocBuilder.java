package com.boco.doc.builer;

import com.boco.doc.model.ApiDoc;
import com.boco.doc.utils.BeetlTemplateUtil;
import com.boco.doc.utils.FileUtils;
import org.beetl.core.Template;

import java.util.List;

public class ApiDocBuilder {

    /**
     * 生成所有controller的api文档
     * @param outPath
     * @param isStrict
     */
    public static void builderControllersApi(String outPath,boolean isStrict){
        FileUtils.mkdirs(outPath);
        SourceBuilder sourceBuilder = new SourceBuilder(isStrict);
        List<ApiDoc> apiDocList = sourceBuilder.getControllerApiData();
        for(ApiDoc doc:apiDocList){
            Template mapper = BeetlTemplateUtil.getByName("ApiDoc.btl");
            mapper.binding("name", doc.getName());
            mapper.binding("list", doc.getList());//类名
            FileUtils.writeFileNotAppend(mapper.render(), outPath + "\\" + doc.getName() + "Api.md");
        }
    }

    /**
     * 生成单个controller的api文档
     * @param outPath
     * @param controllerName
     */
    public static void buildSingleControllerApi(String outPath,String controllerName){
        FileUtils.mkdirs(outPath);
        SourceBuilder sourceBuilder = new SourceBuilder(true);
        ApiDoc doc = sourceBuilder.getSingleControllerApiData(controllerName);
        Template mapper = BeetlTemplateUtil.getByName("ApiDoc.btl");
        mapper.binding("name", doc.getName());
        mapper.binding("list", doc.getList());//类名
        FileUtils.writeFileNotAppend(mapper.render(), outPath + "\\" + doc.getName() + "Api.md");
    }
}
