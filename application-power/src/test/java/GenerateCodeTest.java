import com.boco.power.utils.CreateBaseMapping;

/**
 * Created by yu on 2016/12/6.
 */
public class GenerateCodeTest {
    public static void main(String[] args) {

        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://192.168.15.50:3306/bocohealth";
        String user = "DBAdmin";
        String pwd = "boco1234#BH#DBA";
        String table = "person_person_information";

        String rootDir = "e:\\test";//修改自己的代码输出路径
        String basePackage = "com.boco";//基包名
        CreateBaseMapping.initDir(rootDir,basePackage);//初始化代码输出目录
        String targetDir = rootDir+"\\src\\main\\java\\"+basePackage.replaceAll("[.]","\\\\");
        new CreateBaseMapping().getTableInfo(driver, url, user, pwd, table,targetDir,rootDir,basePackage);
        //new CreateBaseMapping().createBean(;
    }
}
