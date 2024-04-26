package dungeonmania.Player;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.BuildableEntity.Sceptre;
import dungeonmania.CollectableEntity.Bomb;
import dungeonmania.CollectableEntity.CollectableEntity;
import dungeonmania.CollectableEntity.InvincibilityPotion;
import dungeonmania.CollectableEntity.InvisibilityPotion;
import dungeonmania.entity.Entity;
import dungeonmania.entity.Mercenary;
import dungeonmania.entity.MovingEntity;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Entity{
    // attributes
    private double health;
    private double attackDamage;
    private Inventory inventory = new Inventory();
    private List<Buff> buffList = new ArrayList<>();
    private Position prePosition;
    private boolean isInvisible = false; 

    private boolean isInvincible = false; 

    private double defenceBonus = 0;
    private double attackBonus = 0;
 

    // constructor
    public Player(Position position, String type, double health, double attackDamage) {
        super(position, type);
        this.health = health;
        this.attackDamage = attackDamage;
        prePosition = position;
    }

    public double getHealth() {
        return this.health;
    }

    public double getAttackDamage() {
        return this.attackDamage;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public List<Buff> getBuffList() {
        return this.buffList;
    }

    public void setPrePosition(Position prePosition) {
        this.prePosition = prePosition;
    }
 
    public Position getPrePosition() {
        return prePosition;
    }

    public boolean isInvisible() {
        return this.isInvisible;
    }

    public boolean isInvincible() {
        return this.isInvincible;
    }

    public void move(Direction direction) {
        setPrePosition(getPosition());
        setPosition(getPosition().translateBy(direction));
    }
 
    
    public List<ItemResponse> attack(MovingEntity enemy) {
        List<ItemResponse> weaponaryUsed = new ArrayList<ItemResponse>();
        double damage = attackDamage + attackBonus;
        double enemyHealth = enemy.getHealth();
        if (isInvincible) {
            weaponaryUsed.add(new ItemResponse(buffList.get(0).getId(), buffList.get(0).buffType()));
            damage = -enemyHealth;
        } else {
            damage = inventory.calculateDamage(damage, weaponaryUsed, enemy);
        }
        enemy.setHealth(enemyHealth+damage);
        return weaponaryUsed;
    }
 
 
 
    public boolean collect(Entity entity, Dungeon dungeon) {
        // check if entity is collectable 
        if (!entity.isCollectable()) return false;

        // hanlding special cases
        switch(entity.getType()) {
            case "bomb":
                // if bomb is collected, then cannot pick up again
                // if (((Bomb) entity).isCollected()) {return false;}
                // // bomb has not yet collected, it is now being collected.
                // ((Bomb) entity).collected();
                Bomb bomb = (Bomb) entity;
                if (bomb.isCollected()) {
                    return false;
                } else {
                    bomb.collected();
                }
                break;
            case "key":
                // check if player carries another key, if so this key cannot be collected
                if (inventory.hasItem("key")) return false;
                break;
        }


        // remove item from dungeon
        dungeon.removeEntity(entity);
        // add entity to collectableItems in the inventory
        return inventory.getCollectItems().add((CollectableEntity)entity); 
    }
 
 
    public void consume(String itemUsedId, Dungeon dungeon) {
        // search item in inventory, the null situation is handled in dmc before this method is called.
        Entity item = inventory.getEntity(itemUsedId);
        String type = item.getType();
        switch(type) {
            case "bomb":
                // place the bomb on the map at where the player's current position
                //Position position = new Position(this.getPosition().getX(), this.getPosition().getY());
                Bomb bomb = (Bomb)item;
                bomb.setPosition(getPosition());
                dungeon.getEntities().add(bomb);
                bomb.use(dungeon);
                break;
            case "invincibility_potion":
                buffList.add(new Buff(item.getId(), type, ((InvincibilityPotion) item).getInvincibility_potion_duration()));
                if (!inPotionEffect()) this.isInvincible = true;
                break;
            case "invisibility_potion":
                buffList.add(new Buff(item.getId(), type, ((InvisibilityPotion) item).getInvisibility_potion_duration()));
                if (!inPotionEffect()) this.isInvisible = true;
                break;
        }
        // remove the item from the inventory
        inventory.getCollectItems().remove(item);
    }
    
    public boolean inPotionEffect() {
        return isInvincible || isInvisible;
    }

    /**
     * 
     * @pre-condition item is one of type {"bow", "shield", "sceptre", "midnight_armour"} [Note, this is handle by build(String buildable) in dmc]
     * @post-condition item is build and stored in inventory, corresponding consumed item is removed from inventory
     * @param item item to be built
     * @param configName config json file name
     * @return true if build is done successfully, false otherwise
     */
    public boolean build(String item, String configName) {
        return inventory.build(item, configName);
    }
    

    // this is called when enemy attack player 
    public void takeDamage(double attackDamage) {
        double damage = attackDamage;
        // get the defence value
        double defence = this.defenceBonus + inventory.calculateDefence();
        damage = (damage - defence)/10;
        // if damage is not negative, reduce player's health
        if (damage > 0) this.health -= damage;
    }

    public void addAttack(double attackBonus) {
        this.attackBonus += attackBonus;
    }

    public void addDefence(double defenceBonus) {
        this.defenceBonus += defenceBonus;
    }

    @Override
    public JSONObject getEntityStateAsJSON() {
        JSONObject entityObject = super.getEntityStateAsJSON();
        
        entityObject.put("invincible", isInvincible);
        entityObject.put("invisible", isInvisible);

        for (Buff buff: buffList) {
            entityObject.put("buffId", buff.getId());
            entityObject.put("buffType", buff.buffType());
            entityObject.put("buffDuration", buff.getBuffDuration());
        }
        
        JSONArray inventoryJson = new JSONArray();
        for (Entity entity: getInventory().getBuildItems()) inventoryJson.put(entity.getEntityStateAsJSON());
        for (Entity entity: getInventory().getCollectItems()) inventoryJson.put(entity.getEntityStateAsJSON());
        entityObject.put("inventory", inventoryJson);
        
        return entityObject;
    }

    public boolean bribe(MovingEntity enemy) {
        // if player has sceptre, mind control the enemy
        if (getInventory().hasItem("sceptre")) {
            Sceptre sceptre = (Sceptre) getInventory().getBuildItem("sceptre");
            return sceptre.mindControl(enemy);
        }
        return bribe((Mercenary) enemy);
    }

    private boolean bribe(Mercenary enemy) {
        // check bride radius and bride amount
        if(getPosition().withinRadius(enemy.getPosition(), enemy.getBribeRadius()) && getInventory().countTreasure() >= enemy.getBribeAmount() && enemy.isEnemy()) {
            // remove treasures from inventory
            getInventory().removeItem("treasure",(int) enemy.getBribeAmount());
            return enemy.toAlly(this);
        }
        return false; 
    }
}
