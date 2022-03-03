package Work.Work2;

import java.sql.*;

public class PostgresExample {

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement ps;
    private static String insertStatement = "insert into students (name, score) values (?, ?);";
    private static String exampleCall = "{call do_something_prc(?,?,?)}";


    public static void main(String[] args)  {
        try {
            connect();
            createDB();
            simpleInsert();
            batchInsertExample();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    private static void batchInsertExample() throws SQLException {
        connection.setAutoCommit(false);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5000; i++) {
            ps.setString(1, "Student# " + i);
            ps.setInt(2, i);
            ps.addBatch();
        }
        ps.executeBatch();
        connection.setAutoCommit(true);
        System.out.println(System.currentTimeMillis() - start);
    }

    private static void massInsertExample() throws SQLException {
        connection.setAutoCommit(false);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5000; i++) {
            preparedInsert("Student# " + i, i);

        }

        connection.setAutoCommit(true);
        System.out.println(System.currentTimeMillis() - start);
    }

    private static void preparedInsert(String name, int score) throws SQLException {
        ps.setString(1, name);
        ps.setInt(2, score);
        ps.executeUpdate();
    }

    private static void notReallyCorrectInsert(String name, int score) throws SQLException {
        statement.executeUpdate("insert into students (name, score) values (\'" + name + "\', " + score + ");");
    }

    private static void simpleRead() throws SQLException{
        try (ResultSet resultSet = statement.executeQuery("select name, score from students;")) {
            while (resultSet.next()) {
                System.out.printf("Student: %s score %s\n", resultSet.getString(1), resultSet.getInt("score"));
            }
        }
    }

    private static void simpleUpdate() throws SQLException {
        statement.executeUpdate("update students set name = 'Ivan Ivanov' where score > 90");
    }

    private static void simpleDelete() throws SQLException {
        statement.executeUpdate("delete from students where id = 1 or name like '%Morz%' or score > 99;");
    }

    private static void simpleInsert() throws SQLException {
        int insertedRows = statement.executeUpdate("insert into students (name, score) values ('Vasya Pupkin', 80), ('Kolya Morzhov', 90), ('Vitaly Petrov', 100);");
        System.out.println(insertedRows);
    }

    private static void dropTable() throws SQLException {
        statement.execute("drop table if exists students;");
    }

    private static void createDB() throws SQLException {
        statement.execute("create schema gb_students;");
        statement.execute("create table if not exists students (id serial primary key, name text, score integer);");
    }

    private static void connect() throws SQLException {

        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=gb_students", "postgres", "admin");
        statement = connection.createStatement();
        ps = connection.prepareStatement(insertStatement);

    }

    private static void disconnect() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}