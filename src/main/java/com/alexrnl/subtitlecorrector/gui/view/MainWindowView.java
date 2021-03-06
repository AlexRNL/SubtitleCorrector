package com.alexrnl.subtitlecorrector.gui.view;

import static com.alexrnl.subtitlecorrector.common.TranslationKeys.KEYS;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.alexrnl.commons.gui.swing.AbstractFrame;
import com.alexrnl.commons.io.IOUtils;
import com.alexrnl.commons.translation.Translator;
import com.alexrnl.subtitlecorrector.correctionstrategy.Parameter;
import com.alexrnl.subtitlecorrector.correctionstrategy.Strategy;
import com.alexrnl.subtitlecorrector.gui.controller.MainWindowController;

/**
 * The view of the main window.
 * @author Alex
 */
public class MainWindowView extends AbstractFrame {
	/** Logger */
	private static final Logger		LG						= Logger.getLogger(MainWindowView.class.getName());
	
	/** Serial version UID */
	private static final long		serialVersionUID		= -6742939741527280064L;
	
	/** The default insets to use in the window */
	private static final Insets		DEFAULT_INSETS			= new Insets(5, 5, 5, 5);
	/** Minimum size for the text box */
	private static final int		MINIMUM_TEXT_BOX_SIZE	= 18;
	
	// GUI elements
	/** The field where the path of the subtitle will be displayed */
	private JTextField				subtitleField;
	/** The button to open the file explorer window to select the subtitle */
	private JButton					subtitleButton;
	/** The combo box for the strategy */
	private JComboBox<String>		strategyComboBox;
	/** The button to start the correction */
	private JButton					startCorrectingButton;
	/** The check box for the overwrite option */
	private JCheckBox				overwriteCheckbox;
	/** The panel with the parameters of the strategy */
	private JPanel					strategyParameterPanel;
	/** The combo box for the locale parameter */
	private JComboBox<Locale>		localeComboBox;
	/** Map with the strategy parameters components */
	private Map<String, StrategyParameterComponent>	strategyParameters;
	
	/** The controller in charge of the view */
	private MainWindowController	controller;
	/** The translator to use for the view */
	private Translator				translator;
	/** The factory for the component representing strategy parameters */
	private StrategyParameterComponentFactory	parameterComponentFactory;
	
	/**
	 * Constructor #1.<br />
	 * @param iconFile
	 *        the icon file to use for the main window.
	 * @param controller
	 *        the controller which handle this view.
	 * @param translator
	 *        the translator to use for the GUI.
	 */
	public MainWindowView (final Path iconFile, final MainWindowController controller,
			final Translator translator) {
		super(translator.get(KEYS.gui().mainWindow().title()), iconFile, controller, translator);
	}
	
	@Override
	protected void preInit (final Object... parameters) {
		controller = (MainWindowController) parameters[0];
		translator = (Translator) parameters[1];
		parameterComponentFactory = new StrategyParameterComponentFactory();
	}
	
	@Override
	protected void build () {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		buildMainContainer();
		installListeners();
	}
	
	/**
	 * Build the main container of the main window.
	 */
	private void buildMainContainer () {
		final Container panel = getContentPane();
		panel.setLayout(new GridBagLayout());
		
		int xIndex = 0;
		int yIndex = 0;
		final GridBagConstraints c = new GridBagConstraints(xIndex, yIndex, 1, 1, 0, 0,
				GridBagConstraints.BASELINE_TRAILING, GridBagConstraints.HORIZONTAL,
				DEFAULT_INSETS, 0, 0);
		add(new JLabel(translator.get(KEYS.gui().mainWindow().subtitleLabel())), c);
		c.gridy = ++yIndex;
		add(new JLabel(translator.get(KEYS.gui().mainWindow().strategyLabel())), c);
		
		yIndex = 0;
		c.gridy = yIndex;
		c.gridx = ++xIndex;
		subtitleField = new JTextField(MINIMUM_TEXT_BOX_SIZE);
		add(subtitleField, c);
		
		c.gridx = ++xIndex;
		subtitleButton = new JButton(translator.get(KEYS.gui().mainWindow().subtitleButton()));
		add(subtitleButton, c);
		
		c.gridx = --xIndex;
		c.gridy = ++yIndex;
		c.gridwidth = 2;
		strategyComboBox = new JComboBox<>(controller.getStrategiesNames().toArray(new String[0]));
		add(strategyComboBox, c);
		
		c.gridx = 0;
		c.gridy = ++yIndex;
		c.gridwidth = 3;
		overwriteCheckbox = new JCheckBox(translator.get(KEYS.gui().mainWindow().overwriteLabel()));
		add(overwriteCheckbox, c);
		
		c.gridx = 0;
		c.gridy = ++yIndex;
		c.gridwidth = 3;
		strategyParameterPanel = new JPanel();
		strategyParameterPanel.setLayout(new GridBagLayout());
		strategyParameterPanel.setBorder(BorderFactory.createTitledBorder(translator.get(KEYS.gui().mainWindow().strategyParameters())));
		localeComboBox = new JComboBox<>(controller.getAvailableLocales().toArray(new Locale[0]));
		strategyParameters = new HashMap<>();
		updateStrategyParameterPanel(null);
		add(strategyParameterPanel, c);
		
		c.gridx = 0;
		c.gridy = ++yIndex;
		c.gridwidth = 3;
		startCorrectingButton = new JButton(translator.get(KEYS.gui().mainWindow().startCorrectingButton()));
		add(startCorrectingButton, c);
	}
	
