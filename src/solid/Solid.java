package solid;

import model.Vertex;
import model.Part;

import java.util.ArrayList;
import java.util.List;


public abstract class Solid {
    protected List <Integer> indexBuffer = new ArrayList<Integer>();
    protected List<Vertex> vertexBuffer = new ArrayList<Vertex>();
    protected List <Part> partBuffer = new ArrayList<Part>();


    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }
    public List<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }
    public List<Part> getPartBuffer() {
        return partBuffer;
    }
}
