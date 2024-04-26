package dungeonmania.Player;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.BuildableEntity.Bow;
import dungeonmania.BuildableEntity.BuildableEntity;
import dungeonmania.BuildableEntity.MidnightArmour;
import dungeonmania.BuildableEntity.Sceptre;
import dungeonmania.BuildableEntity.Shield;
import dungeonmania.CollectableEntity.CollectableEntity;
import dungeonmania.CollectableEntity.Key;
import dungeonmania.CollectableEntity.Sword;
import dungeonmania.entity.Entity;
import dungeonmania.entity.MovingEntity;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.ExtractFromJson;
import dungeonmania.util.Position;

public class Inventory {

    private List<CollectableEntity> collectItems = new ArrayList<CollectableEntity>();
    private List<BuildableEntity> buildItems = new ArrayList<BuildableEntity>();

    public Inventory() {

    }


    public List<CollectableEntity> getCollectItems() {
        return this.collectItems;
    }

    public List<BuildableEntity> getBuildItems() {
        return this.buildItems;
    }
    
    public Entity getEntity(String itemUsedId) {
        for (CollectableEntity item : collectItems) {
            if (item.getId().equals(itemUsedId)) return (Entity) item;
        }
        return null;
    }

    // return key value, if no key in the inventory, return -1
    public int getKeyValue() {
        if (hasItem("key")) {
            return ((Key) collectItems.stream().filter(item -> item.getType().equals("key")).findAny().get()).getKey();
        }
        return -1;
    }


    public double calculateDamage(double attackDamage, List<ItemResponse> weaponaryUsed, MovingEntity enemy) {
        double damage = attackDamage;

        // sword case:
        // check if sword exists
        if (hasItem("sword")) {
            Sword sword = (Sword) getCollectedItem("sword");
            damage += sword.getSwordAttack();
            // reduce sword duration by 1
            sword.setSword_durability(sword.getSwordDurability() - 1);
            // if sword duration is 0, remove sword from the inventory
            weaponaryUsed.add(new ItemResponse(sword.getId(), sword.getType()));
            if (sword.getSwordDurability() == 0) collectItems.remove(sword);
        }

        // midnight armour case 
        if (hasItem("midnight_armour")) {
            MidnightArmour midnightArmour = (MidnightArmour) getBuildItem("midnight_armour");
            damage += midnightArmour.getArmourAttack();
        }

        // bow case 
        // check if bow exists
        if (hasItem("bow")) {
            Bow bow = (Bow) getBuildItem("bow");
            // double damage
            damage *= 2;
            weaponaryUsed.add(new ItemResponse(bow.getId(), bow.getType()));
            // reduce bow duration by 1
            bow.setDuration(bow.getDuration() - 1);
            // if bow duration is 0, remove bow from the inventory
            if (bow.getDuration() == 0) buildItems.remove(bow);
        } 
        return -damage/5.0;
    }

    public boolean hasItem(String type) {
        return collectItems.stream().anyMatch(item -> item.getType().equals(type)) || buildItems.stream().anyMatch(item -> item.getType().equals(type));
    }

    public CollectableEntity getCollectedItem(String type) {
        return collectItems.stream().filter(item -> item.getType().equals(type)).findFirst().get();
    }

    public BuildableEntity getBuildItem(String type) {
        return buildItems.stream().filter(item -> item.getType().equals(type)).findFirst().get();
    }

    public List<ItemResponse> generateItemResponse() {
        List<ItemResponse> inventory = new ArrayList<ItemResponse>();
        collectItems.stream().forEach(item -> inventory.add(new ItemResponse(item.getId(), item.getType())));
        buildItems.stream().forEach(item -> inventory.add(new ItemResponse(item.getId(), item.getType())));
        return inventory;
    }

    public boolean build(String item, String configName) {
        switch(item) {
            case "bow":
                // check if inventory contains 1 wood and 3 arrows
                Bow bow = new Bow(new Position(1, 1), "bow", ExtractFromJson.getConfigIntFromJson(configName, "bow_durability"));
                return bow.build(this, configName);
            case "shield":
                // check if inventory contains 2 woods and (1 sunstone or 1 treasure or 1 key)
                Shield shield = new Shield(new Position(1, 1), "shield", ExtractFromJson.getConfigIntFromJson(configName, "shield_durability"), ExtractFromJson.getConfigIntFromJson(configName, "shield_defence"));
                return shield.build(this, configName);
            case "sceptre":
                Sceptre sceptre = new Sceptre(new Position(1, 1), "sceptre", ExtractFromJson.getConfigIntFromJson(configName, "mind_control_duration"));
                return sceptre.build(this, configName);
            case "midnight_armour":
                MidnightArmour midnightArmour = new MidnightArmour(new Position(1, 1), item, 0, ExtractFromJson.getConfigIntFromJson(configName, "midnight_armour_defence"), ExtractFromJson.getConfigIntFromJson(configName, "midnight_armour_attack"));
                return midnightArmour.build(this, configName);
        }
        return false;
    }

    /**
     * 
     * @pre-condition type correspond to a type in {"treasure", "key", "wood", "arrow"}, total number of item is greater the amount of item to be removed
     * @post-condition The number of item will be removed from collectItems
     * @param type type of item 
     * @param amount amount of item
     */
    public void removeItem(String type, int amount) {
        for (int i = 0; i < amount; i++) {
            collectItems.remove(collectItems.stream().filter(item -> item.getType().equals(type)).findFirst().get());
        }
    }
    
    public int countSunStone() {
        return (int) collectItems.stream().filter(item -> item.getType().equals("sun_stone")).count();
    }

    public int countTreasure() {
        return (int) collectItems.stream().filter(item -> item.getType().equals("treasure")).count();
    }

    public int countWood() {
        return (int) collectItems.stream().filter(item -> item.getType().equals("wood")).count();
    }

    public int countArrow() {
        return (int) collectItems.stream().filter(item -> item.getType().equals("arrow")).count();
    }


    public double calculateDefence() {
        int defence = 0;
        // check if player have shield
        if (hasItem("shield")) {
            // get shield
            Shield shield = (Shield) getBuildItem("shield");
            // use shield - shield duration - 1
            shield.setDuration(shield.getDuration() - 1);
            // get the defence value
            defence += shield.getDefence();
            // if shield is run out of duration, remove shield
            if (shield.getDuration() == 0) buildItems.remove(shield);
        }

        // check if player have midnight armour
        if (hasItem("midnight_armour")) {
            MidnightArmour armour = (MidnightArmour) getBuildItem("midnight_armour");
            defence += armour.getDefence();
        }

        return defence;
    }


    public List<String> getBuildables() {
        List<String> buildables = new ArrayList<String>();
        // check if player can build bow
        if (countWood() >= 1 && countArrow() >= 3) buildables.add("bow");
        // check if player can build shield
        if (countWood() >= 2 && (countTreasure() >= 1 || hasItem("key"))) buildables.add("shield");
        return buildables;
    }

    
}
