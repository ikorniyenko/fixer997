package com.spscommerce.fixer997;

import org.junit.*;

import java.io.File;

/**
 * Created by ikornienko on 8/3/2017.
 */
public class FolderHandlerTest {

    private MailWorker mailWorker;
    private FolderHandler fr;
    @Before
    public void setUp() throws Exception {
        fr = new FolderHandler(mailWorker);
    }

    @After
    public void tearDown() throws Exception {
        fr  = null;
    }

    @Test
    public void testNameSplitter() throws Exception {
        String initialName="EDI1.edi";
        String expectedName="EDI1.edi";
        fr.nameSplitter(initialName);
        Assert.assertEquals(expectedName,fr.nameSplitter(initialName));
    }

    @Test
    public void testNameSplitter2() throws Exception {
        String initialName="12345_232_EDI1.edi";
        String expectedName="EDI1.edi";
        fr.nameSplitter(initialName);
        Assert.assertEquals(expectedName,fr.nameSplitter(initialName));
    }

   @Ignore
    @Test
    public void testAcrhiveFile() throws Exception{
        File newFile = new File("D:/work/DirectEDI/08182017/20170907_151508_ED14072742703.edi");
        File arcFolder = new File("D:/work/DirectEDI/08182017/archive");
        fr.archiveFile(arcFolder,newFile);
    }

    @Ignore
    @Test
    public void testWriteAndMove() throws Exception {
        File newFile1 = new File("D:/work/DirectEDI/08182017/20170907_151508_ED14072742703 - Copy.edi");
        File prodFolder = new File("D:/work/DirectEDI/08182017/updated");
        fr.writeAndMoveFile("bla bla bla", newFile1, prodFolder, "UTF-8");
    }

}