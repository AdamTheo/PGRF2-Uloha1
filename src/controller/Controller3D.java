package controller;

import model.Vertex;
import raster.DepthBuffer;
import raster.ZBuffer;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.TriangleRasterizer;
import solid.AxisX;
import solid.Cube;
import solid.Solid;
import transforms.*;
import view.Panel;
import renderer.Renderer;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller3D {
    private final Panel panel;
    private final ZBuffer Zbuffer;
    private final Renderer renderer;
    private final LineRasterizer LineRasterizer;
    private final TriangleRasterizer TriangleRasterizer;
    private Camera camera;
    private final Mat4PerspRH proj;
    private final Mat4OrthoRH orth;
    private List<Solid> solidList = new ArrayList<>();
    private int chosenObject = 0;
    private final Solid cube;

    int startX;
    int startY;

    public Controller3D(Panel panel) {
        this.panel = panel;
        Zbuffer = new ZBuffer(panel.getRaster());
        LineRasterizer = new LineRasterizerGraphics(panel.getRaster());
        TriangleRasterizer = new TriangleRasterizer(LineRasterizer, Zbuffer);
        initListeners();
        cube = new Cube();
        cube.setModel(new Mat4Transl(0.2,0.4,0.2));

        renderer = new Renderer(
                new LineRasterizerGraphics(panel.getRaster()),
                TriangleRasterizer,
                panel.getRaster().getWidth(),
                panel.getRaster().getHeight()
        );

        camera = new Camera()
                .withPosition(new Vec3D(0.5, -1.5, 1))
                .withAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-25))
                .withFirstPerson(true);

        proj = new Mat4PerspRH(Math.toRadians(60), panel.getRaster().getHeight() / (float) panel.getRaster().getWidth(), 0.01, 200);
        orth = new Mat4OrthoRH(4, (float) 4 * panel.getWidth() / panel.getHeight(), 0.01, 200);

        renderer.setProjection(proj); // Initial perspective
        renderer.setView(camera.getViewMatrix()); // Setting basic camera.

        redraw();
    }

    private void initListeners() {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {



            }
        });

        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                camera = camera.addAzimuth(Math.PI * (e.getX() - startX) / (double) panel.getWidth());
                camera = camera.addZenith(Math.PI * (e.getY() - startY) / (double) panel.getHeight());

                if (camera.getZenith() >= 90) { // Preventing to spin out of control
                    camera = camera.withZenith(90);
                }
                if (camera.getZenith() <= -90) {
                    camera = camera.withZenith(-90);
                }
                startX = e.getX();
                startY = e.getY();
                renderer.setView(camera.getViewMatrix()); // Updating camera only when we need to
                redraw();
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                Solid chosen; // the currently chosen objest for edit
                chosen = cube; // Dummy value so that Idea doesnt scream
                if (chosenObject == 1) {
                    //chosen = pyramid;
                }
                if (chosenObject == 2) {
                    //chosen = block;
                }

                switch (e.getKeyCode()) { // what key is pressed
                    case KeyEvent.VK_W:
                        if (chosenObject == 0) {
                            camera = camera.forward(0.05);
                            renderer.setView(camera.getViewMatrix()); //Update camera
                        } else {
                            chosen.setModel(chosen.getModel().mul(new Mat4Transl(0, 0.05, 0)));
                        }
                        break;

                    case KeyEvent.VK_S:
                        if (chosenObject == 0) {
                            camera = camera.backward(0.05);
                            renderer.setView(camera.getViewMatrix()); // Update camera
                        } else {
                            chosen.setModel(chosen.getModel().mul(new Mat4Transl(0, -0.05, 0)));
                        }
                        break;

                    case KeyEvent.VK_A:
                        if (chosenObject == 0) {
                            camera = camera.left(0.05);
                            renderer.setView(camera.getViewMatrix()); // Update camera
                        } else {
                            chosen.setModel(chosen.getModel().mul(new Mat4Transl(-0.05, 0, 0)));
                        }
                        break;

                    case KeyEvent.VK_D:
                        if (chosenObject == 0) {
                            camera = camera.right(0.05);
                            renderer.setView(camera.getViewMatrix()); // Update camera
                        } else {
                            chosen.setModel(chosen.getModel().mul(new Mat4Transl(0.05, 0, 0)));
                        }
                        break;

                    case KeyEvent.VK_P: // Changing projection
                        /*perspect = (perspect + 1) % 2;
                        if (perspect == 0) {
                            renderer.setProjection(proj);
                        }
                        if (perspect == 1) {
                            renderer.setProjection(orth);
                        }*/
                        break;

                    case KeyEvent.VK_C: // Changing chosen object
                        chosenObject = (chosenObject + 1) % 3;
                        //pyramid.setChosen(false);
                        //block.setChosen(false);
                        if (chosenObject == 1) {
                        //    pyramid.setChosen(true);
                        }
                        if (chosenObject == 2) {
                        //    block.setChosen(true);
                        }
                        break;


                    case KeyEvent.VK_M:
                        if (chosenObject == 0) {
                            camera = camera.up(0.05);
                            renderer.setView(camera.getViewMatrix()); // Update camera
                        }else{
                            chosen.setModel(chosen.getModel().mul(new Mat4Scale(1.1)));
                        }
                        break;

                    case KeyEvent.VK_N:
                        if (chosenObject == 0) {
                            camera = camera.down(0.05);
                            renderer.setView(camera.getViewMatrix()); // Update camera
                        }else{
                            chosen.setModel(chosen.getModel().mul(new Mat4Scale(0.9)));
                        }
                        break;

                    case KeyEvent.VK_J: // X Axis rotation
                        if (chosenObject != 0) {
                            Mat4 model = chosen.getModel();
                            Vec3D posun = model.getTranslate();
                            chosen.setModel(model.mul(new Mat4Transl(posun.mul(-1))).mul(new Mat4RotX(Math.toRadians(10))).mul(new Mat4Transl(posun)));
                        }
                        break;

                    case KeyEvent.VK_K: // Y Axis rotation
                        if (chosenObject != 0) {
                            Mat4 model = chosen.getModel();
                            Vec3D posun = model.getTranslate();
                            chosen.setModel(model.mul(new Mat4Transl(posun.mul(-1))).mul(new Mat4RotY(Math.toRadians(10))).mul(new Mat4Transl(posun)));
                        }
                        break;

                    case KeyEvent.VK_L: // Z Axis rotation
                        if (chosenObject != 0) {
                            Mat4 model = chosen.getModel();
                            Vec3D posun = model.getTranslate();
                            chosen.setModel(model.mul(new Mat4Transl(posun.mul(-1))).mul(new Mat4RotZ(Math.toRadians(10))).mul(new Mat4Transl(posun)));
                        }
                        break;

                }
                redraw();
            }
        });
    }

    private void redraw() {
        panel.clear();

        //Zbuffer.setPixelWithZTest(50,400,0.1,new Col(0xff0000));
        //Zbuffer.setPixelWithZTest(50,50,0.5,new Col(0x00ff00));

        //Vertex a = new Vertex(new Point3D(-200,-10,0.3),new Col(0xff0000));
        //Vertex b = new Vertex(new Point3D(500,900,0.5),new Col(0x00ff00));
        //Vertex c = new Vertex(new Point3D(1200,60,0.7),new Col(0x0000ff));

        //TriangleRasterizer.rasterize(a,b,c);
        Zbuffer.clear();

        //AxisX axisX = new AxisX();
        //renderer.renderSolid(axisX);
        renderer.renderSolid(cube);
        //cube.test();



        panel.repaint();

    }

}
