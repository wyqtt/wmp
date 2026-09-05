/**
 * Original code credited to Morphe:
 * https://github.com/MorpheApp/morphe-patches/blob/main/patches/src/main/kotlin/app/morphe/patches/all/misc/packagename/ChangePackageNamePatch.kt
 */

package wy.morphe.patches.lawnicons

import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.patch.stringOption
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import wy.morphe.patches.shared.Constants.LAWNICONS_COMPATIBILITY
import wy.morphe.patches.shared.Constants.LAWNICONS_PLAY_COMPATIBILITY

// Helper extension function to convert NodeList to Sequence
fun NodeList.asSequence(): Sequence<org.w3c.dom.Node> = (0 until length).asSequence().map { item(it) }

@Suppress("unused")
val lawniconsClonePatch = resourcePatch(
    name = "Clone App",
    description = "Clone Lawnicons by changing the package name. Appends \".ext\" by default.",
    default = true
) {
    compatibleWith(LAWNICONS_COMPATIBILITY, LAWNICONS_PLAY_COMPATIBILITY)

    val packageNameOption = stringOption(
        key = "packageName",
        default = "Default",
        values = mapOf("Default" to "Default"),
        title = "Package name",
        description = "The package name for the cloned app. Default appends \".ext\" to app.lawnchair.lawnicons",
        required = true,
    ) {
        it == "Default" || it!!.matches(Regex("^[a-z]\\w*(\\.[a-z]\\w*)+\$"))
    }

    val updatePermissions by booleanOption(
        key = "updatePermissions",
        default = false,
        title = "Update permissions",
        description = "Update compatibility receiver permissions. " +
                "Enabling this can fix installation errors, but this can also break features in certain apps.",
    )

    val updateProviders by booleanOption(
        key = "updateProviders",
        default = false,
        title = "Update providers",
        description = "Update provider names declared by the app. " +
                "Enabling this can fix installation errors, but this can also break features in certain apps.",
    )

    finalize {
        document("AndroidManifest.xml").use { document ->
            val manifest = document.documentElement as Element
            val packageName = manifest.getAttribute("package")

            val replacementPackageName = packageNameOption.value
            val newPackageName = if (replacementPackageName != packageNameOption.default) {
                replacementPackageName!!
            } else {
                "$packageName.ext"
            }

            manifest.setAttribute("package", newPackageName)
            println("Lawnicons: Changed package name from $packageName to $newPackageName")

            if (updatePermissions == true) {
                val permissions = manifest.getElementsByTagName("permission").asSequence()
                val usesPermissions = manifest.getElementsByTagName("uses-permission").asSequence()

                val receiverNotExported = "DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION"

                (permissions + usesPermissions)
                    .map { it as Element }
                    .filter { it.getAttribute("android:name") == "$packageName.$receiverNotExported" }
                    .forEach { it.setAttribute("android:name", "$newPackageName.$receiverNotExported") }
            }

            if (updateProviders == true) {
                val providers = manifest.getElementsByTagName("provider")

                for (i in 0 until providers.length) {
                    val provider = providers.item(i) as Element

                    val authorities = provider.getAttribute("android:authorities")
                    if (!authorities.startsWith("$packageName.")) continue

                    provider.setAttribute("android:authorities", authorities.replace(packageName, newPackageName))
                }
            }
        }
    }
}