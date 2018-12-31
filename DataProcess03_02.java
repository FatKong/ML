import java.util.*;
//实现高斯混合聚类
class Kmeans{
	public Kmeans(double dataset[][]){
		double a[]=new double[3];      //模型参数，第i个混合成分的概率
		double u[][]=new double[3][2];  //模型参数，均值
		double cov[][]=new double[2][2]; //模型参数，协方差矩阵
		double postpro[][]=new double[dataset.length][3]; //用于存储所有数据项的后验概率，每行表示一数据项的k个后验概率
		double x[][]=new double[1][2];  //用于提取每个数据项
		ArrayList<double[][]> Cov=new ArrayList<double[][]>();
		
		//新的系数
		double new_u[][]=new double[3][2];
		double new_cov[][]=new double[2][2];
		double new_a[]=new double[3];
		int k=3;        //高斯成分个数为3
		int circle=0;  //循环迭代次数
		
		//这是一个簇
		ArrayList<double[][]> Cluster1=new ArrayList<double[][]>();
		ArrayList<double[][]> Cluster2=new ArrayList<double[][]>();
		ArrayList<double[][]> Cluster3=new ArrayList<double[][]>();
		
		//参数初始化
		for(int i=0;i<k;i++){
			a[i]=1.0/3;
		}
		u[0]=dataset[5];
		u[1]=dataset[21];
		u[2]=dataset[26];
		cov[0][0]=0.1;
		cov[0][1]=0.0;
		cov[1][0]=0.0;
		cov[1][1]=0.1;
		for(int i=0;i<k;i++){
			Cov.add(cov);
		}
		
		while(circle<6){
			
		//计算各个数据项xj由各混合成分生成的后验概率
		for(int j=0;j<dataset.length;j++){
			x[0]=dataset[j];   //数据集中第j个数据项
			postpro[j]=Postpro(x,a,u,Cov);
		}
		
		//计算新的均值,协方差矩阵,和混合系数
		
		//计算均值ui
		for(int i=0;i<k;i++){//最外层循环,计算三个均值向量
			//计算分子
			double temp[][]=new double[1][2];
			double sum1[][]=new double[1][2];
			sum1[0][0]=0;sum1[0][1]=0;
			for(int j=0;j<dataset.length;j++){
				x[0]=dataset[j];   //数据集中第j个数据项
				temp=this.MatrixMultiple(postpro[j][0],x);
				sum1=MatrixAdd(sum1,temp);			
			}
			
			//计算分母
			double sum2=0;//分母
			for(int j=0;j<dataset.length;j++){
				sum2=sum2+postpro[j][i];
			}
			//System.out.println(sum2);
			new_u[i][0]=sum1[0][0]/sum2;
			new_u[i][1]=sum1[0][1]/sum2;
			//均值计算完毕
		}
		
		//计算新的协方差矩阵
		if(!Cov.isEmpty()) Cov.clear();   //将协方差矩阵集清空，准备装入新的协方差矩阵
		for(int i=0;i<k;i++){
			double temp1[][]=new double[1][2];
			double temp2[][]=new double[2][1];
			double Mul1[][]=new double[2][2];
			double Mul2[][]=new double[2][2];
			double temp_u[][]=new double[1][2];
			double sum1[][]=new double[2][2];
			//矩阵sum1初始化
			for(int h=0;h<sum1.length;h++){
				for(int l=0;l<sum1[0].length;l++){
					sum1[h][l]=0;
				}
			}
			for(int j=0;j<dataset.length;j++){
				x[0]=dataset[j];     //提取数据
				temp_u[0]=u[i];      //提取均值
				double x_temp[][]=new double[2][1]; //将x变成列向量
				double temp2_u[][]=new double[2][1];
				x_temp=MatrixTrans(x);
				temp2_u=MatrixTrans(temp_u);
				temp1=MatrixMinus(x_temp,temp2_u); //x-ui
				temp2=MatrixTrans(temp1);    //x-ui的转置
				Mul1=MatrixMultiple(temp1,temp2);  //(x-ui) * (x-ui)T
				Mul2=MatrixMultiple(postpro[j][i],Mul1);
				sum1=MatrixAdd(sum1,Mul2);
			}
			double sum2=0;//分母
			//计算分母
			for(int j=0;j<dataset.length;j++){
				sum2=sum2+postpro[j][i];
			}
			new_cov=MatrixDivide(sum1,sum2);
			Cov.add(new_cov);
		}
		/*System.out.println(circle);//测试
		MatrixPrint(new_cov);*/
		//计算新的混合系数ai
		for(int i=0;i<k;i++){
			double sum=0;
			for(int j=0;j<dataset.length;j++){
				sum=sum+postpro[j][i];
			}
			new_a[i]=sum/dataset.length;
		}
	
		//赋新值
		for(int i=0;i<k;i++){
			a[i]=new_a[i];
			u[i]=new_u[i];
		}
		/*System.out.println(circle);
		for(int i=0;i<3;i++){
			for(int j=0;j<2;j++){
				System.out.print(u[i][j]+" ");
			}
			System.out.println("");
		}*/
		circle++;
	  }
		int max=0;//记录下标
		for(int j=0;j<dataset.length;j++){
			max=0;
			x[0]=dataset[j];   //提取每个数据项
			double temp_postpro[]=new double[3];
			for(int i=0;i<k;i++){
				temp_postpro=postpro[j];
				max=this.SeachforMax(temp_postpro);
			}
			if(max==0){
				Cluster1.add(x);
			}
			if(max==1){
				Cluster2.add(x);
			}
			if(max==2){
				Cluster3.add(x);
			}
		}
		
		//输出每个簇的数据及均值
		System.out.println("Cluster1: ");
		System.out.print("u1均值： "+new_u[0][0]+" ");
		System.out.println(new_u[0][1]);
		
		System.out.println("Cluster2: ");
		System.out.print("u2均值： "+new_u[1][0]+" ");
		System.out.println(new_u[1][1]);
		
		System.out.println("Cluster3: ");
		System.out.print("u3均值： "+new_u[2][0]+" ");
		System.out.println(new_u[2][1]);
	}
	
