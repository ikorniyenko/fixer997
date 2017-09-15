package com.spscommerce.fixer997;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by ikornienko on 8/1/2017.
 */
public class FolderHandler {
    private static Logger log = Logger.getLogger(FolderHandler.class); // logger initialization
    private final MailWorker mailWorker;
    private final SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyyMMddHHmm");

    public FolderHandler(MailWorker mailwork) {
        mailWorker = mailwork;
    }

    // if we need to research all files in folder
    public void folderAnalyzer(String dataFolder, String prFolder, String arcFolder, String server) {
        DocumentFixer documentFixer = new DocumentFixer();
        File errorFolder = new File(dataFolder);
        File prodFolder = new File(prFolder);
        File archiveFolder = new File(arcFolder);
        File[] listOfFiles = errorFolder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            File currentFile = listOfFiles[i];
            if (currentFile.isFile()) { // Look only in files, not directories
                try {
                    String charSet = getCharset(currentFile); // Need to know encoding of File
                    String content = FileUtils.readFileToString(currentFile, charSet); // Send File Data to content variable
                    if (content.isEmpty()) continue;

                    String updatedContent = documentFixer.fileAnalyzer(content);
                    if (updatedContent.isEmpty()) continue;

                    log.info("We have found incorrect file with name '" + currentFile.getName() + "'");
                    archiveFile(archiveFolder, currentFile);

                    String initialName = nameSplitter(currentFile.getName()); // For EDI2 errors we need to know Initial File Name
                    String dateOfFile = newDateFormat.format(currentFile.lastModified()); // For EDI1/EDI2 errors we need to know File Date
                    writeAndMoveFile(updatedContent, currentFile, prodFolder, charSet);
                    mailWorker.emailSearch(initialName, dateOfFile, server);

                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("IOException is in FolderHandler class happened (folderAnalyzer method) \n" + e);
                }
            }
        }
    }

    public void archiveFile(File archiveFolder, File currentFile) throws IOException {
        FileUtils.copyFileToDirectory(currentFile, archiveFolder); // Send old File to archive
        log.info("Initial file has been moved to archive folder " + archiveFolder);
    }

    public void writeAndMoveFile(String updatedContent, File currentFile, File prodFolder, String charSet) throws IOException {
        FileUtils.writeStringToFile(currentFile, updatedContent, charSet); // Put correct data to File
        FileUtils.moveFileToDirectory(currentFile, prodFolder, false); // Send corrected File to production folder
        log.info("It has been fixed and moved to production folder " + prodFolder);
    }

    // Function, that split File Name from EDI2 by "_" symbol and leave only initial File Name (System add also date, time to it when move file to error folder)
    public String nameSplitter(String nameOfFile) {
        String parts[] = nameOfFile.split("_");
        String resultName = parts[parts.length - 1];
        return resultName;
    }

    // Function, that take encoding of file
    public String getCharset(File file) {
        String charset = "";
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buf = new byte[4096];
            UniversalDetector detector = new UniversalDetector(null);
            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            detector.dataEnd();
            charset = detector.getDetectedCharset();
            detector.reset();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("FileNotFoundException is in FolderHandler class happened \n" + e);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("IOException is in FolderHandler class happened (getCharset method) \n" + e);
        }
        return charset;
    }

}
