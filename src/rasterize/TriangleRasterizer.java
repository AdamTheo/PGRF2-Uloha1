package rasterize;

import model.Line;
import model.Vertex;
import raster.ZBuffer;
import transforms.Col;
import utils.Lerp;
import shader.Shader;
import shader.ShaderInterpolated;

public class TriangleRasterizer {
    LineRasterizer lineRasterizer;
    ZBuffer buffer;
    Lerp lerp;
    int maxWidth;
    int maxHeight;
    Shader shader;

    public TriangleRasterizer(LineRasterizer lineRasterizer, ZBuffer buffer) {
        this.lineRasterizer = lineRasterizer;
        this.buffer = buffer;
        this.lerp = new Lerp();
        maxWidth = buffer.getWidth();
        maxHeight = buffer.getHeight();
        shader = new ShaderInterpolated(); //Basic shader that we are going to use
    }

    public void rasterize(Vertex a, Vertex b, Vertex c) {

        Vertex A = new Vertex(a);
        Vertex B = new Vertex(b);
        Vertex C = new Vertex(c);
        //Sort vertexes by Y
        if (A.getPoint().getY() > B.getPoint().getY()) {
            Vertex tmp = new Vertex(A);
            A = new Vertex(B);
            B = new Vertex(tmp);
        }

        if (A.getPoint().getY() > C.getPoint().getY()) {
            Vertex tmp = new Vertex(A);
            A = new Vertex(C);
            C = new Vertex(tmp);
        }
        if (B.getPoint().getY() > C.getPoint().getY()) {
            Vertex tmp = new Vertex(B);
            B = new Vertex(C);
            C = new Vertex(tmp);
        }

        int yA = (int) Math.round(A.getPoint().getY());
        int yB = (int) Math.round(B.getPoint().getY());
        int yC = (int) Math.round(C.getPoint().getY());
        // Interpolation of x coordinates
        for (int y = Math.max(yA, 1); y < Math.min(yB, maxHeight); y++) {
            double tAB = ((double) y - yA) / ((double) yB - yA);
            Vertex a1 = ((Vertex) lerp.lerp(A, B, tAB));
            double tAC = (double) (y - yA) / ((double) yC - yA);
            Vertex a2 = ((Vertex) lerp.lerp(A, C, tAC));

            if (a1.getPoint().getX() > a2.getPoint().getX()) {
                Vertex tmp = new Vertex(a1);
                a1 = new Vertex(a2);
                a2 = new Vertex(tmp);
            }

            int x1 = (int) Math.round(a1.getPoint().getX());
            int x2 = (int) Math.round(a2.getPoint().getX());
            //Interpolation of vertex on each x between x1 and x2
            for (int x = Math.max(x1, 1); x < Math.min(x2, maxWidth); x++) {
                double tz = (double) (x - x1) / (double) (x2 - x1);
                Vertex z3 = ((Vertex) lerp.lerp(a1, a2, tz));
                buffer.setPixelWithZTest(x, y, z3.getPoint().getZ(), shader.getColor(z3));
            }
        }

        // DRUHA CAST

        // Interpolation of x coordinates
        for (int y = Math.max(yB, 1); y < Math.min(yC, maxHeight); y++) {
            double tBC = ((double) y - yB) / ((double) yC - yB);
            Vertex b1 = ((Vertex) lerp.lerp(B, C, tBC));

            double tAC = (double) (y - yA) / ((double) yC - yA);
            Vertex b2 = ((Vertex) lerp.lerp(A, C, tAC));

            if (b1.getPoint().getX() > b2.getPoint().getX()) {
                Vertex tmp = new Vertex(b1);
                b1 = new Vertex(b2);
                b2 = new Vertex(tmp);
            }
            int x1 = (int) Math.round(b1.getPoint().getX());
            int x2 = (int) Math.round(b2.getPoint().getX());
            //Interpolation of vertex on each x between x1 and x2
            for (int x = Math.max(x1, 1); x < Math.min(x2, maxWidth); x++) {
                double tz = (double) (x - x1) / (double) (x2 - x1);
                Vertex z3 = ((Vertex) lerp.lerp(b1, b2, tz));
                buffer.setPixelWithZTest(x, y, z3.getPoint().getZ(), shader.getColor(z3));
            }
        }
    }
}
