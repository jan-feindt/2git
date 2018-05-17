package toGit.migration.sources.ccucm.criteria

import net.praqma.clearcase.ucm.utils.BaselineList
import org.slf4j.LoggerFactory
import toGit.migration.plan.Criteria
import toGit.migration.plan.Snapshot

class BaselineNames extends Criteria {

    final static log = LoggerFactory.getLogger(this.class)

    List<String> baselines

    BaselineNames(String... baselines) {
        this.baselines = baselines
    }

    BaselineNames(List<String> baselines) {
        this.baselines = baselines
    }

    @Override
    boolean appliesTo(Snapshot snapshot, BaselineList list) {
        //def baseline = ((Baseline) snapshot).source
        // String.equals(GString) fails, hence the extra toString
        //def baselineName = "${baseline.shortname}@${baseline.PVob.name}".toString()
        log.debug("Testing '$snapshot' against baseline list $baselines")
        def result = baselines.contains(snapshot.toString())
        log.debug("Result: " + (result ? "MATCH" : "no match"))
        return result
    }
}