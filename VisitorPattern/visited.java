package VisitorPattern;

import BusinessLayer.Enemy.Enemy;
import BusinessLayer.GameTiles.Empty;
import BusinessLayer.GameTiles.Wall;
import BusinessLayer.Player.Player;

public interface visited {
    public void accept(Player attacker);
    public void accept(Enemy attacker);
    public void accept(Wall attacker);
    public void accept(Empty attacker);
}
