import com.boco.power.builder.*;
import com.boco.power.database.DataBaseInfo;

import java.util.List;
import java.util.Properties;

/**
 * Created by yu on 2016/12/7.
 */
public class DataBaseInfoTest {

    public static void main(String[] args)throws Exception{
        Properties props = new Properties();


        DataBaseInfo info = new DataBaseInfo();

        List<String> tables = info.listOfTablesByPrefix("");

        ModelBuilder builder = new ModelBuilder();
        DaoBuilder daoBuilder = new DaoBuilder();
        ServiceBuilder serviceBuilder = new ServiceBuilder();
        ServiceImplBuilder serviceImplBuilder = new ServiceImplBuilder();
        ServiceTestBuilder testBuidler = new ServiceTestBuilder();
        ControllerBuilder controllerBuilder = new ControllerBuilder();
        ControllerTestBuilder controllerTestBuilder = new ControllerTestBuilder();
        MapperBuilder mapperBuilder = new MapperBuilder();
        SpringMvcCfgBuilder springMvcCfgBuilder = new SpringMvcCfgBuilder();
        PomBuilder pomBuilder = new PomBuilder();
        for(String str:tables){
          // builder.generateModel(str);
            //System.out.println(daoBuilder.generateDao(str));
            System.out.println(springMvcCfgBuilder.generateSpringMvcCfg());
        }
    }
}
