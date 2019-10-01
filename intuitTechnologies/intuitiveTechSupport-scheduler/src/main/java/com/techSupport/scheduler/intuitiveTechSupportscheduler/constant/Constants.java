package com.techSupport.scheduler.intuitiveTechSupportscheduler.constant;

public class Constants {

    public static final String mailServiceHost = "${spring.mail.host}";
    public static final String mailServicePort = "${spring.mail.port}";
    public static final String mailServiceUsername = "${spring.mail.username}";
    public static final String mailServicePassword = "${spring.mail.password}";
    public static final String dateFormat = "${date.format}";
    public static final String timeFormat = "${time.format}";
    public static final String slotDaysOpen = "${slots.open.days}";

    public static final String subject = "${call.support.booked.subject}";
    public static final String body ="${call.support.booked.body}";

}
