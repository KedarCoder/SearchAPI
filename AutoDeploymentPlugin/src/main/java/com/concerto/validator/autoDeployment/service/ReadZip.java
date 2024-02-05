package com.concerto.validator.autoDeployment.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReadZip {
	private static final Logger logger = LoggerFactory.getLogger(ReadZip.class);
	
    // Method to initiate the ZIP file processing
    public void zipProcess() {
    try {
        // Provide the path to the ZIP file and the destination directory
       String zipFilePath = "C:/Users/suyog.kedar/Desktop/XML Record/test1.zip";
        String destDirectory = "C:/Users/suyog.kedar/Desktop/Demo2/Test/";
        
        
        File zipFile = new File(zipFilePath);

        // Check if the input ZIP file exists
        if (!zipFile.exists()) {
        	logger.info("ZIP file not found: {}", zipFilePath);
            return;  // Exit the method if the ZIP file is not present
        }
            // Call the unzip method to extract the contents of the ZIP file
            unzip(zipFilePath, destDirectory);
            logger.info("Unzip successful !");
            
          
            
        } catch (IOException e) {
        	logger.error("Error in ReadZip.zipProcess() {}", e);
        }
    }

    // Method to unzip the contents of a ZIP file
    public void unzip(String zipFilePath, String destDirectory) throws IOException {
        // Create a File object for the destination directory
        File destDir = new File(destDirectory);
        // Create the destination directory if it doesn't exist
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
     // Use try-with-resources to automatically close the ZipInputStream
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
        	
            // Get the first entry in the ZIP file
            ZipEntry entry = zipInputStream.getNextEntry();

            while (entry != null) {
            	
                // Get the name of the ZIP file (excluding extension) for creating subdirectories
                String zipFileName = getZipFileName(zipFilePath);
                
                // Construct the full file path for the current entry
                String filePath = destDirectory + File.separator + zipFileName + File.separator + entry.getName();

                try {
                    if (!entry.isDirectory()) {
                        // Ensure the parent directories are created before extracting the file
                        File parentDir = new File(filePath).getParentFile();
                        if (!parentDir.exists()) {
                            parentDir.mkdirs();
                        }

                        // Call the extractFile method to write the entry content to the specified file
                        extractFile(zipInputStream, filePath);
                    } else {
                        // If the entry is a directory, create the corresponding directory
                        File dir = new File(filePath);
                        dir.mkdirs();
                    }
                } catch (IOException e) {
                    // Handle IOException specific to extracting or creating directories
                	logger.error("Error in ReadZip.unzip() {}", e);

                } finally {
                    try {
                        // Close the current entry and move to the next one
                        zipInputStream.closeEntry();
                        entry = zipInputStream.getNextEntry();
                        
                    } catch (IOException e) {
                        // Handle IOException specific to closing the current entry
                    	logger.error("Error in unzip() {}", e);
                    }
                }
            }
        } catch (IOException e) {
            // Handle IOException specific to creating ZipInputStream
        	logger.error("Error in Saving File ReadZip.unzip() {}", e);
        }

    }

    // Method to extract the content of a ZIP entry and write it to a file
    private static void extractFile(ZipInputStream zipInputStream, String filePath) throws IOException {
        // Use try-with-resources to automatically close the FileOutputStream
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            // Buffer to read and write data in chunks
            byte[] buffer = new byte[1024];
            int length;

            // Read data from the ZIP input stream and write it to the output file
            while ((length = zipInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
        }
    }
    
    // Method to get the name of a ZIP file without the file extension
    private static String getZipFileName(String zipFilePath) {
        // Create a File object for the ZIP file
        File file = new File(zipFilePath);
        // Get the name of the ZIP file
        String fileName = file.getName();

        // Find the last dot (.) to determine the position of the file extension
        int lastDotIndex = fileName.lastIndexOf(".");
        
        // If a dot is found, extract the name without the extension
        if (lastDotIndex != -1) {
            fileName = fileName.substring(0, lastDotIndex);
        }
        // Return the name of the ZIP file without the extension
        return fileName;
    }
}
