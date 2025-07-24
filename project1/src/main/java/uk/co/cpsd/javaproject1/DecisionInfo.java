package uk.co.cpsd.javaproject1;

import java.awt.Point;

public class DecisionInfo {

    private DecisionType type;
    private Point nextPos;

    public enum DecisionType {
        EAT,
        FLEE,
        REPRODUCE,
        WANDER,
    }

    public DecisionInfo(DecisionType type, Point nextPos) {
        this.type = type;
        this.nextPos = nextPos;
    }

    public DecisionType getType() {
        return type;
    }

    public Point getNextPos() {
        return nextPos;
    }

}
