package wy.morphe.patches.appfinder

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object PremiumCheckFingerprint : Fingerprint(
    definingClass = "Lscadica/aq/UU;",
    name = "getB",
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
)

internal object PremiumSetFingerprint : Fingerprint(
    definingClass = "Lscadica/aq/UU;",
    name = "setB",
    returnType = "V",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf("Z"),
)
