import java.math.*;
import java.util.*; 
public class DataProcess01 {
	public static void main(String[] args) {
		double a[]={1,2,3,4,5,6,7,8,9};
		//double i=Quantile(a,0.7);
		//System.out.println(i);
		/*String a[]={"male","what","666"};
		String result[]=new String[a.length];
		System.out.println(a.length);
		result=One_hot(a);
		for(int i=0;i<result.length;i++){
			System.out.println(result[i]);
		}*/
		BinningByWidth(a,2);
	}
	//求均值的方法
	public static double Average(double a[]){
		double result;
		double sum=0;
		for(int i=0;i<a.length;i++){
			sum=sum+a[i];
		}
		result=sum/a.length;
		return result;
	}
	
	//求方差的方法
	public static double Variance(double a[]){
		double result;
		double average=Average(a);
		double sum=0;
		for(int i=0;i<a.length;i++){
			sum=sum+(a[i]-average)*(a[i]-average);
		}
		result=sum/a.length;
		return result;
	}
	
	//求两个向量的余弦相似度
	public static double Similarity(double a[],double b[]){
		double sum=0;
		double Alength=0,Blength=0;//向量a与b的范数
		double result;
		if(a.length==b.length){
		for(int i=0;i<a.length;i++){
			sum=sum+a[i]*b[i];
		}
		for(int i=0;i<a.length;i++){
			Alength=Alength+a[i]*a[i];
		}
		for(int i=0;i<b.length;i++){
			Blength=Blength+b[i]*b[i];
		}
		result=sum/(Math.sqrt(Alength)*Math.sqrt(Blength));
		return result;
		}else{
			return -1;
		}
	}
	//求两个向量的欧氏距离
	public static double EDist(double a[],double b[]){
		double result;
		double sum=0;
		if(a.length==b.length){
		for(int i=0;i<a.length;i++){
			sum=sum+(a[i]-b[i])*(a[i]-b[i]);
		}
		result=Math.sqrt(sum);
		return result;
		}else{
			return -1;
		}
	}
	
	//求分位数
	public static double Quantile(double a[],double n){
		Sort(a);
		int  i=(int)Math.ceil(a.length*n);
		return a[i-1];
	}
	//排序
	public static void Sort(double a[]){
		double temp=0;
		for(int i=a.length-1;i>0;i--){
			for(int j=0;j<i;j++){
				if(a[j+1]<a[j]){
					temp=a[j];
					a[j]=a[j+1];
					a[j+1]=temp;
				}
			}
		}
	}
	//等频分箱,n为箱子大小,其亦被称为等深分箱
	public static void BinningByDepth(double a[],int n){
		if(a.length%n==0){
			int k=0;
			List<double[]> allList = new ArrayList<double[]>();
			//将各个数据装箱
			while(true){
				double temp[]=new double[n];
			for(int i=0;i<n;i++,k++){
				temp[i]=a[k];
			}
			allList.add(temp);
			if(k>=a.length){
				break;
			}
			}
			//输出各个箱子
			for(double[] dou:allList){
				System.out.println(Arrays.toString(dou));
			}
		}else{
			System.out.println("箱子大小不符合");
		}
			
		}
	//等宽分箱法，n为箱子宽度
	 public static void BinningByWidth(double a[],int n){
		 ArrayList<ArrayList<Double>> big=new ArrayList<ArrayList<Double>>();
		 int k=0,i=0;
		 while(true){
			 ArrayList<Double> one=new ArrayList<Double>();
			 k=i;
			 while(true){
				 if(a[i]-a[k]<=n){
					 one.add(a[i]);
					 i++;
				 }
				 if(i>=a.length || k>=a.length || a[i]-a[k]>n){
					 big.add(one);
					 break;
				 }
			 }
			 if(i>=a.length) break;
		 }
		 for(ArrayList<Double> arry:big){
			 for(Double arry2:arry){
				 System.out.print(arry2+" ");
			 }
			 System.out.println("");
		 }
	 }
	
    
	//最大最小归一化
	public static double[] Max_Min_Scaling(double a[]){
		double result[]=new double[a.length];
		double max,min;
		int k1=0;
		int k2=0;
		max=a[0];min=a[0];
		//求数组中的最大值
		for(int i=1;i<a.length;i++){
			if(a[i]>a[k1]){
				k1=i;
			}
		}
		max=a[k1];
		//求数组中的最小值
		for(int i=1;i<a.length;i++){
			if(a[i]<a[k2]){
				k2=i;
			}
		}
		min=k2;
		for(int i=0;i<a.length;i++){
			result[i]=(a[i]-min)/(max-min);
		}
		return result;    //返回已经归一化的结果数组
	}
	
	//Z-score归一化
	public static double[] Z_score(double x[]){
		double result[]=new double[x.length];
		double avg=Average(x);                //计算均值
		double Std=Math.sqrt(Variance(x));    //计算机标准差
		for(int i=0;i<x.length;i++){
			result[i]=(x[i]-avg)/Std;
		}
		return result;                        //返回已经归一化的结果数组
	}
	//One hot编码
	public static String[] One_hot(String a[]){
		String result[]=new String[a.length];
		//字符数组初始化
		for(int i=0;i<result.length;i++){
			result[i]="";
		}
		for(int i=0;i<a.length;i++){
			for(int j=0;j<a.length;j++){
				if(i==j){
					result[i]=result[i]+"1";
				}
				if(i!=j){
					result[i]=result[i]+"0";
				}
			}
		}
		return result;
	}
}
