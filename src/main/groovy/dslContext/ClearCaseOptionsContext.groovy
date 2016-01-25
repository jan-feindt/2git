package dslContext

@Grab('org.slf4j:slf4j-simple:1.7.7')
import groovy.util.logging.Slf4j
import migration.ClearCaseOptions
import migration.GitOptions
import net.praqma.clearcase.ucm.view.SnapshotView

@Slf4j
class ClearCaseOptionsContext implements Context {
    ClearCaseOptions clearCaseOptions

    /**
     * GitOptionsContext constructor
     */
    public ClearCaseOptionsContext() {
        log.debug('Entering ClearCaseOptionsContext().')
        gitOptions = new GitOptions()
        log.trace('Configuring ClearCase options.')
        log.debug('Exiting ClearCaseOptionsContext().')
    }

    /**
     * Adds given String arguments to the Git ignore file
     * @param args the String arguments to add
     */
    def void components(String target) {
        log.debug('Entering components().')
        if(target.equalsIgnoreCase('all'))
            ClearCaseOptions.components = SnapshotView.Components.ALL
        else if (target.equalsIgnoreCase('modifiable'))
            ClearCaseOptions.components = SnapshotView.Components.MODIFIABLE
        else
            log.warn("Invalid ClearCase component target '$target'. Expected 'all' or 'modifiable'.")
        log.debug('Exiting components().')
    }
}