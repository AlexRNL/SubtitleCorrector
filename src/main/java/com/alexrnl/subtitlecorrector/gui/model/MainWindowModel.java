package com.alexrnl.subtitlecorrector.gui.model;

import java.nio.file.Path;
import java.util.Locale;

import com.alexrnl.commons.mvc.AbstractModel;
import com.alexrnl.subtitlecorrector.correctionstrategy.Strategy;
import com.alexrnl.subtitlecorrector.gui.controller.MainWindowController;

/**
 * The model for the main window.
 * @author Alex
 */
public class MainWindowModel extends AbstractModel {
	
	/** The path of the subtitle */
	private Path			subtitle;
	/** The name of the strategy to use */
	private Strategy		strategy;
	/** Flag to overwrite the file */
	private Boolean			overwrite;
	/** The language to use for the subtitle file */
	private Locale			locale;
	
	/**
	 * Constructor #1.<br />
	 */
	public MainWindowModel () {
		super();
	}

	/**
	 * Return the attribute subtitle.
	 * @return the attribute subtitle.
	 */
	public Path getSubtitle () {
		return subtitle;
	}
	
	/**
	 * Set the attribute subtitle.
	 * @param subtitle
	 *        the attribute subtitle.
	 */
	public void setSubtitle (final Path subtitle) {
		final Path oldSubtitle = this.subtitle;
		this.subtitle = subtitle;
		fireModelChange(MainWindowController.SUBTITLE_PROPERTY, oldSubtitle, subtitle);
		
	}
	
	/**
	 * Return the attribute strategy.
	 * @return the attribute strategy.
	 */
	public Strategy getStrategy () {
		return strategy;
	}
	
	/**
	 * Set the attribute strategy.
	 * @param strategy
	 *        the attribute strategy.
	 */
	public void setStrategy (final Strategy strategy) {
		final Strategy oldStrategyName = this.strategy;
		this.strategy = strategy;
		fireModelChange(MainWindowController.STRATEGY_PROPERTY, oldStrategyName, strategy);
	}
	
	/**
	 * Return the attribute overwrite.
	 * @return the attribute overwrite.
	 */
	public Boolean isOverwrite () {
		return overwrite;
	}
	
	/**
	 * Set the attribute overwrite.
	 * @param overwrite
	 *        the attribute overwrite.
	 */
	public void setOverwrite (final Boolean overwrite) {
		final Boolean oldOverwrite = this.overwrite;
		this.overwrite = overwrite;
		fireModelChange(MainWindowController.OVERWRITE_PROPERTY, oldOverwrite, overwrite);
	}
	
	/**
	 * Return the attribute locale.
	 * @return the attribute locale.
	 */
	public Locale getLocale () {
		return locale;
	}
	
	/**
	 * Set the attribute locale.
	 * @param locale the attribute locale.
	 */
	public void setLocale (final Locale locale) {
		final Locale oldLocale = this.locale;
		this.locale = locale;
		fireModelChange(MainWindowController.LOCALE_PROPERTY, oldLocale, locale);
	}
	
}
