package ch.nostromo.tiffanys.ui.fx.board;

public interface BoardPaneEvents {

    void fireClickMoveStarted(int fieldIdx);
    void fireClickMoveFinished(int fromFieldIdx, int toFieldIdx);

    void fireDragMoveStarted(int fieldIdx);
    void fireDragMoveFinished(int fromFieldIdx, int toFieldIdx);
    
    void interactionAborted();
     
    void leftclick(int fieldIdx);
    void rightClick(int fieldIdx);
    void doubleClick(int fieldIdx);

    
}
