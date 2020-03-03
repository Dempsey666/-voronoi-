package pojo;

/**
 * Package: beans
 * Description：
 * Author: Dempsey
 * Date:  2020/3/1 14:08
 * Modified By:
 */

import lombok.Data;

import java.util.Date;

@Data
public class man {
    private Date time;   //什么时候
    private long imsi;   //谁
    private double longitude;
    private double latitude; //在哪里
}
