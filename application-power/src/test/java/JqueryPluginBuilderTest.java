import com.boco.common.util.FileUtil;
import com.boco.power.builder.JqueryPluginBuilder;


import java.util.logging.Filter;

public class JqueryPluginBuilderTest {

    public static void main(String[] args) {

        JqueryPluginBuilder builder = new JqueryPluginBuilder();
        String str = builder.writeBuilder("FormBuilder");
       // System.out.println(str);
        FileUtil.writeFileNotAppend(str,"d:\\jquery-formbuilder.js");
    }
}
