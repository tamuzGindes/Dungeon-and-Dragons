package BusinessLayer.Player;

import BusinessLayer.Enemy.Enemy;
import BusinessLayer.Objects.Position;
import BusinessLayer.Objects.Range;
import java.util.LinkedList;
import java.util.List;

public class Mage extends Player {
    private int manaPool;
    private int currentMana;
    private int manaCost;
    private int spellPower;
    private int hitsCount;
    private int abilityRange;
    
    protected static final int MANA_POOL_BONUS = 25;
    protected static final int SPELL_POWER_BONUS = 10;

    public Mage(Position pos, String name, int healthPool, int healthAmount, int attackPoints, int defencePoints, int manaPool, int manaCost, int spellPower, int hitsCount, int abilityRange) {
        super(pos, name, healthAmount, healthPool, attackPoints, defencePoints);
        this.manaPool = manaPool;
        this.currentMana = manaPool / 4;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitsCount = hitsCount;
        this.abilityRange = abilityRange;
    }

    public int getManaPool() {
        return manaPool;
    }
    public int getCurrentMana() {
        return currentMana;
    }
    public int getManaCost() {
        return manaCost;
    }
    public int getSpellPower() {
        return spellPower;
    }
    public int getHitsCount() {
        return hitsCount;
    }
    public int getAbilityRange()  { return abilityRange; }

    @Override
    public void processStep() {

    }

    @Override
    public void gameTick() {
        currentMana = Math.min(manaPool,currentMana + level);
        hitsCount = 0;
    }

    @Override
    public void castAbility(List<Enemy> enemies, Player player) {
        if(currentMana - manaCost >= 0) {
            currentMana = currentMana - manaCost;
            int hits = 0;
    
            if (enemies == null){
            	return;
            }
            List<Enemy> aliveEnemies = new LinkedList<Enemy>();
            for (Enemy enemy : enemies) {
            	if ((enemy.getHealthAmount() > 0) & Range.range(this, enemy) < getAbilityRange()) {
                    aliveEnemies.add(enemy);
                }
            }
            while ((hits < getHitsCount()) & (!aliveEnemies.isEmpty())) {
                int randomIndex = (int) Math.floor(Math.random() * (aliveEnemies.size()));
                Enemy enemy = aliveEnemies.get(randomIndex);
                int defence = enemy.defenceRoll();
                if (spellPower > defence) {
                    int hitPower = spellPower - defence;
                    enemy.gotHit(hitPower);
                    hits = hits + 1;
                    messageCallback.send(this.name + " cast Special ability- Blizzard and made damage of , " + hitPower + " to " + enemy.getName());
                    messageCallback.send(this.getName() + " rolled " + hitPower + " attack points");
                    messageCallback.send(enemy.getName() + " rolled " + defence + " defence points");
                    messageCallback.send(this.getName() + " dealt " + (hitPower - defence) + " damage to " + enemy.getName());
                }
                if (enemy.getHealthAmount() <= 0) {
                    messageCallback.send(this.name + " killed " + enemy.getName() + "and earned " + enemy.getExperience() + " for it");
                    addExperience(enemy.getExperience());
                    enemy.deathCallback.call();
                }
            }
        }
        else{
            messageCallback.send(this.name+" doesn't have enough resources to cast special ability");
        }

    }

    @Override
    public String describe() {
       //return String.format("Playe name : %s      Player type : Mage     Player health : %d    Player %s : %d     Player %s : d%    Player Level : %d      Player Experience : %d",getName(),getHealthAmount(),"current Mana ",currentMana,"spell Power",getspellPower(),getLevel(),getExperience());
        return "Player name: " + getName() + " ---- Player type: Mage ---- Player health: " + getHealthAmount() + " ---- current Mana: " + currentMana + " ---- Spell Power: " + getSpellPower() + " ---- Player Level: " + getLevel() + " ---- Player Experience: " + getExperience();
    }


    protected void levelUp() {
        super.levelUp();
        manaPool = getManaPool() + (MANA_POOL_BONUS*getLevel());
        currentMana = Math.min(getCurrentMana() + (getManaPool()/4) , manaPool);
        spellPower = getSpellPower() + (SPELL_POWER_BONUS*getLevel());

    }
}
