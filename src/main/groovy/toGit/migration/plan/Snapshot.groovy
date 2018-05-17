package toGit.migration.plan

import net.praqma.clearcase.ucm.utils.BaselineList
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper

/**
 * Represents a VCS snapshot.
 * e.g.: Commit, Baseline, State
 */
abstract class Snapshot {
    String identifier

    public Snapshot(String identifier) {
        this.identifier = identifier
    }

    @Override
    boolean equals(Object o) {
        if(!o instanceof Snapshot || o == null) {
            return false
        }
        toGit.migration.plan.Snapshot s = (toGit.migration.plan.Snapshot)o
        return this.identifier.equals(s.identifier)
    }
/**
     * Checks whether this Snapshot matches all given criteria
     * @param criteria A List of Criteria to match the Snapshot against
     * @return true if the Snapshot matches all Criteria, otherwise false
     */
    boolean matches(List<Criteria> criteria) {
        for (def crit : criteria) {
            if (!crit.appliesTo(this, null))
                return false
        }
        return true
    }

    boolean matches(List<Criteria> criteria, BaselineList sortedBaselines) {
        for (def crit : criteria) {
            if (!crit.appliesTo(this, sortedBaselines))
                return false
        }
        return true
    }
}
