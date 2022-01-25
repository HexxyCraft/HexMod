package at.petrak.hexcasting.common.casting;

import at.petrak.hexcasting.HexMod;
import at.petrak.hexcasting.api.Operator;
import at.petrak.hexcasting.api.PatternRegistry;
import at.petrak.hexcasting.api.SpellDatum;
import at.petrak.hexcasting.common.casting.operators.*;
import at.petrak.hexcasting.common.casting.operators.lists.OpAppend;
import at.petrak.hexcasting.common.casting.operators.lists.OpConcat;
import at.petrak.hexcasting.common.casting.operators.lists.OpForEach;
import at.petrak.hexcasting.common.casting.operators.lists.OpIndex;
import at.petrak.hexcasting.common.casting.operators.math.*;
import at.petrak.hexcasting.common.casting.operators.selectors.OpGetCaster;
import at.petrak.hexcasting.common.casting.operators.selectors.OpGetEntitiesBy;
import at.petrak.hexcasting.common.casting.operators.selectors.OpGetEntityAt;
import at.petrak.hexcasting.common.casting.operators.spells.*;
import at.petrak.hexcasting.common.casting.operators.spells.great.OpCreateLava;
import at.petrak.hexcasting.common.casting.operators.spells.great.OpFlight;
import at.petrak.hexcasting.common.casting.operators.spells.great.OpLightning;
import at.petrak.hexcasting.common.casting.operators.spells.great.OpTeleport;
import at.petrak.hexcasting.common.items.magic.ItemArtifact;
import at.petrak.hexcasting.common.items.magic.ItemCypher;
import at.petrak.hexcasting.common.items.magic.ItemTrinket;
import at.petrak.hexcasting.hexmath.HexDir;
import at.petrak.hexcasting.hexmath.HexPattern;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static at.petrak.hexcasting.common.lib.RegisterHelper.prefix;

