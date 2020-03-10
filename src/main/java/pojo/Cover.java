package pojo;

import com.alibaba.fastjson.annotation.JSONField;
import dao.createDelaunayTriangleMap;
import lombok.Data;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Package: pojo
 * Description：
 * Author: Dempsey
 * Date:  2020/3/7 15:54
 * Modified By:
 */

@Data
public class Cover {

    @JSONField(name = "基站位置",ordinal = 1)
    Point position;//基站位置

    @JSONField(name = "边缘点集",ordinal = 3)
    List<Site> broderSites = new ArrayList<>();//边缘点集

    @JSONField(serialize = false)
    List<Edge> coverEdges = new ArrayList<>();//覆盖区的边集

    @JSONField(serialize = false)
    double S_lola = 0;//覆盖区面积的经纬度表示，用于计算人所在的区域

    @JSONField(name = "面积",ordinal = 2)
    double S_km = 0;//覆盖区面积的km^2形式，用于直观的展示和阈值计算

    @JSONField(serialize = false)
    List<man> mans = new ArrayList<>();//覆盖区有哪些人

    public Cover(Point point, List<Site> broderSites, List<Edge> coverEdges) {
        this.position = point;
        this.broderSites = broderSites;
        this.coverEdges = coverEdges;
        countS();
        sortSite();
    }

    //对覆盖区，计算面积
    private void countS() {
        //只要一个点就好了,因为我能保证覆盖区为凸边形
        Site s = broderSites.get(0);
        for (Edge edge : coverEdges) {
            if ((!edge.getA().getS().equals(s)) || (!edge.getB().getS().equals(s))) {
                double x1, x2, x3, y1, y2, y3, a, b, c, p;
                x1 = s.getLongitude();
                y1 = s.getLatitude();
                x2 = edge.getA().getLongitude();
                y2 = edge.getA().getLatitude();
                x3 = edge.getB().getLongitude();
                y3 = edge.getB().getLatitude();
                a = Math.sqrt((Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
                b = Math.sqrt((Math.pow(x1 - x3, 2) + Math.pow(y1 - y3, 2)));
                c = Math.sqrt((Math.pow(x2 - x3, 2) + Math.pow(y2 - y3, 2)));
                p = (a + b + c) / 2;
                S_lola += Math.sqrt(p * (p - a) * (p - b) * (p - c));

                S_km += S_lola * (6371 * 2 * Math.PI / 360) * (3671 * 2 * Math.PI / 360) * Math.cos(42 * 2 * Math.PI / 360);//6371是地球半径
            }
        }
    }

    //边缘点集顺时针或逆时针排序
    private void sortSite(){//别嫌长，从另外一个函数里截取过来的，就这两个地方要用，懒得做复用了
        List<Edge> allBroderEdges = coverEdges;//所有边缘边的集合
        LinkedList<Edge> broderEdges = new LinkedList<>();//有顺序的边缘边集合
        //将边缘边按序排列
        for (int i = 0; i < allBroderEdges.size() - 1; i++) {
            //插入第一个
            if (broderEdges.size() == 0) {
                broderEdges.add(allBroderEdges.get(i));
            }
            Edge lastEdge = broderEdges.getLast();
            //遍历插入
            for (Edge edge : allBroderEdges) {
                if (!edge.equals(lastEdge)) {//在两边不相同的情况下
                    if (lastEdge.getB().equals(edge.getA())) {//若链表中最后一边的Bid=另一条边的Aid
                        broderEdges.addLast(edge);//把该边塞到链表尾
                    }
                    if (lastEdge.getB().equals(edge.getB())) {//若链表中最后一边的Bid=另一条边的Bid
                        Point tempPoint = edge.getB();
                        edge.setB(edge.getA());
                        edge.setA(tempPoint);//把该边的两端换个位置再塞到链表尾
                        broderEdges.addLast(edge);//把该边塞到链表尾
                    }
                }
            }
        }
        broderSites.clear();
        for(Edge edge:broderEdges){
            broderSites.add(edge.getA().getS());
        }

    }
}
