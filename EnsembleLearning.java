import java.util.*;
public class EnsembleLearning {
	public static void main(String[] args) {
		//数据源自西瓜书 P89，含糖率与密度，还有是否为好瓜的关系
		            // 含糖率        密度
		double X[][]={{0.697,0.460,1},
				      {0.774,0.376,1},
				      {0.634,0.264,1},
				      {0.608,0.318,1},
				      {0.556,0.215,1},
				      {0.403,0.237,1},
				      {0.481,0.149,1},
				      {0.437,0.211,1},
				      {0.666,0.091,1},
				      {0.243,0.267,1},
				      {0.245,0.057,1},
				      {0.343,0.099,1},
				      {0.639,0.161,1},
				      {0.657,0.198,1},
				      {0.360,0.370,1},
				      {0.593,0.042,1},
				      {0.719,0.103,1}};
		double Y[]={1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0};//1表示好瓜，0表示坏瓜
		Random random=new Random();
		
		//将产生三个学习器，每个学习器使用对数几率回归
		//之后将从数据集中选出一组数据X[5]，来进行验证，看看分类成好瓜还是坏瓜
		
		int result[]=new int[3]; //三个学习器的最终结果
		
		//这是第一个学习器
		double x_temp1[][]=new double[5][];//每次提取5个数据
		double y_temp1[]=new double[5];
		double w1[]=new double[3];
		for(int i=0;i<5;i++){
			int rand=random.nextInt(16); //从0-16中随机产生一个数，即随机提取数据
			x_temp1[i]=X[rand];
			y_temp1[i]=Y[rand];
		}
		w1=MLR.mlr(x_temp1, y_temp1); //调用已经写好的多元线性回归函数计算出w权重
		//System.out.println(w[0]+" "+w[1]+" "+w[2]);
		double z1=0;
		double y_result1=0;
		z1=w1[0]+w1[1]*X[5][0]+w1[2]*X[5][1];
		y_result1=1.0/(1+Math.pow(Math.E,-z1));
		if(y_result1>=0.5){
			result[0]=1;
		}else{
			result[0]=0;
		}
		
		//这是第二个学习器
		double x_temp2[][]=new double[5][];//每次提取5个数据
		double y_temp2[]=new double[5];
		double w2[]=new double[3];
		for(int i=0;i<5;i++){
			int rand=random.nextInt(16);
			x_temp2[i]=X[rand];
			y_temp2[i]=Y[rand];
		}
		w2=MLR.mlr(x_temp2, y_temp2); //调用已经写好的多元线性回归函数计算出w权重
		double z2=0;
		double y_result2=0;
		z2=w2[0]+w2[1]*X[5][0]+w2[2]*X[5][1];
		y_result2=1.0/(1+Math.pow(Math.E,-z2));
		if(y_result2>=0.5){
			result[1]=1;
		}else{
			result[1]=0;
		}
		
		//这是第三个学习器
		double x_temp3[][]=new double[5][];//每次提取5个数据
		double y_temp3[]=new double[5];
		double w3[]=new double[3];
		for(int i=0;i<5;i++){
			int rand=random.nextInt(16);
			x_temp3[i]=X[rand];
			y_temp3[i]=Y[rand];
		}
		w3=MLR.mlr(x_temp3, y_temp3); //调用已经写好的多元线性回归函数计算出w权重
		double z3=0;
		double y_result3=0;
		z3=w3[0]+w3[1]*X[5][0]+w3[2]*X[5][1];
		y_result3=1.0/(1+Math.pow(Math.E,-z3));
		if(y_result3>=0.5){
			result[2]=1;
		}else{
			result[2]=0;
		}
		
		//使用投票法，判决最终结果
		int y=0,n=0;
		for(int i=0;i<result.length;i++){
			if(result[i]==1){
				y++;
			}else{
				n++;
			}
		}
		if(y>n){
			System.out.println("这是一个好瓜");
		}else{
			System.out.println("这是一个坏瓜");
		}
	}
}
