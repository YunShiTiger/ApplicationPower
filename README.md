ApplicationPower 是一个基于数据库单表Crud操作的项目生成器，生成的web项目自动集成spring,spring mvc,mybatis框架，最终生成基于maven构建的可
    运行web工程，生成完后只需要将生成的项目导入到eclipse、idea或者及其他开发工具部署至tomcat即可运行，当然生成的项目基于maven环境集成了
    jetty web容器，eclipse使用jetty:run命令即可运行，idea的用户只需点击maven projects下的plugins中找到jetty run即可启动项目。<br/>
        ApplicationPower是基于beetl模板来生成源代码的，因此可以灵活的修改模板来生成代码定义自己的开发接口规范。ApplicationPower相对
    mybatis generator来说配置更少、代码灵活性和可控性更高。<br/>
    **重点：** ApplicationPower目前已经完全支持生成Springboot+Mybatis框架的Springboot项目。
## 结构说明
   1. apidoc是一个未来将使用原生doc注释来生成markdown api文档的项目，目前不可用
   2. common-util是开发中常用的一些工具类，目前文档比较详细，也是application-power所依赖的模块，在使用application-power前需要将它安装到你的本地。
   3. application-power是整个项目的核心，专门用于生成Springboot微服务架构项目和Spring mvc+mybatis架构项目的脚手架，
   4. datasource-aspect是spring web应用下多数据源动态切换的通用模块
## 版本说明
    1. v1.0版本的CommonResult依赖于boco-health-common模块
    2. v1.1版本的CommonResult改为依赖独立模块Common-util
    3. v1.2版本升级spring到4.3.6，Controller层生成的代码使用@GetMapping和@PostMapping代替@RequestMapping注解。
    4. v1.3版本升级mybatis和druid的版本，项目框架摒弃log4j，全面将日志升级到log4j2框架，mysql驱动升级到6.x,支持创建springboot项目。
    5. v1.4版本升级实现生成方法可自由控制(ps:参考generator.properties中配置)，基础方法增加一个返回List<Map<String,Object>>的方法。
    6. v1.4.1版本升级springboot和其他依赖的版本，修改springboot测试模板错误，springboot项目增加springloaded热部署插件。
    7. v1.4.2版本优化生成代码时对数据库的连接次数，restful接口单元测试生成中add和update方法增加自动添加参数和赋予随机值的功能
    8. v1.5版本增加springboot项目基于assembly的服务化打包功能，完备的服务脚本使得在window或linux系统启动和运维项目更轻松
## 功能
  1. 根据连接的数据生成dao,model,service,controller,mapper,controllerTest,serviceTest代码
  2. 项目的maven web基础骨架
  3. 生成基于spring,spring mvc,mybatis框架结构项目所需的基础配置文件
  4. 生成web.xml配置文件
  5. 可以修改模板生成自己喜欢风格或者说修改修改来生成自己习惯的方法名
  6. 基于SL4J面向接口的标注化日志输出

## 使用说明
  1.使用注意事项
        在已经进行后，请勿将ApplicationPower的输出目录指定到当前工程，否则会出现代码覆盖，因此建议项目开发启动后将代码生成到别的地方拷贝到自己工        程下，后续会提供不覆盖配置，但是也有可能忘记修改配置，所以还是要小心。
  2.根据自己实际需求，修改generator.properties中的配置
 ```
  #是否生成注释
  generator.comment=true
  
  #代码输出目录
  generator.outDir=E:\\Test
  
  #基包名
  generator.basePackage=com.boco.demo
  
  #数据库表前缀,例如表t_user则需要去除前缀生成正确的实体
  generator.table.prefix=t_
  
  #指定需要用哪张数据表生成代码，不指定则生成全部表的代码
  generator.table.name=
  
  #生成项目的名称
  generator.applicationName=bootstrap-tree
  
  #需要生成的代码层
  #可生成的代码层dao,model,service,controller,mapper,controllerTest,serviceTest
  generator.layers=dao,model,service,controller,mapper,controllerTest
  
  #需要生成的方法，方法间用英文逗号隔开，写错将无法生成基础方法
  #可生成的方法包括add,update,delete,query,page,queryToListMap。
  # query方法查询单条数据，page生成分页,queryToListMap是查询结果以List<Map<Stirng,Object>>返回
  generator.methods=add,update,delete,query,page,queryToListMap
  
  #mybatis自动转驼峰映射，默认开启
  generator.mapUnderscoreToCamelCase=true
  #是否开启mybatis缓存，只能填写true或者false
  generator.enableCache=true
  
  #是否需要生成mybatis mapper配置文件的ResultMap
  #默认不生成result
  generator.resultMap=false

  # @since 1.5
  # 打包springboot时是否采用assembly
  # 如果采用则将生成一系列的相关配置和一系列的部署脚本
  generator.package.assembly=false
```
  3.修改数据库配置jdbc.properties
