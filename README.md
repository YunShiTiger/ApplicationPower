    ApplicationPower 是一个基于数据库单表Crud操作的项目生成器，生成的web项目自动集成spring,spring mvc,mybatis框架，最终生成基于maven构建的可运行web工程，生成完后只需要将生成的项目导入到eclipse、idea或者及其他开发工具部署至tomcat即可运行，当然生成的项目基于maven环境集成了jetty web容器，eclipse使用jetty:run命令即可运行，idea的用户只需点击maven projects下的plugins中找到jetty run即可启动项目。
    ApplicationPower是基于beetl模板来生成源代码的，因此可以灵活的修改模板来生成代码定义自己的开发接口规范。ApplicationPower相对mybatis generator来说配置更少、代码灵活性和可控性更高。
    

 **使用说明：** 
  1.使用注意事项
    在已经进行后，请勿将ApplicationPower的输出目录指定到当前工程，否则会出现代码覆盖，因此建议项目开发启动后将代码生成到别的地方拷贝到自己工        程下，后续会提供不覆盖配置，但是也有可能忘记修改配置，所以还是要小心。
  2.根据自己实际需求，修改generator.properties中的配置
  ```
  是否生成注释
  generator.comment=true

  代码输出目录
  generator.outDir=e:\\Test

  基包名
  generator.basePackage=com.boco

  数据库表前缀,例如表t_user则需要去除前缀生成正确的实体
  generator.table.prefix=

  指定需要用哪张数据表生成代码，不指定则生成全部表的代码
  generator.table.name=

  生成项目的名称
  generator.applicationName=Test

  需要生成的代码层
  可生成的代码层dao,model,service,controller,mapper,controllerTest,serviceTest
  generator.layers=dao,model,service,controller,mapper,controllerTest

  是否开启mybatis缓存，只能填写true或者false
  generator.enableCache=true
  ```
  3.修改数据库配置jdbc.properties
  ```
  jdbc.driver=com.mysql.jdbc.Driver
  jdbc.username=root
  jdbc.password=root
  jdbc.url=jdbc\:mysql\://localhost:3306/cookbook?useUnicode=true&characterEncoding=UTF-8
  ```
  4.运行Test下的GenerateCodeTest生成项目
  5.将生成的项目导入编辑器





