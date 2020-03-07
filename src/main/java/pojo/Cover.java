package pojo;

import lombok.Data;


import java.util.ArrayList;
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
    Point position;//基站位置
    List<Edge> coverEdges = new ArrayList<>();//覆盖区的边集
    double S;//覆盖区面积
    List<man> mans = new ArrayList<>();//覆盖区有哪些人

    public Cover(Point point,List<Edge> coverEdges){
        this.position=point;
        this.coverEdges=coverEdges;
    }
}
