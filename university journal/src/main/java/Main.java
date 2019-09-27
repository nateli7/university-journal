import models.Degree;
import models.Kafedra;
import models.Teacher;
import utils.SQLiteJDBCDriverManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static BufferedReader reader;
    private static SQLiteJDBCDriverManager sqLiteJDBCDriverManager;
    private static String fileName = "university_cli.db";

    public static void main(String[] args) throws IOException {
        sqLiteJDBCDriverManager = new SQLiteJDBCDriverManager();
        sqLiteJDBCDriverManager.connect(fileName);
        sqLiteJDBCDriverManager.createTable(fileName, "Kafedra");
        sqLiteJDBCDriverManager.createTable(fileName, "Teacher");
        sqLiteJDBCDriverManager.createTable(fileName, "KafedraTeacher");
        System.out.println("Доброго дня! Введіть цифру відповідно до дії, яку хочете здійснити.");
        mainMenu();
    }

    private static void mainMenu() throws IOException {
        System.out.println("\n1. Відобразити список кафедр.");
        System.out.println("2. Відобразити список викладачів.");
        System.out.println("3. Додати кафедру.");
        System.out.println("4. Додати лектора/викладача.");
        System.out.println("5. Видалити кафедру.");
        System.out.println("6. Видалити лектора/викладача.");
        System.out.println("7. Встановити/змінити начальника для кафедри.");
        System.out.println("8. Переглянути інформацію про начальника кафедри.");
        System.out.println("9. Додати до кафедри лектора/викладача.");
        System.out.println("10. Відобразити статистику по кафедрі.");
        System.out.println("11. Відобразити середню зарплату по кафедрі.");
        System.out.println("12. Відобразити кількість працівників на кафедрі.");
        System.out.println("13. Глобальний пошук по шаблону.");
        System.out.println("14. Вихід.");
        System.out.print("Ваш вибір: ");
        reader = new BufferedReader(new InputStreamReader(System.in));
        int action = 0;
        boolean success = false;
        while (!success) {
            try {
                action = Integer.parseInt(reader.readLine());
                success = true;
            } catch (NumberFormatException numberFormatException) {
                System.out.println("\nЗробіть коректний вибір!");
                mainMenu();
            }
        }
        switch (action) {
            case 1:
                showKafedrasList();
                mainMenu();
                break;
            case 2:
                showTeachersList();
                mainMenu();
                break;
            case 3:
                createKafedra();
                mainMenu();
                break;
            case 4:
                createTeacher();
                mainMenu();
                break;
            case 5:
                deleteKafedra();
                mainMenu();
                break;
            case 6:
                deleteTeacher();
                mainMenu();
                break;
            case 7:
                setKafedraChief();
                mainMenu();
                break;
            case 8:
                showKafedraChiefInfo();
                mainMenu();
                break;
            case 9:
                addTeacherToKafedra();
                mainMenu();
                break;
            case 10:
                showKafedraStatistic();
                mainMenu();
                break;
            case 11:
                showAverageSalaryByKafedra();
                mainMenu();
                break;
            case 12:
                showTeachersCountOnKafedra();
                mainMenu();
                break;
            case 13:
                globalSearch();
                mainMenu();
                break;
            default:
                exit();
                break;
        }
    }

    private static void showKafedrasList() throws IOException {
        ArrayList<Kafedra> kafedraArrayList = sqLiteJDBCDriverManager.getKafedraArrayList(fileName);
        if (kafedraArrayList.size() == 0) {
            System.out.println("\nВ університеті ще не створено жодної кафедри.");
            mainMenu();
        } else {
            System.out.println("\nСписок кафедр університету:");
            for (Kafedra kafedra : kafedraArrayList) {
                System.out.println(kafedra.getId() + ". " + kafedra.getName());
            }
        }
    }

    private static void showTeachersList() throws IOException {
        ArrayList<Teacher> teacherArrayList = sqLiteJDBCDriverManager.getTeachersList(fileName);
        if (teacherArrayList.size() == 0) {
            System.out.println("\nВ університеті ще не працює жоден викладач.");
            mainMenu();
        } else {
            System.out.println("\nСписок викладачів університету:");
            for (Teacher teacher : teacherArrayList) {
                System.out.println(teacher.getId() + ". " + teacher.getDegree().toString() + " "
                        + teacher.getName() + " " + teacher.getSurname() + " " + teacher.getPatronymic() + " "
                        + teacher.getEmail());
            }
        }
    }

    private static void createKafedra() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        Kafedra kafedra = new Kafedra();
        System.out.print("\nВведіть назву кафедри: ");
        kafedra.setName(reader.readLine());
        sqLiteJDBCDriverManager.insertKafedraObjectToTable(fileName, kafedra);
        System.out.println("\nКафедру додано.");
    }

    private static void createTeacher() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        Teacher teacher = new Teacher();
        System.out.print("\nВведіть ім'я викладача: ");
        teacher.setName(reader.readLine());
        System.out.print("Введіть прізвище викладача: ");
        teacher.setSurname(reader.readLine());
        System.out.print("Введіть по-батькові викладача: ");
        teacher.setPatronymic(reader.readLine());
        boolean success = false;
        while (!success) {
            try {
                System.out.print("Введіть оклад викладача (наприклад - 3900.00 або 3900): ");
                teacher.setOklad(Double.parseDouble(reader.readLine()));
                success = true;
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Спробуйте ввести коректні дані!");
            }
        }
        success = false;
        while (!success) {
            try {
                System.out.print("Введіть надбавку викладача (за замовлуванням 0): ");
                String nadbavka = reader.readLine();
                if (nadbavka.trim().equals("") || nadbavka.trim().length() == 0) {
                    teacher.setNadbavka(.0);
                    break;
                } else {
                    teacher.setNadbavka(Double.parseDouble(nadbavka));
                    success = true;
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Спробуйте ввести коректні дані!");
            }
        }
        System.out.print("Введіть номер телефону викладача: ");
        teacher.setPhone(reader.readLine());
        System.out.print("Введіть почтову скриньку викладача: ");
        teacher.setEmail(reader.readLine());
        success = false;
        while (!success) {
            try {
                System.out.print("Введіть посаду викладача (1 - Асистент, 2 - Доцент, 3 - Професор): ");
                int degree = Integer.parseInt(reader.readLine());
                switch (degree) {
                    case 1:
                        teacher.setDegree(Degree.ASSISTANT);
                        success = true;
                        break;
                    case 2:
                        teacher.setDegree(Degree.DOCENT);
                        success = true;
                        break;
                    case 3:
                        teacher.setDegree(Degree.PROFESSOR);
                        success = true;
                        break;
                    default:
                        System.out.println("Виберіть коректну посаду!");
                        success = false;
                        break;
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Спробуйте ввести коректні дані!");
            }
        }
        sqLiteJDBCDriverManager.insertTeacherObjectToTable(fileName, teacher);
        System.out.println("\nВикладача додано.");
    }

    private static void deleteKafedra() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        showKafedrasList();
        boolean success = false;
        while (!success) {
            try {
                System.out.print("Введіть ID кафедри для видалення: ");
                int kafedraId = Integer.parseInt(reader.readLine());
                if (!sqLiteJDBCDriverManager.getAllIdFromTable(fileName, "Kafedra").contains(kafedraId)) {
                    throw new NumberFormatException();
                }
                sqLiteJDBCDriverManager.deleteKafedra(fileName, kafedraId);
                System.out.println("\nКафедру видалено.");
                success = true;
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Спробуйте ввести коректний ID кафедри!");
            }
        }
    }

    private static void deleteTeacher() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        showTeachersList();
        boolean success = false;
        while (!success) {
            try {
                System.out.print("Введіть ID викладача для видалення: ");
                int teacherId = Integer.parseInt(reader.readLine());
                if (!sqLiteJDBCDriverManager.getAllIdFromTable(fileName, "Teacher").contains(teacherId)) {
                    throw new NumberFormatException();
                }
                sqLiteJDBCDriverManager.deleteTeacher(fileName, teacherId);
                System.out.println("\nВикладача видалено.");
                success = true;
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Спробуйте ввести коректний ID викладача!");
            }
        }

    }

    private static void setKafedraChief() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Kafedra> kafedraArrayList = sqLiteJDBCDriverManager.getKafedraArrayList(fileName);
        ArrayList<Teacher> teacherArrayList = sqLiteJDBCDriverManager.getTeachersList(fileName);
        if (kafedraArrayList.size() == 0 || teacherArrayList.size() == 0) {
            System.out.println("\nВ університеті недостатня кількість викладачів або кафедр. Додайте інформацію " +
                    "і повторіть спробу пізніше.");
            mainMenu();
        } else {
            System.out.println("\nВиберіть ID кафедри, для якої хочете назначити начальника.");
            showKafedrasList();
            boolean success = false;
            int kafedraId = 0;
            int teacherId = 0;
            while (!success) {
                try {
                    System.out.print("Ваш вибір: ");
                    kafedraId = Integer.parseInt(reader.readLine());
                    if (!sqLiteJDBCDriverManager.getAllIdFromTable(fileName, "Kafedra").contains(kafedraId)) {
                        throw new NumberFormatException();
                    }
                    success = true;
                } catch (NumberFormatException numberFormatException) {
                    System.out.println("Введіть коректний ID кафедри!");
                }
            }
            System.out.println("\nВиберіть ID викладача, якого хочете назначити начальником обраної кафедри.");
            showTeachersList();
            success = false;
            while (!success) {
                try {
                    System.out.print("Ваш вибір: ");
                    teacherId = Integer.parseInt(reader.readLine());
                    if (!sqLiteJDBCDriverManager.getAllIdFromTable(fileName, "Teacher").contains(teacherId)) {
                        throw new NumberFormatException();
                    }
                    success = true;
                } catch (NumberFormatException numberFormatException) {
                    System.out.println("Введіть коректний ID викладача!");
                }
            }
            sqLiteJDBCDriverManager.setChiefForKafedra(fileName, kafedraId, teacherId);
            System.out.println("\nНачальника кафедри змінено успішно.");
        }
    }

    private static void showKafedraChiefInfo() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Kafedra> kafedraArrayList = sqLiteJDBCDriverManager.getKafedraArrayList(fileName);
        if (kafedraArrayList.size() == 0) {
            System.out.println("\nВ університеті ще немає жодної кафедри. Додайте інформацію і повторіть спробу" +
                    " пізніше.");
            mainMenu();
        } else {
            System.out.println("\nВиберіть ID кафедри, для якої бажаєте переглянути інформацію про її начальника.");
            showKafedrasList();
            int kafedraId = 0;
            boolean success = false;
            while (!success) {
                try {
                    System.out.print("Ваш вибір: ");
                    kafedraId = Integer.parseInt(reader.readLine());
                    if (sqLiteJDBCDriverManager.getAllIdFromTable(fileName, "Kafedra").contains(kafedraId)) {
                        success = true;
                    } else {
                        throw new NumberFormatException();
                    }
                    success = true;
                } catch (NumberFormatException numberFormatException) {
                    System.out.println("Введіть коректний ID кафедри!");
                }
            }
            Teacher teacher = sqLiteJDBCDriverManager.getChiefTeacherInfo(fileName, kafedraId);
            if (teacher.getName() != null) {
                System.out.println("\nІнформація про начальника кафедри:");
                System.out.println(teacher);
            } else {
                System.out.println("\nДля обраної кафедри ще не назначено начальника!");
            }
        }
    }

    private static void addTeacherToKafedra() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Kafedra> kafedraArrayList = sqLiteJDBCDriverManager.getKafedraArrayList(fileName);
        ArrayList<Teacher> teacherArrayList = sqLiteJDBCDriverManager.getTeachersList(fileName);
        if (kafedraArrayList.size() == 0 || teacherArrayList.size() == 0) {
            System.out.println("\nВ університеті недостатня кількість викладачів або кафедр. Додайте інформацію " +
                    "і повторіть спробу пізніше.");
            mainMenu();
        } else {
            System.out.println("\nВиберіть ID кафедри, до кої хочете додати викладача.");
            showKafedrasList();
            int kafedraId = 0;
            int teacherId = 0;
            boolean success = false;
            while (!success) {
                try {
                    System.out.print("Ваш вибір: ");
                    kafedraId = Integer.parseInt(reader.readLine());
                    if (!sqLiteJDBCDriverManager.getAllIdFromTable(fileName, "Kafedra").contains(kafedraId)) {
                        throw new NumberFormatException();
                    }
                    success = true;
                } catch (NumberFormatException numberFormatException) {
                    System.out.println("Введіть коректний ID кафедри!");
                }
            }
            success = false;
            System.out.println("\nВиберіть ID викладача, якого хочете додати до обраної кафедри.");
            showTeachersList();
            while (!success) {
                try {
                    System.out.print("Ваш вибір: ");
                    teacherId = Integer.parseInt(reader.readLine());
                    if (!sqLiteJDBCDriverManager.getAllIdFromTable(fileName, "Teacher").contains(teacherId)) {
                        throw new NumberFormatException();
                    }
                    success = true;
                } catch (NumberFormatException numberFormatException) {
                    System.out.println("Введіть коректний ID викладача!");
                }
            }
            sqLiteJDBCDriverManager.addTeacherToKafedra(fileName, kafedraId, teacherId);
            System.out.println("Викладача успішно додано до кафедри.");
        }
    }

    private static void showKafedraStatistic() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        boolean success = false;
        int kafedraId = 0;
        System.out.println("\nВиберіть ID кафедри, про яку хочете переглянути статистику.");
        showKafedrasList();
        while (!success) {
            try {
                System.out.print("Ваш вибір: ");
                kafedraId = Integer.parseInt(reader.readLine());
                if (!sqLiteJDBCDriverManager.getAllIdFromTable(fileName, "Kafedra").contains(kafedraId)) {
                    throw new NumberFormatException();
                }
                success = true;
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Введіть коректний ID кафедри!");
            }
        }
        HashMap<String, Integer> kafedraStatistic = sqLiteJDBCDriverManager.getKafedraStatistic(fileName, kafedraId);
        System.out.println("\nСтатистика по кафедрі:");
        int professorsCount = 0;
        int docentsCount = 0;
        int assistantsCount = 0;
        for (Map.Entry<String, Integer> kafedraStatisticEntry : kafedraStatistic.entrySet()) {
            Object key = ((Map.Entry) kafedraStatisticEntry).getKey();
            if (key.toString().equals("Асистент")) {
                professorsCount++;
            }
            if (key.toString().equals("Доцент")) {
                docentsCount++;
            }
            if (key.toString().equals("Професор")) {
                assistantsCount++;
            }
        }
        System.out.println("Професори: " + professorsCount);
        System.out.println("Доценти: " + docentsCount);
        System.out.println("Асистенти: " + assistantsCount);
    }

    private static void showAverageSalaryByKafedra() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        boolean success = false;
        int kafedraId = 0;
        System.out.println("\nВиберіть ID кафедри, де хочете переглянути середню зарплату по кафедрі.");
        showKafedrasList();
        while (!success) {
            try {
                System.out.print("Ваш вибір: ");
                kafedraId = Integer.parseInt(reader.readLine());
                if (!sqLiteJDBCDriverManager.getAllIdFromTable(fileName, "Kafedra").contains(kafedraId)) {
                    throw new NumberFormatException();
                }
                success = true;
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Введіть коректний ID кафедри!");
            }
        }
        String average_salary = sqLiteJDBCDriverManager.getAverageSalaryByKafedra(fileName, kafedraId)
                .split(" ")[0];
        String kafedra_name = sqLiteJDBCDriverManager.getAverageSalaryByKafedra(fileName, kafedraId)
                .split(" ")[1];
        System.out.println("\nСередня зарплата для кафедри " + kafedra_name + ": " + average_salary);
    }

    private static void showTeachersCountOnKafedra() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        boolean success = false;
        int kafedraId = 0;
        System.out.println("\nВиберіть ID кафедри, де хочете переглянути кількість працівників.");
        showKafedrasList();
        while (!success) {
            try {
                System.out.print("Ваш вибір: ");
                kafedraId = Integer.parseInt(reader.readLine());
                if (!sqLiteJDBCDriverManager.getAllIdFromTable(fileName, "Kafedra").contains(kafedraId)) {
                    throw new NumberFormatException();
                }
                success = true;
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Введіть коректний ID кафедри!");
            }
        }
        String teachers_count = sqLiteJDBCDriverManager.getTeachersCountByKafedra(fileName, kafedraId)
                .split(" ")[0];
        String kafedra_name = sqLiteJDBCDriverManager.getTeachersCountByKafedra(fileName, kafedraId)
                .split(" ")[1];
        System.out.println("\nКількість працівників на кафедрі " + kafedra_name + ": " + teachers_count);
    }

    private static void globalSearch() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("\nВведіть слово/частину слова для пошуку інформації: ");
        String searchString = reader.readLine();
        ArrayList<Teacher> teachersArrayList = sqLiteJDBCDriverManager.globalTeacherSearching(fileName, searchString);
        ArrayList<Kafedra> kafedraArrayList = sqLiteJDBCDriverManager.globalKafedraSearching(fileName, searchString);
        if (teachersArrayList.size() == 0) {
            System.out.println("\nІнформація по викладачам не знайдена.");
        } else {
            System.out.println("\nРезультати пошуку (викладачі):");
            for (Teacher teacher : teachersArrayList) {
                System.out.println("Викладач:");
                System.out.println(teacher);
            }
        }
        if (kafedraArrayList.size() == 0) {
            System.out.println("\nІнформація по кафедрам не знайдена.");
        } else {
            System.out.println("\nРезультати пошуку (кафедри):");
            for (Kafedra kafedra : kafedraArrayList) {
                System.out.println("Кафедра:");
                System.out.println(kafedra);
            }
        }
    }

    private static void exit() {
        System.out.println("До наступної зустрічі!");
        System.exit(0);
    }
}