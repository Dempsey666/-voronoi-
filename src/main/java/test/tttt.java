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
import java.util.Set;

public class tttt {
    public static void main(String[] args) throws IOException {
        //从数据库里得到基站散列表
        List<Point> points = getPoints.getP();

        List<DelaunayTriangle> triangles = createDelaunayTriangleMap.createDelanuaryTriangleMap(points);

    }
}
