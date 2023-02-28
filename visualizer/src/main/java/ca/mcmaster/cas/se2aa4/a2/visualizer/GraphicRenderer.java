package ca.mcmaster.cas.se2aa4.a2.visualizer;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;


import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.List;
import javax.swing.*;
import java.awt.geom.Line2D;
import java.util.Random;

public class GraphicRenderer {

    private static final int THICKNESS = 3;
    public void render(Mesh aMesh, Graphics2D canvas, String debug) {
        canvas.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(0.5f);
        canvas.setStroke(stroke);



        // FOR EACH SEGMENT IN THE MESH FILE, GET THE VERTEXES BASED ON THE V1Idx and V2Idx
        for (Segment s: aMesh.getSegmentsList()){
            Vertex vertex = aMesh.getVerticesList().get(s.getV1Idx());
            Vertex vertex2 = aMesh.getVerticesList().get(s.getV2Idx());
            // USING THE TWO VERTEXES, GET THE X AND Y VALUES OF BOTH
            double x1 = vertex.getX();
            double y1 = vertex.getY();
            double x2 = vertex2.getX();
            double y2 = vertex2.getY();
            boolean isNeighbour = false;
            for (Property p : s.getPropertiesList()) {
                // TRY TO FIND THE RGB COLOR
                if (p.getKey().equals("rgb_color")) {
                    if (p.getValue().equals("169,169,169,255")){
                        isNeighbour = true;
                        System.out.println(p.getValue().toString());
                    }
                }
            }
            System.out.println(isNeighbour);
            System.out.println(debug);
            if (debug.equals("debugOff") && !isNeighbour) {
                Color old = canvas.getColor();
                Stroke oldStroke = canvas.getStroke();
                canvas.setColor(extractColor(s.getPropertiesList()));
                Line2D seg = new Line2D.Double(x1, y1, x2, y2);
                Stroke stroke1 = new BasicStroke(2);
                canvas.setStroke(stroke1);
                canvas.draw(seg);
                canvas.setStroke(oldStroke);
                canvas.setColor(old);
            }
            else if (debug.equals("debugOn")){
                Color old = canvas.getColor();
                Stroke oldStroke = canvas.getStroke();

                if (isNeighbour){
                    System.out.println("IM A Neighbour");
                    canvas.setColor(new Color(169,169,169,50));
                } else{
                    canvas.setColor(Color.BLACK);
                }
                Line2D seg = new Line2D.Double(x1, y1, x2, y2);
                Stroke stroke1 = new BasicStroke(1);
                canvas.setStroke(stroke1);
                canvas.draw(seg);
                canvas.setStroke(oldStroke);
                canvas.setColor(old);
            }

        }

        // FOR EACH VERTEX GET THE X AND Y VALUE, GET THE COLOR AND CREATE AN ELLIPSE2D VISUALIZED DOT
        for (Vertex v: aMesh.getVerticesList()) {
            double centre_x = v.getX() - (THICKNESS/2.0d);
            double centre_y = v.getY() - (THICKNESS/2.0d);
            boolean isCentroid = false;
            for (Property p : v.getPropertiesList()) {
                // TRY TO FIND THE RGB COLOR
                if (p.getKey().equals("rgb_color")) {
                    if (p.getValue().equals("255,0,0,255")){
                        isCentroid = true;
                    }
                }
            }
            if (debug.equals("debugOff") && !isCentroid) {
                Color old = canvas.getColor();
                canvas.setColor(extractColor(v.getPropertiesList()));
                Ellipse2D point = new Ellipse2D.Double(centre_x, centre_y, THICKNESS, THICKNESS);
                canvas.fill(point);
                canvas.setColor(old);
            }
            if (debug.equals("debugOn")){
                Color old = canvas.getColor();
                if (isCentroid){
                    System.out.println("IM A CETROID");
                    canvas.setColor(Color.RED);
                } else{
                    canvas.setColor(Color.BLACK);
                }
                Ellipse2D point = new Ellipse2D.Double(centre_x, centre_y, THICKNESS, THICKNESS);
                canvas.fill(point);
                canvas.setColor(old);
            }

        }



    }


    private Color extractColor(List<Property> properties) {
        String val = null;
        // EXTRACTCOLOR GOES THROUGH ALL THE PROPERTIES OF THE OBJECT
        for(Property p: properties) {
            // TRY TO FIND THE RGB COLOR
            if (p.getKey().equals("rgb_color")) {
                val = p.getValue();
            }
        }
        if (val == null){       // IF THE RGB COLOR PROPERTY DOESNT EXIST, COVER THAT CASE BY MAKING IT BLACK
            return Color.BLACK; // COVERING CASE IF KEY RGB_COLOR DOESN'T EXIST
        }
        // IF RGB PROPERTY EXIST, GET VALUES OF EACH PARAMETER
        String[] raw = val.split(",");
        int red = Integer.parseInt(raw[0]);
        int green = Integer.parseInt(raw[1]);
        int blue = Integer.parseInt(raw[2]);
        int alpha = Integer.parseInt(raw[3]);
        // RETURN AS COLOR OBJECT
        return new Color(red, green, blue, alpha);
    }



}
