package BusinessLayer.Enemy;

import java.util.List;
import java.util.Random;

import BusinessLayer.GameTiles.HeroicUnit;
import BusinessLayer.Objects.Position;
import BusinessLayer.Objects.Range;
import BusinessLayer.Player.Player;

public class Boss extends Monster implements HeroicUnit {

	private int abilityFrequency;
	private int combatTicks;

	public Boss(Position position, Character c, String name, int healthPool, int healthAmount, int attackPoints,
			int defencePoints, int experience, int visionRange, int abilityFrequency) {
		super(position, c, name, healthPool, healthAmount, attackPoints, defencePoints, experience, visionRange);

		this.abilityFrequency = abilityFrequency;
		this.combatTicks = 0;
	}

	@Override
	public void castAbility(List<Enemy> enemies, Player player) {
		if (player == null) {
			return;
		}

		double dist = Math.abs(Range.range(this, player));
		if (dist <= getVisionRange()) {
			int playerDefence = player.defenceRoll();

			if (this.attackPoints > playerDefence) {
				int damage = attackPoints - playerDefence;
				player.gotHit(damage);

				player.messageCallback.send(this.name + " cast Special ability and made damage of , "
						+ damage + " to " + player.getName());
				player.messageCallback
						.send(this.getName() + " rolled " + attackPoints + " attack points");
				player.messageCallback
						.send(player.getName() + " rolled " + playerDefence + " defence points");
				player.messageCallback
						.send(this.getName() + " dealt " + damage + " damage to " + player.getName());
			}

			if (player.getHealthAmount() <= 0) {
				player.messageCallback.send(this.name + " killed " + player.getName());
				player.deathCallback.call();
			}
		}

	}

	@Override
	public void gameTick() {

	}

	@Override
	public Position enemyTurn(Player player) {
		Position newPos = new Position(getPosition().getX(), getPosition().getY());
		if (Range.range(this, player) < getVisionRange()) {
			if (this.combatTicks == this.abilityFrequency) {
				this.combatTicks = 0;

				this.castAbility(null, player);
			} else {
				this.combatTicks++;

				int dX = getPosition().getX() - player.getPosition().getX();
				int dY = getPosition().getY() - player.getPosition().getY();
				if (Math.abs(dX) < Math.abs(dY)) {
					if (dY > 0) {
						newPos.moveLeft();
					} else {
						newPos.moveRight();
					}

				} else {
					if (dX > 0) {
						newPos.moveUp();
					} else {
						newPos.moveDown();
					}
				}
			}
		} else {
			this.combatTicks = 0;

			// perform a random movement
			Random random = new Random();
			int rand = random.nextInt(5);
			switch (rand) {
			case 0:
				getPosition();
			case 1:
				newPos.moveLeft();
				break;
			case 2:
				newPos.moveRight();
				break;
			case 3:
				newPos.moveUp();
				break;
			case 4:
				newPos.moveDown();
				break;
			}
		}
		return newPos;
	}

	public String describe() {
		return "Boss name: " + getName() + " ---- Boss health: " + getHealthAmount() + " ---- Attack points: "
				+ getAttackPoints() + " ---- Defence points: " + getDefensePoints() + " ---- Boss Experience: "
				+ getExperience() + " ---- Ability Frequency: " + this.abilityFrequency + " ---- Combat Ticks: "
				+ this.combatTicks;
	}
}
