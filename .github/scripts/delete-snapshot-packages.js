module.exports = async ({ github, context, core }) => {
  const owner = context.repo.owner;
  const repoFull = `${context.repo.owner}/${context.repo.repo}`;

  const { data: ownerData } = await github.rest.users.getByUsername({ username: owner });
  const isOrg = ownerData.type === "Organization";

  const packageListParams = isOrg
    ? { org: owner, package_type: "maven" }
    : { username: owner, package_type: "maven" };
  const packages = await github.paginate(
    isOrg
      ? github.rest.packages.listPackagesForOrganization
      : github.rest.packages.listPackagesForUser,
    { per_page: 100, ...packageListParams }
  );
  const repoPackages = packages.filter(
    (pkg) => pkg.repository && pkg.repository.full_name === repoFull
  );

  core.info(`Found ${repoPackages.length} Maven packages for ${repoFull}.`);

  for (const pkg of repoPackages) {
    const versionListParams = isOrg
      ? { org: owner, package_type: "maven", package_name: pkg.name }
      : { username: owner, package_type: "maven", package_name: pkg.name };
    const versions = await github.paginate(
      isOrg
        ? github.rest.packages.getAllPackageVersionsForPackageOwnedByOrg
        : github.rest.packages.getAllPackageVersionsForPackageOwnedByUser,
      { per_page: 100, ...versionListParams }
    );
    const snapshotVersions = versions.filter(
      (version) => version.name && version.name.includes("SNAPSHOT")
    );

    if (snapshotVersions.length === 0) {
      core.info(`Package ${pkg.name}: no SNAPSHOT versions to delete.`);
      continue;
    }

    const allSnapshots = snapshotVersions.length === versions.length;
    if (allSnapshots) {
      core.warning(`Package ${pkg.name}: all versions are SNAPSHOT; deleting package.`);
      try {
        if (isOrg) {
          await github.rest.packages.deletePackageForOrg({
            org: owner,
            package_type: "maven",
            package_name: pkg.name,
          });
        } else {
          await github.rest.packages.deletePackageForUser({
            username: owner,
            package_type: "maven",
            package_name: pkg.name,
          });
        }
      } catch (error) {
        core.warning(
          `Package ${pkg.name}: failed to delete package after removing versions. ${error.message}`
        );
      }
      continue;
    }

    core.info(`Package ${pkg.name}: deleting ${snapshotVersions.length} SNAPSHOT versions.`);

    for (const version of snapshotVersions) {
      try {
        if (isOrg) {
          await github.rest.packages.deletePackageVersionForOrg({
            org: owner,
            package_type: "maven",
            package_name: pkg.name,
            package_version_id: version.id,
          });
        } else {
          await github.rest.packages.deletePackageVersionForUser({
            username: owner,
            package_type: "maven",
            package_name: pkg.name,
            package_version_id: version.id,
          });
        }
      } catch (error) {
        core.warning(
          `Package ${pkg.name}: failed to delete version ${version.id} (${version.name}). ${error.message}`
        );
      }
    }
  }
};
