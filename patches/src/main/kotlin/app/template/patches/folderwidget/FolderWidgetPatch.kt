package wy.morphe.patches.folderwidget

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import wy.morphe.patches.shared.Constants.FOLDER_WIDGET_COMPATIBILITY

@Suppress("unused")
val folderWidgetPatch = bytecodePatch(
    name = "Pro",
    description = "Enable pro",
) {
    compatibleWith(FOLDER_WIDGET_COMPATIBILITY)

    execute {
        // TEMPORARY: Only patch User.isAf() to confirm the repackage pipeline works.
        // The universal getter hook causes a startup hang — diagnosing separately.

        // Account-level member flag → true
        IsAfFingerprint.method.addInstructions(
            0,
            """
            const/4 v0, 0x1
            return v0
            """.trimIndent(),
        )
    }
}
