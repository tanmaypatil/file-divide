package com.filedivide;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public class MemoryMappedFileDivider {
    public static void main(String[] args) throws NumberFormatException, FileNotFoundException, IOException {
        // Path of the file to be divided
        String inputFilePath = "input.txt";
        // Base path for the output files
        String outputFilePath = "output";
        // Lines per file
        int linesPerFile = Integer.parseInt(FileDivideProperties.getProperties().getProperty("linesPerFile"));
        divideFile(inputFilePath, outputFilePath, linesPerFile);

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

}
