import ca.mcmaster.cas.se2aa4.a2.generator.DotGen;
import ca.mcmaster.cas.se2aa4.a2.generator.IrregMeshGen;
import ca.mcmaster.cas.se2aa4.a2.generator.MeshGen;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("X", false, "Toggles Debug Mode");

        String Mode = "";
        try {
            CommandLine commandline = parser.parse(options, args);
            if (commandline.hasOption("X")){
                Mode = "true";
            }

        }
        catch (ParseException e) {
            System.out.println("Error Occured within Parser");
        }

        // Step 1 -----------
        //        DotGen generator = new DotGen();
        //        Mesh myMesh = generator.generate();

        // Step 2 ----------- OOP Approach w/ Neighbours and Centroids and Debug Mode
        //         MeshGen generator = new MeshGen();
        //        Mesh myMesh = generator.generate(Mode);

        // Step 3 -----------
        IrregMeshGen gen = new IrregMeshGen();
        Mesh myMesh = gen.generate();

        MeshFactory factory = new MeshFactory();
        factory.write(myMesh, args[0]);
    }

}
