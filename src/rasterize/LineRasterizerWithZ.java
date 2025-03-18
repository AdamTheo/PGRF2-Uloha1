package rasterize;

import model.Vertex;
import raster.Raster;
import raster.ZBuffer;
import shader.Shader;
import shader.ShaderInterpolated;
import utils.Lerp;

public class LineRasterizerWithZ {
    private ZBuffer buffer;
    private Lerp lerp;
    int maxWidth;
    int maxHeight;
    Shader shader;

    public LineRasterizerWithZ(ZBuffer buffer) {
        this.buffer = buffer;
        lerp = new Lerp();
        maxWidth = buffer.getWidth() - 1; //This is needed to decrease by one to due to nature of algorithm and my implementation
        maxHeight = buffer.getHeight() - 1;
        shader = new ShaderInterpolated();
    }

    public void rasterize(Vertex a1, Vertex b1) {
        Vertex a = new Vertex(a1);
        Vertex b = new Vertex(b1);

        //Using trivial algorith with lerp interpolation, just checking if line is not vertical or if we should iterate by x or y, otherwise its just interpolation.

        if (a.getPoint().getX() == b.getPoint().getX()) { // Using trivial algorithm, this is needed otherwise in K calculation dividing by zero could happen
            if (b.getPoint().getY() < a.getPoint().getY()) {
                Vertex tmp = a;
                a = new Vertex(b);
                b = new Vertex(tmp);
            }
            int y1 = (int) Math.round(a.getPoint().getY());
            int y2 = (int) Math.round(b.getPoint().getY());
            for (int y = Math.max(y1, 1); y <= Math.min(y2, maxHeight); y++) {
                double t = ((double) y - y1) / ((double) y2 - y1);
                Vertex v = (Vertex) lerp.lerp(a, b, t);
                if (v.getPoint().getX() <= maxWidth && v.getPoint().getX() > 0) { //Check for X coordinate in raster
                    buffer.setPixelWithZTest((int) Math.round(v.getPoint().getX()), (int) Math.round(v.getPoint().getY()), v.getPoint().getZ(), shader.getColor(v));
                }
            }
        } else {

            // if line IS NOT vertical
            double k = (b.getPoint().getY() - a.getPoint().getY()) / (b.getPoint().getX() - a.getPoint().getX());
            if (k > -1 && k < 1) {
                if (a.getPoint().getX() > b.getPoint().getX()) {
                    Vertex tmp = new Vertex(a);
                    a = new Vertex(b);
                    b = new Vertex(tmp);
                }
                int x1 = (int) Math.round(a.getPoint().getX());
                int x2 = (int) Math.round(b.getPoint().getX());

                for (int x = Math.max(1, x1); x <= Math.min(maxWidth, x2); x++) {
                    double t = ((double) x - x1) / ((double) x2 - x1);
                    Vertex v = (Vertex) lerp.lerp(a, b, t);
                    if (v.getPoint().getY() <= maxHeight && v.getPoint().getY() > 0) {  //Check for Y coordinate in raster
                        buffer.setPixelWithZTest((int) Math.round(v.getPoint().getX()), (int) Math.round(v.getPoint().getY()), v.getPoint().getZ(), shader.getColor(v));
                    }
                }
            } else {
                // Using trivial algorithm
                if (b.getPoint().getY() < a.getPoint().getY()) {
                    Vertex tmp = a;
                    a = new Vertex(b);
                    b = new Vertex(tmp);
                }
                int y1 = (int) Math.round(a.getPoint().getY());
                int y2 = (int) Math.round(b.getPoint().getY());
                for (int y = Math.max(y1, 1); y <= Math.min(y2, maxHeight); y++) {
                    double t = ((double) y - y1) / ((double) y2 - y1);
                    Vertex v = (Vertex) lerp.lerp(a, b, t);
                    if (v.getPoint().getX() <= maxWidth && v.getPoint().getX() > 0) { //Check for X coordinate in raster
                        buffer.setPixelWithZTest((int) Math.round(v.getPoint().getX()), (int) Math.round(v.getPoint().getY()), v.getPoint().getZ(), shader.getColor(v));
                    }
                }


            }
        }
    }


}



