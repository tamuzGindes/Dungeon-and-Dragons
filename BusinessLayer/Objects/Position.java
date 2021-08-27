package BusinessLayer.Objects;

public class Position implements Comparable<Position> {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveRight() {
        y = y + 1;
    }

    public void moveLeft() {
        y = y - 1;
    }

    public void moveUp() {
        x = x - 1;
    }

    public void moveDown() {
        x = x + 1;
    }

    public boolean Equals(Position pos) {

        return this.x == pos.x & this.y == pos.y;
    }

    public double distance(Position pos) {
        double b = Math.pow(this.y - pos.y, 2);
        double a = Math.pow(this.x - pos.x, 2);
        return Math.sqrt(a + b);
    }

    @Override
    public int compareTo(Position pos) {
        if (this.x > pos.x)
            return 1;
        else if (this.x < pos.x)
            return -1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
