package wy.morphe.patches.folderwidget

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags

internal object NativeLibraryLoadFingerprint : Fingerprint(
    definingClass = "Lcom/android/app/ap/h/Utils;",
    name = "<clinit>",
    filters = listOf(
        string("cjson"),
        methodCall(
            definingClass = "Ljava/lang/System;",
            name = "loadLibrary",
        ),
    ),
)

internal object JsonCallerRunnableFingerprint : Fingerprint(
    definingClass = "Lɨ/Ϳ;",
    name = "run",
)

internal object PrefsGetFingerprint : Fingerprint(
    returnType = "Ljava/lang/Object;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    parameters = listOf("Ljava/lang/String;", "Ljava/lang/Object;"),
    filters = listOf(
        string("ba_sp"),
        methodCall(
            definingClass = "Landroid/content/Context;",
            name = "getSharedPreferences",
        ),
    ),
)

internal object IsAfFingerprint : Fingerprint(
    definingClass = "Lcom/android/app/ap/h/user/User;",
    name = "isAf",
)
