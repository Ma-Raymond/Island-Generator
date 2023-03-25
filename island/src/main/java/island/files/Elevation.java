package island.files;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.text.DecimalFormat;
import java.util.*;

public class Elevation {
    int altStartIdx;
    List<Integer> heightPoints;
    List<Structs.Polygon> polygonList;
    List<Double> elevations;
    List<Integer> islandBlocks;
    int altType;
    DecimalFormat precision  = new DecimalFormat("0.00");

    public void generate(int type, List<Double> elevate,int startIdx, List<Integer> hPoints, List<Structs.Polygon> pList, List<Integer> iBlocks){
        altStartIdx = startIdx;
        elevations = elevate;
        heightPoints = hPoints;
        polygonList = pList;
        islandBlocks = iBlocks;
        altType = type;
        selectElevation(altType);
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
        for (int i = 0; i < heightPoints.size()/4; i++){
            int Idx = (startIdx + i) % heightPoints.size();
            int polyIdx = heightPoints.get(Idx);
            Structs.Polygon poly = polygonList.get(polyIdx);
            List<Integer> neighbourList = poly.getNeighborIdxsList();
            double elevationVal = Double.parseDouble(precision.format(elevations.get(polyIdx)+10));
            elevations.set(polyIdx,elevationVal);
            for (Integer j : neighbourList){
                double elevationVal2 = Double.parseDouble(precision.format(elevations.get(polyIdx)+1));
                elevations.set(j,elevationVal2);
            }
        }
    }
    private void volcano(int startIdx){
        altStartIdx = startIdx % heightPoints.size();
        Deque<Integer> deque = new ArrayDeque<>();
        Set<Integer> visited = new HashSet<>();
        int polyIdx = heightPoints.get(startIdx);
        double volcanoHeight = 100.0;
        visited.add(polyIdx);
        deque.add(polyIdx);
        double visual = 5;
        while (!deque.isEmpty()){
            int idxVal = deque.removeFirst();
            Structs.Polygon poly = polygonList.get(idxVal);
            elevations.set(idxVal,elevations.get(idxVal)+volcanoHeight);
            List<Integer> neighbourList = poly.getNeighborIdxsList();
            for (Integer idx : neighbourList){
                if (!visited.contains(idx) && islandBlocks.contains(idx)){
                    visited.add(idx);
                    deque.add(idx);
                }
            }
            visual -= 0.01;
            if (volcanoHeight >0)
                volcanoHeight = Double.parseDouble(precision.format(volcanoHeight-0.2));
        }
    }
}
