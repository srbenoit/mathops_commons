package dev.mathops.commons.ui;

import dev.mathops.commons.log.Log;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * A convenience class to house the static code to change to the Nimbus UI.
 */
@Deprecated
public final class ChangeUI {

    /**
     * Private constructor to prevent instantiation.
     */
    private ChangeUI() {

        super();
    }

    /**
     * Sets the UI to the Nimbus UI.
     */
    @Deprecated
    public static void changeUI() {

        final UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        UIManager.LookAndFeelInfo selected = null;
        for (final UIManager.LookAndFeelInfo test : lafs) {
            final String name = test.getName();
            if ("Nimbus".equals(name)) {
                selected = test;
                break;
            }
        }

        if (selected != null) {
            try {
                final String className = selected.getClassName();
                UIManager.setLookAndFeel(className);
            } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException
                           | UnsupportedLookAndFeelException ex) {
                Log.warning(ex);
            }
        }
    }
}
