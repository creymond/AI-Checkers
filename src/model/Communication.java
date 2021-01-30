package model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.util.Pair;

public class Communication {

    public volatile static ConcurrentHashMap<Integer, Piece> playingPieces;
    public volatile static ConcurrentHashMap<Piece, Movement> bestMove;
    
    public volatile static boolean isGameOver;
    
    public volatile static CopyOnWriteArrayList<Piece> piecesCanMove;
    
    public static Board board;

    public static Piece pieceMoving;
    
    public static Movement move;

    public static int numberOfPlaysCurrent;
    
    public static void initList() {
        playingPieces = new ConcurrentHashMap<Integer, Piece>();
        bestMove = new ConcurrentHashMap<Piece, Movement>();
        piecesCanMove = new CopyOnWriteArrayList<>(new ArrayList<Piece>());
        board = null;
        pieceMoving = null;
    }

    public static ConcurrentHashMap<Integer, Piece> getPlayingPieces() {
        return playingPieces;
    }

    public static Piece getPiecePlayingByID(int ID) {
        return playingPieces.get(ID);
    }

    public static void setPiecePlaying(int ID, Piece piece) {
        playingPieces.putIfAbsent(ID, piece);
    }

    public static void removePiecePlaying(int ID) {
        playingPieces.remove(ID);
    }

    public static void clearPiecesPlaying() {
        playingPieces.clear();
    }

    // Should hold only one map
    public static ConcurrentHashMap<Piece, Movement> getBestMoveMap() {
        return bestMove;
    }

    public static void setBestMove(Piece piece, Movement move) {
        synchronized(bestMove) {
            bestMove.put(piece, move);
        }
    }
    
    public static Pair<Piece, Movement> computeBestMove() {
        Pair<Piece, Movement> pair = null;
        int utility = 0;
        //System.out.println("FROM COMMUNICATION SIZE : " + bestMove.size());
        for(Entry<Piece,Movement> i : bestMove.entrySet()) {
            if(i.getValue().getUtility() >= utility) {
                pair = new Pair<Piece,Movement>(i.getKey(),i.getValue());
                //System.out.println("FROM COMMUNICATION : " + pair.getKey());

                utility = i.getValue().getUtility();
            }
        }
        
        //numberOfPlaysCurrent--;
        pieceMoving = pair.getKey();
        move = pair.getValue();
        return pair;
    }

    public static Enumeration<Movement> getMovement() {
        return bestMove.elements();
    }

    public static void removeBestMove() {
        bestMove.clear();
    }

    public static void setNumberOfPlays(int number) {
        numberOfPlaysCurrent = number;
    }

    public static int getNumberOfPlays() {
        return numberOfPlaysCurrent;
    }

    public static void havePlayedOnce() {
        numberOfPlaysCurrent -= 1;
    }

    public static void clearAll() {
        playingPieces.clear();
        bestMove.clear();
        pieceMoving = null;
    }

    public static void setBoard(Board _board) {
        board = _board;
    }
    
    public static void addPiece(Piece piece) {
        synchronized(piecesCanMove) {
            piecesCanMove.addIfAbsent(piece);
        }   
    }
   
    public static boolean piecesCanPlay() {
        Iterator<Piece> iterator = piecesCanMove.iterator();
        ArrayList<Piece> arr = new ArrayList<>();
        iterator.forEachRemaining(arr::add);
        if(arr.size() == 0) {
            return false;
        } else {
            return true;
        }
        
    }
    
    public static ArrayList<Piece> getAllPieces() {
        ArrayList<Piece> res = new ArrayList<>();
        Iterator<Piece> it = piecesCanMove.iterator();
        it.forEachRemaining(res :: add);
        return res;
    }
}
