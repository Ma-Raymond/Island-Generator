import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import graphFiles.GraphGen;
import org.apache.commons.cli.*;

import java.io.IOException;

public class Main1 {
    public static void main(String[] args) throws IOException {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("I", true, "Input");
        options.addOption("O", true, "Output");
        String input = null;
        String output = null;

        try{
            CommandLine commandline = parser.parse(options, args);
            if (commandline.hasOption("I")) {
                input = commandline.getOptionValue("I");
            }
            if (commandline.hasOption("O")) {
                output = commandline.getOptionValue("O");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        // Old Mesh to write on
        System.out.println(input);
        Mesh aMesh = new MeshFactory().read(input);

        // Graph Generation
        GraphGen gen = new GraphGen();
        Mesh myMesh = gen.generate(aMesh);

        // Outputing to new Mesh object
        MeshFactory factory = new MeshFactory();
        factory.write(myMesh, output);
    }
}
// java -jar pathfinder.jar -I ../island/island.mesh -O path.mesh