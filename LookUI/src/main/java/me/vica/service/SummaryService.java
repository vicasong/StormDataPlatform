package me.vica.service;

import me.vica.dao.SummaryDao;
import me.vica.dto.ChatData;
import me.vica.dto.Summary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vica-tony on 12/9/2016.
 */
public class SummaryService {

    private static SummaryDao summaryDao = null;
    private static SummaryService service = null;

    static {
        service = new SummaryService();
        summaryDao = new SummaryDao();
    }
    private SummaryService(){
    }

    public static SummaryService getService(){
        return service;
    }

    public List<ChatData> queryPerHour(int year, int month, int day){
        List<Summary> summaries = summaryDao.selectByDay(year,month,day);
        if(summaries!=null){
            int length = 24;
            String[] categories = new String[length];
            for(int i=0; i<length; i++){
                categories[i] = String.format("%02d", i);
            }
            return calcData(summaries, length, categories, true);
        }
        return null;
    }

    public List<ChatData> queryPerDay(int year, int month){
        List<Summary> summaries = summaryDao.selectByMonth(year,month);
        if(summaries!=null){
            int length = getMaxDays(year, month);
            String[] categories = new String[length];
            for(int i=0; i<length; i++){
                categories[i] = String.format("%d", i + 1);
            }
            return calcData(summaries, length, categories, false);
        }
        return null;
    }

    public List<ChatData> queryPerMonth(int year){
        List<Summary> summaries = summaryDao.selectByYear(year);
        if(summaries!=null){
            int length = 12;
            String[] categories = new String[]{
                    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
            };
            return calcData(summaries, length, categories, false);
        }
        return null;
    }


    private List<ChatData> calcData(List<Summary> summaries, int length, String[] categories, boolean zeroIndex){
        List<ChatData> list = new ArrayList<>();
        int[] pv = new int[length];
        int[] uv = new int[length];
        String[] othersForPv = new String[length];
        String[] othersForUv = new String[length];
        for(Summary summary: summaries){
            int index = summary.getTime();
            if(!zeroIndex) index-=1;
            pv[index] = summary.getPv();
            uv[index] = summary.getUv();
            othersForPv[index] = summary.getResource();
            othersForUv[index] = summary.getMax();
        }
        list.add(new ChatData("PV", pv, othersForPv, categories));
        list.add(new ChatData("UV", uv, othersForUv, categories));
        return list;
    }

    private int getMaxDays(int year, int month){
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                if((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)){
                    return 29;
                }
                return 28;
            default:
                return 30;
        }

    }

}
