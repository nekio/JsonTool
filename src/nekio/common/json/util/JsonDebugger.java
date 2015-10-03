package nekio.common.json.util;

/**
 *
 * @author Nekio
 */

// <editor-fold defaultstate="collapsed" desc="Libraries">
import org.json.JSONArray;
import org.json.JSONObject;
// </editor-fold>

public class JsonDebugger {
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    private final String XML_IDENTS = "   ";
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Exposed Methods">
    public void readJson(JSONObject json) {
        readingFrom("JSON");
        readJsonKeyValues(json, "");
    }
    
    public String readXML(String xml) {
        readingFrom("XML");
        
        if (xml == null || xml.trim().length() == 0) {
            return "";
        }

        int stack = 0;
        StringBuilder pretty = new StringBuilder();
        String[] rows = xml.trim().replaceAll(">", ">\n").replaceAll("<", "\n<").split("\n");

        for (int i = 0; i < rows.length; i++) {
            if (rows[i] == null || rows[i].trim().length() == 0) {
                continue;
            }

            String row = rows[i].trim();
            
            if (row.startsWith("<?")) { // xml version tag
                pretty.append(row + "\n");
            } else if (row.startsWith("</")) { // closing tag
                String indent = repeatString(--stack);
                
                if(stack == 0)
                    pretty.append(indent + row + "\n\n");
                else
                    pretty.append(indent + row + "\n");
            } else if (row.startsWith("<")) { // starting tag
                String indent = repeatString(stack++);
                pretty.append(indent + row + "\n");
            } else { // tag data
                String indent = repeatString(stack);
                pretty.append(indent + row + "\n");
            }
        }

        return pretty.toString().trim();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Internal Methods">
    private void readJsonKeyValues(JSONObject json, String spaces) {
        String keyString = null;
        Object keyValue = null;

        for (Object key : json.keySet()) {
            keyString = (String) key;
            keyValue = json.get(keyString);

            System.out.println(spaces + "K: " + keyString + ", V: " + keyValue);

            detectJsonType(keyValue, spaces);
        }
    }

    private void readJsonArrayKeyValues(JSONArray jsonArray, String spaces) {
        for (Object key : jsonArray) {
            detectJsonType(key, spaces);
        }
    }

    private void detectJsonType(Object object, String spaces) {
        if (object instanceof JSONObject) {
            readJsonKeyValues((JSONObject) object, spaces + "  ");
        } else if (object instanceof JSONArray) {
            readJsonArrayKeyValues((JSONArray) object, spaces + "  ");
        }
    }

    private String repeatString(int stack) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < stack; i++) {
            indent.append(XML_IDENTS);
        }

        return indent.toString();
    }
    
    private void readingFrom(String source){
        System.out.println("\n------------------");
        System.out.println("READING " + source + " ...");
        System.out.println("------------------");
    }
    // </editor-fold>
}
