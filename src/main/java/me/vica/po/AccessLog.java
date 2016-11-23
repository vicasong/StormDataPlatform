package me.vica.po;

import me.vica.tools.LogParser;

import java.util.Date;

/**
 * The AccessLog Object Entity
 * Created by Vica-tony on 10/31/2016.
 */
public class AccessLog {
    // The Json Template Like This Blow
    //{
    // "time_local":"30/Sep/2016:00:10:38 +0800",
    // "remote_addr":"103.226.133.67",
    // "remote_user":"-",
    // "request":"GET /favicon.ico HTTP/1.1",
    // "status":"304",
    // "body_bytes_sent":"0",
    // "http_referer":"-",
    // "http_user_agent":"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0",
    // "http_x_forwarded_for":"-"
    // }

    private Date time_local;
    private String remote_addr;
    private String remote_user;
    private String request;
    private int status;
    private long body_bytes_sent;
    private String http_referer;
    private String http_user_agent;
    private String http_x_forwarded_for;

    private String request_method;
    private String request_version;
    private String request_url;

    public Date getTime_local() {
        return time_local;
    }

    public void setTime_local(Date time_local) {
        this.time_local = time_local;
    }

    public String getRemote_addr() {
        return remote_addr;
    }

    public void setRemote_addr(String remote_addr) {
        this.remote_addr = remote_addr;
    }

    public String getRemote_user() {
        return remote_user;
    }

    public void setRemote_user(String remote_user) {
        this.remote_user = remote_user;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getBody_bytes_sent() {
        return body_bytes_sent;
    }

    public void setBody_bytes_sent(long body_bytes_sent) {
        this.body_bytes_sent = body_bytes_sent;
    }

    public String getHttp_referer() {
        return http_referer;
    }

    public void setHttp_referer(String http_referer) {
        this.http_referer = http_referer;
    }

    public String getHttp_user_agent() {
        return http_user_agent;
    }

    public void setHttp_user_agent(String http_user_agent) {
        this.http_user_agent = http_user_agent;
    }

    public String getHttp_x_forwarded_for() {
        return http_x_forwarded_for;
    }

    public void setHttp_x_forwarded_for(String http_x_forwarded_for) {
        this.http_x_forwarded_for = http_x_forwarded_for;
    }

    public String getRequest_method() {
        return request_method;
    }

    public void setRequest_method(String request_method) {
        this.request_method = request_method;
    }

    public String getRequest_version() {
        return request_version;
    }

    public void setRequest_version(String request_version) {
        this.request_version = request_version;
    }

    public String getRequest_url() {
        return request_url;
    }

    public void setRequest_url(String request_url) {
        this.request_url = request_url;
    }

    @Override
    public String toString() {
        return "AccessLog{" +
                "time_local=" + time_local +
                ", remote_addr='" + remote_addr + '\'' +
                ", remote_user='" + remote_user + '\'' +
                ", request='" + request + '\'' +
                ", status=" + status +
                ", body_bytes_sent=" + body_bytes_sent +
                ", http_referer='" + http_referer + '\'' +
                ", http_user_agent='" + http_user_agent + '\'' +
                ", http_x_forwarded_for='" + http_x_forwarded_for + '\'' +
                ", request_method='" + request_method + '\'' +
                ", request_version='" + request_version + '\'' +
                ", request_url='" + request_url + '\'' +
                '}';
    }

//    public static void main(String[] args) {
//        String json = "{\n" +
//                "\"time_local\":\"30/Sep/2016:00:10:38 +0800\"," +
//                "\"remote_addr\":\"103.226.133.67\"," +
//                "\"remote_user\":\"-\"," +
//                "\"request\":\"GET /favicon.ico HTTP/1.1\"," +
//                "\"status\":\"304\"," +
//                "\"body_bytes_sent\":\"0\"," +
//                "\"http_referer\":\"-\"," +
//                "\"http_user_agent\":\"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0\"," +
//                "\"http_x_forwarded_for\":\"-\"" +
//                "}";
//        AccessLog entity = LogParser.parse(json);
//        String[] fields = entity.getRequest().split(" ");
//        if(fields.length==3){
//            entity.setRequest_method(fields[0].toUpperCase());
//            entity.setRequest_url(fields[1]);
//            entity.setRequest_version(fields[2]);
//        }
//        System.out.println(entity);
//    }

}
