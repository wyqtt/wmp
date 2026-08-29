package wy.morphe.patches.folderwidget

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.patch.bytecodePatch
import wy.morphe.patches.shared.Constants.FOLDER_WIDGET_COMPATIBILITY

@Suppress("unused")
val folderWidgetPatch = bytecodePatch(
    name = "Pro",
    description = "Enable pro",
) {
    compatibleWith(FOLDER_WIDGET_COMPATIBILITY)

    execute {
        // Every pro-feature gate reads a single boolean from the "ba_sp"
        // SharedPreferences store through this generic getter. The flag lives
        // under a disguised, URL-shaped key. Intercept the getter and force the
        // result to TRUE only for that key; all other prefs reads fall through
        // to the original logic untouched.
        PrefsGetFingerprint.method.addInstructionsWithLabels(
            0,
            """
            const-string v0, "play.kotlinlang.org"
            invoke-virtual {p0, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z
            move-result v0
            if-eqz v0, :fw_original
            sget-object v0, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;
            return-object v0
            :fw_original
            nop
            """.trimIndent(),
        )

        // Account-level member flag → true, so the Pro/account UI reflects the
        // unlocked state consistently with the feature gates.
        IsAfFingerprint.method.addInstructions(
            0,
            """
            const/4 v0, 0x1
            return v0
            """.trimIndent(),
        )
    }
}
