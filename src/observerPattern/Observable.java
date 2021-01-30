package observerPattern;

/**
 * Interface observable.
 *
 * @author Cyril Reymond
 */
public interface Observable {

    /**
     * Method to add an observer.
     *
     * @param observer observer to add
     */
    public void addObserver(Observer observer);

    /**
     * Method to remove an observer.
     *
     * @param observer observer to remove
     */
    public void removeObserver(Observer observer);
}
