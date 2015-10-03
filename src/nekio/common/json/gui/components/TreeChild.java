package nekio.common.json.gui.components;

/*
 *
 * @author Nekio
 */

// <editor-fold defaultstate="collapsed" desc="Libraries">
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URI;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import nekio.common.json.enums.Image;
import org.json.JSONArray;
import org.json.JSONObject;
// </editor-fold>

public class TreeChild extends JPanel {
    // <editor-fold defaultstate="collapsed" desc="Atributtes">
    private final String SEPARATOR = "|";
    private JTree tree;
    
    private JPopupMenu popup;
    private JMenuItem popCopyKey;
    private JMenuItem popCopyValue;
    private JMenuItem popCopyNode;
    private JMenuItem popCopyNodePath;
    
    private JSONObject root;
    private String rootName;
    private String nodeName;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">    
    public TreeChild(JSONObject root, String rootName) {
        this.root = root;
        this.rootName = rootName;

        activate();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="GUI Methods">
    private void activate(){        
        loadInfo();
        loadPopup();
        loadListeners();
    }
    
    private void loadInfo() {
        this.setLayout(new BorderLayout());

        DefaultMutableTreeNode treeNodes = addBranch(null, root, rootName);
        tree = new JTree(treeNodes);
        
        
        /*Si descomentamos esto, agrega iconos personalizados a las ramas*/
        /*DefaultTreeCellRenderer render = (DefaultTreeCellRenderer) tree.getCellRenderer();
        
        render.setLeafIcon(new ImageIcon(this.getClass().getResource(Image.Leaf0.getPath())));
        render.setOpenIcon(new ImageIcon(this.getClass().getResource(Image.Open.getPath())));
        render.setClosedIcon(new ImageIcon(this.getClass().getResource(Image.Close.getPath())));*/
        
        tree.setCellRenderer(new DefaultTreeCellRenderer() {
            private Icon leaf0 = new ImageIcon(this.getClass().getResource(Image.Leaf0.getPath()));
            private Icon leaf1 = new ImageIcon(this.getClass().getResource(Image.Leaf1.getPath()));
            private Icon leaf2 = new ImageIcon(this.getClass().getResource(Image.Leaf2.getPath()));
            private Icon open = new ImageIcon(this.getClass().getResource(Image.Open.getPath()));
            private Icon close = new ImageIcon(this.getClass().getResource(Image.Close.getPath()));
            private Icon warning = new ImageIcon(this.getClass().getResource(Image.Warning.getPath()));
            private Icon list = new ImageIcon(this.getClass().getResource(Image.List.getPath()));
            private Icon element = new ImageIcon(this.getClass().getResource(Image.Element.getPath()));
            
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,boolean isLeaf, int row, boolean focused) {
                this.closedIcon = close;
                this.openIcon = open; 
                this.leafIcon = leaf1;
                
                Component component = super.getTreeCellRendererComponent(tree, value, selected, expanded, isLeaf, row, focused);
                
                String trimValue = value.toString().replaceAll("\\s+","");
                if(value.toString().contains(" [List]")){
                    setIcon(list);
                }else if(trimValue.toUpperCase().contains("=FALSE")){
                    setIcon(leaf0);
                }else if(trimValue.toUpperCase().contains("=NULL")){
                    setIcon(leaf0);
                }else if(trimValue.contains("=0")){
                    setIcon(leaf0);
                }
                else if(trimValue.endsWith("=")){
                    setIcon(warning);
                }
                
                return component;
            }
        });
        
        JScrollPane scroll = new JScrollPane();
        scroll.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT); //Scroll Location
        scroll.getViewport().add(tree);

