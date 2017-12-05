package com.boco.doc.builer;

import com.boco.doc.model.ApiDoc;
import com.boco.doc.utils.BeetlTemplateUtil;
import com.boco.doc.utils.FileUtils;
import org.beetl.core.Template;

import java.util.List;

public class ApiDocBuilder {

    public static void builder(String outPath){
        SourceBuilder sourceBuilder = new SourceBuilder();
        List<ApiDoc> apiDocList = sourceBuilder.getControllerApiData();
        for(ApiDoc doc:apiDocList){
            Template mapper = BeetlTemplateUtil.getByName("ApiDoc.btl");
            mapper.binding("name", doc.getName());
            mapper.binding("list", doc.getList());//类名
            FileUtils.writeFileNotAppend(mapper.render(), outPath + "\\" + doc.getName() + "Api.md");
        }
    }
}
