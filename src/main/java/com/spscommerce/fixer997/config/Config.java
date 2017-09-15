package com.spscommerce.fixer997.config;

import java.util.Properties;

/**
 * Created by ikornienko on 9/1/2017.
 */
public class Config {
    private String login;
    private String password;
    private String emailFolder;
    private String toRecipient;
    private String ccRecipient;
    private String imapHost;
    private int emailFault;
    private int minMessageCount;
    private Properties properties;
    private String errorFolderEDI1;
    private String prodFolderEDI1;
    private String archiveFolderEDI1;
    private String errorFolderEDI2;
    private String prodFolderEDI2;
    private String archiveFolderEDI2;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailFolder() {
        return emailFolder;
    }

    public void setEmailFolder(String emailFolder) {
        this.emailFolder = emailFolder;
    }

    public String getToRecipient() {
        return toRecipient;
    }

    public void setToRecipient(String toRecipient) {
        this.toRecipient = toRecipient;
    }

    public String getCcRecipient() {
        return ccRecipient;
    }

    public void setCcRecipient(String ccRecipient) {
        this.ccRecipient = ccRecipient;
    }

    public String getImapHost() {
        return imapHost;
    }

    public int getEmailFault() {
        return emailFault;
    }

    public void setEmailFault(int emailFault) {
        this.emailFault = emailFault;
    }

    public void setImapHost(String imapHost) {
        this.imapHost = imapHost;
    }

    public int getMinMessageCount() {
        return minMessageCount;
    }

    public void setMinMessageCount(int minMessageCount) {
        this.minMessageCount = minMessageCount;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getErrorFolderEDI1() {
        return errorFolderEDI1;
    }

    public void setErrorFolderEDI1(String errorFolderEDI1) {
        this.errorFolderEDI1 = errorFolderEDI1;
    }

    public String getProdFolderEDI1() {
        return prodFolderEDI1;
    }

    public void setProdFolderEDI1(String prodFolderEDI1) {
        this.prodFolderEDI1 = prodFolderEDI1;
    }

    public String getArchiveFolderEDI1() {
        return archiveFolderEDI1;
    }

    public void setArchiveFolderEDI1(String archiveFolderEDI1) {
        this.archiveFolderEDI1 = archiveFolderEDI1;
    }

    public String getErrorFolderEDI2() {
        return errorFolderEDI2;
    }

    public void setErrorFolderEDI2(String errorFolderEDI2) {
        this.errorFolderEDI2 = errorFolderEDI2;
    }

    public String getProdFolderEDI2() {
        return prodFolderEDI2;
    }

    public void setProdFolderEDI2(String prodFolderEDI2) {
        this.prodFolderEDI2 = prodFolderEDI2;
    }

    public String getArchiveFolderEDI2() {
        return archiveFolderEDI2;
    }

    public void setArchiveFolderEDI2(String archiveFolderEDI2) {
        this.archiveFolderEDI2 = archiveFolderEDI2;
    }

}
