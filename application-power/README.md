ApplicationPower 是一个基于数据库单表Crud操作的项目生成器，
生成项目自动集成spring,spring mvc,mybatis框架，
最终生成基于maven构建的可运行web工程

 **使用说明：** 
  1.根据自己实际需求，修改generator.properties中的配置
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
  2.修改数据库配置jdbc.properties
  3.运行Test下的GenerateCodeTest生成项目
  4.将生成的项目导入编辑器





