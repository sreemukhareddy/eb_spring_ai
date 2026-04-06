package com.eazybytes.openai.tool;

import java.time.LocalTime;
import java.time.ZoneId;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class TimeTools {

	@Tool(name = "getCurrentLocalTime", description = "Get the current time in users timezone")
	public String getCurrentLocalTime() {
		return LocalTime.now().toString();
	}
	
	@Tool(name = "getCurrentTime", description = "Get the current time in specified timezone")
	public String getCurrentLocalTime(@ToolParam(description = "value representing the time zone") String timeZone) {
		return LocalTime.now(ZoneId.of(timeZone)).toString();
	}
}
