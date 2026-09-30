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
  const packagePrefix = "com.alfa.api.sdk.api-sdk-";
  const repoPackages = packages.filter(
    (pkg) =>
      pkg.repository?.full_name === repoFull ||
      pkg.name.startsWith(packagePrefix)
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

    if (snapshotVersions.length === versions.length) {
      core.info(`Package ${pkg.name}: all versions are SNAPSHOT; deleting package before republishing.`);
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
      continue;
    }

    core.info(`Package ${pkg.name}: deleting ${snapshotVersions.length} SNAPSHOT versions.`);

    for (const version of snapshotVersions) {
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
    }
  }
};