	/**
	 * Update the strategy parameter panel with the specified {@link Strategy} parameters.
	 * @param strategy
	 *        the strategy currently used, can be <code>null</code> if none is selected.
	 */
	private void updateStrategyParameterPanel (final Strategy strategy) {
		strategyParameterPanel.removeAll();
		
		int xIndex = 0;
		int yIndex = 0;
		final GridBagConstraints c = new GridBagConstraints(xIndex, yIndex, 1, 1, 0, 0,
				GridBagConstraints.BASELINE_TRAILING, GridBagConstraints.HORIZONTAL,
				DEFAULT_INSETS, 0, 0);
		
		strategyParameterPanel.add(new JLabel(translator.get(KEYS.gui().mainWindow().localeLabel())), c);
		c.gridx = ++xIndex;
		strategyParameterPanel.add(localeComboBox, c);
		
		if (strategy != null) {
			for (final Parameter<?> parameter : strategy.getParameters()) {
				c.gridx = 0;
				c.gridy = ++yIndex;
				c.gridwidth = 2;
				final String label = translator.get(parameter);
				final StrategyParameterComponent component = parameterComponentFactory.getParameterComponent(parameter, label);
				strategyParameterPanel.add(component.getComponent(), c);
				strategyParameters.put(label, component);
			}
		}
		
		strategyParameterPanel.revalidate();
	}
	
	/**
	 * Install the listeners on the components.
	 */
	private void installListeners () {
		subtitleField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped (final KeyEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run () {
						controller.changeSubtitle(Paths.get(subtitleField.getText()));
					}
				});
			}
		});
		subtitleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (final ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setMultiSelectionEnabled(false);
				final int answer = fileChooser.showOpenDialog(getFrame());
				if (answer == JFileChooser.APPROVE_OPTION) {
					controller.changeSubtitle(fileChooser.getSelectedFile().toPath());
				} else {
					if (LG.isLoggable(Level.INFO)) {
						LG.info("User canceled file selection");
					}
				}
			}
		});
		strategyComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged (final ItemEvent e) {
				controller.changeStrategy((String) e.getItem());
			}
		});
		overwriteCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (final ActionEvent e) {
				controller.changeOverwrite(overwriteCheckbox.isSelected());
			}
		});
		localeComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged (final ItemEvent e) {
				controller.changeLocale((Locale) e.getItem());
			}
		});
		startCorrectingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (final ActionEvent e) {
				controller.startCorrection();
			}
		});
	}
	
	@Override
	public void dispose () {
		if (LG.isLoggable(Level.INFO)) {
			LG.info("Subtitle corrector exiting");
		}
		super.dispose();
		controller.dispose();
	}
	
	@Override
	public void modelPropertyChange (final PropertyChangeEvent evt) {
		if (!isReady()) {
			LG.warning("Main window is not ready, cannot update view (" + evt + ")");
			return;
		}
		
		if (evt == null || evt.getPropertyName() == null) {
			LG.warning("Change event is null, or property name is null, cannot process event: "
					+ evt);
			return;
		}
		
		switch (evt.getPropertyName()) {
			case MainWindowController.SUBTITLE_PROPERTY:
				subtitleField.setText(IOUtils.getFilename((Path) evt.getNewValue()));
				break;
			case MainWindowController.STRATEGY_PROPERTY:
				final Strategy strategy = (Strategy) evt.getNewValue();
				strategyComboBox.setSelectedItem(translator.get(strategy));
				updateStrategyParameterPanel(strategy);
				pack();
				setMinimumSize(getSize());
				break;
			case MainWindowController.OVERWRITE_PROPERTY:
				overwriteCheckbox.setSelected((boolean) evt.getNewValue());
				break;
			case MainWindowController.LOCALE_PROPERTY:
				localeComboBox.setSelectedItem(evt.getNewValue());
				break;
			default:
				LG.info("Model property not handle by main window: " + evt);
				break;
		}
	}
}
