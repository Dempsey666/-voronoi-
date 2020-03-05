package dao;

import pojo.*;

import java.util.*;

/**
 * Package: dao
 * Description：
 * Author: Dempsey
 * Date:  2020/3/2 19:28
 * Modified By:
 */
public class createDelaunayTriangleMap {


    //创建三角网
    public static List<DelaunayTriangle> createDelanuaryTriangleMap(List<Point> points) {
        //构建初始的三角网

        //构建超级三角形
        DelaunayTriangle superTriangle = createDelaunayTriangleMap.createSuperTriangle(points);

        //构建Delaunary三角网
        List<DelaunayTriangle> temp = new ArrayList<>();//未确定的三角形列表
        List<DelaunayTriangle> triangles = new ArrayList<>();//确定的三角形列表
        List<Edge> buffer = new ArrayList<>();//未使用完的边

        //将超级三角形放入temp中
        temp.add(superTriangle);

        //从左到右遍历所有的点
        for (Point point : points) {
            // 初始化buffer
            buffer.clear();
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
                    continue;
                }
                //在圆右
                if (WHEREISPOINT == 0) {
                    //把三角形从temp移到确定的三角形列表中
                    triangles.add(triangle);
                    temp.remove(triangle);
                    continue;
                }
                //在圆外
                if (WHEREISPOINT == 1) {
                    continue;
                    //跳过，写一下思路清楚一点，其实不用写
                }

            }
            //顺序遍历所有buffer中的边,进行去重（重叠的两条边都删）
            EdgesQuChong(buffer);
            //顺序遍历所有buffer中的边，将现有点与所有边一一组合为新的三角形存入temp中
            for (Edge edge : buffer) {
                temp.add(createTriangleWithEdgePoint(edge, point));
            }
        }
        //所有点遍历完之后，合并所有确定未确定的三角形列表
        triangles.addAll(temp);
        //去除所有与超级三角形三点有关的三角形
        deleteTriangleWithSuperTriangle(triangles, superTriangle);

