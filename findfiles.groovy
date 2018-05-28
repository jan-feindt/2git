import groovy.io.FileType
import groovy.json.*

import static groovy.io.FileType.FILES

def jsonFiles = []
def dir = new File("")
dir.eachFileRecurse(FileType.FILES) { f ->
    if(f.name.endsWith(".json")) {
        jsonFiles << f
        println(f)
    }
}

def downloadLinks = []

jsonFiles.each {
    def json = new JsonSlurper().parseText(it.text)
    json.file.each {
        if(it) {
            println it?.url_private_download
            println it?.timestamp
            def fileUrl = new URL(it.url_private_download)
            def downloadFileName = fileUrl.path.substring(fileUrl.path.lastIndexOf("/")+1)
            def nameOnDisc = it.timestamp + "_" + downloadFileName
            new File(nameOnDisc).withOutputStream { out ->
                fileUrl.withInputStream { from -> out << from }
            }

        }
    }
}
