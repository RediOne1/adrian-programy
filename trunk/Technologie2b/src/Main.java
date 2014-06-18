import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Main {
	static int[][] MaciezNatezen;
	static double maxT;
	static int sukces = 0;
	static int porazki = 0;
	static int m;
	static int gg;
	
	static double opoznienie(SimpleWeightedGraph <Integer, Krawedz> g){
		SimpleWeightedGraph <Integer, Krawedz> g1;
    	g1 = (SimpleWeightedGraph<Integer, Krawedz>) g.clone();
    	
    	
		
        

        double t1 = 0;

        // obliczam t i sprawdzam, czy dla każdej krawędzi c >= a

        Set<Krawedz> edges1 = g1.edgeSet();

        boolean failed1 = false;

        for (Krawedz me: edges1) {

            if ((double)me.getPrzepustowosc()/m - me.getPrzeplyw() <= 0)
                failed1 = true;
            // sprawdzam czy pakiety przejdą przez łącze
        }

        // sumowanie po wszystkich krawędziach - ze wzoru T = 1/G * SUM_e( a(e)/(c(e)/m - a(e)) )


        if (!failed1) {
        	//System.out.println(ilosc+"ilosc");
        	//System.out.println(p+"prawdo");
            // jeśli dostępna jest wystarczająca przepustowość, to sprawdzam opóźnienie
        		t1=0.0;
            for (Krawedz me: edges1) {
            	
            		//System.out.println(me.toString() + " : " + me.getA());
            	
            		t1 += (double) ((double)me.getPrzeplyw()/( (double) me.getPrzepustowosc()/m - (double) me.getPrzeplyw()));
            	

            }
            
	            t1 = (double) t1/gg;
	            
        	
            //System.out.println("opoznienie: "+t1);
            return t1;
            
           
           

        }
        
        return -1.0;
    
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File graphFile = new File("graf.txt");
        BufferedReader brGraph = new BufferedReader( new FileReader(graphFile) );

        File nFile = new File("n.txt");
        BufferedReader brN = new BufferedReader( new FileReader(nFile) );


        /*
         * Czytanie grafu
         */

        // Pierwsza linijka - dane wejściowe
        int e = 15;     //ilość krawędzi
        maxT = 0.04;  //dopuszczalne maksymalne opóźnienie pakietu
        m = 32;          // średnia wielkość pakietu w bitach
        double p = 0.9; 	// prawdopodobienstwo
        int ilosc = 1000;
        //System.out.println(p);
        
        System.out.println("Prawdopodobienstwo: "+p);
        System.out.println("średnia wielkość pakietu w bitach: "+m);
        System.out.println("dopuszczalne maksymalne opóźnienie pakietu "+maxT);

        //graf
        SimpleWeightedGraph <Integer, Krawedz> g =
                new SimpleWeightedGraph<Integer, Krawedz>(Krawedz.class);

        int i, j;
        String[] line;
        int v1, v2, pa;
        double pr;

        for (i=0; i<e; i++) {

            line = brGraph.readLine().split(" ");

            v1 = Integer.parseInt(line[0]);
            v2 = Integer.parseInt(line[1]);
            pa = Integer.parseInt(line[2]);
            //pr = Double.parseDouble(line[2]);

            g.addVertex(v1);
            g.addVertex(v2);

            Krawedz ed = new Krawedz();
            ed.setPrzeplyw(0);
            ed.setPrzepustowosc(pa);
            g.addEdge(v1, v2, ed);

        }

        // wczytywanie tablicy N

        int v = 10;
        MaciezNatezen = new int[v][v];

        for (i=0; i<v; i++) {
            line = brN.readLine().split(" ");
            for (j=0; j<v; j++) {
                MaciezNatezen[i][j] = Integer.parseInt(line[j]);
            }
        }
        
        ConnectivityInspector<Integer, Krawedz> ci = new ConnectivityInspector<Integer, Krawedz>(g);
        if (ci.isGraphConnected()){
	        	
		        gg = 0;
		
		        for (i=1; i<=v; i++) {
		            for (j=1; j<=v; j++) {
		
		                gg += MaciezNatezen[i-1][j-1];
		
		                // dla każdego n(i,j)  szukam najkrótszej ścieżki i zwiększam a na krawędzi o n(i,j)
		                List<Krawedz> path = DijkstraShortestPath.findPathBetween(g, i, j);
		
		                for(Krawedz med: path) {
		                    med.setPrzeplyw(med.getPrzeplyw()+MaciezNatezen[i-1][j-1]);
		                }
		            }            
		        }
		
		       double t = opoznienie(g);
		       System.out.println("opoznienie: "+t);
		        
		       for(i = 0; i<ilosc; i++){
		    	    SimpleWeightedGraph <Integer, Krawedz> g1;
		       		g1 = (SimpleWeightedGraph<Integer, Krawedz>) g.clone();
		       		Random r = new Random();
		       		Set<Krawedz> edges = g.edgeSet();
		       		for (Krawedz me: edges) {
		            	if(r.nextDouble() <= p){
		            		g1.removeEdge(me);
		            	}
		
		            }
		       		t = opoznienie(g1);
			        //System.out.println(t);
		       		if(t >= 0){
			       		if(t < maxT){
			       			sukces++;
			       		}
			       		else
			       			porazki++;
		       		}
		       		else
		       			porazki++;
		       		
		       }
        }
        
        System.out.println();
        System.out.println("ilosc prob: "+ilosc);
        System.out.println("sukcesy "+sukces);
        System.out.println("porazki: "+porazki);
        double procent = (sukces*100)/ilosc;
        System.out.println("Niezawodność "+procent+"%");
	}

}
