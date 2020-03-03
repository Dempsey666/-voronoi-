package dao;

import pojo.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Package: dao
 * Description：
 * Author: Dempsey
 * Date:  2020/3/2 19:28
 * Modified By:
 */
public class createDelaunayTriangleMap {

    //做超级三角形（注：每个点都有自己独特的id（从1开始），而超级三角形三个点的id均为0）
    public static DelaunayTriangle createSuperTriangle(List<Point> points) {

        //先以最高点和最宽的两点做矩形，下一步做初始的超级三角形
        double longitudeMin;//横坐标最小值
        double longitudeMax;//横坐标最大值
        double latitudeMax = 0;//纵坐标最大值
        double latitudeMin = 42.5;//纵坐标最小值
        longitudeMin = points.get(0).getLongitude();
        longitudeMax = points.get(points.size() - 1).getLongitude();
        for (int i = 0; i < points.size(); i++) {
            if (latitudeMax < points.get(i).getLatitude()) {
                latitudeMax = points.get(i).getLatitude();
            }
            if (latitudeMin > points.get(i).getLatitude()) {
                latitudeMin = points.get(i).getLatitude();
            }
        }
        //初始化三角形ABC，先确定宽，下一步调整高
        double h = Math.min(points.get(0).getLatitude(), points.get(points.size() - 1).getLongitude());
        double xa = 1.5 * longitudeMin - 0.5 * longitudeMax;
        double xc = 1.5 * longitudeMax - 0.5 * longitudeMin;
        Point pB = new Point(0, (longitudeMin + longitudeMax) / 2, (latitudeMax * 2 - latitudeMin));
        Point pA = new Point(0, xa, h);
        Point pC = new Point(0, xc, h);
        //A、C两点下移，调大超级三角形ABC
        Line superTriangleLine = new Line(pB, pA);
        for (double i = h; i > 40; i -= 0.01) {
            if (i < latitudeMin) {
                h = i;
                xa += (-0.1 / superTriangleLine.getK());
                xc -= (-0.1 / superTriangleLine.getK());
                pA.setLongitude(xa);
                pA.setLatitude(h);
                pC.setLongitude(xc);
                pC.setLatitude(h);
                break;
            }
        }
        return new DelaunayTriangle(pA, pB, pC);
    }

    //创建三角网
    public static void createDelanuaryTriangleMap(List<Point> points, List<DelaunayTriangle> temp, List<DelaunayTriangle> triangles, List<Edge> buffer, DelaunayTriangle superTriangle) {
        //构建初始的三角网
        //从左到右遍历所有的点
        for (Point point : points) {
            // 初始化buffer
            buffer.clear();
            System.out.print(point.getId() + "\t\t");

            //倒序遍历所有temp中的三角形
            for (int i = temp.size() - 1; i >= 0; i--) {
                DelaunayTriangle triangle = temp.get(i);
                //获取点和三角形外接圆的位置关系
                int WHEREISPOINT = WhereIsPoint(triangle, point);
                //在圆内
                if (WHEREISPOINT == -1) {
                    //把三角形的三边存入buffer中
                    buffer.addAll(triangle.getEdges());
                    //在temp中删除该三角形
                    temp.remove(triangle);
                }
                //在圆右
                if (WHEREISPOINT == 0) {
                    //把三角形从temp移到确定的三角形列表中
                    triangles.add(triangle);
                    temp.remove(triangle);
                }
                //在圆外
                if (WHEREISPOINT == 1) {
                    //跳过，写一下思路清楚一点，其实不用写
                }
            }
            //顺序遍历所有buffer中的边,进行去重（重叠的两条边都删）
            bufferQuChong(buffer);
            System.out.print(buffer.size()+"  ");
            //顺序遍历所有buffer中的边，将现有点与所有边一一组合为新的三角形存入temp中
            for (Edge edge : buffer) {
                temp.add(createTriangleWithEdgePoint(edge, point));
            }
            System.out.println(temp.size());
        }
        //所有点遍历完之后，合并所有确定未确定的三角形列表
        triangles.addAll(temp);
        //去除所有与超级三角形三点有关的三角形
        deleteTriangleWithSuperTriangle(triangles, superTriangle);
    }


    /*---------------------------我是快乐的分割线----------------------*/

    //去除所有与超级三角形三点有关的三角形
    public static void deleteTriangleWithSuperTriangle(List<DelaunayTriangle> triangles, DelaunayTriangle superTriangle) {
        List<Point> superPoints = new ArrayList<>();
        superPoints.add(superTriangle.getP1());
        superPoints.add(superTriangle.getP2());
        superPoints.add(superTriangle.getP3());
        for (Point superPoint : superPoints) {
            for (int i = triangles.size() - 1; i >= 0; i--) {
                DelaunayTriangle triangle = triangles.get(i);
                if (triangle.getP1() == superPoint || triangle.getP2() == superPoint || triangle.getP3() == superPoint) {
                    triangles.remove(triangle);
                }
            }
        }
    }

    //通过一条边和一个点建立一个三角形(三点做三角形在pojo.DelaunayTreiangle里面)
    public static DelaunayTriangle createTriangleWithEdgePoint(Edge edge, Point point) {
        DelaunayTriangle delaunayTriangle = new DelaunayTriangle(edge.getA(), edge.getB(), point);
        return delaunayTriangle;
    }

    //buffer去重（重复的两条边都删除）
    public static void bufferQuChong(List<Edge> buffer) {
        List<Edge> chongFu = new ArrayList<>();
        Set<Edge> newBuffer = new HashSet<>();
        for (int i = buffer.size() - 1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                Edge edge = buffer.get(i);
                Edge edge1 = buffer.get(j);
                if (edge.equals(edge1)) {
                    chongFu.add(edge);
                }
            }
        }
        for (Edge edge1 : chongFu) {
            for (Edge edge : buffer) {
                if (edge != edge1) {
                    newBuffer.add(edge);
                }
            }
        }
        if (newBuffer.size() != 0) {
            buffer.clear();
            buffer.addAll(newBuffer);
        }
    }

    //判断点和三角形外接圆的相对位置
    public static int WhereIsPoint(DelaunayTriangle tri, Point p) {
        //应有三种状态：在圆右侧，在圆内，在圆外
        if (distanceWithTwoPoints(p.getS(), tri.getCenterPoint()) < tri.getRadius()) {
            return -1;//在圆内
        } else if (p.getLongitude() > (tri.getCenterPoint().getLongitude() + tri.getRadius())) {
            return 0;//在圆右
        } else {
            return 1;//在圆外
        }
    }

    //计算两点间距离
    public static double distanceWithTwoPoints(Site s1, Site s2) {
        double x1 = s1.getLongitude();
        double x2 = s2.getLongitude();
        double y1 = s1.getLatitude();
        double y2 = s2.getLatitude();
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }
}
