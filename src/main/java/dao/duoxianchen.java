package dao;

import pojo.DelaunayTriangle;
import pojo.Edge;
import pojo.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Package: dao
 * Description：
 * Author: Dempsey
 * Date:  2020/3/4 01:51
 * Modified By:
 */
public class duoxianchen extends Thread{

    List<DelaunayTriangle> tempTriangles = new ArrayList<>();//未确定的三角形列表
    List<Edge> buffer= new ArrayList<>();//未使用完的边
    Point point;
    int start;
    int end;

    public duoxianchen(List<Edge> buffer, Point point, int start,int end) {
        this.buffer=buffer;
        this.point=point;
        this.start=start;
        this.end=end;
    }

    @Override
    public void run(){
        for(int i = start;i<end;i++){
            tempTriangles.add(createTriangleWithEdgePoint(buffer.get(i),point));
        }
    }

    public static void aa(List<Edge> buffer, Point point , List<DelaunayTriangle> tempTriangles){
        List<DelaunayTriangle> temp = new ArrayList<>();
        int thread = 7;//线程数
        int avg = buffer.size()/thread;
        int more = buffer.size()%thread;
        int[] num = new int[7];
        for(int i = 0 ;i<more;i++){
            num[i]=avg+1;
        }
        for (int i = more; i < 7; i++) {
            num[i]=avg;
        }
        for (int i = 0; i < thread; i++) {
            int start;
            int end;
            if(i==0){
                start=0;
                end=num[0]-1;
            }
            else {
                start=sum(num,i-1);
                end=sum(num,i)-1;
            }
            duoxianchen duo = new duoxianchen(buffer,point,start,end);
            duo.start();
        }
    }


    public static int sum(int[] num,int i){
        int sum = 0;
        for (int j = 0; j < i; j++) {
            sum+=num[j];
        }
        return sum;
    }

    public static DelaunayTriangle createTriangleWithEdgePoint(Edge edge, Point point) {
        DelaunayTriangle delaunayTriangle = new DelaunayTriangle(edge.getA(), edge.getB(), point);
        return delaunayTriangle;
    }

}

