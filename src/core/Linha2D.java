package core;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Linha2D {
    Ponto2D p1,p2;
    private final List<Ponto2D> buffer;


    public Linha2D(Ponto2D a, Ponto2D b) {
        this.p1 = a;
        this.p2 = b;
        this.buffer = new ArrayList<>();
    }
    public void draw(Graphics2D g) {
        g.setColor(Color.red);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        buffer.clear();
        drawBresenhamLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y, g);

        //Desenha todos os pontos armazenados no buffer de uma só vez
        for (Ponto2D ponto : buffer) {
            g.fillRect((int) ponto.x, (int) ponto.y, 1, 1);
        }
    }

    public void drawBresenhamLine(int x1, int y1, int x2, int y2, Graphics2D g){
            int dx = Math.abs(x2 - x1);
            int dy = Math.abs(y2 - y1);
            int sx = (x1 < x2) ? 1 : -1; // Set X direction
            int sy = (y1 < y2) ? 1 : -1; // Set Y direction
            int err = dx - dy;  // error

            while (true){
                buffer.add(new Ponto2D(x1, y1));
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
    public List<Ponto2D> getBuffer() {
        return buffer;
    }

}
