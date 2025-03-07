// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2022
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.bitwig.framework.daw.data;

import de.mossgrabers.framework.controller.valuechanger.IValueChanger;
import de.mossgrabers.framework.daw.IHost;
import de.mossgrabers.framework.daw.data.IEqualizerDevice;
import de.mossgrabers.framework.daw.data.IParameter;

import com.bitwig.extension.controller.api.Device;
import com.bitwig.extension.controller.api.Parameter;
import com.bitwig.extension.controller.api.SpecificBitwigDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;


/**
 * Encapsulates the data of a Bitwig equalizer EQ+ device.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class EqualizerDeviceImpl extends SpecificDeviceImpl implements IEqualizerDevice
{
    /** The ID of the Bitwig EQ+ plugin. */
    public static final UUID                  ID_BITWIG_EQ_PLUS = UUID.fromString ("e4815188-ba6f-4d14-bcfc-2dcb8f778ccb");

    private static final Map<String, String>  EQ_TYPES          = new HashMap<> ();
    private static final Map<String, Integer> EQ_TYPE_INDICES   = new HashMap<> ();
    static
    {
        EQ_TYPES.put ("Off", "off");
        EQ_TYPES.put ("High-c", "highcut");
        EQ_TYPES.put ("Low-s", "lowshelf");
        EQ_TYPES.put ("Low-c", "lowcut");
        EQ_TYPES.put ("High-s", "highshelf");
        EQ_TYPES.put ("Bell", "bell");
        EQ_TYPES.put ("Notch", "notch");

        EQ_TYPE_INDICES.put ("off", Integer.valueOf (0));
        EQ_TYPE_INDICES.put ("lowcut", Integer.valueOf (3));
        EQ_TYPE_INDICES.put ("lowshelf", Integer.valueOf (6));
        EQ_TYPE_INDICES.put ("bell", Integer.valueOf (7));
        EQ_TYPE_INDICES.put ("highcut", Integer.valueOf (10));
        EQ_TYPE_INDICES.put ("highshelf", Integer.valueOf (13));
        EQ_TYPE_INDICES.put ("notch", Integer.valueOf (14));
    }

    private static final int       NUMBER_OF_BANDS = 8;

    private final List<IParameter> types           = new ArrayList<> ();
    private final List<IParameter> frequencies     = new ArrayList<> ();
    private final List<IParameter> gains           = new ArrayList<> ();
    private final List<IParameter> qs              = new ArrayList<> ();


    /**
     * Constructor.
     *
     * @param host The host
     * @param valueChanger The value changer
     * @param device The device to encapsulate
     */
    public EqualizerDeviceImpl (final IHost host, final IValueChanger valueChanger, final Device device)
    {
        super (host, valueChanger, device, 0, 0, 0, 0, 0, 0);

        final SpecificBitwigDevice eqDevice = device.createSpecificBitwigDevice (ID_BITWIG_EQ_PLUS);

        for (int i = 0; i < NUMBER_OF_BANDS; i++)
        {
            final String index = Integer.toString (i + 1);

            final Parameter typeParameter = eqDevice.createParameter ("TYPE" + index);
            this.types.add (new ParameterImpl (valueChanger, typeParameter, i));

            final Parameter frequencyParameter = eqDevice.createParameter ("FREQ" + index);
            this.frequencies.add (new ParameterImpl (valueChanger, frequencyParameter, i));

            final Parameter gainParameter = eqDevice.createParameter ("GAIN" + index);
            this.gains.add (new ParameterImpl (valueChanger, gainParameter, i));

            final Parameter qsParameter = eqDevice.createParameter ("Q" + index);
            this.qs.add (new ParameterImpl (valueChanger, qsParameter, i));
        }
    }


    /** {@inheritDoc} */
    @Override
    public int getBandCount ()
    {
        return NUMBER_OF_BANDS;
    }


    /** {@inheritDoc} */
    @Override
    public String getType (final int index)
    {
        final String typeName = this.types.get (index).getDisplayedValue ();
        for (final Entry<String, String> e: EQ_TYPES.entrySet ())
        {
            if (typeName.startsWith (e.getKey ()))
                return e.getValue ();
        }
        return "off";
    }


    /** {@inheritDoc} */
    @Override
    public void setType (final int index, final String type)
    {
        final IParameter param = this.types.get (index);
        final Integer v = EQ_TYPE_INDICES.get (type);
        ((ParameterImpl) param).getParameter ().set (v == null ? Integer.valueOf (0) : v, Integer.valueOf (15));
    }


    /** {@inheritDoc} */
    @Override
    public IParameter getFrequency (final int index)
    {
        return this.frequencies.get (index);
    }


    /** {@inheritDoc} */
    @Override
    public IParameter getGain (final int index)
    {
        return this.gains.get (index);
    }


    /** {@inheritDoc} */
    @Override
    public IParameter getQ (final int index)
    {
        return this.qs.get (index);
    }
}
