package renderer;

import model.Vertex;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerWithZ;
import rasterize.TriangleRasterizer;
import model.Part;
import solid.Solid;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;
import utils.Lerp;

import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private TriangleRasterizer triangleRasterizer;
    private LineRasterizerWithZ lineRasterizer;
    private int width, height;
    private Mat4 view, projection;
    private List <Vertex> transformed = new ArrayList<Vertex>();
    private Lerp lerp;

    public Renderer(LineRasterizerWithZ lineRasterizer, TriangleRasterizer triangleRasterizer, int width, int height) {
        this.lineRasterizer = lineRasterizer;
        this.triangleRasterizer = triangleRasterizer;
        this.width = width;
        this.height = height;
        lerp = new Lerp();
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
    private void drawTriangle(Vertex a, Vertex b, Vertex c) {
        System.out.println("b");
        Vertex A = new Vertex(a);
        Vertex B = new Vertex(b);
        Vertex C = new Vertex(c);
        //First point
        Point3D p = A.getPoint();
        if(p.getW() != 0){
            p = p.mul(1/p.getW());
        }
        Vec3D e = transformToScreen(new Vec3D(p));
        A.setPoint(new Point3D(e));
        //Second pooint
         p = B.getPoint();
        if(p.getW() != 0){
            p = p.mul(1/p.getW());
        }
         e = transformToScreen(new Vec3D(p));
        B.setPoint(new Point3D(e));
        //Trird point
        p = C.getPoint();
        if(p.getW() != 0){
            p = p.mul(1/p.getW());
        }

        e = transformToScreen(new Vec3D(p));
        System.out.println(e.getX());
        System.out.println(e.getY());
        System.out.println(e.getZ());
        C.setPoint(new Point3D(e));
        triangleRasterizer.rasterize(A,B,C);

    }

    public void drawLine(Vertex a, Vertex b){
        Vertex A = new Vertex(a);
        Vertex B = new Vertex(b);

        Point3D p = A.getPoint();
        if(p.getW() != 0){
            p = p.mul(1/p.getW());
        }
        Vec3D e = transformToScreen(new Vec3D(p));
        A.setPoint(new Point3D(e));

        p = B.getPoint();
        if(p.getW() != 0){
            p = p.mul(1/p.getW());
        }
        e = transformToScreen(new Vec3D(p));
        B.setPoint(new Point3D(e));
        lineRasterizer.rasterize(A,B);

    }


    public void renderSolid(Solid solid){
        transformed.clear(); // We take every point in our vertex buffer and transform it with first three steps of visualisation pipeline
        for(int i = 0;i < solid.getVertexBuffer().size();i++) {
            Point3D p = new Point3D(solid.getVertexBuffer().get(i).getPoint());
            p = p.mul(solid.getModel()).mul(view).mul(projection);

            double x = p.getX();
            double y = p.getY();
            double z = p.getZ();
            double w = p.getW();
            //Here we are doing dehomogenization and screen transformation.
            /*if (w != 0 ) {
                p = p.mul(1 / p.getW());
            }*/
            //Vec3D a = transformToScreen(new Vec3D(p));
            transformed.add(new Vertex(solid.getVertexBuffer().get(i)));
            //transformed.get(i).setPoint(a);
            transformed.get(i).setPoint(p);

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

                        Vertex vertexA = transformed.get(solid.getIndexBuffer().get(indexA) );
                        Vertex vertexB = transformed.get(solid.getIndexBuffer().get(indexB) );
                        Vertex vertexC = transformed.get(solid.getIndexBuffer().get(indexC) );

                        //triangleRasterizer.rasterize(vertexA, vertexB, vertexC);
                        ClipTriangle(vertexA, vertexB, vertexC);
                    }
                    break;
                case Lines:
                    start = part.getStart();
                    for(int i = 0; i < part.getCount();i++) {
                        int indexA = start;
                        int indexB = start + 1;
                        start +=2;

                        Vertex vertexA = transformed.get(solid.getIndexBuffer().get(indexA) );
                        Vertex vertexB = transformed.get(solid.getIndexBuffer().get(indexB) );
                        ClipLine(vertexA, vertexB);

                    }

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

        Vertex A = new Vertex(a);
        Vertex B = new Vertex(b);
        Vertex C = new Vertex(c);

        System.out.println("a");
        if(A.getPoint().getZ() < B.getPoint().getZ()) {
            Vertex tmp = new Vertex(A);
            A = new Vertex(B);
            B = new Vertex(tmp);
        }

        if(A.getPoint().getZ() < C.getPoint().getZ()) {
            Vertex tmp = new Vertex(A);
            A = new Vertex(C);
            C = new Vertex(tmp);
        }
        if(B.getPoint().getZ() < C.getPoint().getZ()) {
            Vertex tmp = new Vertex(B);
            B = new Vertex(C);
            C = new Vertex(tmp);
        }

        double zA = A.getPoint().getZ();
        double zB = B.getPoint().getZ();
        double zC = C.getPoint().getZ();


       /* if(zA > 1 || zB > 1 || zC > 1) { //IT is behind camera, do nothing.
            return;
        }*/

        if(A.getPoint().getZ() < zMin){
            // DO nothing, its outside of sight
            return;
        }
        if(B.getPoint().getZ() < zMin){
            //Two vertexes are out of sight
            double t1 = (0-zA)/(zB-zA); //That zero is not necessary cause its fcking zero, but its for me as litlle check
            Vertex b1 = (Vertex)lerp.lerp(A,B,t1);
            double t2 = (0-zA)/(zC-zA);
            Vertex b2 = (Vertex)lerp.lerp(A,C,t2);
            //triangleRasterizer.rasterize(A,b1,b2);
            drawTriangle(A,b1,b2);
            return;
        }
        if(C.getPoint().getZ() < zMin){
            //Only one vertex is out of sight
            double t1 = (0-zB)/(zC-zB);
            Vertex b1 = ((Vertex)lerp.lerp(B,C,t1));

            double t2 = (double)(0 - zA)/(zC-zA);
            Vertex b2 = ((Vertex)lerp.lerp(A,C,t2));
            //triangleRasterizer.rasterize(A,B,b1);
            drawTriangle(A,B,b1);
            //triangleRasterizer.rasterize(A,b1,b2);
            drawTriangle(A,b1,b2);

            return;
        }

        //triangleRasterizer.rasterize(A,B,C);
        drawTriangle(A,B,C);
    }
    public void ClipLine(Vertex a, Vertex b){
        Vertex A = new Vertex(a);
        Vertex B = new Vertex(b);
        double zMin = 0;
        //Same like with triangle, A has highest Z
        if(A.getPoint().getZ() < B.getPoint().getZ()) {
            Vertex tmp = new Vertex(A);
            A = new Vertex(B);
            B = new Vertex(tmp);
        }
        double zA = A.getPoint().getZ();
        double zB = B.getPoint().getZ();

        if(A.getPoint().getZ() < zMin){ // Case 1, both are lower then 0
            return;

        }
        if(B.getPoint().getZ() < zMin){ // Case 2, only second is outside
            double t1 = (0-zA)/(zB-zA); //That zero is not necessary cause its fcking zero, but its for me as litlle check
            Vertex b1 = (Vertex)lerp.lerp(A,B,t1);
            drawLine(A,b1);
            return;
        }
        //Case 3, both are inside, everything is alright
        drawLine(A,B);

    }
}
