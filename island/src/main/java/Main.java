import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.files.IslandGen;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String input = args[0];
        String output = args[1];
        CommandLineParser parser = new DefaultParser();

        // Old Mesh to write on
        Mesh aMesh = new MeshFactory().read(input);

        // Island Generation
        IslandGen gen = new IslandGen();
        Mesh myMesh = gen.generate(aMesh,"Circle");

        // Outputing to new Mesh object
        MeshFactory factory = new MeshFactory();
        factory.write(myMesh, output);
    }
}
// java -jar island.jar ../generator/sample.mesh island.mesh