/**
 *  Galaxy Lighting Module
 *
 *  Copyright 2019 Ian Perry
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "GLM", namespace: "cosmicc", author: "Ian Perry") {
		capability "Activity Lighting Mode"
		capability "Activity Sensor"
		capability "Beacon"
		capability "Bulb"
		capability "Color"
		capability "Color Control"
		capability "Color Mode"
		capability "Color Temperature"
		capability "Configuration"
		capability "Health Check"
		capability "Illuminance Measurement"
		capability "Light"
		capability "Location Mode"
		capability "Motion Sensor"
		capability "Occupancy Sensor"
		capability "Polling"
		capability "Presence Sensor"
		capability "Refresh"
		capability "Relative Humidity Measurement"
		capability "Sensor"
		capability "Signal Strength"
		capability "Switch"
		capability "Switch Level"
		capability "Temperature Measurement"

		attribute "cpu_temp", "string"

		command "reset"
	}


	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		// TODO: define your main and details tiles here
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'lightingMode' attribute
	// TODO: handle 'activity' attribute
	// TODO: handle 'presence' attribute
	// TODO: handle 'switch' attribute
	// TODO: handle 'colorValue' attribute
	// TODO: handle 'hue' attribute
	// TODO: handle 'saturation' attribute
	// TODO: handle 'color' attribute
	// TODO: handle 'colorMode' attribute
	// TODO: handle 'colorTemperature' attribute
	// TODO: handle 'checkInterval' attribute
	// TODO: handle 'DeviceWatch-DeviceStatus' attribute
	// TODO: handle 'healthStatus' attribute
	// TODO: handle 'healthStatus' attribute
	// TODO: handle 'illuminance' attribute
	// TODO: handle 'switch' attribute
	// TODO: handle 'mode' attribute
	// TODO: handle 'motion' attribute
	// TODO: handle 'occupancy' attribute
	// TODO: handle 'presence' attribute
	// TODO: handle 'humidity' attribute
	// TODO: handle 'lqi' attribute
	// TODO: handle 'rssi' attribute
	// TODO: handle 'switch' attribute
	// TODO: handle 'level' attribute
	// TODO: handle 'temperature' attribute
	// TODO: handle 'cpu_temp' attribute

}

// handle commands
def setLightingMode() {
	log.debug "Executing 'setLightingMode'"
	// TODO: handle 'setLightingMode' command
}

def off() {
	log.debug "Executing 'off'"
	// TODO: handle 'off' command
}

def on() {
	log.debug "Executing 'on'"
	// TODO: handle 'on' command
}

def setColorValue() {
	log.debug "Executing 'setColorValue'"
	// TODO: handle 'setColorValue' command
}

def setHue() {
	log.debug "Executing 'setHue'"
	// TODO: handle 'setHue' command
}

def setSaturation() {
	log.debug "Executing 'setSaturation'"
	// TODO: handle 'setSaturation' command
}

def setColor() {
	log.debug "Executing 'setColor'"
	// TODO: handle 'setColor' command
}

def setColorTemperature() {
	log.debug "Executing 'setColorTemperature'"
	// TODO: handle 'setColorTemperature' command
}

def configure() {
	log.debug "Executing 'configure'"
	// TODO: handle 'configure' command
}

def ping() {
	log.debug "Executing 'ping'"
	// TODO: handle 'ping' command
}

def off() {
	log.debug "Executing 'off'"
	// TODO: handle 'off' command
}

def on() {
	log.debug "Executing 'on'"
	// TODO: handle 'on' command
}

def setMode() {
	log.debug "Executing 'setMode'"
	// TODO: handle 'setMode' command
}

def poll() {
	log.debug "Executing 'poll'"
	// TODO: handle 'poll' command
}

def refresh() {
	log.debug "Executing 'refresh'"
	// TODO: handle 'refresh' command
}

def on() {
	log.debug "Executing 'on'"
	// TODO: handle 'on' command
}

def off() {
	log.debug "Executing 'off'"
	// TODO: handle 'off' command
}

def setLevel() {
	log.debug "Executing 'setLevel'"
	// TODO: handle 'setLevel' command
}

def reset() {
	log.debug "Executing 'reset'"
	// TODO: handle 'reset' command
}