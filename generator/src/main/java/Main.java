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

        try {
            CommandLine commandline = parser.parse(options, args);
            if (commandline.hasOption("I")) {
                regOrNot = "Irreg";
                numPoly = commandline.getOptionValue("P");
            } else {
                if (commandline.hasOption("X")) {
                    debug = "debugOn";
                }
            }

        } catch (ParseException e) {
            System.out.println("IT DIDNT WORK!!!!!!!!!");
        }

        //convert the number of polygons from string to int
        int numOfPolygons = Integer.parseInt(numPoly);

        if (regOrNot.equals("Irreg")) {
            IrregMeshGen gen = new IrregMeshGen();
            Mesh myMesh = gen.generate(numOfPolygons);
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
    }

}
