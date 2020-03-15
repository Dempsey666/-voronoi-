package pojo;

/**
 * Package: beans
 * Description：
 * Author: Dempsey
 * Date:  2020/3/1 14:08
 * Modified By:
 */

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class Man {
    private long time;   //什么时候
    private long imsi;   //谁
    private double longitude;
    private double latitude; //在哪里
    private int lac;//在初始数据中的区块
    private int calLac;//算出来的所述区块
    private int hour;//数据量和密度都不够，只能用小时数据来代替每（5/10秒）

    public void setHour() {
        this.hour = (int) (this.time/10000%100);;
    }
}
