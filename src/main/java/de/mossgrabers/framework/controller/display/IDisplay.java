// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2022
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.framework.controller.display;

/**
 * Interface to a display.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public interface IDisplay
{


    /**
     * Displays a notification message on the display and in the DAW, with additional dump parameter
     *
     * @param message The message to display
     * @param dump Force the message to display if true, otherwise filter duplicate messages
     */
    void notify (final String message, final boolean dump);

    /**
     * Displays a notification message on the display and in the DAW.
     *
     * @param message The message to display
     */
    void notify (final String message);

    /**
     * Cancels the display of a notification message.
     */
    void cancelNotification ();


    /**
     * If there is any cleanup necessary.
     */
    void shutdown ();
}
