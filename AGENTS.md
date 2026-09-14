# OpenWPS — AGENTS.md

## 1. Project Identity

OpenWPS is a modern, AI-native, local-first office productivity application designed to become a serious alternative to WPS Office and Microsoft Office.

OpenWPS is Android-first and tablet-first.

Core workspaces:
- Home / File Manager
- Document Editor
- Spreadsheet
- Presentation
- PDF
- Images / Assets
- AI Assistant
- AI Agent
- Settings

Supported formats:
- PDF
- DOC
- DOCX
- XLS
- XLSX
- PPT
- PPTX
- CSV
- TXT

The long-term goal is to own the OpenWPS internal document, spreadsheet, presentation, and AI architecture instead of permanently depending on third-party office libraries.

---

## 2. Core Engineering Philosophy

Follow these principles in every change:

1. Local-first
2. Modular
3. Offline-capable for non-AI operations
4. Provider-agnostic AI
5. Internal OpenWPS models
6. Replaceable third-party dependencies
7. Secure by default
8. Testable architecture
9. Tablet-first UX
10. Performance-conscious
11. Accessibility-conscious
12. Small incremental changes
13. Never break unrelated modules

Do not take shortcuts that create unnecessary architectural debt.

---

## 46. Definition of Done

A feature is not complete merely because the code compiles.

A feature is considered done when:

- Architecture is respected.
- Code is implemented.
- Tests exist where appropriate.
- Relevant tests pass.
- Build succeeds when required.
- Errors are handled.
- Security is respected.
- Performance is reasonable.
- UI follows the OpenWPS design system.
- Accessibility is considered.
- No unrelated functionality is broken.
- The implementation is documented where necessary.

---

## 47. Final Rule

Build OpenWPS as a real software product, not as a collection of quick demos.

Prefer clean abstractions over shortcuts.

Prefer OpenWPS-owned models over vendor-specific models.

Prefer local deterministic processing over unnecessary AI calls.

Prefer replaceable adapters over permanent dependencies.

Prefer small verified changes over massive untested rewrites.

The coding agent must preserve the long-term OpenWPS architecture while implementing the human developer's requested features.
