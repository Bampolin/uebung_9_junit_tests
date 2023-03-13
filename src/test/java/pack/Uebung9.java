package pack;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class Uebung9 {
    private Connection connection;




    @Before
    public void setup() throws SQLException {
        String url = "jdbc:sqlserver://10.128.6.7;ddatabase=db4b_15;username=sa;encrypt=false";
        String username = "sa";
        String password = "";
        connection = DriverManager.getConnection(url, username, password);
    }

    @After
    public void teardown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }



    @Test
    public void testDelL() throws SQLException {
        // Insert a new row in L table
        CallableStatement insertLStmt = connection.prepareCall("{CALL INSERT_L(?, ?, ?, ?)}");
        insertLStmt.setString(1, "L6");
        insertLStmt.setString(2, "Müller");
        insertLStmt.setInt(3, 15);
        insertLStmt.setString(4, "Berlin");
        insertLStmt.executeUpdate();

        // Delete the row from L table and related rows from LT table
        CallableStatement delLStmt = connection.prepareCall("{CALL DEL_L(?, ?)}");
        delLStmt.setString(1, "L6");
        delLStmt.registerOutParameter(2, Types.INTEGER);
        delLStmt.executeUpdate();
        int numDeletedLT = delLStmt.getInt(2);

        // Check if the row is deleted from L table and related rows are deleted from LT table
        CallableStatement selectLStmt = connection.prepareCall("SELECT COUNT(*) FROM L WHERE LNR = ?");
        selectLStmt.setString(1, "L6");
        boolean lRowExists = selectLStmt.executeQuery().next();

        CallableStatement selectLTStmt = connection.prepareCall("SELECT COUNT(*) FROM LT WHERE LNR = ?");
        selectLTStmt.setString(1, "L6");
        boolean ltRowsExist = selectLTStmt.executeQuery().next();

        assertTrue("L row should be deleted", !lRowExists);
        assertEquals("Number of LT rows deleted should match", 1, numDeletedLT);
        assertTrue("LT rows should be deleted", !ltRowsExist);
    }









}