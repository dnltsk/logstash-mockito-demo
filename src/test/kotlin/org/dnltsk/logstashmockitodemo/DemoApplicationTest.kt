package org.dnltsk.logstashmockitodemo

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.LoggingEvent
import ch.qos.logback.core.Appender
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.atLeastOnce
import com.nhaarman.mockito_kotlin.verify
import net.logstash.logback.LogstashAccessFormatter
import net.logstash.logback.appender.LogstashSocketAppender
import net.logstash.logback.encoder.LogstashEncoder
import net.logstash.logback.layout.LogstashLayout
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.slf4j.LoggerFactory

@RunWith(MockitoJUnitRunner::class)
class DemoApplicationTest {

    val FOO_ARG = "foo arg"
    val BAR_ARG = "bar arg"

    @Mock
    lateinit var mockAppender: Appender<ILoggingEvent>

    @Captor
    lateinit var captorLoggingEvent: ArgumentCaptor<LoggingEvent>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
        logger.addAppender(mockAppender)
    }

    @After
    fun teardown() {
        val logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
        logger.detachAppender(mockAppender)
    }

    @Test
    fun run_logs_the_arguments_only() {
        //given
        val app = DemoApplication()
        //when
        app.run(FOO_ARG, BAR_ARG)
        //than
        verify(mockAppender).doAppend(captorLoggingEvent.capture())
        val loggingEvent = captorLoggingEvent.value

        assertThat(loggingEvent.getLevel()).isEqualTo(Level.INFO)
        assertThat(loggingEvent.formattedMessage).contains(FOO_ARG)
        assertThat(loggingEvent.formattedMessage).contains(FOO_ARG)
    }

    @Test
    fun runVerbose_surrounds_logs_with_started_and_finished_logs() {
        //given
        val app = DemoApplication()
        //when
        app.runVerbose(FOO_ARG, BAR_ARG)

        //than
        verify(mockAppender, atLeastOnce()).doAppend(captorLoggingEvent.capture())
        val loggingEvents = captorLoggingEvent.allValues

        val startedLog = loggingEvents.first()
        assertThat(startedLog.getLevel()).isEqualTo(Level.INFO)
        assertThat(startedLog.formattedMessage).contains("started")

        val endLog = loggingEvents.last()
        assertThat(endLog.getLevel()).isEqualTo(Level.INFO)
        assertThat(endLog.formattedMessage).contains("finished")
    }

}