//要开始凸边修饰了

        //先获取有序的边缘边双向链表
        List<Edge> broaderEdges = selectBroaderEdgeList(triangles);
        //再从链表中提取出所有边缘点集
        List<Point> broaderPoints = selectPointsWithEdgeList(broaderEdges);
        //获取所有边的集合
        List<Edge> allEdges = getAllEdge(triangles);
        //正片开始
        while (true) {
            int anyCross = 0;
            for (int i = 0; i < broaderPoints.size() - 1; i++) {
                //得到点i,i+1,i+2
                Point pointI = broaderPoints.get(i);
                Point pointI1 = broaderPoints.get((i + 1)%broaderPoints.size());
                Point pointI2 = broaderPoints.get((i + 2)%broaderPoints.size());
                //得到i，i+2连接的线段
                Edge edgeII2 = new Edge(pointI, pointI2);
                //得到与点i+1有关的所有线段，以该点为A
                List<Edge> edgeListWithPoint = getEdgeListWithPoint(allEdges, pointI1);
                System.out.print(edgeListWithPoint.size()+"  ");
                //移除与i，i+2有关的线段
                removeEdgesWithPoint(edgeListWithPoint,pointI);
                removeEdgesWithPoint(edgeListWithPoint,pointI2);
                System.out.println(edgeListWithPoint.size()+"  ");

                //遍历除i，i+2有关的所有线段，判断其射线是否与线段（i，i+2）相交
                for(Edge edge : edgeListWithPoint){
                    int isCroiss = isCross(edgeListWithPoint,edgeII2);
                    if(isCroiss==0){
                        //若不相交，在边缘边链表中，去掉（i,i+1）、（i+1,i+2）,添加（i，i+2）
                        broaderEdges.remove(new Edge(pointI,pointI1));
                        broaderEdges.remove(new Edge(pointI1,pointI2));
                        broaderEdges.add(edgeII2);
                        //在所有边集合中，添加（i,i+2)
                        allEdges.add(edgeII2);
                        //在边缘点链表中，去掉i+1
                        broaderPoints.remove(pointI1);
                        //在三角形列表中，新增三角形（i,i+1,i+2）
                        triangles.add(new DelaunayTriangle(pointI,pointI1,pointI2));
                        //ystem.out.println(triangles.get(triangles.size()-1));
                        break;
                    }
                    else{
                        anyCross+=isCroiss;//只要一动，就证明这次遍历发现了凹点，那就再来一次遍历
                    }
                }
            }
            if(anyCross==0){
                break;
            }
            break;
        }
        System.out.println(triangles.size());
        return triangles;
    }

    /*---------------------------我是快乐的分割线----------------------*/

    //判断点(i+1)是否为凹点，返回该点与各条射线相交的数量
    public static int isCross(List<Edge> edgeListWithPoint, Edge edgeII2) {
        int isCross = 0;
        for (Edge edge : edgeListWithPoint) {
            if (isCrossOrNot(edge,edgeII2)) {
                isCross += 1;
            }
        }
        return isCross;
    }

    //判断射线（定点A）与线段是否相交
    public static boolean isCrossOrNot(Edge ShootEdge, Edge edge) {
        //初始化直线,A点不能动，C点要在D点左边
        double xA, xB, xC, xD;
        double yA, yB, yC, yD, y5, y6, y7;
        xA = ShootEdge.getA().getLongitude();
        xB = ShootEdge.getB().getLongitude();
        xC = edge.getA().getLongitude();
        xD = edge.getA().getLongitude();
        yA = ShootEdge.getA().getLatitude();
        yB = ShootEdge.getB().getLatitude();
        yC = edge.getA().getLatitude();
        yD = edge.getA().getLatitude();
        if (xC > xD) {
            double temp;
            temp = xC;
            xC = xD;
            xD = temp;
            temp = yC;
            yC = yD;
            yD = temp;
        }
        double k1 = (yB - yA) / (xB - xA);
        double k2 = (yD - yC) / (xD - xC);
        double b1 = yA - k1 * xA;
        double b2 = yC - k2 * xC;

        //开始判断
        if (xB > xA) {
            y5 = k1 * xC + b1;
            y6 = k1 * xD + b1;
            y7 = k2 * xA + b2;
            if (xC > xA && xD > xA) {
                if (((y5 - yC) * (y6 - yD)) < 0) {
                    return true;
                }
            }
            if (xC < xA && xD > xA) {
                if (((y7 - yA) * (yD - y6)) < 0) {
                    return true;
                }
            }
        }
        if (xB < xA) {
            y5 = k1 * xC + b1;
            y6 = k1 * xD + b1;
            y7 = k2 * xA + b2;
            if (xC < xA && xD < xA) {
                if (((y5 - yC) * (y6 - yD)) < 0) {
                    return true;
                }
            }
            if (xC < xA && xD > xA) {
                if (((y7 - yA) * (yC - y6)) < 0) {
                    return true;
                }
            }
        }

        return false;
    }

    //遍历所有三角形，获得所有边的不重复集
    public static List<Edge> getAllEdge(List<DelaunayTriangle> triangles) {
        List<Edge> allEdges = new ArrayList<>();//所有边缘边的集合
        for (DelaunayTriangle triangle : triangles) {
            for (int i = 0; i < 3; i++) {
                Edge edge = triangle.getEdges().get(i);
                Edge turnEdge = new Edge(edge.getB(), edge.getA());
                if (!allEdges.contains(edge) && !allEdges.contains(turnEdge)) {
                    allEdges.add(edge);
                }
            }
        }
        return allEdges;
    }

    //在边集中去掉与点有关的所有边
    public static void removeEdgesWithPoint(List<Edge> edges,Point point){
        for(int i = edges.size()-1;i>=0;i--){
            Edge edge = edges.get(i);
            if(edge.getA().equals(point) || edge.getB().equals(point)){
                edges.remove(edge);
            }
        }
    }

    //得到一个点所有关联的edge,并调整该点为A
    public static List<Edge> getEdgeListWithPoint(List<Edge> allEdges, Point point) {
        List<Edge> edgeListWithPoint = new ArrayList<>();//点有关的边集
        for (Edge edge : allEdges) {
            if (edge.getA().equals(point)) {
                edgeListWithPoint.add(edge);
            }
            if (edge.getB().equals(point)) {
                edgeListWithPoint.add(new Edge(edge.getB(), edge.getA()));
            }
        }
        return edgeListWithPoint;
    }

    //从链表中提取出所有边缘点集
    public static List<Point> selectPointsWithEdgeList(List<Edge> edges) {
        List<Point> broaderPoints = new LinkedList<>();
        for (Edge edge : edges) {
            broaderPoints.add(edge.getA());
        }
        return broaderPoints;
    }

    //获取边缘边的有序集合
    public static List<Edge> selectBroaderEdgeList(List<DelaunayTriangle> triangles) {
        List<Edge> allBroaderEdges = new ArrayList<>();//所有边缘边的集合
        LinkedList<Edge> broaderEdges = new LinkedList<>();//有顺序的边缘边集合
        //遍历所有三角形，获得所有边
        for (DelaunayTriangle triangle : triangles) {
            allBroaderEdges.addAll(triangle.getEdges());
        }
        //去除所有重复的边，留下边缘边
        EdgesQuChong(allBroaderEdges);
        //将边缘边按序排列
        for (int i = 0; i < allBroaderEdges.size() - 1; i++) {
            //插入第一个
            if (broaderEdges.size() == 0) {
                broaderEdges.add(allBroaderEdges.get(i));
                continue;
            }
            int firstAId = broaderEdges.getFirst().getA().getId();
            //遍历插入
            for (Edge edge : allBroaderEdges) {
                Edge lastEdge = broaderEdges.getLast();
                if (!edge.equals(lastEdge)) {//在两边不相同的情况下
                    if (lastEdge.getB().getId() == edge.getA().getId()) {//若链表中最后一边的Bid=另一条边的Aid
                        broaderEdges.addLast(edge);//把该边塞到链表尾
                    }
                    if (lastEdge.getB().getId() == edge.getB().getId()) {//若链表中最后一边的Bid=另一条边的Bid
                        Point tempPoint = edge.getB();
                        edge.setB(edge.getA());
                        edge.setA(tempPoint);//把该边的两端换个位置再塞到链表尾
                    }
                }
            }
            int lastBId = broaderEdges.getLast().getB().getId();
            //当链表头id=链表尾id，也就是所有点都遍历完了，跳出循环
            if (firstAId == lastBId) {
                break;
            }
        }
        return broaderEdges;
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
    public static void EdgesQuChong(List<Edge> buffer) {
        List<Edge> chongFu = new ArrayList<>();
        for (int i = buffer.size() - 1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                Edge edge = buffer.get(i);
                Edge edge1 = buffer.get(j);
                if (edge.equals(edge1)) {
                    chongFu.add(edge);
                    chongFu.add(edge1);
                }
            }
        }
        for (Edge edge : chongFu) {
            while (true) {
                if (buffer.contains(edge)) {
                    buffer.remove(edge);
                } else {
                    break;
                }
            }
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
        Point pB = new Point(-1, (longitudeMin + longitudeMax) / 2, (latitudeMax * 2 - latitudeMin));
        Point pA = new Point(-2, xa, h);
        Point pC = new Point(-3, xc, h);
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

}
