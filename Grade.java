// src/edu/ccrm/domain/Grade.java
package edu.ccrm.domain;

public enum Grade {
    S("S", 10.0),
    A("A", 9.0),
    B("B", 8.0),
    C("C", 7.0),
    D("D", 6.0),
    E("E", 5.0),
    F("F", 0.0);
    
    private final String letter;
    private final double points;
    
    Grade(String letter, double points) {
        this.letter = letter;
        this.points = points;
    }
    
    public String getLetter() { return letter; }
    public double getPoints() { return points; }
    
    public static Grade fromPoints(double points) {
        if (points >= 9.5) return S;
        if (points >= 8.5) return A;
        if (points >= 7.5) return B;
        if (points >= 6.5) return C;
        if (points >= 5.5) return D;
        if (points >= 4.5) return E;
        return F;
    }
    
    @Override
    public String toString() {
        return letter;
    }
}