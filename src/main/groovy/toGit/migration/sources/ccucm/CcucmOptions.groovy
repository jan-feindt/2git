package toGit.migration.sources.ccucm

import net.praqma.clearcase.ucm.view.SnapshotView
import net.praqma.clearcase.ucm.view.SnapshotView as CoolSnapshotView
import org.slf4j.LoggerFactory

class CcucmOptions {

    final static LOG = LoggerFactory.getLogger(this.class)

    String stream
    String component
    String migrationProject

    CoolSnapshotView.Components loadComponents = CoolSnapshotView.Components.MODIFIABLE
    boolean readOnlyMigrationStream

    public void setReadOnlyMigrationStream(boolean readOnlyMigrationStream) {
        this.readOnlyMigrationStream = readOnlyMigrationStream
        configCheck()
    }

    public void setLoadComponents(SnapshotView.Components loadComponents) {
        this.loadComponents = loadComponents
        configCheck()
    }

    public void configCheck(){
        if(readOnlyMigrationStream && loadComponents == SnapshotView.Components.MODIFIABLE) {
            LOG.warn("Migrating modifiable components in combination with read-only migration streams can cause unexpected behaviour")
        }
    }
}
