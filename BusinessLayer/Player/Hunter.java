package BusinessLayer.Player;

import java.util.ArrayList;
import java.util.List;

import BusinessLayer.Enemy.Enemy;
import BusinessLayer.Objects.Position;
import BusinessLayer.Objects.Range;

public class Hunter extends Player {
	private int range;
	private int arrowsCount;
	private int ticksCount;

	protected static final int ARROWS_MULTIPLIER = 10;
	protected static final int ATTACK_BONUS = 2;
	protected static final int DEFENSE_BONUS = 1;

	public Hunter(Position position, String name, int healthPool, int healthAmount, int attackPoints, int defencePoints,
			int range) {
		super(position, name, healthPool, healthAmount, attackPoints, defencePoints);
		this.range = range;
		this.arrowsCount = ARROWS_MULTIPLIER * getLevel();
		this.ticksCount = 0;
	}

	public int getArrowsCount() {
		return this.arrowsCount;
	}

	@Override
	public void processStep() {

	}

	@Override
	public void gameTick() {
		if (ticksCount == 10) {
			this.arrowsCount += getLevel();
			this.ticksCount = 0;
		} else {
			this.ticksCount++;
		}
	}

	protected void levelUp() {
		super.levelUp();

		this.arrowsCount += ARROWS_MULTIPLIER * getLevel();
		this.attackPoints += ATTACK_BONUS * getLevel();
		this.defensePoints += DEFENSE_BONUS * getLevel();
	}

	@Override
	public void castAbility(List<Enemy> enemies, Player player) {
		if (enemies == null) {
			return;
		}

		List<Enemy> enemiesInRange = new ArrayList<Enemy>();
		for (Enemy enemy : enemies) {
			Double dis = Range.range(this, enemy);

			if (Math.abs(dis) <= this.range) {
				enemiesInRange.add( enemy);
			}
		}

		if (this.arrowsCount == 0) {
			messageCallback.send(this.name + " doesn't have enough arrows to cast special ability");
		} else if (enemiesInRange.size() == 0) {
			messageCallback.send(this.name + " doesn't have any enemies in range for special ability");
		} else {
			this.arrowsCount--;

			// find closest enemy
			Enemy closestEnemy = enemiesInRange.get(0);
			double minDist = Math.abs(Range.range(this, closestEnemy));
			for (Enemy enemy : enemiesInRange) {
				double dist = Math.abs(Range.range(this, enemy));

				if (dist < minDist) {
					closestEnemy = enemy;
					minDist = dist;
				}
			}

			int enemyDefence = closestEnemy.defenceRoll();
			if (this.attackPoints > enemyDefence) {
				int damage = attackPoints - enemyDefence;
				closestEnemy.gotHit(damage);
				messageCallback.send(this.name + " cast Special ability -Shoot and made damage of , " + damage + " to "
						+ closestEnemy.getName());
				messageCallback.send(this.getName() + " rolled " + attackPoints + " attack points");
				messageCallback.send(closestEnemy.getName() + " rolled " + enemyDefence + " defence points");
				messageCallback.send(this.getName() + " dealt " + damage + " damage to " + closestEnemy.getName());
			}

			if (closestEnemy.getHealthAmount() <= 0) {
				addExperience(closestEnemy.getExperience());
				messageCallback.send(this.name + " killed " + closestEnemy.getName() + " and earned for it "
						+ closestEnemy.getExperience() + " experience points ");
				closestEnemy.deathCallback.call();
			}
		}
	}

	@Override
	public String describe() {
		return "Player Name: " + getName() + " ---- Player type: Hunter ---- Player health: " + getHealthAmount()
				+ " ---- Arrows Count: " + getArrowsCount() + " ---- Player Level: " + getLevel()
				+ " ---- Player Experience: " + getExperience();
	}
}
