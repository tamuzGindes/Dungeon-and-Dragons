package BusinessLayer.Enemy;

import BusinessLayer.Objects.Position;
import BusinessLayer.Objects.Range;
import BusinessLayer.Player.Player;

public class Trap extends Enemy {
    private int visibilityTime;
    private int invisibilityTime;
    private int ticksCount;
    private boolean visible;

    public Trap(Position position,Character c, String name, int healthPool, int healthAmount, int attackPoints, int defencePoints, int experience, int visibilityTime, int invisibilityTime) {
        super(position, c, name, healthPool, healthAmount, attackPoints, defencePoints, experience);
        this.visibilityTime = visibilityTime;
        this.invisibilityTime = invisibilityTime;
        visible = true;
    }

    public int getInvisibilityTime() {
        return invisibilityTime;
    }

    public int getVisibilityTime() {
        return visibilityTime;
    }

    public void setInvisibilityTime(int invisibilityTime) {
        this.invisibilityTime = invisibilityTime;
    }

    public void setVisibilityTime(int visibilityTime) {
        this.visibilityTime = visibilityTime;
    }

    @Override
    public void processStep() {

    }

    @Override
    public void onDeath() {

    }

    public Position enemyTurn(Player player) {
    	visible = ticksCount < visibilityTime;
        if (ticksCount == (visibilityTime + invisibilityTime)) {
            ticksCount = 0;
        } else {
            ticksCount++;
        }
        if (Range.range(this, player) < 2) {
            return player.getPosition();
        }
        return new Position(-1,-1);
    }



    @Override
    public String toString() {
        if (visible)
            return String.valueOf(tile);
        else
            return "";
    }

    @Override
    public void gameTick() {

    }
    public String describe() {

       // return String.format("Trap name : %s      Trap health : %d      Trap %s : %d      Trap %s : d%     Trap Experience : %d",getName(),getHealthAmount(),"Attack points ",getAttackPoints(),"Defence points ",getDefensePoints(),getExperience());
        return "Trap name: " + getName() + " ---- Trap health: " + getHealthAmount() + " ---- Attack points: " + getAttackPoints() + " ---- Defence points: "  + getDefensePoints() +" ---- Trap Experience: " + getExperience();
    }

}
