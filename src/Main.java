import java.sql.*;

class Main {
    public static void main(String args[]) {
        try {
            // step1 load the driver class
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // step2 create the connection object
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe", "system", "system");

            // step3 create the statement object
            Statement stmt = con.createStatement();

            // step4 execute query
            String query = "SELECT * FROM TRANSACTIONS";
            ResultSet rs = stmt.executeQuery(query);
            String query2 = "SELECT * FROM ACCOUNT";
            ResultSet rs2 = stmt.executeQuery(query);
            float[] balance = new float[100];
            float[] transactions = new float[100];
            for(int i = 0; i < 100; i++) {
                balance[i] = 0;
            }
            while(rs.next()) {
                int id = rs.getInt("account_id");
                int type = rs.getInt("type");
                float amount = rs.getFloat("amount");
                if (type == 1) {
                    balance[id-1] -= amount;
                    transactions[id-1] += amount;
                }
                else {
                    balance[id-1] += amount;
                    transactions[id-1] += amount;
                }
            }
            int cip=0, vip=0, op=0, nc=0;
            for (int i = 0; i < 100; i++) {
                if (balance[i] > 1000000 && transactions[i] > 500000) {
                    cip++;
                }
                else if (balance[i] > 500000 && balance[i] < 900000 && transactions[i] > 2500000 && transactions[i] < 4500000) {
                    vip++;
                }
                else if (balance[i] < 100000 && transactions[i] < 1000000) {
                    op++;
                }
                else {
                    nc++;
                }
            }
            System.out.printf("%d %d %d %d", cip, vip, op, nc);



            String insertSql = "INSERT into TRANSACTIONS values(?,?,?,?,?)"
            try (PreparedStatement pstmt = con.prepareStatement(insertSql)) {
                pstmt.setString(1, "10000");
                pstmt.setString(2, "2001-11-9 12:45:45");
                pstmt.setString(3, "12");
                pstmt.setString(4, "10000");
                pstmt.setString(5, "1");
                pstmt.executeUpdate();

                pstmt.setString(1, "10001");
                pstmt.setString(2, "2001-9-11 12:45:45");
                pstmt.setString(3, "12");
                pstmt.setString(4, "10000");
                pstmt.setString(5, "0");
                pstmt.executeUpdate();
            }
            catch(SQLException e) {
                System.out.println(e);
            }

            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println(rsmd.getColumnCount());
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                System.out.println(rsmd.getColumnName(i));
                System.out.println (rsmd.getColumnTypeName(i));
            }
            rs.close();

            ResultSetMetaData rsmd2 = rs2.getMetaData();
            System.out.println(rsmd2.getColumnCount());
            for (int i = 1; i <= rsmd2.getColumnCount(); i++) {
                System.out.println(rsmd2.getColumnName(i));
                System.out.println (rsmd2.getColumnTypeName(i));
            }
            rs2.close();

            //Deleting the tables
            String deleteAccount = "DROP TABLE ACCOUNT";
            String deleteTransaction = "DROP TABLE TRANSACTIONS";
            stmt.executeQuery(deleteAccount);
            stmt.executeQuery(deleteTransaction);

            // step5 close the connection object
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
