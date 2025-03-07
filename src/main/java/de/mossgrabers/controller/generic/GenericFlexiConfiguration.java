// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2022
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.generic;

import de.mossgrabers.controller.generic.controller.CommandCategory;
import de.mossgrabers.controller.generic.controller.FlexiCommand;
import de.mossgrabers.controller.generic.flexihandler.AbstractHandler;
import de.mossgrabers.controller.generic.flexihandler.utils.CommandSlot;
import de.mossgrabers.controller.generic.flexihandler.utils.KnobMode;
import de.mossgrabers.framework.configuration.AbstractConfiguration;
import de.mossgrabers.framework.configuration.IActionSetting;
import de.mossgrabers.framework.configuration.IEnumSetting;
import de.mossgrabers.framework.configuration.IIntegerSetting;
import de.mossgrabers.framework.configuration.ISettingsUI;
import de.mossgrabers.framework.configuration.IStringSetting;
import de.mossgrabers.framework.controller.valuechanger.IValueChanger;
import de.mossgrabers.framework.daw.IHost;
import de.mossgrabers.framework.daw.midi.ArpeggiatorMode;
import de.mossgrabers.framework.observer.IValueObserver;
import de.mossgrabers.framework.scale.Scales;
import de.mossgrabers.framework.utils.FileEx;
import de.mossgrabers.framework.utils.Pair;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * The configuration settings for Generic Flexi.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class GenericFlexiConfiguration extends AbstractConfiguration
{
    private static final String                      TAG_TYPE                     = "TYPE";
    private static final String                      TAG_NUMBER                   = "NUMBER";
    private static final String                      TAG_MIDI_CHANNEL             = "MIDI_CHANNEL";
    private static final String                      TAG_RESOLUTION               = "RESOLUTION";
    private static final String                      TAG_KNOB_MODE                = "KNOB_MODE";
    private static final String                      TAG_SEND_VALUE               = "SEND_VALUE";
    private static final String                      TAG_SEND_VALUE_WHEN_RECEIVED = "SEND_VALUE_WHEN_RECEIVED";
    private static final String                      TAG_COMMAND                  = "COMMAND";

    /** Export signal. */
    public static final Integer                      BUTTON_SAVE                  = Integer.valueOf (50);
    /** Import signal. */
    public static final Integer                      BUTTON_LOAD                  = Integer.valueOf (51);
    /** Enable MMC. */
    public static final Integer                      ENABLE_MMC                   = Integer.valueOf (52);
    /** The selected mode. */
    public static final Integer                      SELECTED_MODE                = Integer.valueOf (53);

    private static final String                      CATEGORY_KEYBOARD            = "Keyboard / Pads (requires restart)";
    private static final String                      CATEGORY_OPTIONS             = "Options";

    private static final String []                   NAMES                        = FlexiCommand.getNames ();

    /** The types. */
    public static final List<String>                 OPTIONS_TYPE                 = List.of ("Off", "CC", "Note", "Program Change", "Pitchbend", "MMC");

    // @formatter:off
    static final List<String>                        NUMBER_NAMES              = List.of (
        "0  CC Bank Select",
        "1  MMC Stop, CC Modulation",
        "2  MMC Play, CC Breath Controller",
        "3  MMC Deferred Play",
        "4  MMC Fast Forward, CC Foot Controller",
        "5  MMC Rewind, CC Portamento Time",
        "6  MMC Punch In, CC Data Entry MSB",
        "7  MMC Punch Out, CC Volume",
        "8  MMC Record Pause, CC Balance",
        "9  MMC Play Pause   ",
        "10 MMC Eject, CC Pan",
        "11 MMC Chase, CC Expression",
        "12 CC Effect Controller 1",
        "13 CC Effect Controller 2",
        "14 -",
        "15 -",
        "16  -",
        "17  -",
        "18  -",
        "19  -",
        "20  -",
        "21  -",
        "22  -",
        "23  -",
        "24  -",
        "25  -",
        "26  -",
        "27  -",
        "28  -",
        "29  -",
        "30  -",
        "31  -",
        "32  -",
        "33  -",
        "34  -",
        "35  -",
        "36  -",
        "37  -",
        "38  -",
        "39  -",
        "40  -",
        "41  -",
        "42  -",
        "43  -",
        "44  -",
        "45  -",
        "46  -",
        "47  -",
        "48  -",
        "49  -",
        "50  -",
        "51  -",
        "52  -",
        "53  -",
        "54  -",
        "55  -",
        "56  -",
        "57  -",
        "58  -",
        "59  -",
        "60  -",
        "61  -",
        "62  -",
        "63  -",
        "64  CC Damper Pedal",
        "65  CC Portamento On/Off Switch",
        "66  CC Sostenuto On/Off Switch",
        "67  CC Soft Pedal On/Off Switch",
        "68  CC Legato Footswitch",
        "69  CC Hold 2",
        "70  CC Sound Controller 1",
        "71  CC Sound Controller 2",
        "72  CC Sound Controller 3",
        "73  CC Sound Controller 4",
        "74  CC Sound Controller 5",
        "75  CC Sound Controller 6",
        "76  CC Sound Controller 7",
        "77  CC Sound Controller 8",
        "78  CC Sound Controller 9",
        "79  CC Sound Controller 10",
        "80  CC General Purpose",
        "81  CC General Purpose",
        "82  CC General Purpose",
        "83  CC General Purpose",
        "84  CC Portamento",
        "85  -",
        "86  -",
        "87  -",
        "88  -",
        "89  -",
        "90  -",
        "91  CC Effect 1 Depth",
        "92  CC Effect 2 Depth",
        "93  CC Effect 3 Depth",
        "94  CC Effect 4 Depth",
        "95  CC Effect 5 Depth",
        "96  CC (+1) Data Increment",
        "97  CC (-1) Data Decrement",
        "98  CC NRPN LSB",
        "99  CC NRPN MSB",
        "100 CC RPN LSB",
        "101 CC RPN MSB",
        "102 -",
        "103 -",
        "104 -",
        "105 -",
        "106 -",
        "107 -",
        "108 -",
        "109 -",
        "110 -",
        "111 -",
        "112 -",
        "113 -",
        "114 -",
        "115 -",
        "116 -",
        "117 -",
        "118 -",
        "119 -",
        "120 CC All Sound Off",
        "121 CC Reset All Controllers",
        "122 CC Local On/Off Switch",
        "123 CC All Notes Off",
        "124 CC Omni Mode Off",
        "125 CC Omni Mode On",
        "126 CC Mono Mode",
        "127 CC Poly Mode"
    );


    /** The Modes options. */
    private static final List<String>                MODES                     = List.of (
        "Track",
        "Volume",
        "Panorama",
        "Send 1",
        "Send 2",
        "Send 3",
        "Send 4",
        "Send 5",
        "Send 6",
        "Send 7",
        "Send 8",
        "Parameters"
    );

    /** The MIDI channel options. */
    private static final List<String>                KEYBOARD_CHANNELS         = List.of (
        "Off",
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "All"
    );
    // @formatter:on

    private static final List<String>                CONTROLLER_CHANNELS          = KEYBOARD_CHANNELS.subList (1, KEYBOARD_CHANNELS.size ());
    private static final List<String>                OPTIONS_RESOLUTION           = List.of ("7-bit", "14-bit");

    /** A setting of a slot has changed. */
    static final Integer                             SLOT_CHANGE                  = Integer.valueOf (1000);
    /** The MPE on/off setting has changed. */
    static final Integer                             ENABLED_MPE_ZONES            = Integer.valueOf (1001);
    /** The MPE pitch bend sensitivity setting has changed. */
    static final Integer                             MPE_PITCHBEND_RANGE          = Integer.valueOf (1002);

    /** The number of command slots. */
    public static final int                          NUM_SLOTS                    = 300;

    private IEnumSetting                             slotSelectionSetting;
    private IEnumSetting                             typeSetting;
    private IEnumSetting                             numberSetting;
    private IEnumSetting                             midiChannelSetting;
    private IEnumSetting                             resolutionSetting;
    private IEnumSetting                             knobModeSetting;
    private IEnumSetting                             sendValueSetting;
    private IEnumSetting                             sendValueWhenReceivedSetting;
    private final List<IEnumSetting>                 functionSettings             = new ArrayList<> (CommandCategory.values ().length);
    private final Map<CommandCategory, IEnumSetting> functionSettingsMap          = new EnumMap<> (CommandCategory.class);
    private IEnumSetting                             learnTypeSetting;
    private IEnumSetting                             learnNumberSetting;
    private IEnumSetting                             learnMidiChannelSetting;
    private IEnumSetting                             learnResolutionSetting;
    private IEnumSetting                             selectedModeSetting;
    private IStringSetting                           fileSetting;

    private final CommandSlot []                     commandSlots                 = new CommandSlot [NUM_SLOTS];

    private IValueObserver<FlexiCommand>             commandObserver;
    private String                                   filename;
    private final Object                             syncMapUpdate                = new Object ();
    private int []                                   keyMap;
    private int                                      selectedSlot                 = 0;

    private String                                   learnTypeValue               = null;
    private String                                   learnNumberValue             = null;
    private String                                   learnMidiChannelValue        = null;
    private boolean                                  learnResolution              = false;

    private final AtomicBoolean                      doNotFire                    = new AtomicBoolean (false);
    private final AtomicBoolean                      commandIsUpdating            = new AtomicBoolean (false);
    private final String []                          assignableFunctionActions    = new String [8];

    private String                                   selectedMode                 = MODES.get (0);

    private boolean                                  isMPEEnabled                 = false;
    private int                                      mpePitchBendRange            = 48;
    private int                                      keyboardChannel              = 0;
    private boolean                                  keyboardRouteTimbre          = false;
    private boolean                                  keyboardRouteModulation      = true;
    private boolean                                  keyboardRouteSustain         = true;
    private boolean                                  keyboardRoutePitchbend       = true;


    /**
     * Constructor.
     *
     * @param host The DAW host
     * @param valueChanger The value changer
     * @param arpeggiatorModes The available arpeggiator modes
     */
    public GenericFlexiConfiguration (final IHost host, final IValueChanger valueChanger, final List<ArpeggiatorMode> arpeggiatorModes)
    {
        super (host, valueChanger, arpeggiatorModes);

        Arrays.fill (this.assignableFunctionActions, "");

        this.dontNotifyAll.add (BUTTON_SAVE);
        this.dontNotifyAll.add (BUTTON_LOAD);
    }


    /** {@inheritDoc} */
    @Override
    public void init (final ISettingsUI globalSettings, final ISettingsUI documentSettings)
    {
        String category = "Slot";

        final String [] slotEntries = new String [NUM_SLOTS];
        for (int i = 0; i < NUM_SLOTS; i++)
        {
            this.commandSlots[i] = new CommandSlot ();
            slotEntries[i] = Integer.toString (i + 1);
        }

        this.slotSelectionSetting = globalSettings.getEnumSetting ("Selected:", category, slotEntries, slotEntries[0]);

        ///////////////////////////////////////////////
        // The MIDI learn section

        category = "Use a knob/fader/button then click Set...";

        this.learnTypeSetting = globalSettings.getEnumSetting ("Type:", category, OPTIONS_TYPE, OPTIONS_TYPE.get (0));
        this.learnNumberSetting = globalSettings.getEnumSetting ("Number:", category, NUMBER_NAMES, NUMBER_NAMES.get (0));
        this.learnMidiChannelSetting = globalSettings.getEnumSetting ("Midi Channel:", category, OPTIONS_MIDI_CHANNEL, OPTIONS_MIDI_CHANNEL[0]);
        this.learnResolutionSetting = globalSettings.getEnumSetting ("Resolution:", category, OPTIONS_RESOLUTION, OPTIONS_RESOLUTION.get (0));
        this.learnTypeSetting.setEnabled (false);
        this.learnNumberSetting.setEnabled (false);
        this.learnMidiChannelSetting.setEnabled (false);
        this.learnResolutionSetting.setEnabled (false);

        globalSettings.getSignalSetting (" ", category, "Set").addSignalObserver (value -> {
            if (this.learnTypeValue == null)
                return;
            this.typeSetting.set (this.learnTypeValue);
            this.midiChannelSetting.set (this.learnMidiChannelValue);

            // CC? For 14-bit values only set CCs below 32
            if (OPTIONS_TYPE.get (1).equals (this.learnTypeValue) && this.learnResolution)
            {
                final int number = AbstractConfiguration.lookupIndex (NUMBER_NAMES, this.learnNumberValue);
                if (number >= 32 && number < 64)
                    this.learnNumberValue = NUMBER_NAMES.get (number - 32);
                else if (number >= 64)
                    this.learnResolution = false;
            }

            this.resolutionSetting.set (OPTIONS_RESOLUTION.get (this.learnResolution ? 1 : 0));
            this.numberSetting.set (this.learnNumberValue);
        });

        ///////////////////////////////////////////////
        // Selected Slot - MIDI trigger

        category = "Selected Slot - MIDI trigger";

        this.typeSetting = globalSettings.getEnumSetting ("Type:", category, OPTIONS_TYPE, OPTIONS_TYPE.get (0));
        this.numberSetting = globalSettings.getEnumSetting ("Number:", category, NUMBER_NAMES, NUMBER_NAMES.get (0));
        this.midiChannelSetting = globalSettings.getEnumSetting ("Midi Channel:", category, CONTROLLER_CHANNELS, CONTROLLER_CHANNELS.get (0));
        this.resolutionSetting = globalSettings.getEnumSetting ("Resolution:", category, OPTIONS_RESOLUTION, OPTIONS_RESOLUTION.get (0));

        final String [] knobModeLabels = KnobMode.getLabels ();
        this.knobModeSetting = globalSettings.getEnumSetting ("Knob Mode:", category, knobModeLabels, knobModeLabels[0]);

        ///////////////////////////////////////////////
        // Selected Slot - MIDI device update

        category = "Selected Slot - MIDI device update";

        this.sendValueSetting = globalSettings.getEnumSetting ("Send value to device:", category, AbstractConfiguration.ON_OFF_OPTIONS, AbstractConfiguration.ON_OFF_OPTIONS[1]);
        this.sendValueWhenReceivedSetting = globalSettings.getEnumSetting ("Send value to device when received (only buttons):", category, AbstractConfiguration.ON_OFF_OPTIONS, AbstractConfiguration.ON_OFF_OPTIONS[1]);

        ///////////////////////////////////////////////
        // Selected Slot - Function

        category = "Selected Slot - Function";

        final CommandCategory [] values = CommandCategory.values ();
        for (final CommandCategory value: values)
        {
            final IEnumSetting fs = createFunctionSetting (value.getName (), category, globalSettings);
            this.functionSettings.add (fs);
            this.functionSettingsMap.put (value, fs);
            fs.addValueObserver (this::handleFunctionChange);
        }

        ///////////////////////////////////////////////
        // Export/Import section

        category = "Load / Save";

        // The Setlist file to auto-load
        this.fileSetting = globalSettings.getStringSetting ("Filename", category, -1, "");
        this.filename = this.fileSetting.get ();
        this.fileSetting.addValueObserver (value -> this.filename = value);

        // The different blank labels are necessary to distinguish the widgets!
        globalSettings.getSignalSetting ("  ", category, "Save").addSignalObserver (value -> this.notifyObservers (BUTTON_SAVE));
        globalSettings.getSignalSetting ("   ", category, "Load").addSignalObserver (value -> this.notifyObservers (BUTTON_LOAD));

        this.learnTypeSetting.set (OPTIONS_TYPE.get (0));

        this.typeSetting.addValueObserver (value -> {
            final int type = AbstractConfiguration.lookupIndex (OPTIONS_TYPE, value) - 1;
            this.getSelectedSlot ().setType (type);

            // High resolution is only true for pitchbend as the default
            final int number = AbstractConfiguration.lookupIndex (NUMBER_NAMES, this.numberSetting.get ());
            final boolean isHighRes = type == CommandSlot.TYPE_PITCH_BEND || type == CommandSlot.TYPE_CC && number < 32;
            this.resolutionSetting.set (OPTIONS_RESOLUTION.get (isHighRes ? 1 : 0));

            this.clearNoteMap ();
            this.updateVisibility (!OPTIONS_TYPE.get (0).equals (value));
        });
        this.numberSetting.addValueObserver (value -> {

            final int numberIndex = AbstractConfiguration.lookupIndex (NUMBER_NAMES, value);
            this.getSelectedSlot ().setNumber (numberIndex);

            // Switch resolution setting to low for CC >= 32
            final int type = AbstractConfiguration.lookupIndex (OPTIONS_TYPE, this.typeSetting.get ()) - 1;
            if (type == CommandSlot.TYPE_CC && numberIndex >= 32)
                this.resolutionSetting.set (OPTIONS_RESOLUTION.get (0));

            this.clearNoteMap ();
        });
        this.midiChannelSetting.addValueObserver (value -> {
            this.getSelectedSlot ().setMidiChannel (AbstractConfiguration.lookupIndex (CONTROLLER_CHANNELS, value));
            this.clearNoteMap ();
        });
        this.resolutionSetting.addValueObserver (value -> {

            final boolean isHighRes = OPTIONS_RESOLUTION.get (1).equals (value);
            this.getSelectedSlot ().setResolution (OPTIONS_RESOLUTION.get (1).equals (value));

            // High resolution can only be set for CC < 32 and pitchbend (fixed to high res)
            final int type = AbstractConfiguration.lookupIndex (OPTIONS_TYPE, this.typeSetting.get ()) - 1;
            if (isHighRes)
            {
                final int number = AbstractConfiguration.lookupIndex (NUMBER_NAMES, this.numberSetting.get ());
                if ((type != CommandSlot.TYPE_CC || number >= 32) && type != CommandSlot.TYPE_PITCH_BEND)
                    this.resolutionSetting.set (OPTIONS_RESOLUTION.get (0));
            }
            else
            {
                if (type == CommandSlot.TYPE_PITCH_BEND)
                    this.resolutionSetting.set (OPTIONS_RESOLUTION.get (1));
            }

        });
        this.knobModeSetting.addValueObserver (value -> {
            this.getSelectedSlot ().setKnobMode (KnobMode.lookupByLabel (value));
            this.fixKnobMode ();
        });
        this.sendValueSetting.addValueObserver (value -> this.getSelectedSlot ().setSendValue (AbstractConfiguration.lookupIndex (AbstractConfiguration.ON_OFF_OPTIONS, value) > 0));
        this.sendValueWhenReceivedSetting.addValueObserver (value -> this.getSelectedSlot ().setSendValueWhenReceived (AbstractConfiguration.lookupIndex (AbstractConfiguration.ON_OFF_OPTIONS, value) > 0));

        ///////////////////////////////////////////////
        // Keyboard / Pads

        final IEnumSetting enableMPESetting = globalSettings.getEnumSetting ("MIDI Polyphonic Expression (MPE)", CATEGORY_KEYBOARD, ON_OFF_OPTIONS, ON_OFF_OPTIONS[0]);
        this.isMPEEnabled = ON_OFF_OPTIONS[1].equals (enableMPESetting.get ());

        final IIntegerSetting pitchBendRangeSetting = globalSettings.getRangeSetting ("MPE Pitch Bend Sensitivity", CATEGORY_KEYBOARD, 1, 96, 1, "", 48);
        this.mpePitchBendRange = pitchBendRangeSetting.get ().intValue ();

        final IEnumSetting keyboardMidiChannelSetting = globalSettings.getEnumSetting ("Midi Channel", CATEGORY_KEYBOARD, KEYBOARD_CHANNELS, KEYBOARD_CHANNELS.get (1));
        this.keyboardChannel = AbstractConfiguration.lookupIndex (KEYBOARD_CHANNELS, keyboardMidiChannelSetting.get ()) - 1;

        final IEnumSetting routeTimbreSetting = globalSettings.getEnumSetting ("Route Timbre (CC74)", CATEGORY_KEYBOARD, AbstractConfiguration.ON_OFF_OPTIONS, AbstractConfiguration.ON_OFF_OPTIONS[0]);
        this.keyboardRouteTimbre = "On".equals (routeTimbreSetting.get ());

        final IEnumSetting routeModulationSetting = globalSettings.getEnumSetting ("Route Modulation (CC01)", CATEGORY_KEYBOARD, AbstractConfiguration.ON_OFF_OPTIONS, AbstractConfiguration.ON_OFF_OPTIONS[1]);
        this.keyboardRouteModulation = "On".equals (routeModulationSetting.get ());

        final IEnumSetting routeSustainSetting = globalSettings.getEnumSetting ("Route Sustain (CC64)", CATEGORY_KEYBOARD, AbstractConfiguration.ON_OFF_OPTIONS, AbstractConfiguration.ON_OFF_OPTIONS[1]);
        this.keyboardRouteSustain = "On".equals (routeSustainSetting.get ());

        final IEnumSetting routePitchbendSetting = globalSettings.getEnumSetting ("Route Pitchbend", CATEGORY_KEYBOARD, AbstractConfiguration.ON_OFF_OPTIONS, AbstractConfiguration.ON_OFF_OPTIONS[1]);
        this.keyboardRoutePitchbend = "On".equals (routePitchbendSetting.get ());

        enableMPESetting.addValueObserver (value -> {
            this.isMPEEnabled = ON_OFF_OPTIONS[1].equals (value);
            this.notifyObservers (ENABLED_MPE_ZONES);

            pitchBendRangeSetting.setEnabled (this.isMPEEnabled);
            keyboardMidiChannelSetting.setEnabled (!this.isMPEEnabled);
            routePitchbendSetting.setEnabled (!this.isMPEEnabled);
        });

        pitchBendRangeSetting.addValueObserver (value -> {
            this.mpePitchBendRange = value.intValue ();
            this.notifyObservers (MPE_PITCHBEND_RANGE);
        });

        ///////////////////////////////////////////////
        // Options

        this.selectedModeSetting = globalSettings.getEnumSetting ("Selected Mode", CATEGORY_OPTIONS, MODES, MODES.get (0));
        this.selectedModeSetting.addValueObserver (value -> {
            this.selectedMode = value;
            this.notifyObservers (SELECTED_MODE);
        });

        for (int i = 0; i < this.assignableFunctionActions.length; i++)
        {
            final int pos = i;
            final IActionSetting actionSetting = globalSettings.getActionSetting ("Action " + (i + 1), CATEGORY_OPTIONS);
            actionSetting.addValueObserver (value -> this.assignableFunctionActions[pos] = actionSetting.get ());
        }

        ///////////////////////////////////////////////
        // Workflow

        this.activateKnobSpeedSetting (globalSettings);
        this.activateExcludeDeactivatedItemsSetting (globalSettings);

        this.activateNoteRepeatSetting (documentSettings);

        this.slotSelectionSetting.addValueObserver (this::selectSlot);
    }


    /**
     * Handles changing the function selection by the user.
     *
     * @param value The new value
     */
    private void handleFunctionChange (final String value)
    {
        if (this.commandIsUpdating.get ())
            return;

        if (this.doNotFire.get ())
        {
            this.doNotFire.set (false);
            return;
        }

        final CommandSlot slot = this.getSelectedSlot ();
        final FlexiCommand oldCommand = slot.getCommand ();
        final FlexiCommand newCommand = FlexiCommand.lookupByName (value);
        slot.setCommand (newCommand);

        this.fixKnobMode ();
        this.notifyCommandObserver ();

        final CommandCategory oldCategory = oldCommand.getCategory ();
        if (oldCategory != null && oldCategory != newCommand.getCategory ())
        {
            this.doNotFire.set (true);
            this.functionSettingsMap.get (oldCategory).set (FlexiCommand.OFF.getName ());
        }
    }


    /**
     * Always set the knob mode to absolute for trigger commands.
     */
    private void fixKnobMode ()
    {
        final CommandSlot slot = this.getSelectedSlot ();
        final FlexiCommand command = slot.getCommand ();
        if (!command.isTrigger ())
            return;
        if (!AbstractHandler.isAbsolute (slot.getKnobMode ()) || slot.getType () == CommandSlot.TYPE_MMC)
            this.knobModeSetting.set (KnobMode.ABSOLUTE.getLabel ());
    }


    private CommandSlot getSelectedSlot ()
    {
        return this.commandSlots[this.selectedSlot];
    }


    /**
     * Set a received CC value.
     *
     * @param type The CC, Note or Program Change
     * @param number The number
     * @param midiChannel The MIDI channel
     * @param isHighRes True for 14-bit otherwise 7-bit
     */
    public void setLearnValues (final String type, final int number, final int midiChannel, final boolean isHighRes)
    {
        this.learnTypeValue = type;
        this.learnNumberValue = NUMBER_NAMES.get (number);
        this.learnMidiChannelValue = Integer.toString (midiChannel + 1);
        this.learnResolution = isHighRes;

        this.learnTypeSetting.set (type);
        this.learnNumberSetting.set (this.learnNumberValue);
        this.learnMidiChannelSetting.set (this.learnMidiChannelValue);
        this.learnResolutionSetting.set (OPTIONS_RESOLUTION.get (this.learnResolution ? 1 : 0));
    }


    /**
     * Get a matching configured slot command, if available.
     *
     * @param type The type
     * @param number The number
     * @param midiChannel The MIDI channel
     * @return The slot index or -1 if not found
     */
    public int getSlotCommand (final int type, final int number, final int midiChannel)
    {
        for (int i = 0; i < this.commandSlots.length; i++)
        {
            final CommandSlot slot = this.commandSlots[i];
            if (slot.getCommand () != FlexiCommand.OFF && slot.getType () == type && (type == CommandSlot.TYPE_PITCH_BEND || slot.getNumber () == number))
            {
                final int channel = slot.getMidiChannel ();
                if (channel == midiChannel || channel == 16)
                    return i;
            }
        }
        return -1;
    }


    /**
     * Get a matching configured slot, if available.
     *
     * @param type The type
     * @param number The number
     * @param midiChannel The MIDI channel
     * @return The slot or empty if not found
     */
    public Optional<Pair<Integer, CommandSlot>> getSlot (final int type, final int number, final int midiChannel)
    {
        for (int i = 0; i < this.commandSlots.length; i++)
        {
            final CommandSlot slot = this.commandSlots[i];
            if (slot.getCommand () != FlexiCommand.OFF && slot.getType () == type && (type == CommandSlot.TYPE_PITCH_BEND || slot.getNumber () == number))
            {
                final int channel = slot.getMidiChannel ();
                if (channel == midiChannel || channel == 16)
                    return Optional.of (new Pair<> (Integer.valueOf (i), slot));
            }
        }
        return Optional.empty ();
    }


    /**
     * Get a key translation map which blocks the notes that are mapped to a command from the
     * keyboard note input.
     *
     * @return The key translation map
     */
    public int [] getNoteMap ()
    {
        synchronized (this.syncMapUpdate)
        {
            if (this.keyMap == null)
            {
                this.keyMap = Scales.getIdentityMatrix ();
                for (final CommandSlot slot: this.commandSlots)
                {
                    if (slot.getCommand () == FlexiCommand.OFF || slot.getType () != CommandSlot.TYPE_NOTE)
                        continue;
                    final int midiChannel = slot.getMidiChannel ();
                    if (midiChannel == this.keyboardChannel || this.keyboardChannel == 16)
                        this.keyMap[slot.getNumber ()] = -1;
                }
            }
            return this.keyMap;
        }
    }


    /**
     * Clear the note map.
     */
    public void clearNoteMap ()
    {
        synchronized (this.syncMapUpdate)
        {
            this.keyMap = null;
        }
        this.notifyObservers (SLOT_CHANGE);
    }


    /**
     * Get all command slots.
     *
     * @return The slots
     */
    public CommandSlot [] getCommandSlots ()
    {
        return this.commandSlots;
    }


    /**
     * Get all commands which are used in a slot.
     *
     * @return The commands
     */
    public Set<FlexiCommand> getMappedCommands ()
    {
        final Set<FlexiCommand> commands = new HashSet<> ();
        for (final CommandSlot commandSlot: this.commandSlots)
        {
            final FlexiCommand cmd = commandSlot.getCommand ();
            if (cmd != null)
                commands.add (cmd);
        }
        return commands;
    }


    /**
     * Get the MPE state.
     *
     * @return True if MPE is enabled for the keyboard
     */
    public boolean isMPEEndabled ()
    {
        return this.isMPEEnabled;
    }


    /**
     * Get the MPE pitch bend range.
     *
     * @return The MPE Pitch bend range (1-96)
     */
    public int getMPEPitchBendRange ()
    {
        return this.mpePitchBendRange;
    }


    /**
     * Get the keyboard channel.
     *
     * @return -1 = off, 0-15 the MIDI channel, 16 = omni
     */
    public int getKeyboardChannel ()
    {
        return this.keyboardChannel;
    }


    /**
     * Should CC timbre directly routed to the DAW?
     *
     * @return True to route
     */
    public boolean isKeyboardRouteTimbre ()
    {
        return this.keyboardRouteTimbre;
    }


    /**
     * Should CC modulation directly routed to the DAW?
     *
     * @return True to route
     */
    public boolean isKeyboardRouteModulation ()
    {
        return this.keyboardRouteModulation;
    }


    /**
     * Should CC sustain directly routed to the DAW?
     *
     * @return True to route
     */
    public boolean isKeyboardRouteSustain ()
    {
        return this.keyboardRouteSustain;
    }


    /**
     * Should pitchbend directly routed to the DAW?
     *
     * @return True to route
     */
    public boolean isKeyboardRoutePitchbend ()
    {
        return this.keyboardRoutePitchbend;
    }


    /**
     * Get the file name.
     *
     * @return The file name
     */
    public String getFilename ()
    {
        return this.filename;
    }


    /**
     * Set the file name.
     *
     * @param filename The new file name
     */
    public void setFilename (final String filename)
    {
        this.fileSetting.set (filename);
    }


    /**
     * Gets the file with program names, if present. This is the stored properties file name with
     * the ending "programs".
     *
     * @return The file or null if not present
     */
    public Optional<FileEx> getProgramsFile ()
    {
        if (this.filename == null || this.filename.isBlank ())
            return Optional.empty ();

        final FileEx file = new FileEx (this.filename);
        final String name = file.getNameWithoutType ();
        final FileEx programsFile = new FileEx (file.getParent (), name + ".programs");
        final boolean exists = programsFile.exists ();
        this.host.println ("Scanning for: " + programsFile.getAbsolutePath () + " (" + (exists ? "present" : "not present") + ")");
        return exists ? Optional.of (programsFile) : Optional.empty ();
    }


    /**
     * Export the configuration to the given file.
     *
     * @param exportFile Where to export to
     * @throws IOException Could not save the file
     */
    public void exportTo (final File exportFile) throws IOException
    {
        final Properties props = new Properties ();
        for (int i = 0; i < this.commandSlots.length; i++)
        {
            final String slotName = "SLOT" + i + "_";
            final CommandSlot slot = this.commandSlots[i];
            props.put (slotName + TAG_TYPE, Integer.toString (slot.getType ()));
            props.put (slotName + TAG_NUMBER, Integer.toString (slot.getNumber ()));
            props.put (slotName + TAG_MIDI_CHANNEL, Integer.toString (slot.getMidiChannel ()));
            props.put (slotName + TAG_RESOLUTION, Boolean.toString (slot.getResolution ()));
            props.put (slotName + TAG_KNOB_MODE, Integer.toString (slot.getKnobMode ().ordinal ()));
            props.put (slotName + TAG_COMMAND, slot.getCommand ().getName ());
            props.put (slotName + TAG_SEND_VALUE, Boolean.toString (slot.isSendValue ()));
            props.put (slotName + TAG_SEND_VALUE_WHEN_RECEIVED, Boolean.toString (slot.isSendValueWhenReceived ()));
        }
        try (final Writer writer = new FileWriter (exportFile))
        {
            props.store (writer, "Generic Flexi");
        }
    }


    /**
     * Import the configuration from the given file.
     *
     * @param importFile Where to import from
     * @throws IOException Could not save the file
     */
    public void importFrom (final File importFile) throws IOException
    {
        try
        {
            final Properties props = new Properties ();
            try (final Reader reader = new FileReader (importFile))
            {
                props.load (reader);
            }

            for (int i = 0; i < this.commandSlots.length; i++)
            {
                final String slotName = "SLOT" + i + "_";
                final CommandSlot slot = this.commandSlots[i];

                final String typeProperty = props.getProperty (slotName + TAG_TYPE);
                if (typeProperty == null)
                    continue;

                int type = Integer.parseInt (typeProperty);

                final FlexiCommand command = FlexiCommand.lookupByName (props.getProperty (slotName + TAG_COMMAND));

                // For backwards compatibility
                if (command == FlexiCommand.OFF)
                    type = CommandSlot.TYPE_OFF;

                final String numberProperty = props.getProperty (slotName + TAG_NUMBER);
                final String midiChannelProperty = props.getProperty (slotName + TAG_MIDI_CHANNEL);
                final String knobModeProperty = props.getProperty (slotName + TAG_KNOB_MODE);

                slot.setType (type);
                slot.setNumber (numberProperty == null ? 0 : Integer.parseInt (numberProperty));
                slot.setMidiChannel (midiChannelProperty == null ? 0 : Integer.parseInt (midiChannelProperty));
                slot.setResolution (Boolean.parseBoolean (props.getProperty (slotName + TAG_RESOLUTION)));
                slot.setKnobMode (readKnobMode (knobModeProperty));
                slot.setCommand (command);
                slot.setSendValue (Boolean.parseBoolean (props.getProperty (slotName + TAG_SEND_VALUE)));
                slot.setSendValueWhenReceived (Boolean.parseBoolean (props.getProperty (slotName + TAG_SEND_VALUE_WHEN_RECEIVED)));
            }
        }
        catch (final IOException | NumberFormatException ex)
        {
            this.host.error ("Could not import from file.", ex);
            this.host.showNotification ("Could not import from file. Check Script Console for detailed error.");
            return;
        }

        this.clearNoteMap ();

        this.slotSelectionSetting.set ("1");
        this.selectSlot ("1");
    }


    /**
     * Sets the command observer.
     *
     * @param observer The observer
     */
    public void setCommandObserver (final IValueObserver<FlexiCommand> observer)
    {
        this.commandObserver = observer;
    }


    /**
     * Get the selected mode.
     *
     * @return The ID of the selected mode
     */
    public String getSelectedModeName ()
    {
        return this.selectedMode;
    }


    /**
     * Set the selected mode.
     *
     * @param selectedModeName The name of the selected mode
     */
    public void setSelectedMode (final String selectedModeName)
    {
        this.selectedModeSetting.set (selectedModeName);
    }


    /**
     * If the assignable function is set to Action this method gets the selected action to execute.
     *
     * @param index The index of the assignable
     * @return The ID of the action to execute
     */
    public String getAssignableAction (final int index)
    {
        return this.assignableFunctionActions[index];
    }


    private void selectSlot (final String value)
    {
        this.selectedSlot = Integer.parseInt (value) - 1;
        final CommandSlot slot = this.commandSlots[this.selectedSlot];

        this.setType (slot.getType ());
        this.setNumber (slot.getNumber ());
        this.setMidiChannel (slot.getMidiChannel ());
        this.setResolution (slot.getResolution ());
        this.setKnobMode (slot.getKnobMode ());
        this.setSendValue (slot.isSendValue ());
        this.setSendValueWhenReceived (slot.isSendValueWhenReceived ());
        this.setCommand (slot.getCommand ());
    }


    private void updateVisibility (final boolean visible)
    {
        this.numberSetting.setVisible (visible);
        this.midiChannelSetting.setVisible (visible);
        this.resolutionSetting.setVisible (visible);
        this.knobModeSetting.setVisible (visible);
        this.sendValueSetting.setVisible (visible);
        this.sendValueWhenReceivedSetting.setVisible (visible);
        for (final IEnumSetting fs: this.functionSettings)
            fs.setVisible (visible);
    }


    /**
     * Set the type.
     *
     * @param value The index
     */
    private void setType (final int value)
    {
        this.typeSetting.set (OPTIONS_TYPE.get (value + 1));
    }


    /**
     * Set the number.
     *
     * @param value The number
     */
    private void setNumber (final int value)
    {
        this.numberSetting.set (NUMBER_NAMES.get (value));
    }


    /**
     * Set the MIDI channel.
     *
     * @param value The index
     */
    private void setMidiChannel (final int value)
    {
        this.midiChannelSetting.set (CONTROLLER_CHANNELS.get (value));
    }


    /**
     * Set the MIDI resolution.
     *
     * @param isHighRes True for 14-bit
     */
    private void setResolution (final boolean isHighRes)
    {
        this.resolutionSetting.set (OPTIONS_RESOLUTION.get (isHighRes ? 1 : 0));
    }


    /**
     * Set the knob mode.
     *
     * @param value The index
     */
    private void setKnobMode (final KnobMode value)
    {
        this.knobModeSetting.set (value.getLabel ());
    }


    /**
     * Set the send value.
     *
     * @param value The boolean
     */
    private void setSendValue (final boolean value)
    {
        this.sendValueSetting.set (AbstractConfiguration.ON_OFF_OPTIONS[value ? 1 : 0]);
    }


    /**
     * Set the send value when received.
     *
     * @param value The boolean
     */
    private void setSendValueWhenReceived (final boolean value)
    {
        this.sendValueWhenReceivedSetting.set (AbstractConfiguration.ON_OFF_OPTIONS[value ? 1 : 0]);
    }


    /**
     * Set the command.
     *
     * @param value The command name
     */
    private void setCommand (final FlexiCommand value)
    {
        final CommandCategory category = value.getCategory ();
        final CommandCategory [] values = CommandCategory.values ();
        this.commandIsUpdating.set (true);
        for (int i = 0; i < values.length; i++)
            this.functionSettings.get (i).set (category == values[i] ? value.getName () : FlexiCommand.OFF.getName ());
        this.host.scheduleTask ( () -> this.commandIsUpdating.set (false), 600);
    }


    private void notifyCommandObserver ()
    {
        if (this.commandObserver != null)
            this.commandObserver.update (this.getSelectedSlot ().getCommand ());
    }


    private static IEnumSetting createFunctionSetting (final String functionCategory, final String settingCategory, final ISettingsUI settingsUI)
    {
        final List<String> functionsNames = new ArrayList<> ();
        functionsNames.add (FlexiCommand.OFF.getName ());
        for (final String name: NAMES)
        {
            if (name.startsWith (functionCategory))
                functionsNames.add (name);
        }
        final String [] array = functionsNames.toArray (new String [functionsNames.size ()]);
        return settingsUI.getEnumSetting (functionCategory + ":", settingCategory, array, array[0]);
    }


    private static KnobMode readKnobMode (final String knobModeProperty)
    {
        if (knobModeProperty == null)
            return KnobMode.ABSOLUTE;

        try
        {
            final int knobModeID = Integer.parseInt (knobModeProperty);
            final KnobMode [] knobModeValues = KnobMode.values ();
            return knobModeID < knobModeValues.length ? knobModeValues[knobModeID] : KnobMode.ABSOLUTE;
        }
        catch (final NumberFormatException ex)
        {
            return KnobMode.ABSOLUTE;
        }
    }
}
