package com.boco.power.builder;

import com.boco.power.constant.GeneratorConstant;
import com.boco.power.utils.BeetlTemplateUtil;
import com.boco.power.utils.DateTimeUtil;
import com.boco.power.utils.StringUtils;
import org.beetl.core.Template;

/**
 * @author sunyu on 2017/11/09
 */
public class JqueryPluginBuilder {
    private static final String TPL_PLUGIN = "front\\jquery-plugin.btl";
    private static final String PLUGIN_NAME = "pluginName";
    private static final String PLUGIN_NAME_UPER = "pluginObject";

    /**
     * 创建jquery插件
     * @param pluginName
     */
    public  String writeBuilder(String pluginName){
        Template daoTemplate = BeetlTemplateUtil.getByName(TPL_PLUGIN);
        daoTemplate.binding(GeneratorConstant.AUTHOR, System.getProperty("user.name"));//作者
        daoTemplate.binding(GeneratorConstant.CREATE_TIME, DateTimeUtil.getTime());//创建时间
        daoTemplate.binding(PLUGIN_NAME,pluginName);//类名
        daoTemplate.binding(PLUGIN_NAME_UPER, StringUtils.firstToUpperCase(pluginName));//基包名
        return  daoTemplate.render();
    }
}
