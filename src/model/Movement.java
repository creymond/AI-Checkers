package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which describes the movement of pieces.
 */
public class Movement {

    private Position start;
    private Position end;
    private List<Position> listEnd;
    
    private int utility;
    
    private Position capturedPiece;
    
    private boolean queening;
    
    public Movement() {
        this.start = new Position();
        this.end = new Position();
        utility = 0;
        queening = false;
        listEnd = new ArrayList<>();
    }
    
    public Movement(Position start, Position end) {
        this.start = start;
        this.end = end;
    }
    
    public Movement(Position start, List<Position> listEnd) {
        this.start = start;
        this.listEnd = listEnd;
    }
    
    public Movement(Position start, Position end, Position capturedPiece) {
        this.start = start;
        this.end = end;
        this.capturedPiece = capturedPiece;
    }
    
    public Movement(Position start, List<Position> listEnd, Position capturedPiece) {
        this.start = start;
        this.listEnd = listEnd;
        this.capturedPiece = capturedPiece;
    }
    
    
    
    public Position getStart() {
        return start;
    }
    
    public void setStart(Position start) {
        this.start = start;
    }
    
    public Position getEnd() {
        return end;
    }
    
    public void setEnd(Position end) {
        this.end = end;
    }
    
    public List<Position> getListEnd() {
        return listEnd;
    }
    
    public void setListEnd(List<Position> listEnd) {
        this.listEnd = listEnd;
    }
    
    public Position getCapturedPiece() {
        return this.capturedPiece;
    }
    
    public void setCapturedPiece(Position capturedPiece) {
        this.capturedPiece = capturedPiece;
    }
    
    public int getUtility() {
        return utility;
    }
    
    public void setUtility(int utility) {
        this.utility = utility;
    }
    
    public boolean isQueening() {
        return queening;
    }
    
    public void setQueening(boolean queening) {
        this.queening = queening;
    }
}
