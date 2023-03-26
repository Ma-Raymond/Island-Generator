package island.files;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;


abstract class IslandSeed{
    int islandShape;
    int altType;
    int altStartIdx;
    boolean isSeed = false;
    int maxLakes;
    int lakeNum;
    int lakeStartIdx;
    int riverNum;
    int riverStartIdx;
    int aquaNum;
    int aquaStartIdx;
    int soilMoisture;
    int biome;
    double defaultBlockElev;
    double defaultHumidity;

}

public class IslandGen extends IslandSeed {

    List<Polygon> polygonList;
    List<Segment> segmentList;
    List<Vertex> vertexList;
    List<Double> elevations;
    List<Double> humidity;
    List<Double> vertexHeights;
    List<Double> soil;

    List <Integer> lakeIdxs = new ArrayList<>();

    String islandColor = "253,255,208,255";
    List<Integer> islandBlocks = new ArrayList<>();
    List<Integer> heightPoints = new ArrayList<>();
    List<Integer> islandVertices = new ArrayList<>();

    DecimalFormat precision  = new DecimalFormat("0.00");

    double soilPercent;
    private void soilProfile(){
        if (soilMoisture == 0)
            soilPercent = 1.5;
        else if (soilMoisture == 1)
            soilPercent = 1.0;
        else if (soilMoisture == 2)
            soilPercent = 0.5;
    }

    private void seedDecoder(String seed){
        isSeed = true;
        //adjust seed if it is not correct
        if (seed.length() < 18){
            seed += "0".repeat(18-seed.length());
        }
        if (seed.length() > 18) {
            seed = seed.substring(0,18);
        }
        //Get island details from seed
        String[] seedDetails = seed.split("");
        islandShape = (Integer.parseInt(seedDetails[0]))%4;
        System.out.println(islandShape);
        altType = (Integer.parseInt(seedDetails[1]))%3;
        altStartIdx = Integer.parseInt(seedDetails[2]+seedDetails[3]);
        maxLakes = Integer.parseInt(seedDetails[4] + seedDetails[5]);
        lakeNum = Integer.parseInt(seedDetails[4] + seedDetails[5]);
        lakeStartIdx = Integer.parseInt(seedDetails[6]+seedDetails[7]);
        riverNum = Integer.parseInt(seedDetails[8]+seedDetails[9]);
        riverStartIdx = Integer.parseInt(seedDetails[10]+seedDetails[11]);
        aquaNum = Integer.parseInt(seedDetails[12]+seedDetails[13]);
        aquaStartIdx = Integer.parseInt(seedDetails[14]+seedDetails[15]);
        soilMoisture = (Integer.parseInt(seedDetails[16]))%3;
        biome = (Integer.parseInt(seedDetails[17]))%8;
    }

    private void getIslandShape(String shape){
        Random rand = new Random();
        if (shape.equals("")){
            islandShape = (rand.nextInt(0, 4));
        }
        else{
            HashMap<String, Integer> islandShapes = new HashMap<String, Integer>();
            islandShapes.put("Circle", 0);
            islandShapes.put("Oval", 1);
            islandShapes.put("Moon", 2);
            islandShapes.put("Cross", 3);
            islandShape = islandShapes.get(shape);
        }

    }

    private void getElevationType(String elevType){
        Random rand = new Random();
        if (elevType.equals("")){
            altType = (rand.nextInt(0, 3));
        }

        else{
            HashMap<String, Integer> elevationTypes = new HashMap<String, Integer>();
            elevationTypes.put("Volcano", 0);
            elevationTypes.put("Flat", 1);
            elevationTypes.put("Hill", 2);
            altType = elevationTypes.get(elevType);
        }

    }

    private void getElevationStartIdx(String elevationStartIdx){
        Random rand = new Random();
        int maxIdx = heightPoints.size()-1;
        if (elevationStartIdx.equals("")){
            altStartIdx = rand.nextInt(0, maxIdx%100);
        }
        else{
            int startIdx = Integer.parseInt(elevationStartIdx);
            if (startIdx>maxIdx){
                altStartIdx = maxIdx;
            }
            else{
                altStartIdx = startIdx;
            }
        }
    }

