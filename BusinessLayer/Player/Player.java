package BusinessLayer.Player;

import Presentation.DeathCallback;
import Presentation.MessageCallback;
import Presentation.Printer;

import java.util.List;

import BusinessLayer.Enemy.Enemy;
import BusinessLayer.GameTiles.Empty;
import BusinessLayer.GameTiles.HeroicUnit;
import BusinessLayer.GameTiles.Unit;
import BusinessLayer.GameTiles.Wall;
import BusinessLayer.Objects.Position;

public abstract class Player extends Unit implements HeroicUnit {
    protected int experience;
    protected int level;
    public DeathCallback deathCallback;


    protected static final int REQ_EXP = 50;
    protected static final int ATTACK_BONUS = 4;
    protected static final int DEFENSE_BONUS = 1;
    protected static final int HEALTH_BONUS = 10;

    public Player(Position pos, String name, int healthAmount, int healthPool, int attackPoints, int defencePoints){
        super(pos,'@',name,healthAmount,healthPool,attackPoints,defencePoints);
        this.experience = 0;
        this.level = 1;
    }
    public Player initialize(Position position, MessageCallback messageCallback, DeathCallback deathCallback){
        super.initialize(position, messageCallback);
        this.deathCallback = deathCallback;
        return this;
    }
    // When the player dies
    public void onDeath() {
        Printer.printMessage("You lost.");
        // Use deathCallback to alert the level manager
        deathCallback.call();
    }
    protected int gainHealth(){
        return level * HEALTH_BONUS;
    }
    protected int gainAttack(){
        return level * ATTACK_BONUS;
    }
    protected int gainDefense(){
        return level * DEFENSE_BONUS;
    }

    private int levelUpRequirement(){
        return REQ_EXP * level;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public void addExperience(int exp){
        this.experience += exp;
        int nextLevelReq = levelUpRequirement();
        while(experience >= nextLevelReq){
            levelUp();
            nextLevelReq = levelUpRequirement();
        }

    }

    protected void levelUp() {
    	int nextLevelReq = levelUpRequirement();
        level++;
        experience -= nextLevelReq;
        healthPool = healthPool + (HEALTH_BONUS * level);
        healthAmount = healthPool;
        attackPoints = attackPoints + (ATTACK_BONUS * level);
        defensePoints = defensePoints + (DEFENSE_BONUS * level);
        messageCallback.send("congratulation!!!! you leveled up to LEVEL NO. " + getLevel());
    }


    public abstract void gameTick();

    @Override
    public void visit (Enemy defender) {
        messageCallback.send(this.getName() + " engaged in combat with " + defender.getName());
        int attackRoll = attackRoll();
        messageCallback.send(this.getName() + " rolled " + attackRoll + " attack points");
        int defenceRoll = defender.defenceRoll();
        messageCallback.send(defender.getName() + " rolled " + defenceRoll + " defence points");
        if (attackRoll - defenceRoll > 0) {
            defender.gotHit(attackRoll - defenceRoll);
            messageCallback.send(this.getName() + " dealt " + (attackRoll - defenceRoll) + " damage to " + defender.getName());
            if (defender.getHealthAmount() == 0) {
                messageCallback.send(getName() + " killed " + defender.getName() + " earned " + defender.getExperience() + " experience points");
                this.addExperience(defender.getExperience());
                defender.deathCallback.call();
            }
            if(getHealthAmount() <= 0){
                deathCallback.call();
            }
        }
    }
    @Override
    public void visit (Empty empty) {
        Position emptyPos = empty.getPosition();
        empty.setPosition(this.getPosition());
        setPosition(emptyPos);
    }

    @Override
    public void visit (Wall wall) {

    }
    @Override
    public void accept(Empty empty) {
        empty.visit(this);
    }
    @Override
    public void accept(Enemy attacker) {
        attacker.visit(this);
    }

    public abstract String describe();

    public abstract void castAbility(List<Enemy> enemies, Player player);
}
