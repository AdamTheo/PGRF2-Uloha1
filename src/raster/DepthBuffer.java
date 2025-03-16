package raster;

public class DepthBuffer implements Raster<Double>{
    private final Double[] [] buffer;
    int width;
    int height;

    DepthBuffer(int width, int height) {
        buffer = new Double[width][height];
        this.width = width;
        this.height = height;
        for(int i = 0 ; i < width ; i++){
            for(int j = 0 ; j < height ; j++){
                buffer[i][j] = 1.0;
            }
        }
    }

    @Override
    public void setValue(int x, int y, Double value) {
        buffer[x][y] = value;
    }

    @Override
    public Double getValue(int x, int y) {
        return buffer[x][y];
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void clear() {
        for(int i = 0 ; i < width ; i++){
            for(int j = 0 ; j < height ; j++){
                buffer[i][j] = 1.0;
            }
        }
    }
}
