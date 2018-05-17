package toGit.migration.sources.ccucm

import net.praqma.clearcase.ucm.utils.BaselineFilter
import net.praqma.clearcase.ucm.utils.BaselineList
import toGit.migration.plan.Criteria
import toGit.migration.sources.ccucm.criteria.NewestOnly

/**
 * BaselineFilter that aggregates passed in Criteria
 */
class AggregatedBaselineFilter extends BaselineFilter {

    List<Criteria> criteria // criteria to plan BaselineLists with

    /**
     * AggregatedBaselineFilter constructor
     * @param criteria criteria to plan BaselineLists with
     */
    public AggregatedBaselineFilter(List<Criteria> criteria) {
        this.criteria = criteria
    }

    /**
     * {@inheritDoc}
     */
    @Override
    int filter(BaselineList baselines) {
        int removed = 0
        //Logic only used if the NewestOnly() filter is used
        def sortedBaselines = null
        if(hasOnlyNewestFilter()) {
            def copy = new ArrayList<Baseline>(baselines)
            sortedBaselines = new BaselineList(copy)
            Collections.sort(sortedBaselines, new BaselineList.DescendingDateSort())
        }
        def baselineIterator = baselines.iterator()
        while (baselineIterator.hasNext()) {
            def snapshot = new Baseline(baselineIterator.next())
            if (!snapshot.matches(criteria, sortedBaselines)) {
                baselineIterator.remove()
                removed++
            }
        }

        return removed
    }

    /**
     * {@inheritDoc}
     */
    @Override
    String getName() {
        return "AggregatedBaselineFilter"
    }

    boolean hasOnlyNewestFilter() {
        if(criteria) {
            for(Criteria c : criteria) {
                if(c instanceof NewestOnly) {
                    return true
                }
            }
        }
        return false
    }
}
