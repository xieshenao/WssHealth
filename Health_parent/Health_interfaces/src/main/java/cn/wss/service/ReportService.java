package cn.wss.service;

import java.util.Map;

public interface ReportService {
    //查询总数据封装map
    public Map<String,Object>getBusinessReport() throws Exception;
}
