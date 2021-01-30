package model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import enums.Color;
import enums.PieceType;
import enums.Utility;
import javafx.util.Pair;

@SuppressWarnings("unused")
public class Pawn extends Piece {

    public Pawn(Color color) {
        super(color, PieceType.PAWN);
    }

    public Pawn(Color color, Position position) {
        super(color, PieceType.PAWN, position);
    }

    @Override
    public void computeLists(Board board) {
        clearAllLists();
        listAllowedMove(board);
        listPiecesCanBeTaken(board);
    }

    @Override
    public void listAllowedMove(Board board) {
        getAllowedMovement().clear();
        List<Position> listCases = getDiagonalCases(board);
        for(Position pos : listCases) {
            if(!board.hasPiece(pos)) {
                Movement move = new Movement(this.getPosition(),pos);
                addAllowedMovement(move);
                setPieceCanMove(true);
            }
        }
    }


    @Override
    public List<Position> listPositionNeedDefender(Board board, Position position) {
        List<Position> listCases = new ArrayList<>();
        List<Pair<Piece, String>> listPieceEnemy = listEnemyPiecesDiagonally(board, position);

        for(int i = 0; i < listPieceEnemy.size(); i++) {
            Piece piece = listPieceEnemy.get(i).getKey();
            String direction = listPieceEnemy.get(i).getValue();
            if(direction == "upR") {
                int currentLine = position.getLine();
                int currentColumn = position.getColumn();
                if(!outOfBound(board, currentLine+1, currentColumn-1)) {
                    if(!board.hasPiece(currentLine, currentColumn)) {
                        Position pos = new Position(currentLine+1, currentColumn-1);
                        listCases.add(pos);
                    }
                }
            } else if(direction == "upL") {
                int currentLine = position.getLine();
                int currentColumn = position.getColumn();
                if(!outOfBound(board, currentLine+1, currentColumn+1)) {
                    if(!board.hasPiece(currentLine, currentColumn)) {
                        Position pos = new Position(currentLine+1, currentColumn+1);
                        listCases.add(pos);
                    }
                }
            } else if(direction == "downR") {
                int currentLine = position.getLine();
                int currentColumn = position.getColumn();
                if(!outOfBound(board, currentLine-1, currentColumn-1)) {
                    if(!board.hasPiece(currentLine, currentColumn)) {
                        Position pos = new Position(currentLine-1, currentColumn-1);
                        listCases.add(pos);
                    }
                }
            } else {
                int currentLine = position.getLine();
                int currentColumn = position.getColumn();
                if(!outOfBound(board, currentLine-1, currentColumn+1)) {
                    if(!board.hasPiece(currentLine, currentColumn)) {
                        Position pos = new Position(currentLine-1, currentColumn+1);
                        listCases.add(pos);
                    }
                }
            }
        }
        return listCases;
    }

    @Override
    public List<Pair<Piece, String>> listEnemyPiecesDiagonally(Board board, Position position) {
        List<Pair<Piece, String>> listEnemy = new ArrayList<>();
        List<Position> upperRight = getUpperDiagonalCases(board, position, "upR");
        List<Position> upperLeft = getUpperDiagonalCases(board, position, "upL");
        List<Position> downRight = getDownDiagonalCases(board, position, "downR");
        List<Position> downLeft = getDownDiagonalCases(board, position, "downL");

        Pair<Piece, String> pair_ur = getFirstPieceDiagonally(board, upperRight, "upR");
        Pair<Piece, String> pair_ul = getFirstPieceDiagonally(board, upperLeft, "upL");
        Pair<Piece, String> pair_dr = getFirstPieceDiagonally(board, downRight, "downR");
        Pair<Piece, String> pair_dl = getFirstPieceDiagonally(board, downLeft, "upL");

        if(pair_ur != null) {
            listEnemy.add(pair_ur);
        }

        if(pair_ul != null) {
            listEnemy.add(pair_ul);
        }

        if(pair_dr != null) {
            listEnemy.add(pair_dr);
        }

        if(pair_dl != null) {
            listEnemy.add(pair_dl);
        }      
        return listEnemy; 
    }