    private void getLakeNum(String maxNumLakes){
        Random rand = new Random();
        int maxLand = islandBlocks.size();
        if (maxNumLakes.equals("")){
             maxLakes = rand.nextInt(0, maxLand/20);
        }
        else{
            maxLakes = Integer.parseInt(maxNumLakes);
            if (maxLakes>=maxLand){
                maxLakes = maxLand;
            }

        }

    }
    private void getLakeStartIdx(String lakeStartingIdx) {
        Random rand = new Random();
        int maxIdx = islandBlocks.size();
        if (lakeStartingIdx.equals("")) {
            lakeStartIdx = rand.nextInt(0, maxIdx%100);
        } else {
            int startIdx = Integer.parseInt(lakeStartingIdx);
            if (startIdx > maxIdx) {
                lakeStartIdx = maxIdx -1;
            } else {
                lakeStartIdx = startIdx;
            }
        }
    }

    private void getRiverNum(String numRivers){
        Random rand = new Random();
        int maxRivers = islandVertices.size();
        if (numRivers.equals("")){
            riverNum = rand.nextInt(0, maxRivers%100);
        }
        else{
            int rivers = Integer.parseInt(numRivers);
            if (rivers>maxRivers){
                riverNum = maxRivers;
            }
            else{
                riverNum = rivers;
            }

        }

    }

    private void getRiverStartIdx(String riverIdx) {
        Random rand = new Random();
        int maxIdx = islandVertices.size();
        if (riverIdx.equals("")) {
            riverStartIdx = rand.nextInt(0, maxIdx%100);
        } else {
            int startIdx = Integer.parseInt(riverIdx);
            if (startIdx > maxIdx) {
                riverStartIdx = maxIdx -1;
            } else {
                riverStartIdx = startIdx;
            }
        }
    }


    private void getAquiferNum(String maxNumAquifer){
        Random rand = new Random();
        int maxLand = islandBlocks.size();
        if (maxNumAquifer.equals("")){
            aquaNum = rand.nextInt(0, maxLand%100);
        }
        else{
            int maxAqua = Integer.parseInt(maxNumAquifer);
            if (maxAqua>maxLand/20){
                aquaNum = maxLand/20;
            }
            else{
                aquaNum = maxAqua;
            }

        }
    }
    private void getAquiferStartIdx(String aquiferStartingIdx) {
        Random rand = new Random();
        int maxIdx = islandBlocks.size();
        if (aquiferStartingIdx.equals("")) {
            aquaStartIdx = rand.nextInt(0, maxIdx%100);
        } else {
            int startIdx = Integer.parseInt(aquiferStartingIdx);
            if (startIdx > maxIdx) {
                aquaStartIdx = maxIdx;
            } else {
                aquaStartIdx = startIdx;
            }
        }
    }

    private void getSoil( String soilType){
        Random rand = new Random();
        if (soilType.equals("")){
            soilMoisture = (rand.nextInt(0, 3));
        }

        else{
            HashMap<String, Integer> soilTypes = new HashMap<String, Integer>();
            soilTypes.put("Dry", 0);
            soilTypes.put("Average", 1);
            soilTypes.put("Wet", 2);
            soilMoisture = soilTypes.get(soilType);
        }
    }


    public void defaultValues(Mesh aMesh){
        // Get old mesh details
        polygonList = new ArrayList<>(aMesh.getPolygonsList());
        segmentList = new ArrayList<>(aMesh.getSegmentsList());
        vertexList = new ArrayList<>(aMesh.getVerticesList());

        // Set new Stats
        int nPolygons = polygonList.size();
        int nVertices = vertexList.size();

        elevations = new ArrayList<Double>(Collections.nCopies(nPolygons, 0.0));
        humidity = new ArrayList<Double>(Collections.nCopies(nPolygons, defaultHumidity));
        vertexHeights = new ArrayList<>(Collections.nCopies(nVertices, 0.0));
    }

