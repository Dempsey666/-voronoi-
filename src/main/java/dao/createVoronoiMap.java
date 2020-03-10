package dao;

import pojo.*;

import java.util.*;
import java.util.zip.DeflaterOutputStream;

/**
 * Package: dao
 * Description：
 * Author: Dempsey
 * Date:  2020/3/7 15:31
 * Modified By:
 */
public class createVoronoiMap {

    //绘制维诺图
    public static List<Cover> createVoronoiMap(List<DelaunayTriangle> triangles, List<Edge> broderEdges, List<Point> broderPoints, List<Point> points) {
        List<Cover> cover = new ArrayList<>();//基站和它的覆盖区 集合
        //图形边界

        Site centerPoint = getCenterForPoints(broderPoints);
        double radius = 1.0;

        //遍历求覆盖区
        for (Point point : points) {
            //先完成非边缘点的覆盖区绘制
            if (!broderPoints.contains(point)) {
                //获取点有关的三角形集合
                List<DelaunayTriangle> triangleList = getTrianglesListWithPoint(triangles, point);
                //获取有关三角形的外接圆圆心集合
                List<Site> siteList = getCenterPointsWithTriangles(triangleList);
                //获取点集所有点连成的边集，也就是覆盖区
                List<Edge> coverToPoint = getEdgesWithSites(siteList);
                //最后将结果组合存入cover中
                cover.add(new Cover(point, siteList, coverToPoint));
            }
            //再完成边缘点的预测覆盖区绘制
            else {
                //获取点有关的三角形集合
                List<DelaunayTriangle> triangleList = getTrianglesListWithPoint(triangles, point);
                //获取有关三角形的外接圆圆心集合
                List<Site> siteList = getCenterPointsWithTriangles(triangleList);
                //获取边缘点有关的边缘边集合
                List<Edge> bEforPoint = createDelaunayTriangleMap.getEdgeListWithPoint(broderEdges, point);
                //求边缘点中垂线与图形边界的交点，合并入覆盖区边缘点集
                siteList.addAll(getCrossSites(bEforPoint, centerPoint, radius));
                //获取点集所有点连成的边集，也就是覆盖区
                List<Edge> coverToPoint = getEdgesWithSites(siteList);
                //最后将结果组合存入cover中
                cover.add(new Cover(point, siteList, coverToPoint));
            }
        }


        return cover;
    }

    /*---------------------------我是快乐的分割线----------------------*/

    //求边缘边与图形边界的交点集
    public static List<Site> getCrossSites(List<Edge> bEforPoint, Site centerPoint, double radius) {
        List<Site> CrossSites = new ArrayList<>();
        for (Edge edge : bEforPoint) {
            Site sA = edge.getA().getS();
            Site sB = edge.getB().getS();
            double x1, x2, x3, y1, y2, y3, k1, k2, b1, b2, xc, yc;
            x1 = sA.getLongitude();
            x2 = sB.getLongitude();
            y1 = sA.getLatitude();
            y2 = sB.getLatitude();
            xc = centerPoint.getLongitude();
            yc = centerPoint.getLatitude();
            //点3是中点，线2是中垂线
            x3 = (x1 + x2) / 2;
            y3 = (y1 + y2) / 2;
            k1 = (y1 - y2) / (x1 - x2);
            b1 = y1 - k1 * x1;
            k2 = -1 / k1;
            b2 = y3 - k2 * x3;
            double a = k2 * k2 + 1;
            double b = 2 * k2 * b2 - 2 * xc - 2 * k2 * yc;
            double c = -(radius * radius - xc * xc - b2 * b2 - yc * yc + 2 * b2 * yc);
            double Cross_x1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
            double Cross_x2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
            if (Math.abs(Cross_x1 - x3) < Math.abs(Cross_x2 - x3)) {
                CrossSites.add(new Site(Cross_x1, k2 * Cross_x1 + b2));
            } else {
                CrossSites.add(new Site(Cross_x2, k2 * Cross_x2 + b2));
            }
        }

        return CrossSites;
    }

    //求边缘点的中心，函数运行过程中会输出最远点和最近点与中心的距离，用于手动设置边缘半径
    public static Site getCenterForPoints(List<Point> broderPoints) {
        double x = 0;
        double y = 0;
        for (Point point : broderPoints) {
            x += point.getLongitude();
            y += point.getLatitude();
        }
        x /= broderPoints.size();
        y /= broderPoints.size();

//        double distance_Max=0;
//        double distance_Min=10;
//        double distance;
//        for(Point point:broderPoints){
//            distance=Math.sqrt(Math.pow(x-point.getLongitude(),2)+Math.pow(y-point.getLatitude(),2));
//            distance_Max=Math.max(distance,distance_Max);
//            distance_Min=Math.min(distance,distance_Min);
//        }

        Site centerPointForPoints = new Site(x, y);

        return centerPointForPoints;
    }

