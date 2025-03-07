// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2022
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.ableton.push.mode;

import de.mossgrabers.controller.ableton.push.controller.Push1Display;
import de.mossgrabers.controller.ableton.push.controller.PushControlSurface;
import de.mossgrabers.framework.controller.ButtonID;
import de.mossgrabers.framework.controller.display.IGraphicDisplay;
import de.mossgrabers.framework.controller.display.ITextDisplay;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.ITransport;
import de.mossgrabers.framework.daw.constants.TransportConstants;
import de.mossgrabers.framework.daw.data.IItem;
import de.mossgrabers.framework.featuregroup.AbstractFeatureGroup;
import de.mossgrabers.framework.featuregroup.AbstractMode;
import de.mossgrabers.framework.utils.ButtonEvent;


/**
 * Editing of transport parameters.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class MetronomeMode extends BaseMode<IItem>
{
    private static final String [] PREROLLS      =
    {
        TransportConstants.PREROLL_NONE,
        TransportConstants.PREROLL_1_BAR,
        TransportConstants.PREROLL_2_BARS,
        TransportConstants.PREROLL_4_BARS
    };

    private static final String [] PREROLL_NAMES =
    {
        "None",
        "1 Bar",
        "2 Bars",
        "4 Bars"
    };

    private final ITransport       transport;


    /**
     * Constructor.
     *
     * @param surface The control surface
     * @param model The model
     */
    public MetronomeMode (final PushControlSurface surface, final IModel model)
    {
        super ("Transport", surface, model);

        this.transport = this.model.getTransport ();
    }


    /** {@inheritDoc} */
    @Override
    public void onFirstRow (final int index, final ButtonEvent event)
    {
        if (event != ButtonEvent.UP)
            return;

        if (index < PREROLLS.length)
            this.transport.setPreroll (PREROLLS[index]);
        else if (index == 7)
            this.transport.togglePrerollMetronome ();
    }


    /** {@inheritDoc} */
    @Override
    public String getButtonColorID (final ButtonID buttonID)
    {
        final int index = this.isButtonRow (0, buttonID);
        if (index >= 0)
        {
            final String preroll = this.transport.getPreroll ();
            if (index < PREROLLS.length)
                return PREROLLS[index].equals (preroll) ? AbstractMode.BUTTON_COLOR_HI : AbstractFeatureGroup.BUTTON_COLOR_ON;
            if (index == 7)
                return this.transport.isPrerollMetronomeEnabled () ? AbstractMode.BUTTON_COLOR2_HI : AbstractFeatureGroup.BUTTON_COLOR_ON;
        }

        return AbstractFeatureGroup.BUTTON_COLOR_OFF;
    }


    /** {@inheritDoc} */
    @Override
    public void updateDisplay1 (final ITextDisplay display)
    {
        display.setBlock (2, 0, "Pre-roll");

        final String preroll = this.transport.getPreroll ();
        for (int i = 0; i < PREROLLS.length; i++)
            display.setCell (3, i, (PREROLLS[i].equals (preroll) ? Push1Display.SELECT_ARROW : "") + PREROLL_NAMES[i]);

        display.setBlock (2, 2, "   Play Metronome").setBlock (2, 3, "during Pre-roll?");
        display.setCell (3, 7, this.transport.isPrerollMetronomeEnabled () ? "  Yes" : "  No");
    }


    /** {@inheritDoc} */
    @Override
    public void updateDisplay2 (final IGraphicDisplay display)
    {
        final String preroll = this.transport.getPreroll ();
        for (int i = 0; i < PREROLLS.length; i++)
            display.addOptionElement ("", "", false, i == 0 ? "Pre-roll" : "", PREROLL_NAMES[i], PREROLLS[i].equals (preroll), false);

        display.addEmptyElement ();
        display.addOptionElement ("", "", false, "Play Metronome during Pre-Roll?", "", false, false);
        display.addEmptyElement ();
        display.addOptionElement ("", "", false, "", this.transport.isPrerollMetronomeEnabled () ? "Yes" : "No", this.transport.isPrerollMetronomeEnabled (), false);
    }
}