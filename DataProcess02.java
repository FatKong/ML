import java.util.*;
import java.math.*;
//每个样本的属性类
class Properties{
	int num;          //编号
	String Color;     //色泽
	String Pedicle;   //根蒂
	String Knock;     //敲声
	String texture;   //纹理
	String Navel;     //脐部
	String Touch;     //触感
	boolean result;   //是否好瓜
	ArrayList<String> Prop=null;
	public Properties(){
		super();
	}
    public Properties(int num,String Color,String Pedicle,String Knock,
    		          String texture,String Navel,String Touch,boolean result){
    	Prop=new ArrayList<String>();
    	Prop.add(Color);
    	Prop.add(Pedicle);
    	Prop.add(Knock);
    	Prop.add(texture);
    	Prop.add(Navel);
    	Prop.add(Touch);
    	this.result=result;
    	this.num=num;
    }
	
}
class Node{
	//每个结点包含很多个样本，每个样本有很多属性
	//每个结点有赋予自己属性的方法
	//每个结点有其分支结点的动态数组
	Node parentNode;
	ArrayList<Properties> data=new ArrayList<Properties>();
	ArrayList<Node> nodelist=new ArrayList<Node>();
	public Node(ArrayList<Properties> Come){
		for(int i=0;i<Come.size();i++){
			data.add(Come.get(i));
		}
	}
}
class DecisionTree{
	Node root=null;
	public static boolean Flag[]={false,false,false,false,false,false};
	public DecisionTree(ArrayList<Properties> Come){
		this.CreateTree(Come,root);
	}
	public void CreateTree(ArrayList<Properties> Come,Node parentNode){
		Node node=null;
		if(parentNode==null){
			this.root=new Node(Come);
			root.parentNode=null;
			node=root;
		}else{
		node=new Node(Come);
		node.parentNode=parentNode;
		}
		Properties pro=new Properties();     //指向结点的样本
		int f=0,t=0,co=0,w=0;
		for(int i=0;i<node.data.size();i++){
			pro=node.data.get(i);
			if(pro.result==true){
				t++;
			}else{
				f++;
			}
		} 
		if(t==node.data.size() || f==node.data.size()){
			return;
		} //已经分类好了，结束
		for(int i=0;i<6;i++){
			if(Flag[w]==true){
				co++;
			}
		}
		if(co==6) return;
		ArrayList<Double> gain=new ArrayList<Double>();//信息增益数组，方便以后找出最大的那个作为分支的属性
		                                 //用于记录数组下标
		String temp1;                        //用于指向样本中的属性
		double temp2;
		//boolean Flag[]=new boolean[6];       //设置标志数组，若第i个属性被使用作为分支依据，则置为true
		double max;
		//计算各属性的信息增益
	    for(int j=0;j<6;j++){
	    	temp2=Gain(node,j);
	    	//System.out.println("Gain="+temp2);
	    	gain.add(temp2);
	    }
	    max=0;
	    for(int k=0;k<gain.size();k++){
	    	if(max<gain.get(k) && Flag[k]==false){
	    		max=gain.get(k);
	    		w=k;        //记录下来是哪个属性
	    	}
	    }
	    Flag[w]=true;      //该属性已被使用当做分支属性
	    System.out.println("第"+w+"个属性被当作分支属性");
	    ArrayList<String> Count=new ArrayList<String>();  //记录属性的各个取值
	    Count=ProCountJudge(node,w); 
	    ArrayList<ArrayList<Properties>> big=null;
	    big=new ArrayList<ArrayList<Properties>>();   //用于储存即将要分支的结点
	    int i=0,c=0;
	    String temp3;
	    while(i<Count.size()){
	    	temp1=Count.get(i);
	    	c=0;
	    	ArrayList<Properties> one=new ArrayList<Properties>();
	    	while(c<node.data.size()){
	    		pro=node.data.get(c);       //第c个样本
	    		temp3=pro.Prop.get(w);      //第c个样本的第w个属性
	    		if(temp3.equals(temp1)){
	    			one.add(node.data.get(c));
	    		}
	    		c++;
	    	}
	    	big.add(one);
	    	i++;
	    }
	    for(int j=0;j<big.size();j++){
	    		node.nodelist.add(new Node(big.get(j)));
	    		node.data.remove(big.get(j));
	    		CreateTree(big.get(j),node);
	    }
	}
	
