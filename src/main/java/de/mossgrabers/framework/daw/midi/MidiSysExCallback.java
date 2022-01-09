// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2022
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.framework.daw.midi;

/**
 * Callback for receiving MIDI System exclusive messages.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public interface MidiSysExCallback
{
    /**
     * Handle received MIDI system exclusive data.
     *
     * @param data The system exclusive formatted in hex
     */
    void handleMidi (final String data);
}
