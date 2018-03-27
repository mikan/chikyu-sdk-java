package net.chikyu.chikyu.sdk;

import net.chikyu.chikyu.sdk.config.ApiConfig;
import org.junit.Before;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public abstract class AbsApiTest {
    Properties params;

    @Before
    public void prepare() throws IOException {
        ApiConfig.setMode("local");
        String path = new File(".").getAbsoluteFile().getParent() +
                "/src/test/resources/config." + ApiConfig.getMode() + ".properties";
        params = new Properties();
        params.load(new FileReader(path));
    }

}