public class RegisterPatterns {
    // I guess this means the client will have a big empty map for patterns
    @SubscribeEvent
    public static void registerSpellPatterns(FMLCommonSetupEvent evt) {
        int count = 0;
        try {
            // In general:
            // - CCW is the normal or construction version
            // - CW is the special or destruction version
            // == Getters ==

            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qaq", HexDir.NORTH_EAST), prefix("get_caster"),
                OpGetCaster.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("aa", HexDir.EAST), prefix("get_entity_pos"),
                OpEntityPos.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("wa", HexDir.EAST), prefix("get_entity_look"),
                OpEntityLook.INSTANCE);

            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("wqaawdd", HexDir.EAST), prefix("raycast"),
                OpBlockRaycast.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("weddwaa", HexDir.EAST), prefix("raycast/axis"),
                OpBlockAxisRaycast.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("weaqa", HexDir.EAST), prefix("raycast/entity"),
                OpEntityRaycast.INSTANCE);

            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("edqde", HexDir.SOUTH_WEST), prefix("append"),
                OpAppend.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qaeaq", HexDir.NORTH_WEST), prefix("concat"),
                OpConcat.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("deeed", HexDir.NORTH_EAST), prefix("index"),
                OpIndex.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("dadad", HexDir.NORTH_EAST), prefix("for_each"),
                OpForEach.INSTANCE);

            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqqqqdaqa", HexDir.SOUTH_WEST), prefix("get_entity"),
                new OpGetEntityAt(e -> true));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqqqqdaqaawa", HexDir.SOUTH_WEST),
                prefix("get_entity/animal"),
                new OpGetEntityAt(OpGetEntitiesBy::isAnimal));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqqqqdaqaawq", HexDir.SOUTH_WEST),
                prefix("get_entity/monster"),
                new OpGetEntityAt(OpGetEntitiesBy::isMonster));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqqqqdaqaaww", HexDir.SOUTH_WEST),
                prefix("get_entity/item"),
                new OpGetEntityAt(OpGetEntitiesBy::isItem));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqqqqdaqaawe", HexDir.SOUTH_WEST),
                prefix("get_entity/player"),
                new OpGetEntityAt(OpGetEntitiesBy::isPlayer));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqqqqdaqaawd", HexDir.SOUTH_WEST),
                prefix("get_entity/living"),
                new OpGetEntityAt(OpGetEntitiesBy::isLiving));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqqqqwded", HexDir.SOUTH_WEST), prefix("zone_entity"),
                new OpGetEntitiesBy(e -> true, false));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqqqqwdeddwa", HexDir.SOUTH_WEST),
                prefix("zone_entity/animal"),
                new OpGetEntitiesBy(OpGetEntitiesBy::isAnimal, false));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("eeeeewaqaawa", HexDir.NORTH_WEST),
                prefix("zone_entity/not_animal"),
                new OpGetEntitiesBy(OpGetEntitiesBy::isAnimal, true));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqqqqwdeddwq", HexDir.SOUTH_WEST),
                prefix("zone_entity/monster"),
                new OpGetEntitiesBy(OpGetEntitiesBy::isMonster, false));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("eeeeewaqaawq", HexDir.NORTH_WEST),
                prefix("zone_entity/not_monster"),
                new OpGetEntitiesBy(OpGetEntitiesBy::isMonster, true));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqqqqwdeddww", HexDir.SOUTH_WEST),
                prefix("zone_entity/item"),
                new OpGetEntitiesBy(OpGetEntitiesBy::isItem, false));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("eeeeewaqaaww", HexDir.NORTH_WEST),
                prefix("zone_entity/not_item"),
                new OpGetEntitiesBy(OpGetEntitiesBy::isItem, true));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqqqqwdeddwe", HexDir.SOUTH_WEST),
                prefix("zone_entity/player"),
                new OpGetEntitiesBy(OpGetEntitiesBy::isPlayer, false));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("eeeeewaqaawe", HexDir.NORTH_WEST),
                prefix("zone_entity/not_player"),
                new OpGetEntitiesBy(OpGetEntitiesBy::isPlayer, true));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqqqqwdeddwd", HexDir.SOUTH_WEST),
                prefix("zone_entity/living"),
                new OpGetEntitiesBy(OpGetEntitiesBy::isLiving, false));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("eeeeewaqaawd", HexDir.NORTH_WEST),
                prefix("zone_entity/not_living"),
                new OpGetEntitiesBy(OpGetEntitiesBy::isLiving, true));

            // == Modify Stack ==

            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("a", HexDir.EAST), prefix("undo"), OpUndo.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("d", HexDir.EAST), prefix("const/null"), Widget.NULL);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("aadaa", HexDir.EAST), prefix("duplicate"),
                OpDuplicate.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("aawdd", HexDir.EAST), prefix("swap"), OpSwap.INSTANCE);

            // == Math ==
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("waaw", HexDir.NORTH_EAST), prefix("add"),
                OpAdd.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("wddw", HexDir.NORTH_WEST), prefix("sub"),
                OpSub.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("waqaw", HexDir.SOUTH_EAST), prefix("mul_dot"),
                OpMulDot.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("wdedw", HexDir.NORTH_EAST), prefix("div_cross"),
                OpDivCross.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("wqaqw", HexDir.NORTH_EAST), prefix("abs_len"),
                OpAbsLen.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("wedew", HexDir.NORTH_WEST), prefix("pow_proj"),
                OpPowProj.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("eqqqqq", HexDir.EAST), prefix("construct_vec"),
                OpConstructVec.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qeeeee", HexDir.EAST), prefix("deconstruct_vec"),
                OpDeconstructVec.INSTANCE);

            // == Spells ==

            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("de", HexDir.NORTH_EAST), prefix("print"),
                OpPrint.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("aawaawaa", HexDir.EAST), prefix("explode"),
                new OpExplode(false));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("ddwddwdd", HexDir.EAST), prefix("explode/fire"),
                new OpExplode(true));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("awqqqwaqw", HexDir.SOUTH_EAST), prefix("add_motion"),
                OpAddMotion.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("awqqqwaq", HexDir.SOUTH_EAST), prefix("blink"),
                OpBlink.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qaqqqqq", HexDir.EAST), prefix("break_block"),
                OpBreakBlock.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("eeeeede", HexDir.SOUTH_WEST), prefix("place_block"),
                OpPlaceBlock.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("waqqqqq", HexDir.EAST), prefix("craft/cypher"),
                new OpMakePackagedSpell<>(ItemCypher.class, 100_000));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("wwaqqqqqeaqeaeqqqeaeq", HexDir.EAST),
                prefix("craft/trinket"),
                new OpMakePackagedSpell<>(ItemTrinket.class, 500_000));
            PatternRegistry.mapPattern(
                HexPattern.FromAnglesSig("wwaqqqqqeawqwqwqwqwqwwqqeadaeqqeqqeadaeqq", HexDir.EAST),
                prefix("craft/artifact"),
                new OpMakePackagedSpell<>(ItemArtifact.class, 1_000_000));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("aqawqadaq", HexDir.SOUTH_EAST), prefix("create_water"),
                OpCreateWater.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("dedwedade", HexDir.SOUTH_WEST),
                prefix("destroy_water"), OpDestroyWater.INSTANCE);


            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("waadwawdaaweewq", HexDir.EAST),
                prefix("lightning"), OpLightning.INSTANCE, true);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("eawwaeawawaa", HexDir.NORTH_WEST),
                prefix("flight"), OpFlight.INSTANCE, true);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("eaqawqadaqd", HexDir.EAST),
                prefix("create_lava"), OpCreateLava.INSTANCE, true);
            PatternRegistry.mapPattern(
                HexPattern.FromAnglesSig("wwwqqqwwwqqeqqwwwqqwqqdqqqqqdqq", HexDir.EAST),
                prefix("teleport"), OpTeleport.INSTANCE, true);

            // == Meta stuff ==
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqq", HexDir.WEST), prefix("open_paren"),
                Widget.OPEN_PAREN);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("eee", HexDir.EAST), prefix("close_paren"),
                Widget.CLOSE_PAREN);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqqaw", HexDir.WEST), prefix("escape"), Widget.ESCAPE);
            // http://www.toroidalsnark.net/mkss3-pix/CalderheadJMM2014.pdf
            // eval being a space filling curve feels apt doesn't it
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("deaqq", HexDir.SOUTH_EAST), prefix("eval"),
                OpEval.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("aqdee", HexDir.SOUTH_WEST), prefix("eval/delay"),
                OpEvalDelay.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("aqqqqq", HexDir.EAST), prefix("read"),
                OpRead.INSTANCE);
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("deeeee", HexDir.EAST), prefix("write"),
                OpWrite.INSTANCE);

            // == Consts ==
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqqqqea", HexDir.NORTH_WEST), prefix("const/vec/px"),
                Operator.makeConstantOp(SpellDatum.make(new Vec3(1.0, 0.0, 0.0))));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqqqqew", HexDir.NORTH_WEST), prefix("const/vec/py"),
                Operator.makeConstantOp(SpellDatum.make(new Vec3(0.0, 1.0, 0.0))));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("qqqqqed", HexDir.NORTH_WEST), prefix("const/vec/pz"),
                Operator.makeConstantOp(SpellDatum.make(new Vec3(0.0, 0.0, 1.0))));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("eeeeeqa", HexDir.SOUTH_WEST), prefix("const/vec/nx"),
                Operator.makeConstantOp(SpellDatum.make(new Vec3(-1.0, 0.0, 0.0))));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("eeeeeqw", HexDir.SOUTH_WEST), prefix("const/vec/ny"),
                Operator.makeConstantOp(SpellDatum.make(new Vec3(0.0, -1.0, 0.0))));
            PatternRegistry.mapPattern(HexPattern.FromAnglesSig("eeeeeqd", HexDir.SOUTH_WEST), prefix("const/vec/nz"),
                Operator.makeConstantOp(SpellDatum.make(new Vec3(0.0, 0.0, -1.0))));

        } catch (PatternRegistry.RegisterPatternException exn) {
            exn.printStackTrace();
        }

        // Add zilde->number
        PatternRegistry.addSpecialHandler(pat -> {
            var sig = pat.anglesSignature();
            if (sig.startsWith("aqaa") || sig.startsWith("dedd")) {
                var negate = sig.startsWith("dedd");
                var accumulator = 0.0;
                for (char ch : sig.substring(4).toCharArray()) {
                    if (ch == 'w') {
                        accumulator += 1;
                    } else if (ch == 'q') {
                        accumulator += 5;
                    } else if (ch == 'e') {
                        accumulator += 10;
                    } else if (ch == 'a') {
                        accumulator *= 2;
                    } else if (ch == 'd') {
                        accumulator /= 2;
                    }
                }
                if (negate) {
                    accumulator = -accumulator;
                }
                return Operator.makeConstantOp(SpellDatum.make(accumulator));
            } else {
                return null;
            }
        });

        HexMod.getLogger().info("Registered {} patterns", count);
    }
}
