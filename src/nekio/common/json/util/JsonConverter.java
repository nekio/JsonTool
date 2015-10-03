package nekio.common.json.util;

/**
 *
 * @author Nekio
 */

// <editor-fold defaultstate="collapsed" desc="Libraries">
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
// </editor-fold>

public class JsonConverter {
    // <editor-fold defaultstate="collapsed" desc="Global Methods">
    public static String fileToText(String path) {
        StringBuilder text = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String currentLine = null;

            while ((currentLine = br.readLine()) != null) {
                text.append(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }
    // </editor-fold>
}
