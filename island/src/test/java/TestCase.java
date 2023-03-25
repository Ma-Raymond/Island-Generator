
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.files.IslandGen;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class TestCase {
    @Test
    public void meshIsNotNull() throws IOException {
        IslandGen generator = new IslandGen();
        Structs.Mesh aMesh = new MeshFactory().read("../generator/sample.mesh");

        // These are String the User is able to manipulate
        String seed = "";
        String shape = "";
        String elevType = "";
        String maxNumLakes = "";
        String rivers = "";
        String aquifers = "";
        String soil = "";
        String biome = "";


        // These are Strings the user is not able to Manipulate
        String elevationStartIdx = "";
        String lakeStartIdx = "";
        String riverStartIdx = "";
        String aquiferStartIdx = "";

        Structs.Mesh myMesh = generator.generate(aMesh, seed, shape, elevType, elevationStartIdx, maxNumLakes, lakeStartIdx, rivers, riverStartIdx, aquifers, aquiferStartIdx, soil, biome);
        assertNotNull(myMesh);
    }
}
