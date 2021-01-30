package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.util.Pair;
import enums.Color;
import enums.PieceType;

public abstract class Piece implements Runnable {
    
    private AtomicBoolean running = new AtomicBoolean(false);
    
    private List<Piece> playingPieces;

    private final Color color;
    protected PieceType pieceType;
    
    // Keep a reference of the board, acting as environnement information for the agent
    private Board board;
    
    // Number of plays left.
    private int numberOfPlaysCurrent;
    
    // ID of the piece, used to communicate
    private int ID;
    
    // List of friendly pieces can play
    private List<Piece> friendlyPiecesPlaying;
    
    // Pair of best current move gloabally and the related piece's ID
    private volatile Pair<Integer, Movement> bestCurrentMove;
    
    // Hold the highest utility move for the piece
    private Movement bestUtilityMove;
    
    // Position of the piece on the board
    private Position position;

    // List of movement can be done by normal move
    private List<Movement> allowedMovement;

    // List of position of enemy pieces within 2 cases in each direction
    private List<Position> enemyPiecesAround;

    // List of movement of enemy can be captured and the new position after take
    private List<Movement> enemyPiecesCanBeTaken;
    
    // List of pair position of enemy can be captured and the list of position available after take
    private List<Pair<Position, List<Position>>> piecesCanBeTakenQueen;

    // List of List of position of pieces for a given rafle
    private List<List<Position>> listRafle;
    
    // List of position where an ally piece needs to be defended
    private List<List<Position>> defendAllyPosition;

    // Indicate if the piece can move (not blocked by other pieces)
    private boolean pieceCanMove;
    
    // Indicate if the piece can take at least one piece
    private boolean pieceCanTake;
    
    // Indicate if the piece is moving at the current step.
    private boolean pieceIsMoving;
    
    // Indicate if the piece have already played this turn, a piece can't pay twice
    private boolean pieceHasPlayedThisTurn;


    public Piece(Color color, PieceType pieceType) {
        this.color = color;
        this.pieceType = pieceType;
        this.friendlyPiecesPlaying = new ArrayList<>();
        this.allowedMovement = new ArrayList<>();
        this.enemyPiecesAround = new ArrayList<>();
        this.enemyPiecesCanBeTaken = new ArrayList<>();
        this.piecesCanBeTakenQueen = new ArrayList<>();
        this.listRafle = new ArrayList<>();
        this.pieceCanMove = false;
        this.pieceCanTake = false;
        this.pieceIsMoving = false;
        this.pieceHasPlayedThisTurn = false;
    }

    public Piece(Color color, PieceType pieceType, Position position) {
        this.color = color;
        this.pieceType = pieceType;
        this.position = position;
        this.friendlyPiecesPlaying = new ArrayList<>();
        this.allowedMovement = new ArrayList<>();
        this.enemyPiecesAround = new ArrayList<>();
        this.enemyPiecesCanBeTaken = new ArrayList<>();
        this.piecesCanBeTakenQueen = new ArrayList<>();
        this.listRafle = new ArrayList<>();
        this.pieceCanMove = false;
        this.pieceCanTake = false;
        this.pieceIsMoving = false;
        this.pieceHasPlayedThisTurn = false;
    }
    
    public boolean isRunning() {
        return running.get();
    }
    
    public void setRunning() {
        running.set(true);
    }
    
    public void interrupt() {
        running.set(false);
    }

    public List<Piece> getPlayingPieces() {
        return playingPieces;
    }

