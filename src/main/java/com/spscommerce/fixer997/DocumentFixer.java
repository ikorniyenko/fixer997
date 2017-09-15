package com.spscommerce.fixer997;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by ikornienko on 8/1/2017.
 */
public class DocumentFixer {
    public static Logger log = Logger.getLogger(DocumentFixer.class); // logger initialization

    public String fileAnalyzer(String fileData) {
        String updatedFileText = "";
        try {
            if (fileData.length() > 106) { // if no, it is not EDI file
                String segDel = fileData.substring(105, 106); // Find segment delimiter
                String elDel = fileData.substring(3, 4); // Find element delimiter
                if (elDel.equals("*")) elDel = "\\*";// exception without this
                String[] segs = fileData.split(segDel); // List of all segments in document
                Optional<String> st = Arrays.stream(segs).filter(c -> c.substring(0, 2).equals("ST")).findAny(); // Find ST segment
                if (st.isPresent()) { //Without this we can not identify document number
                    String[] fields = st.get().split(elDel); // Find all elements in ST segment
                    if (fields[1].equals("997")) { // We don't need non 997 documents
                        Optional<String> ta1 = Arrays.stream(segs).filter(c -> c.substring(0, 3).equals("TA1")).findAny(); // Find TA1 segment
                        if (ta1.isPresent()) { // If not - this is not our error type
                            updatedFileText = fileFixer(fileData, segs, segDel);
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            log.warn("We can not read file correctly. Looks like file has strange Non EDI format\n" + e);
        } catch (Exception e) {
            log.warn("We can not read file correctly. Who knows why\n" + e);
        }
        return updatedFileText;
    }

    public String fileFixer(String initialData, String[] segmentList, String segmentDelimiter) {
        String correctFile = "";
        for (int i = 0; i < segmentList.length; i++) {
            if (segmentList[i].substring(0, 3).equals("TA1")) {
                correctFile = initialData.replace(segmentDelimiter + segmentList[i], "");
                ; // Need to delete TA1 info with seg delimiter, that's why we send segmentDelimiter+segmentList[i] as a parameter
            }
        }
        return correctFile;
    }
}
