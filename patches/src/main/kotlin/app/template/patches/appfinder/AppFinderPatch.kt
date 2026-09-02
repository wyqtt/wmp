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
        // Target the getB() method which returns subscription status
        PremiumCheckFingerprint.method.apply {
            // Remove all existing instructions (iget-boolean + return)
            removeInstructions(0, implementation!!.instructions.size)

            // Add new instructions that always return true
            addInstructions(
                0,
                """
                    const/4 v0, 0x1
                    return v0
                """.trimIndent(),
            )
        }
    }
}
