import java.util.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.util.*;
import org.jgrapht.alg.connectivity.*;
import java.util.function.*;

public class Mst {

    public static double getMST(Graph<Integer, DefaultEdge> graph)
	{
        ConnectivityInspector<Integer, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(graph);
        Set<DefaultEdge> edges = graph.edgeSet();
        List<DefaultEdge> mainList = new ArrayList<DefaultEdge>();
        mainList.addAll(edges);
        Comparator<DefaultEdge> comparator = (h1, h2) -> (graph.getEdgeWeight(h2) > graph.getEdgeWeight(h1)) ? 1 : -1;
        Collections.sort(mainList, comparator);
        int edgeCount = mainList.size();
        int vertexCount = graph.vertexSet().size();
        int removals = 0;
        int i = 0;
        
        while (removals < (edgeCount - vertexCount + 1)) {
            DefaultEdge curr = mainList.get(i);    
            int src = graph.getEdgeSource(curr);
            int dest = graph.getEdgeTarget(curr);
            graph.removeEdge(curr);
            if (!connectivityInspector.isConnected()) {
                graph.addEdge(src, dest);
            } else {
                removals++;
            }
            i++;
        }
        Set<DefaultEdge> Mstedges = graph.edgeSet();
        double mstWeight = 0;
        for(DefaultEdge e: Mstedges) {
            mstWeight+= graph.getEdgeWeight(e);
        }
        return mstWeight;
	}

    public static Graph<Integer, DefaultEdge> CreateRandomGraph(Graph<Integer, DefaultEdge> graph, int numNodes) {
        List<Integer> ll = new LinkedList<>();
        ll.add(1);
        int curr_count = 1;
        graph.addVertex(1);
        Random rand = new Random();
        while(curr_count < numNodes) {
            int curr = ll.get(0);
            int a = rand.nextInt(10) + 1;
            for(int i =0; i<a && i+curr_count<=numNodes; i++) {
                curr_count++;
                ll.add(curr_count);
                graph.addVertex(curr_count);
                graph.addEdge(curr, curr_count);
                double weight = rand.nextDouble();
                graph.setEdgeWeight(curr, curr_count, weight);
            }
            ll.remove(0);
        }
        int b = rand.nextInt(9)+1;
        for(int i=1; i<=b; i++) {
            graph.addEdge(i, i+1+rand.nextInt(numNodes/2));            
        }
        return graph;
    } 
    public static void main(String args[]) {
        
        int vertices = 10000;
        Supplier<Integer> vSupplier = new Supplier<Integer>()
        {
            private int id = 0;
            @Override
            public Integer get()
            {
                return id++;
            }
        };
        Graph<Integer, DefaultEdge> graph = new SimpleWeightedGraph<>(vSupplier, SupplierUtil.createDefaultEdgeSupplier());
    /** 
            GnpRandomGraphGenerator<Integer, DefaultEdge> grGen = new GnpRandomGraphGenerator<Integer, DefaultEdge>(100, 1);
            System.out.println("Generating the graph");
            grGen.generateGraph(graph);
            System.out.println("Generated the graph of size vertices: " + graph.vertexSet().size() + " and edges: " + graph.edgeSet().size());
    */
        graph = CreateRandomGraph(graph, vertices);

    /*      graph.addVertex(1);
            graph.addVertex(2);
            graph.addVertex(3);
            graph.addVertex(4);
            graph.addVertex(5);
            graph.addVertex(6);

            DefaultEdge e1 = graph.addEdge(1,2);
            DefaultEdge e2 = graph.addEdge(1,3);
            DefaultEdge e3 = graph.addEdge(2,4);
            DefaultEdge e4 = graph.addEdge(2,5);
            DefaultEdge e5 = graph.addEdge(3,6);
            DefaultEdge e6 = graph.addEdge(3,4);

            graph.setEdgeWeight(e1, 25.0);
            graph.setEdgeWeight(e2, 2.4);
            graph.setEdgeWeight(e3, 1.6);
            graph.setEdgeWeight(e4, 3.3);
            graph.setEdgeWeight(e5, 4.5);
            graph.setEdgeWeight(e6, 25.0);
    */        
        long startTime = System.nanoTime();
        double mstWeight = getMST(graph);
        long endTime = System.nanoTime();
        System.out.println("TotalMstWeight = " + mstWeight);
        System.out.println("Total Execution time: " + (endTime - startTime)/1000f + " micro seconds"+ " numNodes: " + graph.vertexSet().size() + ", numEdges: " + graph.edgeSet().size());
            
    }
}
