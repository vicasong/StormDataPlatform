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

    private Connection connection = null;
    private PreparedStatement statement = null;

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
    public void initConnection(String url, String name, String password, String tabName) {
        if (connection == null) {
            synchronized (this) {
                try {
                    if (connection == null) {
                        connection = DriverManager.getConnection(url, name, password);
                    }
                    statement = connection.prepareStatement("insert into " + tabName
                            + "(time,resource,page_view,user_view,pv_3,pv_4,pv_other,max_method,max_user) values(?,?,?,?,?,?,?,?,?);");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


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
    public synchronized int write(String time, String resource, int allPv, int allUv, int pv3xx, int pv4xx, int otherPv,
                            String maxMethod, String maxUser){
        if (statement != null) {
            try {
                statement.clearParameters();
                String temp = time.substring(0,time.lastIndexOf("-"))+" "
                        +time.substring(time.lastIndexOf("-")+1)+":00:00";
                statement.setObject(1, temp);
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

    /**
     * Close The Connection
     */
    public synchronized void close(){
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
