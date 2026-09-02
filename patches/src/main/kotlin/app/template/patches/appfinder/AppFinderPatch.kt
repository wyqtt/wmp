package wy.morphe.patches.appfinder

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.removeInstructions
import app.morphe.patcher.patch.bytecodePatch
import wy.morphe.patches.shared.Constants.APP_FINDER_COMPATIBILITY

@Suppress("unused")
val appFinderPatch = bytecodePatch(
    name = "Pro",
    description = "Enable pro.",
) {
    compatibleWith(APP_FINDER_COMPATIBILITY)

    execute {
        // Patch getB() to always return true
        PremiumCheckFingerprint.method.apply {
            removeInstructions(0, implementation!!.instructions.size)
            addInstructions(
                0,
                """
                    const/4 v0, 0x1
                    return v0
                """.trimIndent(),
            )
        }

        // Patch setB() to do nothing (prevent billing system from setting it to false)
        PremiumSetFingerprint.method.addInstructions(
            0,
            "return-void",
        )
    }
}
