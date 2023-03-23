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
    int lakeNum;
    int lakeStartIdx;
    int riverNum;
    int riverStartIdx;
    int aquaNum;
    int aquaStartIdx;
    int soilMoisture;
    int biome;

}

public class IslandGen extends IslandSeed {

    List<Polygon> polygonList;
    List<Segment> segmentList;
    List<Vertex> vertexList;
    List<Double> elevations;
    List<Double> humidity;

    String islandColor = "253,255,208,255";
    List<Integer> islandBlocks = new ArrayList<>();
    List<Integer> heightPoints = new ArrayList<>();
    DecimalFormat precision  = new DecimalFormat("0.00");

    private void islandSelector(int shapeSeed, Mesh aMesh){

        if (shapeSeed == 0){
            circleIsland(aMesh);
        }
        else if (shapeSeed == 1){
            ovalIsland(aMesh);
        }

        else if (shapeSeed ==2){
            moonIsland(aMesh);
        }
        else{
            crossIsland(aMesh);
        }

        getIslandBlocks();
    }

    private void circleIsland(Mesh aMesh){
        for (int i =0; i< aMesh.getPolygonsCount(); i++){
            Polygon poly = polygonList.get(i);
            Vertex centroid = vertexList.get(poly.getCentroidIdx());
            double x = centroid.getX();
            double y = centroid.getY();
            double distance = Math.sqrt(Math.pow(x-255,2)+Math.pow(y-255,2));
            //lake
//            if (distance < 100){
//                colorPolygon(poly, 102, 178,255,255);
//            }
            //island
            if (distance < 200){
                colorPolygon(253, 255,208,255, i);
            }
            else{
                colorPolygon( 35, 85,138,255, i);
            }
        }
    }
    private void crossIsland(Mesh aMesh){
        Random bag = new Random();
        for (int i =0; i< aMesh.getPolygonsCount(); i++){
            Polygon poly = polygonList.get(i);
            Vertex centroid = vertexList.get(poly.getCentroidIdx());
            double x = centroid.getX();
            double y = centroid.getY();
            double distance = Math.sqrt(Math.pow(x-250,2)+Math.pow(y-250,2));

            if (distance < 200){
                colorPolygon(253, 255,208,255, i);
            }
            else{
                colorPolygon( 35, 85,138,255, i);
            }

            for (int j = 100; j<= 400; j+=150) {
                if (inOval(50, 100, j, 50, x, y) < 0) {
                    colorPolygon( 35, 85, 138, 255, i);
                }
                if (inOval(50, 100, j, 450, x, y) < 0) {
                    colorPolygon(35, 85, 138, 255, i);
                }
                if (inOval(100, 50, 50, j, x, y) < 0) {
                    colorPolygon( 35, 85, 138, 255, i);
                }

                if (inOval(100, 50, 450, j, x, y) < 0) {
                    colorPolygon(35, 85, 138, 255, i);
                }

            }

        }
    }

    private double inOval(int a,int b,int offsetX, int offsetY, double x, double y){
        double result = Math.pow(((x-offsetX)/a),2) + Math.pow(((y-offsetY)/b),2) -1;
        return result;

    }

    private void moonIsland(Mesh aMesh){
        Random bag = new Random();
        for (int i =0; i< aMesh.getPolygonsCount(); i++){
            Polygon poly = polygonList.get(i);
            Vertex centroid = vertexList.get(poly.getCentroidIdx());
            double x = centroid.getX();
            double y = centroid.getY();
            double distance = Math.sqrt(Math.pow(x-250,2)+Math.pow(y-250,2));

            double distance1 = Math.sqrt(Math.pow(x-450,2)+Math.pow(y-250,2));

            if (distance < 200 && distance1 > 100){
                colorPolygon(253, 255,208,255, i);
            }
            else{
                colorPolygon(35, 85,138,255, i);
            }
        }
    }
    private void ovalIsland(Mesh aMesh){
        Random bag = new Random();
        int a = bag.nextInt(100, 200);
        int b = bag.nextInt(50, 150);
        for (int i =0; i< aMesh.getPolygonsCount(); i++){
            Polygon poly = polygonList.get(i);
            Vertex centroid = vertexList.get(poly.getCentroidIdx());
            double x = centroid.getX();
            double y = centroid.getY();
            double result = Math.pow(((x-250)/a),2) + Math.pow(((y-250)/b),2) -1;

            if (result < 0) {
                colorPolygon(253, 255, 208, 255, i);
            }

            else{
                colorPolygon(35, 85,138,255, i);
            }

        }

    }

