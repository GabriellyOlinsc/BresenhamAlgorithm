import core.Clipping;
import core.Linha2D;
import core.Ponto2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class MainCanvas extends JPanel implements Runnable{
    int W = 800;
    int H = 600;

    Thread runner;
    boolean ativo = true;
    int paintcounter = 0;

    BufferedImage imageBuffer;
    int framecount = 0;
    int fps = 0;

    Font f = new Font("", Font.PLAIN, 30);

    int clickX = 0;
    int clickY = 0;
    int mouseX = 0;
    int mouseY = 0;

    private final int xMin = 50;
    private final int yMin = 50;
    private final int xMax = W - 50;
    private final int yMax = H - 50;

    boolean LEFT = false;
    boolean RIGHT = false;
    boolean UP = false;
    boolean DOWN = false;

    private Ponto2D drawLine = null;
    private final ArrayList<Linha2D> lineList = new ArrayList<>();
    private final Clipping clipping;

    public MainCanvas() {
        setPreferredSize(new Dimension(H,W));
        setFocusable(true);
        clipping = new Clipping(xMin, yMin, xMax, yMax);

        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_W) {
                    UP = false;
                }
                if(key == KeyEvent.VK_S) {
                    DOWN = false;
                }
                if(key == KeyEvent.VK_A) {
                    LEFT = false;
                }
                if(key == KeyEvent.VK_D) {
                    RIGHT = false;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_W) {
                    UP = true;
                }
                if(key == KeyEvent.VK_S) {
                    DOWN = true;
                }
                if(key == KeyEvent.VK_A) {
                    LEFT = true;
                }
                if(key == KeyEvent.VK_D) {
                    RIGHT = true;
                }
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                clickX = e.getX();
                clickY = e.getY();

                if(drawLine == null){
                    drawLine = new Ponto2D(clickX, clickY);
                }else{
                    Ponto2D p2 = new Ponto2D(clickX, clickY);
                    Linha2D line = new Linha2D(drawLine, p2);
                    lineList.add(line);
                    drawLine = null;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub

            }
        });

        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent arg0) {
                // TODO Auto-generated method stub
                mouseX = arg0.getX();
                mouseY = arg0.getY();
            }

            @Override
            public void mouseDragged(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }
        });



    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setFont(f);

        g.setColor(Color.white);
        g.fillRect(0, 0, W, H);

		g.setColor(Color.green);
        //g.drawRect(xMin, yMin, 700, 500);
        g.drawRect(xMin, yMin, xMax - xMin, yMax - yMin);


        g.setColor(Color.black);
        Graphics2D g2d = (Graphics2D) g;
        for (Linha2D linha : lineList) {
            Linha2D clippedLine = clipping.cohenSutherlandClip(linha);
            if (clippedLine != null) { // Verifica se a linha não foi completamente recortada
                clippedLine.draw(g2d);
            }
        }
        System.out.println("Clipping Area: (" + xMin + ", " + yMin + ") -> (" + xMax + ", " + yMax + ")");
        //System.out.println("Line Start: (" + x1 + ", " + y1 + ") End: (" + x2 + ", " + y2 + ")");

        g.setColor(Color.red);
        if(drawLine!=null) {
            g.drawLine((int)drawLine.x, (int)drawLine.y, mouseX, mouseY);
        }


        g.drawImage(imageBuffer,0,0,null);

        g.setColor(Color.black);
        g.drawString("FPS "+fps, 10, 25);
    }

    public void start(){
        runner = new Thread(this);
        runner.start();
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        long segundo = time/1000;
        while(ativo){
            repaint(0, 0, W, H);
            paintcounter+=100;

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            long newtime = System.currentTimeMillis();
            long novoSegundo = newtime/1000;

            framecount++;
            if(novoSegundo!=segundo) {
                fps = framecount;
                framecount = 0;
                segundo = novoSegundo;
            }
        }
    }
}