```
  jdbc.driver=com.mysql.jdbc.Driver
  jdbc.username=root
  jdbc.password=root
  jdbc.url=jdbc\:mysql\://localhost:3306/cookbook?useUnicode=true&characterEncoding=UTF-8
```
  4.运行Test下的GenerateCodeTest生成项目
```
//生成普通SSM框架的maven工程
new CodeWriter().execute();
//生成Springboot+Mybatis的工程
new CodeWriter().executeSpringBoot();
```
  5.将生成的项目导入编辑器

## 使用模板介绍
  1.model层模板
```
package ${basePackage}.model;

import java.io.Serializable;
${modelImports}

/**
 *
 * @author ${authorName}
 * @date ${createTime}
 *
 */
public class ${entitySimpleName} implements Serializable{

  private static final long serialVersionUID = ${SerialVersionUID}L;

 ${fields}
/** getters and setters */
 ${gettersAndSetters}
}
```
  2.dao层模板
```
package ${basePackage}.dao;

import java.util.List;
import java.util.Map;

import ${basePackage}.model.${entitySimpleName};

/**
 *
 * @author ${authorName}
 * @date ${createTime}
 *
 *
 */

public interface ${entitySimpleName}Dao{

    /**
     * 保存数据
     * @param entity
     * @return
     */
    int save(${entitySimpleName} entity);
    
    /**
     * 更新数据
     * @param entity
     * @return
     */
    int update(${entitySimpleName} entity);
    
    /**
     * 删除数据
     * @param id
     * @return
     */
    int delete(int id);
    
    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    ${entitySimpleName} queryById(int id);
    
    /**
     * 查询所有数据
     * @return
     */
    List<${entitySimpleName}> queryAll();
    
    /**
     * query result to list of map
     * @param params Map查询参数
     * @return
     */
    List<Map<String,Object>> queryToListMap(Map<String,Object> params);
}
```
  3.service层模板
```
package ${basePackage}.service;

import java.util.List;
import java.util.Map;
import com.boco.common.model.CommonResult;
import ${basePackage}.model.${entitySimpleName};

/**
 *
 * @author ${authorName}
 * @date ${createTime}
 *
 */

public interface ${entitySimpleName}Service{

    /**
     * 保存数据
     * @param entity
     * @return
     */
    CommonResult save(${entitySimpleName} entity);
    
    /**
     * 修改数据
     * @param entity
     * @return
     */
    CommonResult update(${entitySimpleName} entity);
    
    /**
     * 删除数据
     * @param id
     * @return
     */
    CommonResult delete(int id);
    
    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    CommonResult queryById(int id);
    
    /**
     * 查询所有数据
     * @return
     */
    List<${entitySimpleName}> queryAll();
    
    /**
     * query result to list of map
     * @param params Map查询参数
     * @return
     */
    List<Map<String,Object>> queryToListMap(Map<String,Object> params);
}
```
  4.service实现层模板
