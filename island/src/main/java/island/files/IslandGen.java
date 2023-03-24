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

    private void soilProfile(){


    }
    private void createLakes(int maximumLakes, int startIndexL){

        Random randNum = new Random();
        if(maximumLakes!= 0){
            //int startIndexL = randNum.nextInt(heightPoints.size()); //put this in the generator in an if statement
            if (isSeed){
                lakeNum = maxLakes;
                for (int i = 0; i < maxLakes; i ++){
                    int polyIndex = (startIndexL + i) % heightPoints.size();
                    int validPolyId  = heightPoints.get(polyIndex);
                    lakeIdxs.add(validPolyId);
                    colorPolygon(102, 178,255,255, validPolyId);
                    addLakeHumidity(validPolyId);
                }
            }
            else{
                lakeNum = randNum.nextInt(maxLakes);
                for (int i = 0; i < lakeNum; i ++){
                    int polyIndex = (startIndexL + i) % heightPoints.size();
                    int validPolyId  = heightPoints.get(polyIndex);
                    lakeIdxs.add(validPolyId);
                    colorPolygon(102, 178,255,255, validPolyId);
                    addLakeHumidity(validPolyId);
                }
            }

        }

    }

    private void addLakeHumidity(int lakePoly){
        Polygon poly = polygonList.get(lakePoly);
        double humidityValLake = Double.parseDouble(precision.format(humidity.get(lakePoly)+150));
        humidity.set(lakePoly, humidityValLake);
        for (Integer n : poly.getNeighborIdxsList()){
            //may be used for colouring later
            Polygon neighbourPoly = polygonList.get(n);
            //colorHeight(neighbourPoly,1.2);
            double humidityValNeigbours = Double.parseDouble(precision.format(humidity.get(n)+100));
            humidity.set(n,humidityValNeigbours);
        }
    }

    private void createAquifers(int aquaNum, int startIndexA){
        //int startIndexL = randNum.nextInt(heightPoints.size()); //put this in the generator in an if statement
        for (int i = 0; i < aquaNum; i ++){
            int polyIndex = (startIndexA + i) % heightPoints.size();
            int validPolyId = heightPoints.get(polyIndex);
            colorPolygon(102, 178,255,255, validPolyId);
            addAquaHumidity(validPolyId);
        }
        aquaStartIdx = startIndexA;

    }
    private void addAquaHumidity(int aquaPoly){
        Polygon poly = polygonList.get(aquaPoly);
        double humidityValAqua = Double.parseDouble(precision.format(humidity.get(aquaPoly)+150));
        humidity.set(aquaPoly, humidityValAqua);
        for (Integer n : poly.getNeighborIdxsList()){
            double humidityValNeigbours = Double.parseDouble(precision.format(humidity.get(n)+100));
            humidity.set(n,humidityValNeigbours);
        }
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
        soilMoisture = Integer.parseInt(seedDetails[16]);
        biome = Integer.parseInt(seedDetails[17]);
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
            altStartIdx = rand.nextInt(0, maxIdx);
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
            lakeStartIdx = rand.nextInt(0, maxIdx);
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
            riverNum = rand.nextInt(0, maxRivers/50);
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
            riverStartIdx = rand.nextInt(0, maxIdx);
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
            aquaNum = rand.nextInt(0, maxLand/20);
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
            aquaStartIdx = rand.nextInt(0, maxIdx);
        } else {
            int startIdx = Integer.parseInt(aquiferStartingIdx);
            if (startIdx > maxIdx) {
                aquaStartIdx = maxIdx;
            } else {
                aquaStartIdx = startIdx;
            }
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

        elevations = new ArrayList<Double>(Collections.nCopies(nPolygons, defaultBlockElev));
        humidity = new ArrayList<Double>(Collections.nCopies(nPolygons, defaultHumidity));
        vertexHeights = new ArrayList<>(Collections.nCopies(nVertices, 0.0));
    }

    public Mesh generate(Mesh aMesh,String seedInput, String shape, String elevType, String elevationStartIdx,String maxNumLakes, String lakeStartingIdx, String rivers, String riverStartingIdx, String aquifers, String aquiferStartingIdx, String soil, String biomeSelect){
        //Create new island
        defaultValues(aMesh);

        //If user input a seed
        if (!seedInput.equals("")){
            seedDecoder(seedInput);
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
        }

        //If user did not input seed
        else{
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
        }

        // Generate Elevation
        selectElevation(altType);

        //Lakes
        createLakes(maxLakes, lakeStartIdx);
        createAquifers(aquaNum, aquaStartIdx);

        //Rivers
        Rivers river = new Rivers();
        river.generate(riverNum,riverStartIdx,polygonList,segmentList,vertexList,elevations,vertexHeights,islandVertices,islandBlocks,humidity);
        segmentList = river.segmentList;
        vertexList = river.vertexList;
        humidity = river.humidity;


        Biomes biomeGen = new Biomes();
        defaultBlockElev = biomeGen.BiomeElevation(biomeSelect);
        defaultHumidity = biomeGen.BiomeHumidity(biomeSelect);
        biomeGen.generate(elevations, islandBlocks, lakeIdxs, humidity, polygonList);
        polygonList = biomeGen.polygonList;

        //Aquifers


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
                if (attributes.get(i) == 0){
                    attributesStr.add(String.valueOf((attributes.get(i)+ "0")));
                }
                else{
                    attributesStr.add(String.valueOf(attributes.get(i)));
                }
            }

            String seed = (String.valueOf(islandShape) + String.valueOf(altType) + attributesStr.get(0) + attributesStr.get(1) + attributesStr.get(2) + attributesStr.get(3)+ attributesStr.get(4) + attributesStr.get(5) + attributesStr.get(6) + String.valueOf(soilMoisture) + String.valueOf(biome));
            System.out.println(seed);
        }

        // Assigning Biomes and Types
        return Mesh.newBuilder().addAllVertices(vertexList).addAllSegments(segmentList).addAllPolygons(polygonList).build();
    }

    private void getIslandBlocks(){
        for (int i = 0; i < polygonList.size();i++){
            Polygon poly = polygonList.get(i);
            if (extractColorString(poly.getPropertiesList()).equals(islandColor)){
                islandBlocks.add(i);
                elevations.set(i,1.0);
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


    private void selectElevation(int elevationNum){
        if (elevationNum == 0){
            volcano(altStartIdx);
        }
        else if (elevationNum == 2){
            generateHills(altStartIdx);
        }

    }
    private void generateHills(int startIdx){
        // Have it incrementally do it with the seed
        altStartIdx = startIdx % heightPoints.size();
        for (int i = 0; i < heightPoints.size()/2; i++){
            int Idx = (startIdx + i) % heightPoints.size();
            int polyIdx = heightPoints.get(Idx);
            Polygon poly = polygonList.get(polyIdx);
            List<Integer> neighbourList = poly.getNeighborIdxsList();
            colorHeight(poly,1.5);
            double elevationVal = Double.parseDouble(precision.format(elevations.get(polyIdx)+1.5));
            elevations.set(polyIdx,elevationVal);
            for (Integer j : neighbourList){
                Polygon neighbourPoly = polygonList.get(j);
                colorHeight(neighbourPoly,1.2);
                double elevationVal2 = Double.parseDouble(precision.format(elevations.get(polyIdx)+1.2));
                elevations.set(j,elevationVal2);
            }
        }
    }
    private void volcano(int startIdx){
        altStartIdx = startIdx % heightPoints.size();
        Deque<Integer> deque = new ArrayDeque<>();
        Set<Integer> visited = new HashSet<>();
        int polyIdx = heightPoints.get(startIdx);
        double volcanoHeight = 200.0;
        visited.add(polyIdx);
        deque.add(polyIdx);
        double visual = 5;
        while (!deque.isEmpty()){
            int idxVal = deque.removeFirst();
            Polygon poly = polygonList.get(idxVal);
            elevations.set(idxVal,volcanoHeight);
            List<Integer> neighbourList = poly.getNeighborIdxsList();
            colorHeight(poly,visual);
            for (Integer idx : neighbourList){
                if (!visited.contains(idx) && islandBlocks.contains(idx)){
                    visited.add(idx);
                    deque.add(idx);
                }
            }
            visual -= 0.01;
            if (volcanoHeight >0)
                volcanoHeight = Double.parseDouble(precision.format(volcanoHeight-1.0));
        }
    }
    private void colorHeight(Polygon poly, double value){
        // Island is "253,255,208,255"
        if (value < 1){
            value = 1;
        }
        double red = 253/value;
        double green = 255/value;
        double blue = 208/value;
        int index = polygonList.indexOf(poly);
        colorPolygon((int)red,(int)green,(int)blue,255, index);
    }
    private void colorPolygon(int red, int green, int blue, int alpha, int index){
        Polygon poly = polygonList.get(index);
        Structs.Property color = Structs.Property.newBuilder().setKey("rgb_color").setValue(red + "," + green + "," + blue+ "," + alpha).build();
        Polygon colored = Polygon.newBuilder(poly).addProperties(color).build();
        polygonList.set(index, colored);
    }
    private void assignType(Polygon poly, String type){
        Structs.Property typeProperty = Structs.Property.newBuilder().setKey("Type").setValue(type).build();
        Polygon typed = Polygon.newBuilder(poly).addProperties(typeProperty).build();
        polygonList.set(polygonList.indexOf(poly), typed);
    }
    private String extractType(List<Structs.Property> properties){
        String val = null;
        for(Structs.Property p: properties) {
            // TRY TO FIND THE RGB COLOR
            if (p.getKey().equals("Type")) {
                val = p.getValue();
            }
        }
        if (val == null){       // IF THE RGB COLOR PROPERTY DOESNT EXIST, COVER THAT CASE BY MAKING IT BLACK
            return "None"; // COVERING CASE IF KEY RGB_COLOR DOESN'T EXIST
        }
        return val;
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
