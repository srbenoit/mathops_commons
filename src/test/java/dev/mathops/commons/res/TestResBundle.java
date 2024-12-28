package dev.mathops.commons.res;

import dev.mathops.commons.CoreConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the ResBundle class.
 */
final class TestResBundle extends ResBundle {

    /** A frequently used string. */
    private static final String HELLO_STR = "Hello {0}";

    /** A frequently used string. */
    private static final String YES_STR = "Yes";

    /** A frequently used string. */
    private static final String HELLO_YES_STR = "Hello Yes";

    /** A frequently used string. */
    private static final String HOLA_STR = "Hola {0}";

    /** A frequently used string. */
    private static final String SI_STR = "Si";

    /** A frequently used string. */
    private static final String HOLA_SI_STR = "Hola Si";

    /** A frequently used string. */
    private static final String HALLO_STR = "Hallo {0}";

    /** A frequently used string. */
    private static final String JA_STR = "Ja";

    /** A frequently used string. */
    private static final String HALLO_JA_STR = "Hallo Ja";

    /** A frequently used string. */
    private static final String ENGLISH = "en";

    /** A frequently used string. */
    private static final String SPANISH = "es";

    /** A frequently used string. */
    private static final String US = "US";

    /** A frequently used string. */
    private static final String SPAIN = "SP";

    /** A frequently used string. */
    private static final String GERMAN = "de";

    /** A frequently used string. */
    private static final String VARIANT = "qux";

    /** A frequently used string. */
    private static final String FRENCH = "fr";

    /** A frequently used string. */
    private static final String LA = "la";

    /** A frequently used string. */
    private static final String A = "a";

    /** A frequently used string. */
    private static final String B = "b";

    /** A message key. */
    private static final String HELLO = key(0);

    /** A message key. */
    private static final String YES = key(1);

    /** A message key. */
    private static final String MISSING = key(2);

    /** A common string. */
    private static final String UNUSED = "unused";

    /** The resources - an array of key-values pairs. */
    private static final String[][] EN_US = {{HELLO, HELLO_STR}, {YES, YES_STR}};

    /** The resources - an array of key-values pairs. */
    private static final String[][] ES_ALL = {{HELLO, HOLA_STR}, {YES, SI_STR}};

    /** The resources - an array of key-values pairs. */
    private static final String[][] DE_ALL = {{HELLO, HALLO_STR}, {YES, JA_STR}};

