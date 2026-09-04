module.exports = {
  branches: [
    "main",
    {
      "name": "dev",
      "prerelease": true
    }
  ],
  plugins: [
    [
        "@semantic-release/commit-analyzer", {
        "releaseRules": [
            { "type": "fix", "release": "patch" },
            { "type": "feat", "release": "minor" },
            { "type": "bump", "release": "minor" },
            { "type": "perf", "release": "patch" },
            { "type": "build", "scope": "Needs bump", "release": "patch" }
        ]
        }
    ],
    [
        "@semantic-release/release-notes-generator", {
        "preset": "conventionalcommits",
        "presetConfig": {
            "types": [
                { "type": "fix", "section": "🐛 Bug Fixes", "hidden": false },
                { "type": "feat", "section": "✨ New Features", "hidden": false },
                { "type": "bump", "section": "🚀 Updated App Support", "hidden": false },
                { "type": "perf", "section": "🔧 Improvements",  "hidden": false },
                { "type": "build", "hidden": true }
            ]
        },
        "writerOpts": {
          finalizeContext: (context) => {
            context.commitGroups.forEach((group) => {
              const uniqueCommits = new Map();
              group.commits.forEach((commit) => {
                const key = commit.subject;
                if (!uniqueCommits.has(key)) {
                  uniqueCommits.set(key, {
                    ...commit,
                    allLinks: [`[${commit.shortHash}](https://github.com{context.owner}/${context.repository}/commit/${commit.hash})`]
                  });
                } else {
                  const existing = uniqueCommits.get(key);
                  existing.allLinks.push(`[${commit.shortHash}](https://github.com{context.owner}/${context.repository}/commit/${commit.hash})`);
                }
              });
              group.commits = Array.from(uniqueCommits.values());
            });
            return context;
          },
          commitPartial: '* {{subject}} ({{#each allLinks}}{{{this}}}{{#unless @last}}, {{/unless}}{{/each}})\n'
        }
      }
    ],
    [
      "@MorpheApp/changelog", {
        "releaseJson": {
          "path": "patches-bundle.json",
          "downloadUrlTemplate": "https://github.com/${owner}/${repo}/releases/download/v${version}/patches-${version}.mpp",
          "signatureUrlTemplate": ""
        }
      }
    ],
    "gradle-semantic-release-plugin",
    [
      "@semantic-release/exec",
      {
        "prepareCmd": "./gradlew generatePatchesList \
          && jq '.version=\"${nextRelease.version}\"' patches-list.json > patches-list.json.tmp && mv patches-list.json.tmp patches-list.json \
          && python3 .github/scripts/generate_patches_readme.py $GITHUB_REPOSITORY $GITHUB_REF_NAME patches-list.json README.md"
      }
    ],
    [
      "@semantic-release/git",
      {
        "assets": [
          "CHANGELOG.md",
          "gradle.properties",
          "patches-bundle.json",
          "patches-list.json",
          "README.md"
        ],
        "message": "chore: Release v${nextRelease.version} [skip ci]\n\n${nextRelease.notes}"
      }
    ],
    [
      "@semantic-release/github",
      {
        "assets": [
          {
            "path": "patches/build/libs/patches-!(*sources*|*javadoc*).mpp?(.asc)"
          }
        ],
        "successComment": false
      }
    ],
    [
      "@cleyrop-org/semantic-release-backmerge",
      {
        "backmergeBranches": [{"from": "main", "to": "dev"}],
        "clearWorkspace": true
      }
    ]
  ]
}
