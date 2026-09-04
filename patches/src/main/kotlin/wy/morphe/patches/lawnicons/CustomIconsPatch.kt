package wy.morphe.patches.lawnicons

import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.patch.stringOption
import wy.morphe.patches.shared.Constants.LAWNICONS_COMPATIBILITY
import wy.morphe.patches.shared.Constants.LAWNICONS_PLAY_COMPATIBILITY

@Suppress("unused")
val lawniconsExtIconsPatch = resourcePatch(
    name = "Extended Icons",
    description = "Add exisiting icons to apps without them.",
    default = true
) {
    compatibleWith(LAWNICONS_COMPATIBILITY, LAWNICONS_PLAY_COMPATIBILITY)

    // Users set this when patching - format: component|drawable|name (one per line)
    val extIcons by stringOption(
        key = "ext_icons",
        default = "",
        title = "Ext Icon Entries",
        description = "Add exisiting icons to apps without them (one per line or semicolon-separated).\n\n" +
                "Format: component|drawable|name\n\n" +
                "Examples:\n" +
                "com.example.app/com.example.MainActivity|my_icon|My App\n" +
                "com.test.app/.LauncherActivity|test_icon|Test App\n\n" +
                "The component format is: package/activity (same as in appfilter.xml)\n" +
                "The drawable must match an existing icon name in Lawnicons\n" +
                "The name is the display label for the icon mapping"
    )

    execute {
        // Exit early if no custom icons defined
        if (extIcons?.isBlank() != false) {
            println("Lawnicons: No new apps defined, skipping")
            return@execute
        }

        document("assets/appfilter.xml").use { doc ->
            val root = doc.documentElement

            // Parse entries - support both newline and semicolon separators
            val entriesText = extIcons?.trim() ?: return@use
            val entries = entriesText
                .split("\n", ";")
                .map { it.trim() }
                .filter { it.isNotBlank() }

            println("Lawnicons: Processing ${entries.size} new app icon entry(ies)")

            var added = 0
            for (entry in entries) {
                val parts = entry.split("|")
                if (parts.size != 3) {
                    println("Lawnicons: Skipping malformed entry (expected 3 parts): $entry")
                    continue
                }

                val (component, drawable, name) = parts.map { it.trim() }

                // Validate component format
                if (!component.contains("/")) {
                    println("Lawnicons: Skipping invalid component (missing '/'): $component")
                    continue
                }

                // Create <item> element matching appfilter.xml format
                val item = doc.createElement("item")
                item.setAttribute("component", "ComponentInfo{$component}")
                item.setAttribute("drawable", drawable)
                item.setAttribute("name", name)

                // Append to end of resources
                root.appendChild(item)
                added++
            }

            println("Lawnicons: Successfully added $added new app icon mapping(s) to appfilter.xml")
        }
    }
}
