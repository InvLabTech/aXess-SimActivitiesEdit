package net.teekay.axess.utilities;

import net.minecraft.world.entity.player.Player;
import net.teekay.axess.access.AccessNetwork;

public class AccessUtils {

    public static boolean canPlayerEditNetwork(Player player, AccessNetwork network) {
        if (network == null) return true;

        // 让所有玩家都能编辑网络，实现共享权限空间
        return true;
    }

}