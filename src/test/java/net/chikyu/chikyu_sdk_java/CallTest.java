package net.chikyu.chikyu_sdk_java;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class CallTest {
    private Properties properties;

    @Before
    public void prepare() throws IOException {
        String path = new File(".").getAbsoluteFile().getParent() + "/src/test/resources/config.properties";
        properties = new Properties();
        properties.load(new FileReader(path));
    }

    @Test
    public void test01() throws Exception{
    }
}