    /**
     * In order to know if the current piece can be captured, searh for the first piece in the diagonal.
     * A PAWN must be one case beside, otherwise we ignore it. 
     * @param board game board
     * @param diagonal list of position for the diagonal
     * @param direction can hold four directions as : upR, upL, downR, downL
     * @return pair of piece reference and the direction as string
     */
    private Pair<Piece, String> getFirstPieceDiagonally(Board board, List<Position> diagonal, String direction) {
        // If return null : no pieces have been found on the diagonal, it is safe
        Color oppositeColor = getOppositeColor(this.getColor());
        int distance = 1;
        for(Position p : diagonal) {
            if(board.hasPiece(p)) {
                if(board.getColorPiece(p) == oppositeColor) {
                    if(board.getPiece(p).getPieceType() == PieceType.PAWN && distance == 1) {
                        // the pawn is adjacent and can potentially take the current piece
                        Pair<Piece, String> pair = new Pair<>(board.getPiece(p), direction);
                        return pair;
                    } else if(board.getPiece(p).getPieceType() == PieceType.PAWN && distance > 1){
                        // found a pawn but too far to take the current piece
                        return null;
                    } else {
                        // found a queen
                        Pair<Piece, String> pair = new Pair<>(board.getPiece(p), direction);
                        return pair;
                    }
                } else {
                    return null;
                }
            }
            distance++;
        }
        return null;
    }

    @Override
    public void listPiecesCanBeTaken(Board board) {
        List<Position> listCasesInFront = getTwoCasesInFront(board);
        List<Position> enemyInFront = new ArrayList<>();
        Color oppositeColor = getOppositeColor(this.getColor());

        int column = this.getPosition().getColumn();

        // Compute list of enemy in front
        for(Position position : listCasesInFront) {
            if(board.hasPiece(position)) {
                if(board.getPiece(position).getColor() == oppositeColor) {
                    enemyInFront.add(position);
                } 
            }
        }

        if(!enemyInFront.isEmpty()) {

            // Compute list for white color
            if(this.getColor() == Color.WHITE) {
                for(Position position : enemyInFront) {

                    // Position on diagonal upper right
                    if(column - position.getColumn() < 0) {

                        // Compute the position of new case after take
                        Position nextPos = new Position(position.getLine()-1, position.getColumn()+1);
                        if(!outOfBound(board, nextPos.getLine(), nextPos.getColumn())) {

                            // Case is free
                            if(!board.hasPiece(nextPos)) {
                                setPieceCanTake(true);
                                Movement move = new Movement(this.getPosition(),nextPos,position);
                                addPieceCanBeTaken(move);
                            }
                        }

                        // Position on diagonal upper left
                    } else {

                        // Compute the position of new case after take
                        Position nextPos = new Position(position.getLine()-1, position.getColumn()-1);
                        if(!outOfBound(board, nextPos.getLine(), nextPos.getColumn())) {

                            // Case is free
                            if(!board.hasPiece(nextPos)) {
                                setPieceCanTake(true);
                                Movement move = new Movement(this.getPosition(),nextPos,position);
                                addPieceCanBeTaken(move); 
                            }
                        }
                    }
                }
                // Compute list for black color
            } else {
                for(Position position : enemyInFront) {

                    // Position on diagonal bottom right
                    if(column - position.getColumn() < 0) {
                        Position nextPos = new Position(position.getLine()+1, position.getColumn()+1);
                        if(!outOfBound(board, nextPos.getLine(), nextPos.getColumn())) {
                            if(!board.hasPiece(nextPos)) {
                                setPieceCanTake(true);
                                Movement move = new Movement(this.getPosition(),nextPos,position);
                                addPieceCanBeTaken(move);
                            }
                        }

                        // Position on diagonal bottom left
                    } else {
                        Position nextPos = new Position(position.getLine()+1, position.getColumn()-1);
                        if(!outOfBound(board, nextPos.getLine(), nextPos.getColumn())) {
                            if(!board.hasPiece(nextPos)) {
                                setPieceCanTake(true);
                                Movement move = new Movement(this.getPosition(),nextPos,position);
                                addPieceCanBeTaken(move);
                            }
                        }
                    }
                }
            }
        }
    }

