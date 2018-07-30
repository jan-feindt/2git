package toGit.context

import static toGit.context.ContextHelper.executeInContext

import org.slf4j.LoggerFactory
import toGit.migration.MigrationManager

/**
 * Defines {@link toGit.migration.plan.Action}s to execute before the migration
 * @param closure the closure defining the {@link toGit.migration.plan.Action}s
 */
class BeforeContext implements Context {

    final static LOG = LoggerFactory.getLogger(this.class)

    void actions(@DslContext(ActionsContext) Closure closure) {
        LOG.debug('Registering pre-migration actions')
        ActionsContext actionsContext = MigrationManager.instance.actionsContext
        executeInContext(closure, actionsContext)
        int amount = actionsContext.actions.size()
        MigrationManager.instance.plan.befores.addAll(actionsContext.actions)
        LOG.debug("Registered $amount pre-migration actions")
    }
}
