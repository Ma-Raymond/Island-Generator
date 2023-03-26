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
        String biome = "";

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
                System.out.println("Options: Moon\nCross\nCircle\nOval\nHeart");
                System.out.println("------------------------------------");
                System.out.println("Altitude \"-altitude xx\"");
                System.out.println("Options: Mountain\nHill\nFlat");
                System.out.println("------------------------------------");
                System.out.println("Maximum # Lakes \"-lakes xx\"");
                System.out.println("Options: Integer Value");
                System.out.println("------------------------------------");
                System.out.println("# of Aquifers \"-aquifers xx\"");
                System.out.println("Options: Integer Value");
                System.out.println("------------------------------------");
                System.out.println("Type of Island Soil \"-soil xx\"");
                System.out.println("Options: Wet\nDry\nNormal");
                System.out.println("------------------------------------");
                System.out.println("Type of Biomes \"-biomes xx\"");
                System.out.println("Options: Desert\nGrassland\nDeciduous\nTaiga\nTundra\nForest\nTemperateRain\nTropical\nSavana");
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

            if (!maxNumLakes.equals("")){
                try{
                    numLakes = Integer.parseInt(maxNumLakes);
                    if (numLakes < 0){
                        maxNumLakes = "";
                        throw new ParseException("Sorry, no negative lakes. try again!");
                    }
                    if (numLakes > 400){
                        maxNumLakes = "200";
                    }
                }catch(Exception e){
                    maxNumLakes = "";
                    throw new ParseException("The lake input you've provided is not an integer or negative, try again!");
                }
            }
            if (!aquifers.equals("")){
                try{
                    numAquifers = Integer.parseInt(aquifers);
                    if (numAquifers < 0){
                        aquifers = "";
                        throw new ParseException("Sorry, no negative aquifers. try again!");
                    }
                    if (numAquifers > 400){
                        aquifers = "200";
                    }
                }catch(Exception e){
                    aquifers = "";
                    throw new ParseException("The Aquifer input you've provided is not an integer or negative, try again!");
                }
            }

            if (!rivers.equals("")){
                try{
                    numRivers = Integer.parseInt(rivers);
                    if (numRivers < 0){
                        rivers = "";
                        throw new ParseException("Sorry, no negative rivers. try again!");
                    }
                    if (numRivers > 400){
                        rivers = "200";
                    }
                }catch(Exception e){
                    rivers = "";
                    throw new ParseException("The river input you've provided is not an integer or negative, try again!");
                }
            }

            try{
                if (!(biome.equals("Desert")|biome.equals("Savana")|biome.equals("Tropical")|biome.equals("Grassland")|biome.equals("Deciduous")|biome.equals("TemperateRain")|biome.equals("Taiga")|biome.equals("Tundra")|biome.equals(""))){
                    biome = "Deciduous"; //random default if input incorrectly
                    throw new ParseException("Incorrect Biome Input");
                }
            }catch(Exception e) {
                System.out.println("------------------------------------");
                System.out.println("Incorrect Biome Inputted!! Here are your Options: \n" +
                        "Tundra \n" +
                        "Taiga \n" +
                        "TemperateRain \n" +
                        "Deciduous \n" +
                        "Grassland \n" +
                        "Tropical \n" +
                        "Savana \n" +
                        "Desert");
                System.out.println("------------------------------------");
            }
            if (!(soil.equals("Wet")|soil.equals("Normal")|soil.equals("Dry")|soil.equals(""))){
                soil = ""; //random default if input incorrectly
                throw new ParseException("Incorrect Soil Input");
            }
            if (!(elevType.equals("Mountain")|elevType.equals("Hill")|elevType.equals("Flat")|elevType.equals(""))){
                elevType = ""; //random default if input incorrectly
                throw new ParseException("Incorrect Elevation Input");
            }
            if (!(shape.equals("Circle")|shape.equals("Oval")|shape.equals("Moon")|shape.equals("Cross")|shape.equals("Heart")|shape.equals(""))){
                shape = ""; //random default if input incorrectly
                throw new ParseException("Incorrect Shape Input");
            }
            if (!seed.equals("")){
                try{
                    Long val = Long.parseLong(seed);
                }catch (Exception e){
                    seed = "";
                    throw new ParseException("Only Integer Inputs Allowed for Seeds");
                }
            }
        }
        catch (ParseException e) {
            System.out.println("----------------------------------------------ERROR MESSAGE----------------------------------------------");
            System.out.println("Please Re-Enter. An ERROR occured with your user input \""+e.getMessage()+"\". Use --help/-H");
            System.out.println("----------------------------------------------ERROR MESSAGE----------------------------------------------\n");
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