    public Mesh generate(Mesh aMesh,String seedInput, String shape, String elevType, String elevationStartIdx,String maxNumLakes, String lakeStartingIdx, String rivers, String riverStartingIdx, String aquifers, String aquiferStartingIdx, String soilSelect, String biomeSelect){
        //Create new island
        Biomes biomeGen = new Biomes();
        //If user input a seed
        if (!seedInput.equals("")){
            seedDecoder(seedInput);
            String val = biomeGen.numToBiome(biome);
            defaultBlockElev = biomeGen.BiomeElevation(val);
            defaultHumidity = biomeGen.BiomeHumidity(val);
            defaultValues(aMesh);
            IslandShapes island = new IslandShapes();
            island.islandSelector(islandShape, aMesh, vertexList, polygonList);
            polygonList = island.polygonList;
            vertexList = island.vertexList;
            getIslandBlocks();
            getElevationStartIdx(String.valueOf(altStartIdx));
            getLakeStartIdx(String.valueOf(lakeStartIdx));
            getLakeNum(String.valueOf(maxLakes));
            getRiverNum(String.valueOf(riverNum));
            getRiverStartIdx(String.valueOf(riverStartIdx));
            getAquiferNum(String.valueOf(aquaNum));
            getAquiferStartIdx(String.valueOf(aquaStartIdx));
            System.out.println("IM BIOME FROM SEED "+biome);
        }
        //If user did not input seed
        else{
            defaultBlockElev = biomeGen.BiomeElevation(biomeSelect);
            defaultHumidity = biomeGen.BiomeHumidity(biomeSelect);
            defaultValues(aMesh);
            biome = biomeGen.biome;
            getIslandShape(shape);
            IslandShapes island = new IslandShapes();
            island.islandSelector(islandShape, aMesh, vertexList, polygonList);
            polygonList = island.polygonList;
            vertexList = island.vertexList;
            getIslandBlocks();
            getElevationStartIdx(elevationStartIdx);
            getElevationType(elevType);
            getLakeStartIdx(lakeStartingIdx);
            getLakeNum(maxNumLakes);
            getRiverNum(rivers);
            getRiverStartIdx(riverStartingIdx);
            getAquiferNum(aquifers);
            getAquiferStartIdx(aquiferStartingIdx);
            getSoil(soilSelect);
        }

        System.out.println(defaultBlockElev);
        System.out.println(defaultHumidity);

        // Generate Elevation
        Elevation elevate = new Elevation();
        elevate.generate(altType,elevations,altStartIdx,heightPoints,polygonList,islandBlocks);
        elevations = elevate.elevations;
        altStartIdx = elevate.altStartIdx;


        soilProfile();
        //Lakes
        Lake lakes = new Lake();
        lakes.generateLakes(soilPercent,islandBlocks,isSeed,maxLakes,lakeStartIdx,lakeNum,maxLakes,humidity,heightPoints,polygonList);
        humidity = lakes.humidity;
        lakeIdxs = lakes.lakeIdxs;
        lakeNum = lakes.lakeNum;
        polygonList = lakes.polygonList;

        //Rivers
        Rivers river = new Rivers();
        river.generate(soilPercent,riverNum,riverStartIdx,polygonList,segmentList,vertexList,elevations,vertexHeights,islandVertices,islandBlocks,humidity);
        segmentList = river.segmentList;
        vertexList = river.vertexList;
        humidity = river.humidity;


        //Biomes
        biomeGen.generate(elevations, islandBlocks, lakeIdxs, humidity, polygonList);
        polygonList = biomeGen.polygonList;


        //Aquifers
        Aquifers aquifer = new Aquifers();
        aquifer.generate(heightPoints, aquaStartIdx, polygonList, humidity, soilPercent, aquaNum);
        humidity = aquifer.humidity;

        //Testing the island attribute values
        System.out.println("island shape");
        System.out.println(islandShape);
        System.out.println("alt type");
        System.out.println(altType);
        System.out.println("alrt start idx");
        System.out.println(altStartIdx);
        System.out.println("lake num");
        System.out.println(lakeNum);
        System.out.println(maxLakes);
        System.out.println(maxNumLakes);
        System.out.println("lake start idx");
        System.out.println(lakeStartIdx);
        System.out.println("river num");
        System.out.println(riverNum);
        System.out.println("river start idx");
        System.out.println(riverStartIdx);
        System.out.println("aqua num");
        System.out.println(aquaNum);
        System.out.println("aqua start idx");
        System.out.println(aquaStartIdx);
        System.out.println("soil moisture");
        System.out.println(soilMoisture);
        System.out.println("biome");
        System.out.println(biome);

        //Seed generator
        ArrayList<Integer> attributes = new ArrayList<Integer>();
        ArrayList<String> attributesStr = new ArrayList<String >();
        attributes.add(altStartIdx);
        attributes.add(lakeNum);
        attributes.add(lakeStartIdx);
        attributes.add(riverNum);
        attributes.add(riverStartIdx);
        attributes.add(aquaNum);
        attributes.add(aquaStartIdx);

        if (isSeed){
            System.out.println(seedInput);
        }

        else{
            for (int i = 0; i < attributes.size(); i++){
                if (attributes.get(i) < 10){
                    attributesStr.add("0"+(attributes.get(i)));
                }
                else{
                    attributesStr.add(String.valueOf(attributes.get(i)));
                }
            }

            String seed = (String.valueOf(islandShape) + String.valueOf(altType) + attributesStr.get(0) + attributesStr.get(1) + attributesStr.get(2) + attributesStr.get(3)+ attributesStr.get(4) + attributesStr.get(5) + attributesStr.get(6) + String.valueOf(soilMoisture) + String.valueOf(biome));
            System.out.println(seed);
        }

        // Assigning Biomes and Types

        System.out.println(humidity);
        System.out.println(elevations);

        return Mesh.newBuilder().addAllVertices(vertexList).addAllSegments(segmentList).addAllPolygons(polygonList).build();
    }

