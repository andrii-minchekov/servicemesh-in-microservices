package com.example.orderservice.rest.filter

import org.springframework.web.filter.CommonsRequestLoggingFilter
import javax.servlet.http.HttpServletRequest

class CustomRequestLoggingFilter : CommonsRequestLoggingFilter() {

    companion object {
        const val BEFORE = "REQUEST STARTED : "
        const val AFTER = "REQUEST FINISHED."
        const val PROMETHEUS = "uri=/actuator/prometheus"
    }

    init {
        super.setBeforeMessagePrefix(BEFORE)
        super.setAfterMessagePrefix(AFTER)
    }

    override fun isIncludeHeaders(): Boolean {
        return true
    }

    override fun isIncludeQueryString(): Boolean {
        return true
    }

    override fun beforeRequest(request: HttpServletRequest, message: String) {
        if (!message.startsWith("$BEFORE$PROMETHEUS")) {
            super.beforeRequest(request, message)
        }
    }

    override fun afterRequest(request: HttpServletRequest, message: String) {
        if (!message.startsWith("$AFTER$PROMETHEUS")) {
            super.afterRequest(request, AFTER)
        }
    }
}