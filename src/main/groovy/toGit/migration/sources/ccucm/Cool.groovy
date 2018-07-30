package toGit.migration.sources.ccucm


import net.praqma.clearcase.PVob as CoolPVob
import net.praqma.clearcase.Rebase as CoolRebase
import net.praqma.clearcase.api.Describe
import net.praqma.clearcase.ucm.entities.Baseline as CoolBaseline
import net.praqma.clearcase.ucm.entities.Component as CoolComponent
import net.praqma.clearcase.ucm.entities.Stream as CoolStream
import net.praqma.clearcase.ucm.utils.BaselineFilter
import net.praqma.clearcase.ucm.utils.BaselineList
import net.praqma.clearcase.ucm.view.SnapshotView as CoolSnapshotView
import net.praqma.clearcase.ucm.view.SnapshotView.LoadRules2
import net.praqma.clearcase.ucm.view.UpdateView
import org.slf4j.LoggerFactory

/**
 * A Cool wrapper that adds logging.
 */
class Cool {

    final static LOG = LoggerFactory.getLogger(this.class)

    /**
     * Creates a child Stream for the given Stream at the given Baseline
     * @param coolStream The Cool Stream to create a child Stream for.
     * @param coolBaseline The Cool Baseline to create the child Stream at.
     * @return The new child Cool Stream.
     */
    static CoolStream createStream(CoolStream coolStream, CoolBaseline coolBaseline, String tag, boolean readOnly) {
        LOG.debug("Creating child stream of $coolStream.fullyQualifiedName at baseline $coolBaseline.fullyQualifiedName.")
        def migrationStream = CoolStream.create(coolStream, tag, readOnly, coolBaseline)
        LOG.debug("Created child stream of $coolStream.fullyQualifiedName at baseline $coolBaseline.fullyQualifiedName.")
        return migrationStream
    }

    /**
     * Creates a View for the given Stream at the given path.
     * @param coolStream The Cool Stream to create a Cool View for.
     * @param path The path to create the Cool View at.
     * @return The new Cool View.
     */
    static CoolSnapshotView createView(CoolStream coolStream, File path, String tag) {
        LOG.debug("Creating view for $coolStream.fullyQualifiedName.")
        def coolView = CoolSnapshotView.create(coolStream, path, tag)
        LOG.debug("Created view for $coolStream.fullyQualifiedName.")
        return coolView
    }

    /**
     * Gets the Baselines for the given Component in the given Stream which respect the given name Regex and Promotion Levels.
     * @param coolComponent The Cool Component to get the baselines for.
     * @param coolStream The Cool Stream to get the Baselines in.
     * @param baselineFilter The plan for the Baselines.
     * @return A BaselineList containing all matching Baselines.
     */
    static BaselineList getBaselines(CoolComponent coolComponent, CoolStream coolStream, BaselineFilter baselineFilter) {
        LOG.debug("Retrieving Cool baselines for $coolComponent.fullyQualifiedName in $coolStream.fullyQualifiedName.")
        def baselines = new BaselineList(coolStream, coolComponent, null).addFilter(baselineFilter).apply()
        def baselineCount = baselines.size()
        LOG.debug("Retrieved $baselineCount Cool baseline(s) for $coolComponent.fullyQualifiedName in $coolStream.fullyQualifiedName.")
        return baselines
    }

    /**
     * Gives a Cool Component for the given Component name.
     * @param componentName The name of the Component.
     * @param coolPVob The Cool PVob the Component is in
     * @return The Cool Component.
     */
    static CoolComponent getComponent(String componentName, CoolPVob coolPVob) {
        LOG.debug("Retrieving Cool component $componentName.")
        def coolComponent = CoolComponent.get(componentName, coolPVob)
        LOG.debug("Retrieved Cool component $coolComponent.fullyQualifiedName.")
        return coolComponent
    }

    /**
     * Gets a list of component selectors representing all the modifiable loadComponents in the given stream
     * @param stream The fully qualified name of the stream to get the component selectors for
     * @return the modifiable component selectors as Strings
     */
    static List<String> getModifiableComponentSelectors(String stream) {
        return new Describe(CoolStream.get(stream).project.integrationStream).addModifier(new Describe.Property("mod_comps").extended(true)).execute().first().split(" ").toList()
    }

    /**
     * Gets a list of component selectors representing all the non-modifiable loadComponents in the given stream
     * @param stream The fully qualified name of the stream to get the component selectors for
     * @return the non-modifiable component selectors as Strings
     */
    static List<String> getNonModifiableComponentSelectors(String stream) {
        return new Describe(CoolStream.get(stream).project.integrationStream).addModifier(new Describe.Property("non_mod_comps").extended(true)).execute().first().split(" ").toList()
    }

    /**
     * Gives a Cool PVob for the given PVob name.
     * @param pvobName The name of the PVob
     * @return The Cool PVob.
     */
    static CoolPVob getPVob(String pvobName) {
        LOG.debug("Retrieving Cool vob $pvobName.")
        CoolPVob coolPVob = new CoolPVob(pvobName)
        coolPVob.load()
        LOG.debug("Retrieved Cool vob $coolPVob.fullyQualifiedName.")
        return coolPVob
    }

    /**
     * Gives a Cool Stream for the given Stream name.
     * @param streamName The name of the Stream.
     * @param vob The Cool PVob the Stream is in.
     * @return The Cool Stream.
     */
    static CoolStream getStream(String streamName, CoolPVob vob) {
        LOG.debug("Retrieving Cool stream $streamName.")
        def coolStream = CoolStream.get(streamName, vob)
        LOG.debug("Retrieved Cool stream $coolStream.fullyQualifiedName")
        return coolStream
    }

    /**
     * Rebases given Baseline onto the given View.
     * @param coolBaseline The Cool Baseline to rebase.
     * @param coolView The Cool View to rebase onto.
     */
    static void rebase(CoolBaseline coolBaseline, CoolSnapshotView coolView) {
        LOG.debug("Rebasing $coolBaseline.fullyQualifiedName onto $coolView.fullyQualifiedName.")
        new CoolRebase(coolView).addBaseline(coolBaseline).rebase(true)
        LOG.debug("Rebased $coolBaseline.fullyQualifiedName onto $coolView.fullyQualifiedName.")
    }

    /**
     * Updates the given View.
     * @param coolView The Cool View to update.
     */
    static void updateView(CoolSnapshotView coolView, CoolSnapshotView.Components loadComponents) {
        LOG.debug("Updating view $coolView.fullyQualifiedName.")
        def loadRules = new LoadRules2(loadComponents)
        new UpdateView(coolView).swipe().setLoadRules(loadRules).update()
        LOG.debug("Updated view $coolView.fullyQualifiedName.")
    }
}
