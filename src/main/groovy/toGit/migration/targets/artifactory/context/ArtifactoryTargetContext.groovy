package toGit.migration.targets.artifactory.context

import org.slf4j.LoggerFactory
import toGit.context.TargetContext
import toGit.migration.targets.artifactory.ArtifactoryOptions
import toGit.migration.targets.artifactory.ArtifactoryTarget

class ArtifactoryTargetContext extends TargetContext {

    final static LOG = LoggerFactory.getLogger(this.class)

    public ArtifactoryTargetContext() {
        target = new ArtifactoryTarget(options: new ArtifactoryOptions())
    }

    void user(String user) {
        (target as ArtifactoryTarget).options.user = user
        LOG.debug("Set user to $user")
    }

    void password(String password) {
        (target as ArtifactoryTarget).options.password = password
        LOG.debug("Set password")
    }

    void url(String url) {
        (target as ArtifactoryTarget).options.url = url
        LOG.debug("Set url to $url")
    }

    void repository(String repository) {
        (target as ArtifactoryTarget).options.repository = repository
        LOG.debug("Set repository to $repository")
    }
}
