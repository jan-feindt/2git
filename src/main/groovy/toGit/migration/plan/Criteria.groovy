package toGit.migration.plan

import net.praqma.clearcase.ucm.utils.BaselineList

abstract class Criteria {
    def abstract boolean appliesTo(Snapshot snapshot, BaselineList sortedBaselines)
}
