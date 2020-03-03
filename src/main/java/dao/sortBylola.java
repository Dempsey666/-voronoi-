package dao;

/**
 * Package: dao
 * Description：
 * Author: Dempsey
 * Date:  2020/3/2 14:24
 * Modified By:
 */

import pojo.Point;
import java.util.Comparator;

public class sortBylola implements Comparator<Object>{

    //重写compare，对散点集以经纬度排序
    @Override
    public int compare(Object o1, Object o2) {
        if(((Point) o1).getLongitude() != ((Point) o2).getLongitude()){
            return compareWithLongitude(((Point) o1).getLongitude(),((Point) o2).getLongitude());
        }
        else{
            return compareWithLongitude(((Point) o1).getLatitude(),((Point) o2).getLatitude());
        }
    }

    //对经度排序
    private int compareWithLongitude(double lo1,double lo2){
        if (lo1 < lo2){
            return -1;
        }
        return 1;
    }

    //对纬度排序
    private int compareWithLatitude(double la1,double la2){
        if (la1 < la2){
            return -1;
        }
        return 1;
    }

}
