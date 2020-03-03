package test;

import pojo.*;
import dao.createDelaunayTriangleMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Package: test
 * Description：
 * Author: Dempsey
 * Date:  2020/3/2 17:15
 * Modified By:
 */
public class weerr {
    public static void main(String[] args) {

        List<Point> points = new ArrayList<>();
        points.add(new Point(0, -1,0));
        points.add(new Point(0, 0,1));
        points.add(new Point(0, 1,0));
        points.add(new Point(0, 0,0));
        points.add(new Point(0, 0,2));
        points.add(new Point(0, -1,-1));
        points.add(new Point(0, 2,2));

        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(points.get(0),points.get(1)));
        edges.add(new Edge(points.get(1),points.get(0)));
        for(Edge edge : edges){
            System.out.println(edge);
        }
        System.out.println();
        createDelaunayTriangleMap.bufferQuChong(edges);
        for(Edge edge:edges){
            System.out.println(edge);
        }
    }
}
