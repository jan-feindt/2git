package all2all.migration.sources.mercurial

class MercurialRepoPlan {

    String sourceRepoPath
    String sourceRepoName
    List<MercurialRepo> repos = []
    Map<String, String> structure = [:]


    MercurialRepoPlan(String sourceRepoPath, String sourceRepoName) {
        this.sourceRepoPath = sourceRepoPath
        this.sourceRepoName = sourceRepoName
    }

    void modelAllRepos(String repoName) {
        readStructure()
        createRepos()
        exportRepos()
        buildPlan()
    }

    void readStructure() {
        String file = new File(sourceRepoPath + "/.hgsub").absolutePath
        String fileContent = new File(file).text
        fileContent.eachLine { line ->
            def (whereIs, fromWhere) = line.tokenize("= ")
            structure.put(whereIs, fromWhere)
        }
    }

    void createRepos() {
        //TODO modify the path of the bash commands with the ID of the repo
        //for all in structure
            //create repo
            //extractMerc
            //export
            //extractGit
            //setMergedList
    }

}
