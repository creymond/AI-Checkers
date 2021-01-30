package view;


import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class TileView  extends StackPane {
    private PieceView piece;
    
    private boolean hasLight = false;

    public TileView(Color tileColor) {
        setBackground(new Background(new BackgroundFill(tileColor, null, null)));
        setPrefSize(50,50);
        setMinSize(50,50);
   }

    public boolean hasPiece() {
        return (piece != null);
    }

    public PieceView getPiece() {
        return piece;
    }

    public void setPiece(PieceView piece) {
        this.piece = piece;
        getChildren().clear();
        if (piece != null) {
            getChildren().add(piece);
        }
    }
    
    public void setLight(boolean light) {
        hasLight = light;
        if(hasLight) {
            this.setStyle("-fx-background-color: #90ee90;");
        } else {
            this.setStyle("");
        }
    }
    
    public boolean isLight() {
        return hasLight;
    }
}
