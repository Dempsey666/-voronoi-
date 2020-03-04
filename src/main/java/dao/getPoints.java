package dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import pojo.Site;
import pojo.Point;

/**
 * Package: dao
 * Description：
 * Author: Dempsey
 * Date:  2020/3/1 21:38
 * Modified By:
 */
public class getPoints{
    public static List<Point> getP() throws IOException{
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();

        List<Site> sites = session.selectList("getSiteFromDB");
        session.commit();
        session.close();
        List<Point> points = new ArrayList<>();


        for (Site s : sites) {
            Point p = new Point(0,s);
            points.add(p);
        }

        //对散列表进行排序，经度为主序，纬度为从序,重写id
        Point.Sort(points);
        for(int i=points.size()-1;i>=1;i--){
            if(points.get(i).equals(points.get(i-1))){
                points.remove(i);
            }
        }
        for(Point point:points){
            point.setId(points.indexOf(point)+1);
            System.out.println(point);
        }

        return points;
    }

}
