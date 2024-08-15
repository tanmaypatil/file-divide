package com.filedivide;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.*;
import java.io.BufferedInputStream;
import java.nio.charset.*;

public class FileDivider {
    public static void main(String[] args) throws NumberFormatException, FileNotFoundException, IOException {
        // Path of the file to be divided
        String inputFilePath = FileDivideProperties.getProperties().getProperty("inputFilePath");
        // Base path for the output files
        String outputFilePath = FileDivideProperties.getProperties().getProperty("outputFilePath");
        // Lines per file
        int linesPerFile = Integer.parseInt(FileDivideProperties.getProperties().getProperty("linesPerFile"));
        divideFile(inputFilePath, outputFilePath, linesPerFile);

    }

    public static long countOccurrences(String str, char ch) {
        return str.chars()
                .filter(c -> c == ch)
                .count();
    }

    public static void divideFile(String inputFilePath, String outputFilePath, int linesPerFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            int fileCount = 1;
            int lineCount = 0;
            BufferedWriter writer = null;
            while ((line = reader.readLine()) != null) {
                if (lineCount % linesPerFile == 0) {
                    if (writer != null) {
                        writer.close();
                    }
                    String outputFileName = "output_file_" + fileCount + ".txt";
                    writer = new BufferedWriter(new FileWriter(outputFileName));
                    fileCount++;
                }
                writer.write(line);
                writer.newLine();
                lineCount++;
            }
            if (writer != null) {
                writer.close();
            }
            System.out.println("File has been split into " + (fileCount - 1) + " parts.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int ordinalIndexOf(String str, String substr, int n) {
        int pos = -1;
        do {
            pos = str.indexOf(substr, pos + 1);
        } while (n-- > 0 && pos != -1);
        return pos;
    }

    public static long divideFileNio(String inputFilePath, String outputFilePath, int linesPerFile) throws IOException {
        Path path = Paths.get(inputFilePath);
        long lineCount = 0;
        long cumCount = 0;
        int fileCount = 0;

        char ch = '\n';
        try (InputStream in = Files.newInputStream(path, StandardOpenOption.READ)) {
            BufferedInputStream bin = new BufferedInputStream(in);
            byte[] buffer = new byte[100];
            int bytesRead = 0;
            long residualCount = 0;
            String residue = null;
            while ((bytesRead = bin.read(buffer)) > 0) {
                String str = new String(buffer, StandardCharsets.UTF_8);
                lineCount += countOccurrences(str, ch) + residualCount;
                str = str + residue;
                int i = 0;
                for (i = 0; i * linesPerFile < lineCount && (i + 1)* linesPerFile  < lineCount; i++) {
                    int beginIndex = FileDivider.ordinalIndexOf(str, "\n", i);
                    int endIndex = FileDivider.ordinalIndexOf(str, "\n", i * linesPerFile);
                    String chunk = str.substring(beginIndex, endIndex + 1);
                    /*
                    String outputFileName = outputFilePath + "\\" + "output_file_nio_" + fileCount + ".txt";
                    Path writePath = Paths.get(outputFileName);
                    Files.write(writePath, chunk.getBytes());
                    */
                }
                // check for residual lines
                if (i * linesPerFile < lineCount && i * linesPerFile + linesPerFile > lineCount) {
                    int beginIndex = FileDivider.ordinalIndexOf(str, "\n", i);
                    residue = str.substring(beginIndex);
                    residualCount = lineCount - i;
                }
            }
        }
        return lineCount;
    }
}
