package models;

public enum Degree {
    ASSISTANT("Асистент"),
    DOCENT("Доцент"),
    PROFESSOR("Професор");

    private String degree;

    Degree(String degree) {
        this.degree = degree;
    }

    public String getDegree() {
        return degree;
    }

    @Override
    public String toString() {
        return String.format("%s", this.degree);
    }
}