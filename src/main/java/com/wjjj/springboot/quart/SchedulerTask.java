package com.wjjj.springboot.quart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    @Resource
    private JdbcTemplate jdbcTemplate;

    private List<Map<String, String>> mapList = new ArrayList<>();

    @Scheduled(cron = "*/6 * * * * ?")
    private void process() {

        String sql = "select * from t_dict";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);

        //循环打印结果
        resultList.stream().forEach(System.out::println);

        log.info("#########  info  #########");

    }
}