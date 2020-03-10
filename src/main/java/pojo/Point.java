package pojo;

/**
 * Package: pojo
 * Description：
 * Author: Dempsey
 * Date:  2020/3/1 21:21
 * Modified By:
 */

import com.alibaba.fastjson.annotation.JSONField;
import dao.sortBylola;
import lombok.Data;
import lombok.ToString;

import java.util.Collections;
import java.util.List;


//有id的点
@Data
public class Point {
    private int id;
    @JSONField(serialize = false)
    private Site s;

    public Point(int id, Site s) {
        this.id = id;
        this.s = s;
    }

    public Point(int id, double x,double y) {
        this.id = id;
        this.s = new Site(x,y);
    }

    public void ToString(Point point){
        String sentenceFormat = "%6d    %-15.8f\t %-15.8f";
        System.out.format(sentenceFormat,point.getId(),point.getLongitude(),point.getLatitude());
    }

    public static void Sort(List<Point> points){
        sortBylola sortBylola= new sortBylola();
        Collections.sort(points,sortBylola);
    }

    public static void printPoints(List<Point> points){
        for(Point point : points){
            point.ToString(point);
            System.out.println();
        }
    }

    public double getLongitude(){
        return this.s.getLongitude();
    }

    public double getLatitude(){
        return this.s.getLatitude();
    }

    public void setLongitude(double x){
        this.s.setLongitude(x);
    }

    public void setLatitude(double y){
        this.s.setLatitude(y);
    }

}
