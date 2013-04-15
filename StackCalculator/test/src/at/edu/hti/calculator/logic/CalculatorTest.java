package at.edu.hti.calculator.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import at.edu.hti.calculator.Calculator;
import at.edu.hti.calculator.CalculatorException;
import at.edu.hti.calculator.Calculator.Operation;

public class CalculatorTest {

	@Test
	public void testSimpleAddOperation() throws Exception {

		Calculator calc = new CalculatorImpl();
		calc.push(2.0);
		calc.push(3);
		double result = calc.perform(Operation.add);

		assertEquals(5, result, 0);

	}

	//
	@Test(expected = CalculatorException.class)
	public void testPopOnEmptyStack() throws Exception {

		Calculator calc = new CalculatorImpl();
		calc.pop();

	}

	@Test
	public void testDivisionByZero() throws Exception {

		Calculator calc = new CalculatorImpl();
		try {
			calc.push(0);
			calc.push(2);
			calc.perform(Operation.div);
			fail("Exception expected");

		} catch (CalculatorException e) {
			assertEquals("Division by zero", e.getMessage());
			// e.getCause()
		}

	}
}
