package com.alexrnl.subtitlecorrector;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.alexrnl.subtitlecorrector.common.CommonTests;
import com.alexrnl.subtitlecorrector.correctionstrategy.CorrectionStrategyTests;
import com.alexrnl.subtitlecorrector.gui.GuiTests;
import com.alexrnl.subtitlecorrector.io.IOTests;
import com.alexrnl.subtitlecorrector.service.ServiceTests;

/**
 * All tests for the subtitle corrector software.
 * @author Alex
 */
@RunWith(Suite.class)
@SuiteClasses({ AbstractAppTest.class, CommonTests.class, CorrectionStrategyTests.class,
		GuiTests.class, IOTests.class, ServiceTests.class })
public class AllTests {
	
}
