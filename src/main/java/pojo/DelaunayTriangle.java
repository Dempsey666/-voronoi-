package pojo;

/**
 * Package: pojo
 * Description：
 * Author: Dempsey
 * Date:  2020/3/1 22:18
 * Modified By:
 */

import lombok.Data;
import lombok.ToString;
import pojo.Point;
import pojo.Site;

import java.util.List;
import java.util.ArrayList;

@Data
public class DelaunayTriangle {
    private Point p1, p2, p3; //三角形三点
    private Site centerPoint; //外界圆圆心
    private double radius; //外接圆半径
    private ArrayList<Edge> edges = new ArrayList<>();//三边yi

    //传入三个点的构造方法，构造的同时计算出圆心和半径
    public DelaunayTriangle(Point p1, Point p2, Point p3) {
        centerPoint = new Site();
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        //构造外接圆圆心以及半径
        circle_center(this);
        TriangleEdges(this);
    }

    //计算三角形外接圆圆心和半径
    private void circle_center(DelaunayTriangle delaunayTriangle) {
        double x1, x2, x3, y1, y2, y3;
        double x = 0;
        double y = 0;
        x1 = p1.getLongitude();
        x2 = p2.getLongitude();
        x3 = p3.getLongitude();
        y1 = p1.getLatitude();
        y2 = p2.getLatitude();
        y3 = p3.getLatitude();

        x=((y2-y1)*(y3*y3-y1*y1+x3*x3-x1*x1)-(y3-y1)*(y2*y2-y1*y1+x2*x2-x1*x1))/(2.0*((x3-x1)*(y2-y1)-(x2-x1)*(y3-y1)));
        y=((x2-x1)*(x3*x3-x1*x1+y3*y3-y1*y1)-(x3-x1)*(x2*x2-x1*x1+y2*y2-y1*y1))/(2.0*((y3-y1)*(x2-x1)-(y2-y1)*(x3-x1)));

        centerPoint.setLongitude(x);
        centerPoint.setLatitude(y);

        radius = Math.sqrt(Math.pow(Math.abs(x1 - x),2) + Math.pow(Math.abs(y1 - y),2));
    }

    //获取三角形三边
    private void TriangleEdges(DelaunayTriangle delaunayTriangle){
        edges.add(new Edge(p1,p2));
        edges.add(new Edge(p2,p3));
        edges.add(new Edge(p3,p1));
    }

}




