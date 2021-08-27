package BusinessLayer.Enemy;

import BusinessLayer.Objects.Position;
import BusinessLayer.Objects.Range;
import BusinessLayer.Player.Player;
import java.util.Random;

public class Monster extends Enemy {

    private int visionRange;

    public int getVisionRange() {
        return visionRange;
    }

    public void setVisionRange(int visionRange) {
        this.visionRange = visionRange;
    }

    public Monster(Position position,Character c ,String name, int healthPool, int healthAmount, int attackPoints, int defencePoints, int experience, int visionRange) {
        super(position, c, name, healthPool, healthAmount, attackPoints, defencePoints, experience);
        this.visionRange = visionRange;
    }

    public Position enemyTurn(Player player) {
        Position newPos = new Position(getPosition().getX(),getPosition().getY());
        if (Range.range(this, player) < getVisionRange()) {
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

        } else {
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

    @Override
    public void processStep() {

    }

    @Override
    public void onDeath() {

    }

    @Override
    public void gameTick() {

    }
    public String describe() {
        //return String.format("Monster name : %s     Monster health : %d      Monster %s : %d      Monster %s : d%       Monster Experience : %d",getName(),getHealthAmount(),"Attack points ",getAttackPoints(),"Defence points ",getDefensePoints(),getExperience());
        return "Monster name: " + getName() + " ---- Monster health: " + getHealthAmount() + " ---- Attack points: " + getAttackPoints() + " ---- Defence points: "  + getDefensePoints() +" ---- Monster Experience: " + getExperience();
    }
}