    private void createLakes(int maxLakes, int startIndexL){
        Random randNum = new Random();
        //int startIndexL = randNum.nextInt(heightPoints.size()); //put this in the generator in an if statement
        int numLakes = randNum.nextInt(maxLakes);
        for (int i = 0; i < numLakes; i ++){
            int polyIndex = (startIndexL + i) % heightPoints.size();
            int validPolyId  = heightPoints.get(polyIndex);
            colorPolygon(102, 178,255,255, validPolyId);
            addLakeHumidity(validPolyId);

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
    private void addLakeHumidity(){
        
    }



    private void seedDecoder(String seed){
        //Get island details from seed
        String[] seedDetails = seed.split("-");
        islandShape = Integer.parseInt(seedDetails[0]);
        altType = Integer.parseInt(seedDetails[1]);
        altStartIdx = Integer.parseInt(seedDetails[2]);
        lakeNum = Integer.parseInt(seedDetails[3]);
        lakeStartIdx = Integer.parseInt(seedDetails[4]);
        riverNum = Integer.parseInt(seedDetails[5]);
        riverStartIdx = Integer.parseInt(seedDetails[6]);
        aquaNum = Integer.parseInt(seedDetails[7]);
        aquaStartIdx = Integer.parseInt(seedDetails[8]);
        soilMoisture = Integer.parseInt(seedDetails[9]);
        biome = Integer.parseInt(seedDetails[10]);
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
        int maxIdx = heightPoints.size();
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
            lakeNum = rand.nextInt(0, maxLand/20);
        }
        else{
            int maxLakes = Integer.parseInt(maxNumLakes);
            if (maxLakes>maxLand/20){
                lakeNum = maxLand/20;
            }
            else{
                lakeNum = maxLakes;
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
                lakeStartIdx = maxIdx;
            } else {
                lakeStartIdx = startIdx;
            }
        }
    }

    private void getRiverNum(String numRivers){
        Random rand = new Random();
        ArrayList riverVertexList = new ArrayList();//get rid of this when you make the actual vertex list for rivers
        int maxLand = riverVertexList.size();
        if (numRivers.equals("")){
            riverNum = rand.nextInt(0, maxLand/20);
        }
        else{
            int rivers = Integer.parseInt(numRivers);
            if (rivers>maxLand/20){
                riverNum = maxLand/20;
            }
            else{
                riverNum = rivers;
            }

        }

    }
    private void getRiverStartIdx(String riverIdx) {
        ArrayList riverVertexList = new ArrayList();//get rid of this when you make the actual vertex list for rivers
        Random rand = new Random();
        int maxIdx = riverVertexList.size();
        if (riverIdx.equals("")) {
            riverStartIdx = rand.nextInt(0, maxIdx);
        } else {
            int startIdx = Integer.parseInt(riverIdx);
            if (startIdx > maxIdx) {
                riverStartIdx = maxIdx;
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



    /**
     * Generate the new Islands
     * @param aMesh
     * @return
     */

    public Mesh generate(Mesh aMesh,String seed, String shape, String elevType, String elevationStartIdx,String maxNumLakes, String lakeStartingIdx, String rivers, String riverStartingIdx, String aquifers, String aquiferStartingIdx, String soil, String biomeSelect){
        // Get old mesh details
        polygonList = new ArrayList<>(aMesh.getPolygonsList());
        segmentList = new ArrayList<>(aMesh.getSegmentsList());
        vertexList = new ArrayList<>(aMesh.getVerticesList());

        // Set new Stats
        int nPolygons = polygonList.size();
        elevations = new ArrayList<Double>(Collections.nCopies(nPolygons, 0.0));
        humidity = new ArrayList<Double>(Collections.nCopies(nPolygons, 100.0));

        //Create new island

        //If user input a seed
        if (!seed.equals("")){
            seedDecoder(seed);
            islandSelector(islandShape, aMesh);
        }

        //If user did not input seed
        else{
            getIslandShape(shape);
            islandSelector(islandShape, aMesh);
            getElevationStartIdx(elevationStartIdx);
            getElevationType(elevType);
            getLakeStartIdx(lakeStartingIdx);
            getLakeNum(maxNumLakes);
            //Un Comment these when u add rivers bc they being baka rn without a real river list
//            getRiverNum(rivers);
//            getRiverStartIdx(riverStartingIdx);
            getAquiferNum(aquifers);
            getAquiferStartIdx(aquiferStartingIdx);
        }


        // Generate Elevation
        selectElevation(altType);

        //Lakes
        createLakes(lakeNum, lakeStartIdx);

        //Rivers

        //Aquifers



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
    private void generateInnerIsland(){
        // heightPoint Island Blocks
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
            }
        }
    }

    private void selectElevation(int elevationNum){
        if (elevationNum == 0){
            volcano(altStartIdx);
        }

        else if (elevationNum == 1){
            generateHills(altStartIdx);
        }

    }
    private void generateHills(int startIdx){
        // Have it incrementally do it with the seed
        altStartIdx = startIdx;
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
        altStartIdx = startIdx;
        Deque<Integer> deque = new ArrayDeque<>();
        Set<Integer> visited = new HashSet<>();
        int polyIdx = heightPoints.get(startIdx);
        double volcanoHeight = 150.0;
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
            volcanoHeight -= 10;
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
