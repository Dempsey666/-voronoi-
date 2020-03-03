package dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import pojo.man;

/**
 * Package: dao
 * Description：
 * Author: Dempsey
 * Date:  2020/3/1 21:38
 * Modified By:
 */
public class getMan{
    public static void getM() throws IOException{
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();

        List<man> mans = session.selectList("getManFromDB");
        session.commit();
        session.close();

    }
}
