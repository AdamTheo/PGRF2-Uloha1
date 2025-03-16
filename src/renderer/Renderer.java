package renderer;

import model.Vertex;
import rasterize.LineRasterizer;
import rasterize.TriangleRasterizer;
import model.Part;
import solid.Solid;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private TriangleRasterizer triangleRasterizer;
    private LineRasterizer lineRasterizer;
    private int width, height;
    private Mat4 view, projection;
    private List <Vertex> transformed = new ArrayList<Vertex>();

    public Renderer(LineRasterizer lineRasterizer, TriangleRasterizer triangleRasterizer,int width, int height) {
        this.lineRasterizer = lineRasterizer;
        this.triangleRasterizer = triangleRasterizer;
        this.width = width;
        this.height = height;
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProjection(Mat4 projection) {
        this.projection = projection;
    }

    private Vec3D transformToScreen(Vec3D point) {
        return point.mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D((width - 1) / 2., (height - 1) / 2., 1));

    }


    public void renderSolid(Solid solid){
        transformed.clear(); // We take every point in our vertex buffer and transform it with first three steps of visualisation pipeline
        for(int i = 0;i < solid.getVertexBuffer().size();i++) {
            Point3D p = solid.getVertexBuffer().get(i).getPoint();
            p = p.mul(solid.getModel()).mul(view).mul(projection);

            double x = p.getX();
            double y = p.getY();
            double z = p.getZ();
            double w = p.getW();
            //Here we are doing dehomogenization and screen transformation.
            if (w != 0 && x > -w && x < w && y > -w && y < w && z > 0 && z < w) {   //TODO: TO JE PRISNE OREZANI, DODELAT
                p = p.mul(1 / p.getW());
            }
            Vec3D a = transformToScreen(new Vec3D(p));
            transformed.add(solid.getVertexBuffer().get(i));
            transformed.get(i).setPoint(a);
        }


        List<Part> partBuffer = solid.getPartBuffer();

        for (Part part : partBuffer) {
            switch(part.getType()){
                case Triangles:
                    int start = part.getStart();
                    for(int i = 0; i < part.getCount();i++){
                        int indexA = start;
                        int indexB = start + 1;
                        int indexC = start + 2;
                        start +=3;

                        //  todo: clipping dole
                        Vertex vertexA = transformed.get(solid.getIndexBuffer().get(indexA) );
                        Vertex vertexB = transformed.get(solid.getIndexBuffer().get(indexB) );
                        Vertex vertexC = transformed.get(solid.getIndexBuffer().get(indexC) );

                        triangleRasterizer.rasterize(vertexA, vertexB, vertexC);
                    }
                    break;
                case Lines:
                    break;
                default:
                    break;
            }

        }

    }

    private void ClipTriangle(Vertex a, Vertex b, Vertex c){
        //TODO: seradit dle Z
        double zMin = 0;
        //TODO:PREDELAT RAZENI
        if(a.getPoint().getZ() < b.getPoint().getZ()){
            Point3D tmp = new Point3D(a.getPoint().getX(), a.getPoint().getY(), a.getPoint().getZ());
            a.setPoint(b.getPoint());
            b.setPoint(tmp);
        }

        if(a.getPoint().getZ() < c.getPoint().getZ()){
            Point3D tmp = new Point3D(a.getPoint().getX(), a.getPoint().getY(), a.getPoint().getZ());
            a.setPoint(c.getPoint());
            c.setPoint(tmp);
        }

        if(b.getPoint().getZ() < c.getPoint().getZ()){
            Point3D tmp = new Point3D(b.getPoint().getX(), b.getPoint().getY(), b.getPoint().getZ());
            b.setPoint(c.getPoint());
            c.setPoint(tmp);
        }

        if(a.getPoint().getZ() < zMin){
            return;
        }
        if(b.getPoint().getZ() < zMin){
            //interpoalci spocitat novy trojuhelnik
            return;
        }
        if(c.getPoint().getZ() < zMin){
            //pruser, dva trojuhelniky
            return;
        }

        //Nic z predchoziho neplati, rasterizujeme puvodni trojuhelnik
    }
}
