/**
 * Original code credited to Morphe:
 * https://github.com/MorpheApp/morphe-patches/blob/main/patches/src/main/kotlin/app/morphe/patches/all/misc/packagename/ChangePackageNamePatch.kt
 */

package wy.morphe.patches.lawnicons

import app.morphe.patcher.patch.Option
import app.morphe.patcher.patch.OptionException
import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.patch.stringOption
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.util.logging.Logger

lateinit var packageNameOption: Option<String>

// Helper extension function to convert NodeList to Sequence
fun NodeList.asSequence(): Sequence<org.w3c.dom.Node> = (0 until length).asSequence().map { item(it) }

/**
 * Set the package name to use.
 * If this is called multiple times, the first call will set the package name.
 *
 * @param fallbackPackageName The package name to use if the user has not already specified a package name.
 * @return The package name that was set.
 * @throws OptionException.ValueValidationException If the package name is invalid.
 */
fun setOrGetFallbackPackageName(fallbackPackageName: String): String {
    val packageName = packageNameOption.value!!

    return if (packageName == packageNameOption.default) {
        fallbackPackageName.also { packageNameOption.value = it }
    } else {
        packageName
    }
}

val changePackageNamePatch = resourcePatch(
    name = "Clone App",
    description = "Appends \".ext\" to the package name by default. " +
            "Changing the package name of the app can lead to unexpected issues.",
    default = false
) {
    packageNameOption = stringOption(
        key = "packageName",
        default = "Default",
        values = mapOf("Default" to "Default"),
        title = "Package name",
        description = "The name of the package to rename the app to.",
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
        /**
         * Apps that are confirmed to not work correctly with this patch.
         * This is not an exhaustive list, and is only the apps with
         * Morphe specific patches and are confirmed incompatible with this patch.
         */
        val incompatibleAppPackages = setOf<String>()

        document("AndroidManifest.xml").use { document ->
            val manifest = document.documentElement as Element
            val packageName = manifest.getAttribute("package")

            if (incompatibleAppPackages.contains(packageName)) {
                return@finalize Logger.getLogger(this::class.java.name).severe(
                    "'$packageName' does not work correctly with \"Change package name\"",
                )
            }

            val replacementPackageName = packageNameOption.value
            val newPackageName = if (replacementPackageName != packageNameOption.default) {
                replacementPackageName!!
            } else {
                "$packageName.ext"
            }

            manifest.setAttribute("package", newPackageName)

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