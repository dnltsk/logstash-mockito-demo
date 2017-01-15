package org.dnltsk.logstashmockitodemo

import org.slf4j.LoggerFactory


open class DemoApplication(){

    private val LOG = LoggerFactory.getLogger(javaClass)

    fun runVerbose(vararg args: String) {
        LOG.info("started DemoApplication..")
        run(*args)
        LOG.info("finished DemoApplication!")
    }

    fun run(vararg args: String) {
        LOG.info("args: "+args.asList())
    }

    companion object {
        @JvmStatic fun main(vararg args: String) {
            DemoApplication().run(*args)
        }

    }

}