    public void setPlayingPieces(List<Piece> playingPieces) {
        this.playingPieces = playingPieces;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    public Color getColor() {
        return color;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public void setBoard(Board board) {
        this.board = board;
    }
    
    public int getNumberOfPlaysCurrent() {
        return numberOfPlaysCurrent;
    }
    
    public void setNumberOfPlaysCurrent(int numberOfPlaysCurrent) {
        this.numberOfPlaysCurrent = numberOfPlaysCurrent;
    }

    public int getID() {
        return ID;
    }
    
    public void setID(int ID) {
        this.ID = ID;
    }

    public List<Piece> getFriendlyPiecesPlaying() {
        return friendlyPiecesPlaying;
    }

    public void setFriendlyPiecesPlaying(List<Piece> friendlyPiecesPlaying) {
        this.friendlyPiecesPlaying = friendlyPiecesPlaying;
    }

    public Pair<Integer, Movement> getBestCurrentMove() {
        return bestCurrentMove;
    }

    public void setBestCurrentMove(Pair<Integer, Movement> bestCurrentMove) {
        int currentUtility = this.bestCurrentMove.getValue().getUtility();
        int newUtility = bestCurrentMove.getValue().getUtility();
        
        if(currentUtility < newUtility) {
            this.bestCurrentMove = bestCurrentMove;
            System.out.println("Current best move is piece ID : " + this.bestCurrentMove.getKey());
        } else {
            System.out.println("The new movement is less valuable.");
        }
    }
    
    public Movement getBestUtilityMove() {
        return bestUtilityMove;
    }
    
    public void setBestUtilityMove(Movement bestUtilityMove) {
        this.bestUtilityMove = bestUtilityMove;
    }

    public List<Movement> getAllowedMovement() {
        return allowedMovement;
    }

    public void setAllowedMovement(List<Movement> allowedMovement) {
        this.allowedMovement = allowedMovement;
    }
    
    public void addAllowedMovement(Movement move) {
        this.allowedMovement.add(move);
    }

    public List<Position> getEnemyPiecesAround() {
        return enemyPiecesAround;
    }

    public void setEnemyPiecesAround(List<Position> enemyPiecesAround) {
        this.enemyPiecesAround = enemyPiecesAround;
    }

    public List<Movement> getPiecesCanBeTaken() {
        return enemyPiecesCanBeTaken;
    }

    public void setPiecesCanBeTaken(List<Movement> piecesCanBeTaken) {
        this.enemyPiecesCanBeTaken = piecesCanBeTaken;
    }
    
    public void addPieceCanBeTaken(Movement pieceCanBeTaken) {
        this.enemyPiecesCanBeTaken.add(pieceCanBeTaken);
    }
    
    public List<Pair<Position, List<Position>>> getPiecesCanBeTakenQueen() {
        return piecesCanBeTakenQueen;
    }
    
    public void setPiecesCanBeTakenQueen(List<Pair<Position, List<Position>>> piecesCanBeTakenQueen) {
        this.piecesCanBeTakenQueen = piecesCanBeTakenQueen;
    }

    public List<List<Position>> getListRafle() {
        return listRafle;
    }

    public void setListRafle(List<List<Position>> listRafle) {
        this.listRafle = listRafle;
    }
    
    public List<List<Position>> getDefendAllyPosition() {
        return defendAllyPosition;
    }

    public void setDefendAllyPosition(List<List<Position>> defendAllyPosition) {
        this.defendAllyPosition = defendAllyPosition;
    }
    
    public void addListToDefend(List<Position> list) {
        this.defendAllyPosition.add(list);
    }
    
    public boolean isPieceCanMove() {
        return pieceCanMove;
    }

    public void setPieceCanMove(boolean pieceCanMove) {
        this.pieceCanMove = pieceCanMove;
    }
    
    public boolean isPieceCanTake() {
        return pieceCanTake;
    }
    
    public void setPieceCanTake(boolean pieceCanTake) {
        this.pieceCanTake = pieceCanTake;
    }
    
    public boolean isMoving() {
        return pieceIsMoving;
    }
    
    public void setIsMoving(boolean pieceIsMoving) {
        this.pieceIsMoving = pieceIsMoving;
    }
    
    public boolean hasPlayedThisTurn() {
        return this.pieceHasPlayedThisTurn;
    }
    
    public void setHasPlayedThisTurn(boolean pieceHasPlayedThisTurn) {
        this.pieceHasPlayedThisTurn = pieceHasPlayedThisTurn;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
    
    /**
     * Generate every list.
     * @param board game board
     */
    public abstract void computeLists(Board board);

    /**
     * Generate list of allowed movement, i.e. the empty cases around can be reached.
     * @param board game board
     */
    public abstract void listAllowedMove(Board board);
    
    /**
     * Generate list of first enemy pieces' in diagonnaly which can take the current peice.
     * A Pawn must be adjacent to be considered. 
     * @param board game board
     * @return TODO
     */
    public abstract List<Pair<Piece, String>> listEnemyPiecesDiagonally(Board board, Position position);
    
    /**
     * Generate list of position where an ally can sit on to defend.
     * @param board game board
     * @return list of position to send to 
     */
    public abstract List<Position> listPositionNeedDefender(Board board, Position position);

    /**
     * Generate list of enemy pieces' position which can be taken.
     * @param board game board
     */
    public abstract void listPiecesCanBeTaken(Board board);

    /**
     * Generate list of rafle possible with the current piece.
     * @param board game board
     * @return list of list of position
     */
    public abstract List<List<Position>> computeRafleList(Board board);
    
    /**
     * Send the piece's best utility move to neighbors.
     */
    public abstract void sendMove();
    
    /**
     * Send the position to allies where they might defend the current piece.
     * @param board game board
     */
    public abstract void sendPositionDefend(Board board);
    
    /**
     * Send the piece can actually play this turn to others.
     * @param board TODO
     * @return list of pieces can do a movevement
     */
    public abstract void sendPlayingPiece(Board board);
    
    /**
     * Search for the best movement through all the lists (moves, capture).
     * @param board game board
     */
    public abstract void computeBestUtility(Board board);
    
    /**
     * Search for the best capture move available.
     * @param board game board
     * @return movement initialized
     */
    public abstract Movement computeBestCaptureUtility(Board board);
    
    /**
     * Search for the best simple move available.
     * @param board
     * @return
     */
    public abstract Movement computeBestMoveUtility(Board board);
    
    /**
     * Reinitialize lists used for setting up movement.
     */
    public void clearAllLists() {
        allowedMovement = new ArrayList<>();
        enemyPiecesAround = new ArrayList<>();
        enemyPiecesCanBeTaken = new ArrayList<>();
        listRafle = new ArrayList<>();
        piecesCanBeTakenQueen = new ArrayList<>();
        friendlyPiecesPlaying = new ArrayList<>();
        setPieceCanMove(false);
        setPieceCanTake(false);
    }
    
    public void addfriendlyPiecesPlayingID(int ID) {

    }
    
    @Override
    public String toString() {
        String type = "";
        if(this.pieceType == PieceType.PAWN) {
            type = "PAWN";
        } else {
            type = "QUEEN";
        }
        return type;
    }
}
