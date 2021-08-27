package BusinessLayer.GameTiles;

import Presentation.MessageCallback;
import BusinessLayer.Objects.Position;

import java.util.Random;

public abstract class Unit extends Tile {
    public String name;
    protected int healthPool;
    protected int healthAmount;
    protected int attackPoints;
    protected int defensePoints;
    public MessageCallback messageCallback;


    public Unit(Position pos, char c, String name, int healthAmount, int healthPool, int attackPoints, int defencePoints){
        super(pos,c);
        this.name = name;
        this.healthAmount = healthAmount;
        this.healthPool = healthPool;
        this.attackPoints = attackPoints;
        this.defensePoints = defencePoints;
    }


    // getters
    public String getName() {
        return name;
    }
    public MessageCallback getMessageCallback() {
        return messageCallback;
    }
    public int getHealthAmount() {
        return healthAmount;
    }

    public int getHealthPool() {
        return healthPool;
    }

    public int getAttackPoints() {
        return attackPoints;
    }

    public int getDefensePoints() {
        return defensePoints;
    }

    // setters
    public void setAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
    }

    public void setDefensePoints(int defensePoints) {
        this.defensePoints = defensePoints;
    }
    public void SetMessageCallBack(MessageCallback messageCallback) {
        this.messageCallback = messageCallback;
    }

    public void setHealthAmount(int healthAmount) {
        if (healthAmount < 0)
            this.healthAmount = 0;
        else if ( healthAmount > healthPool)
            this.healthAmount = healthPool;
        else
            this.healthAmount = healthAmount;
    }

    public void setHealthPool(int healthPool) {
        if (healthPool < 0)
            this.healthPool = 0;
        else
            this.healthPool = healthPool;
    }

    public void gotHit(int hit){
        healthAmount -= hit;
    }
    public int defenceRoll(){
        Random random = new Random();
        int defence = random.nextInt(getDefensePoints()+1);
        return  defence;
    }
    public int attackRoll(){
        Random random = new Random();
        int attack = random.nextInt(getAttackPoints()+1);
        return  attack;
    }
    protected void initialize(Position position, MessageCallback messageCallback){
        super.initialize(position);
        this.messageCallback = messageCallback;
    }
    // Should be automatically called once the unit finishes its turn
    public abstract void processStep();

    // What happens when the unit dies
    public abstract void onDeath();

    public abstract int getExperience();
    public abstract void gameTick();
    public abstract String describe();
    protected void swapPosition(Tile tile){
        Position p = tile.getPosition();
        tile.setPosition(this.getPosition());
        this.setPosition(p);
    }
    public void visit(Empty defender) {
        swapPosition(defender);
    }
}

