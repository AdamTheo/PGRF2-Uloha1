package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;
import transforms.Point3D;

public class AxisX extends Solid {
    public AxisX() {
        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0), new Col(0xff0000)));
        vertexBuffer.add(new Vertex(new Point3D(1, 0, 0), new Col(0xff0000)));

        indexBuffer.add(0);
        indexBuffer.add(1);

        partBuffer.add(new Part(0,1, TopologyType.Lines));
    }
}