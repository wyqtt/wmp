package wy.morphe.patches.folderwidget

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod.Companion.toMutable
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c
import wy.morphe.patches.shared.Constants.FOLDER_WIDGET_COMPATIBILITY

@Suppress("unused")
val folderWidgetPatch = bytecodePatch(
    name = "Pro",
    description = "Enable pro",
) {
    compatibleWith(FOLDER_WIDGET_COMPATIBILITY)

    execute {
        // Bypass native integrity: remove System.loadLibrary("cjson") from Utils.<clinit>
        NativeLibraryLoadFingerprint.method.apply {
            val instructions = implementation!!.instructions.toList()

            // Find const-string "cjson" followed by invoke-static System.loadLibrary
            var loadLibraryIndex = -1
            for (i in 0 until instructions.size - 1) {
                val insn = instructions[i]
                val nextInsn = instructions[i + 1]

                if (insn is Instruction21c &&
                    insn.opcode.name == "CONST_STRING" &&
                    insn.reference.toString() == "cjson" &&
                    nextInsn.opcode.name.startsWith("INVOKE_STATIC")) {
                    loadLibraryIndex = i
                    break
                }
            }

            if (loadLibraryIndex >= 0) {
                // Remove both instructions: const-string + invoke-static
                removeInstruction(loadLibraryIndex + 1)  // Remove invoke-static first
                removeInstruction(loadLibraryIndex)      // Then const-string
            }
        }

        // Replace BaseActivity.run() call with direct Runnable.run()
        NativeRunCallerFingerprint.method.apply {
            val instructions = implementation!!.instructions.toList()

            // Find invoke-virtual BaseActivity.run(ZLjava/lang/Runnable;)V
            var callIndex = -1
            for (i in instructions.indices) {
                val insn = instructions[i]
                if (insn is Instruction35c &&
                    insn.opcode.name.startsWith("INVOKE_VIRTUAL") &&
                    insn.reference.toString().contains("BaseActivity;->run(ZLjava/lang/Runnable;)V")) {
                    callIndex = i
                    break
                }
            }

            if (callIndex >= 0) {
                val origInsn = instructions[callIndex] as Instruction35c
                // Original: invoke-virtual {p1, v1, v0}, BaseActivity.run(ZLjava/lang/Runnable;)V
                // Replace with: invoke-interface {v0}, Ljava/lang/Runnable;->run()V
                // v0 is registerD (the Runnable parameter)
                replaceInstruction(
                    callIndex,
                    "invoke-interface {v${origInsn.registerD}}, Ljava/lang/Runnable;->run()V"
                )
            }
        }

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
