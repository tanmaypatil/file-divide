package com.filedivide;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class MemoryMappedFileDivider {
    public static void main(String[] args) throws NumberFormatException, FileNotFoundException, IOException {
        // Path of the file to be divided
        String inputFilePath = "input.txt";

        // Base path for the output files
        String outputFilePath = "output";

        // Lines per file
        int linesPerFile = Integer.parseInt(FileDivideProperties.getProperties().getProperty("linesPerFile"));

        try {
            divideFile(inputFilePath, outputFilePath, linesPerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void divideFile(String inputFilePath, String outputFilePath, int linesPerFile) throws IOException {
        try (RandomAccessFile inputFile = new RandomAccessFile(inputFilePath, "r");
             FileChannel fileChannel = inputFile.getChannel()) {

            long fileSize = fileChannel.size();
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);

            int fileNumber = 1;
            int lineCount = 0;
            StringBuilder currentLine = new StringBuilder();

            PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath + fileNumber + ".txt"));
            try {
                for (int i = 0; i < fileSize; i++) {
                    char c = (char) buffer.get();

                    if (c == '\n') {
                        writer.println(currentLine.toString());
                        currentLine.setLength(0);
                        lineCount++;

                        if (lineCount == linesPerFile) {
                            writer.close();
                            fileNumber++;
                            writer = new PrintWriter(new FileWriter(outputFilePath + fileNumber + ".txt"));
                            lineCount = 0;
                        }
                    } else {
                        currentLine.append(c);
                    }
                }

                // Write any remaining lines
                if (currentLine.length() > 0) {
                    writer.println(currentLine.toString());
                }
            }
            catch(Exception e) {

            }
            finally {
                if ( writer != null ) {
                    writer.close();
                }
            }

            System.out.println("File division completed successfully.");
        }
    }
}
