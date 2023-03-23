import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.files.IslandGen;
import org.apache.commons.cli.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("I", true, "Input");
        options.addOption("O", true, "Output");
        options.addOption("S", true, "Shape");
        options.addOption("E", true, "Elevation Type");
        options.addOption("L", true, "Number of Lakes");
        options.addOption("h", "help", false, "Command information");
        String input = null;
        String output = null;
        String shape = null;
        String elevType = null;
        String maxNumLakes = "0";

        //turn string into int
        int intMaxNumLakes = Integer.parseInt(maxNumLakes);

        try {
            CommandLine commandline = parser.parse(options, args);
            if (commandline.hasOption("I")) {
                input = commandline.getOptionValue("I");
            }
            if (commandline.hasOption("O")) {
                output = commandline.getOptionValue("O");
            }
            if (commandline.hasOption("S")) {
                shape = commandline.getOptionValue("S");
            }
            if (commandline.hasOption("E")){
                elevType = commandline.getOptionValue("E");
            }
            if (commandline.hasOption("L")){
                maxNumLakes = commandline.getOptionValue("E");
            }
            if (commandline.hasOption("h")){
                System.out.println("----------------------------------------------HELP MENU----------------------------------------------\n");
                System.out.println("Enter these commands to personalize the Island you would like to create! \n" );

            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        // Old Mesh to write on
        Mesh aMesh = new MeshFactory().read(input);

        // Island Generation
        IslandGen gen = new IslandGen();
        Mesh myMesh = gen.generate(aMesh, shape);

        // Outputing to new Mesh object
        MeshFactory factory = new MeshFactory();
        factory.write(myMesh, output);
    }
}
//take shape as string
//take elevation string (volcanoes, hills)
//take max number of lakes
// java -jar island.jar -I ../generator/sample.mesh -O island.mesh