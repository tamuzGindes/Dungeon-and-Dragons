package BusinessLayer.Enemy;

import Presentation.DeathCallback;
import Presentation.MessageCallback;
import BusinessLayer.GameTiles.Empty;
import BusinessLayer.GameTiles.Unit;
import BusinessLayer.Objects.Position;
import BusinessLayer.Player.Player;

public abstract class Enemy extends Unit {
    protected int experience;
    public DeathCallback deathCallback;

    public Enemy(Position position, char character,String name, int healthPool, int healthAmount, int attackPoints, int defencePoints, int experience) {
        super(position, character,name,healthPool, healthAmount, attackPoints, defencePoints);
        this.experience = experience;
    }
    public Enemy initialize(Position position, MessageCallback messageCallback, DeathCallback deathCallback){
        super.initialize(position, messageCallback);
        this.deathCallback = deathCallback;
        return this;
    }
    @Override
    public int getExperience() {
        return experience;
    }

    @Override
    public abstract void gameTick();

    public void gotHit(int damage){
        if ((getHealthAmount()-damage)< 0)
            setHealthAmount(0);
        else
            setHealthAmount(getHealthAmount()-damage);

    }
    @Override
    public void visit (Empty empty){
        Position positionOfEmpty =  empty.getPosition();
        empty.setPosition(getPosition());
        setPosition(positionOfEmpty);
    }
    @Override
    public void visit (Player defender) {
        messageCallback.send(this.getName() + " engaged in combat with " + defender.getName());
        int attackRoll = attackRoll();
        messageCallback.send(this.getName() + " rolled " + attackRoll + " attack points");
        int defenceRoll = defender.defenceRoll();
        messageCallback.send(defender.getName() + " rolled " + defenceRoll + " defence points");
        if (attackRoll - defenceRoll > 0) {
            defender.gotHit(attackRoll - defenceRoll);
            messageCallback.send(this.getName() + " dealt " + (attackRoll - defenceRoll) + " damage to " + defender.getName());
        }
        messageCallback.send(healthAmount+" enemy health");
        if(defender.getHealthAmount() <= 0 ){
            defender.deathCallback.call();
        }
        if(getHealthAmount() <= 0){
            deathCallback.call();
        }
    }
    public abstract Position enemyTurn(Player player);

    @Override
    public void accept(Player attacker) {

        attacker.visit(this);
    }
    @Override
    public void accept(Enemy attacker) {

        attacker.visit(this);
    }
    @Override
    public void accept(Empty empty) {

    }

}