```
package ${basePackage}.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.boco.common.model.CommonResult;
import ${basePackage}.model.${entitySimpleName};
import ${basePackage}.dao.${entitySimpleName}Dao;
import ${basePackage}.service.${entitySimpleName}Service;

/**
 * Created by ApplicationPower.
 * @author ${authorName} on ${createTime}.
 */
@Service("${firstLowerName}Service")
public class ${entitySimpleName}ServiceImpl  implements ${entitySimpleName}Service{

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(${entitySimpleName}Service.class);

    @Resource
    private ${entitySimpleName}Dao ${firstLowerName}Dao;

    @Override
    public CommonResult save(${entitySimpleName} entity) {
        CommonResult result = new CommonResult();
        try {
        	${firstLowerName}Dao.save(entity);
        	result.setSuccess(true);
        } catch (Exception e) {
        	result.setMessage("添加数据失败");
        	logger.error("${entitySimpleName}Service添加数据异常：",e);
        }
        return result;
    }

    @Override
    public CommonResult update(${entitySimpleName} entity) {
        CommonResult result = new CommonResult();
        try {
            ${firstLowerName}Dao.update(entity);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setMessage("修改数据失败");
            logger.error("${entitySimpleName}Service修改数据异常：",e);
        }
        return result;
    }

    @Override
    public CommonResult delete(int id) {
        CommonResult result = new CommonResult();
        try {
            ${firstLowerName}Dao.delete(id);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setMessage("删除数据失败");
            logger.error("${entitySimpleName}Service删除数据异常：",e);
        }
        return result;
    }

    @Override
    public CommonResult queryById(int id) {
        CommonResult result = new CommonResult();
        ${entitySimpleName} entity = ${firstLowerName}Dao.queryById(id);
        if (null != entity) {
            //成功返回数据
            result.setData(entity);
            result.setSuccess(true);
        } else {
            result.setMessage("没有找到匹配数据");
            logger.info("${entitySimpleName}Service未查询到数据，编号：{}",id);
        }
        return result;
    }

    @Override
    public PageInfo queryPage(int offset, int limit) {
        PageHelper.offsetPage(offset,limit);
        List<${entitySimpleName}> list = ${firstLowerName}Dao.queryPage();
        return new PageInfo(list);
    }

    @Override
    public List<Map<String,Object>> queryToListMap(Map<String,Object> params){
        return ${firstLowerName}Dao.queryToListMap(params);
    }
}
```
  5.controller层模板
```
package ${basePackage}.controller;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.PageInfo;
import com.boco.common.model.CommonResult;
import ${basePackage}.model.${entitySimpleName};
import ${basePackage}.service.${entitySimpleName}Service;

/**
 * Created by ApplicationPower.
 * @author ${authorName} on ${createTime}.
 */
@Controller
@RequestMapping("${firstLowerName}")
public class ${entitySimpleName}Controller {
    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(${entitySimpleName}Controller.class);

    @Resource
    private ${entitySimpleName}Service ${firstLowerName}Service;

    @ResponseBody
    @PostMapping(value = "/add")
    public CommonResult save(${entitySimpleName} entity) {
        return ${firstLowerName}Service.save(entity);
    }

    @ResponseBody
    @PostMapping(value = "/update")
    public CommonResult update(${entitySimpleName} entity) {
        return ${firstLowerName}Service.update(entity);
    }

    @ResponseBody
    @GetMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable int id) {
        return ${firstLowerName}Service.delete(id);
    }

    @ResponseBody
    @GetMapping(value = "/query/{id}")
    public CommonResult queryById(@PathVariable int id) {
        return ${firstLowerName}Service.queryById(id);
    }

    @ResponseBody
    @GetMapping(value = "/page/{offset}/{limit}")
    public PageInfo queryPage(@PathVariable int offset,@PathVariable int limit) {
        return ${firstLowerName}Service.queryPage(offset,limit);
    }

    @ResponseBody
    @GetMapping(value = "/listMap")
    public List<Map<String,Object>> queryToListMap(@RequestParam Map<String,Object> params) {
        return ${firstLowerName}Service.queryToListMap(params);
    }
}
```





