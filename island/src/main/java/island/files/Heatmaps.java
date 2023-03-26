package island.files;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.util.ArrayList;
import java.util.List;

public class Heatmaps implements IslandColour{

    List<Structs.Polygon> polygonList;
    List<Double> humidity;
    List<Double> elevations;
    List<Integer> islandBlocks;

    public void selectMap(List<Structs.Polygon> polygons, List<Double> humidityList, List<Double> elevationlist ,List<Integer> islandBlocksList, String map){
        polygonList = polygons;
        humidity = humidityList;
        elevations = elevationlist;
        islandBlocks = islandBlocksList;

        if (map.equals("Elevation")){
            elevationMap();
        }

        else if(map.equals("Moisture")){
            moistureMap();
        }
    }

    private void elevationMap(){
//        System.out.println(elevations);
        for (int j: islandBlocks){
            double i = elevations.get(j);
            int greenBlue = (int)((25.5/300.0)*(i));
            if (greenBlue>25){
                greenBlue=25;
            }
            System.out.println(greenBlue);
            colorPolygon(255, 255-(10*greenBlue), 255-(10*greenBlue), 255, j);
        }
    }

    private void moistureMap(){
//        System.out.println(humidity);
        for (int j: islandBlocks){
            double i = humidity.get(j);
            int greenRed = (int)((25.5/2000.0)*(i));
            if (greenRed>25){
                greenRed=25;
            }
//            System.out.println(greenRed);
            colorPolygon(255-(10*greenRed), 255-(10*greenRed), 255, 255, j);
        }
    }

    @Override
    public void colorPolygon(int red, int green, int blue, int alpha, int index) {
        Structs.Polygon poly = polygonList.get(index);
        Structs.Property color = Structs.Property.newBuilder().setKey("rgb_color").setValue(red + "," + green + "," + blue+ "," + alpha).build();
        Structs.Polygon colored = Structs.Polygon.newBuilder(poly).addProperties(color).build();
        polygonList.set(index, colored);
    }
}