	//计算某个混合x生成的后验概率 posterior probability 
	//传参：传入某个数据项x，所有的混合系数a,所有的均值u,以及所有的协方差矩阵Cov
	public double[] Postpro(double x[][],double a[],double u[][],ArrayList<double[][]> Cov){
		//先计算分母
		double sum=0;
		double result[]=new double[a.length];
		double u_temp1[][]=new double[1][2];   //用于提取u中某一行的参数值,即某个混合成分的u
		double u_temp2[][]=new double[2][1];   //用于将u变成列向量
		double cov_temp[][]=new double[2][2]; //用于提取矩阵集Cov中某个协方差矩阵
		double x_temp[][]=new double[2][1];   //用于将x变成列向量
		x_temp=MatrixTrans(x);
		for(int i=0;i<a.length;i++){
			u_temp1[0]=u[i];
			u_temp2=MatrixTrans(u_temp1);
			cov_temp=Cov.get(i);
			sum=sum+a[i]*this.Gaussian(x_temp, u_temp2, cov_temp);
		}
		
		for(int i=0;i<a.length;i++){
			u_temp1[0]=u[i];
			u_temp2=MatrixTrans(u_temp1);
			cov_temp=Cov.get(i);
			result[i]=(a[i]*this.Gaussian(x_temp, u_temp2, cov_temp))/sum;
		}
		return result;
	}
	//求x的高斯分布的概率密度，u为均值向量，cov为协方差矩阵
    public double Gaussian(double x[][],double u[][],double cov[][]){
    	//数据x和均值u都是一行两列的矩阵
    	double result;
    	double temp1=0;
    	double temp2=0;
    	double temp3[][];  //接收x-u的转置矩阵
    	double temp4[][];  //接收x-u
    	double temp5[][];  //接收cov的逆
    	double Multiple1[][];
    	double Multiple2[][];
    	temp1=Math.pow(2*3.14,((double)cov.length)/2) * 
    	      Math.pow(this.MatrixDet(cov),0.5);
    	temp3=this.MatrixMinus(x,u);    //求x-u
    	temp4=this.MatrixTrans(temp3);  //求x-u的转置
    	temp5=this.MatrixInverse(cov);  //协方差矩阵的逆矩阵
    	Multiple1=this.MatrixMultiple(temp4,temp5);
    	Multiple2=this.MatrixMultiple(Multiple1,temp3);
    	temp2=(-1)*(0.5)*this.MatrixDet(Multiple2);  //求得最终e的指数值
    	result=(1.0/temp1)*(Math.pow(Math.E,temp2));
    	return result;
    }
    
