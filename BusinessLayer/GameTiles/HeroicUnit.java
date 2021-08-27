package BusinessLayer.GameTiles;

import java.util.List;

import BusinessLayer.Enemy.Enemy;
import BusinessLayer.Player.Player;

public interface HeroicUnit {
	
	public void castAbility(List<Enemy> enemies, Player player);
}
