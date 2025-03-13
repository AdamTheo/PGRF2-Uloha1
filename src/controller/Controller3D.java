package controller;

import model.Vertex;
import raster.DepthBuffer;
import raster.ZBuffer;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.TriangleRasterizer;
import transforms.Col;
import transforms.Point3D;
import view.Panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller3D {
    private final Panel panel;
    private final ZBuffer Zbuffer;
    private final LineRasterizer LineRasterizer;
    private final TriangleRasterizer TriangleRasterizer;


    public Controller3D(Panel panel) {
        this.panel = panel;
        Zbuffer = new ZBuffer(panel.getRaster());
        LineRasterizer = new LineRasterizerGraphics(panel.getRaster());
        TriangleRasterizer = new TriangleRasterizer(LineRasterizer, Zbuffer);
        initListeners();

        redraw();
    }

    private void initListeners() {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

            }
        });
    }

    private void redraw() {
        panel.clear();

        Zbuffer.setPixelWithZTest(50,400,0.1,new Col(0xff0000));
        Zbuffer.setPixelWithZTest(50,50,0.5,new Col(0x00ff00));

        Vertex a = new Vertex(new Point3D(90,10,1));
        Vertex b = new Vertex(new Point3D(10,110,1));
        Vertex c = new Vertex(new Point3D(70,60,1));

        TriangleRasterizer.rasterize(a,b,c);


        panel.repaint();

    }

}
