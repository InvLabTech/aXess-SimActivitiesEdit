package net.teekay.axess.client;

import net.minecraft.client.Minecraft;
import net.teekay.axess.access.AccessNetwork;
import net.teekay.axess.screen.NetworkCreationScreen;
import net.teekay.axess.screen.NetworkDeletionScreen;
import net.teekay.axess.screen.NetworkEditorScreen;
import net.teekay.axess.screen.NetworkManagerScreen;

public class AxessClientMenus {
    public static boolean openNetworkManagerScreen() {
        Minecraft.getInstance().setScreen(new NetworkManagerScreen());
        return true;
    }

    public static boolean openNetworkDeletionScreen(AccessNetwork net) {
        Minecraft.getInstance().setScreen(new NetworkDeletionScreen(net));
        return true;
    }

    public static boolean openNetworkCreationScreen() {
        Minecraft.getInstance().setScreen(new NetworkCreationScreen());
        return true;
    }

    public static boolean openNetworkEditorScreen(AccessNetwork net) {
        Minecraft.getInstance().setScreen(new NetworkEditorScreen(net));
        return true;
    }

    public static boolean closeScreen(AccessNetwork net) {
        Minecraft.getInstance().setScreen(null);
        return true;
    }
}
