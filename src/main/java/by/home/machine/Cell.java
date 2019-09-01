package by.home.machine;

public final class Cell implements CellInterface {
    private final long x;

    private final long y;


    public Cell(long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public long getX() {
        return x;
    }

    @Override
    public long getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x &&
                y == cell.y;
    }
}