    public List<Position> getDiagonalCases(Board board) {
        List<Position> listCases = new ArrayList<>();
        int line = this.getPosition().getLine();
        int column = this.getPosition().getColumn();

        if(!outOfBound(board, line+1, column+1)) {
            listCases.add(new Position(line+1, column+1));
        }

        if(!outOfBound(board, line+1, column-1)) {
            listCases.add(new Position(line+1, column-1));
        }

        if(!outOfBound(board, line-1, column+1)) {
            listCases.add(new Position(line-1, column+1));
        }

        if(!outOfBound(board, line-1, column-1)) {
            listCases.add(new Position(line-1, column-1));
        }  
        return listCases;
    }

    private List<Position> getUpperDiagonalCases(Board board, Position position, String direction) {
        List<Position> listCases = new ArrayList<>();
        int currentLine = position.getLine();
        int currentColumn = position.getColumn();
        int tmpCol = currentColumn;

        if(direction == "upR") {
            for(int i = currentLine-1; i >= 0; i--) {
                tmpCol += 1;
                if(!outOfBound(board, i, tmpCol)) {
                    listCases.add(new Position(i, tmpCol));
                }
            }
            // upL
        } else {
            for(int i = currentLine-1; i >= 0; i--) {
                tmpCol -= 1;
                if(!outOfBound(board, i, tmpCol)) {
                    listCases.add(new Position(i, tmpCol));
                }
            }
        }
        return listCases;
    }

    private List<Position> getDownDiagonalCases(Board board, Position position, String direction) {
        List<Position> listCases = new ArrayList<>();
        int currentLine = position.getLine();
        int currentColumn = position.getColumn();
        int tmpCol = currentColumn;

        if(direction == "downR") {
            for(int i = currentLine+1; i < board.getRowSize(); i++) {
                tmpCol += 1;
                if(!outOfBound(board, i , tmpCol)) {
                    listCases.add(new Position(i, tmpCol));
                }
            }
            // downL
        } else {
            for(int i = currentLine+1; i < board.getRowSize(); i++) {
                tmpCol -= 1;
                if(!outOfBound(board, i , tmpCol)) {
                    listCases.add(new Position(i, tmpCol));
                }
            }
        }    
        return listCases;
    }


    @Override
    public List<List<Position>> computeRafleList(Board board) {
        // TODO
        Position intialPosition = this.getPosition();


        return null;
    }

    private Color getOppositeColor(Color player) {
        Color opposite;
        if (player == Color.WHITE) {
            opposite = Color.BLACK;
        } else {
            opposite = Color.WHITE;
        }
        return opposite;
    }


    private List<Position> getTwoCasesAround(Board board) {
        List<Position> listCases = new ArrayList<>();
        int line = this.getPosition().getLine();
        int column = this.getPosition().getColumn();

        for(int i = line-2; i < line+2; i++) {
            for(int j = column-2; j < column+2; j++) {
                if(!outOfBound(board, i, j)) {
                    listCases.add(new Position(i,j));
                }
            }
        }      
        return listCases;
    }

    /**
     * Used for communication, generate the list of position which holds a friendly piece.
     * Look only one case in each diagonal.
     * @param board game board
     * @return list of friendly piece's position
     */
    private List<Position> getListFriendlyPiecesBeside(Board board) {
        List<Position> listCases = new ArrayList<>();
        int line = this.getPosition().getLine();
        int column = this.getPosition().getColumn();

        for(int i = line-1; i < line+1; i++) {
            for(int j = column-1; j < column+1; j++) {
                if(i != line && j != column) {
                    if(!outOfBound(board, i, j)) {
                        if(board.hasPiece(i, j) && board.getPiece(i, j).getColor() == this.getColor()) {
                            listCases.add(new Position(i,j));
                        }
                    }
                }
            }
        }
        return listCases;
    }

