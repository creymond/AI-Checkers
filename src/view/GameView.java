package view;

import java.util.ArrayList;
import java.util.List;

import enums.PieceType;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Board;
import model.Piece;
import model.Position;
import observerPattern.Observer;

public class GameView implements Observer {
    public static final int SIZE = 8;
    public static int WIDTH;
    public static int HEIGHT;
    private Board board;

    private BorderPane root;
    private GridPane gridPane;
    private Scene scene;
    private List<List<TileView>> tileView;

    /**
     * Default constructor.
     */
    public GameView(int x, int y, Stage stage, Board board) {
        WIDTH = x;
        HEIGHT = y;
        this.board = board;
        root = new BorderPane();
        root.setPrefSize(WIDTH, HEIGHT);
        
        gridPane = new GridPane();
        setBoard(board);
        root.setCenter(gridPane);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Jeu de Dames");
        stage.show();
    }

    /**
     * Set the view for the board & create a view for each pieces.
     * @param board Board
     */
    public void setBoard(Board board) {
        tileView = new ArrayList<>();

        for (int i = 0; i < board.getRowSize(); ++i) {
            tileView.add(new ArrayList<>());
            for (int j = 0; j < board.getColumnSize(); ++j) {
                Color tileColor;
                if((i + j) % 2 == 0) {
                    tileColor = Color.WHEAT;
                } else {
                    tileColor = Color.BURLYWOOD;
                }
                TileView tile = new TileView(tileColor);
                tileView.get(i).add(tile);
                gridPane.add(tile, j, i);
            }
        }
        ColumnConstraints cc = new ColumnConstraints();
        RowConstraints rc = new RowConstraints();
        cc.setPrefWidth(75);
        rc.setPrefHeight(75);
        for (int i = 0; i < board.getColumnSize(); i++) {
            gridPane.getColumnConstraints().add(cc);
            gridPane.getRowConstraints().add(rc);
        }
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public void update(Board board, Position oldPos) {
        
        for(int i = 0; i < this.board.getRowSize(); i++) {
            for(int j = 0; j < this.board.getColumnSize(); j++) {             
                TileView tile = this.tileView.get(i).get(j);
                tile.setLight(false);
                if(!board.hasPiece(i,j)) {
                    tile.setPiece(null);
                } else {
                    Piece piece = board.getPiece(i,j);
                    PieceView pv = new PieceView();
                    boolean isQueen = false;
                    if(piece.getPieceType() == PieceType.QUEEN) {
                        isQueen = true;
                    }
                    pv.setIsQueen(isQueen);
                    tile.setPiece(pv);
                    Color pieceColor;
                    if(piece.getColor() == enums.Color.BLACK) {
                        pieceColor = Color.BLACK;
                    } else {
                        pieceColor = Color.WHITE;
                    }        
                    pv.setColor(pieceColor);
                    if(piece.isMoving()) {
                        TileView tile2 = this.tileView.get(oldPos.getLine()).get(oldPos.getColumn());
                        tile2.setPiece(null);
                        tile2.setLight(true);
                        tile.setLight(true);
                        tile.getPiece().setLight(true);
                        
                    }
                }
                
            }
        }

    }
}
