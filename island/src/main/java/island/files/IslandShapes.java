package island.files;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.util.List;
import java.util.Random;

public class IslandShapes implements IslandColour{
    Structs.Mesh aMesh;
    List<Structs.Polygon> polygonList;
    List<Structs.Vertex> vertexList;

    public void islandSelector(int shapeSeed, Structs.Mesh mesh, List<Structs.Vertex> vertexes, List<Structs.Polygon> polygons){

        aMesh = mesh;
        vertexList = vertexes;
        polygonList = polygons;

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

    }

    private void circleIsland(Structs.Mesh aMesh){
        for (int i =0; i< aMesh.getPolygonsCount(); i++){
            Structs.Polygon poly = polygonList.get(i);
            Structs.Vertex centroid = vertexList.get(poly.getCentroidIdx());
            double x = centroid.getX();
            double y = centroid.getY();
            double distance = Math.sqrt(Math.pow(x-255,2)+Math.pow(y-255,2));

            //island
            if (distance < 200){
                colorPolygon(253, 255,208,255, i);
            }
            else{
                colorPolygon( 35, 85,138,255, i);
            }
        }
    }
    private void crossIsland(Structs.Mesh aMesh){
        for (int i =0; i< aMesh.getPolygonsCount(); i++){
            Structs.Polygon poly = polygonList.get(i);
            Structs.Vertex centroid = vertexList.get(poly.getCentroidIdx());
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

    private void moonIsland(Structs.Mesh aMesh){
        Random bag = new Random();
        for (int i =0; i< aMesh.getPolygonsCount(); i++){
            Structs.Polygon poly = polygonList.get(i);
            Structs.Vertex centroid = vertexList.get(poly.getCentroidIdx());
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
    private void ovalIsland(Structs.Mesh aMesh){
        int a = 200;
        int b = 100;
        for (int i =0; i< aMesh.getPolygonsCount(); i++){
            Structs.Polygon poly = polygonList.get(i);
            Structs.Vertex centroid = vertexList.get(poly.getCentroidIdx());
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

    @Override
    public void colorPolygon(int red, int green, int blue, int alpha, int index){
        Structs.Polygon poly = polygonList.get(index);
        Structs.Property color = Structs.Property.newBuilder().setKey("rgb_color").setValue(red + "," + green + "," + blue+ "," + alpha).build();
        Structs.Polygon colored = Structs.Polygon.newBuilder(poly).addProperties(color).build();
        polygonList.set(index, colored);
    }

}
