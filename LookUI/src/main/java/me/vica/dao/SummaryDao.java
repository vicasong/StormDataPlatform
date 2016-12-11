package me.vica.dao;

import me.vica.dto.Summary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vica-tony on 12/9/2016.
 */
public class SummaryDao extends BaseDao {

    private Summary assembleSingle(ResultSet set) throws SQLException {
        Summary summary = new Summary();
        summary.setTime(set.getInt("time"));
        summary.setPv(set.getInt("pv"));
        summary.setUv(set.getInt("uv"));
        summary.setResource(set.getString("resource"));
        summary.setMax(set.getString("max"));
        logger.debug("time="+set.getObject(1)+"; pv="+set.getObject(2)+
        "; uv"+set.getObject(3)+"; res="+set.getObject(4)+"; max="+set.getObject(5));
        return summary;
    }

    private List<Summary> assembleList(ResultSet set) throws SQLException {
        List<Summary> list = new ArrayList<>();
        while (set.next()) {
            list.add(assembleSingle(set));
        }
        logger.debug("list size = "+list.size());
        return list.size() >0 ? list : null;
    }


    public List<Summary> selectByDay(int year, int month, int day) {
        if (connection == null) {
            getConnection();
        }
        String sql = "SELECT hour(B.time)`time`, sum(B.page_view)pv, sum(B.user_view)uv, " +
                "(select resource from t_summary where time = B.time and resource <> '/' group by resource order by sum(page_view) desc limit 0,1)resource, " +
                "(select concat(max_user,' ',max_method) from t_summary where time = B.time group by max_user order by count(1) desc limit 0,1)`max` " +
                "FROM t_summary B " +
                "where year(B.time) = ? and month(B.time) = ? and day(B.time) = ? " +
                "group by hour(B.time);";
        try {
            statement = connection.prepareStatement(sql);
            statement.setObject(1, year);
            statement.setObject(2, month);
            statement.setObject(3, day);
            logger.debug("Year="+year+"; Month="+month+"; Day="+day);
            ResultSet set = statement.executeQuery();
            return set.wasNull() ? null : assembleList(set);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public List<Summary> selectByMonth(int year, int month) {
        if (connection == null) {
            getConnection();
        }
        String sql = "SELECT day(B.time)`time`, sum(B.page_view)pv, sum(B.user_view)uv, " +
                "(select resource from t_summary where date(time) = date(B.time) and resource <> '/' group by resource order by sum(page_view) desc limit 0,1)resource, " +
                "(select concat(max_user,' ',max_method) from t_summary where date(time) = date(B.time) group by max_user order by count(1) desc limit 0,1)`max` " +
                "FROM t_summary B " +
                "where year(B.time) = ? and month(B.time) = ? " +
                "group by day(B.time);";
        try {
            statement = connection.prepareStatement(sql);
            statement.setObject(1, year);
            statement.setObject(2, month);
            ResultSet set = statement.executeQuery();
            return set.wasNull() ? null : assembleList(set);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
    public List<Summary> selectByYear(int year) {
        if (connection == null) {
            getConnection();
        }
        String sql = "SELECT month(B.time)`time`, sum(B.page_view)pv, sum(B.user_view)uv, " +
                "(select resource from t_summary where year(time)=year(B.time) and month(time) = month(B.time) and resource <> '/' group by resource order by sum(page_view) desc limit 0,1)resource, " +
                "(select concat(max_user,' ',max_method) from t_summary where year(time)=year(B.time) and month(time) = month(B.time) group by max_user order by count(1) desc limit 0,1)`max` " +
                "FROM t_summary B " +
                "where year(B.time) = ? " +
                "group by month(B.time);";
        try {
            statement = connection.prepareStatement(sql);
            statement.setObject(1, year);
            ResultSet set = statement.executeQuery();
            return set.wasNull() ? null : assembleList(set);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
