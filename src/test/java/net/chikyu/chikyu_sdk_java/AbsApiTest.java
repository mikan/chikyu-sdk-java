package net.chikyu.chikyu_sdk_java;

import net.chikyu.chikyu_sdk_java.config.Config;
import org.junit.Before;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class AbsApiTest {

    protected Properties params;

    @Before
    public void prepare() throws IOException {
        String path = new File(".").getAbsoluteFile().getParent() +
                "/src/test/resources/config." + Config.getMode() + ".properties";
        params = new Properties();
        params.load(new FileReader(path));
    }

}
