package skr.wtf.seckill.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skr.wtf.seckill.dto.Stock;
import skr.wtf.seckill.dto.UserOrder;
import skr.wtf.seckill.mapper.OrderMapper;
import skr.wtf.seckill.mapper.StockMapper;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class OrderService {

    private static final String STOCK_PREFIX = "stock:";

    private static String TOPIC = "OrderTopic";

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private StockMapper stockMapper;


    @Autowired
    private DefaultMQProducer producer;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public static Map<Integer, Boolean> orderInfo = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        stockMapper.findAll().forEach(stock -> {
            redisTemplate.opsForValue().set(STOCK_PREFIX + stock.getId(), String.valueOf(stock.getCount()));
        });
    }

    @Transactional
    public int placeOrder(int prodId, int userId) {
        Stock stock = stockMapper.findById(prodId);
        if (stock == null) {
            log.info("查无次库存{}", prodId);
            return -1;
        }
        if (stock.getCount() > 0) {
            stockMapper.decCount(stock.getId());
            orderMapper.saveOder(new UserOrder(100, userId, stock.getId(), stock.getName()));
            return 1;
        }
        return 0;
    }


    public int placeOrderV2(int prodId, int userId) {
        Long decrement = redisTemplate.opsForValue().decrement(STOCK_PREFIX + prodId);
        if (decrement < 0) {
            //库存销售完
            redisTemplate.opsForValue().increment(STOCK_PREFIX + prodId);
            return -1;
        }
        try {
            stockMapper.decCountNoLock(prodId);
            return orderMapper.saveOder(new UserOrder(100, userId, prodId, "小米手机"));
        } catch (Exception e) {
            redisTemplate.opsForValue().increment(STOCK_PREFIX + prodId);
            return -1;
        }
    }

    public int placeOrderV3(int prodId, int userId) {
        Boolean aBoolean = orderInfo.get(prodId);
        if (aBoolean == null || !aBoolean) {
            Long decrement = redisTemplate.opsForValue().decrement(STOCK_PREFIX + prodId);
            if (decrement < 0) {
                //库存销售完
                orderInfo.put(prodId, true);
                redisTemplate.opsForValue().increment(STOCK_PREFIX + prodId);
                return -1;
            }
            try {
                stockMapper.decCountNoLock(prodId);
                return orderMapper.saveOder(new UserOrder(100, userId, prodId, "小米手机"));
            } catch (Exception e) {
                redisTemplate.opsForValue().increment(STOCK_PREFIX + prodId);
                return -1;
            }
        }
        return 0;
    }

    public int placeOrderV4(int prodId, int userId) throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Boolean aBoolean = orderInfo.get(prodId);
        if (aBoolean == null || !aBoolean) {
            Long decrement = redisTemplate.opsForValue().decrement(STOCK_PREFIX + prodId);
            if (decrement < 0) {
                //库存销售完
                orderInfo.put(prodId, true);
                redisTemplate.opsForValue().increment(STOCK_PREFIX + prodId);
                return -1;
            }
            Message msg=new Message(TOPIC,(prodId+"@"+userId).getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(msg);
            return 1;
        }
        return 0;
    }
}
