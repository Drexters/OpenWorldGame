package se.liu.samgu876_mellu161.managers;

import se.liu.samgu876_mellu161.gameobjects.GameObject;
import se.liu.samgu876_mellu161.interactions.InteractionAction;
import se.liu.samgu876_mellu161.main.GameManager;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;


import static se.liu.samgu876_mellu161.main.Main.LOGGER;
import static se.liu.samgu876_mellu161.utilz.Constants.ARIAL_10;

/**
 * Handles drawing the game user interface, including messages and day phase display.
 * Also manages interaction menus for game objects.
 */
public class UIManager
{
    private final static int DEFAULT_BORDER_SIZE = 3;
    private final static int DEFAULT_ARC_SIZE = 40;
    private final static Color DEFAULT_BORDER_COLOR = new Color(0, 0, 0, 200);
    private final static int SUBWINDOW_ARC_SIZE = 50;
    private final static int SUBWINDOW_BORDER_OFFSET = 5;

    private GameManager gameManager;
    private Graphics2D g2 = null;
    private MessageManager messageManager = new MessageManager();

    public UIManager(GameManager gm) {
    	this.gameManager = gm;
    }

    public void setG2(final Graphics2D g2) {
	this.g2 = g2;
    }

    public void draw() {
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	final int subwindowWidth = 400;
	final int subwindowOffY = 10; //offset from top of screen
	final int subwindowHight = 60;
	drawSubWindow(gameManager.getWidth()/2 - subwindowWidth /2, subwindowOffY, subwindowWidth, subwindowHight, DEFAULT_BORDER_COLOR);

	g2.setFont(ARIAL_10);
	g2.setColor(Color.WHITE);
	final int offY = 50;
	drawCenteredText(gameManager.getEnvironmentManager().getDayManager().getDayPhase().toString(), gameManager.getWidth() / 2, offY);

	messageManager.displayMessages(g2);
    }

    public void drawSubWindow(int x, int y, int width, int height, Color color) {
	g2.setColor(color);
	g2.fillRoundRect(x, y, width, height, SUBWINDOW_ARC_SIZE, SUBWINDOW_ARC_SIZE);

	// BORDER
	g2.setStroke(new BasicStroke(DEFAULT_BORDER_SIZE));
	g2.setColor(Color.WHITE);
	g2.drawRoundRect(
		x+SUBWINDOW_BORDER_OFFSET,
		y+SUBWINDOW_BORDER_OFFSET,
		width-2*SUBWINDOW_BORDER_OFFSET,
		height-2*SUBWINDOW_BORDER_OFFSET,
		DEFAULT_ARC_SIZE, DEFAULT_ARC_SIZE
	);
    }

    public void drawCenteredText(String str, int x, int y) {
	x -= g2.getFontMetrics().stringWidth(str) / 2; // minus half the width of the string
	g2.drawString(str, x, y);
    }

    public MessageManager getMessageManager() {
	return messageManager;
    }

    public void addMessage(String message) {
	messageManager.addMessage(message);
    }

    public void showInteractionMenu(final GameObject obj, final int screenX, final int screenY) {
	if (obj.getInteractions().isEmpty()) return;

	JPopupMenu menu = new JPopupMenu();

	for (InteractionAction action : obj.getInteractions()) {
	    JMenuItem item = new JMenuItem(action.getName());
	    item.addActionListener(e -> {
		action.execute(obj);
		obj.interact(action);
		LOGGER.log(Level.FINE, "Player interacted with " + obj.getClass().getSimpleName());
		System.out.println(obj.getName());
	    });
	    menu.add(item);
	}

	menu.show(gameManager, screenX, screenY);
    }
}
