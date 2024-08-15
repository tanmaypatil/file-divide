package com.filedivide;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void divideaFile() throws FileNotFoundException, IOException {
        String fileName = "random.txt";
        Properties p = FileDivideProperties.getProperties();
        String val = p.getProperty("inputFilePath");
        int linesPerFile = 5;
        String inputFilePath = val + "\\" + fileName;
        val = p.getProperty("outputFilePath");
        String outputFilePath = FileDivideProperties.getProperties().getProperty("outputFilePath");
        FileDivider.divideFile(inputFilePath, outputFilePath, linesPerFile);

    }

    @Test
    void getLineCount() throws FileNotFoundException, IOException {
        String fileName = "random.txt";
        Properties p = FileDivideProperties.getProperties();
        String val = p.getProperty("inputFilePath");
        int linesPerFile = 5;
        String inputFilePath = val + "\\" + fileName;
        val = p.getProperty("outputFilePath");
        String outputFilePath = FileDivideProperties.getProperties().getProperty("outputFilePath");
        long lineCount = FileDivider.divideFileNio(inputFilePath, outputFilePath, linesPerFile);
        System.out.println("linecount :" + lineCount);
        assertTrue(lineCount <= 15);

    }

    @Test
    void getCharCount() {
        String str = "abddddnewnew";
        char ch = 'd';
        long count = FileDivider.countOccurrences(str, ch);
        assertEquals(4, count);
    }

    @Test
    void getCharCount2() {
        String str = "abddddnewnew";
        char ch = 'd';
        long count = FileDivider.countOccurrences(str, ch);
        assertEquals(3, count);
    }

    @Test
    void getJreVersion() {
        // Get the JRE version
        String javaVersion = System.getProperty("java.version");
        // Optionally, you can print it out
        System.out.println("Java Runtime Environment version: " + javaVersion);
    }

    @Test
    void testMulti() {
        char ch = '\n';
        String multiLineString = """ 
                This is a multi-line string.
                It can span multiple lines,
                and it preserves the formatting
                exactly as you write it.
                You can include "quotes" and special characters like \t
                """;
        long count = FileDivider.countOccurrences(multiLineString, ch);
        assertEquals(5, count);
        long lineCount = FileDivider.countOccurrences(multiLineString, ch);
        assertEquals(5, lineCount);
    }

    @Test
    void testMulti1() {
        char ch = '\n';
        String multiLineString = """ 
                This is a multi-line string.
                It can span multiple lines,
                and it preserves the formatting
                exactly as you write it.
                You can include "quotes" and special characters like \t
                """;
        int index = FileDivider.ordinalIndexOf(multiLineString, "\n", 0);
        String str = multiLineString.substring(0, index+1);
        System.err.println(str);
        String result = """
                This is a multi-line string.
                """;
        Assertions.assertEquals(result, str);
        
    }

}
