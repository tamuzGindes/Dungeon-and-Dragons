package BusinessLayer.GameTiles;
import BusinessLayer.Player.Player;
import BusinessLayer.Enemy.Enemy;
import BusinessLayer.Objects.Position;


public abstract class Tile implements Comparable<Tile> {
    protected char tile;
    protected Position position;

    public Tile(int x, int y, char c) {
        position = new Position(x, y);
        tile = c;
    }

    public Tile(Position pos, char c) {
        position = pos;
        tile = c;
    }

    public Position getPosition() {
        return position;
    }

    public char getTile() {
        return tile;
    }
    public void setTile(char tile) {
        this.tile = tile;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public int compareTo(Tile tile) {
        return getPosition().compareTo(tile.getPosition());
    }

    @Override
    public String toString() {
        return String.valueOf(tile);
    }
    protected void initialize(Position position){
        this.position = position;
    }
    public void visit(Enemy defender) { }
    public void visit(Empty empty) { }
    public void visit(Wall wall) { }
    public void visit(Player defender) { }
    public void accept(Player attacker) { }
    public void accept(Enemy attacker) { }
    public void accept(Wall wall) { }
    public void accept(Empty empty) { }

}
