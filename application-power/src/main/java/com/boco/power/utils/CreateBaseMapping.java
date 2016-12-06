package com.boco.power.utils;

import java.io.File;
import java.sql.*;
import java.util.Properties;

/**
 *
 *
 * @author qxl
 * @date 2016年7月22日 下午1:00:34
 */
public class CreateBaseMapping {
	private static final String DRIVER = "com.mysql.jdbc.Driver";

	public void getTableInfo(String driver, String url, String user,
							 String pwd, String table,String targetDir,String rootDir,String basePackage) {
		Connection conn = null;
		try {
			conn = getConnections(driver, url, user, pwd);
			try {
				DatabaseMetaData dbmd = conn.getMetaData();
				ResultSet resultSet = dbmd.getTables(null, "%", "%", new String[] { "TABLE" });
				while (resultSet.next()) {
					String tableName = resultSet.getString("TABLE_NAME");
					if (true) {
						this.createBean(conn,tableName,basePackage,targetDir);
						this.createDao(tableName,basePackage,targetDir);
						this.createService(tableName,basePackage,targetDir);
						this.createServiceImpl(tableName,basePackage,targetDir);
						this.createServiceTest(tableName,basePackage,rootDir);
						this.createController(tableName,basePackage,targetDir);
						this.createMapping(conn,tableName,basePackage,targetDir);
						this.createControllerTest(tableName,basePackage,rootDir);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {

		}
	}

	public static  void initDir(String rootDir,String basePackage){
		File dir = new File(rootDir);
		dir.mkdir();
		String newPackage =  basePackage.replaceAll("[.]","\\\\");
		String srcDir = rootDir+"\\src\\main\\java\\"+newPackage;
		File file = new File(srcDir);
		file.mkdirs();
		String mappingDirStr = srcDir +"\\mapping";
		File mappingDir = new File(mappingDirStr);
		mappingDir.mkdir();

		String daoDirStr = srcDir +"\\dao";
		File daoDir = new File(daoDirStr);
		daoDir.mkdir();

		String serviceDirStr = srcDir  +"\\service";
		File serviceDir = new File(serviceDirStr);
		serviceDir.mkdir();

		String serviceImplDirStr = srcDir +"\\service\\impl";
		File serviceImplDir = new File(serviceImplDirStr);
		serviceImplDir.mkdir();

		String controllerDirStr = srcDir +"\\controller";
		File controllerDir = new File(controllerDirStr);
		controllerDir.mkdir();

		String modelDirStr = srcDir +"\\model";
		File modelDir = new File(modelDirStr);
		modelDir.mkdir();

		String testServiceDir = rootDir+"\\src\\test\\java\\"+newPackage+"\\service";
		File testServiceFile = new File(testServiceDir);
		testServiceFile.mkdirs();

		String testControllerDir = rootDir+"\\src\\test\\java\\"+newPackage+"\\controller";
		File testControllerFile = new File(testControllerDir);
		testControllerFile.mkdirs();

	}
	// 获取连接
	private Connection getConnections(String driver, String url,
									  String user, String pwd) throws Exception {
		Connection conn = null;
		try {
			Properties props = new Properties();
			props.put("remarksReporting", "true");
			props.put("user", user);
			props.put("password", pwd);
			Class.forName(driver);
			conn = DriverManager.getConnection(url, props);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return conn;
	}
	public void createBean(Connection con, String tableName,String basePackage,String targetDir){

		String entityName = StringUtils.toCapitalizeCamelCase(tableName);
		String sql = "select * from " + tableName;
		StringBuilder builder = new StringBuilder();
		builder.append("package ").append(basePackage).append(".").append("model;\n\n");
		builder.append("/**\n");
		builder.append(" *\n");
		builder.append(" * @author ").append(System.getProperty("user.name")).append("\n");
		builder.append(" * @date ").append(DateTimeUtil.long2Str(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
		builder.append("\n");
		builder.append(" *\n");
		builder.append(" */\n");
		builder.append("public class ").append(entityName).append("{\n");
		StringBuilder gettersAndSetters = new StringBuilder();
		DatabaseMetaData databaseMetaData;
		try {
			try {
				Class.forName(DRIVER);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			databaseMetaData = con.getMetaData();
			ResultSet rs = databaseMetaData.getColumns(null, "%", tableName, "%");
			while (rs.next()) {
				String columnName = rs.getString("COLUMN_NAME");
				int digits = rs.getInt("DECIMAL_DIGITS");
				int dataType = rs.getInt("DATA_TYPE");
				String comment = rs.getString("REMARKS");
				String columnType = toJavaType(dataType, digits);
				builder.append("	//").append(comment).append("\n");
				builder.append("	private ").append(columnType).append(" ");
				builder.append(StringUtils.underlineToCamel(columnName)).append(";\n");

				gettersAndSetters.append("	public ").append(columnType).append(" get");
				gettersAndSetters.append(StringUtils.toCapitalizeCamelCase(tableName)).append("(){\n");
				gettersAndSetters.append("		return ").append(StringUtils.underlineToCamel(columnName)).append(";\n");
				gettersAndSetters.append("	}\n");

				gettersAndSetters.append("	public void set").append(StringUtils.toCapitalizeCamelCase(columnName));
				gettersAndSetters.append("(").append(columnType).append(" ").append(StringUtils.underlineToCamel(columnName));
				gettersAndSetters.append("){\n");
				gettersAndSetters.append("		this.").append(StringUtils.underlineToCamel(columnName));
				gettersAndSetters.append(" = ").append(StringUtils.underlineToCamel(columnName)).append(";\n");

				gettersAndSetters.append("	}\n");
			}


		}catch (Exception e){

		}
		builder.append("\n").append("	//getters and setters\n");
		builder.append(gettersAndSetters.toString());
		builder.append("}");


		FileUtil.writeFile(builder.toString(),targetDir+"\\model\\"+entityName+".java",false,"utf-8");
		//System.out.println(builder.toString());
	}

	public void createDao(String tableName,String basePackage,String targetDir){
		String entityName = StringUtils.toCapitalizeCamelCase(tableName);
		String name = basePackage+"."+entityName;
		StringBuffer buffer = new StringBuffer();
		buffer.append("package ").append(basePackage).append(".dao;\n\n");
		buffer.append("import java.util.List;\n");
		buffer.append("import java.util.Map;\n\n");
		buffer.append("import ").append(name).append(";\n\n");
		buffer.append("public interface ").append(entityName).append("Dao{\n");

		buffer.append("	boolean save(").append(entityName).append(" entity);\n");
		buffer.append("	boolean update(").append(entityName).append(" entity);\n");
		buffer.append("	boolean delete(int id);\n");
		buffer.append("	").append(entityName).append(" queryById(int id);\n");
		buffer.append("	List<").append(entityName).append("> queryAll();\n");

		buffer.append("}");

		FileUtil.writeFile(buffer.toString(),targetDir+"\\dao\\"+entityName+"Dao.java",false,"utf-8");
	}
	public void createService(String tableName,String basePackage,String targetDir){
		String entityName = StringUtils.toCapitalizeCamelCase(tableName);
		String name = basePackage+"."+entityName;
		StringBuffer buffer = new StringBuffer();
		buffer.append("package ").append(basePackage).append(".service;\n\n");
		buffer.append("import java.util.List;\n");
		buffer.append("import ").append(name).append(";\n\n");
		buffer.append("public interface ").append(entityName).append("Service{\n\n");
		// 构造保存方法
		buffer.append("	boolean save(").append(entityName);
		buffer.append(" ").append(StringUtils.firstToLowerCase(entityName));
		buffer.append(");\n\n");
		// 构造更新接口
		buffer.append("	boolean update(").append(entityName);
		buffer.append(" ").append(StringUtils.firstToLowerCase(entityName));
		buffer.append(");\n\n");
		// 构造删除数据接口
		buffer.append("	boolean delete(int id);\n\n");
		buffer.append("	").append(entityName).append(" queryById(int id);\n\n");
		buffer.append("	List<").append(entityName).append("> queryAll();\n");
		buffer.append("}");

		FileUtil.writeFile(buffer.toString(),targetDir+"\\service\\"+entityName+"Service.java",false,"utf-8");

	}
	public void createServiceImpl(String tableName,String basePackage,String targetDir){
		String entityName = StringUtils.toCapitalizeCamelCase(tableName);
		String lowerName = StringUtils.underlineToCamel(tableName);
		StringBuffer buffer = new StringBuffer();
		buffer.append("package  ").append(basePackage).append(".service.impl;\n\n");
		buffer.append("import java.util.ArrayList;\n");
		buffer.append("import java.util.List;\n\n");
		buffer.append("import javax.annotation.Resource;\n");
		buffer.append("import org.springframework.stereotype.Service;\n");

		buffer.append("import ").append(basePackage).append(".model.").append(entityName).append(";\n");
		buffer.append("import ").append(basePackage).append(".dao.").append(entityName).append("Dao;\n");
		buffer.append("import ").append(basePackage).append(".service.").append(entityName).append("Service;\n\n");
		// user annotation
		buffer.append("@Service(\"").append(lowerName).append("Service\")\n");
		buffer.append("public class ").append(entityName).append("ServiceImpl ");
		buffer.append(" implements ").append(entityName).append("Service{\n\n");

		buffer.append("	@Resource\n");
		buffer.append("	private ").append(entityName).append("Dao ").append(lowerName).append("Dao;\n\n");
		// 保存
		buffer.append("	@Override\n");
		buffer.append("	public int save(").append(entityName);
		buffer.append(" ").append(StringUtils.firstToLowerCase(lowerName));
		buffer.append("){\n");
		buffer.append("		return ").append(lowerName);
		buffer.append("Dao.save(").append(lowerName);
		buffer.append(");\n");
		buffer.append("	}\n\n");

		// 更新
		buffer.append("	@Override\n");
		buffer.append("	public int update(").append(entityName);
		buffer.append(" ").append(lowerName);
		buffer.append("){\n");
		buffer.append("		return ").append(lowerName);
		buffer.append("Dao.update(").append(lowerName);
		buffer.append(");\n");
		buffer.append("	}\n\n");

		buffer.append("	@Override\n");
		buffer.append("	public int").append(" delete(int id){\n");
		buffer.append("		return ").append(lowerName);
		buffer.append("Dao.delete(id);\n");
		buffer.append("	}\n\n");
		// 查询全部
		buffer.append("	@Override\n");
		buffer.append("	public List<").append(entityName).append("> queryAll(){\n");
		buffer.append("		return ").append(lowerName).append("Dao.queryAll();\n");
		buffer.append("	}\n\n");

		// 根据id查找
		buffer.append("	@Override\n");
		buffer.append("	public ").append(entityName).append(" queryById(int id){\n");
		buffer.append("		return ").append(lowerName).append("Dao.queryById(id);\n");
		buffer.append("	}\n\n");

		buffer.append("}");

		FileUtil.writeFile(buffer.toString(),targetDir+"\\service\\impl\\"+entityName+"ServiceImpl.java",false,"utf-8");

	}

	public void createServiceTest(String tableName,String basePackage,String testRootDir){
		String simpleName = StringUtils.toCapitalizeCamelCase(tableName);
		String lowerName = StringUtils.underlineToCamel(tableName);

		StringBuffer buffer = new StringBuffer();
		buffer.append("package ").append(basePackage).append(".service.impl;\n\n");
		buffer.append("import javax.annotation.Resource;\n\n");

		buffer.append("import org.junit.Test;\n");
		buffer.append("import org.junit.runner.RunWith;\n");
		buffer.append("import org.springframework.test.context.ContextConfiguration;\n");
		buffer.append("import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;\n");
		buffer.append("import ").append(basePackage).append(".model.").append(simpleName).append(";\n");
		buffer.append("import ").append(basePackage).append(".service.").append(simpleName);
		buffer.append("Service;\n\n");
		// 注解
		buffer.append("@RunWith(SpringJUnit4ClassRunner.class)\n");
		buffer.append("@ContextConfiguration(locations = {\"classpath:spring-mybatis.xml\"})\n");
		buffer.append("public class ").append(simpleName).append("ServiceTest{\n\n");

		buffer.append("	@Resource\n");
		buffer.append("	private ").append(simpleName).append("Service ").append(lowerName).append("Service;\n\n");

		buffer.append("	@Test\n");
		buffer.append("	public void testSave(){\n");
		buffer.append("		").append(simpleName).append(" ").append(lowerName).append(" = ");
		buffer.append("new ").append(simpleName).append("();\n");
		buffer.append("		int num = ").append(lowerName).append("Service.save(").append(lowerName).append(");\n");
		buffer.append("	}\n\n");
		buffer.append("	@Test\n");
		buffer.append("	public void testUpdate(){\n");
		buffer.append("		").append(simpleName).append(" ").append(lowerName).append(" = ");
		buffer.append("new ").append(simpleName).append("();\n");
		buffer.append("		int num = ").append(lowerName).append("Service.update(").append(lowerName).append(");\n");
		buffer.append("	}\n\n");
		buffer.append("	@Test\n");
		buffer.append("	public void testDelete(){\n");
		buffer.append("		int num = ").append(lowerName).append("Service.delete(1);\n");
		buffer.append("	}\n\n");
		buffer.append("	@Test\n");
		buffer.append("	public void testQueryById(){\n");
		buffer.append("		").append(simpleName).append(" ").append(lowerName).append(" = ");
		buffer.append(lowerName).append("Service.queryById(1);\n");
		buffer.append("	}\n\n");

		buffer.append("	@Test\n");
		buffer.append("	public void testQueryAll(){\n");
		buffer.append(" 		List<").append(simpleName).append("> list = ");
		buffer.append(lowerName).append("Service.queryAll();\n");
		buffer.append("		//不想通过遍历看数据，建议使用fastjson或者gson的toString方法查看数据\n");
		buffer.append("		for(").append(simpleName).append(" ").append(lowerName).append(":list){\n\n");
		buffer.append("		}\n");
		buffer.append("	}\n\n");
		buffer.append("}");

		String dir = testRootDir+"\\src\\test\\java\\"+basePackage.replaceAll("[.]","\\\\")+"\\service";

		FileUtil.writeFile(buffer.toString(),dir +"\\" +simpleName+"ServiceTest.java",false,"utf-8");

	}

	public void createController(String tableName,String basePackage,String targetDir){

		String simpleName = StringUtils.toCapitalizeCamelCase(tableName);
		String lowerName = StringUtils.underlineToCamel(tableName);

		StringBuilder buffer = new StringBuilder();
		buffer.append("package ").append(basePackage).append(".controller;\n\n");

		buffer.append("import javax.annotation.Resource;\n");
		buffer.append("import javax.servlet.http.HttpServletRequest;\n\n");
		buffer.append("import org.springframework.stereotype.Controller;\n");
		buffer.append("import org.springframework.ui.Model;\n");
		buffer.append("import org.springframework.web.bind.annotation.RequestMethod;\n");
		buffer.append("import org.springframework.web.bind.annotation.ResponseBody;\n");
		buffer.append("import org.springframework.web.bind.annotation.RequestMapping;\n\n");


		buffer.append("import com.boco.health.common.model.CommonResult;\n");
		buffer.append("import ").append(basePackage).append(".model.").append(simpleName).append(";\n");
		buffer.append("import ").append(basePackage).append(".service.").append(simpleName).append("Service;\n\n");
		buffer.append("@Controller\n");
		buffer.append("@RequestMapping(\"").append(lowerName).append("\")\n");
		buffer.append("public class ").append(simpleName).append("Controller{\n");
		buffer.append("	@Resource\n");
		buffer.append("	private ").append(simpleName).append("Service ").append(lowerName).append("Service;\n\n");
		// build add
		buffer.append("	@ResponseBody\n");
		buffer.append("	@RequestMapping(value=\"/add\",method = RequestMethod.POST)\n");
		buffer.append("	public CommonResult save(").append(simpleName).append(" entity){\n");
		buffer.append("		CommonResult result = new CommonResult();\n");
		buffer.append("		try{\n");
		buffer.append("			").append(lowerName).append("Service.save(entity);\n");
		buffer.append("			result.setSuccess(true)\n");
		buffer.append("		}catch(Exception e){\n");
		buffer.append("			result.setMessage(\"添加数据失败\");\n");
		buffer.append("		}\n");
		buffer.append("		return result;\n");
		buffer.append("	}\n\n");
		// build update
		buffer.append("	@ResponseBody\n");
		buffer.append("	@RequestMapping(value=\"/update\",method = RequestMethod.POST)\n");
		buffer.append("	public CommonResult update(").append(simpleName).append(" entity){\n");
		buffer.append("		CommonResult result = new CommonResult();\n");
		buffer.append("		try{\n");
		buffer.append("			").append(lowerName).append("Service.update(entity);\n");
		buffer.append("			result.setSuccess(true)\n");
		buffer.append("		}catch(Exception e){\n");
		buffer.append("			result.setMessage(\"修改数据失败\");\n");
		buffer.append("		}\n");
		buffer.append("		return result;\n");
		buffer.append("	}\n\n");
		// build delete
		buffer.append("	@ResponseBody\n");
		buffer.append("	@RequestMapping(value=\"/delete/{id}\",method = RequestMethod.POST)\n");
		buffer.append("	public CommonResult delete(@PathVariable int id){\n");
		buffer.append("		CommonResult result = new CommonResult();\n");
		buffer.append("		try{\n");
		buffer.append("			").append(lowerName).append("Service.delete(id);\n");
		buffer.append("			result.setSuccess(true)\n");
		buffer.append("		}catch(Exception e){\n");
		buffer.append("			result.setMessage(\"删除数据失败\");\n");
		buffer.append("		}\n");
		buffer.append("		return result;\n");
		buffer.append("	}\n\n");

		buffer.append("	@ResponseBody\n");
		buffer.append("	@RequestMapping(value=\"/query/{id}\",method = RequestMethod.POST)\n");
		buffer.append("	public CommonResult queryById(@PathVariable int id){\n");
		buffer.append("		CommonResult result = new CommonResult();\n");
		buffer.append("		").append(simpleName).append(" ").append(lowerName).append(" = ");
		buffer.append(lowerName).append("Service.queryById(id);\n");
		buffer.append("		if(null != ").append(lowerName).append("){\n");
		buffer.append("			result.setData(").append(lowerName).append(");//成功返回数据\n");
		buffer.append("			result.setSuccess(true)\n");
		buffer.append("		}else{\n");
		buffer.append("			result.setMessage(\"没有找到匹配数据\");\n");
		buffer.append("		}\n");
		buffer.append("		return result;\n");
		buffer.append("	}\n");

		buffer.append("}\n");

		FileUtil.writeFile(buffer.toString(),targetDir+"\\controller\\"+simpleName+"Controller.java",false,"utf-8");

	}

	public void createControllerTest(String tableName,String prePackageName,String testRootDir){

		String simpleName = StringUtils.toCapitalizeCamelCase(tableName);
		String lowerName = StringUtils.underlineToCamel(tableName);


		StringBuilder builder = new StringBuilder();
		builder.append("package ").append(prePackageName).append(".controller;\n\n");
		// import requires class
		builder.append("import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;\n");
		builder.append("import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;\n");
		builder.append("import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;\n");
		builder.append("import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;\n");
		builder.append(
				"import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;\n\n");
		builder.append("import org.junit.Before;\n");
		builder.append("import org.junit.Test;\n");
		builder.append("import org.junit.runner.RunWith;\n");
		builder.append("import org.springframework.beans.factory.annotation.Autowired;\n");
		builder.append("import org.springframework.http.MediaType;\n");
		builder.append("import org.springframework.test.context.ContextConfiguration;\n");
		builder.append("import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;\n");
		builder.append("import org.springframework.test.context.web.WebAppConfiguration;\n");

		builder.append("import org.springframework.test.web.servlet.MockMvc;\n");
		builder.append("import org.springframework.test.web.servlet.MvcResult;\n");
		builder.append("import org.springframework.web.context.WebApplicationContext;\n\n");
		// annotation
		builder.append("@RunWith(SpringJUnit4ClassRunner.class)\n");
		builder.append("@ContextConfiguration({\"classpath:spring-mybatis.xml\", \"classpath:spring-mvc.xml\"})\n");
		builder.append("@WebAppConfiguration\n");
		builder.append("public class ").append(simpleName).append("ControllerTest{\n\n");

		builder.append("	@Autowired\n");
		builder.append("	private WebApplicationContext wac;\n\n");
		builder.append("	private MockMvc mockMvc;\n\n");

		builder.append("	@Before\n");
		builder.append("	public void setup() {\n");
		builder.append("		this.mockMvc = webAppContextSetup(this.wac).build();\n");
		builder.append("	}\n\n");
		// test add function
		builder.append("	@Test\n");
		builder.append("	public void testAdd() throws Exception {\n");
		builder.append("		MvcResult result = mockMvc.perform(post(\"/");
		builder.append(lowerName).append("/add\")\n");
		builder.append(" 			.contentType(MediaType.APPLICATION_JSON)\n");
		builder.append(" 			.param(\"param1\", \"pm1\")\n");
		builder.append(" 		).andExpect(status().isOk()).andDo(print()).andReturn();\n");
		builder.append("		System.out.println(\"result:\"+result.getResponse().getContentAsString());\n");
		builder.append("	}\n\n");
		// get by id
		builder.append("	@Test\n");
		builder.append("	public void testQueryById() throws Exception {\n");
		builder.append("		MvcResult result = mockMvc.perform(post(\"/");
		builder.append(lowerName).append("/query/{id}\", 1)\n");
		builder.append(" 			.contentType(MediaType.APPLICATION_JSON)\n");
		builder.append(" 			.param(\"param1\", \"pm1\")\n");
		builder.append(" 		).andExpect(status().isOk()).andDo(print()).andReturn();\n");
		builder.append("		System.out.println(\"result:\"+result.getResponse().getContentAsString());\n");
		builder.append("	}\n\n");
		// test update function
		builder.append("	@Test\n");
		builder.append("	public void testUpdate() throws Exception {\n\n");
		builder.append("	}\n\n");
		// test delete function
		builder.append("	@Test\n");
		builder.append("	public void testDelete() throws Exception {\n\n");
		builder.append("		MvcResult result = mockMvc.perform(post(\"/");
		builder.append(lowerName).append("/delete/{id}\", 1)\n");
		builder.append(" 			.contentType(MediaType.APPLICATION_JSON)\n");
		builder.append(" 			.param(\"param1\", \"pm1\")\n");
		builder.append(" 		).andExpect(status().isOk()).andDo(print()).andReturn();\n");
		builder.append("		System.out.println(\"result:\"+result.getResponse().getContentAsString());\n");
		builder.append("	}\n");
		builder.append("}\n");

		String dir = testRootDir+"\\src\\test\\java\\"+prePackageName.replaceAll("[.]","\\\\")+"\\controller\\";

		FileUtil.writeFile(builder.toString(),dir +simpleName+"ControllerTest.java",false,"utf-8");

	}
	public String  toJavaType(int type, int digits) {
		String dataType = "";
		switch (type) {
			case Types.VARCHAR:  //12
				dataType = "String";
				break;
			case Types.INTEGER:    //4
				dataType = "Integer";
				break;
			case Types.TINYINT:    //4
				dataType = "Integer";
				break;
			case Types.BIT:    //-7
				dataType = "Integer";
				break;
			case Types.BIGINT: //-5
				dataType = "Long";
				break;
			case Types.DOUBLE: //8
				dataType = "Double";
				break;
			case Types.FLOAT: //6
				dataType = "Float";
				break;
			case Types.DECIMAL:    //3
				dataType = "BigDecimal";
				break;
			case Types.TIME:  //91
				dataType = "Date";
				break;
			case Types.DATE:  //91
				dataType = "Date";
				break;
			default:
				dataType = "String";
		}
		return dataType;
	}
	public void createMapping(Connection con, String tablename,String basePackage,String targetDir) {

		String simpleName = StringUtils.toCapitalizeCamelCase(tablename);
		String sql = "select * from " + tablename;
		PreparedStatement pStemt = null;
		try {
			try {
				Class.forName(DRIVER);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			// con = DriverManager.getConnection(URL,NAME,PASS);
			pStemt = con.prepareStatement(sql);
			ResultSetMetaData rsmd = pStemt.getMetaData();
			int size = rsmd.getColumnCount();
			StringBuilder insertSql = new StringBuilder();
			insertSql.append("insert into ").append(tablename).append("(\n");

			StringBuilder updateSql = new StringBuilder();
			updateSql.append(" update ").append(tablename).append(" set\n");

			StringBuilder selectSql = new StringBuilder();
			selectSql.append(" select \n");

			for (int i = 0; i < size; i++) {
				if(i < size-1){
					insertSql.append("			").append(rsmd.getColumnName(i + 1)).append(",\n");
					updateSql.append("			").append(rsmd.getColumnName(i + 1)).append(" = #{");
					updateSql.append(getColnameToCamelCase(rsmd.getColumnName(i + 1))).append("},\n");
					selectSql.append("			").append(rsmd.getColumnName(i + 1)).append(",\n");
				}else{
					insertSql.append("			").append(rsmd.getColumnName(i + 1)).append("\n");
					updateSql.append("			").append(rsmd.getColumnName(i + 1)).append(" = #{");
					updateSql.append(getColnameToCamelCase(rsmd.getColumnName(i + 1))).append("}\n");
					selectSql.append("			").append(rsmd.getColumnName(i + 1)).append("\n");
				}
			}
			insertSql.append("		) values (\n");
			updateSql.append("		 where 你的判定条件...");
			selectSql.append(" 		 from ").append(tablename).append(" where 你的查询条件...");

			for (int i = 0; i < size; i++) {
				if(i < size -1){
					insertSql.append("			#{").append(getColnameToCamelCase(rsmd.getColumnName(i + 1))).append("},\n");
				}else{
					insertSql.append("			#{").append(getColnameToCamelCase(rsmd.getColumnName(i + 1))).append("}\n");
				}
			}
			insertSql.append("		)");


			StringBuilder mapper = new StringBuilder();
			mapper.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
			mapper.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" "
					+ "\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");

			mapper.append("<mapper namespace=\"").append(basePackage).append(".dao.");
			mapper.append(simpleName).append("Dao").append("\">\n");

			mapper.append("	<!--保存-->\n");
			mapper.append("	<insert id=\"save\" ").append("parameterType=\"").append(basePackage).append(".model.");
			mapper.append(simpleName).append("\">\n");
			mapper.append("	<!---selectKey注释去掉后被插入对象的自增值会自动返回-->\n");
			mapper.append("	<!-- <selectKey resultType=\"int\" keyProperty=\"id\" order=\"AFTER\">-->\n");
			mapper.append("		<!--	SELECT LAST_INSERT_ID()-->\n");
			mapper.append("	<!--	 </selectKey>-->\n");
			mapper.append("		").append(insertSql.toString()).append("\n");
			mapper.append("	</insert>\n");

			// 根据id修改
			mapper.append("	<!--更新-->\n");
			mapper.append("	<update id=\"update\" ").append("parameterType=\"").append(basePackage).append(".model.");
			mapper.append(simpleName).append("\">\n");
			mapper.append("		").append(updateSql).append("\n");
			mapper.append("	</update>\n");

			// 删除
			mapper.append("	<!--删除-->\n");
			mapper.append("	<delete id=\"delete\" ").append("parameterType=\"");
			mapper.append("Integer").append("\">\n");
			mapper.append("		").append("delete from ").append(tablename);
			mapper.append(" where 你的删除条件....\n");
			mapper.append("	</delete>\n");

			mapper.append("	<!--根据编号查询-->\n");
			mapper.append("	<select id=\"queryById\" ").append("parameterType=\"");
			mapper.append("Integer").append("\" ").append("resultType=\"").append(basePackage).append(".model.");
			mapper.append(simpleName).append("\">\n");
			mapper.append("		").append(selectSql.toString()).append("\n");
			mapper.append("	</select>\n");

			// 查询全部
			mapper.append("	<!--查询全部-->\n");
			mapper.append("	<select id=\"queryAll\" ").append("resultType=\"").append(basePackage).append(".model.").append(simpleName);
			mapper.append("\">\n");
			mapper.append("		").append(selectSql.toString()).append("\n");
			mapper.append("	</select>\n");

			mapper.append("</mapper>");
			FileUtil.writeFile(mapper.toString(),targetDir+"\\mapping\\"+simpleName+"Dao.xml",false,"utf-8");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String getColnameToCamelCase(String colName) {
		String colnameCamelCase = "";
		String[] strs = colName.split("_");
		int tag = 0;
		for (String str : strs) {
			if (tag != 0) {
				colnameCamelCase += initcap(str);
			} else {
				colnameCamelCase += str;
			}
			tag++;
		}
		return colnameCamelCase;
	}

	private String initcap(String str) {

		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}

		return new String(ch);
	}
}
