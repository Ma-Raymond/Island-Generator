/*
 * Copyright (c) 2016 Vivid Solutions.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v20.html
 * and the Eclipse Distribution License is available at
 *
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.locationtech.jts.operation.polygonize.Polygonizer;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;


/**
 * Shows basic ways of creating and operating on geometries
 *
 * @version 1.7
 */
public class BasicExample
{


    public static void main(String[] args)
            throws Exception
    {
         int WIDTH = 500;
         int HEIGHT = 500;
         int points = 100;
        Random xVal = new Random();
        Random yVal = new Random();
        Collection cords = new ArrayList<Coordinate>();

        Coordinate[] coords = new Coordinate[points];
        for (int i = 0; i < points; i++) {
            int xCoord = xVal.nextInt(WIDTH);
            int yCoord = yVal.nextInt(HEIGHT);
            coords[i] = (new Coordinate(xCoord,yCoord));
            cords.add(new Coordinate(xCoord,yCoord));
        }
        Geometry g0 = new GeometryFactory().createLineString(coords);
        VoronoiDiagramBuilder newDiagram = new VoronoiDiagramBuilder();
        newDiagram.setSites(cords);
        GeometryFactory geomFact = new GeometryFactory();

        Geometry coll = newDiagram.getDiagram(geomFact);
        coll.getFactory();
        System.out.println(coll.getArea());
        System.out.println(newDiagram);
        Polygonizer polygonizer = new Polygonizer();
        polygonizer.add(coll);
        System.out.println(polygonizer.getPolygons());
        List<Polygon> polys = new ArrayList<Polygon>(polygonizer.getPolygons());

        for (int i = 0; i < polygonizer.getPolygons().size(); i++){
            System.out.println(polys.get(i));
        }

    }
}