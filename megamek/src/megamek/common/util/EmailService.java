/*
* MegaMek -
* Copyright (C) 2021 - The MegaMek Team. All Rights Reserved.
*
* This program is free software; you can redistribute it and/or modify it under
* the terms of the GNU General Public License as published by the Free Software
* Foundation; either version 2 of the License, or (at your option) any later
* version.
*
* This program is distributed in the hope that it will be useful, but WITHOUT
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
* FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
* details.
*/

package megamek.common.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map;
import java.util.Vector;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import megamek.common.IGame;
import megamek.common.IPlayer;
import megamek.common.Report;


public class EmailService {


    private static class RoundReportMessage extends MimeMessage {


        private RoundReportMessage(InternetAddress from,
                                   IPlayer to,
                                   IGame game,
                                   Vector<Report> reports,
                                   int sequenceNumber,
                                   Session session) throws Exception {
            super(session);

            // Since MM mutates game state as it progresses, need to
            // fully create the complete message here, so that by the
            // time it is sent things like the current round number
            // hasn't changed from underneath it

            setFrom(from);
            setRecipient(
                RecipientType.TO,
                new InternetAddress(to.getEmail(), to.getName())
            );

            setHeader(
                "Message-ID",
                newMessageId(from, to, game, sequenceNumber)
            );
            if (sequenceNumber > 0) {
                setHeader(
                    "In-Reply-To",
                    newMessageId(from, to, game, sequenceNumber - 1)
                );
            }

            Report subjectReport;
            var round = game.getRoundCount();
            if (round < 1) {
                subjectReport = new Report(990);
            } else {
                subjectReport = new Report(991);
                subjectReport.add(round, false);
            }
            setSubject(subjectReport.getText());

            var body = new StringBuilder("<div style=\"white-space: pre\">");
            for (var report: reports) {
                body.append(report.getText());
            }
            body.append("</div>");
            setText(body.toString(), "UTF-8", "html");
        }

        protected void updateMessageID() throws MessagingException {
            // no-op, we have already set it in the ctor
        }

        private static String newMessageId(InternetAddress from,
                                           IPlayer to,
                                           IGame game,
                                           int actualSequenceNumber) {
            final var address = from.getAddress();
            return String.format(
                "<megamek.%s.%d.%d.%d@%s>",
                game.getUUIDString(),
                game.getRoundCount(),
                to.getId(),
                actualSequenceNumber,
                address.substring(address.indexOf("@") + 1)
            );
        }

    }


    private InternetAddress from;
    private Map<IPlayer,Integer> messageSequences = new HashMap<>();
    private Properties mailProperties;
    private Session mailSession;


    public EmailService(Properties mailProperties) throws Exception {
        this.from = InternetAddress.parse(
            mailProperties.getProperty("megamek.smtp.from", "")
        )[0];
        this.mailProperties = mailProperties;

        Authenticator auth = null;
        var login = mailProperties.getProperty("megamek.smtp.login", "").trim();
        var password = mailProperties.getProperty("megamek.smtp.password", "").trim();
        if (login.length() > 0 && password.length() > 0) {
            auth = new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(login, password);
                    }
                };
        }

        mailSession = Session.getInstance(mailProperties, auth);
    }

    public Message newReportMessage(IGame game,
                                    Vector<Report> reports,
                                    IPlayer player) throws Exception {
        int nextSequence = 0;
        synchronized (messageSequences) {
            var messageSequence = messageSequences.get(player);
            if (messageSequence != null) {
                nextSequence = messageSequence + 1;
            }
            messageSequences.put(player, nextSequence);
        }
        return new RoundReportMessage(
            from, player, game, reports, nextSequence, mailSession
        );
    }

    public void send(final Message message) throws MessagingException {
        Transport.send(message);
    }

    public void reset() {
        messageSequences.clear();
    }

}