    /**
     * Search the closest friendly piece beside in order to define it as neighbor.
     * @param board game board
     * @return position of closest piece
     */
    private Position findClosestFriendlyPiece(Board board) {
        Position friendlyPos = null;
        for(int i = 0; i < board.getRowSize(); i++) {
            for(int j = 0; j < board.getColumnSize(); j++) {
                if(board.hasPiece(i, j) && board.getPiece(i, j).getColor() == this.getColor()) {
                    friendlyPos = new Position(i,j);
                    return friendlyPos;
                }
            }
        }
        return friendlyPos;
    }

    /**
     * Getting the two cases diagonally in front of the PAWN 
     * depending on its color.
     * @param board
     * @return
     */
    private List<Position> getTwoCasesInFront(Board board) {
        List<Position> listCases = new ArrayList<>();
        int line = this.getPosition().getLine();
        int column = this.getPosition().getColumn();

        if(this.getColor() == Color.WHITE) {
            if(!outOfBound(board, line-1, column-1)) {
                listCases.add(new Position(line-1, column-1));
            }

            if(!outOfBound(board, line-1, column+1)) {
                listCases.add(new Position(line-1, column+1));
            }
        } else {
            if(!outOfBound(board, line+1, column+1)) {
                listCases.add(new Position(line+1, column+1));
            }

            if(!outOfBound(board, line+1, column-1)) {
                listCases.add(new Position(line+1, column-1));
            }
        }  
        return listCases;
    }

    /**
     * Getting the two cases diagonally in front of black PAWN
     * as PAWN can take only in front
     * @param board current board
     * @param pos position of the considered piece
     * @return
     */
    private List<Position> getTakingCasesBlack(Board board, Position pos) {
        List<Position> listCases = new ArrayList<>();
        int rowSize = board.getRowSize();
        int columnSize = board.getColumnSize();

        if(pos.getLine()+1 <= rowSize && pos.getColumn()+1 <= columnSize) {
            listCases.add(new Position(pos.getLine()+1, pos.getColumn()+1));
        }

        if(pos.getLine()+1 <= rowSize && pos.getColumn()-1 >= 0) {
            listCases.add(new Position(pos.getLine()+1, pos.getColumn()-1));
        }
        return listCases;
    }

    /**
     * Getting the two cases diagonally in front of white PAWN
     * as PAWN can take only in front
     * @param board current board
     * @param pos position of the considered piece
     * @return
     */
    private List<Position> getTakingCasesWhite(Board board, Position pos) {
        List<Position> listCases = new ArrayList<>();
        int columnSize = board.getColumnSize();

        if(pos.getLine()-1 >= 0 && pos.getColumn()-1 >= 0) {
            listCases.add(new Position(pos.getLine()-1, pos.getColumn()-1));
        }

        if(pos.getLine()-1 >= 0 && pos.getColumn()+1 <= columnSize) {
            listCases.add(new Position(pos.getLine()+1, pos.getColumn()-1));
        }

        return listCases;
    }

