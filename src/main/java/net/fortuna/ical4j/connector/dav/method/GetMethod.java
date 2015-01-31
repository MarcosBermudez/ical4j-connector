/**
 * Copyright (c) 2012, Ben Fortuna
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  o Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 *  o Neither the name of Ben Fortuna nor the names of any other contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.fortuna.ical4j.connector.dav.method;

import java.io.IOException;

import net.fortuna.ical4j.connector.dav.DavContext;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ConstraintViolationException;

import org.apache.commons.httpclient.Header;

/**
 * $Id$
 *
 * Created on 19/11/2008
 *
 * @author Ben
 *
 */
public class GetMethod extends org.apache.commons.httpclient.methods.GetMethod {

    DavContext context;

    /**
     * 
     */
    public GetMethod(DavContext context) {
        this.context = context;
    }

    /**
     * @param uri a calendar URI
     */
    public GetMethod(String uri, DavContext context) {
        super(uri);
        this.context = context;
    }

    /**
     * @return a calendar object instance
     * @throws IOException where a communication error occurs
     * @throws ParserException where calendar parsing fails
     * @throws ConstraintViolationException
     */
    public Calendar getCalendar() throws IOException, ParserException, ConstraintViolationException {
        String contentType = getResponseHeader("Content-Type").getValue();
        Header etagHeader = getResponseHeader("ETag");
        if (contentType.startsWith("text/calendar")) {
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(getResponseBodyAsStream());
            if (etagHeader != null) {
                String etag = etagHeader.getValue();
                context.addCalendarEtag(calendar, etag);
            }
            return calendar;
        }
        return null;
    }
}
