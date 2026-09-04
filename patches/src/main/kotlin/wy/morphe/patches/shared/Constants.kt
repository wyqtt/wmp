package wy.morphe.patches.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.SupportedAbi

object Constants {
    
    val KWGT_COMPATIBILITY = Compatibility(
        name = "KWGT",
        packageName = "org.kustom.widget",
        appIconColor = 0x3476B6,
        targets = listOf(
            AppTarget(version = "3.82b619510aosp", versionCode = 382619510)
        )
    )

    val KLWP_COMPATIBILITY = Compatibility(
        name = "KLWP",
        packageName = "org.kustom.wallpaper",
        appIconColor = 0xBF2F4A,
        targets = listOf(
            AppTarget(version = "3.82b621115aosp", versionCode = 382621115)
        )
    )

    val FOLDER_WIDGET_COMPATIBILITY = Compatibility(
        name = "Folder Widget",
        packageName = "pub.hanks.appfolderwidget",
        appIconColor = 0x634FAC,
        targets = listOf(
            AppTarget(version = "11.0.2", versionCode = 11020)
        )
    )

    val APP_FINDER_COMPATIBILITY = Compatibility(
        name = "App Finder",
        packageName = "scadica.aq",
        appIconColor = 0x242424,
        targets = listOf(
            AppTarget(
                version = "1.5.1", 
                versionCodes = mapOf(
                    SupportedAbi.ARM64_V8A to 168,   // Play Store
                    SupportedAbi.ARMEABI_V7A to 169  // App Site
                )
            )
        )
    )

}
