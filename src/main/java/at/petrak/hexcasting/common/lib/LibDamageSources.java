package at.petrak.hexcasting.common.lib;

import net.minecraft.world.damagesource.DamageSource;

public class LibDamageSources {
    public static final DamageSource OVERCAST = new DamageSource("hexcasting.overcast").bypassArmor()
        .bypassMagic()
        .setMagic();
}
