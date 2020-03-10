package test;
/**
 * Package: text
 * Description：
 * Author: Dempsey
 * Date:  2020/3/1 15:47
 * Modified By:
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import dao.createVoronoiMap;
import dao.getPoints;
import dao.wrJsonFile;
import pojo.Cover;
import pojo.DelaunayTriangle;
import pojo.Edge;
import pojo.Point;
import dao.createDelaunayTriangleMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class tttt {
    public static void main(String[] args) throws IOException {

        long start = System.currentTimeMillis();

        //从数据库里得到基站散列表
        List<Point> points = getPoints.getP();

        //构建Delaunary三角网
        List<DelaunayTriangle> triangles = new ArrayList<>();//确定的三角形列表
        List<Edge> broderEdges= new ArrayList<>();//边缘边集
        List<Point> broderPoints = new ArrayList<>();//边缘点集

        createDelaunayTriangleMap.createDelanuaryTriangleMap(triangles,broderEdges,broderPoints,points);

        //绘制维诺图
        List<Cover> covers = createVoronoiMap.createVoronoiMap(triangles,broderEdges,broderPoints,points);
//        for(Cover cover:covers){
//            System.out.println(cover.getPosition().getId()+" "+cover.getBroderSites().size()+" "+cover.getS_km());
//        }

        String jsonOutput= JSON.toJSONString(covers, SerializerFeature.DisableCircularReferenceDetect);
        wrJsonFile.writeFile("D:\\123.json",jsonOutput);


        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start) + "ms");

    }
}
