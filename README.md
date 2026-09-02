# 👋🧩 Morphe Patches template

![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/wyqtt/wmp/release.yml)
![GitHub License](https://img.shields.io/github/license/wyqtt/wmp)

## ❓ About

Messing around with patching apps with Morphe. I don't know much and I'm just experimenting, it's not great.

> [!WARNING]
> Some patches created with AI assistance.

### How to use these patches

Click here to add these patches to Morphe: https://morphe.software/add-source?github=wyqtt/wmp

## 🩹 Patches list

<!-- PATCHES_START EXPANDED -->
[![GitHub Release](https://img.shields.io/github/v/tag/wyqtt/wmp)](https://github.com/wyqtt/wmp/releases/tag/v1.0.0-dev.10)&nbsp;&nbsp;•&nbsp;&nbsp;`dev`&nbsp;&nbsp;•&nbsp;&nbsp;4 patches total
<details open>
<summary>&lt;/&gt; App Finder&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

| 📦&nbsp;Package | ⚙️&nbsp;Version | 🩹&nbsp;Patch | 📜&nbsp;Description |
|---|---|---|---|
| scadica.aq | `1.5.1` | [Pro](#pro) | Enable pro. |

</details>

<details open>
<summary>&lt;/&gt; Folder Widget&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

| 📦&nbsp;Package | ⚙️&nbsp;Version | 🩹&nbsp;Patch | 📜&nbsp;Description |
|---|---|---|---|
| pub.hanks.appfolderwidget | `11.0.2` | [Pro](#pro) | Enable pro |

</details>

<details open>
<summary>&lt;/&gt; KLWP&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

| 📦&nbsp;Package | ⚙️&nbsp;Version | 🩹&nbsp;Patch | 📜&nbsp;Description |
|---|---|---|---|
| org.kustom.wallpaper | `3.82b621115aosp` | [Pro](#pro) | Enable pro |

</details>

<details open>
<summary>&lt;/&gt; KWGT&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

| 📦&nbsp;Package | ⚙️&nbsp;Version | 🩹&nbsp;Patch | 📜&nbsp;Description |
|---|---|---|---|
| org.kustom.widget | `3.82b619510aosp` | [Pro](#pro) | Enable pro |

</details>

<!-- PATCHES_END -->

### 🛠️ Building locally

- Run `./gradlew buildAndroid`
- The built patches .mpp file is found in `patches/build/libs/patches-*.mpp`
- Patch the mpp file using [Morphe-Desktop](https://github.com/MorpheApp/morphe-desktop)
  like any other patch bundle.

See the [Morphe documentation](https://github.com/MorpheApp/morphe-documentation) for more information.

## 📜 License

wy's Patches are licensed under the [GNU General Public License v3.0](LICENSE)
