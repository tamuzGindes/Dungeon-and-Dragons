package VisitorPattern;

import BusinessLayer.Enemy.Enemy;
import BusinessLayer.GameTiles.Empty;
import BusinessLayer.GameTiles.Wall;
import BusinessLayer.Player.Player;

public interface Visitor {
    public void visit(Enemy defender);
    public void visit(Empty defender);
    public void visit(Wall defender);
    public void visit(Player defender);
}
