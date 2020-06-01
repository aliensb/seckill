package skr.wtf.seckill.mqconsumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import skr.wtf.seckill.dto.UserOrder;
import skr.wtf.seckill.mapper.OrderMapper;
import skr.wtf.seckill.mapper.StockMapper;

import java.util.List;

@Slf4j
@Component
public class OrderConsumerListener implements MessageListenerConcurrently {


    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private StockMapper stockMapper;




    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        if (CollectionUtils.isEmpty(msgs)) {
            log.info("receive blank msgs...");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        MessageExt messageExt = msgs.get(0);
        String msg = new String(messageExt.getBody());

        // mock 消费逻辑
        placeOrder(msg);

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

    }

    private void placeOrder(String msg) {
        log.info("consumer receive msg :{}", msg);
        if (StringUtils.isBlank(msg) || !msg.contains("@")) {
            return;
        }
        String[] split = msg.split("@");
        if (split == null || split.length != 2) {
            return;
        }
        String prodId = split[0];
        String userId = split[1];
        stockMapper.decCountNoLock(Integer.valueOf(prodId));
        orderMapper.saveOder(new UserOrder(100, Integer.valueOf(userId), Integer.valueOf(prodId), "小米手机"));
    }
}
