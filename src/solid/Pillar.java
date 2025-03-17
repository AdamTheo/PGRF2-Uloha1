package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;
import transforms.Point3D;

public class Pillar extends Solid {

    public Pillar(){
        super();
        vertexBuffer.add(new Vertex(new Point3D(-0.2,-0.2,0.4),new Col(0xf00000)));
        vertexBuffer.add(new Vertex(new Point3D(0.2,-0.2,0.4),new Col(0x0f0000)));
        vertexBuffer.add(new Vertex(new Point3D(0.0,0.2,0.4),new Col(0x00f000)));

        vertexBuffer.add(new Vertex(new Point3D(-0.2,-0.2,-0.4),new Col(0x000f00)));
        vertexBuffer.add(new Vertex(new Point3D(0.2,-0.2,-0.4),new Col(0x0f00f0)));
        vertexBuffer.add(new Vertex(new Point3D(0.0,0.2,-0.4),new Col(0xff000f)));
        //Top
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);
        //Bottom
        indexBuffer.add(3);
        indexBuffer.add(4);
        indexBuffer.add(5);
        //LinesS
        indexBuffer.add(0);
        indexBuffer.add(3);
        indexBuffer.add(1);
        indexBuffer.add(4);
        indexBuffer.add(2);
        indexBuffer.add(5);

        partBuffer.add(new Part(0,2, TopologyType.Triangles));
        partBuffer.add(new Part(6,3, TopologyType.Lines));

    }
}
