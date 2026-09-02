package wy.morphe.patches.appfinder

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
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
        PremiumCheckFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """.trimIndent(),
        )
    }
}
