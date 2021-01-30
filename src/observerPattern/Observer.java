package observerPattern;

import model.Board;
import model.Position;

/**
 * Interface Observer.
 */
public interface Observer {

    /**
     * Method which updates the model when the view have changed.
     */
    public void update(Board board, Position oldPos);
}
