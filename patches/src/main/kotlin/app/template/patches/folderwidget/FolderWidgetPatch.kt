package wy.morphe.patches.folderwidget

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.patch.bytecodePatch
import wy.morphe.patches.shared.Constants.FOLDER_WIDGET_COMPATIBILITY

@Suppress("unused")
val folderWidgetPatch = bytecodePatch(
    name = "Pro",
    description = "Enable pro",
) {
    compatibleWith(FOLDER_WIDGET_COMPATIBILITY)

    execute {
        // Bypass native integrity: remove System.loadLibrary("cjson") from Utils.<clinit>
        NativeLibraryLoadFingerprint.let {
            val stringIndex = it.instructionMatches[0].index  // const-string "cjson"
            val loadLibIndex = it.instructionMatches[1].index  // invoke-static System.loadLibrary

            it.method.apply {
                // Remove in reverse order to preserve indices
                removeInstruction(loadLibIndex)
                removeInstruction(stringIndex)
            }
        }

        // No-op the Runnable that calls Utils.json() to prevent UnsatisfiedLinkError
        JsonCallerRunnableFingerprint.method.addInstructions(
            0,
            "return-void",
        )

        // Pro unlock: universal getter → force true for pro flag key
        PrefsGetFingerprint.method.addInstructions(
            0,
            """
            const-string v0, "play.kotlinlang.org"
            invoke-virtual {p0, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z
            move-result v0
            if-eqz v0, :skip
            const/4 v0, 0x1
            invoke-static {v0}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;
            move-result-object v0
            return-object v0
            :skip
            nop
            """.trimIndent(),
        )

        // Account flag → true
        IsAfFingerprint.method.addInstructions(
            0,
            """
            const/4 v0, 0x1
            return v0
            """.trimIndent(),
        )
    }
}
