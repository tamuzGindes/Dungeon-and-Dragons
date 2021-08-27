package BusinessLayer.Player;

import BusinessLayer.Enemy.Enemy;
import BusinessLayer.Objects.Position;
import Presentation.Printer;
import BusinessLayer.Objects.Range;

import java.util.ArrayList;
import java.util.List;

public class Warrior extends Player {
	protected int abilityCooldown;
	protected int remainingCooldown;

	protected static final int ATTACK_BONUS = 2;
	protected static final int DEFENSE_BONUS = 1;
	protected static final int HEALTH_BONUS = 5;
	protected static final int ABILITY_HEALTH_BONUS = 10;

	public Warrior(Position pos, String name, int healthAmount, int healthPool, int attackPoints, int defencePoints,
			int abilityCooldown) {
		super(pos, name, healthAmount, healthPool, attackPoints, defencePoints);
		this.abilityCooldown = abilityCooldown;
		remainingCooldown = 0;
	}

	protected void levelUp() {
		super.levelUp();
		remainingCooldown = 0;
		setHealthPool(getHealthPool() + (HEALTH_BONUS * getLevel()));
		setHealthAmount(getHealthPool());
		setAttackPoints(getAttackPoints() + (ATTACK_BONUS * getLevel()));
		setDefensePoints(getDefensePoints() + DEFENSE_BONUS * getLevel());
	}

	@Override
	public void processStep() {

	}

	public void gameTick() {
		if (remainingCooldown > 0)
			remainingCooldown--;
	}

	@Override
	public void castAbility(List<Enemy> enemies, Player player) {
		if (remainingCooldown > 0) {
			Printer.printMessage("Ability is still on cooldown");
		} else {
			remainingCooldown = abilityCooldown;
			setHealthAmount(Math.min(healthAmount + (ABILITY_HEALTH_BONUS * defensePoints), healthPool));

			if (enemies == null) {
				return;
			}

			List<Enemy> enemiesInRange = new ArrayList<Enemy>();
			for (Enemy enemy : enemies) {
				Double dis = Range.range(this, enemy);

				if (Math.abs(dis) < 3) {
					enemiesInRange.add(enemy);
				}
			}

			if (enemiesInRange.size() > 0) {
				int randomIndex = (int) Math.floor(Math.random() * (enemiesInRange.size()));
				Enemy enemy = enemiesInRange.get(randomIndex);

				int damage = ((Double) (healthPool * 0.1)).intValue();
				messageCallback.send("Special ability -Avenger’s Shield has been cast, " + this.name + " made a damage of  " + damage
						+ " points to " + enemy.getName());
				enemy.gotHit(damage);
				if (enemy.getHealthAmount() <= 0) {
					addExperience(enemy.getExperience());
					messageCallback.send(this.name + " killed " + enemy.getName() + " and earned for it "
							+ enemy.getExperience() + " experience points ");
					enemy.deathCallback.call();
				}

			}
		}
	}

	@Override
	public String describe() {
		return "Player name: " + getName() + " ---- Player type: Warrior ---- Player health: " + getHealthAmount()
				+ " ---- ability Cooldown: " + abilityCooldown + " ---- Remaining cooldown: " + remainingCooldown
				+ " ---- Player Level: " + getLevel() + " ---- Player Experience: " + getExperience();
	}
}
