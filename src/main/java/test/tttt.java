package test;
/**
 * Package: text
 * Description：
 * Author: Dempsey
 * Date:  2020/3/1 15:47
 * Modified By:
 */

import dao.getPoints;
import dao.getMan;
import pojo.DelaunayTriangle;
import pojo.Edge;
import pojo.Point;
import dao.createDelaunayTriangleMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class tttt {
    public static void main(String[] args) throws IOException {
        //从数据库里得到基站散列表
        List<Point> points = getPoints.getP();

        //对散列表进行排序，经度为主序，纬度为从序
        Point.Sort(points);

        //构建超级三角形
        DelaunayTriangle superTriangle=createDelaunayTriangleMap.createSuperTriangle(points);

        //构建Delaunary三角网
        List<DelaunayTriangle> tempTriangles = new ArrayList<>();//未确定的三角形列表
        List<DelaunayTriangle> triangles =new ArrayList<>();//确定的三角形列表
        List<Edge> buffer= new ArrayList<>();//未使用完的边

        tempTriangles.add(superTriangle);//将超级三角形放入temp中
        //从左向右遍历所有的点
        for(Point point : points){

        }

    }
}
