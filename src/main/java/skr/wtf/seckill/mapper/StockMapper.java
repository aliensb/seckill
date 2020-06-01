package skr.wtf.seckill.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import skr.wtf.seckill.dto.Stock;

import java.util.List;

public interface StockMapper {
    @Select("select * from stock")
    List<Stock> findAll();

    @Select("select * from stock where id=#{id}")
    Stock findById(@Param("id") Integer id);

    @Update("update stock set count =count-1 where id=#{id} and count>0")
    int decCount(Integer id);

    @Update("update stock set count =count-1 where id=#{id} ")
    int decCountNoLock(Integer id);
}
