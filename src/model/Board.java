package model;

import enums.Color;

/**
 * Class which create the game board.
 */
public class Board {

    private Piece[][] board;
    private int rowSize;
    private int columnSize;

    public int getRowSize() {
        return this.rowSize;
    }

    public int getColumnSize() {
        return this.columnSize;
    }

    public Board(int row, int column) {
        board = new Piece[row][column];
        this.rowSize = row;
        this.columnSize = column;

        // Set the pawns for white and black
        initBoard();
    }

    private void initBoard() {
        initWhitePieces();
        initBlackPieces();
        initBlankSpace();
    }

    /**
     * Initialize blank spaces.
     * At the beginning, two lines are blank between the players
     */
    private void initBlankSpace() {
        int nbLinesPlayer = (rowSize/2)-1;
        for(int i = nbLinesPlayer; i < nbLinesPlayer+1; i++) {
            for(int j = 0; j < columnSize; j++) {
                board[i][j] = null;
            }
        }
    }

    /**
     * Initialize black pawns on the board.
     */
    private void initBlackPieces() {
        int nbLinesBlack = (rowSize/2)-1;
        int id = 51;
        for(int i = 0; i < nbLinesBlack; i++) {
            for(int j = 0; j < columnSize; j++) {                   
                if((i + j) % 2 == 1) { // every odds case except (0,0) contains a black pawn
                    board[i][j] = new Pawn(Color.BLACK, new Position(i,j));
                    board[i][j].setID(id);                    
                    id++;
                    System.out.println("My id : " + board[i][j].getID());
                } else {
                    board[i][j] = null;
                }
            }
        }
    }

    /**
     * Initialize white pawns on the board.
     */
    private void initWhitePieces() {
        int rowWhiteBegin = (rowSize/2)+1;
        int id = 1;
        for(int i = rowWhiteBegin; i < rowSize; i++) {
            for(int j = 0; j < columnSize; j++) {
                if((i + j) % 2 == 1) {
                    board[i][j] = new Pawn(Color.WHITE, new Position(i,j));
                    board[i][j].setID(id);
                    id++;
                    System.out.println("My id : " + board[i][j].getID());
                } else {
                    board[i][j] = null;
                }
            }
        }
    }

    public Piece getPiece(int line, int column) {
        return board[line][column];
    }

    public Piece getPiece(Position pos) {
        return board[pos.getLine()][pos.getColumn()];
    }
    
    public Piece getPieceByID(int ID) {
        Piece piece = null;
        for(int i = 0; i < rowSize; i++) {
            for(int j = 0; j < columnSize; j++) {
                if(hasPiece(i,j)) {
                    if(board[i][j].getID() == ID) {
                        piece = board[i][j];
                        break;
                    }
                }  
            }
        }
        return piece;
    }
    
    public Color getColorPiece(Position pos) {
        return board[pos.getLine()][pos.getColumn()].getColor();
    }

    public Color getColorPiece(int line, int column) {
        return board[line][column].getColor();
    }

    public boolean hasPiece(Position pos) {
        if(board[pos.getLine()][pos.getColumn()] != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasPiece(int line, int column) {
        if(board[line][column] != null) {
            return true;
        } else {
            return false;
        }
    }
    
    public void removePiece(Position pos) {
        board[pos.getLine()][pos.getColumn()] = null;
    }

    public void setPiece(Piece piece, Position pos) {
        board[pos.getLine()][pos.getColumn()] = piece;
    }

    public void setPiece(Piece piece, int line, int column) {
        board[line][column] = piece;
    }

    public Piece[][] getBoard() {
        return board;
    }
    
    /**
     * After each turn, tell the piece that they can move.
     */
    public void resetPiecesHasPlayed() {
        for(int i = 0; i < rowSize; i++) {
            for(int j = 0; j < columnSize; j++) {
                if(board[i][j] != null) {
                    board[i][j].setHasPlayedThisTurn(false);
                }
            }
        }
    }
    
    @Override
    public String toString() {
        String str = "";
        
        for(int i = 0; i < rowSize; i++) {
            for(int j = 0; j < columnSize; j++) {
                if(board[i][j] != null) {
                    if(getColorPiece(i,j) == Color.WHITE) {
                        System.out.print(" W ");
                    } else {
                        System.out.print(" B ");
                    }
                } else {
                    System.out.print(" . ");
                }
            }
            System.out.println();
        }
        
        return str;
    }
}
