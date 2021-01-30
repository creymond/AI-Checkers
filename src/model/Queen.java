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
public class Queen extends Piece {

    public Queen(Color color) {
        super(color, PieceType.QUEEN);
    }

    public Queen(Color color, Position position) {
        super(color, PieceType.QUEEN, position);
    }

    @Override
    public void computeLists(Board board) {
        clearAllLists();
        listAllowedMove(board);
        listPiecesCanBeTaken(board);
    }

    /**
     * Display the pieces moves for the current turn.
     * Used for debug only.
     */
    private void displayAllowedMove() {
        for(Movement p : getAllowedMovement()) {

        }
    }

    @Override
    public void listAllowedMove(Board board) {
        List<Position> upperRight = getUpperRightDiagonal(board);
        List<Position> upperLeft = getUpperLeftDiagonal(board);
        List<Position> downRight = getDownRightDiagonal(board);
        List<Position> downLeft = getDownLeftDiagonal(board);
        if(upperRight.size() != 0) {
            for(Position p : upperRight) {
                if(p != null) {
                    Movement move = new Movement(this.getPosition(), p);
                    addAllowedMovement(move);
                    setPieceCanMove(true);
                }            
            }
        }

        if(upperLeft.size() != 0) {
            for(Position p : upperLeft) {
                if(p != null) {
                    Movement move = new Movement(this.getPosition(), p);
                    addAllowedMovement(move);
                    setPieceCanMove(true);
                }
            }
        }

        if(downRight.size() != 0) {
            for(Position p : downRight) {
                if(p != null) {
                    Movement move = new Movement(this.getPosition(), p);
                    addAllowedMovement(move);
                    setPieceCanMove(true);
                }
            }
        }

        if(downLeft.size() != 0) {
            for(Position p : downLeft) {
                if(p != null) {
                    Movement move = new Movement(this.getPosition(), p);
                    addAllowedMovement(move);
                    setPieceCanMove(true);
                }
            }
        }
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
        Color oppositeColor = getOppositeColor(this.getColor());
        int currentLine = this.getPosition().getLine();
        int currentColumn = this.getPosition().getColumn();
        int tmpCol = currentColumn;

        // Upper right diagonal
        for(int i = currentLine - 1; i >= 0; i--) {
            tmpCol = tmpCol + 1;
            if(!outOfBound(board, i, tmpCol)) {
                if(board.hasPiece(i,tmpCol)) {
                    if(board.getPiece(i, tmpCol).getColor() == oppositeColor) {
                        // There is a opposite color piece on the way
                        if(emptyCaseAfterPiece(board, i, tmpCol, "upR")) {
                            setPieceCanTake(true);

                            // Creating new pair <Position of enemy, List of free position after>
                            Position enemy = new Position(i,tmpCol);
                            List<Position> freeCases = new ArrayList<>();
                            freeCases = getListEmptyCasesAfterPiece(board, i, tmpCol, "upR");

                            Movement move = new Movement(this.getPosition(), freeCases, enemy);              
                            addPieceCanBeTaken(move);
                        }
                    } else {
                        return;
                    }
                }
            }
        }
        tmpCol = currentColumn;

        // Upper left diagonal
        for(int i = currentLine - 1; i >= 0; i--) {
            tmpCol = tmpCol - 1;
            if(!outOfBound(board, i, tmpCol)) {
                if(board.hasPiece(i,tmpCol)) {
                    if(board.getPiece(i, tmpCol).getColor() == oppositeColor) {
                        // There is a opposite color piece on the way
                        if(emptyCaseAfterPiece(board, i, tmpCol, "upL")) {
                            setPieceCanTake(true);

                            // Creating new pair <Position of enemy, List of free position after>
                            Position enemy = new Position(i,tmpCol);
                            List<Position> freeCases = new ArrayList<>();
                            freeCases = getListEmptyCasesAfterPiece(board, i, tmpCol, "upL");

                            Movement move = new Movement(this.getPosition(), freeCases, enemy);              
                            addPieceCanBeTaken(move);
                        }
                    } else {
                        return;
                    }
                }
            }
        }
        tmpCol = currentColumn;

        // Down right diagonal
        for(int i = currentLine + 1; i < board.getRowSize(); i++) {
            tmpCol = tmpCol + 1;
            if(!outOfBound(board, i, tmpCol)) {
                if(board.hasPiece(i,tmpCol)) {
                    if(board.getPiece(i, tmpCol).getColor() == oppositeColor) {
                        // There is a opposite color piece on the way
                        if(emptyCaseAfterPiece(board, i, tmpCol, "downR")) {
                            setPieceCanTake(true);

                            // Creating new pair <Position of enemy, List of free position after>
                            Position enemy = new Position(i,tmpCol);
                            List<Position> freeCases = new ArrayList<>();
                            freeCases = getListEmptyCasesAfterPiece(board, i, tmpCol, "downR");

                            Movement move = new Movement(this.getPosition(), freeCases, enemy);              
                            addPieceCanBeTaken(move);
                        }
                    } else {
                        return;
                    }
                }
            }
        }
        tmpCol = currentColumn;

        // Down left diagonal
        for(int i = currentLine + 1; i < board.getRowSize(); i++) {
            tmpCol = tmpCol - 1;
            if(!outOfBound(board, i, tmpCol)) {
                if(board.hasPiece(i,tmpCol)) {
                    if(board.getPiece(i, tmpCol).getColor() == oppositeColor) {
                        // There is a opposite color piece on the way
                        if(emptyCaseAfterPiece(board, i, tmpCol, "downL")) {
                            setPieceCanTake(true);

                            // Creating new pair <Position of enemy, List of free position after>
                            Position enemy = new Position(i,tmpCol);
                            List<Position> freeCases = new ArrayList<>();
                            freeCases = getListEmptyCasesAfterPiece(board, i, tmpCol, "downL");

                            Movement move = new Movement(this.getPosition(), freeCases, enemy);              
                            addPieceCanBeTaken(move);
                        }
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /**
     * Get the list of cases which can be accessed for normal move along the upper right diagonal.
     * @param board
     * @return list of free cases
     */
    public List<Position> getUpperRightDiagonal(Board board) {
        List<Position> listCases = new ArrayList<>();

        int currentLine = this.getPosition().getLine();
        int currentColumn = this.getPosition().getColumn();
        if(currentLine != 0) {
            for(int i = currentLine-1; i >= 0; i--) {
                currentColumn = currentColumn + 1;
                if(!outOfBound(board, i, currentColumn)) {
                    if(board.hasPiece(i, currentColumn)) {
                        return listCases;
                    } else {
                        listCases.add(new Position(i, currentColumn));
                    }
                }
            }            
        }
        return listCases;
    }

    /**
     * Get the list of cases which can be accessed for normal move along the upper left diagonal.
     * @param board
     * @return list of free cases
     */
    public List<Position> getUpperLeftDiagonal(Board board) {
        List<Position> listCases = new ArrayList<>();

        int currentLine = this.getPosition().getLine();
        int currentColumn = this.getPosition().getColumn();
        if(currentLine != 0) {
            for(int i = currentLine-1; i >= 0; i--) {
                currentColumn = currentColumn - 1;
                if(!outOfBound(board, i, currentColumn)) {
                    if(board.hasPiece(i, currentColumn)) {
                        return listCases;
                    } else {
                        listCases.add(new Position(i, currentColumn));
                    }  
                }
            }
        }
        return listCases;
    }

    /**
     * Get the list of cases which can be accessed for normal move along the down left diagonal.
     * @param board
     * @return list of free cases
     */
    public List<Position> getDownLeftDiagonal(Board board) {
        List<Position> listCases = new ArrayList<>();

        int currentLine = this.getPosition().getLine();
        int currentColumn = this.getPosition().getColumn();
        if(currentLine != 7) {
            for(int i = currentLine+1; i <= board.getRowSize(); i++) {
                currentColumn = currentColumn - 1;
                if(!outOfBound(board, i, currentColumn)) {
                    if(board.hasPiece(i, currentColumn)) {
                        return listCases;
                    } else {
                        listCases.add(new Position(i, currentColumn));
                    }
                }
            }
        }   
        return listCases;
    }

    /**
     * Get the list of cases which can be accessed for normal move along the down right diagonal.
     * @param board
     * @return list of free cases
     */
    public List<Position> getDownRightDiagonal(Board board) {
        List<Position> listCases = new ArrayList<>();

        int currentLine = this.getPosition().getLine();
        int currentColumn = this.getPosition().getColumn();
        if(currentLine != 7) {
            for(int i = currentLine+1; i <= board.getRowSize(); i++) {
                currentColumn = currentColumn + 1;
                if(!outOfBound(board, i, currentColumn)) {
                    if(board.hasPiece(i, currentColumn)) {
                        return listCases;
                    } else {
                        listCases.add(new Position(i, currentColumn));
                    } 
                }
            }
        } 
        return listCases;
    }

    @Override
    public List<List<Position>> computeRafleList(Board board) {
        // TODO Auto-generated method stub
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

    private boolean emptyCaseAfterPiece(Board board, int line, int column, String direction) {
        if(direction == "upR") {
            if(!outOfBound(board, line-1, column+1)) {
                if(!board.hasPiece(line-1, column+1)) {
                    return true;
                }
            }
        } else if(direction == "upL") {
            if(!outOfBound(board, line-1, column-1)) {
                if(!board.hasPiece(line-1, column-1)) {
                    return true;
                }
            }

        } else if(direction == "downR") {
            if(!outOfBound(board, line+1, column+1)) {
                if(!board.hasPiece(line+1, column+1)) {
                    return true;
                }
            }

        } else { // downL
            if(!outOfBound(board, line+1, column-1)) {
                if(!board.hasPiece(line+1, column-1)) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * Obtain the cases where a queen can stop her path on after taking a piece.
     * @param board
     * @param line position's line of the enemy piece to capture
     * @param column position's column of the enemy piece to capture
     * @param direction define the diagonal
     * @return list of free cases
     */
    private List<Position> getListEmptyCasesAfterPiece(Board board, int line, int column, String direction) {
        List<Position> listCases = new ArrayList<>();
        int tmpCol = column;
        if(direction == "upR") {
            for(int i = line-1; i >= 0; i--) {
                tmpCol = tmpCol + 1;
                if(!outOfBound(board, i, tmpCol)) {
                    if(!board.hasPiece(i, tmpCol)) {
                        listCases.add(new Position(i, tmpCol));
                    } else {
                        return listCases;
                    }
                }
            }
        } else if(direction == "upL") {
            for(int i = line-1; i >= 0; i--) {
                tmpCol = tmpCol - 1;
                if(!outOfBound(board, i, tmpCol)) {
                    if(!board.hasPiece(i, tmpCol)) {
                        listCases.add(new Position(i, tmpCol));
                    } else {
                        return listCases;
                    }
                }
            }
        } else if(direction == "downR") {
            for(int i = line+1; i < board.getRowSize(); i++) {
                tmpCol = tmpCol + 1;
                if(!outOfBound(board, i, tmpCol)) {
                    if(!board.hasPiece(i, tmpCol)) {
                        listCases.add(new Position(i, tmpCol));
                    } else {
                        return listCases;
                    }
                }
            }
        } else {
            for(int i = line+1; i < board.getRowSize(); i++) {
                tmpCol = tmpCol - 1;
                if(!outOfBound(board, i, tmpCol)) {
                    if(!board.hasPiece(i, tmpCol)) {
                        listCases.add(new Position(i, tmpCol));
                    } else {
                        return listCases;
                    }
                }
            }
        }
        return listCases;
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

    @Override
    public void sendMove() {
        Communication.setBestMove(this,this.getBestUtilityMove());
    }

    @Override
    public void run() {
        boolean cond = true;
        while(cond) {
            setBoard(Communication.board);
            //setHasPlayedThisTurn(getBoard().getPieceByID(getID()).hasPlayedThisTurn());
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
    public void sendPositionDefend(Board board) {

    }

    @Override
    public void sendPlayingPiece(Board board) {
        if(this.isPieceCanMove() || this.isPieceCanTake()) {
            Communication.setPiecePlaying(this.getID(),this);
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

    @Override
    public Movement computeBestCaptureUtility(Board board) {
        int utility = 0;
        Movement bestmove = new Movement();
        bestmove.setUtility(utility);
        List<Movement> randomList = new ArrayList<>();
        List<Movement> piecesTaken = getPiecesCanBeTaken();
        for(Movement m : piecesTaken) {
            List<Position> listEnd = m.getListEnd();
            int random = (int)(Math.random() * (listEnd.size()));
            m.setEnd(listEnd.get(random));
            List<Position> listDefend = listPositionNeedDefender(board, this.getPosition());
            if(listDefend.isEmpty()) {
                // needn't be defended, no risk
                m.setUtility(Utility.CAPTURE.getValue()+1);
            } else {
                // could need to be defended
                m.setUtility(Utility.CAPTURE_DEFEND.getValue()+1);
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
        int utility = 0;
        Movement bestmove = new Movement();
        List<Movement> randomList = new ArrayList<>();       
        List<Movement> simpleMove = getAllowedMovement();
        List<Position> listAlly = computeDefendAlly(board);

        for(Movement m : simpleMove) {
            System.out.println("Queen from " + getPosition() + "move : " + m.getEnd().toString());
            if(listAlly.contains(m.getEnd())) {
                m.setUtility(Utility.SIMPLE_MOVE_DEFEND.getValue()+1);
            } else if(positionSafeOnEdge(m)) {
                m.setUtility(Utility.SIMPLE_MOVE_SAFE.getValue()+1);
            } else if(positionInMiddle(m)) {
                m.setUtility(Utility.SIMPLE_MOVE_MIDDLE.getValue()+1);
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

    /**
     * Search if the end position of movement is contained inside the list.
     * @param defendAlly list of list position
     * @param moveToTest taking only the end position in consideration
     * @return true if position have been found, false otherwise
     */
    private boolean positionInList(Movement moveToTest) {
        boolean posFound = false;
        /*
        Enumeration<List<Position>> values = Communication.getDefendAllyPosition();
        endLoop :
            while(values.hasMoreElements()) {
                List<Position> list = values.nextElement();
                if(list.contains(moveToTest.getEnd())) {
                    posFound = true;
                    break endLoop;
                }
            }
            */
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

    private boolean canMove() {
        if(this.isPieceCanMove() || this.isPieceCanTake()) {
            return true;
        }
        return false;
    }
}
