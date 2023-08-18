package com.mc.controller;

import com.mc.common.ApiResponse;
import com.mc.entity.Replies;
import com.mc.entity.Replise;
import com.mc.service.RepliseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @className TestController
 * @description TODO
 * @author: LM理智梦
 * @date: 2023/8/18
 * @version: 1.0
 */
@Slf4j
@RestController
public class TestController {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private RepliseService repliseService;

    @Value("${bilibili.page}")
    private Integer page;

    @Value("${bilibili.oid}")
    private Integer oid;

    @GetMapping("/getTest")

    public void getTest() throws InterruptedException {
        log.info("{}接收到请求:", Thread.currentThread().getName());
        TimeUnit.HOURS.sleep(1);
    }


    @GetMapping("/getBilibiliMessage")
    public String getMessage() {
        Date startDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedStartDate = sdf.format(startDate);
        log.info("开始获取评论:{}", formattedStartDate);

        AtomicInteger totalCount = new AtomicInteger(0);

        ExecutorService executorService = Executors.newFixedThreadPool(10); // Adjust pool size as needed

        for (int i = 1; i <= page; i++) {
            final int currentPage = i;
            executorService.execute(() -> processPage(currentPage, totalCount));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            log.error("线程池执行被中断：{}", e.getMessage());
            Thread.currentThread().interrupt();
        }

        Date endDate = new Date(System.currentTimeMillis());
        String formattedEndDate = sdf.format(endDate);
        log.info("结束获取评论:{}", formattedEndDate);
        log.info("总用时:{}秒", (endDate.getTime() - startDate.getTime()) / 1000);

        return "获取" + totalCount.get() + "条数据成功";
    }

    private void processPage(int currentPage, AtomicInteger totalCount) {
        String apiUrl = "https://api.bilibili.com/x/v2/reply/" +
                "main?jsonp=jsonp&type=1&oid=" + oid + "&mode=3&plat=1&next=" + currentPage;

        ApiResponse response = restTemplate.getForObject(apiUrl, ApiResponse.class);
        List<Replies> replies = response.getData().getReplies();

        List<Replise> replises = new ArrayList<>();
        cycleSave(replies, replises, totalCount);

        repliseService.saveBatch(replises);

    }

    private void cycleSave(List<Replies> replies, List<Replise> replises, AtomicInteger totalCount) {
        replies.stream().forEach(replies1 -> {
            Replise replise = new Replise();
            replise.setUname(replies1.getMember().getUname());
            replise.setMessage(replies1.getContent().getMessage());
            replise.setLike(replies1.getLike());
            replise.setRCount(replies1.getRcount());
            replise.setCTime(new Date(Long.parseLong(replies1.getCtime() + "000")));

            if (replies1.getReplies() != null) {
                cycleSave(replies1.getReplies(), replises, totalCount);
            }
            replises.add(replise);
            totalCount.incrementAndGet(); // Increment totalCount atomically
        });
    }

    /*@GetMapping("/getBilibiliMessage")
    public String getMessage() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = sdf.format(date);
        log.info("开始获取评论:{}", formattedDateTime);

        // 查询前100页数据
        for (int i = 1; i <= page; i++) {
            String apiUrl = "https://api.bilibili.com/x/v2/reply/" +
                    "main?jsonp=jsonp&type=1&oid=" + oid + "&mode=3&plat=1&next=" + i;

            // 发送GET请求并获取响应
            ApiResponse response = restTemplate.getForObject(apiUrl, ApiResponse.class);
            // 获取到每页的评论
            List<Replies> replies = response.getData().getReplies();
            //转换
            List<Replise> replises = new ArrayList<>();
            cycleSave(replies, replises);

            repliseService.saveBatch(replises);

        }
        Date date1 = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime1 = sdf.format(date1);
        log.info("结束获取评论:{}", formattedDateTime1);
        log.info("获取总评论条数:{}", count);
        return "获取" + count + "条数据成功";

    }

    private void cycleSave(List<Replies> replies, List<Replise> replises) {
        replies.stream().forEach(replies1 -> {
            Replise replise = new Replise();
            replise.setUname(replies1.getMember().getUname());
            replise.setMessage(replies1.getContent().getMessage());
            replise.setLike(replies1.getLike());
            replise.setRCount(replies1.getRcount());
            replise.setCTime(new Date(Long.parseLong(replies1.getCtime() + "000")));

            if (replies1.getReplies() != null) {
                cycleSave(replies1.getReplies(), replises);
            }
            replises.add(replise);
            count++;
        });
    }*/

}
