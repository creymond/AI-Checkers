package model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import enums.Color;
import enums.PieceType;
import javafx.util.Pair;

/**
 * Class which holds the board and set up the game.
 * 
 * @author Cyril Reymond
 *
 */
@SuppressWarnings("unused")
public class MainGame {

    private Board board;
    private Color currentPlayer;
    private final Color whitePlayer;
    private final Color blackPlayer;
    private Color winner;

    private List<Piece> playablePieces;
    private int numberOfPieces;
    private int numberOfPlaysPerRound;
    private int numberOfPlaysCurrent;
    private boolean isGameOver;
    private Position oldPosMoving;

    private List<Thread> threadList;
    private List<Runnable> runnableList;
    private List<Pair<Thread,Piece>> whitePieces;
    private List<Pair<Thread,Piece>> blackPieces;

    public MainGame() {
        whitePlayer = Color.WHITE;
        blackPlayer = Color.BLACK;
        currentPlayer = whitePlayer;
        board = new Board(8,8);
        numberOfPieces = 12;
        isGameOver = false;
        winner = null;
        playablePieces = new ArrayList<>();
        numberOfPlaysPerRound = (int)(Math.random() * (3) + 1); // number between 1 and 2 inclusive
        System.out.println(numberOfPlaysPerRound);
        numberOfPlaysCurrent = numberOfPlaysPerRound;
        Communication.setNumberOfPlays(numberOfPlaysPerRound);
        Communication.initList();
        threadList = new ArrayList<>();
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        createThreadForEachPiece();
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Color currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Color getWinner() {
        return winner;
    }

    public Position getOldPosMoving() {
        return oldPosMoving;
    }

    public void setOldPosMoving(Position oldPosMoving) {
        this.oldPosMoving = oldPosMoving;
    }

    public void setWinner(Color winner) {
        this.winner = winner;
        isGameOver = true;
    }

    public Board getBoard() {
        return board;
    }

    public Piece[][] getBoardPiece() {
        return board.getBoard();
    }

    public void setPlayablePieces(List<Piece> playablePieces) {
        this.playablePieces = playablePieces;
    }

    public boolean getGameOver() {
        return isGameOver;
    }

    public List<Runnable> getRunnableList() {
        return runnableList;
    }

    /**
     * Define which pieces can play for the current turn.
     */
    public void setPlayablePieces() {
        playablePieces = new ArrayList<>();
        for(int i = 0; i < board.getRowSize(); i++) {
            for(int j = 0; j < board.getColumnSize(); j++) {
                if(board.hasPiece(i, j) && board.getPiece(i, j).getColor() == currentPlayer) {
                    if(!board.getPiece(i, j).hasPlayedThisTurn()) {
                        board.getPiece(i,j).computeLists(board);
                        if(board.getPiece(i, j).isPieceCanMove() ||
                                board.getPiece(i, j).isPieceCanTake()) {
                            playablePieces.add(board.getPiece(i, j));
                        }
                    }         
                }
            }
        }
    }

    /**
     * Compute how many pieces left for the current player.
     */
    public void setNumberOfPieces() {
        numberOfPieces = 0;
        for(int i = 0; i < board.getRowSize(); i++) {
            for(int j = 0; j < board.getColumnSize(); j++) {
                if(board.hasPiece(i, j) && board.getPiece(i, j).getColor() == currentPlayer) {
                    numberOfPieces++;
                }
            }
        }
    }

    /**
     * Display the position of all pieces.
     * Used for debug only.
     */
    private void displayPosPieces() {
        for(int i = 0; i < board.getRowSize(); i++) {
            for(int j = 0; j < board.getColumnSize(); j++) {
                if(board.hasPiece(i, j)) {
                    System.out.println("Piece " + board.getPiece(i, j).getID() + " at " 
                            + board.getPiece(i, j).getPosition().toString());
                }
            }
        }
    }

    /**
     * Display the position of all playable pieces.
     * Used for debug only.
     */
    private void diplayPlayablePieces() {
        for(Piece p : playablePieces) {
            System.out.println("Can be played : " + p.getPieceType() + " " + p.getColor() + " position : (" + 
                    p.getPosition().getLine() + "," + p.getPosition().getColumn() + ")");
        }
    }

    private void displayAllowedMove(Piece piece) {
        List<Movement> listMoves = piece.getAllowedMovement();
        for(Movement move : listMoves) {
            Position pos = move.getEnd();
            //System.out.println("Allowed Pos : (" + pos.getLine() + "," + pos.getColumn() + ")");
        }
    }

    /**
     * Create a thread for each pieces with good color this turn.
     */
    private void createThreadForCurrentPieces() {
        for(int i = 0; i < board.getRowSize(); i++) {
            for(int j = 0; j < board.getColumnSize(); j++) {
                if(board.hasPiece(i, j) && board.getColorPiece(i, j) == currentPlayer) {
                    board.getPiece(i,j).setBoard(board);
                    Pair<Thread, Piece> pair = new Pair<>(new Thread(board.getPiece(i, j)), board.getPiece(i, j));
                    whitePieces.add(pair);
                }
            }
        }
    }


    /**
     * Create thread for each pieces at initialization.
     */
    private void createThreadForEachPiece() {
        for(int i = 0; i < board.getRowSize(); i++) {
            for(int j = 0; j < board.getColumnSize(); j++) {
                if(board.hasPiece(i, j)) {
                    if(board.getColorPiece(i, j) == Color.WHITE) {
                        Pair<Thread, Piece> pair = new Pair<>(new Thread(board.getPiece(i, j)), board.getPiece(i, j));
                        whitePieces.add(pair);
                    } else {
                        Pair<Thread, Piece> pair = new Pair<>(new Thread(board.getPiece(i, j)), board.getPiece(i, j));
                        blackPieces.add(pair);
                    }
                }
            }
        }
    }

    private void updatePiece(int ID) {
        if(currentPlayer == Color.WHITE) {
            for(int i = 0; i < whitePieces.size(); i++) {
                if(whitePieces.get(i).getValue().getID() == ID) {
                    Thread t = whitePieces.get(i).getKey();
                    whitePieces.remove(i);
                    whitePieces.add(new Pair<Thread,Piece>(t,board.getPieceByID(ID)));
                }
            }
        } else {
            for(int i = 0; i < whitePieces.size(); i++) {
                if(blackPieces.get(i).getValue().getID() == ID) {
                    Thread t = blackPieces.get(i).getKey();
                    blackPieces.remove(i);
                    blackPieces.add(new Pair<Thread,Piece>(t,board.getPieceByID(ID)));
                }
            }
        }

    }

    /**
     * Used to empty the thread and runnable list before next turn.
     */
    private void resetPlayingList() {
        whitePieces = new ArrayList<>();
    }

    /**
     * Execute the run() method of each threads.
     */
    private void runCurrentPiecesThread() {
        for(int i = 0; i < whitePieces.size(); i++) {
            if(whitePieces.get(i).getKey().getState() == Thread.State.TIMED_WAITING) {
                whitePieces.get(i).getKey().interrupt();
            } else {
                whitePieces.get(i).getKey().start();
            }
        }
    }

    /**
     * Let all the threads finish their execution before collecting the data.
     */
    private void waitThreadFinished() {
        for(int i = 0; i < whitePieces.size(); i++) {
            while(whitePieces.get(i).getKey().isAlive()) {
                try {
                    whitePieces.get(i).getKey().join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } 
            }     
        }
    }

    /**
     * Break the sleep state of white threads to run it again or start initially the thread.
     */
    private void runWhiteThread() {
        for(int i = 0; i < whitePieces.size(); i++) {
            if(whitePieces.get(i).getKey().getState() == Thread.State.TIMED_WAITING) {
                whitePieces.get(i).getKey().interrupt();
            } else {
                whitePieces.get(i).getKey().start();
            }
        }
    }

    /**
     * Break the sleep state of white threads to run it again or start initially the thread.
     */
    private void runBlackThread() {
        for(int i = 0; i < blackPieces.size(); i++) {
            if(blackPieces.get(i).getKey().getState() == Thread.State.TIMED_WAITING) {
                blackPieces.get(i).getKey().interrupt();
            } else {
                blackPieces.get(i).getKey().start();
            }
        }
    }

    /**
     * Extract the list of current playing pieces.
     * @return list of pieces
     */
    private List<Piece> getListPlayingPieces() {
        List<Piece> currentPieces = new ArrayList<>();
        for(int i = 0; i < whitePieces.size(); i++) {
            currentPieces.add(whitePieces.get(i).getValue());
        }
        return currentPieces;
    }

    /**
     * Send to every playing pieces the reference of others to allow communication.
     */
    private void sendReferenceOfPieces() {
        List<Piece> listPieces = getListPlayingPieces();
        for(int i = 0; i < whitePieces.size(); i++) {
            whitePieces.get(i).getValue().setPlayingPieces(listPieces);
        }
    }

    /**
     * Obtain the piece which is moving this turn after the threads did compute.
     * @return reference to the piece
     */
    private Piece getPieceMoving() {
        Piece piece = null;

        for(int i = 0; i < board.getRowSize(); i++) {
            for(int j = 0; j < board.getColumnSize(); j++) {
                if(board.hasPiece(i, j) && board.getColorPiece(i, j) == currentPlayer && board.getPiece(i, j).isMoving()) {
                    piece = board.getPiece(i, j);
                }
            }
        }
        return piece;
    }

    private void removeThread(int id) {
        if(currentPlayer == Color.WHITE) {
            for(int i = 0; i < whitePieces.size(); i++) {
                if(whitePieces.get(i).getValue().getID() == id) {
                    whitePieces.remove(i);
                    break;
                }
            }
        } else {
            for(int i = 0; i < blackPieces.size(); i++) {
                if(blackPieces.get(i).getValue().getID() == id) {
                    blackPieces.remove(i);
                    break;
                }
            }
        }
    }

    private void removeThreadCapture(int id) {
        if(currentPlayer == Color.BLACK) {
            for(int i = 0; i < whitePieces.size(); i++) {
                if(whitePieces.get(i).getValue().getID() == id) {
                    whitePieces.remove(i);
                    break;
                }
            }
        } else {
            for(int i = 0; i < blackPieces.size(); i++) {
                if(blackPieces.get(i).getValue().getID() == id) {
                    blackPieces.remove(i);
                    break;
                }
            }
        }
    }


    /**
     * Main function which ask pieces the move to play.
     */

    //  si une pièce ne dépasse pas la ligne du milieu sur plusieurs coups = blocage

    public void move() {
        Communication.setBoard(board);
        setNumberOfPieces();
        for(int i = 0; i < board.getRowSize(); i++) {
            for(int j = 0; j < board.getColumnSize(); j++) {
                if(board.getPiece(i, j) != null) {
                    board.getPiece(i, j).setIsMoving(false);
                }
            }
        }
        if(numberOfPieces == 0) {
            nextPlayer();
            setWinner(currentPlayer);
            System.out.println("The winner is " + winner + " player.");
            Communication.isGameOver = true;
            runBlackThread();
            runWhiteThread();
        } else {
            if(currentPlayer == Color.WHITE) {
                System.out.println("**** White Playing ****");
                runWhiteThread(); 
                System.out.println("Size thread white : " + whitePieces.size());
            } else {
                System.out.println("**** Black Playing ****");
                runBlackThread();
                System.out.println("Size thread black : " + blackPieces.size());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean allSleeping = false;
            while(!allSleeping) {
                if(currentPlayer == Color.WHITE) {
                    int count = 0;
                    for(Pair<Thread,Piece> p : whitePieces) {
                        if(p.getKey().getState() == Thread.State.TIMED_WAITING) {
                            count++;
                        }
                    }
                    if(count == whitePieces.size()) {
                        // All thread are sleeping
                        allSleeping = true;
                    }
                } else {
                    int count = 0;
                    for(Pair<Thread,Piece> p : blackPieces) {
                        if(p.getKey().getState() == Thread.State.TIMED_WAITING) {
                            count++;
                        }
                    }
                    if(count == blackPieces.size()) {
                        // All thread are sleeping
                        allSleeping = true;
                    }
                }
            }
            if(!Communication.piecesCanPlay()) {
                nextPlayer();
                setWinner(currentPlayer);
                System.out.println("The winner is " + winner + " player.");
                Communication.isGameOver = true;

                runBlackThread();
                runWhiteThread();

            } else {
                Communication.computeBestMove();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Pair<Piece,Movement> pair = Communication.computeBestMove();
                System.out.println("Move : " + pair.getValue().getStart().toString() + " -> " + pair.getValue().getEnd().toString());
                Piece piece = pair.getKey();
                Movement move = pair.getValue();
                Position capturedPiece = move.getCapturedPiece();
                Position oldPos = move.getStart();
                oldPosMoving = oldPos;
                Position newPos = move.getEnd();
                boolean queening = move.isQueening();

                //System.out.println("CAPTURE : " + capturedPiece);
                if(capturedPiece != null) {
                    int id = board.getPiece(capturedPiece).getID();
                    removeThreadCapture(id);
                    board.removePiece(capturedPiece);
                    capturedPiece = null;
                }

                if(queening) {
                    int id = piece.getID();
                    board.removePiece(oldPos);
                    removeThread(id);
                    piece = new Queen(piece.getColor(), newPos);
                    piece.setID(id);
                    piece.setHasPlayedThisTurn(false);
                    board.setPiece(piece, newPos);
                    piece.setPosition(newPos);
                    board.getPiece(newPos).setIsMoving(true);
                    if(currentPlayer == Color.WHITE) {
                        Thread t = new Thread(piece);
                        Pair<Thread,Piece> p = new Pair<>(t,piece);
                        whitePieces.add(p);

                    } else {
                        Thread t = new Thread(piece);
                        Pair<Thread,Piece> p = new Pair<>(t,piece);
                        blackPieces.add(p);
                    }
                } else {
                    board.removePiece(oldPos);
                    piece.setHasPlayedThisTurn(true);
                    piece.setPosition(newPos);
                    board.setPiece(piece, newPos);
                    board.getPiece(newPos).setIsMoving(true);
                }

                Communication.clearAll();
                numberOfPlaysCurrent--;
                if(numberOfPlaysCurrent == 0) {
                    numberOfPlaysCurrent = numberOfPlaysPerRound;
                    nextPlayer();
                }
            }
        }

    }



    /**
     * Ask agents to set the next piece movement
     * @return the piece which move this turn
     */
    public Piece pieceToMove() {
        /*
         * Precondition : list playable pieces already filled up
         */
        numberOfPieces = getNumberOfPiecesLeftCurrentPlayer();
        int randomNumber = 0;
        Piece pieceMove = null;
        if(playablePieces.size() != 0) {
            randomNumber = (int)(Math.random() * (playablePieces.size()));
            pieceMove = playablePieces.get(randomNumber);
        }
        return pieceMove;
    }

    public int getNumberOfPiecesLeftCurrentPlayer() {
        int number = 0;
        for(int i = 0; i < board.getRowSize(); i++) {
            for(int j = 0; j < board.getColumnSize(); j++) {
                if(board.hasPiece(i, j) && board.getColorPiece(i, j) == currentPlayer) {
                    number++;
                }
            }
        }      
        return number;
    }

    /*
     * Alternate player.
     */
    public void nextPlayer() {
        if(currentPlayer == Color.WHITE) {
            currentPlayer = Color.BLACK;
        } else {
            currentPlayer = Color.WHITE;
        }
    }

    /**
     * Method to know if a piece reached queening line depending on its color.
     * @param piece has to be a pawn
     * @return true if piece has reached queening line, false otherwise
     */
    private boolean haveReachedQueeningLine(Piece piece) {
        // Precondition : the piece finished its movement
        boolean isQueening = false;

        if(piece.getColor() == Color.WHITE) {
            if(piece.getPosition().getLine() == 0) {
                isQueening = true;
            }
        } else {
            if(piece.getPosition().getLine() == 7) {
                isQueening = true;
            }
        }
        return isQueening;
    }

    /**
     * Method to know if at least one piece can play this turn.
     * @return true if one piece can play, false otherwise
     */
    private boolean piecesCanPlay() { 
        boolean canPlay = false;
        ArrayList<Piece> pieces = Communication.getAllPieces();
        for(Piece p : pieces) {
            if(p.getColor() == currentPlayer && (p.isPieceCanMove() || p.isPieceCanTake())) {
                canPlay = true;
            }
        }

        return canPlay;
    }

    /**
     * Test the conditions for the end of the game.
     * @return true if current player can't play, false otherwise
     */
    private boolean endConditions() {
        boolean finished = false;
        if(getNumberOfPiecesLeftCurrentPlayer() == 0 || !piecesCanPlay()) {
            finished = true;
        }
        return finished;
    }
}