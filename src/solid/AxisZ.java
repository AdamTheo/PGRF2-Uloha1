package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;
import transforms.Point3D;

public class AxisZ extends Solid {
    public AxisZ() {
        super();
        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0), new Col(0x0000ff)));
        vertexBuffer.add(new Vertex(new Point3D(0, 0, 1), new Col(0x0000ff)));

        indexBuffer.add(0);
        indexBuffer.add(1);

        partBuffer.add(new Part(0, 1, TopologyType.Lines));
    }
}
