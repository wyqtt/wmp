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
        // Patch getF() to return 100 (0x64) - the maximum feature level
        FeatureLevelFingerprint.method.apply {
            removeInstructions(0, implementation!!.instructions.size)
            addInstructions(
                0,
                """
                    const/16 v0, 0x64
                    return v0
                """.trimIndent(),
            )
        }
    }
}
