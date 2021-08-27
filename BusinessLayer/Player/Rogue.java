package BusinessLayer.Player;

import java.util.List;

import BusinessLayer.Enemy.Enemy;
import BusinessLayer.Objects.Position;
import BusinessLayer.Objects.Range;

public class Rogue extends Player {
    private int cost;
    private int currentEnergy;
    protected static final int MAX_ENERGY= 100;
    protected static final int ATTACK_BONUS = 3;
    
    public Rogue(Position position,String name, int healthPool, int healthAmount, int attackPoints, int defencePoints, int cost) {
        super(position,name, healthPool, healthAmount, attackPoints, defencePoints);
        this.cost = cost;
        this.currentEnergy = MAX_ENERGY;
    }
    public int getCost() {
        return cost;
    }
    public int getCurrentEnergy() {
        return currentEnergy;
    }

    @Override
    public void processStep() {

    }

    @Override
    public void gameTick() {
        currentEnergy = Math.min(getCurrentEnergy()+10,MAX_ENERGY);
    }

    @Override
    public void castAbility(List<Enemy> enemies, Player player) {
        if(getCurrentEnergy() - getCost() >= 0) {
            currentEnergy = getCurrentEnergy() - getCost();
            
            if (enemies == null){
            	return;
            }
            
            for (Enemy enemy: enemies) {
                if(Range.range(this, enemy) < 2){
                    int defence = enemy.defenceRoll();
                    if(attackPoints > defence) {
                        int hitPower = attackPoints - defence;
                        enemy.gotHit(hitPower);
                        messageCallback.send(this.name + " cast Special ability- Fan of Knives and made damage of , " + hitPower + " to " + enemy.getName());
                        messageCallback.send(this.getName() + " rolled " + hitPower + " attack points");
                        messageCallback.send(enemy.getName() + " rolled " + defence + " defence points");
                        messageCallback.send(this.getName() + " dealt " + (hitPower - defence) + " damage to " + enemy.getName());
                    }
                    if(enemy.getHealthAmount() <= 0 ){
                        messageCallback.send(this.name+" killed "+ enemy.getName() + "and earned "+enemy.getExperience() + " for it");
                        addExperience(enemy.getExperience());
                        enemy.deathCallback.call();
                        break;
                    }
                }

            }
        }
        else{
            messageCallback.send(this.name+" doesn't have enough resources to cast special ability");
        }

    }
    protected void levelUp() {
        super.levelUp();
        currentEnergy = MAX_ENERGY;
        attackPoints = getAttackPoints() + (ATTACK_BONUS*getLevel());
    }
    @Override
    public String describe() {

        //return String.format("Playe name : %s    Player type : Rouge   Player health : %d   Player %s : %d    Player %s : d%    Player Level : %d     Player Experience : %d",getName(),getHealthAmount(),"current Energy ",currentEnergy,"cost",getCost(),getLevel(),getExperience());
        return "Player Name: " + getName() + " ---- Player type: Rouge ---- Player health: " + getHealthAmount() + " ---- Current Energy: " + currentEnergy + " ---- Cost:" + getCost() + " ---- Player Level: " + getLevel() + " ---- Player Experience: " + getExperience();
    }
}
