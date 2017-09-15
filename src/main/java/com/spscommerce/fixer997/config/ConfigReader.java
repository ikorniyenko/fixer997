package com.spscommerce.fixer997.config;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by ikornienko on 9/1/2017.
 */
public class ConfigReader {
    public static Logger log = Logger.getLogger(ConfigReader.class); // logger initialization

    public Config readYAML() {
        Yaml yaml = new Yaml();
        try {
            File f = new File(System.getenv("config")); // Read from environment variable 'config'
            URI uri = f.toURI();
            try (InputStream in = Files.newInputStream(Paths.get(uri))) {
                Config config = yaml.loadAs(in, Config.class); // Automatically reads config to class
                checkingYAMLForRequiredInfo(config);
                return config;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void checkingYAMLForRequiredInfo(Config config) { // Without some info we can not continue application
        if (config.getLogin() == null) {
            log.error("During YAML reading, found that Login Information is not provided. Closing application");
            throw new RuntimeException("During YAML reading, found that Login Information is not provided. Closing application");
        }
        if (config.getPassword() == null) {
            log.error("During YAML reading, found that Password Information is not provided. Closing application");
            throw new RuntimeException("During YAML reading, found that Password Information is not provided. Closing application");
        }
        if (config.getEmailFolder() == null) {
            log.error("During YAML reading, found that EmailFolder Information is not provided. Closing application");
            throw new RuntimeException("During YAML reading, found that EmailFolder Information is not provided. Closing application");
        }
        if (config.getImapHost() == null) {
            log.error("During YAML reading, found that ImapHost Information is not provided. Closing application");
            throw new RuntimeException("During YAML reading, found that ImapHost Information is not provided. Closing application");
        }
        if (config.getToRecipient() == null) {
            log.error("During YAML reading, found that ToRecipient Information is not provided. Closing application");
            throw new RuntimeException("During YAML reading, found that ToRecipient Information is not provided. Closing application");
        }
        if (config.getEmailFault() == 0) {
            log.error("During YAML reading, found that EmailFault Information is not provided. Closing application");
            throw new RuntimeException("During YAML reading, found that EmailFault Information is not provided. Closing application");
        }
        if (config.getMinMessageCount() == 0) {
            log.error("During YAML reading, found that MinMessageCount Information is not provided. Closing application");
            throw new RuntimeException("During YAML reading, found that MinMessageCount Information is not provided. Closing application");
        }
        if (config.getProperties() == null) {
            log.error("During YAML reading, found that Properties Information is not provided. Closing application");
            throw new RuntimeException("During YAML reading, found that Properties Information is not provided. Closing application");
        }
        if (config.getErrorFolderEDI1() == null) {
            log.error("During YAML reading, found that ErrorFolderEDI1 Information is not provided. Closing application");
            throw new RuntimeException("During YAML reading, found that ErrorFolderEDI1 Information is not provided. Closing application");
        } else {
            File folder = new File(config.getErrorFolderEDI1());
            if (!folder.exists()) {
                log.error("Provided ErrorFolderEDI1 [" + folder + "] doesn't exist");
                throw new RuntimeException("Provided ErrorFolderEDI1 doesn't exist");
            }
        }
        if (config.getProdFolderEDI1() == null) {
            log.error("During YAML reading, found that ProdFolderEDI1 Information is not provided. Closing application");
            throw new RuntimeException("During YAML reading, found that ProdFolderEDI1 Information is not provided. Closing application");
        } else {
            File folder = new File(config.getProdFolderEDI1());
            if (!folder.exists()) {
                log.error("Provided ProdFolderEDI1 [" + folder + "] doesn't exist");
                throw new RuntimeException("Provided ProdFolderEDI1 doesn't exist");
            }
        }
        if (config.getArchiveFolderEDI1() == null) {
            log.error("During YAML reading, found that ArchiveFolderEDI1 Information is not provided. Closing application");
            throw new RuntimeException("During YAML reading, found that ArchiveFolderEDI1 Information is not provided. Closing application");
        } else {
            File folder = new File(config.getArchiveFolderEDI1());
            if (!folder.exists()) {
                log.error("Provided ArchiveFolderEDI1 [" + folder + "] doesn't exist");
                throw new RuntimeException("Provided ArchiveFolderEDI1 doesn't exist");
            }
        }
        if (config.getErrorFolderEDI2() == null) {
            log.error("During YAML reading, found that ErrorFolderEDI2 Information is not provided. Closing application");
            throw new RuntimeException("During YAML reading, found that ErrorFolderEDI2 Information is not provided. Closing application");
        } else {
            File folder = new File(config.getErrorFolderEDI2());
            if (!folder.exists()) {
                log.error("Provided ErrorFolderEDI2 [" + folder + "] doesn't exist");
                throw new RuntimeException("Provided ErrorFolderEDI2 doesn't exist");
            }
        }
        if (config.getProdFolderEDI2() == null) {
            log.error("During YAML reading, found that ProdFolderEDI2 Information is not provided. Closing application");
            throw new RuntimeException("During YAML reading, found that ProdFolderEDI2 Information is not provided. Closing application");
        } else {
            File folder = new File(config.getProdFolderEDI2());
            if (!folder.exists()) {
                log.error("Provided ProdFolderEDI2 [" + folder + "] doesn't exist");
                throw new RuntimeException("Provided ProdFolderEDI2 doesn't exist");
            }
        }
        if (config.getArchiveFolderEDI2() == null) {
            log.error("During YAML reading, found that ArchiveFolderEDI2 Information is not provided. Closing application");
            throw new RuntimeException("During YAML reading, found that ArchiveFolderEDI2 Information is not provided. Closing application");
        } else {
            File folder = new File(config.getArchiveFolderEDI2());
            if (!folder.exists()) {
                log.error("Provided ArchiveFolderEDI2 [" + folder + "] doesn't exist");
                throw new RuntimeException("Provided ArchiveFolderEDI2 doesn't exist");
            }
        }
    }
}
