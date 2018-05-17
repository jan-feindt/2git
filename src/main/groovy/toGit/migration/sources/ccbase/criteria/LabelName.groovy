package toGit.migration.sources.ccbase.criteria

import net.praqma.clearcase.ucm.utils.BaselineList
import toGit.migration.plan.Criteria
import toGit.migration.plan.Snapshot

class LabelName extends Criteria {

    String regex

    LabelName(String regex) {
        this.regex = regex;
    }

    @Override
    boolean appliesTo(Snapshot snapshot, BaselineList list) {
        return snapshot.identifier =~ regex
    }
}
