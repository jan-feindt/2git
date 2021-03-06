package togit.migration.targets.git.context

import org.slf4j.LoggerFactory
import togit.context.TargetContext
import togit.migration.targets.git.GitOptions
import togit.migration.targets.git.GitTarget

class GitTargetContext extends TargetContext {

    final static LOG = LoggerFactory.getLogger(this.class)

    /**
     * GitOptionsContext constructor
     */
    GitTargetContext() {
        target = new GitTarget(options:new GitOptions())
    }

    /**
     * Adds given String arguments to the Git ignore file
     * @param args the String arguments to add
     */
    void ignore(String... args) {
        target.options.ignore.addAll(args)
        LOG.debug("Added $args to git ignore.")
    }

    /**
     * Tracks given String arguments under Git LFS
     * @param args the String arguments to add
     */
    void lfs(String... args) {
        target.options.lfs.addAll(args)
        LOG.debug("Added $args to git lfs.")
    }

    /**
     * Sets the Git user
     * @param user the user name
     */
    void user(String user) {
        target.options.user = user
        LOG.debug("Set user to $user.")
    }

    /**
     * Sets the Git user email
     * @param email the user email
     */
    void email(String email) {
        target.options.email = email
        LOG.debug("Set email to $email.")
    }

    /**
     * Sets the Git path
     */
    void workspace(String path) {
        target.workspace = path
        LOG.debug("Set workspace to $path.")
    }

    /**
     * Allow long (>255 characters) path names to be handled by Git
     */
    void longPaths(boolean longPaths = true) {
        target.options.longPaths = longPaths
        LOG.debug('Set longPaths for repo.')
    }

    /**
     * Sets the URL of the origin remote
     */
    void remote(String remoteUrl) {
        target.options.remote = remoteUrl
        LOG.debug("Set remote url to $remoteUrl.")
    }
}
