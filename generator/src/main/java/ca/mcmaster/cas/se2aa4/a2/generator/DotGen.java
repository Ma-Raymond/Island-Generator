package ca.mcmaster.cas.se2aa4.a2.generator;

import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.List;


import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;

public class DotGen {

    private final int width = 500;
    private final int height = 500;
    private final int square_size = 20;

    public List<Segment> segmentMaker(Integer i,Integer j, Integer[][] map, Set<Segment> segments,Set<Integer> visited){
        Integer[][] directions = {{0,1},{0,-1},{1,0},{-1,0}};
        List<Segment> segmentList = new ArrayList<Segment>();
        for (Integer[] dir : directions){
            int x = i + dir[0];
            int y = j + dir[1];

            if (x >= 0 && x < map.length && y >= 0 && y < map[0].length && !visited.contains(map[x][y])){
                Segment current = Segment.newBuilder().setV1Idx(map[i][j]).setV2Idx(map[x][y]).build();
                if (!segments.contains(current)){
                    segments.add(current);
                    segmentList.add(current);
                }
            }
        }
        return segmentList;
    }

    public Mesh generate() {
        Set<Vertex> vertices = new HashSet<>();
        // Create all the vertices
        Set<Segment> segments = new HashSet<>();
        // Hashmap = { i : (x,y) }
        int count = 0;
        Integer[][] vertexMap = new Integer[height/square_size][width/square_size];
        // [ 0  1  3  4  5  6   7... 24  ]
        // [ 25 26 ...              ]
        // [                ]
        // [                ]
        // [                ]

        int i = 0;
        int j = 0;
        List<Vertex> vertexList = new ArrayList<Vertex>();
        for(int x = 0; x < width; x += square_size) {
            for(int y = 0; y < height; y += square_size) {
                vertexMap[i][j] = count;
                Vertex current = Vertex.newBuilder().setX((double) x).setY((double) y).build();
                vertices.add(current);

                vertexList.add(current);
                count += 1;
                j += 1;
            }
            j=0;
            i += 1;
        }

        Set<Integer> visited = new HashSet<Integer>();
        List<Segment> segmentList = new ArrayList<Segment>();
        for (int x=0; x< vertexMap.length; x++){
            for (int y=0; y< vertexMap[0].length; y++){
                segmentList.addAll(segmentMaker(x,y,vertexMap,segments,visited));
                visited.add(vertexMap[x][y]);
            }
        }

        // Distribute colors randomly. Vertices are immutable, need to enrich them
        List<Vertex> verticesWithColors = new ArrayList<>();
        Random bag = new Random();
        for(Vertex v: vertexList){
            int red = bag.nextInt(255);
            int green = bag.nextInt(255);
            int blue = bag.nextInt(255);
            String colorCode = red + "," + green + "," + blue;
            Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
            Vertex colored = Vertex.newBuilder(v).addProperties(color).build();
            verticesWithColors.add(colored);
        }

        //Coloring the Segments
        Set<Segment> segmentsWithColors = new HashSet<>();
        ArrayList<Vertex> vertexToVertexMap = new ArrayList<Vertex>();
        vertexToVertexMap.addAll(verticesWithColors);
        for (Segment s : segmentList){
//            System.out.println("V1: " + extractColor(vertexToVertexMap.get(s.getV1Idx()).getPropertiesList()) );
//            System.out.println("V2: " + extractColor(vertexToVertexMap.get(s.getV2Idx()).getPropertiesList()) );
            Color v1Color = extractColor(vertexToVertexMap.get(s.getV1Idx()).getPropertiesList());
            Color v2Color = extractColor(vertexToVertexMap.get(s.getV2Idx()).getPropertiesList());
            int red = (v1Color.getRed()+v2Color.getRed())/2;
            int green = (v1Color.getGreen()+v2Color.getGreen())/2;
            int blue = (v1Color.getBlue()+v2Color.getBlue())/2;
//            Color segmentColour = new Color((v1Color.getRed()+v2Color.getRed())/2,(v1Color.getGreen()+v2Color.getGreen())/2, (v1Color.getBlue()+v2Color.getBlue())/2);
            Property color = Property.newBuilder().setKey("rgb_color").setValue(red + "," + green + "," + blue).build();
            Segment colored = Segment.newBuilder(s).addProperties(color).build();
            segmentsWithColors.add(colored);
        }
        for (Segment s: segmentsWithColors){
            System.out.println(s);
        }

        return Mesh.newBuilder().addAllVertices(verticesWithColors).addAllSegments(segmentsWithColors).build();
    }

    private Color extractColor(List<Property> properties) {
        String val = null;
        for(Property p: properties) {
            if (p.getKey().equals("rgb_color")) {
                System.out.println(p.getValue());
                val = p.getValue();
            }
        }
        if (val == null)
            return Color.BLACK;
        String[] raw = val.split(",");
        int red = Integer.parseInt(raw[0]);
        int green = Integer.parseInt(raw[1]);
        int blue = Integer.parseInt(raw[2]);
        return new Color(red, green, blue);
    }



}
