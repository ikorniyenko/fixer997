package com.spscommerce.fixer997.app;

import com.spscommerce.fixer997.FolderHandler;
import com.spscommerce.fixer997.MailWorker;
import com.spscommerce.fixer997.config.Config;
import com.spscommerce.fixer997.config.ConfigReader;

/**
 * Created by ikornienko on 8/1/2017.
 */
public class AppRunner {
    public static void main(String[] args) throws Exception {
        ConfigReader configReader = new ConfigReader();
        Config config = configReader.readYAML(); // Reading YAML Config
        try (MailWorker mailWorker = new MailWorker(config)) {
            FolderHandler checkFolder = new FolderHandler(mailWorker);
            checkFolder.folderAnalyzer(config.getErrorFolderEDI2(), config.getProdFolderEDI2(), config.getArchiveFolderEDI2(), "EDI2");
            checkFolder.folderAnalyzer(config.getErrorFolderEDI1(), config.getProdFolderEDI1(), config.getArchiveFolderEDI1(), "EDI1");
        }
    }
}
