package skr.wtf.seckill.mapper;

import org.apache.ibatis.annotations.Insert;
import skr.wtf.seckill.dto.UserOrder;

public interface OrderMapper {
    @Insert("insert into user_order (stock_id,stock_name,user_id) values (#{stockId},#{stockName},#{userId})")
    int saveOder(UserOrder order);
}
