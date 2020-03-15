package pojo;

/**
 * Package: pojo
 * Description：
 * Author: Dempsey
 * Date:  2020/3/1 21:21
 * Modified By:
 */

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Collections;
import java.util.List;


//有id的点
@Data
public class Point implements Comparable<Point> {
    private int id;
    @JSONField(serialize = false)
    private Site s;

    public Point(int id, Site s) {
        this.id = id;
        this.s = s;
    }

    public Point(int id, double x, double y) {
        this.id = id;
        this.s = new Site(x, y);
    }

    public void ToString(Point point) {
        String sentenceFormat = "%6d    %-15.8f\t %-15.8f";
        System.out.format(sentenceFormat, point.getId(), point.getLongitude(), point.getLatitude());
    }

    public static void printPoints(List<Point> points) {
        for (Point point : points) {
            point.ToString(point);
            System.out.println();
        }
    }

    public double getLongitude() {
        return this.s.getLongitude();
    }

    public double getLatitude() {
        return this.s.getLatitude();
    }

    public void setLongitude(double x) {
        this.s.setLongitude(x);
    }

    public void setLatitude(double y) {
        this.s.setLatitude(y);
    }

    @Override
    public int compareTo(Point o) {
        if (this.getLongitude() != o.getLongitude()) {
            return compareWithLongitude(this.getLongitude(),o.getLongitude());
        }
        else{
            return compareWithLongitude(this.getLatitude(),o.getLatitude());
        }
    }

    //对经度排序
    private int compareWithLongitude(double lo1,double lo2){
        if (lo1 < lo2){
            return -1;
        }
        return 1;
    }

    //对纬度排序
    private int compareWithLatitude(double la1,double la2){
        if (la1 < la2){
            return -1;
        }
        return 1;
    }
}
