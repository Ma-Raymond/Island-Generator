package island.files;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.text.DecimalFormat;
import java.util.List;

public class Aquifers{
    List<Structs.Polygon> polygonList;
    List<Integer> heightPoints;
    List<Double> humidity;
    int aquaStartIdx;
    int aquaNum;
    double soilPercent;

    DecimalFormat precision  = new DecimalFormat("0.00");

    public void generate(List<Integer> hP, int startIdx, List<Structs.Polygon> polygons,  List<Double> humidityList, double soilPer, int numAquifers){
        heightPoints = hP;
        aquaStartIdx = startIdx;
        polygonList = polygons;
        humidity = humidityList;
        soilPercent = soilPer;
        aquaNum = numAquifers;

        createAquifers(aquaNum, aquaStartIdx);

    }

    private void createAquifers(int aquaNum, int startIndexA){
        //int startIndexL = randNum.nextInt(heightPoints.size()); //put this in the generator in an if statement
        for (int i = 0; i < aquaNum; i ++){
            int polyIndex = (startIndexA + i) % heightPoints.size();
            int validPolyId = heightPoints.get(polyIndex);
//            colorPolygon(102, 178,255,255, validPolyId);
            addAquaHumidity(validPolyId);
        }
        aquaStartIdx = startIndexA;

    }
    private void addAquaHumidity(int aquaPoly){
        Structs.Polygon poly = polygonList.get(aquaPoly);
        double humidityValAqua = Double.parseDouble(precision.format(humidity.get(aquaPoly)+150*soilPercent));
        humidity.set(aquaPoly, humidityValAqua);
        for (Integer n : poly.getNeighborIdxsList()){
            double humidityValNeigbours = Double.parseDouble(precision.format(humidity.get(n)+100*soilPercent));
            humidity.set(n,humidityValNeigbours);
        }
    }
}
