package solid;

import model.TopologyType;
import model.Part;
import model.Vertex;
import transforms.Col;

public class Arrow extends Solid {

    public Arrow() {
        super();

        vertexBuffer.add(new Vertex(50,150,0.5,new Col(0xff0000)));
        vertexBuffer.add(new Vertex(200,150,0.5,new Col(0xff0000)));
        vertexBuffer.add(new Vertex(200,100,0.5,new Col(0xff0000)));
        vertexBuffer.add(new Vertex(200,200,0.5,new Col(0xff0000)));
        vertexBuffer.add(new Vertex(250,150,0.5,new Col(0xff0000)));

        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(4);




        partBuffer.add(new Part(0,1,TopologyType.Lines));
        partBuffer.add(new Part(2,1,TopologyType.Triangles));

    }
}
