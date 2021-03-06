import java.io.*;

/*
 * 今の場所から最も近い場所に進んでいく方法。
 * その後、交差があるか調べ、交差があったら交差しないようにする。
 */

public class TCP {
	static int n,route[];
	static double distance[][];
	static double Length;

    /** 出力 と　ファイル書き込み*/
    private static void printTour(String outputfile){
    	try{
    	//ファイル書き込み
    	FileWriter fw = new FileWriter(outputfile);
    	BufferedWriter bw = new BufferedWriter(fw);
    	
    	bw.write("index");
    	bw.newLine();
    	
        System.out.print("ルート =( ");
        for (int i = 0; i < n; i++) {
        	System.out.print(route[i] + " ");
        	bw.write(Integer.toString(route[i]));
        	bw.newLine();
        }
        System.out.println(")");
        System.out.println("距離 = " + Length);
        bw.close();
    	}catch(IOException e){
			System.out.println("Error");
		}
    	
    }

    /*greedy法
     * スタート地点は0
     * */
    public static void Tour() {
        
        boolean used[] = new boolean[n];
        for (int i = 0; i < n; i++) used[i] = false;
        
        int current = 0; //はじまり
        for (int i = 0; i < n; i++) {
        	route[i] = current;  
        	used[current] = true;
        	int min_j = 0;
        	double min = Double.MAX_VALUE;
        	for (int j = 0; j < n; j++){
        		if (!used[j] && distance[current][j] < min) {
        			min = distance[current][j];  
        			min_j = j;
        		}
        	}
        	current = min_j;  // まだ回ってない場所の最も近い場所
        }
        Length = 0;  // 距離
        for (int i = 0; i < n; i++){
        	Length += distance[route[i]][route[(i + 1) % n]];
        }
        	       
        
    }
    
    /*交差する枝をないようにする*/
    private static boolean notcross(final int a) {
        int a1 = route[a % n];
        int a2 = route[(a + 1) % n];
        for (int k = a + 2; k < a + n - 1; k++) {
            int b1 = route[k % n];  
            int b2 = route[(k + 1) % n];
            double dif = (distance[a1][b1] + distance[a2][b2])- (distance[a1][a2] + distance[b1][b2]) ;
            if (dif < 0) {  // 解を発見したら
                Length += dif;
                System.out.println("距離 : " + Length);
                for (int i = 0; i < (k - a) / 2; i++) {  // 実現できたら
                    int swap = route[(a + 1 + i) % n];
                    route[(a + 1 + i) % n] = route[(k - i) % n];
                    route[(k - i) % n] = swap;
                }
                return true;  
            }
        }
        return false;  // 実現できなかったらfalse
    }




    public static double[][] readfile(String inputfile) throws IOException{
    	//try{
    	FileReader fr = new FileReader(inputfile);
    	BufferedReader br = new BufferedReader(fr); 
    	//final int n=5;
    	//final int n=8;
    	//final int n=16;
    	//final int n=64;
    	//final int n=128;
    	//final int n=512;
    	final int n=2048;

    	
        System.out.println(n);
        double[] x = new double[n];
        double[] y = new double[n];
        double[][] distance = new double[n][n];
        
      //読み込んだファイルを１行ずつ処理する
        String line="";
        String[] str=new String[2];
        line=br.readLine();
        int i=0;
        while((line=br.readLine())!=null) {
            //区切り文字","で分割する
            str = line.split(",",0);
            x[i]=Double.parseDouble(str[0]);
            y[i]=Double.parseDouble(str[1]);
            for (int j = 0; j < i; j++) {
                distance[i][j] = distance[j][i] =Math.sqrt(Math.pow(x[i] - x[j],2)+ Math.pow(y[i] - y[j],2));
            }
            i++;
        }
        br.close();

        return distance;
    }

    public static void main(String[] args) throws IOException{
        distance = readfile(args[0]);  
        n = distance.length;  
        route = new int[n];  
        Tour();
        System.out.println("nearest neighbor法");  
        printTour(args[1]);
        for (int i = 0,l=0; i < l + n; i++){  // 交差がないように
            if (notcross(i)){
            	l = i + 1;
            }
        }
        System.out.println("改善"); //最終的な解を出力 
        printTour(args[1]);
    	
    }
}
