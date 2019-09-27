package models;

public class Teacher {

    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private Double oklad;
    private Double nadbavka;
    private String phone;
    private String email;
    private Degree degree;
    private int kafedraChiefId;

    public Teacher() {
        this.nadbavka = null;
        this.id = 0;
        this.kafedraChiefId = 0;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public Double getOklad() {
        return oklad;
    }

    public void setOklad(Double oklad) {
        this.oklad = oklad;
    }

    public Double getNadbavka() {
        return nadbavka;
    }

    public void setNadbavka(Double nadbavka) {
        this.nadbavka = nadbavka;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setKafedraChiefId(int kafedraChiefId) {
        this.kafedraChiefId = kafedraChiefId;
    }

    @Override
    public String toString() {
        return String.format(" - ім'я: %s,\n - прізвище: %s,\n - по-батькові: %s,\n - зарплата: %.3f,\n " +
                        "- надбавка: %.3f,\n - номер телефону: %s,\n - поштова скринька: %s,\n - ступінь: %s,\n " +
                        "- начальник кафедри №: %s",
                this.name, this.surname, this.patronymic, this.oklad, this.nadbavka, this.phone, this.email,
                this.degree, this.kafedraChiefId);
    }
}