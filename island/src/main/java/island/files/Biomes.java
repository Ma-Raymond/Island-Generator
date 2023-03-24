package island.files;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Biomes {

    List<Double> elevations;
    List<Double> humidities;
    List<Structs.Polygon> polygonList;
    List<Integer> IslandBlocks;

    List<Integer> LakeIdxs;

    int startHeightCase;
    List<String> BiomeTypes;
    String BiomeType;

    //if island polygon is not a lake, get the elevation

    public void generate(List<Double> elev, List<Integer> IsleBlocks, List<Integer> LakeBlocks, List<Double> humidity, List<Structs.Polygon> polyList){
        elevations = elev;
        IslandBlocks = IsleBlocks;
        LakeIdxs = LakeBlocks;
        humidities = humidity;
        polygonList = polyList;
        BiomeType(elevations, humidities, IslandBlocks, LakeIdxs);

    }

    public void BiomeType(List<Double> elev, List<Double> humidity, List<Integer> IsleBlocks, List<Integer> LakeBlocks){
        for (Integer i: IsleBlocks){
            if (!LakeBlocks.contains(i)){
                double height = elev.get(i);
                double temp = humidity.get(i);
                if (0 <= temp && temp <= 50 && 0 <= height && height < 175){
                    //BiomeTypes.add("Desert");
                    colorPolygon(206,112,44,255, i);
                    new Color(206,112,44,255);
                }
                else if (50 < temp && temp < 275 && 0 <= height && height < 50) {
                    //BiomeTypes.add("Savana");
                    colorPolygon(255,255,221,255, i);
                    new Color(255,255,221,255);

                }
                else if (275 <= temp && 0 <= height && height < 50) {
                    //BiomeTypes.add("Tropic Rain Forest");
                    colorPolygon(181,220,178,255, i);
                    new Color(181,220,178,255);
                }
                else if (50 < temp && temp < 110 && 50 <= height && height < 175) {
                    //BiomeTypes.add("Grassland");
                    colorPolygon(240,167,74,255, i);
                    new Color(240,167,74,255);
                }
                else if (110 < temp && temp < 225 && 50 <= height && height < 125) {
                    //BiomeTypes.add("Deciduous");
                    colorPolygon(49,113,79,255, i);
                    new Color(49,113,79,255);

                }
                else if (225 < temp && 50 <= height && height <= 125) {
                    //BiomeTypes.add("Temperate Rain Forest");
                    colorPolygon(0,255,179,255, i);
                    new Color(0,255,179,255);
                }
                else if (50 <= temp && 125 <= height && height < 175) {
                    //BiomeTypes.add("Taiga");
                    colorPolygon(0,127,0,255, i);
                    new Color(0,127,0,255);
                }
                else if (0 <= temp && 175 <= height) {
                    //BiomeTypes.add("Tundra");
                    colorPolygon(206,221,237,255, i);
                    new Color(206,221,237,255);
                }
                else{
                    System.out.println("Humidity:"+temp+" Height:"+height);
                    colorPolygon(255,255,255,255,i);
                }

                }

            else{
                continue;
            }
        }
    }


    private void colorPolygon(int red, int green, int blue, int alpha, int index){
        Structs.Polygon poly = polygonList.get(index);
        Structs.Property color = Structs.Property.newBuilder().setKey("rgb_color").setValue(red + "," + green + "," + blue+ "," + alpha).build();
        Structs.Polygon colored = Structs.Polygon.newBuilder(poly).addProperties(color).build();
        polygonList.set(index, colored);
    }

}



    //one method to generally set type of biome with case statements and polygon elevation input
    //check if a polygon is a lake, make a list of lakes.




