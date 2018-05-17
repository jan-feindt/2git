package toGit.migration.sources.ccucm.criteria

import net.praqma.clearcase.ucm.utils.BaselineList
import org.slf4j.LoggerFactory
import toGit.migration.plan.Criteria
import toGit.migration.plan.Snapshot
import toGit.migration.sources.ccucm.Baseline

/**
 * Created by mads on 5/14/18.
 */
class NewestOnly extends Criteria {

    final static log = LoggerFactory.getLogger(this.class)

    @Override
    boolean appliesTo(Snapshot snapshot, BaselineList sortedBaselines) {
        def latest = new Baseline(sortedBaselines.get(0))
        def result = snapshot.equals(latest)
        if(!result) {
            log.debug("Discarding baseline '$snapshot' because it isn't the newest baseline")
        }
        return result
    }
}