	//打印矩阵
	public static void MatrixPrint(double x[][]){
		for(int i=0;i<x.length;i++){
			for(int j=0;j<x[0].length;j++){
				System.out.print(x[i][j]+" ");
			}
			System.out.println(" ");
		}
		System.out.println("");
	}
        //两个矩阵相减
        public double[][] MatrixMinus(double x[][],double u[][]){
        	double result[][]=new double[x.length][x[0].length];
        	for(int i=0;i<x.length;i++){
        		for(int j=0;j<x[0].length;j++){
        			result[i][j]=x[i][j]-u[i][j];
        		}
        	}
        	return result;
        }
        
        //寻找一个一位矩阵矩阵中的最小数
        public int SeachforMax(double x[]){
        	double max=x[0];
        	int result=0; //记录下标
        	for(int i=0;i<x.length;i++){
        		if(max<x[i]){
        			max=x[i];
        			result=i;
        		}
        	}
        	return result;
        }
        //矩阵除以一个数
        public double[][] MatrixDivide(double x[][],double n){
        	double result[][]=new double[x.length][x[0].length];
        	for(int i=0;i<x.length;i++){
        		for(int j=0;j<x[0].length;j++){
        			result[i][j]=x[i][j]/n;
        		}
        	}
        	return result;
        }
        //两个矩阵相加
        public double[][] MatrixAdd(double x1[][],double x2[][]){
        	double result[][]=new double[x1.length][x1[0].length];
        	for(int i=0;i<x1.length;i++){
        		for(int j=0;j<x1[0].length;j++){
        			result[i][j]=x1[i][j]+x2[i][j];
        		}
        	}
        	return result;
        }
        //一个数与一个矩阵相乘，返回二维矩阵
        public double[][] MatrixMultiple(double n,double x[][]){
        	double result[][]=new double[x.length][x[0].length];
        	for(int i=0;i<x.length;i++){
        		for(int j=0;j<x[0].length;j++){
        			result[i][j]=n*x[i][j];
        		}
        	}
        	return result;
        }
        
        //一个数与一个矩阵相乘,矩阵是一个一行两列的矩阵,返回一个一维矩阵
        /*public double[] MatrixMultiple(double n,double[][] x){
        	double result[]=new double[x[0].length];
        	for(int i=0;i<x[0].length;i++){
        		result[i]=x[0][i]*n;
        	}
        	return result;
        }*/
	    //两个矩阵相乘,其中矩阵x的列数等于矩阵y的行数,结果为矩阵Multiple
		public double[][] MatrixMultiple(double x[][],double xt[][]){
			double Multiple[][]=new double[x.length][xt[0].length];
			double sum;
			int i=0,j;
			int row=0;
			int temp=0;
			while(true){
				sum=0;
				for(j=0;j<x[0].length;j++){
					sum=sum+x[i][j]*xt[j][temp];
				}
					Multiple[i][row]=sum;
					row++;
					temp++;
				if(row>Multiple[0].length-1){
					row=0;
					temp=0;
					i++;
				}
				if(i>=x.length)
					break;
			}
			return Multiple;
		}
		
	    //求矩阵的转置矩阵
		public double[][] MatrixTrans(double x[][]){
			double xt[][]=new double[x[0].length][x.length];
			for(int i=0;i<x.length;i++){
				for(int j=0;j<x[0].length;j++){
					xt[j][i]=x[i][j];           //将原矩阵的行变为新矩阵的列
				}
			}
			return xt;
		}
		
