package net.teekay.axess.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.teekay.axess.Axess;
import net.teekay.axess.item.KeycardItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Supplier;

public class AxessIconRegistry {
    

    private static HashMap<String, ResourceLocation> ENTRIES = new HashMap<>();
    private static HashMap<String, ResourceLocation> META_ENTRIES = new HashMap<>();

    // ICONS

    // META
    public static ResourceLocation SCAN = registerMetaIcon("scan");
    public static ResourceLocation ACCEPT = registerMetaIcon("accept");
    public static ResourceLocation DENY = registerMetaIcon("deny");


    // NORMAL ALPHABET
    public static ResourceLocation A = registerIcon("a");
    public static ResourceLocation B = registerIcon("b");
    public static ResourceLocation C = registerIcon("c");
    public static ResourceLocation D = registerIcon("d");
    public static ResourceLocation E = registerIcon("e");
    public static ResourceLocation F = registerIcon("f");
    public static ResourceLocation G = registerIcon("g");
    public static ResourceLocation H = registerIcon("h");
    public static ResourceLocation I = registerIcon("i");
    public static ResourceLocation J = registerIcon("j");
    public static ResourceLocation K = registerIcon("k");
    public static ResourceLocation L = registerIcon("l");
    public static ResourceLocation M = registerIcon("m");
    public static ResourceLocation N = registerIcon("n");
    public static ResourceLocation O = registerIcon("o");
    public static ResourceLocation P = registerIcon("p");
    public static ResourceLocation Q = registerIcon("q");
    public static ResourceLocation R = registerIcon("r");
    public static ResourceLocation S = registerIcon("s");
    public static ResourceLocation T = registerIcon("t");
    public static ResourceLocation U = registerIcon("u");
    public static ResourceLocation V = registerIcon("v");
    public static ResourceLocation W = registerIcon("w");
    public static ResourceLocation X = registerIcon("x");
    public static ResourceLocation Y = registerIcon("y");
    public static ResourceLocation Z = registerIcon("z");
    
    // SQUARE ALPHABET
    public static ResourceLocation SQUARE_A = registerIcon("square_a");
    public static ResourceLocation SQUARE_B = registerIcon("square_b");
    public static ResourceLocation SQUARE_C = registerIcon("square_c");
    public static ResourceLocation SQUARE_D = registerIcon("square_d");
    public static ResourceLocation SQUARE_E = registerIcon("square_e");
    public static ResourceLocation SQUARE_F = registerIcon("square_f");
    public static ResourceLocation SQUARE_G = registerIcon("square_g");
    public static ResourceLocation SQUARE_H = registerIcon("square_h");
    public static ResourceLocation SQUARE_I = registerIcon("square_i");
    public static ResourceLocation SQUARE_J = registerIcon("square_j");
    public static ResourceLocation SQUARE_K = registerIcon("square_k");
    public static ResourceLocation SQUARE_L = registerIcon("square_l");
    public static ResourceLocation SQUARE_M = registerIcon("square_m");
    public static ResourceLocation SQUARE_N = registerIcon("square_n");
    public static ResourceLocation SQUARE_O = registerIcon("square_o");
    public static ResourceLocation SQUARE_P = registerIcon("square_p");
    public static ResourceLocation SQUARE_Q = registerIcon("square_q");
    public static ResourceLocation SQUARE_R = registerIcon("square_r");
    public static ResourceLocation SQUARE_S = registerIcon("square_s");
    public static ResourceLocation SQUARE_T = registerIcon("square_t");
    public static ResourceLocation SQUARE_U = registerIcon("square_u");
    public static ResourceLocation SQUARE_V = registerIcon("square_v");
    public static ResourceLocation SQUARE_W = registerIcon("square_w");
    public static ResourceLocation SQUARE_X = registerIcon("square_x");
    public static ResourceLocation SQUARE_Y = registerIcon("square_y");
    public static ResourceLocation SQUARE_Z = registerIcon("square_z");
    
