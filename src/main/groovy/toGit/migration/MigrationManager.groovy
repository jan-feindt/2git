package toGit.migration

import org.slf4j.LoggerFactory
import toGit.context.ActionsContext
import toGit.context.CriteriaContext
import toGit.context.ExtractionsContext
import toGit.context.Context
import toGit.migration.plan.MigrationPlan
import toGit.migration.sources.MigrationSource
import toGit.migration.targets.MigrationTarget
import toGit.utils.ExceptionHelper

@Singleton
class MigrationManager {

    final static LOG = LoggerFactory.getLogger(this.class)

    MigrationSource source
    LinkedHashMap<String, MigrationTarget> targets = []
    MigrationPlan plan

    Context criteriaContext
    Context extractionsContext
    Context actionsContext

    void reset() {
        source = null
        targets = []
        plan = new MigrationPlan()
        criteriaContext = new CriteriaContext()
        extractionsContext = new ExtractionsContext()
        actionsContext = new ActionsContext()
    }

    void resetMigrationPlan() {
        plan = new MigrationPlan()
    }

    void migrate(boolean dryRun = false) {
        if(dryRun) {
            plan.build()
            return
        }
        try {
            LOG.info("Preparing source")
            source.prepare()
            LOG.info("Prepared source")
            LOG.info("Preparing targets")
            targets.values().each { t -> t.prepare() }
            LOG.info("Prepared targets")
            plan.build()
            LOG.info("Executing migration plan")
            plan.execute()
            LOG.info("Executed migration plan")
        } catch (Exception e) {
            LOG.error('An error occurred during the migration')
            ExceptionHelper.simpleLog(e)
            LOG.error('The migration has been stopped')
            throw e
        } finally {
            LOG.info('Cleaning up source')
            source.cleanup()
            LOG.info('Cleaned up source')
            LOG.info('Cleaning up targets')
            targets.values().each { t -> t.cleanup() }
            LOG.info('Cleaned up targets')
        }
    }
}