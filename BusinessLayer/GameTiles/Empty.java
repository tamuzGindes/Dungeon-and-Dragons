package BusinessLayer.GameTiles;

import BusinessLayer.Player.Player;
import BusinessLayer.Enemy.Enemy;
import BusinessLayer.Objects.Position;

public class Empty extends Tile {


    public Empty(int x, int y){
        super(x,y,'.');
    }

    public String describe(){
        return "Free, characters can step over";
    }
    @Override
    public void visit (Player player) {
        Position playerPos = player.getPosition();
        player.setPosition(this.getPosition());
        setPosition(playerPos);
    }
    public void visit (Enemy enemy) {
        Position enemyPos = enemy.getPosition();
        enemy.setPosition(this.getPosition());
        setPosition(enemyPos);
    }
    public void accept (Enemy enemy) {
        enemy.visit(this);
    }
    public void accept (Player player) {
        player.visit(this);
    }
}
