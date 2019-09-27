package models;

public class Kafedra {

    private int id;
    private String name;
    private int chiefId;

    public Kafedra() {
        this.id = 0;
        this.chiefId = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChiefId(int chiefId) {
        this.chiefId = chiefId;
    }

    @Override
    public String toString() {
        return String.format(" -назва: %s,\n -ID начальника: %d", this.name, this.chiefId);
    }
}