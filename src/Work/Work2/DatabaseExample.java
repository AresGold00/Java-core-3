package Work.Work2;

import java.sql.*;

public class DatabaseExample {
    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement ps;
    private static String insertStatement = "insert into students (name, score) values (?, ?);";
    private static final String EXAMPLE_CALL = "{call do_something_prc(?,?,?)}";
    private static final String DB_CONNECTION_STRING = "jdbc:sqlite:db/example.db";
    private static final String CREATE_REQUEST = "create table if not exists students " +
            "(id integer primary key autoincrement, name text, score integer);";
    private static final String DROP_REQUEST = "drop table if exists students;";
    private static final String SIMPLE_INSERT_REQUEST =
            "insert into students (name, score) values ('Vasya Pupkin', 80), ('Kolya Morzhov', 90), ('Vitaly Petrov', 100);";

    public static void main(String[] args)  {
        try {
            connect();
            createTable();
//            simpleInsertExample();
//            dropExample();
//            simpleUpdateExample();
//            notReallyCorrectInsertExample("Radamir", 100);
//            preparedStatementExample("Pavel", 100);
//            simpleReadExample();
//            massInsertExample();
//            massInsertTransactionalExample();
            massInsertBatchExample();
//            var callableStatement = connection.prepareCall(EXAMPLE_CALL);
//            callableStatement.setString(1, "safdvad");
//            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    private static void massInsertBatchExample()  throws SQLException  {
        var start = System.currentTimeMillis();
        connection.setAutoCommit(false);
        for (int i = 0; i < 5000; i++) {
            ps.setInt(2, i);
            ps.setString(1, "Student #" + i + 1);
            ps.addBatch();
        }
        ps.executeBatch();
        connection.setAutoCommit(true);
        System.out.println(System.currentTimeMillis() - start);
    }

    private static void massInsertTransactionalExample()  throws SQLException  {
        var start = System.currentTimeMillis();
        connection.setAutoCommit(false);
        for (int i = 0; i < 5000; i++) {
            preparedStatementExample("Student #" + i + 1, i);
        }
//        connection.commit(); //Делает коммит, не включает автокоммит
        connection.setAutoCommit(true);
        System.out.println(System.currentTimeMillis() - start);
    }

    private static void massInsertExample()  throws SQLException  {
        var start = System.currentTimeMillis();
        for (int i = 0; i < 5000; i++) {
            preparedStatementExample("Student #" + i + 1, i);
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    private static void preparedStatementExample(String name, int score)  throws SQLException  {
        ps.setString(1, name);
        ps.setInt(2, score);
        ps.executeUpdate();
    }

    private static void notReallyCorrectInsertExample(String name, int score)  throws SQLException  {
        var count = statement.executeUpdate("insert into students (name, score) values (\'" + name + "\', " + score + ");");
        System.out.printf("Updated %d rows\n", count);
    }

    private static void simpleReadExample()  throws SQLException  {
        try (var resultSet = statement.executeQuery("select * from students order by name desc;")) {
            while (resultSet.next()) {
                System.out.printf("Student id: %d, name: %s, score: %d\n",
                        resultSet.getInt(1),
                        resultSet.getString("name"),
                        resultSet.getInt("score"));
            }
        }
    }

    private static void simpleUpdateExample()  throws SQLException  {
        var count = statement.executeUpdate("update students set name = 'Alex' where score > 90;");
        System.out.printf("Updated %d rows\n", count);
    }

    private static void dropExample()  throws SQLException  {
        statement.execute(DROP_REQUEST);
    }

    private static void simpleInsertExample()  throws SQLException  {
        var count = statement.executeUpdate(SIMPLE_INSERT_REQUEST);
        System.out.printf("Inserted %d rows\n", count);
    }

    private static void createTable() throws SQLException {
        statement.execute(CREATE_REQUEST);
    }

    private static void connect() throws SQLException   {
//        Раньше требовалось проинициализировать драйвер
//        try {
//            Class.forName("org.sqlite.JDBC");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        connection = DriverManager.getConnection(DB_CONNECTION_STRING);
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