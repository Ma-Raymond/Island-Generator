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

        options.addOption("P", true, "Indicates number of Polygons");
        options.addOption("I", false, "Toggles Irregular Mesh");
        options.addOption("H", false, "Command information");
        options.addOption("R", true, "Indicates Number of times to Relax Irregular Mesh");

        //defaults to the regular mesh
        String regOrNot = "Reg";
        //Default debug mode toggle state
        String debug = "debugOff";
        //Default number of Polygons
        String numPoly = "100";
        //Default
        String defaultRelaxTimes = "0";

        try {
            CommandLine commandline = parser.parse(options, args);
            if (commandline.hasOption("I")) {
                regOrNot = "Irreg";
                if (commandline.hasOption("P")) {
                    numPoly = commandline.getOptionValue("P");
                }
                if (commandline.hasOption("R")){
                    defaultRelaxTimes = commandline.getOptionValue("R");
                }
            }
            if (commandline.hasOption("H")){
                System.out.println("You've reached the help menu!");
                System.out.println("Enter these commands to personalize the mesh you would like to create! \n" +
                                    "If no commands are entered, the default colourful mesh will appear.");
                System.out.println("-I ~~ This creates the default irregular mesh with 100 polygons \n" +
                                    "-P x ~~ In place of x enter a integer to choose how many polygons are created in the irregular mesh \n" +
                                    "-R x ~~ In place of x enter an integer to choose the level of relaxation of the irregular mesh. \n" +
                                    " NOTE: to toggle debug mode for either type of Mesh, use -X in the visualizer command line!");
            }

        } catch (ParseException e) {
            System.out.println("IT DIDNT WORK!!!!!!!!!");
        }

        //convert the number of polygons from string to int
        int numOfPolygons = Integer.parseInt(numPoly);
        int userRelaxRequests = Integer.parseInt(defaultRelaxTimes);

        if (regOrNot.equals("Irreg")) {
            IrregMeshGen gen = new IrregMeshGen();
            Mesh myMesh = gen.generate(numOfPolygons, userRelaxRequests);
            MeshFactory factory = new MeshFactory();
            factory.write(myMesh, args[0]);
        } else {
            MeshGen gen = new MeshGen();
            Mesh myMesh = gen.generate(debug);
            MeshFactory factory = new MeshFactory();
            factory.write(myMesh, args[0]);
        }

        System.out.println(regOrNot);
        System.out.println(numOfPolygons);




        // Step 1 -----------
//                DotGen generator = new DotGen();
//                Mesh myMesh = generator.generate();

        // Step 2 ----------- OOP Approach w/ Neighbours and Centroids and Debug Mode
        //         MeshGen generator = new MeshGen();
        //        Mesh myMesh = generator.generate(Mode);

        // Step 3 -----------

    }

}