	    //求矩阵的行列式,使用对角线法则，适用于三阶及以下的行列式
		public double MatrixDet(double a[][]){
			int temp1=0,i1=0,j1=0,count1=0,c1=0;//用于主对角线
			int temp2=a.length-1,i2=0,j2=a.length-1,count2=0,c2=0;//用于从对角线
			double Mul1=1,Mul2=1,sum1=0,sum2=0;
			//当行列式为一阶时
			if(a.length==1 && a[0].length==1){
				return a[0][0];
			}
			//当行列式为二阶时
			if(a.length==2 && a[0].length==2){
				i1=0;j1=0;
				i2=0;j2=1;
			//计算主对角线的乘积的和
			while(true){
				Mul1=Mul1*a[i1%a.length][j1%a.length];
				i1++;j1++;
				if(i1>=a.length){
					sum1=Mul1;
					break;
				}
			}
			while(true){
				Mul2=Mul2*a[i2%a.length][j2%a.length];
				i2++;j2--;
				if(i2>=a.length){
					sum2=Mul2;
					break;
				}
			}
			return sum1-sum2;
			}
			
			//当行列式为三阶时
			if(a.length==3 && a[0].length==3){
			//计算主对角线上的乘积和
			while(true){
				Mul1=Mul1*a[i1%a.length][j1%a.length];
				count1++;c1++;i1++;j1++;
				if(count1>=a.length){
					temp1++;
					j1=temp1;
					sum1=sum1+Mul1;
					Mul1=1;
					count1=0;
				}
				if(c1>=(a.length*a.length)) break;
			}
			//计算从对角线上的乘积和
			while(true){
				Mul2=Mul2*a[i2%a.length][j2];
				i2++;j2--;count2++;c2++;
				if(count2>=a.length){
					temp2--;
					j2=temp2;
					sum2=sum2+Mul2;
					Mul2=1;
					count2=0;
				}
				if(j2<0){
					j2=a.length-1;
				}
				if(c2>=(a.length*a.length)) break;
			}
			}
			return sum1-sum2;
		}
		
	//求余子式,求矩阵D中第i行第j列元素的余子式
	public double[][] MatrixCofactor(double D[][],int i,int j){
		double temp[][]=new double[D.length-1][D.length-1];
		int c2=0,r2=0;
		for(int c1=0;c1<D.length;c1++){
			for(int r1=0;r1<D.length;r1++){
				if(c1!=i && r1!=j){
					temp[c2][r2]=D[c1][r1];
					r2++;
				}
			}
			if(r2>=temp.length){
			c2++;
			r2=0;
			}
		}
		return temp;
	}
	
	//求伴随矩阵
	public double[][] MatrixAdjugate(double D[][]){
		double temp[][]=new double[D.length-1][D.length-1];
		double A=0;
		double AX[][]=new double[D.length][D.length]; //最终求得的伴随矩阵
		for(int i=0;i<D.length;i++){
			for(int j=0;j<D.length;j++){
				temp=this.MatrixCofactor(D,i,j);//求余子式
				A=(Math.pow(-1, (i+j)))*this.MatrixDet(temp);  //求伴随矩阵每个元素的核心公式
				AX[i][j]=A;
			}
		}
		return AX;
	}
	
	//求矩阵的逆
	public double[][] MatrixInverse(double D[][]){
		double DA[][]=new double[D.length][D.length];//用来装所求的伴随矩阵
		double result[][]=new double[D.length][D.length];
		DA=this.MatrixAdjugate(D);  //求矩阵D的伴随矩阵
		for(int i=0;i<D.length;i++){
			for(int j=0;j<D.length;j++){
				result[i][j]=(DA[i][j])/this.MatrixDet(D);  //求逆矩阵的核心公式
			}
		}
		return result;
	}
}
public class DataProcess03_02 {
	public static void main(String[] args) {
		//数据源自西瓜书
		//计算高斯混合聚类
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
             Kmeans kmeans=new Kmeans(dataset);
	}
}
