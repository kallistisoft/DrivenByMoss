// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2022
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.framework.controller.grid;

/**
 * Callback for a virtual fader.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public interface IVirtualFaderCallback
{
    /**
     * Hook for getting the fader value.
     *
     * @return The fader value
     */
    int getValue ();


    /**
     * Hook for setting the fader value.
     *
     * @param value The fader value
     */
    void setValue (int value);
}
