package com.wjjj.springboot.quart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *@author WJJJ
 *@createTime 2019/7/15 23:52
 *@description 定时任务调度
 */

@Slf4j
@Component
public class SchedulerTask {

    @Autowired
    @Qualifier("oracleJdbcTemplate")
    private JdbcTemplate oracleJdbcTemplate;

    @Autowired
    @Qualifier("mssqlJdbcTemplate")
    private JdbcTemplate mssqlJdbcTemplate;

    private List<Map<String, String>> mapList = new ArrayList<>();

    @Scheduled(cron = "*/6 * * * * ?")
    private void process() {

        String oraclesql = "select * from WX_ALI_CONFIG";
        List<Map<String, Object>> resultList = oracleJdbcTemplate.queryForList(oraclesql);
        //循环打印结果
        resultList.stream().forEach(System.out::println);
        log.info("#########  oracle数据库查询  #########");

        String serversql = "select * from WXANDALIPAYCONFIG";
        List<Map<String, Object>> mapList = mssqlJdbcTemplate.queryForList(serversql);
        //循环打印结果
        mapList.stream().forEach(System.out::println);
        log.info("#########  server  #########");

    }
}