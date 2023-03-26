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
            generateHilly(altStartIdx);
        }
    }
    private void generateHills(int startIdx){
        // Have it incrementally do it with the seed
        for (int i = 0; i < heightPoints.size()/4; i++){
            int Idx = (startIdx + i) % heightPoints.size();
            int polyIdx = heightPoints.get(Idx);
            Structs.Polygon poly = polygonList.get(polyIdx);
            List<Integer> neighbourList = poly.getNeighborIdxsList();
            double elevationVal = Double.parseDouble(precision.format(elevations.get(polyIdx)+15));
            elevations.set(polyIdx,elevationVal);
            for (Integer j : neighbourList){
                double elevationVal2 = Double.parseDouble(precision.format(elevations.get(polyIdx)+1));
                elevations.set(j,elevationVal2);
            }
        }
    }
    private void generateHilly(int startIdx){
        // Have it incrementally do it with the seed
        for (int i = 0; i < heightPoints.size()/4; i++){
            Deque<Integer> deque = new ArrayDeque<>();
            Set<Integer> visited = new HashSet<>();
            int growthPoint = (startIdx + i) % heightPoints.size();
            int polyIdx = heightPoints.get(growthPoint);
            double hillHeight = 20.0;
            visited.add(polyIdx);
            deque.add(polyIdx);
            while (!deque.isEmpty()){
                int idxVal = deque.removeFirst();
                Structs.Polygon poly = polygonList.get(idxVal);
//                System.out.println("Current:"+elevations.get(idxVal)+ "HILL: "+hillHeight+" OUTCOME: "+(elevations.get(idxVal)+hillHeight));
                double val = elevations.get(idxVal) + hillHeight;
//                System.out.println("Current:"+elevations.get(idxVal)+"VAL: "+val);
                elevations.set(idxVal,Double.parseDouble(precision.format(val)));
                List<Integer> neighbourList = poly.getNeighborIdxsList();
                for (Integer idx : neighbourList){
                    if (!visited.contains(idx) && islandBlocks.contains(idx)){
                        visited.add(idx);
                        deque.add(idx);
                    }
                }
                if (hillHeight > 0){
                    hillHeight = Double.parseDouble(precision.format(hillHeight-5.0));
                }
            }
        }
    }
    private void volcano(int startIdx){
        Deque<Integer> deque = new ArrayDeque<>();
        Set<Integer> visited = new HashSet<>();
        int polyIdx = heightPoints.get(startIdx);
        double volcanoHeight = 100.0;
        visited.add(polyIdx);
        deque.add(polyIdx);
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
            if (volcanoHeight >0)
                volcanoHeight = Double.parseDouble(precision.format(volcanoHeight-0.2));
        }
    }
}