    private boolean willBeQueen(Board board, Position position) {
        int line = position.getLine();
        if(this.getColor() == Color.WHITE) {
            if(line == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            if(line == 7) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Test if the position is on the board
     * @param board game board
     * @param line line of the position
     * @param column column of the position
     * @return true if the position is out of the board, false otherwise
     */
    private boolean outOfBound(Board board, int line, int column) {
        if(line < 0 || line > board.getRowSize()-1 ||
                column < 0 || column > board.getColumnSize()-1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void sendMove() {
        Communication.setBestMove(this,this.getBestUtilityMove());
    }

    @Override
    public void run() {
        boolean cond = true;
        while(cond) {
            setBoard(Communication.board);
            setHasPlayedThisTurn(getBoard().getPieceByID(getID()).hasPlayedThisTurn());
            setPosition(getBoard().getPieceByID(getID()).getPosition());
            
            computeLists(getBoard());
            computeBestUtility(getBoard());

            if(getBestUtilityMove() != null) {
                sendMove();
                Communication.addPiece(this);
            }
            if(!Communication.isGameOver) {
                try {
                    Thread.sleep(2000000000);
                } catch (InterruptedException e) {
                    clearAllLists();
                }
            } else {
                cond = false;
            }
            
        }
    }

    @Override
    public void computeBestUtility(Board board) {
        Movement simpleMove = computeBestMoveUtility(board);
        Movement capture = computeBestCaptureUtility(board);
        Movement bestmove = new Movement();
        if(capture != null && simpleMove != null) {
            if(simpleMove.getUtility() >= capture.getUtility()) {
                bestmove = simpleMove;
            } else {
                bestmove = capture;
            }     
        } else if(capture == null) {
            bestmove = simpleMove;
        } else if(simpleMove == null) {
            bestmove = capture;
        } else {
            bestmove = null;
        }

        this.setBestUtilityMove(bestmove);
    }
    
    private List<Position> getAllyPosition(Board board) {
        List<Position> listCases = new ArrayList<>();
        for(int i = 0; i < board.getRowSize(); i++) {
            for(int j = 0; j < board.getColumnSize(); j++) {
                if(board.hasPiece(i, j) && board.getColorPiece(i, j) == getColor()) {
                    listCases.add(new Position(i,j));
                }
            }
        }  
        return listCases;
    }
    
    private List<Position> computeDefendAlly(Board board) {
        List<Position> listCases = new ArrayList<>();
        List<Position> ally = getAllyPosition(board);
        for(Position p : ally) {
            List<Position> l = listPositionNeedDefender(board, p);
            for(Position p1 : l) {
                if(p1 != null) {
                    listCases.add(p1);
                }
            }
        }
        return listCases;
    }

    @Override
    public void sendPositionDefend(Board board) {
    }

    @Override
    public Movement computeBestCaptureUtility(Board board) {
        int utility = 0;
        Movement bestmove = new Movement();
        bestmove.setUtility(utility);
        List<Movement> randomList = new ArrayList<>();
        List<Movement> piecesTaken = getPiecesCanBeTaken();
        for(Movement m : piecesTaken) {
            
            List<Position> listDefend = listPositionNeedDefender(board, m.getEnd());
            if(listDefend.isEmpty()) {
                // needn't be defended, no risk
                m.setUtility(Utility.CAPTURE.getValue());
            } else {
                // could need to be defended
                m.setUtility(Utility.CAPTURE_DEFEND.getValue());
            }
            
            if(willBeQueen(board, m.getEnd())) {
                m.setQueening(true);
            }
             
            if(m.getUtility() > bestmove.getUtility()) {
                bestmove = m;
            } else if(m.getUtility() == bestmove.getUtility()) {
                randomList.add(m);
            } else {
                if(!randomList.contains(bestmove)) {
                    randomList.add(bestmove);
                }
            }
        }

        if(!randomList.isEmpty()) {
            int randomNumber = (int)(Math.random() * (randomList.size()));
            bestmove = randomList.get(randomNumber);
        }      
        return bestmove;
    }

    @Override
    public Movement computeBestMoveUtility(Board board) {
        Movement bestmove = new Movement();
        List<Movement> randomList = new ArrayList<>();       
        List<Movement> simpleMove = getAllowedMovement();
        List<Position> listAlly = computeDefendAlly(board);
        if(simpleMove.isEmpty()) {
            return null;
        } else {
            for(Movement m : simpleMove) {
                List<Position> listDefend = listPositionNeedDefender(board, m.getEnd());
                if(willBeQueen(board, m.getEnd())) {
                    m.setUtility(Utility.QUEENING.getValue());
                    m.setQueening(true);
                    
                } else if(listAlly.contains(m.getEnd())) {
                    m.setUtility(Utility.SIMPLE_MOVE_DEFEND.getValue());
                    
                }else if(movingForward(m) && listDefend.size() == 0) {
                    m.setUtility(Utility.SIMPLE_MOVE_FORWARD_SAFE.getValue());
                    
                } else if(movingForward(m) && positionInMiddle(m)) {
                    m.setUtility(Utility.SIMPLE_MOVE_MIDDLE.getValue());
                    
                } else if(movingForward(m) && positionSafeOnEdge(m)) {
                    m.setUtility(Utility.SIMPLE_MOVE_SAFE.getValue());
                    
                } else if(movingForward(m) && listDefend.size() != 0) {
                    m.setUtility(Utility.SIMPLE_MOVE_FORWARD.getValue());
                }
                else {
                    m.setUtility(Utility.SIMPLE_MOVE.getValue());           
                }
                if(m.getUtility() > bestmove.getUtility()) {
                    bestmove = m;
                } 
            }
        }
        return bestmove;
    }
    
    /**
     * Getting to know if the piece is moving forward closer to the queening line.
     * @param moveToTest movement computed
     * @return true if piece is moving forward, false otherwise
     */
    private boolean movingForward(Movement moveToTest) {
        boolean forward = false;
        Position start = moveToTest.getStart();
        Position end = moveToTest.getEnd();
        
        if(getColor() == Color.WHITE) {
            if(end.getLine() == start.getLine()-1) {
                forward = true;
            }
        } else {
            if(end.getLine() == start.getLine()+1) {
                forward = true;
            }
        }
        
        return forward;
    }

    /**
     * Search if the end position of movement is contained inside the list.
     * @param defendAlly list of list position
     * @param moveToTest taking only the end position in consideration
     * @return true if position have been found, false otherwise
     */
    private boolean positionInList(Movement moveToTest) {
        boolean posFound = false;
        List<List<Position>> l = getDefendAllyPosition();
        for(int i = 0; i < l.size(); i++) {
            List<Position> lp = l.get(i);
            if(lp.contains(moveToTest.getEnd())) {
                posFound = true;
                break;
            }
        }
        return posFound;
    }

    /**
     * Search if the end position of movement is safe on edge of the board.
     * @param moveToTest taking only the end position in consideration
     * @return true if the position is on edge, false otherwise
     */
    private boolean positionSafeOnEdge(Movement moveToTest) {
        boolean isSafe = false;
        int line = moveToTest.getEnd().getLine();
        int column = moveToTest.getEnd().getColumn();
        if(line == 0 || line == 7) {
            isSafe = true;
        } else if(column == 0 || column == 7) {
            isSafe = true;
        } else {
            isSafe = false;
        }
        return isSafe;
    }

    /**
     * Search if the end position of movement is in the middle of the board.
     * @param moveToTest taking only the end position in consideration
     * @return true if the position is in the middle, false otherwise
     */
    private boolean positionInMiddle(Movement moveToTest) {
        boolean inMiddle = false;
        int line = moveToTest.getEnd().getLine();
        int column = moveToTest.getEnd().getColumn();
        int size = this.getBoard().getRowSize();
        int middle = (size/2)-1;
        if(line > middle-1 && line < size-middle) {
            if(column > middle-1 && column < size-middle) {
                inMiddle = true;
            }
        }
        return inMiddle;
    }

    /**
     * Computer the number of pieces can actually play this turn.
     * @return number of pieces
     */
    private int numberPlayablePieces() {
        int number = 0;
        number = Communication.playingPieces.size();
        return number;
    }

    @Override
    public void sendPlayingPiece(Board board) {
        if(this.isPieceCanMove() || this.isPieceCanTake()) {
            Communication.setPiecePlaying(this.getID(),this);
        }
    }

    private boolean canMove() {
        if(this.isPieceCanMove() || this.isPieceCanTake()) {
            return true;
        }
        return false;
    }
}
