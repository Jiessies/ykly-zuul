package com.ykly.zuul.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ykly.zuul.config.mq.AmqpConfig;
import com.ykly.zuul.config.mq.DelayPostProcessor;
import com.ykly.zuul.entity.MQOrderMsg;
import com.ykly.zuul.entity.Notice;
import com.ykly.zuul.entity.ResMsg;
import com.ykly.zuul.repository.NoticeRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoticeController {

    @Value("${spring.application.name}")
    private String serverName;

    @Autowired
    private NoticeRepository nticeRepository;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @GetMapping("/el/save")
    public ResMsg save(@RequestParam(value = "id") long id,
                       @RequestParam(value = "title") String title){

        Notice article = new Notice();
        article.setId(id);
        article.setReadCount(123);
        article.setTitle(title);
        article.setTitle("springboot整合elasticsearch，这个是新版本 2018年");
        nticeRepository.save(article);
        return ResMsg.succ();
    }


    /**
     * @param title   搜索标题
     * @param pageable page = 第几页参数, value = 每页显示条数
     */
    @GetMapping("/el/search")
    public ResMsg<List<Notice>> search(@RequestParam(value = "title") String title, @PageableDefault(page = 1, value = 10) Pageable pageable){

        //按标题进行搜索
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("title", title);

        //如果实体和数据的名称对应就会自动封装，pageable分页参数
        Iterable<Notice> listIt =  nticeRepository.search(queryBuilder,pageable);

        //Iterable转list
        List<Notice> list= Lists.newArrayList(listIt);

        return ResMsg.succWithData(list);
    }

    @PostMapping(value = "/send/{delayTime}")
    public String send(@RequestBody MQOrderMsg orderMsg, @PathVariable(value = "delayTime") String delayTime) {
        try {
            String msg = JSON.toJSONString(orderMsg);
            amqpTemplate.convertAndSend(AmqpConfig.DELAY_EXCHANGE, AmqpConfig.DELAY_ROUTING_KEY, msg,
                    new DelayPostProcessor(delayTime));
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "发送失败";
        }
    }

    @GetMapping("/")
    public ResMsg hello(){
        String name = "HELLO " + serverName;
        return ResMsg.succWithData(name);
    }
}
