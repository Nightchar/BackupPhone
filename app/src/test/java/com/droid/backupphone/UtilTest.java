package com.droid.backupphone;

import com.droid.backupphone.helper.DatabaseHelper;
import com.droid.backupphone.helper.LoginHelper;
import com.droid.backupphone.model.contact.Contact;
import com.droid.backupphone.model.contact.PhoneDetail;
import com.droid.backupphone.util.CommonUtils;
import com.droid.backupphone.util.PreferenceUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by nikhil1804 on 05-06-2017.
 */

public class UtilTest {

    @Test
    public void test1()  {
        PhoneDetail phoneDetail = mock(PhoneDetail.class);
        when(phoneDetail.getPhoneNumber()).thenReturn("1234567890");
        assertEquals(phoneDetail.getPhoneNumber(), "1234567890");
        //when(utils.isCollectionNullOrEmpty(new ArrayList<>())).thenReturn(true);

        //assertEquals(true, CommonUtils.isCollectionNullOrEmpty(new ArrayList<>()));

    }

    @Test
    public void testMoreThanOneReturnValue()  {
        Iterator<String> i= mock(Iterator.class);
        when(i.next()).thenReturn("Mockito").thenReturn("rocks");
        String result= i.next()+" "+i.next();
        //assert
        assertEquals("Mockito rocks", result);
    }

    @Test
    public void testReturnValueInDependentOnMethodParameter()  {
        Comparable<Integer> c= mock(Comparable.class);
        when(c.compareTo(anyInt())).thenReturn(-1);
        //assert
        assertEquals(-1, c.compareTo(9));

        Properties properties = mock(Properties.class);
        when(properties.get("Anddroid")).thenThrow(new IllegalArgumentException("Illeagal argument"));

        try {
            properties.get("Anddroid");
            fail("Anddroid is misspelled");
        } catch (IllegalArgumentException ex) {
            // good!

        }
    }


    @Test
    public void testLinkedListSpyWrong() {
        // Lets mock a LinkedList
        List<String> list = new LinkedList<>();
        list.add("1");
        List<String> spy = spy(list);

        // this does not work
        // real method is called so spy.get(0)
        // throws IndexOutOfBoundsException (list is still empty)
        when(spy.get(0)).thenReturn("foo");

        assertEquals("foo", spy.get(0));
    }

    @Test
    public void testLinkedListSpyCorrect() {
        // Lets mock a LinkedList
        List<String> list = new LinkedList<>();
        List<String> spy = spy(list);

        // You have to use doReturn() for stubbing
        doReturn("foo").when(spy).get(0);
        doReturn("bar").when(spy).get(1);

        String s = spy.get(0) + spy.get(1);
        assertEquals("foobar", s);
    }
}
