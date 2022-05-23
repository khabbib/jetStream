
package worldMapAPI;

import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.SVGPath;

import java.util.Locale;

public class CountryPath extends SVGPath {
    private final String  NAME;
    private final Locale  LOCALE;
    private final Tooltip TOOLTIP;


    // ******************** Constructors **************************************
    public CountryPath(final String NAME) {
        this(NAME, null);
    }
    public CountryPath(final String NAME, final String CONTENT) {
        super();
        this.NAME    = NAME;
        this.LOCALE  = new Locale("", NAME);
        this.TOOLTIP = new Tooltip(LOCALE.getDisplayCountry());
        Tooltip.install(this, TOOLTIP);
        if (null == CONTENT) return;
        setContent(CONTENT);
    }

    public CountryPath(final String NAME, final String CONTENT, boolean b) {
        super();
        this.NAME    = NAME;
        this.LOCALE  = new Locale("", NAME);
        TOOLTIP = null;
        if (null == CONTENT) return;
        setContent(CONTENT);
    }


    // ******************** Methods *******************************************
    public String getName() { return NAME; }

    public Locale getLocale() { return LOCALE; }

    public Tooltip getTooltip() { return TOOLTIP; }
}
