package toGit.context

import org.slf4j.LoggerFactory
import toGit.migration.MigrationManager
import toGit.migration.plan.Filter

class FilterContext implements Context {

    final static LOG = LoggerFactory.getLogger(this.class)

    Filter filter = new Filter()

    /**
     * Adds child {@link Filter}s to the current {@link Filter}.
     * @param closure the Filter configuration
     */
    void filter(@DslContext(FilterContext) Closure closure) {
        LOG.debug("Entering subfilter block")
        def filterContext = new FilterContext()
        ContextHelper.executeInContext(closure, filterContext)
        filter.filters.add(filterContext.filter)
        LOG.debug("Exiting subfilter block")
    }

    /**
     * Adds {@link toGit.migration.plan.Action}s to the current {@link Filter}.
     * @param closure the Actions configuration
     */
    void actions(@DslContext(ActionsContext) Closure closure) {
        LOG.debug("Entering actions block")
        def actionsContext = MigrationManager.instance.actionsContext
        ContextHelper.executeInContext(closure, actionsContext)
        filter.actions.addAll(actionsContext.actions)
        LOG.debug("Exiting actions block")
    }

    /**
     * Adds {@link toGit.migration.plan.Extraction}s to the current {@link Filter}.
     * @param closure the Extractions configuration
     */
    void extractions(@DslContext(ExtractionsContext) Closure closure) {
        LOG.debug("Entering extractions block")
        def extractionsContext = MigrationManager.instance.extractionsContext
        ContextHelper.executeInContext(closure, extractionsContext)
        filter.extractions.addAll(extractionsContext.extractions)
        LOG.debug("Exiting extractions block")
    }

    /**
     * Adds {@link toGit.migration.plan.Criteria} to the current {@link Filter}.
     * @param closure the Criteria configuration
     */
    void criteria(@DslContext(CriteriaContext) Closure closure) {
        LOG.debug("Entering criteria block")
        def criteriaContext = MigrationManager.instance.criteriaContext
        ContextHelper.executeInContext(closure, criteriaContext)
        filter.criteria.addAll(criteriaContext.criteria)
        LOG.debug("Exiting criteria block")
    }
}
