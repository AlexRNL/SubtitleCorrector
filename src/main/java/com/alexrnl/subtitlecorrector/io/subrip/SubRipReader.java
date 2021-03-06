package com.alexrnl.subtitlecorrector.io.subrip;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alexrnl.commons.error.ExceptionUtils;
import com.alexrnl.commons.io.IOUtils;
import com.alexrnl.subtitlecorrector.common.Subtitle;
import com.alexrnl.subtitlecorrector.common.SubtitleFile;
import com.alexrnl.subtitlecorrector.io.SubtitleReader;

/**
 * Reader for the SubRip format.<br />
 * @author Alex
 */
public class SubRipReader extends SubtitleReader {
	/** Logger */
	private static final Logger		LG	= Logger.getLogger(SubRipReader.class.getName());
	
	/** The date formatter */
	private final SimpleDateFormat	dateFormatter;
	
	/**
	 * Constructor #1.<br />
	 * Default constructor, uses UTF-8 for reading the file.
	 */
	public SubRipReader () {
		this(StandardCharsets.UTF_8);
	}

	/**
	 * Constructor #2.<br />
	 * @param charSet
	 *        the character set to use for reading the file.
	 */
	public SubRipReader (final Charset charSet) {
		super(charSet);
		dateFormatter = new SimpleDateFormat(SubRip.SUBRIP_DATE_FORMAT);
	}

	@Override
	protected Subtitle readSubtitle (final SubtitleFile subtitleFile, final BufferedReader reader)
			throws IOException {
		final long begin;
		final long end;
		final StringBuilder content = new StringBuilder();
		String currentLine;
		
		// Removing empty lines
		do {
			currentLine = IOUtils.readLine(reader).trim();
		} while (currentLine.isEmpty());
		
		// Get the subtitle number
		int subtitleIndex;
		try {
			subtitleIndex = Integer.parseInt(currentLine);
		} catch (final NumberFormatException e) {
			LG.warning("Could not parse '" + currentLine + "' as a number: " + ExceptionUtils.display(e));
			throw new IOException("Could not parse a subtitle number properly", e);
		}
		if (LG.isLoggable(Level.FINE)) {
			LG.fine("Reading subtitle#" + subtitleIndex);
		}
		
		// Get the begin and end dates of the subtitle
		currentLine = IOUtils.readLine(reader).trim();
		final String[] dates = currentLine.split(SubRip.SUBRIP_DATE_SEPARATOR);
		if (dates.length < 2) {
			throw new IOException("Could not parse " + currentLine + " as a valid line in the subrip format");
		}
		try {
			begin = dateFormatter.parse(dates[0]).getTime();
			end = dateFormatter.parse(dates[1]).getTime();
			if (LG.isLoggable(Level.FINER)) {
				LG.finer("Begin date=" + dateFormatter.format(new Date(begin)));
				LG.finer("End date=" + dateFormatter.format(new Date(end)));
			}
		} catch (final ParseException e) {
			LG.warning("Could not parse either " + dates[0] + " or " + dates[1] + " as a date");
			throw new IOException("Problem while parsing a date", e);
		}
		
		// Get the content of the subtitle
		do {
			currentLine = IOUtils.readLine(reader).trim();
			if (!currentLine.isEmpty()) {
				content.append(currentLine).append(System.lineSeparator());
			}
		} while (!currentLine.isEmpty());
		
		return new Subtitle(begin, end, content.toString());
	}
}
