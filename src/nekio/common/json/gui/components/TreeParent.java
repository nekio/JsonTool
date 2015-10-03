package nekio.common.json.gui.components;

/**
 *
 * @author Nekio
 */

// <editor-fold defaultstate="collapsed" desc="Libraries">
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.json.JSONObject;
// </editor-fold>

public class TreeParent extends JPanel {
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    private TreeChild tree;
    
    private JPanel pnlTreeChild;
    private JButton btnCollapseTree;
    private JButton btnExpandTree;
    
    private JSONObject root;
    private String rootName;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public TreeParent(JSONObject root, String rootName){
        if(root != null && rootName != null){
            this.root = root;
            this.rootName = rootName;
            
            addComponents();
            addListeners();
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="GUI Methods">
    private void addComponents(){     
        this.setLayout(new BorderLayout());
        
        // North
        JPanel pnlTreeButtons = new JPanel(new GridLayout(1,2));
        btnCollapseTree = new JButton("Contraer Arbol");
        pnlTreeButtons.add(btnCollapseTree);
        
        btnExpandTree = new JButton("Expandir Arbol");
        pnlTreeButtons.add(btnExpandTree);
        
        this.add(pnlTreeButtons, BorderLayout.NORTH);
        
        // Center
        tree = null;
        tree = new TreeChild(root, rootName);
        tree.setOpaque(false);
        
        this.add(tree, BorderLayout.CENTER);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Listeners">
    private void addListeners() {
        btnCollapseTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                tree.collapseTree();
            }
        });
        
        btnExpandTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                tree.expandTree();
            }
        });
    }
    // </editor-fold>
}
