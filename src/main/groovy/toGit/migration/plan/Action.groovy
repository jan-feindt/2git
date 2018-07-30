package toGit.migration.plan

abstract class Action {
    def abstract void act(Map<String, Object> extractionMap)
}
