package toGit.migration.targets.git.actions

import org.slf4j.LoggerFactory
import toGit.migration.plan.Action

class Clear extends Action {

    final static LOG = LoggerFactory.getLogger(this.class)

    String path

    public Clear(String path) {
        this.path = path
    }

    @Override
    void act(Map<String, Object> extractionMap) {
        LOG.debug("Clearing git repository")
        new File(path).listFiles().findAll { !it.name.startsWith(".git") }.each {
            if (it.directory) it.deleteDir()
            else it.delete()
        }
        LOG.debug("Cleared git repository")
    }
}
