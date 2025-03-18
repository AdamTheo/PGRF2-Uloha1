package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;
import transforms.Point3D;

public class AxisY extends Solid {
    public AxisY() {
        super();
        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0), new Col(0x000000)));
        vertexBuffer.add(new Vertex(new Point3D(0, 1, 0), new Col(0x00ff00)));

        indexBuffer.add(0);
        indexBuffer.add(1);

        partBuffer.add(new Part(0,1, TopologyType.Lines));
    }
}
