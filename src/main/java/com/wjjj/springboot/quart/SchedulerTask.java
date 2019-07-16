package com.wjjj.springboot.quart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    @Resource
    private RedisTemplate<String, Object> redisCacheTemplate;

    @Scheduled(cron = "*/6 * * * * ?")
    private void process() {

//        String oraclesql = "select * from WX_ALI_CONFIG";
//        List<Map<String, Object>> resultList = oracleJdbcTemplate.queryForList(oraclesql);
//        //循环打印结果
//        resultList.stream().forEach(System.out::println);
//        log.info("#########  oracle数据库查询  #########");

        String serversql = "select * from WX_ALI_CONFIG";
        List<Map<String, Object>> mapList = mssqlJdbcTemplate.queryForList(serversql);
        //循环打印结果
        //mapList.stream().forEach(System.out::println);
        //log.info("#########  sqlserver数据库查询  #########");

        //查询redis中的list
        String key = "wxalipay";
        final List<Map<String, String>> redisList =
                (List<Map<String, String>>) redisCacheTemplate.opsForValue().get(key);

        //1.循环新查询的mapList数据 与redis中的list轮询比较
        mapList.stream().forEach(oraleMap -> {
            if (redisList == null || redisList.size() == 0) {
                //将每次定时查询到的数据放入redis缓存
                redisCacheTemplate.opsForValue().set(key, mapList);
            } else {
                //如果map中的存在
                String jgdm = (String) oraleMap.get("jgdm");
                redisList.stream().forEach(redisMap -> {
                    if (!redisMap.containsValue(jgdm)) {
                        log.info("新增数据为：[" + redisMap + "]数据");
                    }
                });
            }

        });

        //将每次定时查询到的数据放入redis缓存
        redisCacheTemplate.opsForValue().set(key, mapList);

        log.info("[List缓存结果] - [{}]", redisList);


    }
}