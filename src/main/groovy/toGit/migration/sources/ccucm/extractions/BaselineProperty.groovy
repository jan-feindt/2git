package toGit.migration.sources.ccucm.extractions

import org.slf4j.LoggerFactory
import toGit.migration.plan.Extraction
import toGit.migration.plan.Snapshot
import toGit.migration.sources.ccucm.Baseline

class BaselineProperty extends Extraction {

    final static LOG = LoggerFactory.getLogger(this.class)

    Map<String, String> map

    BaselineProperty(Map<String, String> map) {
        this.map = map
    }

    @Override
    HashMap<String, Object> extract(Snapshot snapshot) {
        def result = [:]
        map.each { propertyName ->
            LOG.debug("Extracting baseline property '${propertyName.value}'")
            def value = ((Baseline) snapshot).source."$propertyName.value"
            LOG.debug("Extracted value ${value}")
            result.put(propertyName.key, value)
        }
        return result
    }
}