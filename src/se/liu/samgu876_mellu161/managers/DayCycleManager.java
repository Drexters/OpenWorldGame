package se.liu.samgu876_mellu161.managers;

import se.liu.jonkv82.annotations.BorrowedCode;
import se.liu.samgu876_mellu161.components.TimerComponent;

import java.awt.*;
import java.util.logging.Level;

import static se.liu.samgu876_mellu161.main.Main.LOGGER;
import static se.liu.samgu876_mellu161.utilz.HelpFunctions.secToUpdates;

/**
 * Handles the in-game day cycle with four phases: NIGHT, DAWN, DAY, and DUSK.
 * Each phase has a set duration and affects screen tint (e.g. night is dark, dawn/dusk fade with a red tint).
 * Use {@link #getTint()} for the current color and {@link #getDayPhase()} for the current phase.
 */
public class DayCycleManager
{
    public enum DayPhase {NIGHT, DAWN, DAY, DUSK}
    private TimerComponent dayTime;

    // --- How much of a whole day a phase take up
    // --- These percents should add up to 1, meaning a whole day
    private static final float NIGHT_PERCENT = 0.3f;
    private static final float DAWN_PERCENT = 0.2f;
    private static final float DAY_PERCENT = 0.3f;
    private static final float DUSK_PERCENT = 0.2f;

    // --- The start time for each phase
    private static final float NIGHT_END = NIGHT_PERCENT;
    private static final float DAWN_END = NIGHT_END + DAWN_PERCENT;
    private static final float DAY_END = DAWN_END + DAY_PERCENT;
    private static final float DUSK_END = DAY_END + DUSK_PERCENT;

    // --- Colors
    private static final Color NIGHT_COLOR = new Color(0, 0, 20, 240);
    private static final Color DAY_COLOR = new Color(0, 0, 0, 0);
    private static final Color TRANSITION_COLOR = new Color(165, 44, 7, 68);

    public DayCycleManager(final int dayDuration) {
        this.dayTime = new TimerComponent(secToUpdates(dayDuration));
        setDayPhase(DayPhase.DAY);
    }

    public void update() {
        dayTime.update();
        if (dayTime.isDone()) dayTime.restart();
    }

    public Color getTint() {
        float dayProgress = dayTime.getProgress();

        if (dayProgress < NIGHT_END) { // It is night
            return NIGHT_COLOR;
        } else if (dayProgress < DAWN_END) { // It is dawn
            // Transition: NIGHT → DAY
            float transitionProgress = (dayProgress - NIGHT_END) / DAWN_PERCENT;
            return getSwitchPhaseColor(NIGHT_COLOR, DAY_COLOR, TRANSITION_COLOR, transitionProgress);
        } else if (dayProgress < DAY_END) { // It is day
            return DAY_COLOR;
        } else { // It is dusk
            // Transition: DAY → NIGHT
            float transitionProgress = (dayProgress - DAY_END) / DUSK_PERCENT;
            return getSwitchPhaseColor(DAY_COLOR, NIGHT_COLOR, TRANSITION_COLOR, transitionProgress);
        }
    }

    private Color getSwitchPhaseColor(Color from, Color to, Color midTone, float transitionProgress) {
        final float midpoint = 0.5f;
        final float transitionColorSpeed = 2.0f; // When the progress is 0.5 (50%) the color should be 100% of the midtone therefor the transitions speed is x2
        if (transitionProgress < midpoint) {
            return lerpColor(from, midTone, transitionProgress * transitionColorSpeed);
        } else {
            return lerpColor(midTone, to, (transitionProgress - midpoint) * transitionColorSpeed);
        }
    }

    @BorrowedCode(source = "ChatGPT")
    private Color lerpColor(Color a, Color b, float transitionProgress) {
        int r = (int) (a.getRed() + (b.getRed() - a.getRed()) * transitionProgress);
        int g = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * transitionProgress);
        int bCol = (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * transitionProgress);
        int alpha = (int) (a.getAlpha() + (b.getAlpha() - a.getAlpha()) * transitionProgress);
        return new Color(r, g, bCol, alpha);
    }

    public DayPhase getDayPhase() {
        float dayProgress = dayTime.getProgress();

        if (dayProgress < NIGHT_END) return DayPhase.NIGHT;
        if (dayProgress < DAWN_END) return DayPhase.DAWN;
        if (dayProgress < DAY_END) return DayPhase.DAY;
        return DayPhase.DUSK;
    }

    public void setDayPhase(DayPhase phase) {
        LOGGER.log(Level.FINE, "Day phase changed to: " + phase);
        switch (phase) {
            case NIGHT -> dayTime.setProgress(DUSK_END);
            case DAWN -> dayTime.setProgress(NIGHT_END);
            case DAY -> dayTime.setProgress(DAWN_END);
            case DUSK -> dayTime.setProgress(DAY_END);
        }
    }
}
