package toGit.utils

import org.slf4j.LoggerFactory


class ExceptionHelper {

    final static LOG = LoggerFactory.getLogger(this.class)

    static void simpleLog(Exception e) {
        while (e) {
            def name = e.class.simpleName
            def message = e.message
            LOG.error("$name: $message")
            e = e.cause instanceof Exception ? e.cause as Exception : null
        }
    }
}
