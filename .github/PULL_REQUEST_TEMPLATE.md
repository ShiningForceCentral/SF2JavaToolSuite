## 🎯 Purpose of This PR

Briefly describe the goal of this PR. PRs should be single-purpose, limited to a single tool, single feature, or single (or related) bugfix.

**PRs should not target master**, they should be targeted at a branch relevant to the tool(s) affected, or a new feature branch.

Please also include relevant motivation and context.

## 🔗 Related Discussion or Issue

Reference any relevant GitHub Discussions or Issues:
- Discussion(s): #[#discussion-number]
- Issue(s): #[issue-number]

## 🛠️ Affected tools

List the tool(s) that this change relates to as well as any dependent tools that may be affected by this change:
- MapEditor
- May affect BattleEditor

## 🧩 Summary of Changes

List the key changes made in this PR. For example:
1. Created visualisation to see battle map boundaries and AI regions
2. Bugfix to show correct MapSprites in BattleEditor
3. Updated help info for BattleSpriteAnimator

## 🔄 Coordination Notes

Explain any dependencies or coordination needed between the [SF2DISASM](https://github.com/ShiningForceCentral/SF2DISASM) and [SF2JavaToolSuite](https://github.com/ShiningForceCentral/SF2JavaToolSuite) projects:
- Requires the ability to parse data files presented in the ASM format to be implemented
- Changes to enums need to be accounted for
- Default file paths need to be updated

## 🧪 How Has This Been Tested?

Please confirm that the following standard tests have been performed. Then, list any additional tests that you ran to verify your changes and provide instructions so we can reproduce them. 
- [ ] Affected tool(s) is able to import/export data in all formats (e.g. binary, ASM, and/or png/gif image)
    - [ ] Affected dependent tools are also able to import/export data in all formats  (e.g. BattleSpriteAnimator, if BattleSpriteManager was modified)
- [ ] Importing and immediately exporting data files (.bin or .ASM) results in identical data
- [ ] [Where applicable] Exporting and immediately re-importing image files (.png or .gif) imports without any loss of data or data corruption
- [ ] Run SF2DISASM build.bat and confirm that it produces a bit-perfect replica of original rom
- [ ] [Where applicable] Run SF2DISASM buildstandard.bat or buildexpanded.bat and confirm that build succeeds

## ✅ Checklist

- [ ] All tests were performed with an unmodified [SF2DISASM](https://github.com/ShiningForceCentral/SF2DISASM)
- [ ] Any user interactions have appropriate undo/redo functionality (ActionManager in the CoreLibrary)
- [ ] I have performed a self-review of my own code
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] I have made corresponding changes to the documentation
- [ ] My changes generate no new warnings

## 📚 Additional Notes

Any other context, screenshots, or edge cases reviewers should be aware of?
