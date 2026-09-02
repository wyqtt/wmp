package wy.morphe.patches.appfinder

import app.morphe.patcher.patch.bytecodePatch
import wy.morphe.patches.shared.Constants.APP_FINDER_COMPATIBILITY

@Suppress("unused")
val appFinderPatch = bytecodePatch(
    name = "Pro",
    description = "Enable pro.",
) {
    compatibleWith(APP_FINDER_COMPATIBILITY)

    execute {
        // Target the getB() method which returns subscription status
        PremiumCheckFingerprint.method.apply {
            // Clear existing instructions and replace with: return true
            implementation!!.instructions.clear()

            // const/4 v0, 0x1 (load 1/true into register v0)
            addInstruction(
                com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction11n(
                    com.android.tools.smali.dexlib2.Opcode.CONST_4,
                    0,  // register v0
                    1   // value = 1 (true)
                )
            )

            // return v0
            addInstruction(
                com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction11x(
                    com.android.tools.smali.dexlib2.Opcode.RETURN,
                    0   // register v0
                )
            )
        }
    }
}