    /**
     * Constructs a new {@code IvtResBundle}.
     */
    TestResBundle() {

        super(Locale.US, EN_US);

        final Locale spanishLocale = Locale.of(SPANISH);
        addMessages(spanishLocale, ES_ALL);

        final Locale germanLocale = Locale.of(GERMAN);
        addMessages(germanLocale, DE_ALL);
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("Exception on missing Locale")
    void test001() {

        final String[][] junk = new String[1][];
        junk[0] = new String[]{A, B};

        assertThrows(IllegalArgumentException.class, () -> {
            addMessages(null, junk);
        }, "No exception on missing Locale");
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("Exception on null message array")
    void test002() {

        assertThrows(IllegalArgumentException.class, () -> {
                    final Locale frenchLocale = Locale.of(FRENCH);
                    addMessages(frenchLocale, null);
                },
                "No exception on null message arrays");
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("Exception on null message sub-array")
    void test003() {

        final String[][] junk = new String[1][];

        assertThrows(IllegalArgumentException.class, () -> {
                    final Locale frenchLocale = Locale.of(FRENCH);
                    addMessages(frenchLocale, junk);
                },
                "No exception on null message sub-array");
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("Exception on message sub-array of length 3")
    void test004() {

        final String[][] junk = new String[1][];
        junk[0] = new String[3];

        assertThrows(IllegalArgumentException.class, () -> {
                    final Locale frenchLocale = Locale.of(FRENCH);
                    addMessages(frenchLocale, junk);
                },
                "No exception on message sub-array of length 3");
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("Exception on null messages")
    void test005() {

        final String[][] junk = new String[1][];
        junk[0] = new String[2];

        assertThrows(IllegalArgumentException.class, () -> {
                    final Locale frenchLocale = Locale.of(FRENCH);
                    addMessages(frenchLocale, junk);
                },
                "No exception on null messages");
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("Exception on null message 1")
    void test006() {

        final String[][] junk = new String[1][];
        junk[0] = new String[2];
        junk[0][0] = A;

        assertThrows(IllegalArgumentException.class, () -> {
                    final Locale frenchLocale = Locale.of(FRENCH);
                    addMessages(frenchLocale, junk);
                },
                "No exception on null message 1");
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("Exception on null message 0")
    void test007() {

        final String[][] junk = new String[1][];
        junk[0] = new String[2];
        junk[0][1] = A;

        assertThrows(IllegalArgumentException.class, () -> {
                    final Locale frenchLocale = Locale.of(FRENCH);
                    addMessages(frenchLocale, junk);
                },
                "No exception on null message 0");
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("getMsg() for 'en', 'US', 'qux'")
    void test008() {

        final Locale def = Locale.getDefault();
        final Locale locale = Locale.of(ENGLISH, US, VARIANT);
        Locale.setDefault(locale);

        final String helloMsg = getMsg(HELLO);
        assertEquals(HELLO_STR, helloMsg, "GetEnUsQux Hello");

        final String yesMsg = getMsg(YES);
        assertEquals(YES_STR, yesMsg, "GetEnUsQux Yes");

        final String missingMsg = getMsg(MISSING);
        assertEquals(CoreConstants.EMPTY, missingMsg, "GetEnUsQux Empty");

        Locale.setDefault(def);
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("getMsg() for 'en', 'US'")
    void test009() {

        final Locale def = Locale.getDefault();
        Locale.setDefault(Locale.US);

        final String helloMsg = getMsg(HELLO);
        assertEquals(HELLO_STR, helloMsg, "GetEnUs Hello");
        final String yesMsg = getMsg(YES);
        assertEquals(YES_STR, yesMsg, "GetEnUs Yes");
        final String missingMsg = getMsg(MISSING);
        assertEquals(CoreConstants.EMPTY, missingMsg, "GetEnUs Empty");

        Locale.setDefault(def);
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("getMsg() for 'en'")
    void test010() {

        final Locale def = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        final String helloMsg = getMsg(HELLO);
        assertEquals(HELLO_STR, helloMsg, "GetEn Hello");
        final String yesMsg = getMsg(YES);
        assertEquals(YES_STR, yesMsg, "GetEn Yes");
        final String missingMsg = getMsg(MISSING);
        assertEquals(CoreConstants.EMPTY, missingMsg, "GetEn Empty");

        Locale.setDefault(def);
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("getMsg() for 'es', 'SP', 'qux'")
    void test011() {

        final Locale def = Locale.getDefault();
        final Locale locale = Locale.of(SPANISH, SPAIN, VARIANT);
        Locale.setDefault(locale);

        final String helloMsg = getMsg(HELLO);
        assertEquals(HOLA_STR, helloMsg, "GetEsSpQux Hello");
        final String yesMsg = getMsg(YES);
        assertEquals(SI_STR, yesMsg, "GetEsSpQux Yes");
        final String missingMsg = getMsg(MISSING);
        assertEquals(CoreConstants.EMPTY, missingMsg, "GetEsSpQux Empty");

        Locale.setDefault(def);
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("getMsg() for 'es', 'SP'")
    void test012() {

        final Locale def = Locale.getDefault();
        final Locale locale = Locale.of(SPANISH, SPAIN);
        Locale.setDefault(locale);

        final String helloMsg = getMsg(HELLO);
        assertEquals(HOLA_STR, helloMsg, "GetEsSp Hello");
        final String yesMsg = getMsg(YES);
        assertEquals(SI_STR, yesMsg, "GetEsSp Yes");
        final String missingMsg = getMsg(MISSING);
        assertEquals(CoreConstants.EMPTY, missingMsg, "GetEsSp Empty");

        Locale.setDefault(def);
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("getMsg() for 'es'")
    void test013() {

        final Locale def = Locale.getDefault();
        final Locale locale = Locale.of(SPANISH);
        Locale.setDefault(locale);

        final String helloMsg = getMsg(HELLO);
        assertEquals(HOLA_STR, helloMsg, "GetEs Hello");
        final String yesMsg = getMsg(YES);
        assertEquals(SI_STR, yesMsg, "GetEs Yes");
        final String missingMsg = getMsg(MISSING);
        assertEquals(CoreConstants.EMPTY, missingMsg, "GetEs Empty");

        Locale.setDefault(def);
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("getMsg() for 'de'")
    void test014() {

        final Locale def = Locale.getDefault();
        final Locale locale = Locale.of(GERMAN);
        Locale.setDefault(locale);

        final String helloMsg = getMsg(HELLO);
        assertEquals(HALLO_STR, helloMsg, "GetDe Hello");
        final String yesMsg = getMsg(YES);
        assertEquals(JA_STR, yesMsg, "GetDe Yes");
        final String missingMsg = getMsg(MISSING);
        assertEquals(CoreConstants.EMPTY, missingMsg, "GetDe Empty");

        Locale.setDefault(def);
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("getMsg() for 'la'")
    void test015() {

        final Locale def = Locale.getDefault();
        final Locale locale = Locale.of(LA);
        Locale.setDefault(locale);

        final String helloMsg = getMsg(HELLO);
        assertEquals(HELLO_STR, helloMsg, "GetLa Hello");
        final String yesMsg = getMsg(YES);
        assertEquals(YES_STR, yesMsg, "GetLa Yes");
        final String missingMsg = getMsg(MISSING);
        assertEquals(CoreConstants.EMPTY, missingMsg, "GetLa Empty");

        Locale.setDefault(def);
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("formatMsg() for 'en', 'US', 'qux'")
    void test016() {

        final Locale def = Locale.getDefault();
        final Locale locale = Locale.of(ENGLISH, US, VARIANT);
        Locale.setDefault(locale);

        final String helloMsg = formatMsg(HELLO, YES_STR);
        assertEquals(HELLO_YES_STR, helloMsg, "FmtEnUsQux Hello Yes");

        Locale.setDefault(def);
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("formatMsg() for 'en', 'US'")
    void test017() {

        final Locale def = Locale.getDefault();
        Locale.setDefault(Locale.US);

        final String helloMsg = formatMsg(HELLO, YES_STR);
        assertEquals(HELLO_YES_STR, helloMsg, "FmtEnUs Hello Yes");

        Locale.setDefault(def);
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("formatMsg() for 'en'")
    void test018() {

        final Locale def = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        final String helloMsg = formatMsg(HELLO, YES_STR);
        assertEquals(HELLO_YES_STR, helloMsg, "FmtEn Hello Yes");

        Locale.setDefault(def);
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("formatMsg() for 'es', 'SP', 'qux'")
    void test019() {

        final Locale def = Locale.getDefault();
        final Locale locale = Locale.of(SPANISH, SPAIN, VARIANT);
        Locale.setDefault(locale);

        final String helloMsg = formatMsg(HELLO, SI_STR);
        assertEquals(HOLA_SI_STR, helloMsg, "FmtEsSpQux Hola Si");

        Locale.setDefault(def);
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("formatMsg() for 'es', 'SP'")
    void test020() {

        final Locale def = Locale.getDefault();
        final Locale locale = Locale.of(SPANISH, SPAIN);
        Locale.setDefault(locale);

        final String helloMsg = formatMsg(HELLO, SI_STR);
        assertEquals(HOLA_SI_STR, helloMsg, "FmtEsSp Hola Si");

        Locale.setDefault(def);
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("formatMsg() for 'es'")
    void test021() {

        final Locale def = Locale.getDefault();
        final Locale locale = Locale.of(SPANISH);
        Locale.setDefault(locale);

        final String helloMsg = formatMsg(HELLO, SI_STR);
        assertEquals(HOLA_SI_STR, helloMsg, "FmtEs Hola Si");

        Locale.setDefault(def);
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("formatMsg() for 'de'")
    void test022() {

        final Locale def = Locale.getDefault();
        final Locale locale = Locale.of(GERMAN);
        Locale.setDefault(locale);

        final String helloMsg = formatMsg(HELLO, JA_STR);
        assertEquals(HALLO_JA_STR, helloMsg, "FmtDe Hallo Ja");

        Locale.setDefault(def);
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("formatMsg() for 'la'")
    void test023() {

        final Locale def = Locale.getDefault();
        final Locale locale = Locale.of(LA);
        Locale.setDefault(locale);

        final String helloMsg = formatMsg(HELLO, YES_STR);
        assertEquals(HELLO_YES_STR, helloMsg, "FmtLa Hello Yes");

        Locale.setDefault(def);
    }
}
