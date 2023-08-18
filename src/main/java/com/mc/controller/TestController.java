package com.mc.controller;

import com.mc.common.ApiResponse;
import com.mc.entity.Replies;
import com.mc.entity.Replise;
import com.mc.service.RepliseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @GetMapping("/getTest")
    public void getTest() throws InterruptedException {
        log.info("{}接收到请求:", Thread.currentThread().getName());
        TimeUnit.HOURS.sleep(1);
    }

    @GetMapping("/getMessage")
    public String getMessage() {
        // 查询前100页数据
        for (int i = 1; i < 1000; i++) {
            String apiUrl = "https://api.bilibili.com/x/v2/reply/" +
                    "main?jsonp=jsonp&type=1&oid=956733745&mode=3&plat=1&next=" + i;
            /*String responseJson = restTemplate.getForObject(apiUrl, String.class);
            System.out.println(responseJson);*/

            // 发送GET请求并获取响应
            ApiResponse response = restTemplate.getForObject(apiUrl, ApiResponse.class);
            // 获取到每页的评论
            List<Replies> replies = response.getData().getReplies();
            //转换
            List<Replise> replises = new ArrayList<>();
            cycleSave(replies, replises);

            repliseService.saveBatch(replises);
        }


        return null;
    }

    private void cycleSave(List<Replies> replies, List<Replise> replises) {
        replies.stream().forEach(replies1 -> {
            Replise replise = new Replise();
            replise.setUname(replies1.getMember().getUname());
            replise.setMessage(replies1.getContent().getMessage());
            replise.setLike(replies1.getLike());
            replise.setRCount(replies1.getRcount());
            replise.setCTime(new Date(Long.parseLong(replies1.getCtime() + "000")));
          /*  System.out.println("用户名称:" + replies1.getMember().getUname() +
                    ",回复内容：" + replies1.getContent().getMessage() +
                    ",点赞数:" + replies1.getLike() + ",评论数:" +
                    replies1.getRcount() +
                    ",评论时间:" + new Date(Long.parseLong(replies1.getCtime() + "000")));*/
            if (replies1.getReplies() != null) {
                cycleSave(replies1.getReplies(), replises);
            }
            replises.add(replise);

        });
    }

}
