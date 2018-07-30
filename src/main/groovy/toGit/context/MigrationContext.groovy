package toGit.context

import org.slf4j.LoggerFactory
import toGit.migration.MigrationManager

import static ContextHelper.executeInContext

class MigrationContext implements Context {

    final static LOG = LoggerFactory.getLogger(this.class)

    /**
     * Code block to configure {@link toGit.migration.plan.Action}s to be executed after the migration
     * @param closure the After configuration
     */
    void after(@DslContext(AfterContext) Closure closure) {
        LOG.debug("Entering after block")
        executeInContext(closure, new AfterContext())
        LOG.debug("Exiting after block")
    }

    /**
     * Code block to configure {@link toGit.migration.plan.Action}s to be executed before the migration
     * @param closure the Before configuration
     */
    void before(@DslContext(BeforeContext) Closure closure) {
        LOG.debug("Entering before block")
        executeInContext(closure, new BeforeContext())
        LOG.debug("Exiting before block")
    }

    /**
     * Configures {@link toGit.migration.plan.Filter}s for this migration
     * @param closure the Filter configuration
     */
    void filters(@DslContext(FiltersContext) Closure closure) {
        LOG.debug("Entering filters block")
        def filtersContext = new FiltersContext()
        executeInContext(closure, filtersContext)
        MigrationManager.instance.plan.filters.addAll(filtersContext.filters)
        LOG.debug("Exiting filters block")
    }
}
