package pojo;

/**
 * Package: pojo
 * Description：
 * Author: Dempsey
 * Date:  2020/3/2 20:27
 * Modified By:
 */
import lombok.Data;

@Data
public class Line {
    private Point p1;
    private Point p2;
    private double k;
    private double b;

    public Line(Point p1,Point p2){
        this.p1=p1;
        this.p2=p2;
        createLineWithTwoPoints(this);
    }

    private void createLineWithTwoPoints(Line line){
        double k , b;
        double x1,x2,y1,y2;
        x1=p1.getLongitude();
        x2=p2.getLongitude();
        y1=p1.getLatitude();
        y2=p2.getLatitude();
        line.k=(y1-y2)/(x1-x2);
        line.b=y1-(y1-y2)*x1/(x1-x2);
    }

}
