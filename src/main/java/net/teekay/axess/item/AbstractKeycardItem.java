package net.teekay.axess.item;

import net.minecraft.world.item.Item;
import net.teekay.axess.access.AccessLevel;

public abstract class AbstractKeycardItem extends Item {

    public AbstractKeycardItem(Item.Properties properties) {
        super(properties);
    }

    public AccessLevel getAccessLevel() {
        return null;
    }

}
