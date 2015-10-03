package nekio.common.json.gui;

/**
 *
 * @author Nekio
 */

// <editor-fold defaultstate="collapsed" desc="Libraries">
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import nekio.common.json.gui.components.TreeParent;
import nekio.common.json.util.JsonConverter;
import org.json.JSONException;
import org.json.JSONObject;
// </editor-fold>

public class GUI extends JFrame {
    // <editor-fold defaultstate="collapsed" desc="Atributtes">
    private Container container;
    private JPanel pnlNorth;
    private JPanel pnlCenter;
    private JPanel pnlSouth;

    private JTextField txtFile;
    private JButton btnOpenFile;

    private String jsonFilename;
    private JSONObject json;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public GUI() {
        activate();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="GUI Methods">
    private void activate() {
        this.setTitle("JSON Tools");
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(720, 640));
        this.setLocationRelativeTo(null);

        addComponents();
        addListeners();

        initialize();
    }
    
    private void addComponents() {
        container = this.getContentPane();
        
        activatePanels();
    }

    private void initialize() {
        json = null;
        jsonFilename = null;

        this.setVisible(true);
    }
    
    private void activatePanels(){
        activatePanel(activateNorthPanel(), BorderLayout.NORTH);
        activatePanel(activateCenterPanel(), BorderLayout.CENTER);
    }
    
    private void activatePanel(JPanel panel, String location) {
        container.add(panel, location);
    }
    
    private void deactivatePanel(JPanel panel) {
        if (panel != null)
            panel.setVisible(false);
        
        panel = null;
    }

    private JPanel activateNorthPanel() {
        pnlNorth = new JPanel(new BorderLayout());

        pnlNorth.add(new JLabel("Ruta JSON: "), BorderLayout.WEST);

        txtFile = new JTextField();
        txtFile.setEditable(false);
        pnlNorth.add(txtFile, BorderLayout.CENTER);

        btnOpenFile = new JButton("...");
        pnlNorth.add(btnOpenFile, BorderLayout.EAST);

        return pnlNorth;
    }

    private JPanel activateCenterPanel() {
        pnlCenter = new TreeParent(json, jsonFilename);

        return pnlCenter;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Listeners">
    private void addListeners() {
        btnOpenFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                selectFile();
                readFile();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                exit();
            }
        });
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Behaviours">
    private void selectFile() {
        JFileChooser fileExplorer = new JFileChooser();
        fileExplorer.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileExplorer.showSaveDialog(null);
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        } else {
            jsonFilename = fileExplorer.getSelectedFile().getName();
            txtFile.setText(fileExplorer.getSelectedFile().getAbsolutePath());
        }
    }

    private void readFile() {
        String text = JsonConverter.fileToText(txtFile.getText());
        try{
            json = new JSONObject(text);

            deactivatePanel(pnlCenter);
            activatePanel(activateCenterPanel(), BorderLayout.CENTER);
        }catch(JSONException je){
            showMessage("JSON Error:\n" + je.getMessage());
        }catch(Exception e){
            showMessage(e.getMessage());
        }
    }

    public void showMessage(String text) {
        JOptionPane.showMessageDialog(null, text, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    private void exit() {
        this.setVisible(false);
        this.dispose();
    }
    // </editor-fold>
}
