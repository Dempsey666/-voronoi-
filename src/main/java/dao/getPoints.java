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
        int id=0;
        for (Site s : sites) {
            id+=1;
            Point p = new Point(id,s);
            points.add(p);
        }
        return points;
    }

}
