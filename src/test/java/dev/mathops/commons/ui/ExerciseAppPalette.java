package dev.mathops.commons.ui;

import dev.mathops.commons.CoreConstants;
import dev.mathops.commons.ui.layout.StackedBorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.FlowLayout;

/**
 * Exercises the {@code AppPalette} class.
 */
public final class ExerciseAppPalette implements Runnable {

    private ExerciseAppPalette() {

        // No action
    }

    /**
     * Constructs the UI in the event dispatch thread.
     */
    public void run() {

        final JFrame frame = new JFrame("AppPalette Exerciser");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final JPanel content = new JPanel(new StackedBorderLayout());
        frame.setContentPane(content);
        final Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        content.setBorder(border);

        final Color origBg = content.getBackground();
        final AppPalette palette = new AppPalette(origBg, EAppPalette.BASIC);

        final Color bg = palette.getBackground();
        final Color bg1 = palette.getAccentedBackground1();
        final Color bg2 = palette.getAccentedBackground2();
        final Color fg = palette.getForeground();
        final Color accent1 = palette.getAccent1();
        final Color accent2 = palette.getAccent2();
        final Color accent3 = palette.getAccent3();
        final Color accent4 = palette.getAccent4();
        final Color accent5 = palette.getAccent5();

        content.setBackground(bg);

        final JPanel flow1 = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 4));
        final JLabel foregroundLabel = new JLabel("This is the foreground color.");
        foregroundLabel.setForeground(fg);
        flow1.add(foregroundLabel);
        content.add(flow1, StackedBorderLayout.NORTH);

        final JPanel flow2 = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 4));
        final JLabel accent1Label = new JLabel("This is accent color 1.");
        accent1Label.setForeground(accent1);
        flow2.add(accent1Label);
        content.add(flow2, StackedBorderLayout.NORTH);

        final JPanel flow3 = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 4));
        final JLabel accent2Label = new JLabel("This is accent color 2 (errors).");
        accent2Label.setForeground(accent2);
        flow3.add(accent2Label);
        content.add(flow3, StackedBorderLayout.NORTH);

        final JPanel flow4 = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 4));
        final JLabel accent3Label = new JLabel("This is accent color 3 (math).");
        accent3Label.setForeground(accent3);
        flow4.add(accent3Label);
        content.add(flow4, StackedBorderLayout.NORTH);

        final JPanel flow5 = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 4));
        final JLabel accent4Label = new JLabel("This is accent color 4 (success).");
        accent4Label.setForeground(accent4);
        flow5.add(accent4Label);
        content.add(flow5, StackedBorderLayout.NORTH);

        final JPanel flow6 = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 4));
        final JLabel accent5Label = new JLabel("This is accent color 5.");
        accent5Label.setForeground(accent5);
        flow6.add(accent5Label);
        content.add(flow6, StackedBorderLayout.NORTH);

        final Border etch = BorderFactory.createEtchedBorder();
        final JPanel inset1 = new JPanel(new StackedBorderLayout());
        inset1.setBackground(bg1);
        inset1.setBorder(etch);

        final JPanel flow7 = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 10));
        flow7.setBackground(palette.getAccentedBackground1());
        final JLabel inset1Label1 = new JLabel("  This ");
        inset1Label1.setForeground(fg);
        flow7.add(inset1Label1);
        final JLabel inset1Label2 = new JLabel("panel ");
        inset1Label2.setForeground(palette.getAccent1());
        flow7.add(inset1Label2);
        final JLabel inset1Label3 = new JLabel("has ");
        inset1Label3.setForeground(palette.getAccent2());
        flow7.add(inset1Label3);
        final JLabel inset1Label4 = new JLabel("accented ");
        inset1Label4.setForeground(palette.getAccent3());
        flow7.add(inset1Label4);
        final JLabel inset1Label5 = new JLabel("background ");
        inset1Label5.setForeground(palette.getAccent4());
        flow7.add(inset1Label5);
        final JLabel inset1Label6 = new JLabel("color 1.  ");
        inset1Label6.setForeground(palette.getAccent5());
        flow7.add(inset1Label6);

        inset1.add(flow7, StackedBorderLayout.NORTH);
        content.add(inset1, StackedBorderLayout.NORTH);

        final JLabel spacer = new JLabel(CoreConstants.SPC);
        content.add(spacer, StackedBorderLayout.NORTH);

        final JPanel inset2 = new JPanel(new StackedBorderLayout());
        inset2.setBackground(bg2);
        inset2.setBorder(etch);

        final JPanel flow8 = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 10));
        flow8.setBackground(palette.getAccentedBackground2());
        final JLabel inset2Label1 = new JLabel("  This ");
        inset2Label1.setForeground(fg);
        flow8.add(inset2Label1);
        final JLabel inset2Label2 = new JLabel("panel ");
        inset2Label2.setForeground(palette.getAccent1());
        flow8.add(inset2Label2);
        final JLabel inset2Label3 = new JLabel("has ");
        inset2Label3.setForeground(palette.getAccent2());
        flow8.add(inset2Label3);
        final JLabel inset2Label4 = new JLabel("accented ");
        inset2Label4.setForeground(palette.getAccent3());
        flow8.add(inset2Label4);
        final JLabel inset2Label5 = new JLabel("background ");
        inset2Label5.setForeground(palette.getAccent4());
        flow8.add(inset2Label5);
        final JLabel inset2Label6 = new JLabel("color 1.  ");
        inset2Label6.setForeground(palette.getAccent5());
        flow8.add(inset2Label6);
        inset2.add(flow8, StackedBorderLayout.NORTH);
        content.add(inset2, StackedBorderLayout.NORTH);

        UIUtilities.packAndCenter(frame);
        frame.setVisible(true);
    }

    /**
     * Main method to launch the application
     *
     */
    static void main() {

        SwingUtilities.invokeLater(new ExerciseAppPalette());
    }
}
