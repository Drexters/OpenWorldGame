package se.liu.samgu876_mellu161.managers;


import se.liu.samgu876_mellu161.components.TimerComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static se.liu.samgu876_mellu161.main.Main.LOGGER;
import static se.liu.samgu876_mellu161.utilz.Constants.ARIAL_10;
import static se.liu.samgu876_mellu161.utilz.HelpFunctions.secToUpdates;

/**
 * Handles the display and timing of temporary on-screen messages.
 *
 * <p>The {@code MessageManager} maintains a queue of messages, each visible for a fixed duration
 * (currently 4 seconds). Messages are rendered to the screen in a vertical list and automatically
 * removed when they expire.
 *
 * <p>Use {@link #addMessage(String)} to add a new message for display.
 * Call {@link #displayMessages(Graphics2D)} during rendering to draw messages.
 */
public class MessageManager
{
    private static class Message {
	private final String message;
	private final static int MESSAGE_DURATION = secToUpdates(4);
	private final TimerComponent timer = new TimerComponent(MESSAGE_DURATION);

	private Message(final String message) {
	    this.message = message;
	    timer.restart();
	}

	public boolean isExpired() {
	    timer.update();
	    return timer.isDone();
	}

	@Override public String toString() {
	    return message;
	}
    }

    private List<Message> messages;

    public MessageManager() {
	messages = new ArrayList<>();
    }

    public void displayMessages(Graphics2D g2) {

	int offsetY = 200; // Track the y-pos of the messages
	final int spaceBetween = 40;
	final int offsetX = 10;

	g2.setColor(Color.WHITE);
	g2.setFont(ARIAL_10);

	// Collect expired messages
	List<Message> expiredMessages = new ArrayList<>();

	for (Message message : messages) {

	    g2.drawString(message.toString(), offsetX, offsetY);
	    offsetY += spaceBetween;

	    if (message.isExpired())
		expiredMessages.add(message);
	}

	// REMOVE EXPIRED MESSAGES
	for (Message message : expiredMessages)
	    messages.remove(message);
    }

    public void addMessage(String message) {
	messages.add(new Message(message));
	LOGGER.log(Level.FINE, "Message: '" + message + "' added to screen.");
    }
}
