package nekio.common.json;

/**
 * JsonTools
 * 
 * @author Nekio 
 * (nekio@outlook.com)
 * 
 * @creationDate    Oct. 01, 2015
 * @lastUpdate      Oct. 02, 2015
 */

// <editor-fold defaultstate="collapsed" desc="Libraries">
import nekio.common.json.gui.GUI;
import nekio.common.json.util.JsonConverter;
import nekio.common.json.util.JsonDebugger;
import org.json.JSONObject;
// </editor-fold>

public class Test {
    // <editor-fold defaultstate="collapsed" desc="Main">
    public static void main(String[] args) {
        new GUI();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Debug">
    private void debug(String path){
        JsonDebugger debug = new JsonDebugger();
        
        String text = JsonConverter.fileToText(path);
                
        JSONObject json = new JSONObject(text);
        debug.readJson(json);
        String xml = org.json.XML.toString(json);
        System.out.println(debug.readXML(xml));
    }
    // </editor-fold>
}
