package controller;

import model.Vertex;
import raster.DepthBuffer;
import raster.ZBuffer;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.LineRasterizerWithZ;
import rasterize.TriangleRasterizer;
import solid.*;
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
    private final LineRasterizerWithZ lineRasterizerWithZ;
    private Camera camera;
    private final Mat4PerspRH proj;
    private final Mat4OrthoRH orth;
    private int perspect;
    private List<Solid> solidList = new ArrayList<>();
    private int chosenObject = 0; //Index of currently chosen object
    private final Solid cube;
    private final Solid pyramid;
    private final Solid pillar;
    Solid chosen; // Used for determining currently selected solid

    private final AxisX axisX;
    private final AxisY axisY;
    private final AxisZ axisZ;
    private List<Solid> axis = new ArrayList<>();
    private List<Col> activeColors = new ArrayList<>(); // Used to store colors of active chosen SOlid

    int startX;
    int startY;

    public Controller3D(Panel panel) {
        this.panel = panel;
        Zbuffer = new ZBuffer(panel.getRaster());
        LineRasterizer = new LineRasterizerGraphics(panel.getRaster());
        lineRasterizerWithZ = new LineRasterizerWithZ(Zbuffer);
        TriangleRasterizer = new TriangleRasterizer(LineRasterizer, Zbuffer);
        cube = new Cube();
        cube.setModel(new Mat4Transl(0.2, 0.4, 0.2));
        pyramid = new Pyramid();
        pyramid.setModel(new Mat4Transl(0.2, 0.4, 0.2));
        pillar = new Pillar();
        pillar.setModel(new Mat4Transl(0.3, -0.4, 0.1));
        chosen = cube; // This is dummy value so that Ida doesnt yell at me

        solidList.add(cube);
        solidList.add(pyramid);
        solidList.add(pillar);

        axisX = new AxisX();
        axisY = new AxisY();
        axisZ = new AxisZ();

        axis.add(axisX);
        axis.add(axisY);
        axis.add(axisZ);

        renderer = new Renderer(
                new LineRasterizerWithZ(Zbuffer),
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
        perspect = 0; // 0 = projective, 1 = orthogonal.
        initListeners();
        redraw();
    }

    private void initListeners() {
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

                //Basically when chosenObject = 0, we update camera. Otherwise we are updating currently selected object

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
                        perspect = (perspect + 1) % 2;
                        if (perspect == 0) {
                            renderer.setProjection(proj);
                        }
                        if (perspect == 1) {
                            renderer.setProjection(orth);
                        }
                        break;

                    case KeyEvent.VK_C: // Changing chosen object
                        chosenObject = (chosenObject + 1) % 4;
                        if (chosenObject == 0) {
                            if (activeColors.size() > 0) {
                                pillar.setAllColors(activeColors);
                            }
                        }

                        if (chosenObject == 1) {
                            chosen = pyramid;
                            activeColors = chosen.getColors();
                            chosen.setActiveColors();
                        }
                        if (chosenObject == 2) {
                            pyramid.setAllColors(activeColors);
                            chosen = cube;
                            activeColors = chosen.getColors();
                            chosen.setActiveColors();
                        }
                        if (chosenObject == 3) {
                            cube.setAllColors(activeColors);
                            chosen = pillar;
                            activeColors = chosen.getColors();
                            chosen.setActiveColors();
                        }
                        break;


                    case KeyEvent.VK_M:
                        if (chosenObject == 0) {
                            camera = camera.up(0.05);
                            renderer.setView(camera.getViewMatrix()); // Update camera
                        } else {
                            chosen.setModel(chosen.getModel().mul(new Mat4Scale(1.1)));
                        }
                        break;

                    case KeyEvent.VK_N:
                        if (chosenObject == 0) {
                            camera = camera.down(0.05);
                            renderer.setView(camera.getViewMatrix()); // Update camera
                        } else {
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
        Zbuffer.clear();
        renderer.renderSolids(axis);
        renderer.renderSolids(solidList);
        panel.repaint();
    }

}
