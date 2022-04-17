package ch.nostromo.tiffanys.dragonborn.engine.board;

public class RobustBoardPiecePositions implements Cloneable {

    public int pieceCount;

    public int[] positions;

    private RobustBoard board;

    private int zobristColor;
    private int zobristType;

    public RobustBoardPiecePositions(RobustBoard board, int zobristColor, int zobristType) {
        this.board = board;
        this.positions = new int[8];
        this.pieceCount = 0;
        this.zobristColor = zobristColor;
        this.zobristType = zobristType;
    }

    public void addPiece(int position) {
        this.positions[pieceCount] = position;
        this.pieceCount++;

        // Add to zobrist key
     //   board.zobristKey ^= Zobrist.ZOBRIST_FACTORS[zobristColor][zobristType][position];

    }

    public void removePiece(int idx) {

        // Remove from zobrist key
       // board.zobristKey ^= Zobrist.ZOBRIST_FACTORS[zobristColor][zobristType][positions[idx]];

        this.pieceCount--;
        this.positions[idx] = this.positions[pieceCount];

        if (pieceCount > 0) {
            board.boardPieceIndexes[this.positions[idx]] = idx;
        }

        this.positions[pieceCount] = 0;
    }

    public void insertPiece(int idx, int position) {
        this.positions[pieceCount] = positions[idx];

        if (idx != pieceCount) {
            board.boardPieceIndexes[this.positions[pieceCount]] = pieceCount;
        }

        this.positions[idx] = position;
        this.pieceCount++;

        // Add to zobrist key
        // board.zobristKey ^= Zobrist.ZOBRIST_FACTORS[zobristColor][zobristType][position];

    }

    public void updatePosition(int idx, int position) {
        //board.zobristKey ^= Zobrist.ZOBRIST_FACTORS[zobristColor][zobristType][positions[idx]];

        this.positions[idx] = position;

        //board.zobristKey ^= Zobrist.ZOBRIST_FACTORS[zobristColor][zobristType][positions[idx]];
    }

    public RobustBoardPiecePositions clone(RobustBoard newBoard) throws CloneNotSupportedException {
        RobustBoardPiecePositions result = (RobustBoardPiecePositions) super.clone();
        result.positions = positions.clone();
        result.board = newBoard;
        result.zobristColor = zobristColor;
        result.zobristType = zobristType;
        return result;
    }

}
