package com.droid.backupphone;

import com.droid.backupphone.helper.LoginHelper;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    /**
     * Test to check password for invalid input.
     */
    @Test
    public void testValidatePasswordForInvalidCase() {
        assertFalse(LoginHelper.isPasswordValid(""));
        assertFalse(LoginHelper.isPasswordValid("a"));
        assertFalse(LoginHelper.isPasswordValid("ab"));
        assertFalse(LoginHelper.isPasswordValid("abc"));
        assertFalse(LoginHelper.isPasswordValid("abcd"));
    }

    /**
     * Test to check password for valid input.
     */
    @Test
    public void testValidatePasswordForValidCase() {
        assertTrue(LoginHelper.isPasswordValid("abcde"));
        assertTrue(LoginHelper.isPasswordValid("abcdef"));
        assertTrue(LoginHelper.isPasswordValid("@#$%^&*()"));
        assertTrue(LoginHelper.isPasswordValid("1234567"));
    }

    /**
     * Test to check email for invalid input.
     */
    @Test
    public void testValidateEmailForInvalidCase() {
        assertFalse(LoginHelper.isEmailValid(""));
        assertFalse(LoginHelper.isEmailValid("abc.g.com"));
    }

    /**
     * Test to check email for valid input.
     */
    @Test
    public void testValidateEmailForValidCase() {
        assertTrue(LoginHelper.isEmailValid("abc@gmail.com"));
        assertTrue(LoginHelper.isEmailValid("abc.efg@gmail.com"));
        assertTrue(LoginHelper.isEmailValid("@"));
        assertTrue(LoginHelper.isEmailValid("@@"));
    }
}