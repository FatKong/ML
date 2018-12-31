public class MLR {
	 public static double[] mlr(double X[][],double Y[]){
		 double XT[][]=new double[X[0].length][X.length];
			double Multiple[][]=new double[XT.length][X[0].length];
			double temp1[][]=new double[XT.length][X[0].length];    //接收逆矩阵
			double temp2[][]=new double[XT.length][X[0].length];
			double w[]=new double[temp2.length];
			double Z[]=new double[XT.length];
			
			MatrixTrans(X,XT); //求X的转置矩阵XT，得XT
			MatrixMultiple(XT,X,Multiple);   //XT 与 X相乘,结果为Multiple
			temp1=MatrixInverse(Multiple);   //(XT * X)^(-1) 即求逆
			MatrixMultiple(temp1,XT,temp2);  //(XT * X)^(-1)*XT,结果为temp2
		    MatrixMultiple(XT,Y,Z);          //求XT * Y ,结果为Z
		    MatrixMultiple(temp2,Z,w);       //求得最后的回归方程系数w
		    
		    return w;
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
		
		//求矩阵的转置矩阵
		public static void MatrixTrans(double x[][],double xt[][]){
			for(int i=0;i<x.length;i++){
				for(int j=0;j<x[0].length;j++){
					xt[j][i]=x[i][j];           //将原矩阵的行变为新矩阵的列
				}
			}
		}
		
		//两个矩阵相乘，其中y矩阵是列向量，结果z也是列向量
		public static void MatrixMultiple(double x[][],double y[],double z[]){
			int i=0,j=0;
			double sum=0;
			while(true){
				sum=0;
				for(j=0;j<x[0].length;j++){
					sum=sum+x[i][j]*y[j];
				}
				z[i]=sum;
				i++;
				if(i>=x.length){
					break;
				}
			}
		}
		
		//两个矩阵相乘,其中矩阵x的列数等于矩阵y的行数,结果为矩阵Multiple
		public static void MatrixMultiple(double x[][],double xt[][],double Multiple[][]){
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
		}
		
		//求矩阵的行列式,使用对角线法则，适用于三阶及以下的行列式
		public static double MatrixDet(double a[][]){
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
		public static double[][] MatrixCofactor(double D[][],int i,int j){
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
		public static double[][] MatrixAdjugate(double D[][]){
			double temp[][]=new double[D.length-1][D.length-1];
			double A=0;
			double AX[][]=new double[D.length][D.length]; //最终求得的伴随矩阵
			for(int i=0;i<D.length;i++){
				for(int j=0;j<D.length;j++){
					temp=MatrixCofactor(D,i,j);//求余子式
					A=(Math.pow(-1, (i+j)))*MatrixDet(temp);  //求伴随矩阵每个元素的核心公式
					AX[i][j]=A;
				}
			}
			return AX;
		}
		
		//求矩阵的逆矩阵
		public static double[][] MatrixInverse(double D[][]){
			double DA[][]=new double[D.length][D.length];//用来装所求的伴随矩阵
			double result[][]=new double[D.length][D.length];
			DA=MatrixAdjugate(D);  //求矩阵D的伴随矩阵
			for(int i=0;i<D.length;i++){
				for(int j=0;j<D.length;j++){
					result[i][j]=(DA[i][j])/MatrixDet(D);  //求逆矩阵的核心公式
				}
			}
			return result;
		}
}
