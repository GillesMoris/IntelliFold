<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# IntelliFold Changelog

## [Unreleased]
## [0.1.2]
### Added
- Added support for platform version 2021.2.
### Changed
- Adjacent and overlapping fold regions are now merged into a single region.
- Fold regions are now non-expandable.
### Fixed
- Fixed an issue where consecutive line comments would not be folded correctly.
## [0.1.1]
### Added
- Added plugin icon for light and dark themes.
## [0.1.0]
### Added
- Initial scaffold created from [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template).
- Added "Enable/Disable" folding button.
- Added basic regex based expression folding for Java and JS/TS.
- Added project-scoped settings menu with configurable regex list.
- Added basic comment folding for Java and JS/TS.
- Added checkbox to settings menu to enable/disable comment folding.
