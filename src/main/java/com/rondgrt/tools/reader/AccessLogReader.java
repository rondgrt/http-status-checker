package com.rondgrt.tools.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccessLogReader {

    public List<String> getUniqueLinesCount() {
        Set<String> uniqueLines = new HashSet<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("/Users/groot100/Documents/logs/hlnwebdesktop_v0_access_log-20201004"));
//            reader = new BufferedReader(new FileReader("/Users/groot100/Documents/logs/hln-logs-test-file"));
            String line = reader.readLine();
            while(line != null) {
                uniqueLines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(uniqueLines);
    }

}
