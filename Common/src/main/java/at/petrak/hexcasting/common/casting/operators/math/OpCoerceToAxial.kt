package at.petrak.hexcasting.common.casting.operators.math

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getNumOrVec
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.core.Direction
import net.minecraft.world.phys.Vec3
import kotlin.math.sign

object OpCoerceToAxial : ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val value = args.getNumOrVec(0, argc)
        return value.map({ num ->
            num.sign.asActionResult
        }, { vec ->
            if (vec == Vec3.ZERO)
                vec.asActionResult
            else
                Vec3.atLowerCornerOf(Direction.getNearest(vec.x, vec.y, vec.z).normal).asActionResult
        })
    }
}
