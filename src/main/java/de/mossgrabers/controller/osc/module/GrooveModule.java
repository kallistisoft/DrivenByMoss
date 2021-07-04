// Written by Kallistisoft - kallistisoft.com
// (c) 2021
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.osc.module;

import de.mossgrabers.controller.osc.exception.IllegalParameterException;
import de.mossgrabers.controller.osc.exception.MissingCommandException;
import de.mossgrabers.controller.osc.exception.UnknownCommandException;
import de.mossgrabers.framework.daw.IHost;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.IGroove;
import de.mossgrabers.framework.daw.GrooveParameterID;
import de.mossgrabers.framework.daw.data.IParameter;
import de.mossgrabers.framework.osc.IOpenSoundControlWriter;

import java.util.LinkedList;


/**
 * All groove related commands.
 *
 * @author Kallistisoft
 */
public class GrooveModule extends AbstractModule {
    private final IGroove   groove;

    /**
     * Constructor.
     *
     * @param host The host
     * @param model The model
     * @param surface The surface
     * @param writer The writer
     */
    public GrooveModule(final IHost host, final IModel model, final IOpenSoundControlWriter writer) {
        super (host, model, writer);
        this.groove = model.getGroove();
    }

    /** {@inheritDoc} */
    @Override
    public String [] getSupportedCommands ()
    {
        return new String []
        {
                "groove"
        };
    }

    /** {@inheritDoc} */
    @Override
    public void execute (final String command, final LinkedList<String> path, final Object value) throws IllegalParameterException, UnknownCommandException, MissingCommandException
    {
        // groove on/off/toggle
        if( path.isEmpty () ) {
            final IParameter enabledParameter = groove.getParameter (GrooveParameterID.ENABLED);
            if (value == null ) {
                final boolean toggle = (enabledParameter.getValue() == 0);
                enabledParameter.setNormalizedValue (toggle ? 1 : 0);
            } else {
                enabledParameter.setNormalizedValue (isTrigger (value) ? 1 : 0);
            }
            return;
        }


        final String subCommand = getSubCommand (path);
        switch( subCommand ) {

            // set shuffle value
            case "value":
                this.groove.getParameter(GrooveParameterID.SHUFFLE_AMOUNT).setValueImmediatly (toInteger(value));
                break;

            // set shuffle rate
            case "rate":
                this.groove.getParameter(GrooveParameterID.SHUFFLE_RATE).setNormalizedValue (toInteger(value) == 1 ? 1 : 0);
                break;

            // set accent phase
            case "phase":
                this.groove.getParameter(GrooveParameterID.ACCENT_PHASE).setValueImmediatly (toInteger(value));
                break;

            // set accent value
            case "accentValue":
                this.groove.getParameter(GrooveParameterID.ACCENT_AMOUNT).setValueImmediatly (toInteger(value));
                break;

            // set accent rate
            case "accentRate":
                int accentRate;
                switch (toInteger(value)) {
                    case 0:
                        accentRate = 0;
                        break;
                    case 1:
                        accentRate = 64;
                        break;
                    case 2:
                        accentRate = 127;
                        break;
                    default:
                        throw new IllegalParameterException("value for /groove/accent/rate must be {0,1,2}");
                }
                this.groove.getParameter(GrooveParameterID.ACCENT_RATE).setValue (accentRate);
                break;

            // default throw error
            default:
                throw new UnknownCommandException (subCommand);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void flush (final boolean dump)
    {
        this.writer.sendOSC ("/groove", this.groove.getParameter(GrooveParameterID.ENABLED).getValue() == 0 ? 0 : 1, dump);
        this.writer.sendOSC ("/groove/value", this.groove.getParameter(GrooveParameterID.SHUFFLE_AMOUNT).getValue(), dump);
        this.writer.sendOSC ("/groove/valueStr", this.groove.getParameter(GrooveParameterID.SHUFFLE_AMOUNT).getDisplayedValue(), dump);
        this.writer.sendOSC ("/groove/rate", this.groove.getParameter(GrooveParameterID.SHUFFLE_RATE).getValue(), dump);
        this.writer.sendOSC ("/groove/rateStr", this.groove.getParameter(GrooveParameterID.SHUFFLE_RATE).getDisplayedValue()+"th", dump);
        this.writer.sendOSC ("/groove/accentValue", this.groove.getParameter(GrooveParameterID.ACCENT_AMOUNT).getValue(), dump);
        this.writer.sendOSC ("/groove/accentValueStr", this.groove.getParameter(GrooveParameterID.ACCENT_AMOUNT).getDisplayedValue(), dump);
        this.writer.sendOSC ("/groove/accentRate", this.groove.getParameter(GrooveParameterID.ACCENT_RATE).getValue(), dump);
        this.writer.sendOSC ("/groove/accentRateStr", this.groove.getParameter(GrooveParameterID.ACCENT_RATE).getDisplayedValue()+"th", dump);
        this.writer.sendOSC ("/groove/phase", this.groove.getParameter(GrooveParameterID.ACCENT_PHASE).getValue(), dump);
        this.writer.sendOSC ("/groove/phaseStr", this.groove.getParameter(GrooveParameterID.ACCENT_PHASE).getDisplayedValue(), dump);
    }
}
