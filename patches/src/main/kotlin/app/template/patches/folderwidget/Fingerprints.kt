package wy.morphe.patches.folderwidget

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags

// Utils.<clinit> loads the "cjson" native library which performs integrity checks.
// Target the clinit to surgically remove the System.loadLibrary("cjson") call.
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

// The method that invokes the native BaseActivity.run(Z, Runnable)V.
// We'll replace the invoke-virtual call with a direct invoke-interface to Runnable.run()V.
internal object NativeRunCallerFingerprint : Fingerprint(
    definingClass = "Lcom/android/app/ap/h/BaseActivity\$initRewardAD\$1;",
    name = "invokeSuspend",
)

// Generic SharedPreferences reader: get(String key, Object default) -> Object.
// Reads from the "ba_sp" prefs. Every pro-feature gate resolves the local pro
// flag through this single method, so overriding a boolean read here to true
// unlocks the whole app. Disambiguated from the sibling setter (returns V) by
// the Object return type + the getSharedPreferences call on the "ba_sp" store.
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

// User.isAf() -> Z : the account-level "member" flag. Not obfuscated, so matched
// by defining class + name. Flipped to true so the account/Pro UI reflects the
// unlocked state consistently with the feature gates above.
internal object IsAfFingerprint : Fingerprint(
    definingClass = "Lcom/android/app/ap/h/user/User;",
    name = "isAf",
)
