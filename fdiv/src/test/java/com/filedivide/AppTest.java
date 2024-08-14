package com.filedivide;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Unit test for simple App.
 */
class AppTest {
    /**
     * Rigorous Test.
     */
    @Test
    void testApp() {
        assertEquals(1, 1);
    }

    @Test
    void getFileProperties() throws FileNotFoundException, IOException {
        Properties p = FileDivideProperties.getProperties();
        String val = p.getProperty("linesPerFile");
        assertNotNull(val);
        int linesPerFile = Integer.parseInt(val);
        assertEquals(10, linesPerFile);

    }
}
