package toGit.migration.targets.git.actions

import org.slf4j.LoggerFactory
import toGit.migration.plan.Action

class FillEmptyDirs extends Action {

    final static LOG = LoggerFactory.getLogger(this.class)

    String path

    public FillEmptyDirs(String path) {
        this.path = path
    }

    @Override
    void act(Map<String, Object> extractionMap) {
        LOG.info("Sprinkling dummy files in empty directories")
        sprinkleDummies(new File(path))
        LOG.info("Finished sprinkling dummy files")
    }

    def sprinkleDummies(File directory) {
        def subDirs = directory.listFiles().findAll { it.isDirectory() && !it.name.equals(".git") }
        subDirs.each { subDir ->
            def contents = subDir.listFiles()
            if(contents.any()) {
                sprinkleDummies(subDir)
            } else {
                new File(subDir, ".dummy").createNewFile()
                LOG.info("Dropped .dummy file in $subDir")
            }
        }
    }
}
