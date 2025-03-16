package solid;

import model.Vertex;
import model.Part;
import transforms.Mat4;
import transforms.Mat4Identity;

import java.util.ArrayList;
import java.util.List;


public abstract class Solid {
    protected List <Integer> indexBuffer = new ArrayList<Integer>();
    protected List<Vertex> vertexBuffer = new ArrayList<Vertex>();
    protected List <Part> partBuffer = new ArrayList<Part>();
    protected Mat4 model = new Mat4Identity();


    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }
    public List<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }
    public List<Part> getPartBuffer() {
        return partBuffer;
    }
    public Mat4 getModel() {
        return model;
    }
    public void setModel(Mat4 model) {
        this.model = model;
    }
}
