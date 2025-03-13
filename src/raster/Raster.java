package raster;


//DODELAT OSETRENI MIMO RASTR""""
public interface Raster <T> {
    void setValue(int x, int y, T value);
    T getValue(int x, int y);
    int getWidth();
    int getHeight();
    void clear();
}
