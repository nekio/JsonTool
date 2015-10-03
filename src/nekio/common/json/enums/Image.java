package nekio.common.json.enums;

/**
 *
 * @author Nekio
 */

public enum Image {
    // <editor-fold defaultstate="collapsed" desc="Values">
    Leaf0("leaf0.png"),
    Leaf1("leaf1.png"),
    Leaf2("leaf2.png"),
    List("list.png"),
    Element("element.png"),
    Warning("warning.png"),
    Open("open.png"),
    Close("close.png");
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Atributtes">
    private final String RELATIVE_PATH = "/nekio/common/json/img/";
    private String path;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    private Image(String value){
        this.path = RELATIVE_PATH + value;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    public String getPath(){
        return path;
    }
    // </editor-fold>
}
