import java.util.*;
import org.jgrapht.*;
import org.jgrapht.generate.*;
import org.jgrapht.graph.*;
import org.jgrapht.util.*;
import java.util.function.*;



public class CycleFinder {
    
    public static boolean hasCycleDFS(Graph<Integer, DefaultEdge> graph){
         Set<Integer> visited = new HashSet<Integer>();
         for(int vertex : graph.vertexSet()){
             if(visited.contains(vertex)){
                 continue;
             }
             boolean flag = hasCycleDFSUtil(vertex, visited, null, graph);
             if(flag){
                 return true;
             }
         }
         return false;
     }
    
    public static boolean hasCycleDFSUtil(Integer vertex, Set<Integer> visited, Integer parent, Graph<Integer, DefaultEdge> graph){
        visited.add(vertex);
        if(graph.degreeOf(vertex) >= 1) {
            List<Integer> adjList = Graphs.neighborListOf(graph, vertex); 
            for(Integer adj : adjList){ 
                if(adj.equals(parent)){
                    continue;
                }
                if(visited.contains(adj)){
                    System.out.print(vertex+"->");
                    return true;
                }
                boolean hasCycle = hasCycleDFSUtil(adj,visited,vertex, graph);
                if(hasCycle){
                    System.out.print(vertex+"->");
                    return true;
                }
            }
        }
        return false;
    }

    public static Graph<Integer, DefaultEdge> makeWorstCaseCycleGraph(Graph<Integer, DefaultEdge> graph, int numNodes) {
        
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
            }
            ll.remove(0);
        }
        return graph;
    }

    public static Graph<Integer, DefaultEdge> makeRandomGraph(Graph<Integer, DefaultEdge> graph, int numNodes, double j) {
        GnpRandomGraphGenerator<Integer, DefaultEdge> grGen = new GnpRandomGraphGenerator<Integer, DefaultEdge>(numNodes, j);
        grGen.generateGraph(graph);
        return graph;
    }

    public static void main(String args[]) {
        
        Supplier<Integer> vSupplier = new Supplier<Integer>()
        {
            private int id = 0;

            @Override
            public Integer get()
            {
                return id++;
            }
        };
        double j = 0.001;
        int vertices = 10000;
        Graph<Integer, DefaultEdge> graph = new SimpleGraph<>(vSupplier, SupplierUtil.createDefaultEdgeSupplier(), false);
                     
        System.out.println("Generating the graph");
        // below line is to make a tree for worst cse calculations (as no cycle is present)
        //graph = makeWorstCaseCycleGraph(graph, vertices);
        
        graph = makeRandomGraph(graph, vertices, j);
        System.out.println("Generated the graph of size vertices: " + graph.vertexSet().size() + " and edges: " + graph.edgeSet().size());
          
        
        System.out.println("\nFinding the cycle:");
        long startTime = System.nanoTime();
        boolean a = hasCycleDFS(graph);
        long endTime = System.nanoTime();
        if(!a) System.out.println("no cycle found");
        System.out.println("\nTotal Execution time: " + (endTime - startTime)/1000f + " micro seconds for n = " + vertices + " and edges = " + graph.edgeSet().size());    
        
    }        
}