	//计算某个属性的信息增益,样本的第c个属性.
	public double Gain(Node node,int c){
		ArrayList<String> Count=new ArrayList<String>();  //属性值数组
		Count=ProCountJudge(node,c);     //先假设获得第c个属性的取值，Count为接收的属性值数组
		Properties pro=new Properties(); //接收结点中个某个样本
		String temp1=null;               //用于遍历结点的样本
		String temp2=null;               //用于遍历属性值数组
		int i=0;                         //用于temp2,遍历Count数组
		int j=0;                         //用于temp1,遍历结点每个样本
		int sum=node.data.size();        //这个结点的样本总数
		double pos=0;                       //统计正例的个数
		double neg=0;                       //统计反例的个数
		double Ent[]=new double[Count.size()];  //收集每个属性值的信息熵
		double sample[]=new double[Count.size()];
		double gain;                        //这个属性的信息增益
		while(i<Count.size()){          
			temp2=Count.get(i);         //第i个属性值
			pos=0;neg=0;                //重置正反例的值
			for(j=0;j<node.data.size();j++){   //遍历结点的所有样本
				pro=node.data.get(j);          //第j个样本
				temp1=pro.Prop.get(c);         //第j个样本的第c个属性
				if(temp1.equals(temp2)){              //寻找到了符合的属性值
					if(pro.result==true){      //计算正例的个数
						pos++;               //大概是这个部分再加个判断，
						                    //因为可能某些属性的取值导致结果都是正例或者反例，公式不适用
					}else{
						neg++;
					}
				}
			}
			
			if(pos !=0 && neg !=0){
			Ent[i]=(-1)*((pos/sum)*((Math.log((pos/sum))/Math.log(2)))+
					     (neg/sum)*((Math.log((neg/sum))/Math.log(2))));
			}
			if(pos==0){ //如果该属性对应的正例全为0
				Ent[i]=(-1)*((neg/sum)*((Math.log((neg/sum))/Math.log(2))));
			}
			if(neg==0){
				Ent[i]=(-1)*((pos/sum)*((Math.log((pos/sum))/Math.log(2))));
			}
	
			sample[i]=pos+neg;         //统计每个属性值分别有多少个样本
			i++;
		}
		
		
		//计算该结点中所有正例反例分别是多少
		pos=0;neg=0; //重置所需要变量
		double temp3=0;      //用来计算信息增益的中间变量
		for(int k=0;k<Count.size();k++){
			temp3=temp3+(sample[k]/sum)*Ent[k];
		}
		double Ent_Node;    //这个结点正与反例的信息增益
		for(int k=0;k<node.data.size();k++){    //遍历结点所有样本，分别找出正反例的总数
			pro=node.data.get(k);               //第k个样本
			if(pro.result==true){
				pos++;
			}
			if(pro.result==false){
				neg++;
			}
		}
		Ent_Node=(-1)*((pos/sum)*(Math.log(pos/sum)/Math.log(2))+
				       (neg/sum)*(Math.log(neg/sum)/Math.log(2)) );
		gain=Ent_Node-temp3;
		return gain;
	}
	
	
	//传入这个结点，结点里包含样本，计算样本的第c个属性的取值个数,返回属性取值数组
	public ArrayList<String> ProCountJudge(Node node,int c){
		Properties pro=new Properties();     //接收结点中个某个样本
		ArrayList<String> Count=new ArrayList<String>();  //统计数组，属性取值存于此
		boolean flag=true;                //设置标志数组，若发现Count数组中有取值与样本相同,则置为true
		String temp2=null;                 //用来指向统计数组里的属性取值名字
		for(int i=0;i<node.data.size();i++){   //循环取出每个样本的第c个属性
			pro=node.data.get(i);          //取出结点的第i个样本
			String temp1=pro.Prop.get(c);  //第i个样本的第c个属性,即所要求的属性
			flag=true;                    //重置标志变量
			for(int j=0;j<Count.size();j++){  //用temp2对Count数组遍历
				temp2=Count.get(j);         //指向统计数组里的属性取值名字
				if(temp2.equals(temp1)){
					flag=false;
					break;
				}
			}
			if(flag==true){        //若Count数组中没有属性值与temp1相等,则将其加入到Count数组中，取值个数+1
				Count.add(temp1);
			}
		}
		return Count;
	}
}
public class DataProcess02_02 {
	public static void main(String[] args) {
		ArrayList<Properties> Come=new ArrayList<Properties>();
		//数据部分摘自周志华《机器学习》76页
		Properties pro1=new Properties(1,"青绿","蜷缩","浊响","清晰","凹陷","硬滑",true);
		Properties pro2=new Properties(2,"乌黑","蜷缩","沉闷","清晰","凹陷","硬滑",true);
		Properties pro3=new Properties(3,"乌黑","蜷缩","浊响","清晰","凹陷","硬滑",true);
		Properties pro4=new Properties(4,"青绿","蜷缩","沉闷","清晰","凹陷","硬滑",true);
		Properties pro5=new Properties(5,"浅白","蜷缩","浊响","清晰","凹陷","硬滑",true);
		Properties pro6=new Properties(6,"青绿","稍蜷","浊响","清晰","稍凹","软粘",true);
		Properties pro7=new Properties(7,"乌黑","稍蜷","浊响","稍糊","稍凹","软粘",true);
		Properties pro8=new Properties(8,"乌黑","稍蜷","浊响","清晰","稍凹","硬滑",true);
		Properties pro9=new Properties(9,"乌黑","稍蜷","沉闷","稍糊","稍凹","硬滑",false);
		Properties pro10=new Properties(10,"青绿","硬挺","清脆","清晰","平坦","软粘",false);
		Properties pro11=new Properties(11,"浅白","硬挺","清脆","模糊","平坦","硬滑",false);
		Properties pro12=new Properties(12,"浅白","蜷缩","浊响","模糊","平坦","软粘",false);
		Properties pro13=new Properties(13,"青绿","稍蜷","浊响","稍糊","凹陷","硬滑",false);
		Properties pro14=new Properties(14,"浅白","稍蜷","沉闷","稍糊","凹陷","硬滑",false);
		Properties pro15=new Properties(15,"乌黑","稍蜷","浊响","清晰","稍凹","软粘",false);
		Properties pro16=new Properties(16,"浅白","蜷缩","浊响","模糊","平坦","硬滑",false);
		Properties pro17=new Properties(17,"青绿","蜷缩","沉闷","稍糊","稍凹","硬滑",false);
		Come.add(pro1);
		Come.add(pro2);
		Come.add(pro3);
		Come.add(pro4);
		Come.add(pro5);
		Come.add(pro6);
		Come.add(pro7);
		Come.add(pro8);
		Come.add(pro9);
		Come.add(pro10);
		Come.add(pro11);
		Come.add(pro12);
		Come.add(pro13);
		Come.add(pro14);
		Come.add(pro15);
		Come.add(pro16);
		Come.add(pro17);
		DecisionTree tree=new DecisionTree(Come);
		Order(tree.root);
	}
	
	//遍历这棵树
	public static void Order(Node root){
			Properties pro=new Properties();//用于指向样本
			int temp;                    //中途输出结点的各种属性
			int i=0;
			if(root.nodelist.size()==0) return;  //递归结束
			while(i<root.data.size()){
				pro=root.data.get(i);
				temp=pro.num;
				System.out.print(temp+" ");
				i++;
			}
			i=0;
			while(i<root.nodelist.size()){
				//System.out.println("1");  //记录递归了多少次
				Order(root.nodelist.get(i));
				i++;
			}
	}
}
