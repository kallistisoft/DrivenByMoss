// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2021
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.framework.parameterprovider.track;

import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.data.bank.ITrackBank;
import de.mossgrabers.framework.observer.IBankPageObserver;
import de.mossgrabers.framework.observer.IParametersAdjustObserver;
import de.mossgrabers.framework.observer.IValueObserver;
import de.mossgrabers.framework.parameterprovider.AbstractParameterProvider;


/**
 * Get a number of parameters. This abstract implementation provides a parameter of the current or
 * the given track bank.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public abstract class AbstractTrackParameterProvider extends AbstractParameterProvider implements IBankPageObserver, IValueObserver<ITrackBank>
{
    protected final IModel model;
    protected ITrackBank   bank;


    /**
     * Constructor. Monitors the given bank.
     *
     * @param bank The bank from which to get the parameters
     */
    public AbstractTrackParameterProvider (final ITrackBank bank)
    {
        this.bank = bank;
        this.model = null;
    }


    /**
     * Constructor. Monitors the track and effect track banks as well as switching between them.
     *
     * @param model Uses the current track bank from this model to get the parameters
     */
    public AbstractTrackParameterProvider (final IModel model)
    {
        this.model = model;
        this.bank = model.getCurrentTrackBank ();
    }


    /** {@inheritDoc} */
    @Override
    public int size ()
    {
        return this.bank.getPageSize ();
    }


    /** {@inheritDoc} */
    @Override
    public void addParametersObserver (final IParametersAdjustObserver observer)
    {
        super.addParametersObserver (observer);

        if (this.model == null)
        {
            this.bank.addPageObserver (this);
            return;
        }

        // Monitor the instrument/audio and effect track banks
        this.model.getTrackBank ().addPageObserver (this);
        final ITrackBank effectTrackBank = this.model.getEffectTrackBank ();
        if (effectTrackBank != null)
            effectTrackBank.addPageObserver (this);

        // Monitor switching between the instrument/audio and effect track banks
        this.model.addTrackBankObserver (this);
    }


    /** {@inheritDoc} */
    @Override
    public void removeParametersObserver (final IParametersAdjustObserver observer)
    {
        super.removeParametersObserver (observer);

        if (this.hasObservers ())
            return;

        if (this.model == null)
        {
            this.bank.removePageObserver (this);
            return;
        }

        this.model.getTrackBank ().removePageObserver (this);
        final ITrackBank effectTrackBank = this.model.getEffectTrackBank ();
        if (effectTrackBank != null)
            effectTrackBank.removePageObserver (this);

        this.model.removeTrackBankObserver (this);
    }


    /** {@inheritDoc} */
    @Override
    public void pageAdjusted ()
    {
        this.notifyParametersObservers ();
    }


    /** {@inheritDoc} */
    @Override
    public void update (final ITrackBank bank)
    {
        this.bank = bank;

        this.notifyParametersObservers ();
    }
}
