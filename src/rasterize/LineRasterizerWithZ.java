package rasterize;

import model.Vertex;
import raster.Raster;
import raster.ZBuffer;

public class LineRasterizerWithZ {
    private ZBuffer buffer;

    public LineRasterizerWithZ(ZBuffer buffer) {
        this.buffer = buffer;
    }

    public void rasterize(Vertex a1, Vertex b1) {
            Vertex a = new Vertex(a1);
            Vertex b = new Vertex(b1);




/*
            if (x1 == x2) { // Using trivial algorithm
                if (y2 < y1) {
                    int tmp = y2;
                    y2 = y1;
                    y1 = tmp;
                }
                for (int i = y1; i <= y2; i++) {

                }
            } else {

                //If x1 != x2

                float k = (y2 - y1) / (float) (x2 - x1);
                float q = y1 - k * x1;
                if (k > -1 && k < 1) {




                    for (int x = x1; x <= x2; x++) {


                        float y = k * x + q;
                        raster.setRGB(x, Math.round(y), color);
                    }
                } else {
                    for (int y = y1; y <= y2; y++) {


                        float x = ((float) y - q) / k;
                        raster.setRGB(Math.round(x), y, color);
                    }
                }
            }

 */
        }


    }


