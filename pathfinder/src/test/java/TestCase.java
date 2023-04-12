
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import graphFiles.Graph;
import graphFiles.GraphGen;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TestCase {

    @Test
    public void negativeWeightNode() throws IOException {
        GraphGen gen = new GraphGen();
        Integer negativeTest = -1;
        Integer testValue = gen.getWeight(negativeTest);
        assertNotNull(testValue);
    }
    @Test
    public void tooBigWeightNode() throws IOException {
        GraphGen gen = new GraphGen();
        Integer tooBig = 1000000000;
        Integer testValue = gen.getWeight(tooBig);
        assertNotNull(testValue);
    }

    @Test
    public void negativeDijkstraNode() throws IOException {
        GraphGen gen = new GraphGen();
        Integer negativeTest = -1;
        List<Integer> test = gen.getDijkstras(negativeTest);
        assertNotNull(test);
    }
    @Test
    public void tooBigDijkstraNode() throws IOException {
        GraphGen gen = new GraphGen();
        Integer tooBig = 1000000000;
        List<Integer> test = gen.getDijkstras(tooBig);
        assertNotNull(test);
    }
    @Test
    public void unConnectedVertices() throws IOException {
        GraphGen gen = new GraphGen();
        Structs.Mesh aMesh = new MeshFactory().read("../generator/sample.mesh");

        gen.generate(aMesh);
        // The Idea here is that node 9 and 999 are NOT connected at all.
        // Explanation 9 is a centroid, 999 is apart of the island, therefore they are not connected
        assertNotNull(gen.getShortestPath(gen.getDijkstras(9),9,999));
    }
    @Test
    public void connectedVertices() throws IOException {
        GraphGen gen = new GraphGen();
        Structs.Mesh aMesh = new MeshFactory().read("../generator/sample.mesh");

        gen.generate(aMesh);
        assertNotNull(gen.getShortestPath(gen.getDijkstras(999),1000,999));
    }


}
