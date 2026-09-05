package wy.morphe.patches.appfinder

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object FeatureLevelFingerprint : Fingerprint(
    definingClass = "Lscadica/aq/UU;",
    name = "getF",
    returnType = "I",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
)