    //对点集将他们连接，形成一个区块，内部没有连接
    public static List<Edge> getEdgesWithSites(List<Site> siteList) {
        List<Edge> coverToPoint = new ArrayList<>();
        //先把所有点连起来
        for (Site site : siteList) {
            for (Site site1 : siteList) {
                if (!site.equals(site1)) {
                    coverToPoint.add(new Edge(site, site1));
                }
            }
        }

        //遍历去重
        for (int i = coverToPoint.size() - 1; i >= 1; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (coverToPoint.get(i).equals(coverToPoint.get(j))) {
                    coverToPoint.remove(j);
                    break;
                }
            }
        }

        //遍历去交线
        Set<Integer> indexs = new HashSet<>();
        List<Integer> integers = new ArrayList<>();
        for (int i = coverToPoint.size() - 1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (Cross(coverToPoint.get(i), coverToPoint.get(j))) {
                    indexs.add(i);
                    indexs.add(j);
                }
            }
        }
        integers.addAll(indexs);
        Collections.sort(integers, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // 返回值为int类型，大于0表示正序，小于0表示逆序
                return o2 - o1;
            }
        });
        for (int i = 0; i < integers.size(); i++) {
            coverToPoint.remove((int) integers.get(i));
        }

        return coverToPoint;
    }

    //判断连接，条件：两线段没有同一端点，两线段相交
    public static boolean Cross(Edge e1, Edge e2) {
        if (e1.getA().equals(e2.getA()) || e1.getA().equals(e2.getB()) ||
                e1.getB().equals(e2.getA()) || e1.getB().equals(e2.getB())) {
            return false;
        } else {
            //规范化两线段，两点分别为AB，CD，A在B左边，C在D左边
            //定义x的范围为AC的大，BD的小
            //然后求交点，判断交点的x在不在范围中
            double xA, xB, xC, xD;
            double yA, yB, yC, yD;
            xA = e1.getA().getLongitude();
            xB = e1.getB().getLongitude();
            xC = e2.getA().getLongitude();
            xD = e2.getB().getLongitude();
            yA = e1.getA().getLatitude();
            yB = e1.getB().getLatitude();
            yC = e2.getA().getLatitude();
            yD = e2.getB().getLatitude();
            if (xA > xB) {
                double temp;
                temp = xA;
                xA = xB;
                xB = temp;
                temp = yA;
                yA = yB;
                yB = temp;
            }
            if (xC > xD) {
                double temp;
                temp = xC;
                xC = xD;
                xD = temp;
                temp = yC;
                yC = yD;
                yD = temp;
            }
            //要是两根线差的十万八千里，就不用算下去了
            if (xB < xC || xA > xD) {
                return false;
            } else {
                double k1 = (yB - yA) / (xB - xA);
                double k2 = (yD - yC) / (xD - xC);
                double b1 = yA - k1 * xA;
                double b2 = yC - k2 * xC;
                double x = (b2 - b1) / (k1 - k2);//x是两线交点的横坐标
                if (x > Math.max(xA, xC) && x < Math.min(xB, xD)) {
                    return true;
                }
            }
        }
        return false;
    }

    //获取一个三角形集合中所有圆心的集合
    public static List<Site> getCenterPointsWithTriangles(List<DelaunayTriangle> triangleList) {
        List<Site> centerPoints = new ArrayList<>();
        for (DelaunayTriangle triangle : triangleList) {
            centerPoints.add(triangle.getCenterPoint());
        }
        return centerPoints;
    }

    //得到一个点所有关联的triangles
    public static List<DelaunayTriangle> getTrianglesListWithPoint(List<DelaunayTriangle> triangles, Point point) {
        List<DelaunayTriangle> trianglesListWithPoint = new ArrayList<>();//点有关的三角形集
        for (DelaunayTriangle triangle : triangles) {
            if (triangle.getP1().equals(point) || triangle.getP2().equals(point) || triangle.getP3().equals(point)) {
                trianglesListWithPoint.add(triangle);
            }
        }
        return trianglesListWithPoint;
    }
}