    // NORMAL NUMBERS
    public static ResourceLocation ZERO = registerIcon("zero");
    public static ResourceLocation ONE = registerIcon("one");
    public static ResourceLocation TWO = registerIcon("two");
    public static ResourceLocation THREE = registerIcon("three");
    public static ResourceLocation FOUR = registerIcon("four");
    public static ResourceLocation FIVE = registerIcon("five");
    public static ResourceLocation SIX = registerIcon("six");
    public static ResourceLocation SEVEN = registerIcon("seven");
    public static ResourceLocation EIGHT = registerIcon("eight");
    public static ResourceLocation NINE = registerIcon("nine");

    // ROMAN NUMBERS
    public static ResourceLocation ROMAN_ONE = registerIcon("roman_one");
    public static ResourceLocation ROMAN_TWO = registerIcon("roman_two");
    public static ResourceLocation ROMAN_THREE = registerIcon("roman_three");
    public static ResourceLocation ROMAN_FOUR = registerIcon("roman_four");
    public static ResourceLocation ROMAN_FIVE = registerIcon("roman_five");
    public static ResourceLocation ROMAN_SIX = registerIcon("roman_six");
    public static ResourceLocation ROMAN_SEVEN = registerIcon("roman_seven");
    public static ResourceLocation ROMAN_EIGHT = registerIcon("roman_eight");
    public static ResourceLocation ROMAN_NINE = registerIcon("roman_nine");
    public static ResourceLocation ROMAN_TEN = registerIcon("roman_ten");

    // SQUARE NUMBERS
    public static ResourceLocation SQUARE_ZERO = registerIcon("square_zero");
    public static ResourceLocation SQUARE_ONE = registerIcon("square_one");
    public static ResourceLocation SQUARE_TWO = registerIcon("square_two");
    public static ResourceLocation SQUARE_THREE = registerIcon("square_three");
    public static ResourceLocation SQUARE_FOUR = registerIcon("square_four");
    public static ResourceLocation SQUARE_FIVE = registerIcon("square_five");
    public static ResourceLocation SQUARE_SIX = registerIcon("square_six");
    public static ResourceLocation SQUARE_SEVEN = registerIcon("square_seven");
    public static ResourceLocation SQUARE_EIGHT = registerIcon("square_eight");
    public static ResourceLocation SQUARE_NINE = registerIcon("square_nine");

    // GREEK ALPHABET
    public static ResourceLocation PI = registerIcon("pi");
    public static ResourceLocation PHI = registerIcon("phi");
    public static ResourceLocation LAMBDA = registerIcon("lambda");
    public static ResourceLocation OMEGA = registerIcon("omega");
    public static ResourceLocation PSI = registerIcon("psi");
    public static ResourceLocation THETA = registerIcon("theta");
    public static ResourceLocation DELTA = registerIcon("delta");

    // SHAPES
    public static ResourceLocation SQUARE = registerIcon("square");
    public static ResourceLocation SQUARE_FILLED = registerIcon("square_filled");
    public static ResourceLocation CIRCLE = registerIcon("circle");
    public static ResourceLocation CIRCLE_FILLED = registerIcon("circle_filled");
    public static ResourceLocation TRIANGLE = registerIcon("triangle");
    public static ResourceLocation TRIANGLE_FILLED = registerIcon("triangle_filled");

    // MISCELLANEOUS
    public static ResourceLocation SIX_NINE = registerIcon("six_nine");
    public static ResourceLocation SIX_SEVEN = registerIcon("six_seven");
    public static ResourceLocation HOME = registerIcon("home");
    public static ResourceLocation FILLED = registerIcon("filled");

    public static ResourceLocation getIcon(String iconID) {
        ResourceLocation iconTex = ENTRIES.get(iconID);
        if (iconTex != null) return iconTex;

        iconTex = META_ENTRIES.get(iconID);
        if (iconTex != null) return iconTex;

        return FILLED;
    }

    public static ResourceLocation registerIcon(String iconID) {
        ResourceLocation newItemTex = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/icon/" + iconID + ".png");
        ENTRIES.put(iconID, newItemTex);

        return newItemTex;
    }

    public static ResourceLocation registerMetaIcon(String iconID) {
        ResourceLocation newItemTex = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/icon/" + iconID + ".png");
        META_ENTRIES.put(iconID, newItemTex);

        return newItemTex;
    }

    public static Collection<ResourceLocation> getAllEntries() {
        return ENTRIES.values();
    }

    public static Collection<ResourceLocation> getAllMetaEntries() {
        return META_ENTRIES.values();
    }
}