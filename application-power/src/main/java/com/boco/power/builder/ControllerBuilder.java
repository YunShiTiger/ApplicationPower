package com.boco.power.builder;

import com.boco.common.util.DateTimeUtil;
import com.boco.common.util.StringUtil;
import com.boco.power.constant.ConstVal;
import com.boco.power.constant.GeneratorConstant;
import com.boco.power.utils.BeetlTemplateUtil;
import com.boco.power.utils.GeneratorProperties;
import org.beetl.core.Template;

/**
 * 生成controller层代码
 *
 * @author sunyu on 2016/12/7.
 */
public class ControllerBuilder {
    /**
     * @param tableName
     * @return
     */
    public String generateController(String tableName) {
        String entitySimpleName = StringUtil.toCapitalizeCamelCase(tableName);//类名
        String firstLowName = StringUtil.firstToLowerCase(entitySimpleName);//类实例变量名
        Template controllerTemplate = BeetlTemplateUtil.getByName(ConstVal.TPL_CONTROLLER);
        controllerTemplate.binding(GeneratorConstant.AUTHOR, System.getProperty("user.name"));//作者
        controllerTemplate.binding(GeneratorConstant.FIRST_LOWER_NAME, firstLowName);
        controllerTemplate.binding(GeneratorConstant.ENTITY_SIMPLE_NAME, entitySimpleName);//类名
        controllerTemplate.binding(GeneratorConstant.BASE_PACKAGE, GeneratorProperties.basePackage());//基包名
        controllerTemplate.binding(GeneratorConstant.CREATE_TIME, DateTimeUtil.getTime());//创建时间
        controllerTemplate.binding(GeneratorProperties.getGenerateMethods());//绑定需要生成的方法
        return controllerTemplate.render();
    }
}
