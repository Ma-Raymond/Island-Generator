import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import island.files.IslandGen;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {

        Random rand = new Random();
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("I", true, "Input");
        options.addOption("O", true, "Output");
        options.addOption("shape", true, "Shape");
        options.addOption("altitude", true, "Elevation Type");
        options.addOption("aquifers", true, "Number of Aquifers");
        options.addOption("lakes", true, "Max Number of Lakes");
        options.addOption("rivers", true, "Number of Rivers");
        options.addOption("soil", true, "Soil Profile");
        options.addOption("biomes", true, "Biome");
        options.addOption("seed", true, "Island Seed");
        options.addOption("H", "help", false, "Command information");
        String input = null;
        String output = null;

        //Seed Parameters
        String shape = String.valueOf(rand.nextInt(0, 3));
        String elevType = String.valueOf(rand.nextInt(0, 2));
        String biome = String.valueOf(rand.nextInt(0, 7));

        //These can all be randomized after island generation
        String maxNumLakes = "";
        String lakeStartIdx = "";
        String aquifers = "";
        String aquiferStartIdx = "";
        String rivers = "";
        String riverStartIdx = "";
        String soil = "";

        //Seed will be either inputted or created from the above parameters
        String seed = "";


        try {
            CommandLine commandline = parser.parse(options, args);

            if (commandline.hasOption("H")) {
                System.out.println("----------------------------------------------HELP MENU----------------------------------------------\n");
                System.out.println("Enter these commands to personalize the Island you would like to create! \n");

            }

            if (commandline.hasOption("I")) {
                input = commandline.getOptionValue("I");
            }
            if (commandline.hasOption("O")) {
                output = commandline.getOptionValue("O");
            }

            if (commandline.hasOption("seed")) {
                seed = commandline.getOptionValue("seed");

            }

            if (commandline.hasOption("shape")) {
                shape = commandline.getOptionValue("shape");
            }
            if (commandline.hasOption("altitude")){
                elevType = commandline.getOptionValue("altitude");
            }

            if (commandline.hasOption("aquifers")){
                aquifers = commandline.getOptionValue("aquifers");
            }

            if (commandline.hasOption("lakes")){
                maxNumLakes = commandline.getOptionValue("lakes");
            }
            if (commandline.hasOption("rivers")){
                rivers = commandline.getOptionValue("rivers");
            }
            if (commandline.hasOption("soil")){
                soil = commandline.getOptionValue("soil");
            }
            if (commandline.hasOption("biomes")){
                biome = commandline.getOptionValue("biomes");


            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        // Old Mesh to write on
        Mesh aMesh = new MeshFactory().read(input);

        // Island Generation
        IslandGen gen = new IslandGen();
        Mesh myMesh = gen.generate(aMesh, seed, shape, elevType, maxNumLakes, lakeStartIdx, rivers, riverStartIdx, aquifers, aquiferStartIdx, soil, biome);

        // Outputing to new Mesh object
        MeshFactory factory = new MeshFactory();
        factory.write(myMesh, output);
    }
}
//take shape as string
//take elevation string (volcanoes, hills)
//take max number of lakes
// java -jar island.jar -I ../generator/sample.mesh -O island.mesh