package raster;
import transforms.Col;

public class ZBuffer {
    private final Raster<Double>depthBuffer;
    private final Raster<Col> imageBuffer;

    public ZBuffer(Raster <Col> imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }
    public void setPixelWithZTest(int x, int y, Double z, Col color) {
        if(z != null && z < depthBuffer.getValue(x,y)){
            depthBuffer.setValue(x,y,z);
            imageBuffer.setValue(x,y,color);

        }
    }
}
