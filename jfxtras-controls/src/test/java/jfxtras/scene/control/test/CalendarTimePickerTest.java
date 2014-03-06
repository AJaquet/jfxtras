/**
 * CalendarPickerTest.java
 *
 * Copyright (c) 2011-2014, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.scene.control.test;

import java.util.GregorianCalendar;
import java.util.Locale;

import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jfxtras.scene.control.CalendarTimePicker;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;
import jfxtras.util.PlatformUtil;

import org.junit.Assert;
import org.junit.Test;


/**
 * Created by Tom Eugelink on 26-12-13.
 */
public class CalendarTimePickerTest extends JFXtrasGuiTest {

	/**
	 * 
	 */
	public Parent getRootNode()
	{
		Locale.setDefault(Locale.ENGLISH);
		
		VBox box = new VBox();

		calendarTimePicker = new CalendarTimePicker();
		box.getChildren().add(calendarTimePicker);

		// make sure there is enough room for the time sliders
		box.setPrefSize(300, 300);
		return box;
	}
	private CalendarTimePicker calendarTimePicker = null;

	/**
	 * 
	 */
	@Test
	public void defaultControl()
	{
		// default value is not null
		Assert.assertNotNull(calendarTimePicker.getCalendar());
	}

	/**
	 * 
	 */
	@Test
	public void locale()
	{
		Text lLabelText = (Text)find(".timeLabel");
		
		// set time to 12:30:00
		PlatformUtil.runAndWait( () -> {
			calendarTimePicker.setCalendar(new GregorianCalendar(2013, 0, 1, 20, 30, 00));			
		});
		
		// assert label
		Assert.assertEquals("8:30 PM", lLabelText.getText());
		
		// change locale
		PlatformUtil.runAndWait( () -> {
			calendarTimePicker.setLocale(Locale.GERMAN);			
		});
		
		// assert label
		Assert.assertEquals("20:30", lLabelText.getText());
	}
	
	/**
	 * 
	 */
	@Test
	public void slide()
	{
		// set time to 12:30:00
		PlatformUtil.runAndWait( () -> {
			calendarTimePicker.setCalendar(new GregorianCalendar(2013, 0, 1, 12, 30, 00));			
		});
		
		// move the hour slider
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(100,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("20:30:00", TestUtil.quickFormatCalendarAsTime(calendarTimePicker.getCalendar()));
		
		// move the minute slider
		move("#minuteSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-50,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("20:19:00", TestUtil.quickFormatCalendarAsTime(calendarTimePicker.getCalendar()));
	}
	
	/**
	 * 
	 */
	@Test
	public void slideStep15()
	{
		// set time to 12:30:00
		PlatformUtil.runAndWait( () -> {
			calendarTimePicker.setCalendar(new GregorianCalendar(2013, 0, 1, 12, 30, 00));
			calendarTimePicker.setMinuteStep(15);
		});
		
		// move the hour slider
		move("#hourSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(100,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("20:30:00", TestUtil.quickFormatCalendarAsTime(calendarTimePicker.getCalendar()));
		
		// move the minute slider
		move("#minuteSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-50,0);		
		release(MouseButton.PRIMARY);
		Assert.assertEquals("20:15:00", TestUtil.quickFormatCalendarAsTime(calendarTimePicker.getCalendar()));
	}
}
