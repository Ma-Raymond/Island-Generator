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
        options.addOption("biomes", true, "Biomes");
        options.addOption("seed", true, "Island Seed");
        options.addOption("H", "help", false, "Command information");
        String input = null;
        String output = null;

        //Seed Parameters
        String shape = "";
        String elevType = "";
        String biome = String.valueOf(rand.nextInt(0, 7));

        //These can all be randomized after island generation
        String elevationStartIdx = "";
        String maxNumLakes = "";
        String lakeStartIdx = "";
        String aquifers = "";
        String aquiferStartIdx = "";
        String rivers = "";
        String riverStartIdx = "";
        String soil = "";

        //Seed will be either inputted or created from the above parameters
        String seed = "";

        int numLakes;
        int numAquifers;
        int numRivers;


        try {
            CommandLine commandline = parser.parse(options, args);

            if (commandline.hasOption("H")) {
                System.out.println("----------------------------------------------HELP MENU----------------------------------------------\n");
                System.out.println("Enter these commands to personalize the Island you would like to create! \n");
                System.out.println("------------------------------------");
                System.out.println("Shape \"-shape xx\"");
                System.out.println("Options: Moon\tCross\tCircle\tOval");
                System.out.println("------------------------------------");
                System.out.println("Altitude \"-altitude xx\"");
                System.out.println("Options: Volcano\tHills\tFlat");
                System.out.println("------------------------------------");
                System.out.println("Maximum # Lakes \"-lakes xx\"");
                System.out.println("Options: Integer");
                System.out.println("------------------------------------");
                System.out.println("# of Aquifers \"-aquifers xx\"");
                System.out.println("Options: Integer");
                System.out.println("------------------------------------");
                System.out.println("Type of Island Soil \"-soil xx\"");
                System.out.println("Options: Wet\tDry\tNormal");
                System.out.println("------------------------------------");
                System.out.println("Type of Biomes \"-biomes xx\"");
                System.out.println("Options: Desert\tGrassland\tTaiga\tForest\tRainForest\tTropical\tSavana");
                System.out.println("------------------------------------");
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


            try{
                numLakes = Integer.parseInt(maxNumLakes);
                numAquifers = Integer.parseInt(aquifers);
                numRivers = Integer.parseInt(rivers);
                if (numLakes < 0){
                    maxNumLakes = "";
                }
                if (numAquifers < 0){
                    aquifers = "";
                }
                if (numRivers < 0){
                    rivers = "";
                }

            }catch(Exception e){
                throw new ParseException("Values for max Lakes, Aquifers, and Rivers must be integers!");
            }
            try{
                if (!(biome.equals("Desert")||biome.equals("Savana")||biome.equals("Trpoical")||biome.equals("Grassland")||biome.equals("Deciduous")||biome.equals("TemperateRain")||biome.equals("Taiga")||biome.equals("Tundra"))){
                    biome = "Deciduous"; //random default if input incorrectly
                    throw new Exception("");
                }
            }catch(Exception e){
                System.out.println("Biome type must be inputted as a valid string. Any one of the following will be accepted: \n" +
                        "Tundra \n" +
                        "Taiga \n" +
                        "TemperateRain \n" +
                        "Deciduous \n" +
                        "Grassland \n" +
                        "Tropical \n" +
                        "Savana \n" +
                        "Desert \n");
            }
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Old Mesh to write on
        Mesh aMesh = new MeshFactory().read(input);

        // Island Generation
        IslandGen gen = new IslandGen();
        Mesh myMesh = gen.generate(aMesh, seed, shape, elevType, elevationStartIdx, maxNumLakes, lakeStartIdx, rivers, riverStartIdx, aquifers, aquiferStartIdx, soil, biome);

        // Outputing to new Mesh object
        MeshFactory factory = new MeshFactory();
        factory.write(myMesh, output);
    }
}
//take shape as string
//take elevation string (volcanoes, hills)
//take max number of lakes
// java -jar island.jar -I ../generator/sample.mesh -O island.mesh