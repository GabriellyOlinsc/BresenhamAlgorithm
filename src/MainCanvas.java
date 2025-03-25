import core.Clipping;
import core.Linha2D;
import core.Ponto2D;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MainCanvas extends JPanel implements Runnable{
    int W = 800;
    int H = 600;

    Thread runner;
    boolean ativo = true;
    int paintcounter = 0;

    BufferedImage imageBuffer;
    byte bufferDeVideo[];
    int framecount = 0;
    int fps = 0;

    Font f = new Font("", Font.PLAIN, 30);

    int clickX = 0;
    int clickY = 0;
    int mouseX = 0;
    int mouseY = 0;
    BufferedImage imgtmp = null;

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


        imageBuffer = new BufferedImage(W,H, BufferedImage.TYPE_4BYTE_ABGR);
        bufferDeVideo = ((DataBufferByte)imageBuffer.getRaster().getDataBuffer()).getData();

        System.out.println("Buffer SIZE "+bufferDeVideo.length );

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
                    Linha2D clippedLine = clipping.cohenSutherlandClip(line);
                    if (clippedLine != null) {
                        lineList.add(clippedLine);
                    }
                    drawLine = null;
                    repaint();
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

        //limpar buffer antes de desenhar
        for(int i = 0; i < bufferDeVideo.length; i++) {
            bufferDeVideo[i] = 0;
        }

        g.setColor(Color.white);
        g.fillRect(0, 0, W, H);

        Graphics2D g2d = (Graphics2D) g;
        for (Linha2D linha : lineList) {
            linha.draw(g2d, bufferDeVideo);
        }

        //Area do clipping
		g.setColor(Color.green);
        g.drawRect(xMin, yMin, xMax - xMin, yMax - yMin);

        // Desenha a linha temporária (durante o arraste do mouse)
        g.setColor(Color.red);
        if (drawLine != null) {
            g.drawLine((int)drawLine.x, (int)drawLine.y, mouseX, mouseY);
        }

        // Exibe o buffer na tela
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

    public BufferedImage loadImage(String filename) {
        try {
            imgtmp = ImageIO.read(new File(filename));

            BufferedImage imgout = new BufferedImage(imgtmp.getWidth(), imgtmp.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

            imgout.getGraphics().drawImage(imgtmp, 0, 0, null);

            imgtmp = null;

            return imgout;
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
    }
}

