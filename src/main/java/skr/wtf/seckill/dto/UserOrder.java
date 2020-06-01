package skr.wtf.seckill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserOrder {
    private int id;
    private int userId;
    private int stockId;
    private String stockName;
}
