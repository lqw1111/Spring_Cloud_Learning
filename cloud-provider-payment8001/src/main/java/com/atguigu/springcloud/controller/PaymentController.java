package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommentResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Resource
    private DiscoveryClient discoveryClient;

    @Value("${server.port}")
    private String serverPort;

    @PostMapping(value = "/payment/create")
    public CommentResult create(@RequestBody Payment payment)
    {
        int result = paymentService.create(payment);
        log.info("*******插入结果: " + result);

        if (result > 0) {
            return new CommentResult(200, "插入成功, server Port" + serverPort, result);
        } else {
            return new CommentResult(444, "插入数据库失败", null);
        }
    }

    @GetMapping(value = "/payment/get/{id}")
    public CommentResult getPaymentById(@PathVariable Long id)
    {
        Payment result = paymentService.getPayment(id);
        log.info("*******查询结果: " + result);

        if (result != null) {
            return new CommentResult(200, "查询成功, server Port" + serverPort, result);
        } else {
            return new CommentResult(444, "没有对应记录，查询Id: " + id, null);
        }
    }

    @GetMapping(value = "/payment/discovery")
    public Object discovery()
    {
        List<String> services = discoveryClient.getServices();
        for (String e : services) {
            log.info("****** element: " + e);
        }

        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getServiceId() + " " + instance.getHost() + " " + instance.getPort() + " " + instance.getUri());
        }

        return discoveryClient;
    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }

    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeighTimeout() {
        try {TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) {e.printStackTrace();}
        return serverPort;
    }

    @GetMapping(value="/payment/zipkin")
    public String paymentZipkin() {
        return "hello,i am paymentZipkin server fallback,O(∩_∩)O哈哈~";
    }

}
