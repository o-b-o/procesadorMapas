// NEW CLASS: Header.java
package com.proyecto;
import java.util.*;

public class Header {
    private Map<String, String> metadata;
    
    public Header() {
        this.metadata = new HashMap<>();
    }
    
    public void addMetadata(String key, String value) {
        metadata.put(key, value);
    }
    
    public String getMetadata(String key) {
        return metadata.get(key);
    }
    
    public String toCSVHeader() {
        // Format: # key1=value1; key2=value2; key3=value3
        StringBuilder sb = new StringBuilder("# ");
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
        }
        return sb.toString().trim();
    }
    
    public static Header fromCSVHeader(String line) {
        Header h = new Header();
        if (line.startsWith("# ")) {
            String content = line.substring(2);
            String[] pairs = content.split("; ");
            for (String pair : pairs) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2) {
                    h.addMetadata(kv[0].trim(), kv[1].trim());
                }
            }
        }
        return h;
    }
}
