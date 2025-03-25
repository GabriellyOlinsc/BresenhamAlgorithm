package core;

import java.util.ArrayList;
import java.util.List;

public class Clipping {
    private static final int INSIDE = 0; // 0000
    private static final int LEFT = 1;   // 0001
    private static final int RIGHT = 2;  // 0010
    private static final int BOTTOM = 4; // 0100
    private static final int TOP = 8;  //1000
    private final int xMin, yMin, xMax, yMax;

    public Clipping(int xMin, int yMin, int xMax, int yMax){
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    private int computeOutCode(double x, double y){
        int code = INSIDE;

        if (x < xMin) code |= LEFT;
        if (x > xMax) code |= RIGHT;
        if (y < yMin) code |= BOTTOM;
        if (y > yMax) code |= TOP;

        return code;
    }

    //Cohen-Sutherland algorithm from p1 to p2
    public Linha2D cohenSutherlandClip(Linha2D line){
        double x1 = line.p1.x, y1 = line.p1.y;
        double x2 = line.p2.x, y2 = line.p2.y;

        int outcode1 = computeOutCode(x1, y1);
        int outcode2 = computeOutCode(x2, y2);

        boolean accept = false;

        while (true) {
            if ((outcode1 | outcode2) == 0) { //Inside
                accept = true;
                break;
            } else if ((outcode1 & outcode2) != 0) { //Outside
                break;
            } else {
                //Cut line
                int outcodeOut = (outcode1 != 0) ? outcode1 : outcode2;
                double x, y;

                //Formula y = y1 + slope * (x-x1)

                if ((outcodeOut & TOP) != 0) { // Above area
                    x = x1 + (x2 - x1) * (yMax - y1) / (y2 - y1);
                    y = yMax;
                } else if ((outcodeOut & BOTTOM) != 0) { // Bellow area
                    x = x1 + (x2 - x1) * (yMin - y1) / (y2 - y1);
                    y = yMin;
                } else if ((outcodeOut & RIGHT) != 0) { // Right
                    y = y1 + (y2 - y1) * (xMax - x1) / (x2 - x1);
                    x = xMax;
                } else { // Left
                    y = y1 + (y2 - y1) * (xMin - x1) /(x2 - x1);
                    x = xMin;
                }

                if (outcodeOut == outcode1) {
                    x1 = x;
                    y1 = y;
                    outcode1 = computeOutCode(x1, y1);
                } else {
                    x2 = x;
                    y2 = y;
                    outcode2 = computeOutCode(x2, y2);
                }
            }
        }

        //return accept ? new Linha2D(new Ponto2D(x1, y1), new Ponto2D(x2, y2)) : null;

        if (accept) {
            return new Linha2D(new Ponto2D(x1, y1), new Ponto2D(x2, y2));
        }

        return null;
    }

}
