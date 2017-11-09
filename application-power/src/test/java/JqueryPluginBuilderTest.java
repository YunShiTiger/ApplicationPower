import com.boco.power.builder.JqueryPluginBuilder;
import com.boco.power.utils.FileUtils;

import java.util.logging.Filter;

public class JqueryPluginBuilderTest {

    public static void main(String[] args) {

        JqueryPluginBuilder builder = new JqueryPluginBuilder();
        String str = builder.writeBuilder("FormBuilder");
       // System.out.println(str);
        FileUtils.writeFileNotAppend(str,"d:\\jquery-formbuilder.js");
    }
}
