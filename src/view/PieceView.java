package view;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PieceView extends Parent {
    boolean isQueen = false;

    private final Circle bigCircle;
    private final Circle smallCircle;
    private Color centerColor;
    private Color borderColor;
    private Color color;

    public PieceView() {
        bigCircle = new Circle(20);
        smallCircle = new Circle(10);
        getChildren().addAll(bigCircle, smallCircle);
    }

    public void setIsQueen(boolean isQueen) {
        this.isQueen = isQueen;
        smallCircle.setVisible(isQueen);
    }

    public boolean isQueen() {
        return isQueen;
    }

    public void setColor(Color color) {
        this.color = color;
        centerColor = color;
        if(color == Color.WHITE) {
            borderColor = Color.BLACK;
        } else {
            borderColor = Color.WHITE;
        }
        bigCircle.setFill(centerColor);
        bigCircle.setStroke(borderColor);

        if (isQueen()) {
            smallCircle.setFill(centerColor);
            smallCircle.setStroke(borderColor);
        }
    }
    
    public Color getColor() {
        return color;
    }
    
    public void setLight(boolean light) {
        if(light) {
            bigCircle.setStyle("-fx-effect: dropshadow(three-pass-box, "
                    + "rgba(0,0,0,0.8), 10, 0, 0, 0);");
        } else {
            bigCircle.setStyle("");
        }
}
}

