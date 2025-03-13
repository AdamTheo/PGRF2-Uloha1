package rasterize;

import model.Line;
import model.Vertex;
import raster.ZBuffer;
import transforms.Col;
import utils.Lerp;

public class TriangleRasterizer {
    LineRasterizer lineRasterizer;
    ZBuffer buffer;
    Lerp lerp;

    public TriangleRasterizer(LineRasterizer lineRasterizer, ZBuffer buffer) {
        this.lineRasterizer = lineRasterizer;
        this.buffer = buffer;
        this.lerp = new Lerp();
    }
    public void rasterize(Vertex a, Vertex b, Vertex c) {
        //(int)a.getPoint().getX(),(int)a.getPoint().getY()
        //lineRasterizer.drawLine(new Line((int)a.getPoint().getX(),(int)a.getPoint().getY(),(int)b.getPoint().getX(),(int)b.getPoint().getY()));
        //lineRasterizer.drawLine(new Line((int)b.getPoint().getX(),(int)b.getPoint().getY(),(int)c.getPoint().getX(),(int)c.getPoint().getY()));
        //lineRasterizer.drawLine(new Line((int)a.getPoint().getX(),(int)a.getPoint().getY(),(int)c.getPoint().getX(),(int)c.getPoint().getY()));


        Vertex A = new Vertex(a);
        Vertex B = new Vertex(b);
        Vertex C = new Vertex(c);


        //TODO: A MUSI BYT NEJMENSI, PAK B, PAK C


        if(A.getPoint().getY() > B.getPoint().getY()) {
            Vertex tmp = new Vertex(A);
            A = new Vertex(B);
            B = new Vertex(tmp);
        }

        if(A.getPoint().getY() > C.getPoint().getY()) {
            Vertex tmp = new Vertex(A);
            A = new Vertex(C);
            C = new Vertex(tmp);
        }
        if(B.getPoint().getY() > C.getPoint().getY()) {
            Vertex tmp = new Vertex(B);
            B = new Vertex(C);
            C = new Vertex(tmp);
        }

        int yA =(int)Math.round(A.getPoint().getY());
        int yB =(int)Math.round(B.getPoint().getY());
        int yC =(int)Math.round(C.getPoint().getY());

        System.out.println(yA + " " + yB + " " + yC);

        for(int y = yA; y < yB; y++) {
            double tAB = ((double)y-yA)/((double)yB-yA);
            //int x1= (int)Math.round((1-tAB)*xA + tAB*xB);
            Vertex a1 = ((Vertex)lerp.lerp(A,B,tAB));
            double tAC = (double)(y - yA)/((double)yC-yA);
            //int x2= (int)Math.round((1-tAC)*xA + tAC*xC);
            Vertex a2 = ((Vertex)lerp.lerp(A,C,tAC));

           /*if(x2 < x1){
               int tmp = x2;
               x2 = x1;
               x1 = tmp;
           }*/
           if(a1.getPoint().getX() > a2.getPoint().getX()) {
               Vertex tmp = new Vertex(a1);
               a1 = new Vertex(a2);
               a2 = new Vertex(tmp);
           }

           int x1 = (int) Math.round(a1.getPoint().getX());
           int x2 = (int) Math.round(a2.getPoint().getX());

            for(int x = x1; x < x2; x++) {
                //TODO: OREZANI HLIDAT OKNO OBRAZOVKY
                double z1 = a1.getPoint().getZ();
                double z2 = a2.getPoint().getZ();

                double tz = (double)(y-a1.getPoint().getY())/((double)a2.getPoint().getY() - a1.getPoint().getY());
                Vertex z3 = ((Vertex)lerp.lerp(a1,a2,tz));
                double z = z3.getPoint().getZ();
                System.out.println(z);



                buffer.setPixelWithZTest(x,y,0.1,new Col(0xffff00));
            }
        }

        // DRUHA CAST

        for(int y = yB; y < yC; y++) {
            double tBC = ((double)y-yB)/((double)yC-yB);
            //int x1 =(int)Math.round((1-tBC)*xB + tBC*xC);
            Vertex b1 = ((Vertex)lerp.lerp(B,C,tBC));

            double tAC = (double)(y - yA)/((double)yC-yA);
            Vertex b2 = ((Vertex)lerp.lerp(A,C,tAC));

            /*if(x2 < x1){
                int tmp = x2;
                x2 = x1;
                x1 = tmp;
            }*/
            if(b1.getPoint().getX() > b2.getPoint().getX()) {
                Vertex tmp = new Vertex(b1);
                b1 = new Vertex(b2);
                b2 = new Vertex(tmp);
            }
            int x1 = (int) Math.round(b1.getPoint().getX());
            int x2 = (int) Math.round(b2.getPoint().getX());

            for(int x = x1; x < x2; x++) {
                //TODO: OREZANI HLIDAT OKNO OBRAZOVKY
                buffer.setPixelWithZTest(x,y,0.1,new Col(0xffff00));
            }

        }

    }
}
