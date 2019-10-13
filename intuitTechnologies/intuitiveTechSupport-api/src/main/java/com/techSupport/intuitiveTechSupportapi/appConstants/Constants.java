package com.techSupport.intuitiveTechSupportapi.appConstants;

import org.springframework.beans.factory.annotation.Value;

public class Constants {
    public static final String bookingConfirmationSubject="${call.support.booked.subject}";
    public static final String bookingConfirmationBody = "${call.support.booked.body}";
    public static final String bookingCancellationSubject="${call.support.cancel.subject}";
    public static final String bookingCancellationBody="${call.support.cancel.body}";
    public static final String maximumCallsInSlots = "${slot.max.calls}";
    public static final String bookingFreezeTime = "${book.cancel.freeze.time}";
    public static final String dateFormat = "${date.format}";
    public static final String timeFormat = "${time.format}";


    public static final String mailServiceHost = "${spring.mail.host}";
    public static final String mailServicePort = "${spring.mail.port}";
    public static final String mailServiceUsername = "${spring.mail.username}";
    public static final String mailServicePassword = "${spring.mail.password}";
    public static final String mailFrom="${spring.mail.from.support}";

    public static final String securityUSERusername="${spring.security.user.username}";
    public static final String securityUSERpaassword="${spring.security.user.password}";
    public static final String securityADMINusername="${spring.security.admin.username}";
    public static final String securityADMINpassword="${spring.security.admin.password}";


}
