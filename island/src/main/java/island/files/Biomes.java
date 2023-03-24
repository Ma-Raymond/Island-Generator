package island.files;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

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
                if (0 <= temp && temp <= 50 && 0 <= height && height < 150){
                    //BiomeTypes.add("Desert");
                    colorPolygon(255,128,0,255, i);
                }
                else if (50 < temp && temp < 275 && 0 < height && height < 50) {
                    //BiomeTypes.add("Savana");
                    colorPolygon(102,204,0,255, i);

                }
                else if (275 < temp && 0 < height && height < 50) {
                    //BiomeTypes.add("Tropic Rain Forest");
                    colorPolygon(51,255,51,255, i);

                }
                else if (50 < temp && temp < 110 && 50 < height && height < 175) {
                    //BiomeTypes.add("Grassland");
                    colorPolygon(255,204,0,255, i);

                }
                else if (110 < temp && temp < 225 && 50 < height && height < 125) {
                    //BiomeTypes.add("Deciduous");
                    colorPolygon(0,182,85,255, i);

                }
                else if (225 < temp && 50 < height && height < 125) {
                    //BiomeTypes.add("Temperate Rain Forest");
                    colorPolygon(0,255,179,255, i);

                }
                else if (50 < temp && 125 < height && height < 175) {
                    //BiomeTypes.add("Taiga");
                    colorPolygon(0,127,0,255, i);

                }
                else if (0 < temp && 175 < height) {
                    //BiomeTypes.add("Tundra");
                    colorPolygon(0,230,255,255, i);

                }
                else{
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




