package dungeonmania.Player;

public class Buff {
    // invincibility or invisibility
    private String buffType;
    private double buffDuration;
    private String id;

    public Buff (String id, String buffType, double duration) {
        this.id = id;
        this.buffType = buffType;
        buffDuration = duration;
    }

    // consume duration and return duration left
    public double consumeDuration() {
        buffDuration--;
        return buffDuration;
    }

    public double getBuffDuration() {
        return buffDuration;
    }

    public String getId() {
        return id;
    }

    public String buffType() {
        return buffType;
    }
}
