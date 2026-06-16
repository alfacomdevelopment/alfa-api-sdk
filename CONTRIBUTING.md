# Contributing

Thank you for considering a contribution to Alfa API SDK.

## Development requirements

- JDK 21 for building the project.
- Gradle Wrapper from this repository.
- Java 8 compatible source and target bytecode for SDK modules.

## Local build

Run the full verification before opening a pull request:

```bash
./gradlew clean build
```

For faster local feedback during SDK changes, run the affected module first:

```bash
./gradlew :api-sdk-core:build
```

## Pull requests

- Keep changes focused and reviewable.
- Preserve public API compatibility unless the change is explicitly breaking.
- Add or update tests when behavior changes.
- Do not commit generated build output, local IDE files, credentials, or environment-specific settings.
- Use generated OpenAPI models where the project already relies on OpenAPI generation.

## Dependency updates

Dependabot is configured for Gradle dependencies and GitHub Actions. Dependency update pull requests should pass the regular build workflow before merging.

## Release process

Stable library releases are manual.

1. Open the `Publish library to GitHub Packages` workflow in GitHub Actions.
2. Run it from the `main` branch.
3. Select one of `incrementPatch`, `incrementMinor`, or `incrementMajor`.

The release workflow tags the version through Axion Release and publishes artifacts to GitHub Packages.

Snapshot artifacts are published automatically from non-`main` branch pushes. Before publishing a new snapshot, the workflow removes older `SNAPSHOT` package versions for this repository so a single current snapshot line is kept.
