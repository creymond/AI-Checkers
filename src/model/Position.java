package model;

/**
 * Class which gives the position on the board.
 */
public class Position {

    private final int line;
    private final int column;
    
    public Position() {
        this.line = -1;
        this.column = -1;
    }

    public Position(int l, int c) {
        this.line = l;
        this.column = c;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public boolean isSamePos(Position p) {
        return (this.line == p.getLine() && this.column == p.getColumn());
    }
    
    @Override
    public String toString() {
        String str = "(" + line + "," + column + ")";
        return str;
    }
}
