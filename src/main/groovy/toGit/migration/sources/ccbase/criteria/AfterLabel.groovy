package toGit.migration.sources.ccbase.criteria

import net.praqma.clearcase.ucm.utils.BaselineList
import toGit.migration.plan.Criteria
import toGit.migration.plan.Snapshot

class AfterLabel extends Criteria{

    String label

    AfterLabel(String label) {
        this.label = label;
    }

    @Override
    boolean appliesTo(Snapshot snapshot, BaselineList list) {
        return label.compareTo(snapshot.identifier) < 0
    }
}
