package core;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Linha2D {
    Ponto2D p1,p2;


    public Linha2D(Ponto2D a, Ponto2D b) {
        super();
        this.p1 = a;
        this.p2 = b;
    }
    public void draw(Graphics2D g, byte[] bufferDeVideo) {
        g.setColor(Color.red);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBresenhamLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y, bufferDeVideo);
    }

    public void drawPixel(int x, int y, int r, int g, int b, byte[] buffer) {
        int pospix = y*(800*4)+x*4;

        buffer[pospix] = (byte)255;
        buffer[pospix+1] = (byte)(b&0xff);
        buffer[pospix+2] = (byte)(g&0xff);
        buffer[pospix+3] = (byte)(r&0xff);

    }

    public void drawBresenhamLine(int x1, int y1, int x2, int y2, byte[] buffer){
            int dx = Math.abs(x2 - x1);
            int dy = Math.abs(y2 - y1);
            int sx = (x1 < x2) ? 1 : -1; // Set X direction
            int sy = (y1 < y2) ? 1 : -1; // Set Y direction
            int err = dx - dy;  // error

            while (true){
                drawPixel(x1, y1, 204, 0, 136,buffer);
                if (x1 == x2 && y1 == y2) break;

                int e2 = 2 * err;

                if (e2 > -dy) {
                    err -= dy;
                    x1 += sx; // Move to X direction
                }

                if (e2 < dx) {
                    err += dx;
                    y1 += sy; // Move to Y direction
                }
            }
    }

}
