package skr.wtf.seckill;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import skr.wtf.seckill.dto.Stock;
import skr.wtf.seckill.dto.UserOrder;
import skr.wtf.seckill.mapper.OrderMapper;
import skr.wtf.seckill.mapper.StockMapper;

@SpringBootTest
@RunWith(SpringRunner.class)
class SeckillApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private StockMapper stockMapper;

    @Test
    public void testMapper(){
        Stock byId = stockMapper.findById(1);
        orderMapper.saveOder(new UserOrder(0,1,byId.getId(),byId.getName()));
        stockMapper.decCount(byId.getId());
    }

}
