package utils;

import models.Degree;
import models.Kafedra;
import models.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteJDBCDriverManager {
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public SQLiteJDBCDriverManager() {
        this.connection = null;
        this.statement = null;
        this.preparedStatement = null;
        this.resultSet = null;
    }

    public void connect(String fileName) {
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        try {
            this.connection = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        } finally {
            try {
                if (this.connection != null) {
                    this.connection.close();
                }
            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
            }
        }
    }

    public void createTable(String fileName, String tableName) {
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        try {
            this.connection = DriverManager.getConnection(url);
            this.statement = this.connection.createStatement();
            if (tableName.equals("Teacher")) {
                String teacherSql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                        + " id INTEGER PRIMARY KEY AUTOINCREMENT, \n"
                        + " name TEXT NOT NULL, \n"
                        + " surname TEXT NOT NULL, \n"
                        + " patronymic TEXT NOT NULL, \n"
                        + " oklad DOUBLE NOT NULL, \n"
                        + " nadbavka DOUBLE, \n"
                        + " phone TEXT NOT NULL, \n"
                        + " email TEXT NOT NULL, \n"
                        + " degree TEXT NOT NULL, \n"
                        + " kafedra_chief_id INTEGER, \n"
                        + " FOREIGN KEY (kafedra_chief_id) REFERENCES Kafedra (id) \n"
                        + ");";
                this.statement.execute(teacherSql);
            } else if (tableName.equals("Kafedra")) {
                String kafedraSql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                        + " id INTEGER PRIMARY KEY AUTOINCREMENT, \n"
                        + " name TEXT NOT NULL, \n"
                        + " chief_id INTEGER, \n"
                        + " FOREIGN KEY (chief_id) REFERENCES Teacher (id) \n"
                        + ");";
                this.statement.execute(kafedraSql);
            } else if (tableName.equals("KafedraTeacher")) {
                String kafedraTeacherSql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                        + " kafedra_id INTEGER, \n"
                        + " teacher_id INTEGER, \n"
                        + " FOREIGN KEY (kafedra_id) REFERENCES Kafedra (id), \n"
                        + " FOREIGN KEY (teacher_id) REFERENCES  Teacher (id) \n"
                        + ");";
                this.statement.execute(kafedraTeacherSql);
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public void insertKafedraObjectToTable(String fileName, Kafedra kafedra) {
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        try {
            this.connection = DriverManager.getConnection(url);
            String insertSql = "INSERT INTO Kafedra (name) VALUES (?);";
            this.preparedStatement = this.connection.prepareStatement(insertSql);
            this.preparedStatement.setString(1, kafedra.getName());
            this.preparedStatement.execute();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public void insertTeacherObjectToTable(String fileName, Teacher teacher) {
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        try {
            this.connection = DriverManager.getConnection(url);
            String insertSql = "INSERT INTO Teacher (name, surname, patronymic, oklad, nadbavka, phone, email, " +
                    "degree) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            this.preparedStatement = this.connection.prepareStatement(insertSql);
            this.preparedStatement.setString(1, teacher.getName());
            this.preparedStatement.setString(2, teacher.getSurname());
            this.preparedStatement.setString(3, teacher.getPatronymic());
            this.preparedStatement.setDouble(4, teacher.getOklad());
            this.preparedStatement.setDouble(5, teacher.getNadbavka());
            this.preparedStatement.setString(6, teacher.getPhone());
            this.preparedStatement.setString(7, teacher.getEmail());
            this.preparedStatement.setString(8, teacher.getDegree().getDegree());
            this.preparedStatement.execute();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public ArrayList<Kafedra> getKafedraArrayList(String fileName) {
        ArrayList<Kafedra> kafedraArrayList = new ArrayList<Kafedra>();
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        String selectAllSql = "SELECT id, name, chief_id FROM Kafedra;";
        try {
            this.connection = DriverManager.getConnection(url);
            this.statement = this.connection.createStatement();
            this.resultSet = this.statement.executeQuery(selectAllSql);
            while (this.resultSet.next()) {
                Kafedra kafedra = new Kafedra();
                kafedra.setId(this.resultSet.getInt("id"));
                kafedra.setName(this.resultSet.getString("name"));
                kafedra.setChiefId(this.resultSet.getInt("chief_id"));
                kafedraArrayList.add(kafedra);
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return kafedraArrayList;
    }

    public void deleteKafedra(String fileName, int kafedraId) {
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        String deleteSql = "DELETE FROM Kafedra WHERE id = ?;";
        try {
            this.connection = DriverManager.getConnection(url);
            this.preparedStatement = this.connection.prepareStatement(deleteSql);
            preparedStatement.setInt(1, kafedraId);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public ArrayList<Teacher> getTeachersList(String fileName) {
        ArrayList<Teacher> teacherArrayList = new ArrayList<Teacher>();
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        String selectAllSql = "SELECT id, name, surname, patronymic, oklad, nadbavka, phone, email, degree, " +
                "kafedra_chief_id FROM Teacher;";
        try {
            this.connection = DriverManager.getConnection(url);
            this.statement = this.connection.createStatement();
            this.resultSet = this.statement.executeQuery(selectAllSql);
            while (this.resultSet.next()) {
                teacherArrayList.add(createTeacher(this.resultSet));
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return teacherArrayList;
    }

    public void deleteTeacher(String fileName, int teacherId) {
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        String deleteSql = "DELETE FROM Teacher WHERE id = ?;";
        try {
            this.connection = DriverManager.getConnection(url);
            this.preparedStatement = this.connection.prepareStatement(deleteSql);
            preparedStatement.setInt(1, teacherId);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public ArrayList<Integer> getAllIdFromTable(String fileName, String tableName) {
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        String selectAllId = "";
        if (tableName.equals("Kafedra")) {
            selectAllId = "SELECT id FROM Kafedra;";
        } else if (tableName.equals("Teacher")) {
            selectAllId = "SELECT id FROM Teacher;";
        }
        ArrayList<Integer> idList = new ArrayList<Integer>();
        try {
            this.connection = DriverManager.getConnection(url);
            this.statement = this.connection.createStatement();
            this.resultSet = this.statement.executeQuery(selectAllId);
            while (this.resultSet.next()) {
                idList.add(this.resultSet.getInt("id"));
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return idList;
    }

    public void setChiefForKafedra(String fileName, int kafedraId, int teacherId) {
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        String updateKafedraChiefSql = "UPDATE Kafedra SET chief_id = ? WHERE id = ?;";
        String updateTeacherChiefSql = "UPDATE Teacher SET kafedra_chief_id = ? WHERE id = ?;";
        try {
            this.connection = DriverManager.getConnection(url);
            this.preparedStatement = this.connection.prepareStatement(updateKafedraChiefSql);
            this.preparedStatement.setInt(2, kafedraId);
            this.preparedStatement.setInt(1, teacherId);
            this.preparedStatement.executeUpdate();
            this.preparedStatement = this.connection.prepareStatement(updateTeacherChiefSql);
            this.preparedStatement.setInt(1, kafedraId);
            this.preparedStatement.setInt(2, teacherId);
            this.preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public Teacher getChiefTeacherInfo(String fileName, int kafedraId) {
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        String getChiefTeacherInfoSql = "SELECT * FROM Teacher where kafedra_chief_id = ?;";
        Teacher teacher = new Teacher();
        try {
            this.connection = DriverManager.getConnection(url);
            this.preparedStatement = this.connection.prepareStatement(getChiefTeacherInfoSql);
            this.preparedStatement.setInt(1, kafedraId);
            this.resultSet = this.preparedStatement.executeQuery();
            while (this.resultSet.next()) {
                teacher = createTeacher(this.resultSet);
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return teacher;
    }

    public void addTeacherToKafedra(String fileName, int kafedraId, int teacherId) {
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        String addTeacherToKafedraSql = "INSERT INTO KafedraTeacher (kafedra_id, teacher_id) VALUES (?, ?);";
        try {
            this.connection = DriverManager.getConnection(url);
            this.preparedStatement = this.connection.prepareStatement(addTeacherToKafedraSql);
            this.preparedStatement.setInt(1, kafedraId);
            this.preparedStatement.setInt(2, teacherId);
            this.preparedStatement.execute();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public HashMap<String, Integer> getKafedraStatistic(String fileName, int kafedraId) {
        HashMap<String, Integer> kafedraStatistic = new HashMap<String, Integer>();
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        String getKafedraStatistic = "SELECT Teacher.degree AS degree, COUNT (Teacher.degree) \n" +
                " AS kafedra_teachers_count FROM Kafedra INNER JOIN KafedraTeacher" +
                " ON KafedraTeacher.kafedra_id = Kafedra.id INNER JOIN Teacher ON" +
                " Teacher.id = KafedraTeacher.teacher_id WHERE Kafedra.id = ? GROUP BY Teacher.degree;";
        try {
            this.connection = DriverManager.getConnection(url);
            this.preparedStatement = this.connection.prepareStatement(getKafedraStatistic);
            this.preparedStatement.setInt(1, kafedraId);
            this.resultSet = this.preparedStatement.executeQuery();
            while (this.resultSet.next()) {
                kafedraStatistic.put(this.resultSet.getString("degree"),
                        this.resultSet.getInt("kafedra_teachers_count"));
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return kafedraStatistic;
    }

    public String getAverageSalaryByKafedra(String fileName, int kafedraId) {
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        String averageSalaryQuery = "SELECT AVG (Teacher.oklad + Teacher.nadbavka) AS average_salary_by_kafedra, \n" +
                " kafedra.name as kafedra_name FROM Teacher INNER JOIN KafedraTeacher ON\n" +
                " KafedraTeacher.teacher_id = Teacher.id INNER JOIN  Kafedra ON\n" +
                " Kafedra.id = KafedraTeacher.kafedra_id WHERE Kafedra.id = ?;";
        double average_salary = 0;
        String kafedra_name = "";
        try {
            this.connection = DriverManager.getConnection(url);
            this.preparedStatement = this.connection.prepareStatement(averageSalaryQuery);
            this.preparedStatement.setInt(1, kafedraId);
            this.resultSet = this.preparedStatement.executeQuery();
            if (this.resultSet.next()) {
                average_salary = this.resultSet.getDouble("average_salary_by_kafedra");
                kafedra_name = this.resultSet.getString("kafedra_name");
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return average_salary + " " + kafedra_name;
    }

    public String getTeachersCountByKafedra(String fileName, int kafedraId) {
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        String teachersCountByKafedra = "SELECT COUNT (KafedraTeacher.teacher_id) AS teachers_count, \n" +
                " Kafedra.name AS kafedra_name FROM KafedraTeacher INNER JOIN Kafedra ON\n " +
                " Kafedra.id = KafedraTeacher.kafedra_id WHERE KafedraTeacher.kafedra_id = ?;";
        int teachers_count = 0;
        String kafedra_name = "";
        try {
            this.connection = DriverManager.getConnection(url);
            this.preparedStatement = this.connection.prepareStatement(teachersCountByKafedra);
            this.preparedStatement.setInt(1, kafedraId);
            this.resultSet = this.preparedStatement.executeQuery();
            if (this.resultSet.next()) {
                teachers_count = this.resultSet.getInt("teachers_count");
                kafedra_name = this.resultSet.getString("kafedra_name");
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return teachers_count + " " + kafedra_name;
    }

    public ArrayList<Teacher> globalTeacherSearching(String fileName, String searchString) {
        ArrayList<Teacher> teachersArrayList = new ArrayList<Teacher>();
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        String searchQuery = "SELECT * FROM Teacher WHERE Teacher.name LIKE '%" + searchString + "%'" +
                " OR Teacher.surname LIKE '%" + searchString + "%' OR Teacher.patronymic" +
                " LIKE '%" + searchString + "%' OR Teacher.oklad LIKE '%" + searchString + "%' OR Teacher.nadbavka" +
                " LIKE '%" + searchString + "%' OR Teacher.phone LIKE '%" + searchString + "%' OR Teacher.email" +
                " LIKE '%" + searchString + "%' OR Teacher.degree LIKE '%" + searchString + "%'" +
                " OR Teacher.kafedra_chief_id LIKE '%" + searchString + "%';";
        try {
            this.connection = DriverManager.getConnection(url);
            this.statement = this.connection.createStatement();
            this.resultSet = this.statement.executeQuery(searchQuery);
            while (this.resultSet.next()) {
                teachersArrayList.add(createTeacher(this.resultSet));
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return teachersArrayList;
    }

    public ArrayList<Kafedra> globalKafedraSearching(String fileName, String searchString) {
        ArrayList<Kafedra> kafedraArrayList = new ArrayList<Kafedra>();
        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
        String searchQuery = "SELECT * FROM Kafedra WHERE Kafedra.name LIKE '%" + searchString + "%'" +
                " OR Kafedra.chief_id LIKE '%" + searchString + "%';";
        try {
            this.connection = DriverManager.getConnection(url);
            this.statement = this.connection.createStatement();
            this.resultSet = this.statement.executeQuery(searchQuery);
            while (this.resultSet.next()) {
                Kafedra kafedra = new Kafedra();
                kafedra.setName(this.resultSet.getString("name"));
                kafedra.setChiefId(this.resultSet.getInt("chief_id"));
                kafedraArrayList.add(kafedra);
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return kafedraArrayList;
    }

    private Teacher createTeacher(ResultSet resultSet) {
        Teacher teacher = new Teacher();
        try {
            teacher.setId(resultSet.getInt("id"));
            teacher.setName(resultSet.getString("name"));
            teacher.setSurname(resultSet.getString("surname"));
            teacher.setPatronymic(resultSet.getString("patronymic"));
            teacher.setOklad(resultSet.getDouble("oklad"));
            teacher.setNadbavka(resultSet.getDouble("nadbavka"));
            teacher.setPhone(resultSet.getString("phone"));
            teacher.setEmail(resultSet.getString("email"));
            teacher.setKafedraChiefId(resultSet.getInt("kafedra_chief_id"));
            String strDegree = resultSet.getString("degree");
            if (strDegree.equals("Асистент")) {
                teacher.setDegree(Degree.ASSISTANT);
            } else if (strDegree.equals("Доцент")) {
                teacher.setDegree(Degree.DOCENT);
            } else if (strDegree.equals("Професор")) {
                teacher.setDegree(Degree.PROFESSOR);
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return teacher;
    }
}