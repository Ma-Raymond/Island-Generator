package island.files;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Biomes implements IslandColour{

    List<Double> elevations;
    List<Double> humidities;
    List<Structs.Polygon> polygonList;
    List<Integer> IslandBlocks;
    List<Integer> LakeIdxs;
    int biome;
    public HashMap<Integer,String> biomeMap;

    //if island polygon is not a lake, get the elevation

    public void generate(List<Double> elev, List<Integer> IsleBlocks, List<Integer> LakeBlocks, List<Double> humidity, List<Structs.Polygon> polyList){
        elevations = elev;
        IslandBlocks = IsleBlocks;
        LakeIdxs = LakeBlocks;
        humidities = humidity;
        polygonList = polyList;

        BiomeType(elevations, humidities, IslandBlocks, LakeIdxs);

    }

    public String numToBiome(int num){
        biomeMap = new HashMap<>();
        biomeMap.put(0,"Desert");
        biomeMap.put(1,"Savana");
        biomeMap.put(2,"Tropical");
        biomeMap.put(3,"Grassland");
        biomeMap.put(4,"Deciduous");
        biomeMap.put(5,"TemperateRain");
        biomeMap.put(6,"Taiga");
        biomeMap.put(7,"Tundra");

        return biomeMap.get(num);
    }

    public double BiomeElevation(String Biome){
        double elevation = 0;
        if (Biome.equals("Desert")){
            elevation = 10;

        }
        else if (Biome.equals("Savana")){
            elevation = 25;
        }
        else if (Biome.equals("Tropical")){
            elevation = 25;
        }
        else if (Biome.equals("Grassland")){
            elevation = 100;
        }
        else if (Biome.equals("Deciduous")){
            elevation = 100;
        }
        else if (Biome.equals("TemperateRain")){
            elevation = 100;
        }
        else if (Biome.equals("Taiga")){
            elevation = 150;
        }
        else if (Biome.equals("Tundra")){
            elevation = 200;
        }
    return elevation;

    }

    public double BiomeHumidity(String Biome){
        double humidity = 0;

        if (Biome.equals("Desert")){
            humidity = 0;
            biome = 0;
        }
        else if (Biome.equals("Savana")){
            humidity = 175;
            biome = 1;
        }
        else if (Biome.equals("Tropical")){
            humidity = 330;
            biome = 2;

        }
        else if (Biome.equals("Grassland")){
            humidity = 70;
            biome = 3;

        }
        else if (Biome.equals("Deciduous")){
            humidity = 140;
            biome = 4;

        }
        else if (Biome.equals("TemperateRain")){
            humidity = 240;
            biome = 5;

        }
        else if (Biome.equals("Taiga")){
            humidity = 100;
            biome = 6;

        }
        else if (Biome.equals("Tundra")){
            humidity = 10;
            biome = 7;

        }
        return humidity;

    }

    private void BiomeType(List<Double> elev, List<Double> humidity, List<Integer> IsleBlocks, List<Integer> LakeBlocks){
        for (Integer i: IsleBlocks){
            if (!LakeBlocks.contains(i)){
                double height = elev.get(i);
                double humid = humidity.get(i);
                if (0 <= humid && humid <= 50 && 0 <= height && height < 175){
                    //BiomeTypes.add("Desert");
                    colorPolygon(206,112,44,255, i);
                    new Color(206,112,44,255);
                }
                else if (50 < humid && humid < 275 && 0 <= height && height < 50) {
                    //BiomeTypes.add("Savana");
                    colorPolygon(255,255,221,255, i);
                    new Color(255,255,221,255);
                }
                else if (275 <= humid && 0 <= height && height < 50) {
                    //BiomeTypes.add("Tropic Rain Forest");
                    colorPolygon(75,255,132,255, i);
                    new Color(75, 255, 132,255);
                }
                else if (50 < humid && humid < 100 && 50 <= height && height < 175) {
                    //BiomeTypes.add("Grassland");
                    colorPolygon(240,167,74,255, i);
                    new Color(240,167,74,255);
                }
                else if (100 <= humid && humid <= 225 && 50 <= height && height < 125) {
                    //BiomeTypes.add("Deciduous");
                    colorPolygon(49,113,79,255, i);
                    new Color(49,113,79,255);
                }
                else if (225 < humid && 50 <= height && height <= 125) {
                    //BiomeTypes.add("Temperate Rain Forest");
                    colorPolygon(163,255,181,255, i);
                    new Color(163, 255, 181,255);
                }
                else if (50 <= humid && 125 <= height && height <= 175) {
                    //BiomeTypes.add("Taiga");
                    colorPolygon(0,127,0,255, i);
                    new Color(0,127,0,255);
                }
                else if (0 <= humid && 175 <= height) {
                    //BiomeTypes.add("Tundra");
                    colorPolygon(206,221,237,255, i);
                    new Color(206,221,237,255);
                }
                else{
                    System.out.println("Humidity:"+humid+" Height:"+height);
                    colorPolygon(255,255,255,255,i);
                }

                }

            else{
                continue;
            }
        }
    }


    @Override
    public void colorPolygon(int red, int green, int blue, int alpha, int index){
        Structs.Polygon poly = polygonList.get(index);
        Structs.Property color = Structs.Property.newBuilder().setKey("rgb_color").setValue(red + "," + green + "," + blue+ "," + alpha).build();
        Structs.Polygon colored = Structs.Polygon.newBuilder(poly).addProperties(color).build();
        polygonList.set(index, colored);
    }

}



    //one method to generally set type of biome with case statements and polygon elevation input
    //check if a polygon is a lake, make a list of lakes.