    private void getIslandBlocks(){
        for (int i = 0; i < polygonList.size();i++){
            Polygon poly = polygonList.get(i);
            if (extractColorString(poly.getPropertiesList()).equals(islandColor)){
                islandBlocks.add(i);
                elevations.set(i,defaultBlockElev);
            }
        }
        Collections.shuffle(islandBlocks,new Random(2));
        generateInnerIsland();
    }

    private List<Integer> extractVertices(List<Structs.Property> properties){
        String val = null;
        for(Structs.Property p: properties) {
            // TRY TO FIND THE RGB COLOR
            if (p.getKey().equals("vertices")) {
                val = p.getValue();
            }
        }
        if (val == null){       // IF THE RGB COLOR PROPERTY DOESNT EXIST, COVER THAT CASE BY MAKING IT BLACK
            System.out.println("NO VERTEX PROPERTY");
            return null;
        }
        String[] raw = val.split(",");
        List<Integer> rawInts = new ArrayList<>();
        for (int i =0; i< raw.length;i++){
            Integer value = Integer.parseInt(raw[i]);
            rawInts.add(value);
        }
        return rawInts;
    }
    private void generateInnerIsland(){
        // heightPoint Island Blocks
        Set<Integer> verticesInIsland = new HashSet<>();
        for (Integer polyIdx : islandBlocks){
            boolean allNeighbourIslands = true;
            Polygon poly = polygonList.get(polyIdx);
            List<Integer> neighbourList = poly.getNeighborIdxsList();
            for (Integer j : neighbourList){
                if (!islandBlocks.contains(j)){
                    allNeighbourIslands = false;
                }
            }
            if (allNeighbourIslands){
                heightPoints.add(polyIdx);
                List<Integer> islandVertexList = extractVertices(poly.getPropertiesList());
                if (islandVertexList != null)
                    for (Integer i : islandVertexList){
                        if (!verticesInIsland.contains(i)){
                            islandVertices.add(i);
                            verticesInIsland.add(i);
                        }
                    }
            }
        }
        Collections.shuffle(islandVertices,new Random(2));
    }


    private void assignType(Polygon poly, String type){
        Structs.Property typeProperty = Structs.Property.newBuilder().setKey("Type").setValue(type).build();
        Polygon typed = Polygon.newBuilder(poly).addProperties(typeProperty).build();
        polygonList.set(polygonList.indexOf(poly), typed);
    }
    private String extractColorString(List<Structs.Property> properties){
        String val = null;
        for(Structs.Property p: properties) {
            // TRY TO FIND THE RGB COLOR
            if (p.getKey().equals("rgb_color")) {
                val = p.getValue();
            }
        }
        if (val == null){       // IF THE RGB COLOR PROPERTY DOESNT EXIST, COVER THAT CASE BY MAKING IT BLACK
            return "0,0,0,0"; // COVERING CASE IF KEY RGB_COLOR DOESN'T EXIST
        }
        return val;
    }
}
