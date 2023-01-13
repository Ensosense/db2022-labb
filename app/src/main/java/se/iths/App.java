package se.iths;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class App {


  private static final Collection<Student> students = new ArrayList<>();
  private static final String URL = "jdbc:mysql://localhost:3306/iths";
  private static final String USERNAME = "iths";
  private static final String PASSWORD = "iths";
  private static final String SELECT_FROM_STUDENT = "select * from Student";
  private static final String INSERT_STUDENT = "insert into Student (Name) values (?)";

  private static final String STUDENT_ID = "StudentId";
  private static final String STUDENT_NAME = "Name";
  private static final String TEST_NAME = "Otto Kraft";


  static Connection con = null;
  static ResultSet rs = null;

  public static void main(String[] args) throws Exception {

    setUp();

    // getStudentData();
    //  insertIntoStudent("Kalle Nilsson");
    //  updateRowInStudentTable("Kalle Nilsson", TEST_NAME);
    //  deleteRowInStudentTable("Otto Kraft");
    // findRowInStudentTable(1L);

    conClose();

  }

  public static void setUp() throws Exception {
    con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
  }

  //READ
  private static void getStudentData() {
    try {
      rs = con.createStatement().executeQuery(SELECT_FROM_STUDENT);
      while (rs.next()) {
        long id = rs.getLong(STUDENT_ID);
        String name = rs.getString(STUDENT_NAME);
        students.add(new Student(id, name));
      }
    } catch (SQLException e) {
      System.err.println(String.format("Fel vid läsning av databas %s", e.toString()));
    } finally {
      try {
        rs.close();
      } catch (Exception ignore) {
      }
    }

    for (Student student : students) {
      System.out.println(student);
    }
  }

  //CREATE
  private static void insertIntoStudent(String name) {

    try {
      PreparedStatement preparedStatement = con.prepareStatement(INSERT_STUDENT);
      preparedStatement.setString(1, name);
      preparedStatement.execute();


    } catch (SQLException e) {
      System.err.println(String.format("Fel vid läsning av databas %s", e.toString()));
    }

  }

  // UPDATE
  private static void updateRowInStudentTable(String oldName, String newName) {

    try {
      PreparedStatement preparedStatement = con.prepareStatement("UPDATE Student Set Name = ? WHERE Name = ?");
      preparedStatement.setString(1, newName);
      preparedStatement.setString(2, oldName);
      preparedStatement.execute();
    } catch (SQLException e) {
      System.err.println(String.format("Fel vid läsning av databas %s", e.toString()));
    }

  }

  //DELETE
  private static void deleteRowInStudentTable(String name) {
    try {
      PreparedStatement stmt = con.prepareStatement("Delete from Student where name = ?");
      stmt.setString(1, name);
      stmt.execute();
    } catch (SQLException e) {
      System.err.println(String.format("Fel vid läsning av databas %s", e.toString()));
    }
  }

  private static void findRowInStudentTable(Long actualIdAfterInsert) {

    try {
      PreparedStatement stmt = con.prepareStatement("SELECT Id, Name FROM Student WHERE Id = ?");
      stmt.setLong(1, actualIdAfterInsert);
      ResultSet rs = stmt.executeQuery();
      System.out.println(rs.toString());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

  public static void conClose() throws Exception {
    con.close();
  }
}