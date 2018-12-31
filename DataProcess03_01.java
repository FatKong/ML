import java.util.*;
class K_means{
	public K_means(double dataset[][]){
		//这是一个所有簇的集合
		ArrayList<double[]> Cluster1=new ArrayList<double[]>();
		//这是一个簇
		ArrayList<double[]> Cluster2=new ArrayList<double[]>();
		ArrayList<double[]> Cluster3=new ArrayList<double[]>();
		double u[][]=new double[3][];
		u[0]=dataset[5];
		u[1]=dataset[11];
		u[2]=dataset[23];
		double new_u[][]=new double[3][];
		double du[]=new double[3];     //用于存储某个点到均值的距离
		int count=0;           //计算迭代次数
		while(true){
			if(!Cluster1.isEmpty()) Cluster1.clear();
			if(!Cluster2.isEmpty()) Cluster2.clear();
			if(!Cluster3.isEmpty()) Cluster3.clear();
		for(int k=0;k<dataset.length;k++){
			for(int i=0;i<3;i++){
				
				du[i]=EDist(dataset[k],u[i]);
			}
			double min=du[0];
			int c=0;  //用于记录最小值的下标
			for(int i=0;i<3;i++){
				if(min>du[i]){
					min=du[i];
					c=i;
				}
			}
			if(c==0){
				Cluster1.add(dataset[k]);
			}
			if(c==1){
				Cluster2.add(dataset[k]);
			}
			if(c==2){
				Cluster3.add(dataset[k]);
			}
		}
		
		new_u[0]=newAverage(Cluster1);
		new_u[1]=newAverage(Cluster2);
		new_u[2]=newAverage(Cluster3);
		int flag=0;
		if(u[0][0]!=new_u[0][0] || u[0][1]!=new_u[0][1]){
			u[0]=new_u[0];
			flag++;
		}
		if(u[1][0]!=new_u[1][0] || u[1][1]!=new_u[1][1]){
			u[1]=new_u[1];
			flag++;
		}
		if(u[2][0]!=new_u[2][0] || u[2][1]!=new_u[2][1]){
			u[2]=new_u[2];
			flag++;
		}
		count++;
		if(flag==0) break;
	  }
		//上面已经计算完毕，下面是输出迭代次数，各个簇的均值和数据点
		System.out.println("迭代次数="+count);
		System.out.println("");
		System.out.println("Cluster1: ");
		System.out.print("u1均值： "+new_u[0][0]+" ");
		System.out.println(new_u[0][1]);
		for(int i=0;i<Cluster1.size();i++){
			double temp[]=new double[2];
			temp=Cluster1.get(i);
			for(int j=0;j<2;j++){
				System.out.print(temp[j]+" ");
			}
			System.out.println("");
		}
		System.out.println("");
		System.out.println("Cluster2: ");
		System.out.print("u2均值： "+new_u[1][0]+" ");
		System.out.println(new_u[1][1]);
		for(int i=0;i<Cluster2.size();i++){
			double temp[]=new double[2];
			temp=Cluster2.get(i);
			for(int j=0;j<2;j++){
				System.out.print(temp[j]+" ");
			}
			System.out.println("");
		}
		System.out.println("");
		System.out.println("Cluster3: ");
		System.out.print("u3均值： "+new_u[2][0]+" ");
		System.out.println(new_u[2][1]);
		for(int i=0;i<Cluster3.size();i++){
			double temp[]=new double[2];
			temp=Cluster3.get(i);
			for(int j=0;j<2;j++){
				System.out.print(temp[j]+" ");
			}
			System.out.println("");
		}
	}
	//计算某个值与均值的欧氏距离
	public double EDist(double x[],double u[]){
		double result;
		double sum=0;
		for(int i=0;i<x.length;i++){
			sum=sum+(x[i]-u[i])*(x[i]-u[i]);
		}
		result=Math.sqrt(sum);
		return result;
	}
	//从一个簇中计算新的均值
    public double[] newAverage(ArrayList<double[]> Cluster){
    	double sum1=0,avg1=0,sum2=0,avg2=0;
    	double temp[]=new double[2];
    	double result[]=new double[2];
    	//先计算密度的均值
    	for(int i=0;i<Cluster.size();i++){
    		temp=Cluster.get(i);
    		sum1=sum1+temp[0];
    	}
    	avg1=sum1/Cluster.size();
    	//再计算含糖率的均值
    	for(int i=0;i<Cluster.size();i++){
    		temp=Cluster.get(i);
    		sum2=sum2+temp[1];
    	}
    	avg2=sum2/Cluster.size();
    	result[0]=avg1;
    	result[1]=avg2;
    	return result;
    }
}
public class DataProcess03_01 {
	public static void main(String[] args) {
		//计算原型聚类
		//数据来源西瓜书第202页，西瓜密度与含糖率的关系
		                 //   密度              含糖率
		double dataset[][]={{0.697,0.460},
				            {0.774,0.376},
				            {0.634,0.264},
				            {0.608,0.318},
				            {0.556,0.215},
				            {0.403,0.237},
				            {0.481,0.149},
				            {0.437,0.211},
				            {0.666,0.091},
				            {0.243,0.267},
				            {0.245,0.057},
				            {0.343,0.099},
				            {0.639,0.161},
				            {0.657,0.198},
				            {0.360,0.370},
				            {0.593,0.042},
				            {0.719,0.103},
				            {0.359,0.188},
				            {0.339,0.241},
				            {0.282,0.257},
				            {0.748,0.232},
				            {0.714,0.346},
				            {0.483,0.312},
				            {0.478,0.437},
				            {0.525,0.369},
				            {0.751,0.489},
				            {0.532,0.472},
				            {0.473,0.376},
				            {0.725,0.445},
				            {0.446,0.459}};
		K_means kmeans=new K_means(dataset);
		
	}
}
