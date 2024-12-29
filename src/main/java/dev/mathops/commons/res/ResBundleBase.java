package dev.mathops.commons.res;

import dev.mathops.commons.CoreConstants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A base class for resource bundles the handles storage or resource messages and locales.
 */
class ResBundleBase {

    /** The language string for English (fallback if no matching language). */
    private static final String ENGLISH = "en";

    /** Map from locale to the list of messages. */
    private final Map<Locale, Map<String, String>> messageMap;

    /** Map from request locale to best match locale. */
    private final Map<Locale, Locale> localeMap;

    /**
     * Constructs a new {@code ResBundleBase}.
     */
    ResBundleBase() {

        this.messageMap = new HashMap<>(1);
        this.localeMap = new HashMap<>(1);
    }

    /**
     * Adds messages to the message map.
     *
     * @param locale   the locale under which to register the messages
     * @param messages an array with dimensions [N][2] of {@code String} key/message pairs
     */
    protected final void addMessages(final Locale locale, final String[][] messages) {

        if (locale == null) {
            final String msg = Res.get(Res.NULL_LOCALE);
            throw new IllegalArgumentException(msg);
        }
        if (messages == null) {
            final String msg = Res.get(Res.BAD_ARRAY);
            throw new IllegalArgumentException(msg);
        }

        // Validate message array before allocating a map
        for (final String[] message : messages) {
            if (message == null || message.length != 2 || message[0] == null || message[1] == null) {
                final String msg = Res.get(Res.BAD_ARRAY);
                throw new IllegalArgumentException(msg);
            }
        }

        final ConcurrentMap<String, String> map = new ConcurrentHashMap<>(messages.length);

        for (final String[] message : messages) {
            map.put(message[0], message[1]);
        }

        this.messageMap.put(locale, map);
        this.localeMap.clear();
    }

    /**
     * Gets the message with a specified key using a specified locale.
     *
     * @param key    the message key
     * @param locale the desired {@code Locale}
     * @return the best-matching message, an empty string if none is registered (never {@code null})
     */
    final String getMsg(final String key, final Locale locale) {

        final Locale best = bestLocale(locale);
        final String msg = best == null ? null : this.messageMap.get(best).get(key);

        return msg == null ? CoreConstants.EMPTY : msg;
    }

    /**
     * Chooses the registered {@code Locale} that best matches the current default {@code Locale}.
     *
     * @param locale the desired {@code Locale}
     * @return the best matching {@code Locale}, {@code null} if no locales have been registered
     */
    private Locale bestLocale(final Locale locale) {

        // Identify the best matching Locale from the set of registered locales
        Locale best = this.localeMap.get(locale);

        if (best == null) {

            best = matchLangCountryVar(locale);

            if (best == null) {
                best = matchLangCountry(locale);
            }

            if (best == null) {
                best = matchLang(locale);
            }

            if (best == null) {
                best = matchNone();
            }

            if (best != null) {
                this.localeMap.put(locale, best);
            }
        }

        return best;
    }

    /**
     * Searches for a registered {@code Locale} that matches the language, country, and variant of the default locale.
     *
     * @param defLoc the default locale
     * @return the matching {@code Locale} if found, {@code null} if none found
     */
    private Locale matchLangCountryVar(final Locale defLoc) {

        Locale match = null;

        for (final Locale loc : this.messageMap.keySet()) {
            final String defaultLanguage = defLoc.getLanguage();
            if (loc.getLanguage().equals(defaultLanguage)) {

                final String defaultCountry = defLoc.getCountry();
                final String locVariant = loc.getVariant();
                final String defaultVariant = defLoc.getVariant();

                if (loc.getCountry().equals(defaultCountry) && Objects.equals(locVariant, defaultVariant)) {
                    match = loc;
                    break;
                }
            }
        }

        return match;
    }

    /**
     * Searches for a registered {@code Locale} that matches the language and country of the default locale.
     *
     * @param defLoc the default locale
     * @return the matching {@code Locale} if found, {@code null} if none found
     */
    private Locale matchLangCountry(final Locale defLoc) {

        Locale match = null;

        for (final Locale loc : this.messageMap.keySet()) {
            final String defaultLanguage = defLoc.getLanguage();
            final String country = loc.getCountry();
            final String defaultCountry = defLoc.getCountry();

            if (loc.getLanguage().equals(defaultLanguage) && Objects.equals(country, defaultCountry)) {
                match = loc;
                break;
            }
        }

        return match;
    }

    /**
     * Searches for a registered {@code Locale} that matches the language of the default locale.
     *
     * @param defLoc the default locale
     * @return the matching {@code Locale} if found, {@code null} if none found
     */
    private Locale matchLang(final Locale defLoc) {

        Locale match = null;

        for (final Locale loc : this.messageMap.keySet()) {
            final String defaultLanguage = defLoc.getLanguage();
            if (loc.getLanguage().equals(defaultLanguage)) {
                match = loc;
                break;
            }
        }

        return match;
    }

    /**
     * Finds an arbitrary language since none match the default locale. English is preferred in the absence of other
     * information.
     *
     * @return the matching {@code Locale} if found, {@code null} if none found
     */
    private Locale matchNone() {

        Locale match = null;

        for (final Locale loc : this.messageMap.keySet()) {
            final String language = loc.getLanguage();
            if (ENGLISH.equals(language)) {
                match = loc;
                break;
            }
        }

        if (match == null) {
            final Iterator<Locale> iterator = this.messageMap.keySet().iterator();
            if (iterator.hasNext()) {
                match = iterator.next();
            }
        }

        return match;
    }
}
