package skr.wtf.seckill.controller;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import skr.wtf.seckill.service.OrderService;

import java.io.UnsupportedEncodingException;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private DefaultMQProducer producer;


    @GetMapping("placeOrder")
    public int placeOrder(Integer userId, Integer prodId) {
        return orderService.placeOrder(prodId, userId);
    }

    @GetMapping("placeOrderV2")
    public int placeOrderV2(Integer userId, Integer prodId) {
        return orderService.placeOrderV2(prodId, userId);
    }

    @GetMapping("placeOrderV3")
    public int placeOrderV3(Integer userId, Integer prodId) {
        return orderService.placeOrderV3(prodId, userId);
    }

    @GetMapping("placeOrderV4")
    public int placeOrderV4(Integer userId, Integer prodId) throws InterruptedException, RemotingException, MQClientException, MQBrokerException, UnsupportedEncodingException {
        return orderService.placeOrderV4(prodId, userId);
    }
}