        this.add(BorderLayout.CENTER, scroll);
    }
    
    private void loadPopup(){
        popup = new JPopupMenu();
        
        popCopyKey=new JMenuItem("Copiar Llave");
        popup.add(popCopyKey);
        
        popCopyValue=new JMenuItem("Copiar Valor");
        popup.add(popCopyValue);
        
        popCopyNode=new JMenuItem("Copiar Nodo");
        popup.add(popCopyNode);
        
        popCopyNodePath=new JMenuItem("Copiar Ruta Nodo");
        popup.add(popCopyNodePath);
        
        tree.add(popup);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Listeners">
    private void loadListeners() {
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                //DefaultMutableTreeNode node=(DefaultMutableTreeNode)e.getPath().getLastPathComponent();
                nodeName = e.getPath().toString().replace(", ", SEPARATOR);
                nodeName = nodeName.replace(rootName, "<" + rootName + "> ");
            }
        });

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouse) {
                if (mouse.getClickCount() == 2) {
                    tryToOpen(getStringValueNode());
                }
            }
        });
        
        tree.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent mouse){
                if (mouse.getButton()==MouseEvent.BUTTON3){
                    popup.show(tree, mouse.getX(), mouse.getY());
                }
            }
        });
        
        popCopyKey.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed( ActionEvent evt){
                String text = getStringKeyNode();
                
                toClipboard(text);
            }
        });
        
        popCopyValue.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed( ActionEvent evt){
                String text = getStringValueNode();
                
                toClipboard(text);
            }
        });
        
        popCopyNode.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed( ActionEvent evt){
                String text = getStringNode();
                
                toClipboard(text);
            }
        });
        
        popCopyNodePath.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed( ActionEvent evt){
                toClipboard(nodeName);
            }
        });
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Add Branch">
    private DefaultMutableTreeNode addBranch(DefaultMutableTreeNode treeNode, JSONObject json, String name) {
        DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(name);
        
        if (treeNode != null) //Solo debe ser nulo en la raiz
            treeNode.add(currentNode);
        
        String nodeName = null;
        String keyString = null;
        Object keyValue = null;

        for (Object key : json.keySet()) {
            keyString = (String) key;
            keyValue = json.get(keyString);

            if (keyValue instanceof JSONObject || keyValue instanceof JSONArray)
                nodeName = keyString; 
            else
                nodeName = keyString + " = " + keyValue.toString(); 
            
            detectJsonType(currentNode, keyValue, nodeName);
        }
        
        return currentNode;
    }

    private void readJsonArrayKeyValues(DefaultMutableTreeNode treeNode,JSONArray jsonArray, String name) {
        boolean isJsonList = false;
        DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(name + " [List]"); // Only used in case of key instanceof JSONObject
        
        String aux = null;
        for (Object key : jsonArray) {
            
            if(!(key instanceof JSONObject)){
                isJsonList = true;
                currentNode.add(new DefaultMutableTreeNode(name + " = " + key.toString()));
                continue;
            }
            else
                aux = name;
            
            detectJsonType(treeNode, key, aux);
        }
        
        if(isJsonList)
            treeNode.add(currentNode);
    }

    private void detectJsonType(DefaultMutableTreeNode treeNode, Object object, String name) {
        if (object instanceof JSONObject) {
            addBranch(treeNode, (JSONObject) object, name);
        } else if (object instanceof JSONArray) {
            readJsonArrayKeyValues(treeNode, (JSONArray) object, name);
        }else{
            treeNode.add(new DefaultMutableTreeNode(name));
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Utils">
    public void expandTree(){
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }
    
    public void collapseTree(){
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.collapseRow(i);
        }
    }
    
    private String getStringKeyNode(){
        String text = "";
        
        try{
            text = getStringNode();
            text = text.substring(0, text.lastIndexOf("=") - 1);
            text = text.trim();
        }catch(Exception e){}
        
        return text;
    }
    
    private String getStringValueNode(){
        String text = "";
        
        try{
            text = getStringNode();
            text = text.substring(text.lastIndexOf("=") + 1);
            text = text.substring(0, text.length());
            text = text.trim();
        }catch(Exception e){}
        
        return text;
    }
    
    private String getStringNode(){
        String text = "";
        
        try{
            text = nodeName; 
            text = text.replace("<" + rootName + "> ", "");
            text = text.substring(text.lastIndexOf(SEPARATOR) + 1);
            text = text.substring(0, text.length() - 1);
        }catch(Exception e){}
        
        return text;
    }
    
    private void toClipboard(String text){
        StringSelection stringSelection = new StringSelection(text);
        
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
    
    private void tryToOpen(String text){
        if(Desktop.isDesktopSupported()){

            try{
                Desktop.getDesktop().browse(new URI(text));
            }catch(Exception e){
                try{
                    File file = new File(text);

                    if(file.exists())
                        Desktop.getDesktop().open(file);
                }catch(Exception ex){}
            }
        }
        
    }
    // </editor-fold>
}