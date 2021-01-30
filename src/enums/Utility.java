package enums;

public enum Utility {
    CAPTURE(6),
    CAPTURE_DEFEND(5),
    CAPTURE_MALUS(4),
    QUEENING(7),
    SIMPLE_MOVE_SAFE(3),
    SIMPLE_MOVE_MALUS(2),
    SIMPLE_MOVE_DEFEND(5),
    SIMPLE_MOVE_FORWARD(4),
    SIMPLE_MOVE_FORWARD_SAFE(5),
    SIMPLE_MOVE_MIDDLE(4),
    SIMPLE_MOVE(2);
    
    private final int value;
    
    private Utility(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
}
