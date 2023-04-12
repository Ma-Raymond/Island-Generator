package graphFiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Path Interface used by ShortestPath
 */
public interface Path {
    public List<Integer> dijkstra(Graph graph,Integer node);

}
