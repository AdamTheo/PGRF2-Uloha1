package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;
import transforms.Point3D;

public class Cube extends Solid {
    public Cube() {
        super();
        vertexBuffer.add(new Vertex(new Point3D( -0.2,0.2,-0.2),new Col(0xff0000)));
        vertexBuffer.add(new Vertex(new Point3D( 0.2,0.2,-0.2),new Col(0xff0000)));
        vertexBuffer.add(new Vertex(new Point3D( -0.2,0.2,0.2),new Col(0x00ff00)));
        vertexBuffer.add(new Vertex(new Point3D( 0.2,0.2,0.2),new Col(0x00ff00)));

        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(2);
        indexBuffer.add(1);

        partBuffer.add(new Part(0,2, TopologyType.Triangles));
    }
}
