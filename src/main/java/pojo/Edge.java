package pojo;

import lombok.Data;

@Data
//三角形的边
public class Edge
{
	private Point a, b;
	public Edge(Point a, Point b)
	{
		this.a = a;
		this.b = b;
	}

	//重写equals，使两边的两点相等时，判断两边为一边
	@Override
	public boolean equals(Object obj){
		if(obj==null){
			return false;
		}
		if(this==obj){
			return true;
		}
		if(obj instanceof Edge) {
			Edge edge = (Edge) obj;
			if(edge.getA()==this.getA()&&edge.getB()==this.getB()){
				return true;
			}else if (edge.getA()==this.getB()&&edge.getB()==this.getA()){
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	@Override
	public String toString(){
		return 	"("+this.getA().getId()+","+this.getB().getId()+")";

	}
}