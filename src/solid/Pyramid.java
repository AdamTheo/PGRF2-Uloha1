package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;
import transforms.Point3D;

public class Pyramid extends Solid {
    public Pyramid() {
        super();
        vertexBuffer.add(new Vertex(new Point3D(-0.3, -0.3, -0.3), new Col(0xffff00)));
        vertexBuffer.add(new Vertex(new Point3D(0.3, -0.3, -0.3), new Col(0xff00ff)));
        vertexBuffer.add(new Vertex(new Point3D(0.3, 0.3, -0.3), new Col(0xff000f)));
        vertexBuffer.add(new Vertex(new Point3D(-0.3, 0.3, -0.3), new Col(0x0000ff)));
        vertexBuffer.add(new Vertex(new Point3D(-0, 0, 0.3), new Col(0xffffff)));

        //Sides
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(4);
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(4);
        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(4);
        indexBuffer.add(3);
        indexBuffer.add(0);
        indexBuffer.add(4);

        //bottom
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(3);
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(3);

        partBuffer.add(new Part(0, 6, TopologyType.Triangles));
    }
}
