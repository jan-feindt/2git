package toGit.migration.sources.ccbase

import org.slf4j.LoggerFactory
import toGit.context.Context
import toGit.migration.plan.Criteria
import toGit.migration.plan.Snapshot
import toGit.migration.sources.MigrationSource
import toGit.migration.sources.ccbase.context.ClearCaseCriteriaContext
import toGit.migration.sources.ccbase.context.ClearCaseExtractionsContext

class ClearCaseSource implements MigrationSource {

    final static LOG = LoggerFactory.getLogger(this.class)

    String configSpec
    String labelVob
    String labelFile
    String viewTag
    List<String> vobPaths = []

    @Override
    List<Snapshot> getSnapshots(List<Criteria> initialFilter) {
        if (!labelFile && !labelVob) {
            LOG.error("Missing label source")
            LOG.error("Neither labelFile or labelVob were specified")
            System.exit(1)
        }

        if(labelFile) {
            def asFile = new File(labelFile)
            if(!asFile.exists() || asFile.isDirectory()) {
                LOG.error("Couldn't find labelFile: ${asFile.absolutePath}")
                System.exit(1)
            }
            def labels = asFile.text.readLines().collect { it.trim() }.grep { it != null && it != "" }
            return labels.collect { new ClearCaseSnapshot(it) }
        }

        if(labelVob) {
            LOG.info("Retrieving labels from vob ${labelVob}")
            def labels = runCommand(["cleartool", "lstype", "-kind", "lbtype", "-short", "-invob", labelVob], true, false).split("\n")
            return labels.collect { new ClearCaseSnapshot(it) }
        }
    }

    @Override
    void checkout(Snapshot snapshot) {
        updateConfigSpec(snapshot.identifier)
        updateViewConfigSpec()
        LOG.info('Done preparing snapshot ' + snapshot.identifier)
    }

    @Override
    void prepare() {
        LOG.info("Creating snapshot view '${viewTag}'")
        runCommand(["cleartool", "mkview", "-snapshot", "-tag", viewTag, "-stgloc", "-auto", workspace], false, true)
    }

    @Override
    void cleanup() {
        runCommand(['cleartool', 'rmview', workspace], false, true)
    }

    @Override
    Context withCriteria(Context criteriaContext) {
        return criteriaContext as ClearCaseCriteriaContext
    }

    @Override
    Context withExtractions(Context extractionsContext) {
        return extractionsContext as ClearCaseExtractionsContext
    }

    /**
     * Updates the config spec to load the given label
     * @param label The label to update the spec with
     */
    private void updateConfigSpec(String label) {
        def csText = csTemplateAsFile().text.replace('$label', label)
        vobPaths.each {
            csText += ("load /$it\n")
        }
        csAsFile().text = csText
    }

    /**
     * Sets the view's config spec to
     */
    private void updateViewConfigSpec() {
        runCommand(["cleartool", "setcs", "-force", csAsFile().absolutePath], true, true)
    }

    /**
     * Runs given String list as a command, logging and returning the output
     * @param command String list representing the command to execute
     * @return The process output as a String
     */
    private String runCommand(List<String> command, boolean runInWorkspace, boolean printOutput) {
        LOG.info("Executing: ${command.join(" ")}")
        def builder = new ProcessBuilder(command).redirectErrorStream(true)
        builder = runInWorkspace ? builder.directory(new File(workspace)) : builder
        def process = builder.start()
        def output = ""
        process.in.eachLine {
            if (printOutput) {
                LOG.info(it)
            }
            output += "${it}\n"
        }
        LOG.info("Executed: ${process.waitFor()}")
        return output
    }

    private File csTemplateAsFile() {
        def configSpecText = new File(configSpec);
        if (!configSpecText.exists() || configSpecText.isDirectory()) {
            throw new Exception("Could not find config spec template at ${configSpecText.absolutePath}")
        }
        return configSpecText
    }

    private File csAsFile() {
        def configSpecText = csTemplateAsFile()
        return new File(configSpecText.parentFile, 'configspec.tmp')
    }
}
