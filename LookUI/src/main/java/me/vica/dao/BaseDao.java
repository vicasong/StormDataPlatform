package me.vica.dao;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Vica-tony on 12/9/2016.
 */
public class BaseDao {
    protected Connection connection = null;
    protected PreparedStatement statement = null;
    protected static Logger logger = Logger.getLogger(BaseDao.class);

    protected void getConnection() {
        connection = DBConnectionPool.getCurrentPool().getConnection();
        try {
            connection.createStatement()
                    .execute("set @@sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';");
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void close(){
        try {
            if (connection != null) {
                connection.close();
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        statement = null;
        logger = null;
    }
}
