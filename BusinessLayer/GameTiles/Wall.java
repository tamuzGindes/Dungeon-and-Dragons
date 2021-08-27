package BusinessLayer.GameTiles;

import BusinessLayer.Player.Player;
import BusinessLayer.Enemy.Enemy;

public class Wall extends Tile {

    public Wall(int x, int y){
        super(x,y,'#');
    }

    public String description(){
        return "Wall, blocked, no characters may step over";
    }

    public void accept(Player attacker) {

        attacker.visit(this);
    }


    public void accept(Enemy attacker) {

        attacker.visit(this);
    }
}
