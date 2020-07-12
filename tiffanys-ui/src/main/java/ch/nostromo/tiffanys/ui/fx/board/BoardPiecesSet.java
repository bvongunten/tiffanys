package ch.nostromo.tiffanys.ui.fx.board;

import javafx.scene.image.Image;

public class BoardPiecesSet {

    private static final String WP_FILE = "wp.png";
    private static final String WN_FILE = "wn.png";
    private static final String WB_FILE = "wb.png";
    private static final String WR_FILE = "wr.png";
    private static final String WQ_FILE = "wq.png";
    private static final String WK_FILE = "wk.png";
    
    private static final String BP_FILE = "bp.png";
    private static final String BN_FILE = "bn.png";
    private static final String BB_FILE = "bb.png";
    private static final String BR_FILE = "br.png";
    private static final String BQ_FILE = "bq.png";
    private static final String BK_FILE = "bk.png";
    
    Image dd;
    
    Image wp;
    Image wn;
    Image wb;
    Image wr;
    Image wq;
    Image wk;
    
    Image bp;
    Image bn;
    Image bb;
    Image br;
    Image bq;
    Image bk;
        
    public Image getDD() {
        return dd;
    }
    
    public BoardPiecesSet() {
        String path = "/pieces/standard/";
        
        dd = new Image(getClass().getResourceAsStream(path + WP_FILE));
        
        wp = new Image(getClass().getResourceAsStream(path + WP_FILE)); 
        wn = new Image(getClass().getResourceAsStream(path + WN_FILE)); 
        wb = new Image(getClass().getResourceAsStream(path + WB_FILE)); 
        wr = new Image(getClass().getResourceAsStream(path + WR_FILE)); 
        wq = new Image(getClass().getResourceAsStream(path + WQ_FILE)); 
        wk = new Image(getClass().getResourceAsStream(path + WK_FILE)); 
        
        bp = new Image(getClass().getResourceAsStream(path + BP_FILE)); 
        bn = new Image(getClass().getResourceAsStream(path + BN_FILE)); 
        bb = new Image(getClass().getResourceAsStream(path + BB_FILE)); 
        br = new Image(getClass().getResourceAsStream(path + BR_FILE)); 
        bq = new Image(getClass().getResourceAsStream(path + BQ_FILE)); 
        bk = new Image(getClass().getResourceAsStream(path + BK_FILE)); 
    }
    
    public Image getWhitePawn() {
        return wp;
    }

    public Image getWhiteKnight() {
        return wn;
    }

    public Image getWhiteBishop() {
        return wb;
    }

    public Image getWhiteRook() {
        return wr;
    }

    public Image getWhiteQueen() {
        return wq;
    }

    public Image getWhiteKing() {
        return wk;
    }
    
    public Image getBlackPawn() {
        return bp;
    }

    public Image getBlackKnight() {
        return bn;
    }

    public Image getBlackBishop() {
        return bb;
    }

    public Image getBlackRook() {
        return br;
    }

    public Image getBlackQueen() {
        return bq;
    }

    public Image getBlackKing() {
        return bk;
    }
    
}
