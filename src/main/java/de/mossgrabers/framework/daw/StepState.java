// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2022
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.framework.daw;

/**
 * Enumeration for the state of a step.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public enum StepState
{
    /** Step contains no note. */
    OFF,
    /** A note starts at this step. */
    START,
    /** A started note continues at that step. */
    CONTINUE;
}
