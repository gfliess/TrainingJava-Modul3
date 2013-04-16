package at.edu.hti.calculator.parser;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import static org.mockito.Mockito.*;

import at.edu.hti.calculator.Calculator;
import at.edu.hti.calculator.Calculator.Operation;

public class ParserTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullParser() {
		new Parser(null);
	}

	@Test(expected = FileNotFoundException.class)
	public void testParserInvalidFile() throws Exception {

		Calculator cal = mock(Calculator.class);

		Parser parser = new Parser(cal);
		parser.parse(new File("invalid"));
	}

	@Test
	public void testParserTest01Xml() throws Exception {

		Calculator cal = mock(Calculator.class);

		Parser parser = new Parser(cal);
		parser.parse(new File("test/resources/test01.xml"));

//		when(cal.perform(Operation.add)).thenReturn(3.0);
		
		verify(cal).push(1.0);
		verify(cal).push(2.0);
		verify(cal).perform(Operation.add);

		verifyNoMoreInteractions(cal);
	}

}
