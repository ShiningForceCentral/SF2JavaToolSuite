package com.sfc.sf2.map;

/**
 * A mapsetup entity from s1_entities.asm.
 *
 * @author TiMMy
 */
public class MapEntity {

    private final int x;
    private final int y;
    private final String facing;
    private final String sprite;
    private final boolean walking;

    public MapEntity(int x, int y, String facing, String sprite, boolean walking) {
        this.x = x;
        this.y = y;
        this.facing = facing;
        this.sprite = sprite;
        this.walking = walking;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getFacing() {
        return facing;
    }

    public String getSprite() {
        return sprite;
    }

    public boolean isWalking() {
        return walking;
    }

    public String getDisplayName() {
        String name = sprite;
        int underscore = sprite == null ? -1 : sprite.indexOf('_');
        if (underscore >= 0 && underscore < sprite.length() - 1) {
            name = sprite.substring(underscore + 1);
        }
        return name;
    }

    public boolean isOffMapPlaceholder() {
        return x >= 63 && y >= 63;
    }
}
