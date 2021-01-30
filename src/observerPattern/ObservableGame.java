package observerPattern;

import java.util.ArrayList;

import model.Board;
import model.MainGame;
import model.Piece;

public class ObservableGame implements Observable {
    
    private final ArrayList<Observer> listObservers;
    private final MainGame game;
    
    public ObservableGame() {
        listObservers = new ArrayList<>();
        game = new MainGame();
    }
    
    @Override
    public void addObserver(Observer observer) {
        listObservers.add(observer);
        notifyObs();
    }

    @Override
    public void removeObserver(Observer observer) {
        listObservers.remove(observer);
        notifyObs();
        
    }
    
    private void notifyObs() {
        for(Observer obs : listObservers) {
            obs.update(game.getBoard(), game.getOldPosMoving());
        }
    }
    
    public void move() {
        game.move();
        notifyObs();
    }
    
    public Board getBoard() {
        return game.getBoard();
    }
    
    public Piece[][] getBoardPiece() {
        return game.getBoardPiece();
    }
    
    public boolean isGameOver() {
        return game.getGameOver();
    }
    
    

}
