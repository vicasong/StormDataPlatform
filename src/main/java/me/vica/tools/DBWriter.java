package me.vica.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Mysql DB LogObject Writer
 * Created by Vica-tony on 10/31/2016.
 */
public class DBWriter {

    private static Connection connection = null;
    private static final Object local = new Object();
    private static PreparedStatement statement = null;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Init The Mysql Connection For Next Operation
     *
     * @param url      Connection URL
     * @param name     User Name
     * @param password Password
     * @param tabName  The Table Name
     */
    public static PreparedStatement initConnection(String url, String name, String password, String tabName) {
        if (connection == null) {
            synchronized (local) {
                try {
                    if (connection == null) {
                        connection = DriverManager.getConnection(url, name, password);
                    }
                    statement = connection.prepareStatement("insert into " + tabName + " values(?,?,?,?,?,?,?,?,?);");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return statement;
    }

//    /**
//     * Write AccessLog Record Into DB
//     *
//     * @param log The Object Of AccessLog To Write
//     * @return 1 for Success and 0 for failed
//     */
//    public static int write(AccessLog log) {
//        PreparedStatement statement = local.get();
//        if (statement != null) {
//            try {
//                statement.clearParameters();
//                statement.setObject(1, log.getTime_local());
//                statement.setObject(2, log.getRemote_addr());
//                statement.setObject(3, log.getRemote_user());
//                statement.setObject(4, log.getBody_bytes_sent());
//                statement.setObject(5, log.getRequest_time());
//                statement.setObject(6, log.getStatus());
//                statement.setObject(7, log.getRequest());
//                statement.setObject(8, log.getRequest_method());
//                statement.setObject(9, log.getHttp_referrer());
//                statement.setObject(10, log.getHttp_x_forwarded_for());
//                statement.setObject(11, log.getHttp_user_agent());
//                return statement.executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        } else {
//            throw new NullPointerException("Please init connection first.");
//        }
//        return 0;
//    }

    /**
     * Write Data Into DB
     * @param time         The DateTime String Of Record Begin
     * @param resource      The Resource Of Record
     * @param allPv         This Resource's Page View Num In This Time Duration
     * @param allUv         This Resource's User View Num In This Time Duration
     * @param pv3xx         This Resource's 3xx Status PageView Num In This Time Duration
     * @param pv4xx         This Resource's 4xx Status PageView Num In This Time Duration
     * @param otherPv       This Resource's Other Status PageView Num In This Time Duration
     * @param maxMethod     This Resource's MaxTimes Method In This Time Duration
     * @param maxUser       This Resource's MaxTimes User In This Time Duration
     * @return              The Data Num Be Write
     */
    public static int write(String url, String name, String password, String tabName,
                            String time, String resource, int allPv, int allUv, int pv3xx, int pv4xx, int otherPv,
                            String maxMethod, String maxUser){
        PreparedStatement statement = DBWriter.initConnection(url,name,password,tabName);
        if (statement != null) {
            try {
                statement.clearParameters();
                statement.setObject(1, time);
                statement.setObject(2, resource);
                statement.setObject(3, allPv);
                statement.setObject(4, allUv);
                statement.setObject(5, pv3xx);
                statement.setObject(6, pv4xx);
                statement.setObject(7, otherPv);
                statement.setObject(8, maxMethod);
                statement.setObject(9, maxUser);
                return statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            throw new NullPointerException("Please init connection first.");
        }
        return 0;
    }

//    /**
//     * Write Custom
//     *
//     * @param sql  The Sql To Execute
//     * @param args The Parameters Of Sql
//     * @return row count effected
//     */
//    public static int write(String sql, Object... args) {
//        PreparedStatement statement = local.get();
//        if (statement != null) {
//            try {
//                statement = connection.prepareStatement(sql);
//                if (args != null)
//                    for (int i = 0; i < args.length; i++) {
//                        statement.setObject(i, args[i]);
//                    }
//                return statement.executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        } else {
//            throw new NullPointerException("Please init connection first.");
//        }
//        return 0;
//    }

    /**
     * Close The Connection
     */
    public synchronized static void close(){
        PreparedStatement statement = DBWriter.statement;
        try {
            if (statement != null) {
                statement.close();
            }
            if(connection != null){
                if(!connection.isClosed()){
                    connection.close();
                }
                connection = null;
            }
        }catch (Exception e){
            //Nothing
        }
    }


}
