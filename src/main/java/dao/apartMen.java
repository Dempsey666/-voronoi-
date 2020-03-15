package dao;

import pojo.Cover;
import pojo.Edge;
import pojo.Man;

import java.util.List;

/**
 * Package: dao
 * Description：
 * Author: Dempsey
 * Date:  2020/3/11 21:00
 * Modified By:
 */
public class apartMen {
    //根据地域分人
    public static void apartMenByWhere(List<Cover> covers, List<Man> men) {
        //遍历所有的人，按覆盖区分开
        for (Man man : men) {
            double s = 100;//计算面积
            int index = 0;//cover的索引

            //再次遍历所有覆盖区，计算该人的点与覆盖区所有边分别组成的三角形面积和
            for (Cover cover : covers) {
                int tempIndex=covers.indexOf(cover);
                double tempS=0;
                //计算面积和
                for (Edge edge : cover.getCoverEdges()) {
                    double x1, x2, x3, y1, y2, y3, a, b, c, p;
                    x1 = man.getLongitude();
                    y1 = man.getLatitude();
                    x2 = edge.getA().getLongitude();
                    y2 = edge.getA().getLatitude();
                    x3 = edge.getB().getLongitude();
                    y3 = edge.getB().getLatitude();
                    a = Math.sqrt((Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
                    b = Math.sqrt((Math.pow(x1 - x3, 2) + Math.pow(y1 - y3, 2)));
                    c = Math.sqrt((Math.pow(x2 - x3, 2) + Math.pow(y2 - y3, 2)));
                    p = (a + b + c) / 2;
                    tempS += Math.sqrt(p * (p - a) * (p - b) * (p - c));
                }
                tempS=Math.abs(tempS-cover.getS_lola());
                //找到面积差最小的
                if(tempS<s){
                    s=tempS;
                    index=tempIndex;
                }
            }
            covers.get(index).getMen().add(man);
            //同时根据小时分
            covers.get(index).getWhenMan().get(man.getHour()).add(man);
        }

        //看看匹配度
        int match=0;
        for (Cover cover:covers){
            for (Man man :men){
                if(man.getLac()==cover.getPosition().getId()){
                    match+=1;
                }
            }
        }
        //System.out.println(match/men.size());

    }

}
