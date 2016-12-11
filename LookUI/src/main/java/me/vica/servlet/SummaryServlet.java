package me.vica.servlet;

import me.vica.dto.ChatData;
import me.vica.service.SummaryService;
import me.vica.tools.ParseTool;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Vica-tony on 12/9/2016.
 */
@WebServlet(name = "Summary", urlPatterns = "/summary/*")
public class SummaryServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String where = request.getPathInfo();
        Calendar calendar = Calendar.getInstance();
        if(where != null) { //need day, count by hour, like /summary/
            if (where.startsWith("/year")) { //need year, count by month, like /summary/year/2016
                String tmp = where.replace("/year", "");
                monthSummary(response, ParseTool.toInt(tmp.substring(tmp.indexOf("/") + 1).replace("/", ""), calendar.get(Calendar.YEAR)));
            } else if (where.startsWith("/month")) { //need month, count by day, like /summary/month/2016/10
                String tmp = where.replace("/month", "");
                String o = tmp.substring(tmp.indexOf("/") + 1);
                String y = o.substring(0, o.indexOf("/"));
                String m = o.substring(o.indexOf("/") + 1).replace("/", "");
                daySummary(response, ParseTool.toInt(y, calendar.get(Calendar.YEAR)), ParseTool.toInt(m, calendar.get(Calendar.MONTH)));
            } else if (where.startsWith("/day")) { //need day, count by hour, like /summary/day/2016/10/30
                String tmp = where.replace("/day", "");
                String o = tmp.substring(tmp.indexOf("/") + 1);
                String y = o.substring(0, o.indexOf("/"));
                String p = o.substring(o.indexOf("/") + 1);
                String m = p.substring(0, p.indexOf("/"));
                String d = p.substring(p.indexOf("/")+1).replace("/","");
                hourSummary(response, ParseTool.toInt(y, calendar.get(Calendar.YEAR)), ParseTool.toInt(m, calendar.get(Calendar.MONTH)), ParseTool.toInt(d, calendar.get(Calendar.DATE)));
            } else {
                hourSummary(response, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            }
        }else {
            hourSummary(response, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }


    private void hourSummary(HttpServletResponse response, int year, int month, int day){
        List<ChatData> source = SummaryService.getService().queryPerHour(year,month,day);
        try {
            json(response, source);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void daySummary(HttpServletResponse response, int year, int month){
        List<ChatData> source = SummaryService.getService().queryPerDay(year,month);
        try {
            json(response, source);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void monthSummary(HttpServletResponse response, int year){
        List<ChatData> source = SummaryService.getService().queryPerMonth(year);
        try {
            json(response, source);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void json(HttpServletResponse response, List<ChatData> source) throws IOException {
        if(source == null || source.size() < 1){
            response.getWriter().write("{\"total\":0}");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"total\":"); sb.append(source.size());
        sb.append(",");
        sb.append("\"msg\":");
        sb.append("[");
        for(ChatData data : source){
            sb.append("{");
            sb.append("\"name\":\""); sb.append(data.getName()); sb.append("\",");
            sb.append("\"data\":[");
            for(int v : data.getData()){
                sb.append(v);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append("],");
            sb.append("\"categories\":[");
            for(String c : data.getCategories()){
                sb.append("\"");
                sb.append(c);
                sb.append("\",");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append("],");
            sb.append("\"extra\":[");
            for(String c : data.getOthers()){
                sb.append("\"");
                sb.append(c);
                sb.append("\",");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append("]");
            sb.append("},");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        sb.append("}");
        response.getWriter().write(sb.toString());
    }

}
