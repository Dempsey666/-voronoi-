package test;

import pojo.*;
import dao.createDelaunayTriangleMap;

import java.rmi.Remote;
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

        Site s1 = new Site(1,2);
        Site s2 = new Site(1,2);
        Site s3 = new Site(2,2);

        System.out.println(s1.equals(s2));
        System.out.println(s1==s2);

    }
}
